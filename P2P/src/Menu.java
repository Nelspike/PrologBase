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
	public void intialize()
	{
		System.out.println("Welcome to P2P-1337z0rz!\n");
		System.out.println("1 - Search&Get files");
		System.out.println("2 - Check for Files in your Downloads Folder");
		System.out.println("3 - Current Downloads");
		System.out.println("4 - Change Downloads Folder");
		System.out.println("5 - Exit Client\n");
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
