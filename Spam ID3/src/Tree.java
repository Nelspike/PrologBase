import java.util.*;

public class Tree 
{
	private ArrayList<Edge> edges;
	private ArrayList<Node> nodes;
	private int nEdges;
	private int nNodes;
	private Node startingNode;
	
	public Tree()
	{
		edges = new ArrayList<Edge>();
		nodes = new ArrayList<Node>();
	}

	public void addNode(Node node)
	{
		nodes.add(node);
		nNodes++;
	}
	
	public void addEdge(Edge edge)
	{
		edges.add(edge);
		nEdges++;
	}

	public void removeNode(Node node)
	{
		nodes.remove(node);
		nNodes--;
	}
	
	public void removeEdge(Edge edge)
	{
		edges.remove(edge);
		nEdges--;
	}
	
	public ArrayList<Edge> getEdges() 
	{
		return edges;
	}

	public void setEdges(ArrayList<Edge> edges) 
	{
		this.edges = edges;
	}

	public int getnEdges() 
	{
		return nEdges;
	}

	public void setnEdges(int nEdges) 
	{
		this.nEdges = nEdges;
	}

	public ArrayList<Node> getNodes() 
	{
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) 
	{
		this.nodes = nodes;
	}

	public int getnNodes() 
	{
		return nNodes;
	}

	public void setnNodes(int nNodes) 
	{
		this.nNodes = nNodes;
	}

	public Node getStartingNode() 
	{
		return startingNode;
	}

	public void setStartingNode(Node startingNode) 
	{
		this.startingNode = startingNode;
	}
	
	public String toString()
	{
		String tree = "";
		
		tree += startingNode.toString();
		
		return tree;
	}
	
}
