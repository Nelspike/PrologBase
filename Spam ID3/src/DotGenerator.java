import java.io.*;
import java.util.*;

public class DotGenerator 
{
	private Tree tree;
	private FileWriter fileStream;
	private BufferedWriter outStream;
	private String filename;
	private String type;
	
	public DotGenerator(Tree t, String file, String tp) throws IOException
	{
		setTree(t);
		filename = file;
		type = tp;
		openFile();
		fileHeader();
	}
	
	public void generateDot() throws IOException
	{
		writeNodes();
		writeEdges();
		fileFooter();
		closeFile();
	}
	
	private void openFile() throws IOException
	{
		File f = new File(filename);
		if(f.exists()) f.delete();
		fileStream = new FileWriter(filename);
	}

	private void closeFile() throws IOException
	{
		outStream.close();
	}
	
	private void fileHeader() throws IOException
	{
		outStream = new BufferedWriter(fileStream);
		outStream.write("digraph "+type+" {");
		outStream.write('\n');
	}
	
	private void writeNodes() throws IOException
	{
		ArrayList<Node> nodes = tree.getNodes();
		for(int i = 0; i < nodes.size(); i++)
		{
			Node n = nodes.get(i);
			if(n.isFinalNode())
			{
				String s = n.getSplit();
				s += " ";
				s += "[label=\""+n.getNodeValue()+"\"]";
				s += '\n';
				outStream.write(s);
			}
			else
			{
				String s = "";

				String sub = n.getSplit().substring(1);
				s += "Word";
				
				if(n.getSplit().matches("\\;\\§[0-9]+"))
					s += "SemiColon"+sub;
				else if(n.getSplit().matches("\\(\\§[0-9]+"))
					s += "OpenBracket"+sub;
				else if(n.getSplit().matches("\\[\\§[0-9]+"))
					s += "OpenStraightBracket"+sub;
				else if(n.getSplit().matches("\\!\\§[0-9]+"))
					s += "Exclamation"+sub;
				else if(n.getSplit().matches("\\$\\§[0-9]+"))
					s += "Dollar"+sub;
				else if(n.getSplit().matches("\\#\\§[0-9]+"))
					s += "Sharp"+sub;
				else
				{
					if(n.getSplit().indexOf(" ") == -1)
						s += n.getSplit();
					else
					{
						String[] concat = n.getSplit().split(" ");
						s += concat[0] + concat[1];
					}
				}		
				
				String numberlessLabel = "";
				
				int index = n.getSplit().indexOf('§');
				numberlessLabel = n.getSplit().substring(0,index);
					
				s += " ";
				s += "[label=\""+numberlessLabel+"\"]";
				s += '\n';
				outStream.write(s);
			}
		}
	}
	
	private void writeEdges() throws IOException
	{
		outStream.write('\n');
		ArrayList<Edge> edges = tree.getEdges();
		
		for(int i = 0; i < edges.size(); i++)
		{
			Edge e = edges.get(i);
			String s = "";
			Node start = e.getStartNode();
			Node end = e.getEndNode();
			float value = e.getEdgeValue();
			
			if(!start.isFinalNode())
			{
				String sub = start.getSplit().substring(1);
				s += "Word";
				
				if(start.getSplit().matches("\\;\\§[0-9]+"))
					s += "SemiColon"+sub;
				else if(start.getSplit().matches("\\(\\§[0-9]+"))
					s += "OpenBracket"+sub;
				else if(start.getSplit().matches("\\[\\§[0-9]+"))
					s += "OpenStraightBracket"+sub;
				else if(start.getSplit().matches("\\!\\§[0-9]+"))
					s += "Exclamation"+sub;
				else if(start.getSplit().matches("\\$\\§[0-9]+"))
					s += "Dollar"+sub;
				else if(start.getSplit().matches("\\#\\§[0-9]+"))
					s += "Sharp"+sub;
				else
				{
					if(start.getSplit().indexOf(" ") == -1)
						s += start.getSplit();
					else
					{
						String[] concat = start.getSplit().split(" ");
						s += concat[0] + concat[1];
					}
				}	
			}
			else
				s += start.getSplit();
				
			s += " -> ";

			if(!end.isFinalNode())
			{
				String sub = end.getSplit().substring(1);
				s += "Word";
				
				if(end.getSplit().matches("\\;\\§[0-9]+"))
					s += "SemiColon"+sub;
				else if(end.getSplit().matches("\\(\\§[0-9]+"))
					s += "OpenBracket"+sub;
				else if(end.getSplit().matches("\\[\\§[0-9]+"))
					s += "OpenStraightBracket"+sub;
				else if(end.getSplit().matches("\\!\\§[0-9]+"))
					s += "Exclamation"+sub;
				else if(end.getSplit().matches("\\$\\§[0-9]+"))
					s += "Dollar"+sub;
				else if(end.getSplit().matches("\\#\\§[0-9]+"))
					s += "Sharp"+sub;
				else
				{
					if(end.getSplit().indexOf(" ") == -1)
						s += end.getSplit();
					else
					{
						String[] concat = end.getSplit().split(" ");
						s += concat[0] + concat[1];
					}
				}	
			}
			else
				s += end.getSplit();
				
			if(value == 100.0)
				s += "[label=\"" + "<= "+ value + "\"]";
			else
				s += "[label=\"" + "< "+ value + "\"]";
			
			s += '\n';
			
			outStream.write(s);
		}
	}
	
	private void fileFooter() throws IOException
	{
		outStream.write("}");
	}
	
	//Getters and Setters
	
	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	public FileWriter getFilestream() {
		return fileStream;
	}

	public void setFilestream(FileWriter filestream) {
		this.fileStream = filestream;
	}

	public BufferedWriter getOutstream() {
		return outStream;
	}

	public void setOutstream(BufferedWriter outstream) {
		this.outStream = outstream;
	}
	
	
}
