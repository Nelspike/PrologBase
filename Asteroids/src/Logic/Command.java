package Logic;

import java.util.Random;
import java.util.Vector;
import java.awt.*;

public class Command {

	private int level;
	Vector<Enemy> enemies;
	private double sX, sY;
	
	/**
	 * Default Constructor of Command.
	 */
	public Command() 
	{
		level = 1;
		enemies = new Vector<Enemy>();
	}
	
	/**
	 * Gets the vector of enemies of the current game.
	 * @return A vector of enemies.
	 */
	public Vector<Enemy> getEnemies()
	{
		return enemies;
	}
	
	/**
	 * Sets the vector of enemies of the current game.
	 * @param _enemies The vector of enemies to be set.
	 */
	public void setEnemies(Vector<Enemy> _enemies)
	{
		enemies = _enemies;
	}
	
	/**
	 * Moves the Spaceship in the game.
	 * @param key Key pressed.
	 * @param s Current spaceship
	 * @param boost Current boost
	 */
	public void moveSpaceShip(char key, Spaceship s, int boost) 
	{
		double posY = s.getY();
		double posX = s.getX();
		int angle = s.getAngle();
		switch(key)
		{
			case 'w':
				verifyAngle(angle,posX,posY,s);
				s.setX(sX);
				s.setY(sY);
				break;
			case 'd':
				if(angle < 350)
					angle += 10;
				else
					angle = 0;
				
				s.setAngle(angle);
				break;
			case 'a':
				if(angle > 10)
					angle -= 10;
				else
					angle = 359;
				
				s.setAngle(angle);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Verifies the angle of the spaceship, giving it the necessary coordinates to move.
	 * @param angle The Angle to be verified.
	 * @param posX The current X position.
	 * @param posY The current Y position.
	 * @param s The Spaceship in question.
	 */
	public void verifyAngle(double angle, double posX, double posY, Spaceship s)
	{
		double dec, yInO;
		if(angle == 90)
		{
			posY += 5;
			sY = posY;
			sX = posX;
		}
		else if(angle == 0 || angle == 360)
		{
			posX += 5;
			sY = posY;
			sX = posX;
		}
		else if(angle == 180)
		{
			posX -= 5;
			sY = posY;
			sX = posX;
		}
		else if(angle == 270)
		{
			posY -= 5;
			sY = posY;
			sX = posX;
		}
		else if(angle > 0 && angle < 90)
		{
			posX += 5;
			s.calculateTrajectoryS();
			dec = s.getDeclive();
			yInO = s.getOrigin();
			posY = (dec*posX + yInO);
			sX = posX;
			sY = posY;
		}
		else if(angle > 90 && angle < 270)
		{
			posX -= 5;
			s.calculateTrajectoryS();
			dec = s.getDeclive();
			yInO = s.getOrigin();
			posY = (dec*posX + yInO);
			sX = posX;
			sY = posY;
		}
		else if (angle > 270 && angle < 360)
		{
			posX += 5;
			s.calculateTrajectoryS();
			dec = s.getDeclive();
			yInO = s.getOrigin();
			posY = (dec*posX + yInO);
			sX = posX;
			sY = posY;
		}
	}
	
	/**
	 * Fires a shot from the current spaceship.
	 * @param key Key pressed.
	 * @param s Current spaceship.
	 * @return A Boolean data type.
	 */
	public boolean fireSpaceShip(char key, Spaceship s) 
	{
		switch(key)
		{
			case 32: //space
				Shot sh = new Shot(s);
				Vector<Shot> shots = s.getShots();
				shots.addElement(sh);
				s.setShots(shots);
				return true;
			default:
					break;
		}
		
		return false;
	}


	/**
	 * Moves the enemy by one.
	 * @param e The Enemy in question.
	 */
	public void moveEnemy(Enemy e)
	{
		Vector<Double> xPoints = e.getXCoordinates();
		Vector<Double> yPoints = e.getYCoordinates();
		
		e.setX(xPoints.elementAt(0));
		e.setY(yPoints.elementAt(0));
		xPoints.remove(0);
		yPoints.remove(0);
		
		if(!(xPoints.size() == 0) || !(yPoints.size() == 0))
		{
			e.setXCoordinates(xPoints);
			e.setYCoordinates(yPoints);
		}
		else
		{
			suicideEnemy(e);
		}
	}
	
	/**
	 * Moves the current shots in-game.
	 * @param s The shot to be moved.
	 */
	public void moveShots(Shot s)
	{
		Vector<Double> xPoints = s.getXCoordinates();
		Vector<Double> yPoints = s.getYCoordinates();
		
		s.setX(xPoints.elementAt(0));
		s.setY(yPoints.elementAt(0));
		xPoints.remove(0);
		yPoints.remove(0);
		
		if(!(xPoints.size() == 0) || !(yPoints.size() == 0))
		{
			s.setXCoordinates(xPoints);
			s.setYCoordinates(yPoints);
		}
		else
		{
			suicideShot(s);
		}
	}
	
	/**
	 * Kills the enemy, via suicide.
	 * @param e The enemy in question.
	 */
	public void suicideEnemy(Enemy e)
	{
		e.setAlive(false);
	}
	
	/**
	 * Kills the shot, via suicide.
	 * @param s The shot in question.
	 */
	public void suicideShot(Shot s)
	{
		s.setAlive(false);
	}
	
	/**
	 * Divides asteroid when hit by shot
	 * @param a The asteroid to be divided.
	 * @param i Index of this asteroid in the enemies vector.
	 */
	public void divideAsteroid(Asteroid a) 
	{
		String type = a.getTypeAst();
		double posX = a.getX();
		double posY = a.getY();
		
		if(type.equals("large"))
		{
			Asteroid a1 = new AsteroidMedium(posX, posY);
			Asteroid a2 = new AsteroidMedium(posX, posY);			
			enemies.add(a1);
			enemies.add(a2);
			suicideEnemy(a);
		}
		else if(type.equals("medium"))
		{
			Asteroid a1 = new AsteroidSmall(posX, posY);
			Asteroid a2 = new AsteroidSmall(posX, posY);
			Asteroid a3 = new AsteroidSmall(posX, posY);
			enemies.add(a1);
			enemies.add(a2);
			enemies.add(a3);
			suicideEnemy(a);
		}
		else
			suicideEnemy(a);
	}
	
	/**
	 * Increments the spaceship score by a certain value.
	 * @param s The Spaceship to have its score incremented.
	 * @param score The score to increment.
	 */
	public void incrementSpaceShipScore(Spaceship s, int score)
	{
		s.setTotalScore(score); 
	}
	
	/**
	 * Creates enemies into the game.
	 * @param type Type of enemy to be created.
	 */
	public void enemyCreation(String type) 
	{
		if(type.equals("Asteroid"))
		{
			Asteroid a;
			Random rand = new Random();
			int size = rand.nextInt(3);
			switch(size)
			{
				case 0:
					a = new AsteroidLarge();
					enemies.add(a);
					break;
				case 1:
					a = new AsteroidMedium();
					enemies.add(a);
					break;
				case 2:
					a = new AsteroidSmall();
					enemies.add(a);
					break;
				default:
					break;
			}
		}
		else
		{
			OVNI o = new OVNI(10*level, 200*level);
			enemies.add(o);
		}
	}
	
	/**
	 * Verifies if the current spaceship crashes into an enemy, with boundary boxes.
	 * @param s The current spaceship.
	 * @param e The enemy in question.
	 * @return A boolean data type.
	 */
	public boolean crashIntoEnemy(Spaceship s, Enemy e) 
	{
		Polygon spaceship = s.getShape();
		Polygon enemy = e.getShape();
		
		Rectangle sBoundBox = spaceship.getBounds();
		Rectangle eBoundBox = enemy.getBounds();
		
		if(sBoundBox.intersects(eBoundBox))
		{
			s.setAlive(false);
			return true;
		}

		return false;
	}
	
	/**
	 * Verifies if the current shot crashes into an enemy, with boundary boxes.
	 * @param s The current shot.
	 * @param e The enemy in question.
	 * @param i Index of this enemy in the enemies vector.
	 * @return A boolean data type.
	 */
	public boolean shotCrashesIntoEnemy(Shot sh, Enemy e, Spaceship s, int i)
	{
		String typeEnemy = e.getType();
		Polygon shot = sh.getShape();
		Polygon enemy = e.getShape();
		
		Rectangle shBoundBox = shot.getBounds();
		Rectangle eBoundBox = enemy.getBounds();
		
		if(shBoundBox.intersects(eBoundBox))
		{
			suicideShot(sh);
			incrementSpaceShipScore(s, e.getScore()*level);
			
			if(typeEnemy.equals("Asteroid"))
				divideAsteroid((Asteroid) e);
			else
				suicideEnemy(e);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Advances to next level.
	 */
	public void goToNextLevel()
	{
		level++;
	}
	
	/**
	 * Sets the current level of the game.
	 * @param _level The level to be set.
	 */
	public void setLevel(int _level)
	{
		level = _level;
	}
	
	/**
	 * Gets the level within the game.
	 * @return An Integer data type.
	 */
	public int getLevel()
	{
		return level;
	}
	
}
