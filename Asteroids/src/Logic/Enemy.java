package Logic;

import java.util.Random;

public class Enemy extends Character{

	private int startWall;
	public int startX, startY, finalX, finalY;
	public String enemyType;
	
	/**
	 * Default constructor of Enemy.
	 */
	public Enemy()
	{
		startWall = genStartWall(); //0 - up; 1 - down; 2 - right; 3 - left
		checkStartWallPos(startWall);
		posX = startX;
		posY = startY;
		alive = true;
		calculateTrajectory(angle);
	}
	
	/**
	 * Generates the wall where the enemy shall appear from.
	 * @return An Integer data type.
	 */
	public int genStartWall()
	{
		Random r = new Random();
		return r.nextInt(4);
	}
	
	/**
	 * generates the wall where the enemy shall disappear from.
	 * @return An Integer data type.
	 */
	public int genEndWall()
	{
		Random r = new Random();
		int next = r.nextInt(4);
		if(next == startWall)
		{
			while(next == startWall)
			{
				next = r.nextInt(4);
			}
		}
		return next;
	}
	
	/**
	 * Generates a random number for a position to be defined.
	 * @return An Integer data type.
	 */
	public int genPos()
	{
		Random r = new Random();
		return r.nextInt(640);
	}
	
	/**
	 * Checks which one is the starting wall, and calculates the starting position from it.
	 * @param wall The starting wall.
	 */
	public void checkStartWallPos(int wall)
	{
		Random r = new Random();
		switch(wall)
		{
			case 0:
				startY = 0;
				startX = genPos();
				angle = r.nextInt(179)+1;
				break;
			case 1:
				startY = 639;
				startX = genPos();
				angle = r.nextInt(178)+181;
				break;
			case 2:
				startY = genPos();
				startX = 639;
				angle = r.nextInt(180)+89;
				break;
			case 3:
				startY = genPos();
				startX = 0;
				int ori = r.nextInt(2);
				if(ori == 0)
				{
					angle = r.nextInt(89)+1;
				}
				else
				{
					angle = r.nextInt(180)+180;
					if(angle <= 270)
					{
						while(angle <= 270)
							angle = r.nextInt(180)+180;
					}
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Verifies the X coordinates of the starting and final points to the point of not allowing vertical lines to exist.
	 */
	public void verifyX()
	{
		if(startX == finalX)
		{
			if(startX == 639)
				startX -= 1;
			else
				finalX += 1;
		}
	}
	
	/**
	 * Gets the starting X position of the enemy in the pane.
	 * @return An Integer data type.
	 */
	public int getStartX()
	{
		return startX;
	}
	
	/**
	 * Gets the starting Y position of the enemy in the pane.
	 * @return An Integer data type.
	 */
	public int getStartY()
	{
		return startY;
	}
	
	/**
	 * Gets the enemy type of an asteroid.
	 * @return A String data type.
	 */
	public String getType()
	{
		return enemyType;
	}
}
