import java.io.*;
import java.util.*;

public class SpamDomain 
{
	private ArrayList<String> words;
	private ArrayList<Character> characters;
	private ArrayList<ArrayList<Float>> inateKnowledge;
	private Table valueTable;
	private Table testMessagesTable;
	private ArrayList<ArrayList<Float>> testMessages;

	public SpamDomain(String basePath, String testPath, String namePath)
	{
		testMessages = new ArrayList<ArrayList<Float>>();
		inateKnowledge = new ArrayList<ArrayList<Float>>();
		valueTable = new Table(58);
		testMessagesTable = new Table(58);
		words = new ArrayList<String>();
		characters = new ArrayList<Character>();
		readSpambase(basePath);
		readTestMessages(testPath);
		readSpamNames(namePath);
		fillTable(inateKnowledge, valueTable);
		fillTable(testMessages, testMessagesTable);
	}

	public void addWord(String word)
	{
		words.add(word);
	}
	
	public void addChar(char character)
	{
		characters.add(character);
	}

	public void readSpamNames(String path)
	{
		File file = new File(path);
		Scanner scanner = null;
		try 
		{
			scanner = new Scanner(new FileReader(file));
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Can't find specified file!");
			return;
		}
		
		try 
		{
			while (scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				int index = line.indexOf("_");
				String type = line.substring(0, index);
				String info = line.substring(index+1);
				if(type.equals("word")) words.add(info);
				else if(type.equals("char")) characters.add(info.charAt(0));
			}
		} 
		finally 
		{
			scanner.close();
		}
	}
	
	public void readSpambase(String path)
	{
		File file = new File(path);
		Scanner scanner = null;
		try 
		{
			scanner = new Scanner(new FileReader(file));
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Can't find specified file!");
			return;
		}
		
		try 
		{
			while (scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				String[] values = line.split(",");
				ArrayList<Float> aux = new ArrayList<Float>();
				for(int i = 0; i < values.length; i++)
					aux.add(Float.valueOf(values[i].trim()).floatValue());

				inateKnowledge.add(aux);
			}
		} 
		finally 
		{
			scanner.close();
		}
	}
	
	public void readTestMessages(String path)
	{
		File file = new File(path);
		Scanner scanner = null;
		try 
		{
			scanner = new Scanner(new FileReader(file));
		} 
		catch (FileNotFoundException e)
		{
			System.err.println("Can't find specified file!");
			return;
		}
		
		try 
		{
			while (scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				String[] values = line.split(",");
				ArrayList<Float> aux = new ArrayList<Float>();
				for(int i = 0; i < values.length; i++)
					aux.add(Float.valueOf(values[i].trim()).floatValue());

				testMessages.add(aux);
			}
		} 
		finally 
		{
			scanner.close();
		}
	}
	
	
	
	public void fillTable(ArrayList<ArrayList<Float>> values, Table table)
	{
		ArrayList<ArrayList<Float>> intoTable = convertInfoToColumns(values);
		int nWords = words.size();
		int counter = 0;
		for(int i = 0; i < characters.size(); i++)
		{
			words.add(characters.get(i).toString());
			counter++;
		}
		words.add("Capital Average");
		words.add("Capital Longest");
		words.add("Capital Total");
		words.add("Spam");
		counter += 4;
		table.fillTable(words, intoTable);
		
		for(int i = 0; i < counter; i++)
		{
			words.remove(nWords);
		}
	}
	
	private ArrayList<ArrayList<Float>> convertInfoToColumns(ArrayList<ArrayList<Float>> values)
	{
		ArrayList<ArrayList<Float>> ret = new ArrayList<ArrayList<Float>>();
		int nLines = values.get(0).size();
		int j = 0;
		while(j < nLines)
		{
			ArrayList<Float> aux = new ArrayList<Float>();
			for(int i = 0; i < values.size(); i++)
				aux.add(values.get(i).get(j));

			ret.add(aux);
			j++;
		}
		
		return ret;
	}
	
	//getters and setters
	
	public ArrayList<ArrayList<Float>> getInateKnowledge() {
		return inateKnowledge;
	}

	public void setInateKnowledge(ArrayList<ArrayList<Float>> inateKnowledge) {
		this.inateKnowledge = inateKnowledge;
	}

	public Table getValueTable() {
		return valueTable;
	}

	public void setValueTable(Table valueTable) {
		this.valueTable = valueTable;
	}
	
	public Table getTestMessagesTable() {
		return testMessagesTable;
	}

	public void setTestMessagesTable(Table testMessagesTable) {
		this.testMessagesTable = testMessagesTable;
	}
	

	public ArrayList<String> getWords() 
	{
		return words;
	}

	public void setWords(ArrayList<String> words) 
	{
		this.words = words;
	}

	public ArrayList<Character> getCharacters() 
	{
		return characters;
	}

	public void setCharacters(ArrayList<Character> characters) 
	{
		this.characters = characters;
	}
	
	
	/*public static void main(String[] args)
	{
		SpamDomain m = new SpamDomain("C:\\spambase.data", "C:\\spamnames.txt");
		Table<Float> t = m.getValueTable();
		
		System.out.println(t.getValue(1, "Spam"));
		
		
		for(int i = 0; i < m.getInateKnowledge().size(); i++)
		{
			for(int j = 0; j < m.getInateKnowledge().get(i).size(); j++)
				System.out.println("Float - " + m.getInateKnowledge().get(i).get(j));
		}
	}*/
	
	
}
