package gui;

import java.awt.Component;
import java.awt.Container;

import javax.swing.*;

public class IntroSpace extends JFrame{

	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;
	private JMenuBar bar;
	private ImageIcon fundo = createImageIcon("images/titleMenu.png", "lulos");
	private JPanel mainPanel;
	private JLabel imageLabel;
	
	/**
	 * Default constructor or IntroSpace.
	 */
	public IntroSpace()
	{
		setTitle("Asteroids - The New Conquest");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);

	}
	
	/**
	 * Adds component to the frame.
	 * @param comp The component to be added.
	 */
	public void addComponent(Component comp)
	{
		add(comp);
	}
	
	/**
	 * Removes all of the contents of the frame.
	 */
	public void clearAll()
	{
		Container cont = getContentPane();
		cont.removeAll();
	}
	
	/**
	 * Sets whether the frame is visible or not.
	 * @param option The visibility term.
	 */
	public void switchVisibility(boolean option)
	{
		setVisible(option);
	}
	
	/**
	 * Sets the JMenuBar into the frame.
	 * @param b
	 */
	public void setBar(JMenuBar b)
	{
		bar = b;
		setJMenuBar(bar);
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
	
	/**
	 * Creates the image label of the main Menu.
	 */
	public void labelChoice()
	{
		mainPanel = new JPanel();
		imageLabel = new JLabel();
		imageLabel.setIcon(fundo);
		mainPanel.add(imageLabel);
		addComponent(mainPanel);
	}
}
