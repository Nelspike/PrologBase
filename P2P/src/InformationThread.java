import java.io.IOException;

public class InformationThread extends Thread
{
	/**
	 * The client in the network.
	 */
	private Client client;
	
	/**
	 * The constructor for this thread.
	 * @param _client The client to run the thread.
	 * @param name The name of the thread.
	 */
	public InformationThread(Client _client, String name)
	{
		super(name);
		client = _client;
	}

	/**
	 * Overriden function of the Thread Class, that will execute everything that is related to this thread.
	 */
	public void run()
    {
        while(true)
        {
            try
            {
                client.getMessage();
            }
            catch (IOException e)
            {
                System.out.println("Impossible to get message");
                System.exit(0);
            } catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
}