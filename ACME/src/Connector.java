import java.util.ArrayList;


public class Connector
{
	private String name;
	private ArrayList<String> types;
	private ArrayList<String> variables;
	private ArrayList<String> values;
	private ArrayList<Property> properties;
	private ArrayList<Role> roles;
	
	public Connector(String n)
	{
		name = n;
		types = new ArrayList<String>();
		variables = new ArrayList<String>();
		values = new ArrayList<String>();
		properties = new ArrayList<Property>();
		roles = new ArrayList<Role>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	public ArrayList<String> getTypes() {
		return types;
	}
	public void setVariables(ArrayList<String> variables) {
		this.variables = variables;
	}
	public ArrayList<String> getVariables() {
		return variables;
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
	
	public void setValues(ArrayList<String> variables) {
		this.values = variables;
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
				p.setValue(values.get(i));
				properties.add(p);
			}
		}
		
		return properties;
	}

	public void setRoles(ArrayList<Role> roles) {
		this.roles = roles;
	}

	public ArrayList<Role> getRoles() {
		
		for(int i = 0; i < types.size(); i++)
		{
			if(types.get(i).equals("Role"))
			{
				Role p = new Role();
				p.setName(variables.get(i));
				if(values.size() > 0) p.setValue(values.get(i));
				roles.add(p);
			}
			else if(types.get(i).equals("RoleProperty"))
			{
				Property prop = new Property();
				prop.setName(variables.get(i));
				if(values.size() > 0) prop.setValue(values.get(i));
				roles.get(roles.size()-1).addProperty(prop);
			}
		}
		
		return roles;
	}
}
