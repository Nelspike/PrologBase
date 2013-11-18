package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class GameSpace extends JFrame {

	/**
	 * The Serial Version UID.
	 */
	private static final long serialVersionUID = 1L;
	private File soundFile = new File("src/maverick.wav");
	private Clip clip;
	private char listeningKey;
	
	/**
	 * Default constructor of GameSpace.
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 * @throws LineUnavailableException 
	 */
	public GameSpace() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		setTitle("Crush'em all!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(640, 640);
		invokeKeyListeners();
		
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

		clip = AudioSystem.getClip();

		clip.open(audioIn);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
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
	 * Creates the Key Listeners.
	 */
	public void invokeKeyListeners()
	{
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) {
				listeningKey = e.getKeyChar();
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		}
		);
	}
	
	/**
	 * Returns the key that has been pressed.
	 * @return A Character data type.
	 */
	public char getListeningKey()
	{
		return listeningKey;
	}
	
	/**
	 * Sets the key that has been pressed.
	 * @param key The key to be set.
	 */
	public void setListeningKey(char key)
	{
		listeningKey = key;
	}
	
	/**
	 * Stops the game sound.
	 */
	public void stopAudio()
	{
		clip.stop();
	}
	
}
