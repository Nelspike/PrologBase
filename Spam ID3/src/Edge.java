
public class Edge
{
	private float edgeValue;
	private Node startNode;
	private Node endNode;
	
	public Edge()
	{
		
	}

	public float getEdgeValue() 
	{
		return edgeValue;
	}

	public void setEdgeValue(float edgeValue) 
	{
		this.edgeValue = edgeValue;
	}

	public Node getEndNode() {
		return endNode;
	}

	public void setEndNode(Node endNode) {
		this.endNode = endNode;
	}

	public Node getStartNode() {
		return startNode;
	}

	public void setStartNode(Node startNode) {
		this.startNode = startNode;
	}
	
	public String toString()
	{
		String edge = "";
		
		edge += edgeValue + "|";
		edge += "->" + endNode.toString();
		
		return edge;
	}
	
}
