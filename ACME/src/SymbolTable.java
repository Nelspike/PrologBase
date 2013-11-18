import java.util.*;

public class SymbolTable {
	
	private HashMap<String, String> Symbols;
	private ArrayList<String> errorList;
	
	public SymbolTable(int capacity)
	{
		setStrings(new HashMap<String, String>(capacity));
		setErrorList(new ArrayList<String>());
	}
	
	public SymbolTable(HashMap<String, String> t)
	{
		setStrings(new HashMap<String, String>(t));
		setErrorList(new ArrayList<String>());
	}
	
	public void printHashMap()
	{
        System.out.println(Symbols);
	}
	
	public String addSymbol(String name, String s)
	{
		if(!Symbols.containsKey(name)) Symbols.put(name, s);
		else
		{
			 return "Variable " + name + " has already been declared!";
		}
		
		return "";
	}
	
	public boolean existsInTable(String name)
	{
		return Symbols.containsKey(name);
	}
	
	public void addError(String error)
	{
		errorList.add(error);
	}
	
	public String getString(String name)
	{
		return Symbols.get(name);
	}

	public boolean StringExists(String name)
	{
		return Symbols.containsKey(name);
	}
	
	public void setStrings(HashMap<String, String> Strings) {
		this.Symbols = Strings;
	}

	public HashMap<String, String> getStrings() {
		return Symbols;
	}

	public ArrayList<String> getErrorList() {
		return errorList;
	}

	public void setErrorList(ArrayList<String> errorList) {
		this.errorList = errorList;
	}
	
	public int size()
	{
		return Symbols.size();
	}
}
