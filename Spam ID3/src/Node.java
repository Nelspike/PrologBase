import java.util.*;

public class Node
{
	private ArrayList<Edge> exitEdges;
	private boolean nodeValue;
	private int depthLevel;
	private boolean finalNode;
	private String split;

	public Node()
	{
		exitEdges = new ArrayList<Edge>();
	}

	public ArrayList<Edge> getExitEdges() 
	{
		return exitEdges;
	}

	public void setExitEdges(ArrayList<Edge> exitEdges)
	{
		this.exitEdges = exitEdges;
	}

	public void addExitEdge(Edge e)
	{
		exitEdges.add(e);
	}
	
	public boolean getNodeValue() 
	{
		return nodeValue;
	}

	public void setNodeValue(boolean nodeValue)
	{
		this.nodeValue = nodeValue;
	}

	public int getDepthLevel() 
	{
		return depthLevel;
	}

	public void setDepthLevel(int depthLevel) 
	{
		this.depthLevel = depthLevel;
	}

	public boolean isFinalNode() 
	{
		return finalNode;
	}

	public void setFinalNode(boolean finalNode) 
	{
		this.finalNode = finalNode;
	}

	public String getSplit() {
		return split;
	}

	public void setSplit(String split) {
		this.split = split;
	}
	
	public String toString()
	{
		String node = "";
		node += '\n';
		if(split!=null) node += split + " {";
		else node += nodeValue + " {";
		
		for(int i = 0; i < exitEdges.size(); i++)
		{
			node += exitEdges.get(i).toString();
		}
		
		node += '\n' + "}";
		
		return node;
	}
	
}
