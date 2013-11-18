import java.util.*;

public class MailClassifier
{
	private ID3 internalID3;
	private Tree classifierTree;
	private ArrayList<Row<Float>> mails;
	
	public MailClassifier(ID3 id3, ArrayList<Row<Float>> m)
	{
		setInternalID3(id3);
		classifierTree = id3.getInternalTree();
		cleanTree(classifierTree.getStartingNode());
		mails = m;
	}
	
	public float classifySingleMail(int pos)
	{
		Row<Float> row = mails.get(pos);
		Node initNode = classifierTree.getStartingNode();
		boolean result = verifyTree(initNode, row);
		
		if(result) return (float)1.0;
		
		return (float)0.0;
	}
	
	public void classifyMails()
	{
		for(int i = 0; i < mails.size(); i++)
		{
			float resultFromTree = classifySingleMail(i);
			float resultFromMail = mails.get(i).getFromRow("Spam");
			System.out.print("\nE-mail no." + i + " has been considered: ");
			if(resultFromTree == 1.0) System.out.println("Spam");
			else System.out.println("Non Spam");
			
			System.out.print("Expected output: ");
			if(resultFromMail == 1.0) System.out.println("Spam");
			else System.out.println("Non Spam");
		}
		System.out.println("\n\n");
	}
	
	private boolean verifyTree(Node node, Row<Float> row)
	{
		if(node.isFinalNode())
		{
			return node.getNodeValue();
		}
		
		String split = node.getSplit();
		float valueInRow = row.getFromRow(split);

		ArrayList<Edge> exitEdges = node.getExitEdges();

		for(int i = 0; i < exitEdges.size(); i++)
		{
			Edge edge = exitEdges.get(i);
			float valueFromEdge = edge.getEdgeValue();

			if(split.equals("Capital Total") || 
					split.equals("Capital Average") || 
					split.equals("Capital Longest"))
			{
				if(!(valueFromEdge < valueInRow)) verifyTree(edge.getEndNode(), row);
			}
			else
			{
				if(valueFromEdge > valueInRow) return verifyTree(edge.getEndNode(), row);
			}
		}
			
		return node.getNodeValue();
	}
	
	private void cleanTree(Node node)
	{
		if(node.isFinalNode()) return;
		
		String split = node.getSplit();
		int index = split.indexOf('§');
		split = split.substring(0, index);
		node.setSplit(split);
		
		ArrayList<Edge> edges = node.getExitEdges();
		
		for(int i = 0; i < edges.size(); i++)
			cleanTree(edges.get(i).getEndNode());
	}

	//Getters and Setters
	
	public Tree getClassifierTree() {
		return classifierTree;
	}

	public void setClassifierTree(Tree classifierTree) {
		this.classifierTree = classifierTree;
	}

	public ArrayList<Row<Float>> getMails() {
		return mails;
	}

	public void setMails(ArrayList<Row<Float>> mails) {
		this.mails = mails;
	}

	public ID3 getInternalID3() {
		return internalID3;
	}

	public void setInternalID3(ID3 internalID3) {
		this.internalID3 = internalID3;
	}
}
