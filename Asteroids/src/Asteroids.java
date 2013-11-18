import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import gui.*;

public class Asteroids {

	/**
	 * The main function.
	 * @param args Default arguments.
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		new MainSpace();
	}
}
