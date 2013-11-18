import java.util.*;

public class Table 
{
	private HashMap<String, ArrayList<Float>> columnInfo;
	private ArrayList<Row<Float>> rowInfo;
	private int capacity;
	private int nColumns;
	
	public Table(int c)
	{
		capacity = c;
		rowInfo = new ArrayList<Row<Float>>();
		columnInfo = new HashMap<String, ArrayList<Float>>(capacity);
	}
	
	public ArrayList<Float> getColumnInformation(String column)
	{
		return columnInfo.get(column);
	}

	public ArrayList<Row<Float>> getRowsWithValue(float value, String column)
	{
		ArrayList<Row<Float>> ret = new ArrayList<Row<Float>>();
		
		for(int i = 0; i < rowInfo.size(); i++)
		{
			if(rowInfo.get(i).getFromRow(column) == value)
				ret.add(rowInfo.get(i));
		}
		
		return ret;
	}
	
	public ArrayList<Row<Float>> getRowsForInterval(float value, String column)
	{
		ArrayList<Row<Float>> ret = new ArrayList<Row<Float>>();
		
		for(int i = 0; i < rowInfo.size(); i++)
		{
			if(rowInfo.get(i).getFromRow(column) <= value-10.0 && rowInfo.get(i).getFromRow(column) > value)
				ret.add(rowInfo.get(i));
		}
		
		return ret;
	}
	
	public void addRow(Row<Float> row)
	{
		rowInfo.add(row);
        Iterator<String> iterator = columnInfo.keySet().iterator();
        while(iterator.hasNext())
        {        
            String col = iterator.next();
            ArrayList<Float> array = columnInfo.get(col);
            array.add(row.getFromRow(col));
            columnInfo.put(col, array);
        }
        
        capacity = rowInfo.size();
	}
	
	public void insertInRow(int nRow, String column, Float value)
	{     
		rowInfo.get(nRow).insert(column, value);
	}
	
	public void fillColumnWithInfo(String column, ArrayList<Float> info)
	{
		columnInfo.put(column, info);
	}
	
	public void fillTable(ArrayList<String> columns, ArrayList<ArrayList<Float>> info)
	{
		nColumns = columns.size();
		for(int i = 0; i < nColumns	; i++)
			fillColumnWithInfo(columns.get(i), info.get(i));

		fillRowsWithInfo();
	}
	
	private void fillRowsWithInfo()
	{
        Iterator<String> iterator = columnInfo.keySet().iterator();
        fillRowsWithNullInfo(columnInfo.get("Spam").size());
        while(iterator.hasNext())
        {        
            String col = iterator.next();
            ArrayList<Float> valArray = columnInfo.get(col);
            int nRows = valArray.size();
            for(int i = 0; i < nRows; i++)
            {
            	if(rowInfo.get(i) == null)
            	{
            		Row<Float> r = new Row<Float>(150);
            		r.insert(col, valArray.get(i));
            		rowInfo.set(i, r);
            	}
            	else
            	{
            		Row<Float> r = rowInfo.get(i);
            		r.insert(col, valArray.get(i));  
            		rowInfo.set(i, r);
            	}
            }
        }
	}
	
	private void fillRowsWithNullInfo(int nRows)
	{
		for(int i = 0; i < nRows; i++)
			rowInfo.add(null);
	}
	
	//Getters and setters
	
	public HashMap<String, ArrayList<Float>> getColumnInfo() 
	{
		return columnInfo;
	}

	public void setColumnInfo(HashMap<String, ArrayList<Float>> columnInfo)
	{
		this.columnInfo = columnInfo;
	}
	
	public int getCapacity()
	{
		return capacity;
	}
	
	public void setCapacity(int c)
	{
		capacity = c;
	}

	public ArrayList<Row<Float>> getRowInfo() {
		return rowInfo;
	}

	public void setRowInfo(ArrayList<Row<Float>> rowInfo) 
	{
		this.rowInfo = rowInfo;
	}
	
	public boolean searchInRows(ArrayList<Pair> values)
	{
		int actualRow = -1;
		int numKeys = 0;
		for(int i = 0; i < rowInfo.size(); i++)
		{
			int counter = 0;
			Row<Float> rowValues = rowInfo.get(i);
			numKeys = rowValues.getRowValues().size();
			for(int j = 0; j < values.size(); j++)
			{
				String column = values.get(j).getSecond();
				float value = (float) values.get(j).getFirst();
				if(rowValues.getFromRow(column) == value)
				{
					actualRow =  i;
					counter++;
				}
				if(counter == numKeys) break;
			}
		}
		
		if(actualRow == -1) return false;
		
		if(rowInfo.get(actualRow).getFromRow("Spam") == 1.0) return true;
		
		return false;
	}
	
	public float getValue(int nRow, String column)
	{
		Row<Float> r = rowInfo.get(nRow);
		return r.getFromRow(column);
	}

	public int numCasesWithSuccess(float value, String column)
	{
		ArrayList<Float> col = columnInfo.get(column);
		ArrayList<Float> colS = columnInfo.get("Spam");
		int numCases = 0;
		
		for(int i = 0; i < col.size(); i++)
		{
			if(col.get(i) == value && colS.get(i) == 1.0)
				numCases++;
		}
		
		return numCases;
	}
	
	public int numCases(float value, String column)
	{
		ArrayList<Float> col = columnInfo.get(column);
		int numCases = 0;
		
		for(int i = 0; i < col.size(); i++)
		{
			if(col.get(i) == value)
				numCases++;
		}
		
		return numCases;
	}
	
	public ArrayList<Float> getValuesInColumn(String column, float first, float value)
	{
		ArrayList<Float> ret = new ArrayList<Float>();
		ArrayList<Float> list = columnInfo.get(column);
		ArrayList<Float> pruned = new ArrayList<Float>();
		
		for(int i = 0; i < list.size(); i++)
		{
			if(list.get(i) >= first && list.get(i) < value)
				pruned.add(list.get(i));
		}
		
		HashSet<Float> hs = new HashSet<Float>();
		hs.addAll(pruned);
		ret.clear();
		ret.addAll(hs);
		
		return ret;
	}
	
	public ArrayList<Row<Float>> getSpectificRows(Pair pair)
	{
		ArrayList<Row<Float>> ret = new ArrayList<Row<Float>>();
		String column = pair.getSecond();
		float value = (float) pair.getFirst();
		
		for(int i = 0; i < rowInfo.size(); i++)
		{
			Row<Float> row = rowInfo.get(i);
			if(row.getFromRow(column) >= value-10.0 && row.getFromRow(column) < value) ret.add(row);
		}
		
		return ret;
	}
}
