import java.io.*;
import java.util.ArrayList;


public class DotGenerator 
{
	private ArrayList<Component> components;
	private ArrayList<Connector> connectors;
	private ArrayList<Attachment> attachments;
	private String filename;
	private FileWriter fileStream;
	private BufferedWriter outStream;
	
	public DotGenerator(ArrayList<Component> comp, ArrayList<Connector> conn, ArrayList<Attachment> attach, String file)
	{
		components = comp;
		connectors = conn;
		attachments = attach;
		filename = file;
		try
		{
			openFile();
			fileHeader();
			generateDot();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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
        outStream.write("digraph ACME {");
        outStream.write('\n');
        outStream.write("graph [rankdir = \"LR\"];\n");
    }
    
    public void generateDot() throws IOException
    {
    	writeComponents();
    	outStream.write("\n\n");
    	writeConnectors();
    	outStream.write("}");
    	closeFile();
    }
    
    private void writeComponents() throws IOException
    {
    	for(int i = 0; i < components.size(); i++)
    	{
    		outStream.write("subgraph cluster_"+i);
    		outStream.write('\n');
    		outStream.write("{\n");
    		Component c = components.get(i);
    		outStream.write('\t' + c.getName());
    		outStream.write(" [label=\""+c.getName()+"\", shape=\"component\", fontname=\"Tahoma\"];\n");
    		String propertyCode = "Properties"+ i + " [fontname = \"Tahoma\" style=\"invisible\"" +
    				" shape = \"square\" label =<<table border=\"1\" cellborder=\"0\" cellpadding=\"3\" bgcolor=\"white\">" +
    				"<tr><td bgcolor=\"black\" align=\"center\" colspan=\"2\"><font color=\"white\">Properties</font></td></tr>";
    		
    		ArrayList<Property> properties = c.getProperties();
    		ArrayList<Port> ports = c.getPorts();
    		ArrayList<String> portPropertiesS = new ArrayList<String>();

    		for(int j = 0; j < ports.size(); j++)
    		{
    			Port p = ports.get(j);
    			String name = p.getName();
    			String var = "";
    			if(name.contains("-"))
    			{
    				String[] split = name.split("-");
    				for(int x = 0; x < split.length; x++)
    				{
    					if(x < split.length-1) var += split[x] + "_";
    					else var += split[x];
    				}
    			}
    			else var = name;
    			
    			outStream.write('\t' + var);
    			outStream.write(" [label=\""+p.getName().split("_")[0]+"\", fontname=\"Tahoma\"];");
    			outStream.write('\n');
    			
    			ArrayList<Property> portProperties = p.getProperties();
    			
    			if(portProperties.size() > 0)
    			{
	    			String innerProp = "Properties"+ var + " [fontname = \"Tahoma\" style=\"invisible\"" +
					" shape = \"square\" label =<<table border=\"1\" cellborder=\"0\" cellpadding=\"3\" bgcolor=\"white\">" +
					"<tr><td bgcolor=\"black\" align=\"center\" colspan=\"2\"><font color=\"white\">Properties</font></td></tr>";

	        		for(int x = 0; x < portProperties.size(); x++)
	        		{
	        			Property prop = portProperties.get(x);
	        			innerProp += "<tr><td align=\"center\">";
	        			innerProp += prop.getName();
	        			innerProp += "</td>";
	        			innerProp += "<td>" + prop.getValue() + "</td></tr>";
	        		}
	        		
	        		innerProp += "</table>> ];";
	        		String res = '\t' + innerProp + '\n';
	        		res += var +"->"+"Properties"+var+";";
	        		portPropertiesS.add(res);
    			}
    		}

    		for(int j = 0; j < properties.size(); j++)
    		{
    			Property p = properties.get(j);
    			propertyCode += "<tr><td align=\"center\">";
    			propertyCode += p.getName();
    			propertyCode += "</td>";
    			propertyCode += "<td>" + p.getValue() + "</td></tr>";
    		}
    		
    		propertyCode += "</table>> ];";
    		outStream.write("}\n");
    		if(properties.size() > 0)
			{
    			outStream.write('\t' + propertyCode + '\n');
    			outStream.write('\t' + c.getName() + "->" + "Properties"+i);
			}
    		
    		outStream.write("\n\n");
    		
    		for(int j = 0; j < portPropertiesS.size(); j++)
    			outStream.write(portPropertiesS.get(j));
    	}
    }
 	
    private void writeConnectors() throws IOException
    {
    	for(int i = 0; i < connectors.size(); i++)
    	{
    		Connector c = connectors.get(i);
    		
			String name = c.getName();
			String connName = "";
			if(name.contains("-"))
			{
				String[] split = name.split("-");
				for(int x = 0; x < split.length; x++)
				{
					if(x < split.length-1) connName += split[x] + "_";
					else connName += split[x];
				}
			}
			else connName = name;
			
    		outStream.write("subgraph cluster_" + connName);
    		outStream.write("\n{\n");
    		outStream.write("\t"+connName);
    		outStream.write(" [label=\"" + c.getName() + "\", shape=\"Msquare\", fontname=\"Tahoma\"];\n");
    		ArrayList<Property> properties = c.getProperties();
    		
    		ArrayList<Role> roles = c.getRoles();
    		
    		ArrayList<String> rolePropertiesS = new ArrayList<String>();
    		
    		String propertyCode = "Properties"+ connName + " [fontname = \"Tahoma\" style=\"invisible\"" +
    				" shape = \"square\" label =<<table border=\"1\" cellborder=\"0\" cellpadding=\"3\" bgcolor=\"white\">" +
    				"<tr><td bgcolor=\"black\" align=\"center\" colspan=\"2\"><font color=\"white\">Properties</font></td></tr>";
    		
    		for(int j = 0; j < roles.size(); j++)
    		{
    			Role p = roles.get(j);
    			String nameRole = p.getName();
    			String var = "";
    			if(nameRole.contains("-"))
    			{
    				String[] split = nameRole.split("-");
    				for(int x = 0; x < split.length; x++)
    				{
    					if(x < split.length-1) var += split[x] + "_";
    					else var += split[x];
    				}
    			}
    			else var = nameRole;
    			
    			outStream.write('\t' + var);
    			outStream.write(" [label=\""+p.getName().split("_")[0]+"\", fontname=\"Tahoma\"];");
    			outStream.write('\n');
    			
    			ArrayList<Property> roleProperties = p.getProperties();
    			
    			if(roleProperties.size() > 0)
    			{
	    			String innerProp = "Properties"+ var + " [fontname = \"Tahoma\" style=\"invisible\"" +
					" shape = \"square\" label =<<table border=\"1\" cellborder=\"0\" cellpadding=\"3\" bgcolor=\"white\">" +
					"<tr><td bgcolor=\"black\" align=\"center\" colspan=\"2\"><font color=\"white\">Properties</font></td></tr>";
	    			
	        		for(int x = 0; x < roleProperties.size(); x++)
	        		{
	        			Property prop = roleProperties.get(x);
	        			innerProp += "<tr><td align=\"center\">";
	        			innerProp += prop.getName();
	        			innerProp += "</td>";
	        			innerProp += "<td>" + prop.getValue() + "</td></tr>";
	        		}
	        		
	        		innerProp += "</table>> ];";
	        		String res = '\t' + innerProp + '\n';
	        		res += var +"->"+"Properties"+var+";";
	        		rolePropertiesS.add(res);
    			}
    		}

    		for(int j = 0; j < properties.size(); j++)
    		{
    			Property p = properties.get(j);
    			propertyCode += "<tr><td align=\"center\">";
    			propertyCode += p.getName();
    			propertyCode += "</td>";
    			propertyCode += "<td>" + p.getValue() + "</td></tr>";
    		}
    		
    		propertyCode += "</table>> ];";
    		outStream.write("}\n");
    		if(properties.size() > 0) outStream.write('\t' + propertyCode + '\n');
    		
    		for(int x = 0; x < attachments.size(); x++)
    		{
    			Attachment a = attachments.get(x);
    			
				String nameSource = a.getFromPort();
				String varSource = "";
				if(nameSource.contains("-"))
				{
					String[] split = nameSource.split("-");
					for(int y = 0; y < split.length; y++)
					{
						if(y < split.length-1) varSource += split[y] + "_";
						else varSource += split[y];
					}
				}
				else varSource = nameSource;
				
				String nameDest = a.getToPort();
				String varDest = "";
				if(nameDest.contains("-"))
				{
					String[] split = nameDest.split("-");
					for(int y = 0; y < split.length; y++)
					{
						if(y < split.length-1) varDest += split[y] + "_";
						else varDest += split[y];
					}
				}
				else varDest = nameDest;
    			
    			String source = varSource+"_"+a.getFromPackage();
    			String destination = varDest;
    			
    			outStream.write('\t' + source + "->" + destination+";\n");
    		}
    		
    		outStream.write('\n');
    		
    		if(properties.size() > 0) outStream.write('\t' + connName + "->" + "Properties"+ connName + ";");
    		
    		outStream.write('\n');
    		
    		for(int j = 0; j < rolePropertiesS.size(); j++)
    			outStream.write(rolePropertiesS.get(j));
    	}
    }
    
	public void setConnectors(ArrayList<Connector> connectors) {
		this.connectors = connectors;
	}
	public ArrayList<Connector> getConnectors() {
		return connectors;
	}
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
	}
	public ArrayList<Component> getComponents() {
		return components;
	}
	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}
	public ArrayList<Attachment> getAttachments() {
		return attachments;
	}
}
