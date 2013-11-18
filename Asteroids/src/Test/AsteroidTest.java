package Test;

import java.util.Vector;

import org.junit.Test;

import Logic.*;
import static org.junit.Assert.*;

public class AsteroidTest {

	private Universe testField = new Universe();
	private Command c = new Command();
	public AsteroidTest() {}
	
	@Test
	public void moveSpaceShipLeft()
	{
		Spaceship s = new Spaceship();
		c.moveSpaceShip('w', s, 1);
		
		assertEquals((int)325, (int)s.getX());
	}
	
	@Test
	public void shootSpaceShip()
	{
		Spaceship s = new Spaceship();
		assertEquals(true, c.fireSpaceShip((char) 32, s));
	}

	@Test
	public void dieSpaceShip()
	{
		Spaceship s = new Spaceship();
		OVNI e = new OVNI(0, 0);
		
		s.setX(320);
		s.setY(320);
		e.setX(320);
		e.setY(320);
		
		assertEquals(true, c.crashIntoEnemy(s,e));
		assertEquals(false, s.getAlive());
	}
	
	@Test
	public void moveEnemy()
	{
		c.enemyCreation("Asteroid");
		Vector<Enemy> en = c.getEnemies();
		//System.out.println(en.size());
		Enemy e = en.elementAt(0);
		double currentX = e.getX();
		double currentY = e.getY();

		System.out.println("MOVE ENEMY");
		c.moveEnemy(e);
		
		assertEquals(false, (currentX != e.getX()) || (currentY != e.getY()));
	}
	
	
	@Test
	public void killAsteroidWithShot()
	{
		setTestField(new Universe());
		Asteroid a = new AsteroidSmall();
		Spaceship s = new Spaceship();
		s.setAngle(45);
		boolean intersect;
		s.setX(100);
		s.setY(100);
		a.setX(200);
		a.setY(200);
		
		Shot testShot = new Shot(s);
		
		testShot.setX(s.getX());
		testShot.setY(s.getY());
		intersect = testShot.shotIntersectsEnemy(a);
		assertEquals(true, intersect);
	}
	
	@Test
	public void divideAsteroid()
	{
		setTestField(new Universe());
		Asteroid a = new AsteroidMedium();
		Vector<Enemy> en = c.getEnemies();
		en.clear();
		en.add(a);
		c.setEnemies(en);
		c.divideAsteroid(a);
		
		assertEquals(4, c.getEnemies().size());

	}
	
	@Test
	public void scoreUpAfterEnemy()
	{
		Spaceship s = new Spaceship();
		Enemy e = new AsteroidLarge();
		c.incrementSpaceShipScore(s, e.getScore()*1);
		
		assertEquals(200,s.getTotalScore());
	}
		

	@Test
	public void goToNextLevel()
	{
		Stopwatch s = new Stopwatch();
		Command c = new Command();
		c.setLevel(1);
		s.start();
		
		while(s.getElapsedTimeSecs() != 3);
		s.stop();
		
		c.goToNextLevel();
		
		assertEquals(2, c.getLevel());
	}

	/**
	 * Sets the Test field.
	 * @param testField The field to be set.
	 */
	public void setTestField(Universe testField) {
		this.testField = testField;
	}

	/**
	 * Gets the Test Field.
	 * @return A Universe Object.
	 */
	public Universe getTestField() {
		return testField;
	}

}
