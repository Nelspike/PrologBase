package Logic;

import java.awt.Polygon;

public class AsteroidSmall extends Asteroid{

	/**
	 * Default constructor of a Small Asteroid.
	 */
	public AsteroidSmall()
	{
		type = "small";
		speed = 50;
		score = 100;
	}
	
	/**
	 * Constructor of a Small Asteroid in a specific position.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public AsteroidSmall(double x, double y)
	{
		type = "small";
		speed = 50;
		score = 100;
		posX = x;
		posY = y;
		shapingPoints();
	}
	
	/**
	 * Shapes the Polygon that represents the Small Asteroid.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();
		
		shape.addPoint((int) posX+5, (int) posY+10);
		shape.addPoint((int) posX+10, (int) posY);
		shape.addPoint((int) posX+25, (int) posY+5);
		shape.addPoint((int) posX+10, (int) posY-20);
		shape.addPoint((int) posX-15, (int) posY-15);
		shape.addPoint((int) posX-5, (int) posY);
		shape.addPoint((int) posX-15, (int) posY+15);
	}
}
