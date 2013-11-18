package Logic;

import java.awt.Polygon;

public class AsteroidLarge extends Asteroid {

	/**
	 * Default constructor of a Large Asteroid.
	 */
	public AsteroidLarge() 
	{
		type = "large";
		speed = 100;
		score = 200;
		shapingPoints();
	}
	
	/**
	 * Shapes the Polygon that represents the Large Asteroid.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();
		
		shape.addPoint((int) posX+25, (int) posY+40);
		shape.addPoint((int) posX+40, (int) posY+10);
		shape.addPoint((int) posX+30, (int) posY);
		shape.addPoint((int) posX+35, (int) posY-30);
		shape.addPoint((int) posX-10, (int) posY-35);
		shape.addPoint((int) posX+5, (int) posY-20);
		shape.addPoint((int) posX-20, (int) posY-25);
		shape.addPoint((int) posX-35, (int) posY+20);
		shape.addPoint((int) posX-15, (int) posY+40);
		shape.addPoint((int) posX, (int) posY+30);
	}
}
