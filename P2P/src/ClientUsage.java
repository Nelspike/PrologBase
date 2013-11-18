import java.io.IOException;


public class ClientUsage 
{
	/**
	 * The client to be ran.
	 */
	private Client infoPart;
	
	/**
	 * The menu of the application.
	 */
	private Menu menu;
	
	/**
	 * Default constructor of ClientUsage - the main class of the program that engages everything else.
	 */
	public ClientUsage()
	{
		try {
			infoPart = new Client("224.0.2.10");
		} catch (Exception e) {
			e.printStackTrace();
		}
		menu = new Menu();
		startInfoThread();
	}
	
	/**
	 * Starts the information thread, thread that will always be waiting for new messages.
	 */
	public void startInfoThread()
	{
     	InformationThread infoThread = new InformationThread(infoPart, "InfoRecieve");
     	infoThread.start();
	}
	
	/**
	 * The main cycle of the program.
	 */
	public void mainCycle()
	{
		for(;;)
		{
			menu.intialize();
			int option = menu.inputOption();
			try {
				checkOption(option);
			} catch (IOException e) {
				System.out.println("Can't send message!");
			}
		}
	}
	
	/**
	 * Checks which option has been picked and performs an action.
	 * @param option The option picked.
	 * @throws IOException
	 */
	public void checkOption(int option) throws IOException
	{
		switch(option)
		{
			case 1:
				infoPart.doSearch();				
				break;
			case 2:
				infoPart.checkDownloadsFolder();
				break;
			case 3:
				infoPart.checkDownloadsProgress();
				break;
			case 4:
				infoPart.changeFolder();
				break;
			case 5:
				disconnect();
				break;
			default:
				break;
		}
	}
	
	/**
	 * Disconnects from the program.
	 */
	private void disconnect()
	{
		infoPart.clearScreen();
		System.out.println("See you next time!");
		System.exit(0);
	}
}
