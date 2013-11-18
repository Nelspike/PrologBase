import java.util.ArrayList;


public class Port
{
	private String name;
	private String value;
	private ArrayList<Property> properties;
	
	public Port()
	{
		properties = new ArrayList<Property>();
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void addProperty(Property p)
	{
		properties.add(p);
	}
}
