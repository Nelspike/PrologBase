package Logic;

import java.awt.Polygon;
import java.util.Vector;

public class Character {

	public double speed;
	public int score;
	public boolean alive;
	public int initialPos;
	public int angle;
	public double posX;
	public double posY;
	public double finalX;
	public double finalY;
	public Polygon shape = new Polygon();
	public Vector<Double> xPoints;
	public Vector<Double> yPoints;
	
	/**
	 * Default constructor of Character.
	 */
	public Character(){}
	
	/**
	 * Sets the speed of the character in question.
	 * @param _speed The speed to be set.
	 */
	public void setSpeed(double _speed)
	{
		speed = _speed;
	}
	
	/**
	 * Gets the current value of speed of a character.
	 * @return A double data type.
	 */
	public double getSpeed()
	{
		return speed;
	}
	
	/**
	 * Sets the score of the character in question.
	 * @param _score The score to be set.
	 */
	public void setScore(int _score)
	{
		score = _score;
	}
	
	/**
	 * Gets the current value in score of a character.
	 * @return An Integer data type.
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Sets whether the character is alive or not.
	 * @param _alive The boolean with the information if the character is alive or not.
	 */
	public void setAlive(boolean _alive)
	{
		alive = _alive;
	}
	
	/**
	 * Gets the living status of a character.
	 * @return A Boolean data type.
	 */
	public boolean getAlive()
	{
		return alive;
	}
	
	/**
	 * Sets the initial position of the character on the screen.
	 * @param init The position to be set.
	 */
	public void setInitialPoint(int init)
	{
		initialPos = init;
	}
	
	/**
	 * Gets the initial position of the character on the screen.
	 * @return An Integer Data type.
	 */
	public int getInitialPoint()
	{
		return initialPos;
	}
	
	/**
	 * Gets the position X of the character on the pane.
	 * @return A Double data type.
	 */
	public double getX()
	{
		return posX;
	}
	
	/**
	 * Sets the position X of the character on the pane.
	 * @param _posX The position X to be set.
	 */
	public void setX(double _posX)
	{
		posX = _posX;
	}
	
	/**
	 * Gets the position Y of the character on the pane.
	 * @return A Double data type.
	 */
	public double getY()
	{
		return posY;
	}
	
	/**
	 * Sets the position Y of the character on the pane.
	 * @param _posY The position Y to be set.
	 */
	public void setY(double _posY)
	{
		posY = _posY;
	}
	
	/**
	 * Gets the final X position of the character in the pane.
	 * @return A Double data type.
	 */
	public double getFinalX()
	{
		return finalX;
	}
	
	/**
	 * Gets the final Y position of the character in the pane.
	 * @return A Double data type.
	 */
	public double getFinalY()
	{
		return finalY;
	}
	
	/**
	 * Gets the array with the coordinates of X where the character shall go through.
	 * @return A vector of Double objects.
	 */
	public Vector<Double> getXCoordinates()
	{
		return xPoints;
	}
	
	/**
	 * Gets the array with the coordinates of Y where the character shall go through.
	 * @return A vector of Double objects.
	 */
	public Vector<Double> getYCoordinates()
	{
		return yPoints;
	}
	
	/**
	 * Sets the array with the coordinates of X where the character shall go through.
	 * @param _x A vector of Integer objects. The Vector to be set.
	 */
	public void setXCoordinates(Vector<Double> _x)
	{
		xPoints = _x;
	}
	
	/**
	 * Sets the array with the coordinates of Y where the character shall go through.
	 * @param _y A vector of Integer objects. The Vector to be set.
	 */
	public void setYCoordinates(Vector<Double> _y)
	{
		yPoints = _y;
	}
	
	/**
	 * Gets the polygon correspondent to the character in-game.
	 * @return A Polygon Object.
	 */
	public Polygon getShape()
	{
		return shape;
	}
	
	/**
	 * Gets the current Character angle.
	 * @return An Integer data type.
	 */
	public int getAngle() 
	{
		return angle;
	}
	
	/**
	 * Sets the Character angle.
	 * @param a Angle to be set.
	 */
	public void setAngle(int a)
	{
		angle = a;
	}
	
	/**
	 * Calculates all of the points within the "path" a shot goes through.
	 * @param angle The Angle of the trajectory.
	 */
	public void calculateTrajectory(int angle)
	{
		double m = 0;
		xPoints = new Vector<Double>();
		yPoints = new Vector<Double>();

		switch(angle)
		{
			case 90:
				finalX = posX;
				finalY = 639;
				fillInPointsY();
				break;
			case 270:
				finalX = posX;
				finalY = 0;
				fillInPointsY();
				break;
			case 180:
				finalX = 0;
				finalY = posY;
				fillInPointsX();
				break;
			case 0:
				finalX = 639;
				finalY = posY;
				fillInPointsX();
				break;
			case 360:
				finalX = 639;
				finalY = posY;
				fillInPointsX();
				break;
			default:
				double a = Math.toRadians((double) angle);
				m = Math.tan(a);
				calculatePoints(m, angle);
				break;
		}
	}
	
	/**
	 * Calculates which points the arrays shall contain, so the character goes through them.
	 * @param m The inclination of the line.
	 * @param a The angle in question.
	 */
	public void calculatePoints(double m, int a)
	{
		double b = posY - m*posX;

		if(a > 0 && a < 90)
		{
			if(a <= 45)
				calculateForFirstQuadrant(0, m, b);
			else
				calculateForFirstQuadrant(1, m, b);
		}
		else if(a > 90 && a < 180)
		{
			if(a <= 135)
				calculateForSecondQuadrant(0, m, b);
			else
				calculateForSecondQuadrant(1, m, b);
		}
		else if(a > 180 && a < 270)
		{
			if(a <= 225)
				calculateForThirdQuadrant(0, m, b);
			else
				calculateForThirdQuadrant(1, m, b);
		}
		else if(a > 270 && a < 360)
		{
			if(a <= 315)
				calculateForFourthQuadrant(0, m, b);
			else
				calculateForFourthQuadrant(1, m, b);
		}
	}
	
	/**
	 * Fills in the X coordinates in the array.
	 */
	public void fillInPointsX()
	{
		double posToBeStatic = posY;
		if(finalX > posX)
		{
			while(posX <= finalX)
			{
				xPoints.addElement(posX);
				yPoints.addElement(posToBeStatic);
				posX++;
			}
		}
		else
		{
			while(posX >= finalX)
			{
				xPoints.addElement(posX);
				yPoints.addElement(posToBeStatic);
				posX--;
			}
		}
	}
	
	/**
	 * Fills in the Y coordinates in the array.
	 */
	public void fillInPointsY()
	{
		double posToBeStatic = posX;
		if(finalY > posY)
			while(posY <= finalY)
			{
				yPoints.addElement(posY);
				xPoints.addElement(posToBeStatic);
				posY++;
			}
		else
			while(posY >= finalY)
			{
				yPoints.addElement(posY);
				xPoints.addElement(posToBeStatic);
				posY--;
			}
	}
	
	/**
	 * Calculates which points need to be filled in the Array, according to the first Quadrant.
	 * @param place The Integer that represents the octant in question (first or second).
	 * @param m The inclination of the line.
	 * @param b The value of Y when X is 0 (Y in the origin).
	 */
	public void calculateForFirstQuadrant(int place, double m, double b)
	{
		double i = posX;
		double y = 0;
		if(place == 0)
		{
			while(i < 640)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i++;
			}
		}
		else
		{
			while(y < 640)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i++;
			}
		}
	}

	/**
	 * Calculates which points need to be filled in the Array, according to the second Quadrant.
	 * @param place The Integer that represents the octant in question (first or second).
	 * @param m The inclination of the line.
	 * @param b The value of Y when X is 0 (Y in the origin).
	 */
	public void calculateForSecondQuadrant(int place, double m, double b)
	{
		double i = posX;
		double y = 0;
		if(place == 0)
		{
			while(y < 640)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i--;
			}
		}
		else
		{
			while(i > 0)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i--;
			}
		}
	}

	/**
	 * Calculates which points need to be filled in the Array, according to the third Quadrant.
	 * @param place The Integer that represents the octant in question (first or second).
	 * @param m The inclination of the line.
	 * @param b The value of Y when X is 0 (Y in the origin).
	 */
	public void calculateForThirdQuadrant(int place, double m, double b)
	{
		double i = posX;
		double y = 0;
		if(place == 0)
		{
			while(i > 0)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i--;
			}
		}
		else
		{
			while(y >= 0)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i--;
			}
		}
	}

	/**
	 * Calculates which points need to be filled in the Array, according to the fourth Quadrant.
	 * @param place The Integer that represents the octant in question (first or second).
	 * @param m The inclination of the line.
	 * @param b The value of Y when X is 0 (Y in the origin).
	 */
	public void calculateForFourthQuadrant(int place, double m, double b)
	{
		double i = posX;
		double y = 0;
		if(place == 0)
		{
			while(y >= 0)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i++;
			}
		}
		else
		{
			while(i < 640)
			{
				y = (m*i + b);
				xPoints.addElement(i);
				yPoints.addElement(y);
				i++;
			}
		}
	}
	
}
