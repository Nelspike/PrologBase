package Logic;

import java.awt.Polygon;

public class AsteroidMedium extends Asteroid{

	/**
	 * Default constructor of a Medium Asteroid.
	 */
	public AsteroidMedium() 
	{
		type = "medium";
		speed = 75;
		score = 150;
	}
	
	/**
	 * Constructor of a Medium Asteroid in a specific position.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public AsteroidMedium(double x, double y)
	{
		type = "medium";
		speed = 75;
		score = 150;
		posX = x;
		posY = y;
		shapingPoints();
	}
	
	/**
	 * Shapes the Polygon that represents the Medium Asteroid.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();
		
		shape.addPoint((int) posX, (int) posY+30);
		shape.addPoint((int) posX+15, (int) posY+15);
		shape.addPoint((int) posX+25, (int) posY+20);
		shape.addPoint((int) posX+15, (int) posY-20);
		shape.addPoint((int) posX-15, (int) posY-15);
		shape.addPoint((int) posX-10, (int) posY);
		shape.addPoint((int) posX-35, (int) posY-5);
	}
}
