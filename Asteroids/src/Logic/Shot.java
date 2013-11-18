package Logic;

import java.awt.Polygon;

public class Shot extends Character{
	
	
	double declive;
	double yInOrigin;
	/**
	 * Default constructor of Shot.
	 * @param s The spaceship it'll need to be constructed.
	 */
	public Shot(Spaceship s)
	{
		speed = 50;
		score = 0;
		alive = true;
		posX = s.getX();
		posY = s.getY();
		calculateTrajectory(s.getAngle());
	}
	
	/**
	 * Checks if the shot intersects the enemy while going through its "path".
	 * @param a The Enemy in question.
	 * @return A Boolean data type.
	 */
	public boolean shotIntersectsEnemy(Enemy a)
	{
		boolean xVerify = false;
		boolean yVerify = false;
		
		for(int i = 0; i < xPoints.size(); i++)
		{
			if(xPoints.elementAt(i) == a.getX())
			{
				xVerify = true;
				break;
			}
		}
		
		for(int i = 0; i < yPoints.size(); i++)
		{
			if(yPoints.elementAt(i) == a.getY())
			{
				yVerify = true;
				break;
			}
		}

		return xVerify && yVerify;
	}

	/**
	 * Shapes the Polygon that represents the Shot.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();
		shape.addPoint((int) posX-2, (int) posY-2);
		shape.addPoint((int) posX+2, (int) posY-2);
		shape.addPoint((int) posX+1, (int) posY-1);
		shape.addPoint((int) posX+1, (int) posY+2);
		shape.addPoint((int) posX-1, (int) posY+2);
		shape.addPoint((int) posX-1, (int) posY-1);
	}

	
}
