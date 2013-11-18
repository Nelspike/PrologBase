package Logic;

import java.awt.Polygon;
import java.util.Vector;

public class Spaceship extends Character{

	private boolean isBoosted;
	private int totalScore;
	private Vector<Shot> shots = new Vector<Shot>();
	private double declive, yInOrigin;
	
	/**
	 * Default constructor of Spaceship.
	 */
	public Spaceship()
	{
		totalScore = 0;
		score = 0;
		//definir valor de speed aqui assim como o valor a acrescentar de boost (x2)
		speed = 0;
		posX = 320;
		posY = 320;
		angle = 0;
		alive = true;
		isBoosted = false;
		shapingPoints();
	}
	
	/**
	 * Sets whether the spaceship has a speed boost or not.
	 * @param boost
	 */
	public void setBoost(boolean boost)
	{
		isBoosted = boost;
	}
	
	/**
	 * Gets whether the spaceship has been boosted or not.
	 * @return A boolean data type.
	 */
	public boolean getBoost()
	{
		return isBoosted;
	}
	
	/**
	 * Sums the score of the spaceship after destroying a certain object.
	 * @param plus The value to increment to the final score.
	 */
	public void setTotalScore(int plus)
	{
		totalScore += plus;
	}
	
	/**
	 * Gets the total score of a spaceship.
	 * @return An Integer data type.
	 */
	public int getTotalScore()
	{
		return totalScore;
	}
	
	/**
	 * Sets the vector of shots.
	 * @param s The vector to be set.
	 */
	public void setShots(Vector<Shot> s)
	{
		shots = s;
	}
	
	/**
	 * Gets the vector of shots of a Spaceship.
	 * @return A Vector of Shot objects.
	 */
	public Vector<Shot> getShots()
	{
		return shots;
	}
	
	/**
	 * Gets the inclination of the line.
	 * @return A Double Data type.
	 */
	public double getDeclive()
	{
		return declive;
	}
	
	/**
	 * Gets the Y in the Origin of the line.
	 * @return A Double Data type.
	 */
	public double getOrigin()
	{
		return yInOrigin;
	}
	
	/**
	 * Shapes the Polygon that represents the Spaceship.
	 */
	public void shapingPoints()
	{
		shape = new Polygon();

		shape.addPoint((int) posX+13,(int) posY);
		shape.addPoint((int) posX-13,(int) posY-8);
		shape.addPoint((int) posX-13,(int) posY+8);
	}

	/**
	 * Calculates the trajectory of the spaceship, according to only the angle.
	 */
	public void calculateTrajectoryS()
	{
		double a = Math.toRadians((double) angle);
		declive = Math.tan(a);
		
		yInOrigin = posY - declive*posX;		
	}
}
