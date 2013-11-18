package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

import Logic.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class AsteroidsSpace{

	public GameSpace gameFrame;
	public JInternalFrame scoresFrame;
	public JMenuBar menu;
	public JPanel menuPanel, gPanel;
	public drawIntoGame gamePanel;
	public JLabel introLabel, gameLabel;
    public JMenuItem newGame, exitMenu, aboutMenu;
    private Universe gameField;
    private Spaceship player;
    private Vector<Enemy> enemies;
    private Vector<Shot> shots;
    private int level, score;
    private Timer timerPlayer, timerEnemies, enemyC;
    private long time = 0;
    private Stopwatch clock  = new Stopwatch();
    
    /**
     * Default constructor of AsteroidsSpace.
     * @throws LineUnavailableException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     */
	public AsteroidsSpace() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		level = 1;
		gameCycle();
	}

	/**
	 * Initializes the frame's timers.
	 */
	public void makeTimers()
	{
		timerPlayer = new Timer(5, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				gameField.stepPlayer(gameFrame.getListeningKey());
				gameFrame.setListeningKey('0');
				player = gameField.getSpaceship();
				shots = player.getShots();
				
				recalculateShaping();
				time = clock.getElapsedTimeSecs();
				
				gamePanel.setLevel(level);
				gamePanel.setTime(time);
				gamePanel.setShots(shots);
				gamePanel.repaint();
			}

		});
		
		timerEnemies = new Timer(50, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				gameField.stepEnemies();
				enemies = gameField.getEnemies();
				shots = player.getShots();
				recalculateShaping();
				gamePanel.setEnemies(enemies);
				gamePanel.setShots(shots);
				gamePanel.repaint();
			}
		});

		enemyC = new Timer(2500/level, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				gameField.enemyCreation();				
			}
		});
		
	}
	
	/**
	 * Game's main cycle. Ends if spaceship dies.
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void gameCycle() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		makeTimers();
		gameField = new Universe();
		gameField.setLevel(level);
		
		gameFrame = new GameSpace();
		player = gameField.getSpaceship();
		enemies = gameField.getEnemies();
		shots = player.getShots();
		
		gamePanel = new drawIntoGame(player, enemies, shots);
		gameFrame.addComponent(gamePanel);
		gameFrame.switchVisibility(true);
		gamePanel.repaint();

		clock.start();
		timerPlayer.start();
		timerEnemies.start();
		enemyC.start();
		
		while(true)
		{
			player = gameField.getSpaceship();
			if(!player.getAlive())
				break;
			
			if(clock.getElapsedTimeSecs() == 30*level)
			{
				clock.stop();
				level++;
				gameField.nextLevel();
				clock.start();
			}
		}
		
		clock.stop();
		timerPlayer.start();
		timerEnemies.stop();
		enemyC.stop();
		score = player.getTotalScore();
		gameFrame.switchVisibility(false);
		gameFrame.stopAudio();
		gameFrame.clearAll();
	}
	
	/**
	 * Re-draw function.
	 */
	private void recalculateShaping()
	{
		player.shapingPoints();
		
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy inVec = enemies.elementAt(i);
			if(inVec.getType().equals("Asteroid"))
			{
				Asteroid a = (Asteroid) inVec;
				if(a.getTypeAst().equals("large"))
				{
					AsteroidLarge aL = (AsteroidLarge) a;
					aL.shapingPoints();
					inVec = (Enemy) aL;
				}
				else if(a.getTypeAst().equals("medium"))
				{
					AsteroidMedium aM = (AsteroidMedium) a;
					aM.shapingPoints();
					inVec = (Enemy) aM;
				}
				else
				{
					AsteroidSmall aS = (AsteroidSmall) a;
					aS.shapingPoints();
					inVec = (Enemy) aS;
				}	
			}
			else
			{
				OVNI o = (OVNI) inVec;
				o.shapingPoints();
				inVec = (Enemy) o;
			}
			
			enemies.set(i,inVec);
		}
		
		for(int i = 0; i < shots.size(); i++)
		{
			Shot inVec = shots.elementAt(i);
			inVec.shapingPoints();
			shots.set(i,inVec);
		}
		
	}
	
	/**
	 * Gets the final score of the spaceship.
	 * @return An Integer Data type.
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * Destroys one object of AsteroidsSpace.
	 */
	public void finalize()
	{
	        try {
				super.finalize();
			} catch (Throwable e) {
				System.out.println("Lol");
				e.printStackTrace();
			}

	}
}
