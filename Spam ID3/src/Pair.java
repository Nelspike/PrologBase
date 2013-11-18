
public class Pair implements Comparable<Pair>
{
	private double first;
	private String second;
	
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	public double getFirst() {
		return first;
	}
	public void setFirst(double first) {
		this.first = first;
	}
	
	@Override
	public int compareTo(Pair o) 
	{
		if(first < o.getFirst()) return -1;
		else if(first == o.getFirst()) return 0;
		
		return 1;
	}
	
	public String toString()
	{
		return first + " " + second;
	}
}
