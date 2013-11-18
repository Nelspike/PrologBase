package gui;

import java.awt.*;
import java.util.Vector;
import Logic.*;

import javax.swing.*;

public class drawIntoGame extends JPanel{

	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;

	private Spaceship s;
	private Vector<Enemy> en;
	private Vector<Shot> sh;
	private ImageIcon fundo = createImageIcon("images/night-sky.jpg", "lulos");
	private Image buffered = fundo.getImage();
	private long time = 0;
	private int level = 0;
	
	/**
	 * Default constructor of class drawIntoGame.
	 * @param _s The current spaceship.
	 * @param _en The current enemies.
	 * @param _sh The current shots.
	 */
	public drawIntoGame(Spaceship _s, Vector<Enemy> _en, Vector<Shot> _sh) 
	{
		super();
		s = _s;
		en = _en;
		sh = _sh;
	}
	
	/**
	 * Overrides the paintComponent function of JPanel.
	 */
	@Override
	public void paintComponent(Graphics g)
	{

		super.paintComponent(g);
		g.drawImage(buffered, 0, 0, this);
		g.setColor(Color.BLACK);
    
		for(int i = 0; i < en.size(); i++)
		{
			Enemy inVec = en.elementAt(i);
			Polygon eShape = inVec.getShape();
			g.setColor(Color.BLACK);
			g.drawPolygon(eShape);
			g.setColor(Color.WHITE);
			g.fillPolygon(eShape);
		}
		
		for(int i = 0; i < sh.size(); i++)
		{
			Shot inVec = sh.elementAt(i);
			Polygon shShape = inVec.getShape();
			g.setColor(Color.BLACK);
			g.drawPolygon(shShape);
			g.setColor(Color.WHITE);
			g.fillPolygon(shShape);
		}
		
		Font f = new Font("Calibri", Font.BOLD, 25);
		g.setColor(Color.WHITE);
		g.setFont(f);
		int points = s.getTotalScore();
		String pointsToDraw = "Your score - " + points;
		String timeToDraw = "Time in Level " + level + "- " + time;
		String levelToDraw = "Level - "+ level;
		g.drawString(pointsToDraw, 10, 25);
		g.drawString(timeToDraw, 230, 25);
		g.drawString(levelToDraw, 525, 25);
		
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.translate(s.getX(), s.getY());
	    g2d.rotate(Math.toRadians((double) s.getAngle()));
	    g2d.translate(-s.getX(), -s.getY());
	    Polygon shape = s.getShape();
	    g.setColor(Color.BLACK);
	    g.drawPolygon(shape);
	    g.setColor(Color.WHITE);
	    g.fillPolygon(shape);
	}
	
	/**
	 * Sets spaceship.
	 * @param _s The new spaceship.
	 */
	public void setSpaceship(Spaceship _s)
	{
		s = _s;
	}
	
	/**
	 * Sets enemies.
	 * @param _en The new vector of enemies.
	 */
	public void setEnemies(Vector<Enemy> _en)
	{
		en = _en;
	}
	
	/**
	 * Sets shots.
	 * @param _sh The new vector of shots.
	 */
	public void setShots(Vector<Shot> _sh)
	{
		sh = _sh;
	}
	
	/**
	 * Sets time.
	 * @param t The time to be set.
	 */
	public void setTime(long t)
	{
		time = t;
	}
	
	/**
	 * Sets level.
	 * @param l The level to be set.
	 */
	public void setLevel(int l)
	{
		level = l;
	}
	
	/**
	 * Creates an image from a file.
	 * @param path The file's path.
	 * @param description The image description
	 * @return An ImageIcon.
	 */
	protected ImageIcon createImageIcon(String path, String description) 
	{
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) 
		{
			return new ImageIcon(imgURL, description);
		} 
		else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
