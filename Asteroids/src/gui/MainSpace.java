package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

public class MainSpace {

	public JFrame scoresFrame;
	public JMenuBar menu;
	public JPanel menuPanel, gPanel;
	public drawIntoGame gamePanel;
	public JLabel introLabel, gameLabel;
	public JMenu fileMenu, about;
    public JMenuItem newGame, exitMenu, aboutMenu;
    private Vector<Integer> hScores = new Vector<Integer>();
    private Comparator<Integer> comparator = Collections.reverseOrder();
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JButton jButton1, jButton2;
    private int score;
    private AsteroidsSpace space;
    private boolean pressed;
    private IntroSpace introFrame;
	
    /**
     * Default constructor of MainSpace.
     * @throws LineUnavailableException 
     * @throws IOException 
     * @throws UnsupportedAudioFileException 
     */
	public MainSpace() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		//frame principal
		scoresFrame = new JFrame("Hall of Fame");
		initComponents();
		scoresFrame.setVisible(false);
		scoresFrame.setLocation(700, 50);
		
		pressed = false;
		introFrame = new IntroSpace();
		makeMenu();
		introFrame.setBar(menu);
		introFrame.labelChoice();
		whileInMain();
	}

	/**
	 * Starts a new Game. 
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void startGame() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		pressed = false;
		space = new AsteroidsSpace();
		scoresFrame.setVisible(true);
		score = space.getScore();
		whileInMain();
	}
	
	/**
	 * Initializes all of the components of MainSpace.
	 */
	public void initComponents()
	{
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        
        jButton1.addActionListener(new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				hScores.add(score);
				
				Collections.sort(hScores,comparator);
				
				for(int i = 0; i < hScores.size(); i++)
				{
					if(i < 10)
						jTable1.setValueAt(hScores.elementAt(i),i,0);
					else break;
				}
        	
			}
        });
        
        jButton2.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent evt) {
                scoresFrame.setVisible(false);
            }
        });
        
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Score"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Add Score!");
        jButton2.setText("Go to Main Menu");
 

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(scoresFrame.getContentPane());
        scoresFrame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );

        scoresFrame.pack();
	}
	
	/**
	 * Gets the HIgh Scores Frame visible.
	 */
	public void readHighScores()
	{
		scoresFrame.setVisible(true);
		
	}
	
	/**
	 * Function that wait for a menu/button to be pressed.
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void waitForSignal() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		while(!pressed){};
		startGame();
	}
	
	/**
	 * Creates the Menu bar and its components.
	 */
	public void makeMenu()
	{
		fileMenu = new JMenu("Game");
		about = new JMenu("Ranks");
		menu = new JMenuBar();
		newGame = new JMenuItem("New Game");
		exitMenu = new JMenuItem("Exit");
		aboutMenu = new JMenuItem("High Scores");
		
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				introFrame.switchVisibility(false);
				pressed = true;
			}
			
		});
		
		exitMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				introFrame.switchVisibility(false);
				System.exit(0);
			}
			
		});
		
		aboutMenu.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scoresFrame.setVisible(true);
			}
			
		});
		
		fileMenu.add(newGame);
		fileMenu.add(exitMenu);
		about.add(aboutMenu);
		
		menu.add(fileMenu);
		menu.add(about);
	}
	
	/**
	 * Function that decides what to do while in the main menu.
	 * @throws LineUnavailableException 
	 * @throws IOException 
	 * @throws UnsupportedAudioFileException 
	 */
	public void whileInMain() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{
		introFrame.switchVisibility(true);
		waitForSignal();
	}
}
