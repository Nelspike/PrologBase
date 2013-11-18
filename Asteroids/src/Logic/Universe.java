package Logic;

import java.util.Random;
import java.util.Vector;

public class Universe {

	Spaceship player;
	Command game;
	
	/**
	 * Default Constructor of Universe.
	 */
	public Universe()
	{
		game = new Command();
		player = new Spaceship();
	}
	
	/**
	 * Gets the array of the current players in game.
	 * @return An array of Spaceship objects.
	 */
	public Spaceship getSpaceship()
	{
		return player;
	}
	
	/**
	 * Sets the array of the current players in game.
	 * @param v The array to be set.
	 */
	public void setSpaceship(Spaceship v)
	{
		player = v;
	}
	
	/**
	 * Sets the current level.
	 * @param level The level to be set.
	 */
	public void setLevel(int level)
	{
		game.setLevel(level);
	}
	
	/**
	 * Skips the game to the next level.
	 */
	public void nextLevel()
	{
		game.goToNextLevel();
	}
	
	/**
	 * Gets the array of the enemies alive.
	 * @return
	 */
	public Vector<Enemy> getEnemies()
	{
		return game.getEnemies();
	}
	
	/**
	 * Gets the current Game.
	 * @return The current Game.
	 */
	public Command getGame()
	{
		return game;
	}
	
	public void stepPlayer(char key)
	{
		Vector<Enemy> enemies = game.getEnemies();
		Vector<Shot> currentShots = player.getShots();
		int boost = 1;
		game.moveSpaceShip(key, player, boost);
		verifyShotsFired(key, player, currentShots);
		verifyShotCollisions(enemies, currentShots, player);
	}
	
	public void stepEnemies()
	{
		Vector<Enemy> enemies = game.getEnemies();
		Vector<Shot> currentShots = player.getShots();
		
		game.setEnemies(enemies);
		verifyEnemyMoving(enemies);
		player.setShots(currentShots);
		
		boolean crash = false;
		for(int i = 0; i < enemies.size() && !crash; i++)
		{
			crash = game.crashIntoEnemy(player, enemies.elementAt(i));
		}
	}
	
	public void enemyCreation()
	{
		Random r = new Random();
		int choice = r.nextInt(2);
		String type;
		switch (choice)
		{
			case 0:
				type = "Asteroid";
				game.enemyCreation(type);
				break;
			case 1:
				type = "OVNI";
				game.enemyCreation(type);
				break;
			default:
				break;
		}
	}
	
	public void verifyShotCollisions(Vector<Enemy> enemies, Vector<Shot> currentShots, Spaceship currentPlayer)
	{
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy inVecEnemies = enemies.elementAt(i);
			for(int j = 0; j < currentShots.size(); j++)
			{
				Shot inVecShots = currentShots.elementAt(j);
				game.shotCrashesIntoEnemy(inVecShots, inVecEnemies, currentPlayer, i);
				if(!inVecShots.getAlive())
				{
					currentShots.remove(j);
					j -= 1;
				}
				else
					currentShots.set(j,inVecShots);
				if(!inVecEnemies.getAlive())
					break;
			}
			enemies.set(i, inVecEnemies);
		}
	}
	
	public void verifyShotsFired(char key, Spaceship currentPlayer, Vector<Shot> currentShots)
	{
		game.fireSpaceShip(key, currentPlayer);
		for(int i = 0; i < currentShots.size(); i++)
		{
			Shot inVec = currentShots.elementAt(i);
			if(inVec.getAlive())
			{
				game.moveShots(inVec);
				currentShots.set(i, inVec);
			}
			else
			{
				currentShots.remove(i);
				i -= 1;
			}
		}
	}
	
	public void verifyEnemyMoving(Vector<Enemy> enemies)
	{
		
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy inVec = enemies.elementAt(i);
			if(inVec.getAlive())
			{
				game.moveEnemy(inVec);
				enemies.set(i,inVec);
			}
			else
			{
				enemies.remove(i);
				i -= 1;
			}
		}
	}
	
}
