import java.util.*;

public class Row<T>
{
	private HashMap<String, T> rowValues;
	private int nColumns;
	
	public Row(int c)
	{
		nColumns = c;
		rowValues = new HashMap<String, T>(nColumns);
	}

	public HashMap<String, T> getRowValues() 
	{
		return rowValues;
	}

	public void setRowValues(HashMap<String, T> rowValues) 
	{
		this.rowValues = rowValues;
	}
	
	public void insert(String columnName, T value)
	{
		rowValues.put(columnName, value);
	}
	
	public T getFromRow(String columnName)
	{
		return rowValues.get(columnName);
	}
	
	public String toString()
	{
		Iterator<String> iterator = rowValues.keySet().iterator();
		String s = "Row Info ";
		while(iterator.hasNext())
		{
			s += rowValues.get(iterator.next()) + "   | ";
		}
		
		return s;
	}
}
