import java.util.ArrayList;


public class Component 
{
	private String name;
	private ArrayList<String> types;
	private ArrayList<String> variables;
	private ArrayList<String> values;
	private ArrayList<Property> properties;
	private ArrayList<Port> ports;
	
	public Component(String n)
	{
		name = n;
		types = new ArrayList<String>();
		variables = new ArrayList<String>();
		values = new ArrayList<String>();
		properties = new ArrayList<Property>();
		ports = new ArrayList<Port>();
	}
	
	public void setVariables(ArrayList<String> variables) {
		this.variables = variables;
	}
	public ArrayList<String> getVariables() {
		return variables;
	}
	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	public ArrayList<String> getTypes() {
		return types;
	}
	
	public void addProperty(String type, String var)
	{
		types.add(type);
		variables.add(var);
	}

	public void addValue(String value)
	{
		values.add(value);
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public ArrayList<String> getValues() {
		return values;
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}

	public ArrayList<Property> getProperties() {
		
		for(int i = 0; i < types.size(); i++)
		{
			if(types.get(i).equals("Property"))
			{
				Property p = new Property();
				p.setName(variables.get(i));
				if(values.size() > 0) p.setValue(values.get(i));
				properties.add(p);
			}
			else if(types.get(i).equals("PortProperty"))
			{
				Property prop = new Property();
				prop.setName(variables.get(i));
				if(values.size() > 0) prop.setValue(values.get(i));
				ports.get(ports.size()-1).addProperty(prop);
			}
		}
		
		return properties;
	}

	public void setPorts(ArrayList<Port> ports) {
		this.ports = ports;
	}

	public ArrayList<Port> getPorts() {
		
		for(int i = 0; i < types.size(); i++)
		{
			if(types.get(i).equals("Port"))
			{
				Port p = new Port();
				p.setName(variables.get(i));
				p.setValue(values.get(i));
				ports.add(p);
			}
		}
		
		return ports;
	}
}
