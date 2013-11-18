package Logic;

public class Asteroid extends Enemy{
	
	public String type;
	
	/**
	 * Default constructor of Asteroid.
	 */
	public Asteroid()
	{
		enemyType = "Asteroid";
	}
	
	/**
	 * Gets the type of an asteroid.
	 * @return A String data type.
	 */
	public String getTypeAst()
	{
		return type;
	}
}
