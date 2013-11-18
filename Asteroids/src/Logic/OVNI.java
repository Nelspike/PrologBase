package Logic;

import java.awt.Polygon;

public class OVNI extends Enemy{

	/**
	 * Default constructor of OVNI.
	 * @param _speed The speed of the OVNI.
	 * @param _score The score of the OVNI.
	 */
	public OVNI(double _speed, int _score)
	{
		speed = _speed;
		score = _score;
		enemyType = "OVNI";
	}
	
	/**
	 * Shapes the Polygon that represents the UFO.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();
		shape.addPoint((int) posX-10, (int) posY-10);
		shape.addPoint((int) posX+10, (int) posY-10);
		shape.addPoint((int) posX+10, (int) posY);
		shape.addPoint((int) posX+20, (int) posY+10);
		shape.addPoint((int) posX-20, (int) posY+10);
		shape.addPoint((int) posX-10, (int) posY);
	}
}
