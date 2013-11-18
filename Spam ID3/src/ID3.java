import java.io.*;
import java.util.*;

public class ID3 extends Algorithm
{
	private Tree internalTree;
	private Table table;
	private double generalEntropy;
	private ArrayList<String> wordsNChars;
	private HashMap<String, ArrayList<Float>> casesPerColumn;
	private HashMap<String, Float> oddIntervals;
	private HashMap<String, Integer> wordNames;
	private int finalNodeCounter;
	
	public ID3(Table t, ArrayList<String> wnc)
	{
		table = t;
		wordsNChars = wnc;
		wordsNChars.add("Capital Average");
		wordsNChars.add("Capital Total");
		wordsNChars.add("Capital Longest");
		internalTree = new Tree();
		casesPerColumn = new HashMap<String, ArrayList<Float>>(wordsNChars.size());
		oddIntervals = new HashMap<String, Float>(3);
		finalNodeCounter = 1;
		wordNames = new HashMap<String, Integer>(60);
	}
	
	public void applyID3()
	{
		calculateGeneralEntropy();
		internalTree = new Tree();
		float[] percentages = {(float)0.01, (float)0.02, (float)0.03,
				(float)0.04, (float)0.05, (float)0.06, 
				(float)0.07, (float)0.08, (float)0.09,
				(float)0.1, (float)0.15, (float)0.2, 
				(float)0.25, (float)0.3, (float)0.35,
				(float)0.4, (float)0.45, (float)0.5,
				(float)0.6, (float)0.7, (float)0.8, 
				(float)0.9, (float)1.0, (float)1.5, 
				(float)2.0, (float)3.0, (float)4.0,
				(float)5.0, (float)10.0, (float)100.0};
		
		ArrayList<Row<Float>> rows = table.getRowInfo();
		Pair bestGain = getBestGain(rows, percentages, wordsNChars);
		ArrayList<String> wordsCopy = new ArrayList<String>();
		for(int i = 0; i < wordsNChars.size(); i++)
			wordsCopy.add(null);
		Collections.copy(wordsCopy, wordsNChars);
		internalTree.setStartingNode(constructTree(bestGain, rows, wordsCopy, percentages));
		
		raiseTree();
		replaceTree();
		generateDot();
	}
	
	private void raiseTree()
	{
		internalTree.setStartingNode(subtreeRaising(internalTree.getStartingNode()));
	}
	
	private void replaceTree()
	{
		internalTree.setStartingNode(subtreeSimpleReplacement(internalTree.getStartingNode()));
		internalTree.setStartingNode(subtreeRaising(internalTree.getStartingNode()));
	}
	
	private Node constructTree(Pair bestGain, ArrayList<Row<Float>> splitTable, ArrayList<String> words, float[] percentages)
	{
		if(splitTable.size() == 1 || bestGain == null)
		{
			Node finalNode = new Node();
			finalNode.setFinalNode(true);
			if(splitTable.get(0).getFromRow("Spam") == 1.0) finalNode.setNodeValue(true);
			else finalNode.setNodeValue(false);
			finalNode.setSplit("FinalNode"+finalNodeCounter);
			finalNodeCounter++;
			internalTree.addNode(finalNode);
			return finalNode;
		}
		
		Node node = new Node();
		node.setFinalNode(false);
		String split = bestGain.getSecond();
		
		int counter = 0;
		if(wordNames.containsKey(split)) counter = wordNames.get(split);
		
		node.setSplit(split+'§'+counter);
		counter++;
		wordNames.put(split, counter);
		
		words.remove(split);
		ArrayList<Float> splits = casesPerColumn.get(split);
		
		//Cicle for Edges
		for(int i = 0; i < splits.size(); i++)
		{
			Edge edge = new Edge();
			edge.setStartNode(node);
			edge.setEdgeValue(splits.get(i));			
			ArrayList<Row<Float>> specTable = getSplitTable(splitTable, splits.get(i), split, percentages);			
			Pair newGain = getBestGain(specTable, percentages, words);
			
			ArrayList<String> newWords = new ArrayList<String>(words.size());
			for(int j = 0; j < words.size(); j++)
				newWords.add(null);
			Collections.copy(newWords, words);
			
			edge.setEndNode(constructTree(newGain, specTable, newWords, percentages));
			internalTree.addEdge(edge);
			node.addExitEdge(edge);
		}
		
		internalTree.addNode(node);
		return node;
	}

	//Sort Gains
	
	private Pair getBestGain(ArrayList<Row<Float>> splitTable, float[] percentages, ArrayList<String> words)
	{
		ArrayList<Pair> gains = new ArrayList<Pair>();
			
		for(int i = 0; i < words.size(); i++)
		{
			double gain = calculateGain(words.get(i), splitTable, percentages);
			Pair p = new Pair();
			p.setFirst(gain);
			p.setSecond(words.get(i));
			gains.add(p);
		}
		
		Collections.sort(gains);

		if(gains.size() == 0) return null;
		return gains.get(gains.size()-1);
	}
	
	//Enf of Sort Gains
	
	private ArrayList<Row<Float>> getSplitTable(ArrayList<Row<Float>> sTable, float value, String split, float[] percentages)
	{
		ArrayList<Row<Float>> ret = new ArrayList<Row<Float>>();
		
		//oddIntervals
		
		for(int i = 0; i < sTable.size(); i++)
		{
			Row<Float> row = sTable.get(i);
			float inTable = row.getFromRow(split);
			
			if(split.equals("Capital Total") || 
					split.equals("Capital Average") || 
					split.equals("Capital Longest"))
			{
				float interval = oddIntervals.get(split);
				if(inTable <= value && inTable >= value-interval)
					ret.add(row);
			}
			else
			{
				int index = 0;
				for(int j = 0; j < percentages.length; j++)
				{
					if(percentages[j] == value)
					{
						index = j;
						break;
					}
				}
				
				if(value != (float)100.0)
				{
					if(index == 0)
					{
						if(inTable < value && inTable >= 0.0)
							ret.add(row);
					}
					else
					{
						if(inTable < value && inTable >= percentages[index-1])
							ret.add(row);
					}
				}
				else
				{
					if(inTable <= value && inTable >= percentages[index-1])
						ret.add(row);			
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Calculates the general Entropy for the table.
	 */
	private void calculateGeneralEntropy()
	{
		ArrayList<? extends Object> arr = table.getColumnInformation("Spam");
		int totalCases = arr.size();
		int positiveCases = 0;
		int negativeCases = 0;
		Class<Float> intern = Float.class;
		if(arr.get(0).getClass().equals(intern))
		{
			for(int i = 0; i < arr.size(); i++)
			{
				if(intern.cast(arr.get(i)) == ((float)1.0) ) positiveCases++;
				else negativeCases++;
			}
		}
		double posCases = (double)positiveCases/(double)totalCases;
		double negCases = (double)negativeCases/(double)totalCases;
		setGeneralEntropy(calcEntropy(posCases, negCases));
	}
	
	/**
	 * Calculates the Entropy for any table.
	 * @param splitTable The table to have its entropy calculated.
	 * @return The entropy.
	 */
	private double calculateTableEntropy(ArrayList<Row<Float>> splitTable)
	{
		int totalCases = splitTable.size();
		int positiveCases = 0;
		int negativeCases = 0;
		
		for(int i = 0; i < splitTable.size(); i++)
		{
			if(splitTable.get(i).getFromRow("Spam") == 1.0)
				positiveCases++;
			else
				negativeCases++;
		}
		
		double posCases = (double)positiveCases/(double)totalCases;
		double negCases = (double)negativeCases/(double)totalCases;
		return calcEntropy(posCases, negCases);
	}
	
	/**
	 * Calculates the gain per column in the given table.
	 * @param column The column to have its gain calculated.
	 * @param splitTable The table that will be used.
	 * @return The gain.
	 */
	private double calculateGain(String column, ArrayList<Row<Float>> splitTable, float[] percentages)
	{
		double tableEntropy = calculateTableEntropy(splitTable);
		double childrenEntropies = 0.0;
		ArrayList<Float> cases = new ArrayList<Float>();
		
		if(column.equals("Capital Total") || column.equals("Capital Average") || column.equals("Capital Longest"))
		{
			float[] values = new float[11];
			ArrayList<Float> valuesInTable = table.getColumnInformation(column);

			Collections.sort(valuesInTable);
			
			float max = valuesInTable.get(valuesInTable.size()-1);
			float min = valuesInTable.get(0);
			float interval = (max-min)/(float)10.0;
			
			
			oddIntervals.put(column, interval);
			
			for(int i = 0; i < 11; i++)
			{
				values[i] = min;
				min+=interval;
			}
			
			for(int i = 0; i < values.length; i++)
			{
				if(i == 0)
				{
					double childEntropy = childEntropy((float)0.0, values[i], splitTable, column, cases);
					if(childEntropy == -1.0) continue;
					else childrenEntropies += childEntropy;
				}
				else
				{
					double childEntropy = childEntropy(values[i-1], values[i], splitTable, column, cases);
					if(childEntropy == -1.0) continue;
					else childrenEntropies += childEntropy;
				}
			}
		}
		else
		{
		
			for(int i = 0; i < percentages.length; i++)
			{
				if(i == 0)
				{
					double childEntropy = childEntropy((float)0.0, percentages[i], splitTable, column, cases);
					if(childEntropy == -1.0) continue;
					else childrenEntropies += childEntropy;
				}
				else
				{
					double childEntropy = childEntropy(percentages[i-1], percentages[i], splitTable, column, cases);
					if(childEntropy == -1.0) continue;
					else childrenEntropies += childEntropy;
				}
			}
			
		}

		casesPerColumn.put(column, cases);
		return tableEntropy-childrenEntropies;
	}
	
	/**
	 * Calculates the Entropy for a case in the table.
	 * @param firstValue The first value of the case.
	 * @param secondValue The second value of the case.
	 * @param splitTable The table to be used to calculate.
	 * @param column The column to be addressed.
	 * @param cases The ArrayList with all the cases that had a matching positive number of cases.
	 * @return The entropy for that case.
	 */
	private double childEntropy(float firstValue, float secondValue, ArrayList<Row<Float>> splitTable, String column, ArrayList<Float> cases)
	{
		int nCases = 0;
		int positiveCases = 0;
		int negativeCases = 0;
		int totalCases = splitTable.size();
		
		for(int i = 0; i < totalCases; i++)
		{
			Row<Float> row = splitTable.get(i);
			if(secondValue != (float)100.0)
			{
				if(row.getFromRow(column) >= firstValue &&
						row.getFromRow(column) < secondValue)
				{
					nCases++;
					
					if(row.getFromRow("Spam") == 1.0)
						positiveCases++;
					else
						negativeCases++;
				}
			}
			else
			{
				if(row.getFromRow(column) >= firstValue &&
						row.getFromRow(column) <= secondValue)
				{
					nCases++;
					
					if(row.getFromRow("Spam") == 1.0)
						positiveCases++;
					else
						negativeCases++;
				}
			}
		}
		

		if(nCases == 0)
		{
			return -1.0;
		}
		
		cases.add(secondValue);
		double posCases = (double)positiveCases/(double)nCases;
		double negCases = (double)negativeCases/(double)nCases;
		double entropy =  calcEntropy(posCases, negCases);
		double aux = ((double)nCases)/((double)totalCases);
		return entropy*aux;
	}
	
	public void addRow(Row<Float> row)
	{
		table.addRow(row);
		System.out.println("Size - " + table.getCapacity());
	}
	
	public void generateDot()
	{
		try {
			DotGenerator dot = new DotGenerator(internalTree, "ID3.dot", "ID3");
			dot.generateDot();
		} catch (IOException e) {
			System.out.println("Can't Generate Dot!");
			e.printStackTrace();
		}
		
		String[] cmdarray = {"dot/dot.exe",  "-Tpng",  "ID3.dot", "-o", "ID3.png"};
		
		try {
			Runtime.getRuntime().exec(cmdarray);
		} catch (IOException e) {
			System.out.println("Cannot execute DOT generation command!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Raises all the subtrees of the main tree that have
	 * one edge each, and do not have a child that is a
	 * Final Node.
	 * @param node
	 * @return The node in question.
	 */
	public Node subtreeRaising(Node node)
	{
		ArrayList<Edge> edges = node.getExitEdges();

		if(edges.size() == 1)
		{
			Edge e = edges.get(0);
			Node nextNode = e.getEndNode();
			if(nextNode.getExitEdges().size() > 1)
			{
				ArrayList<Edge> edgesIn = nextNode.getExitEdges();
				for(int i = 0; i < edgesIn.size(); i++)
				{
					Edge edge = edgesIn.get(i);
					Node afterNode = edge.getEndNode();
					edge.setEndNode(subtreeRaising(afterNode));

					if(!afterNode.isFinalNode() && afterNode != edge.getEndNode())
					{
						edgesIn.remove(i);
						edgesIn.add(i, edge);
						ArrayList<Edge> innerEdges = afterNode.getExitEdges();
						for(int j = 0; j < innerEdges.size(); j++)
							internalTree.removeEdge(innerEdges.get(j));
						
						internalTree.removeNode(afterNode);
					}
				}
				nextNode.setExitEdges(edgesIn);
				return nextNode;
			}

			if(!nextNode.isFinalNode())
			{
				while(!nextNode.isFinalNode() && nextNode.getExitEdges().size() == 1)
				{
					e.setEndNode(subtreeRaising(nextNode));
					edges.remove(0);
					edges.add(e);
					internalTree.removeEdge(nextNode.getExitEdges().get(0));
					internalTree.removeNode(nextNode);
					node.setExitEdges(edges);
					nextNode = e.getEndNode();
				}
				
				if(!nextNode.isFinalNode())
				{
					ArrayList<Edge> edgesAfterwards = nextNode.getExitEdges();
					for(int i = 0; i < edgesAfterwards.size(); i++)
					{
						Edge ed = edgesAfterwards.get(i);
						Node afterwardsNode = ed.getEndNode();

						ArrayList<Edge> innerEdges = afterwardsNode.getExitEdges();
						
						for(int x = 0; x < innerEdges.size(); x++)
						{
							Edge edge = innerEdges.get(x);
							Node nodus = edge.getEndNode();
							edge.setEndNode(subtreeRaising(nodus));
							innerEdges.remove(x);
							innerEdges.add(x, edge);
							afterwardsNode.setExitEdges(innerEdges);
						}
					}
				}
				else
				{
					return nextNode;
				}

				return node;
			}
			else
			{
				internalTree.removeEdge(e);
				internalTree.removeNode(node);
				return nextNode;
			}
		}
		else
		{
			for(int i = 0; i < edges.size(); i++)
			{
				Edge e = edges.get(i);
				Node nextNode = e.getEndNode();
				e.setEndNode(subtreeRaising(nextNode));

				if(!nextNode.isFinalNode() && nextNode != e.getEndNode())
				{
					edges.remove(i);
					edges.add(i, e);
					ArrayList<Edge> innerEdges = nextNode.getExitEdges();
					for(int j = 0; j < innerEdges.size(); j++)
						internalTree.removeEdge(innerEdges.get(j));
					
					internalTree.removeNode(nextNode);
				}
			}
			node.setExitEdges(edges);
		}
		
		return node;
	}
	
	//Subtree Replacement
	
	public Node subtreeSimpleReplacement(Node node)
	{
		ArrayList<Edge> edges = node.getExitEdges();
		ArrayList<Edge> resultsFromNode = new ArrayList<Edge>();
		
		for(int i = 0; i < edges.size(); i++)
		{
			Edge e = edges.get(i);
			Node nextNode = e.getEndNode();
			
			if(nextNode.isFinalNode())
				resultsFromNode.add(e);
			else
				subtreeSimpleReplacement(nextNode);
		}
		
		boolean firstValue = true;

		for(int i = 0; i < resultsFromNode.size(); i++)
		{
			if(i == 0) firstValue = resultsFromNode.get(i).getEndNode().getNodeValue();
			else
			{
				boolean secondValue = resultsFromNode.get(i).getEndNode().getNodeValue();
				if(firstValue == secondValue)
				{
					//System.out.println("here");
					Edge toRemove = resultsFromNode.get(i-1);
					internalTree.removeEdge(toRemove);
					removeSubtree(toRemove.getEndNode());
					firstValue = secondValue;
				}
			}
		}
		
		return node;
	}
	
	//remove
	public void removeSubtree(Node toRemove)
	{
		if(toRemove.isFinalNode()) internalTree.removeNode(toRemove);
		
		ArrayList<Edge> edges = toRemove.getExitEdges();
		
		for(int i = 0; i < edges.size(); i++)
		{
			Edge e = edges.get(i);
			Node next = e.getEndNode();
			internalTree.removeEdge(e);
			internalTree.removeNode(toRemove);
			removeSubtree(next);
		}
	}

	//getters and Setters
	
	public Tree getInternalTree() {
		return internalTree;
	}
	public void setInternalTree(Tree internalTree) {
		this.internalTree = internalTree;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}

	public double getGeneralEntropy() {
		return generalEntropy;
	}

	public void setGeneralEntropy(double generalEntropy) {
		this.generalEntropy = generalEntropy;
	}

	public ArrayList<String> getWordsNChars() {
		return wordsNChars;
	}

	public void setWordsNChars(ArrayList<String> wordsNChars) {
		this.wordsNChars = wordsNChars;
	}
}
