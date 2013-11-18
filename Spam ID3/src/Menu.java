import java.util.Scanner;

public class Menu 
{
	/**
	 * Default constructor or Menu.
	 */
	public Menu() {}
	
	/**
	 * Prints out the menu.
	 */
	public void showMenu()
	{
		System.out.println("Welcome to Spam Detector");
		System.out.println("ID3 and C4.5 Spam Detection System\n");
		System.out.println("1 - Use a small spambase to check a Message");
		System.out.println("2 - Use a medium spambase to check a Message");
		System.out.println("3 - Use a big spambase to check a Message");
		System.out.println("4 - Exit Client\n");
	}
	
	/**
	 * Input an option in the command line.
	 * @return The option input in an integer format.
	 */
	public int inputOption()
	{
		System.out.print("Choose one: ");
		Scanner input = new Scanner(System.in);
		String option = input.nextLine();
		
		int intOption = -1;
		
		try
		{
			intOption = Integer.parseInt(option);
		}
		catch(Exception e)  
		{  
			inputOption();
		}
		
		return intOption;
	}	
}
