import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class Client
{
	/**
	 * The Multicast Socket that is used for the Information Control.
	 */
    private MulticastSocket mSocket;
    
    /**
     * The Muticast socket group address.
     */
    private String groupAddress;
    
    /**
     * The port that will be used to connect to the Multicast group.
     */
    private int port;
   
    /**
     * The DatagramPacket that will be used to transfer byts within the network.
     */
    private DatagramPacket packet;
    
    /**
     * The files' data that is to be located inside the "Downloads" folder.
     */
    private ArrayList<FileData> filesData;
    
    /**
     * The files' data that has been found via a Search within the Network.
     */
    private ArrayList<FileData> foundFiles;
    
    /**
     * The files' data that is active downloading right now.
     */
    private ArrayList<FileData> currentDownloads;
    
    /**
     * The Search ID used for a certain search in the network.
     */
    private long searchID;
    
    /**
     * The path to the "Downloads" folder.
     */
    private String filePath;
    
    /**
     * A global variable used to help the user picking one of the files within its search.
     */
    private int fileIndex;
    
    /**
     * A Boolean Array with the information whether the file has been received in its totality or not.
     */
    private ArrayList<Boolean> chunkIndexes;
    
    /**
     * The key that has been pressed on the keyboard.
     */
    private char keyPressed;
    
    /**
     * Client constructor.
     * @param group The group's address in a String.
     * @throws Exception
     */
    public Client(String group) throws Exception
    {
    	setGroupAddress(group);
    	setPort(8967);
    	filePath = "C:/SDISDownloads/";
    	filesData = new ArrayList<FileData>();
    	foundFiles = new ArrayList<FileData>();
    	currentDownloads = new ArrayList<FileData>();
    	chunkIndexes = new ArrayList<Boolean>();
    	byte[] message = new byte[256];
        packet = new DatagramPacket(message, message.length);
    	getFiles(filePath);
    	groupAddress = "224.0.2.10";
    	searchID = 0;
    	fileIndex = 1;
    	connectToMulticastPort();
    }
    
    /**
     * Default Constructor of Client.
     */
    public Client() { }

    /**
     * Connects to the Multicast Port that has been specified.
     * @throws UnknownHostException
     * @throws IOException
     */
	public void connectToMulticastPort() throws UnknownHostException, IOException
	{
		try {
			setMulticastSocket(new MulticastSocket(getPort()));
		} catch (IOException e) {
			System.err.println("Can't connect to Multicast Port " + getPort() + "...");
		}
		 mSocket.joinGroup(InetAddress.getByName(groupAddress));
	}
    
	//Send and Receive Messages
	
    /**
     * Sends a message to the multicast group.
     * @param message The message to be sent.
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException
    {
        DatagramPacket toSend = new DatagramPacket(message.getBytes(), message.getBytes().length, InetAddress.getByName(groupAddress), port);
        mSocket.setTimeToLive(1);
        mSocket.send(toSend);
    }
   
    /**
     * Waits for a message pending in the multicast group.
     * @throws Exception
     */
	public void getMessage() throws Exception
	{
		mSocket.receive(packet);
		String recieved = "";
		if(packet.getData().length != 0) recieved = new String(packet.getData(), 0, packet.getLength());
		    
		String[] messageChunks = recieved.split(" ");
		int nChunks = messageChunks.length;
		if(messageChunks[0].equals("SEARCH"))
		{
			String sID = messageChunks[1];
			String id  = sID.substring(2);
			if(Long.parseLong(id) != searchID)
			{
				getFiles(filePath);
				ArrayList<Integer> fileList = searchInArrayList(formSearchTerm(nChunks-2, messageChunks, 2));
				if(fileList.size() != 0)
				{
					foundCommand(fileList, sID);
				}
			}
		}
		else if(messageChunks[0].equals("FOUND"))
		{
			String sID = messageChunks[1];
			String id  = sID.substring(2);
			if(Long.parseLong(id) == searchID)
			{
				if(!fileHasBeenFound(messageChunks[2]))
				{
					String filename = formSearchTerm(nChunks-4, messageChunks, 4);
					long byteSize = (long) Math.ceil((double)Integer.parseInt(messageChunks[3]));
					System.out.println(fileIndex + " - " + filename + " / " + Math.ceil(byteSize/1024.0) + "KB");
					FileData fd = new FileData();
					fd.setName(filename);
					fd.setCheckSum(messageChunks[2]);
					fd.setnPeers(1);
					fd.setSize(byteSize);
					foundFiles.add(fd);
					fileIndex++;
				}
				else
				{
					foundFiles.get(fileIndex-2).augmentPeers();
				}
			}
		}
		else if(messageChunks[0].equals("GET"))
		{
			String[] chunksToSendString = null;
			ArrayList<Long> chunksToSend = new ArrayList<Long>();
			
			int exist = fileExists(messageChunks[1]);
			
			if(exist == -1)
			{
				return;
			}
			
			//Format "1,2,3,4,5,6"
			if(messageChunks[2].matches("(?i).*,.*"))
			{
				 chunksToSendString = messageChunks[2].split(",");
				
				for(int i = 0; i < chunksToSendString.length; i++)
				{
					//Format "1-6" inside commas
					if(chunksToSendString[i].matches("(?i).*-.*"))
					{
						String[] rangedChunks = chunksToSendString[i].split("-");
						int start = Integer.parseInt(rangedChunks[0]);
						int end = Integer.parseInt(rangedChunks[1]);

						for(int j = start; j <= end; j++) chunksToSend.add((long)j);
					}
					else chunksToSend.add(Long.parseLong(chunksToSendString[i]));
				}
			}
			//Format "1-6"
			else if (messageChunks[2].matches("(?i).*-.*"))
			{
				chunksToSendString = messageChunks[2].split("-");
				int start = Integer.parseInt(chunksToSendString[0]);
				int end = Integer.parseInt(chunksToSendString[1]);

				for(int i = start; i <= end; i++) chunksToSend.add((long)i);
			}
			else if(messageChunks[2].matches("[0-9]*"))
			{
				chunksToSend.add(Long.parseLong(messageChunks[2]));
			}
			
			DataThread dataThread = new DataThread(this, "DataThread", "Sending", groupAddress);
			byte[] sha256 = calculateSHAFromHash(messageChunks[1]);
			dataThread.setSha256(sha256);
			dataThread.setFile(filesData.get(exist).getFile());
			dataThread.setChunksToSend(chunksToSend);
			dataThread.start();
		}
	}
	
	/**
	 * Check if file with "checkSum" exists in this computer
	 * @param checkSum The checkSum itself.
	 * @return True if exists, False otherwise
	 */
	public int fileExists(String checkSum)
	{
		for(int i = 0; i < filesData.size(); i++)
		{
			if(filesData.get(i).getCheckSum().equals(checkSum))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Check if file with "hash" exists in the found files list.
	 * @param hash The hash itself.
	 * @return A boolean confirming such.
	 */
	public boolean fileHasBeenFound(String hash)
	{
		for(int i = 0; i < foundFiles.size(); i++)
		{
			if(foundFiles.get(i).getCheckSum().equals(hash))
			{
				return true;
			}
		}
		return false;
	}
    
	//Command Processing Area
	
	/**
	 * Constructs the "FOUND" command inside the Client.
	 * @param list ArrayList with the integer positions of the files in the "Downloads" folder.
	 * @param sID The search ID that has been used for the search.
	 */
    public void foundCommand(ArrayList<Integer> list, String sID)
    {
    	for(int i = 0; i < list.size(); i++)
    	{
    		String toSend = "FOUND " + sID + " " + filesData.get(list.get(i)).getCheckSum() + " "
    					    + filesData.get(list.get(i)).getSize() + " " + filesData.get(list.get(i)).getName();
    		try {
				sendMessage(toSend);
			} 
    		catch (IOException e) 
			{
				System.out.println("Cannot send FOUND message!");
			}
    	}
    }
    
    /**
     * Forms the search command that will be sent to the multicast port to make a search within the P2P network.
     * @param searchMethod
     * @return A String with the message formed.
     */
	public String formSearchMessage(String searchMethod)
	{
		Random r = new Random();
		searchID = r.nextInt(1000000000);
		String result = "SEARCH " + "id" + searchID;
		result += " " + searchMethod;
		return result;		
	}
	
	/**
	 * Forms the string of the search input.
	 * @param size Number of "String-chunks" needed to form a Search Input. 
	 * @param chunks The "String-Chunks".
	 * @param index Starting Index of the chunks in the string.
	 * @return The Search Input in a String.
	 */
	public String formSearchTerm(int size, String[] chunks, int index)
	{
		String result = "";
		
		while(size > 0)
		{
			result += chunks[index];
			index++;
			size--;
		}
		
		return result;
	}
	
	/**
	 * Searches the file in the available files in the directory.
	 * @param name The terms to Search in the folder.
	 */
	public void searchFile(String name)
	{
		String searchMessage = formSearchMessage(name);
		try {
			sendMessage(searchMessage);
		} catch (IOException e) {
			System.out.println("Cannot send your Search Method!");
		}
	}
	

	//Search Methods
	
	/**
	 * Searches for a specific term in the ArrayList that contains all the infos of the files.
	 * @param search Term to be searched.
	 * @return An ArrayList of Integers that contains all the indexes of the files in the original Array that have matched with any term of the Search Input.
	 */
	public ArrayList<Integer> searchInArrayList(String search)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0; i < filesData.size(); i++)
		{
			FileData fd = filesData.get(i);
			String fileName = fd.getName();
			if(checkFileName(fileName, search)) result.add(i);
		}
		
		return result;
	}
	
	/**
	 * Checks if any of the chunks specified on the previous String match with the filename chunks.
	 * @param name The name of the file.
	 * @param searchMethod The search term.
	 * @return A boolean saying if they matched or not.
	 */
	public boolean checkFileName(String name, String searchMethod)
	{	
		String[] searchChunked = searchMethod.split(" ");
		name.toUpperCase();
		
		for(int i = 0; i < searchChunked.length; i++)
		{
			searchChunked[i].toUpperCase();
			if(name.matches("(?i).*" + searchChunked[i] + ".*"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Engages a search in the Client.
	 * @throws IOException 
	 */
	public void doSearch() throws IOException
	{
		clearScreen();
		System.out.println("What do you wish to look for? ");
		Scanner input = new Scanner(System.in);
		searchFile(input.nextLine());
		clearScreen();
		System.out.println("Escolha um dos ficheiros (0 para cancelar)\n");
		int choice = -1;
		while(choice == -1) choice = chooseFile();

		if(choice == 0)
		{
			foundFiles.clear();
			fileIndex = 1;
			return;
		}
		
		searchID = 0;
		FileData chosenFile = foundFiles.get(choice-1);
		currentDownloads.add(chosenFile);
		long fileSize = getFileSizeFromHash(chosenFile.getCheckSum());
		formChunkList(fileSize);
		String path = new String();
		path = filePath + chosenFile.getName();
		
		long size = chosenFile.getSize();
		File f = new File(path);
		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		raf.setLength(size);
		
		int nChunks = (int) Math.ceil(size/1024.0);
			
		//Call threads to receive all the data
		
		for(int i = 1; i <= chosenFile.getnPeers(); i++)
		{
			byte[] reqSha = calculateSHAFromHash(chosenFile.getCheckSum());
			DataThread dataThread = new DataThread(this, chosenFile.getCheckSum()+"-"+i, "Recieving", groupAddress);
			dataThread.setFileSize((int)size);
			dataThread.setFileToMake(raf);
			dataThread.setFoundFile(chosenFile);
			dataThread.setnChunks(nChunks);
			dataThread.setRequestedHash(reqSha);

			//downloads in background
			dataThread.start();
		}

		foundFiles.clear();
		fileIndex = 1;
		clearScreen();
	}

	/**
	 * Chooses a file from the given Search List.
	 * @return The choice itself.
	 */
	public int chooseFile()
	{
		Scanner number = new Scanner(System.in);
		String input = number.nextLine();
		if(isInteger(input))
			return Integer.parseInt(input);
		else
			return -1;
	}
	
	/**
	 * Forms the get messages that are sent in case the user wants a file.
	 * @param fs The file data of the file to be transfered.
	 * @param first The first chunk to be asked.
	 * @param last The last chunk to be asked.
	 * @return The stringified message.
	 */
	public String formGetMessage(FileData fs, long first, long last)
	{
		String ret = "GET " + fs.getCheckSum() + " " + first + "-" + last;
		return ret;
	}
	
	/**
	 * Goes to the files list and returns the size of the file in bytes.
	 * @param hash The hash code to pass as argument.
	 * @return The size or -1 if unsuccessful.
	 */
	public long getFileSizeFromHash(String hash)
	{
		for(int i = 0; i < foundFiles.size(); i++)
		{
			if(foundFiles.get(i).getCheckSum().equals(hash))
				return (long) Math.ceil((double)foundFiles.get(i).getSize()/1024.0);
		}
		
		return -1;
	}
	
	//File operations
	
	/**
	 * Gets all the information of the files inside the "Downloads" directory.
	 * @param _path The "Downloads" folder path.
	 * @throws Exception
	 */
	public void getFiles(String _path) throws Exception
	{
		filesData.clear();
		
		File f = new File(_path);
		
		if(!f.exists()) f.mkdir();
		
		File[] files = f.listFiles();
		
		if(files.length != 0)
		{
			for(int i = 0; i < files.length; i++)
			{
				if(!files[i].isDirectory())
				{
					//Atributos de um ficheiro
					String name = files[i].getName();
					byte[] sha256 = calculateSHA(files[i].getAbsolutePath(), "SHA-256");
					String sha = checkSumString(sha256);
					long size = (long) Math.ceil((double) files[i].length());
					
					FileData fileData = new FileData(name, sha, size, files[i], sha256);
					
					filesData.add(fileData);
				}
			}
		}
	}
	
	/**
	 * Calculates the SHA256 for each file.
	 * @param filePath The path to the file.
	 * @return The byte array with all of the information with the SHA256. 
	 * @throws Exception
	 */
	public byte[] calculateSHA(String filePath, String mode) throws Exception
	{
		
		MessageDigest md = MessageDigest.getInstance(mode);
		FileInputStream fis = new FileInputStream(filePath);
		byte[] dataBytes = new byte[1024];
		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);	
		};
	
		byte[] mdbytes = md.digest();
		
		return mdbytes;
	}
	
	/**
	 * Converts the SHA from a String to a Byte Array.
	 * @param hash The String with the SHA.
	 * @return A Byte Array with the same information.
	 */
	public byte[] calculateSHAFromHash(String hash) 
	{
		int length = hash.length();
		if (length % 2 == 1) {
			hash = "0" + hash;
			length++;
		}
		byte[] number = new byte[length / 2];

		int i;
		for (i = 0; i < hash.length(); i += 2) {
			int j = Integer.parseInt(hash.substring(i, i + 2), 16);
			number[i / 2] = (byte) (j & 0x000000ff);
		}
		return number;
	}
	
	/**
	 * Converts the SHA from a Byte Array to a String.
	 * @param buf The Byte Array in question.
	 * @return The String with the respective Information.
	 */
	public String checkSumString(byte[] buf)
	{
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < buf.length; i++) {
			sb.append(Integer.toString((buf[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();		
	}
	
	//Tratamento de chunks
	
	/**
	 * Forms the Boolean Array according to the number of chunks to be sent/received.
	 * @param size The number of chunks.
	 */
	public void formChunkList(long size)
	{
		while(size != 0)
		{
			chunkIndexes.add(false);
			size--;
		}
	}

	/**
	 * Checks if the boolean array is filled with true values or not.
	 * @return The result in a boolean value.
	 */
	public boolean checkBooleanArray()
	{
		for(int i = 0; i < chunkIndexes.size(); i++)
		{
			if(!chunkIndexes.get(i)) return true;
		}
		
		return false;
	}
	
    //Getters and Setters
    
	/**
	 * Sets the MulticastSocket within this Client.
	 * @param _s The MulticastSocket to be set.
	 */
    public void setMulticastSocket(MulticastSocket _s)
    {
    	mSocket = _s;
    }
    
    /**
     * Gets the MulticastSocket within this Client.
     * @return The MulticastSocket.
     */
    public MulticastSocket getMulticastSocket()
    {
    	return mSocket;
    }
    
    /**
     * Sets the GroupAddress within this Client.
     * @param _group The Address itself, in a String Object.
     */
    public void setGroupAddress(String _group)
    {
    	groupAddress = _group;
    }
    
    /**
     * Gets the GroupAddress within this Client.
     * @return The GroupAddress in a String Object.
     */
    public String getGroupAddress()
    {
    	return groupAddress;
    }
    
    /**
     * Sets the Port for the connection.
     * @param _port The number of the port.
     */
    public void setPort(int _port)
    {
    	port = _port;
    }
    
    /**
     * Gets the port for the connection.
     * @return The number of the port.
     */
    public int getPort()
    {
    	return port;
    }
   
    /**
     * Gets the boolean array with all the information relatively to the file status.
     * @return The Boolean Array itself.
     */
	public ArrayList<Boolean> getBooleanArray()
	{
		return chunkIndexes;
	}
	
	/**
	 * Sets the Boolean Array.
	 * @param a The Boolean Array to be set.
	 */
	public void setBooleanArray(ArrayList<Boolean> a)
	{
		chunkIndexes = a;
	}
	
	//Check if it's an Integer
	
	/**
	 * Check if a certain string is an integer.
	 * @param input The string to verify.
	 * @return A boolean confirming such.
	 */
	public boolean isInteger( String input )  
	{  
	   try  
	   {  
	      Integer.parseInt(input);  
	      return true;  
	   }  
	   catch(Exception e)  
	   {  
	      return false;  
	   }  
	} 
	
	/**
	 * Clears the screen with a series of newlines.
	 */
	public void clearScreen()
	{
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
		System.out.println('\n');
	}
    
	//Downloads Progress
	
	/**
	 * Checks the download's progress, and gives an output regarding it.
	 */
	public void checkDownloadsProgress()
	{
		keyPressed = 0x0;
		clearScreen();
		if(currentDownloads.size() == 0)
		{
			System.out.println("Nothing is downloading right now!");
			waitForEnter();
			clearScreen();
			return;
		}
		System.out.println("Current Downloads:\n");
		Thread[] threads = new Thread[256];
		threads = getRunningThreads();
		int count = 0;
		for(int i = 0; i < currentDownloads.size(); i++)
		{
			int lastPos = checkForHashOnThreads(currentDownloads.get(i).getCheckSum(), threads);
			DataThread t = null;
			if(lastPos == -1) currentDownloads.remove(i);
			else t = (DataThread) threads[lastPos];
			if(t != null)
			{
				if(t.isAlive()) 
				{
					t.getProgress(currentDownloads.get(i).getName());
					count++;
				}
			}
		}
		if(count == 0)
		{
			System.out.println("Nothing is downloading right now!");
			waitForEnter();
			clearScreen();
			return;
		}
		waitForEnter();
		clearScreen();
	}
	
	/**
	 * Gets all the threads that are running in this current JVM.
	 * Idea taken from: http://tinyurl.com/threadstrategy
	 * @return An Array with all of the Thread Objects.
	 */
	public Thread[] getRunningThreads()
	{
		ThreadGroup rootGroup = Thread.currentThread( ).getThreadGroup( );
		ThreadGroup parentGroup;
		while ( ( parentGroup = rootGroup.getParent() ) != null ) {
		    rootGroup = parentGroup;
		}
		
		Thread[] threads = new Thread[ rootGroup.activeCount() ];
		while ( rootGroup.enumerate( threads, true ) == threads.length ) {
		    threads = new Thread[ threads.length * 2 ];
		}
		
		return threads;
	}
	
	/**
	 * Checks whether the hash value of a file is in any thread's name.
	 * @param hash The hash to be verified.
	 * @param ts The Thread Array to be verified.
	 * @return The last position of the running thread with the given name.
	 */
	public int checkForHashOnThreads(String hash, Thread[] ts)
	{
		int ret = -1;
		for(int i = 0; i < ts.length; i++)
		{
			if(ts[i] != null)
			{
				String threadName = ts[i].getName();
				if(threadName != null)
				{
					if(threadName.matches("(?i).*-.*"))
					{
						String[] chunkedName = threadName.split("-");
						if(chunkedName[0].equals(hash)) ret = i;
					}
				}
			}
		}
		
		return ret;
	}
	
	/**
	 * Checks the "Downloads" folder and outputs all data regarding its contents.
	 */
	public void checkDownloadsFolder()
	{
		keyPressed = 0x0;
		clearScreen();
		try {
			getFiles(filePath);
		} catch (Exception e) {
			System.out.println("Cannot read from Downloads Folder");
		}
		if(filesData.size() == 0)
		{
			System.out.println("You have no files in your downloads folder..\n");
			waitForEnter();
			return;
		}
		System.out.println("Files available in your Folder:\n");
		
		for(int i = 0; i < filesData.size(); i++)
		{
			printSingleFile(filesData.get(i));
			if(i != filesData.size()-1) Separator();
		}
		
		waitForEnter();
		clearScreen();
	}
	
	/**
	 * Prints information regarding a File.
	 * @param fd The file information to be printed.
	 */
	public void printSingleFile(FileData fd)
	{
		System.out.println("Name: " + fd.getName());
		System.out.println("SHA256 (Hash Value): " + fd.getCheckSum());
		System.out.println("File Size (kB): " + (int) Math.ceil(fd.getSize()/1024.0));
	}
	
	/**
	 * A simple console Separator.
	 */
	private void Separator()
	{
		System.out.println("--------------------------");
	}
	
	/**
	 * Waits for the enter key to be presed.
	 */
	private void waitForEnter()
	{
		System.out.println("\nPress ONLY Enter to Continue...");
		pressKey();
		while(keyPressed != 13)
		{
			System.out.println("\nPress ONLY Enter to Continue...");
			pressKey();	
		}
	}

	/**
	 * Checks for a key pressed in the console.
	 */
	private void pressKey()
	{
		try {
			keyPressed = (char) new InputStreamReader(System.in).read ();
		} catch (IOException e) {
			System.out.println("Cannot read from Keyboard");
		}
	}
	
	/**
	 * Changes the "Downloads" directory.
	 */
	public void changeFolder()
	{
		clearScreen();
		System.out.println("Choose a new Folder: ");
		Scanner input = new Scanner(System.in);
		String newPath = input.nextLine();
		boolean exists = checkDirectories(newPath);
		if(!exists)
		{
			System.out.println("Invalid Directory!\n");
			waitForEnter();
			changeFolder();
		}
		else
		{
			filePath = newPath+"/";
			System.out.println("Dirextory changed!\n");
			waitForEnter();
		}
	}
	
	/**
	 * Checks whether the directory exists.
	 * @param inputPath The path to be checked.
	 * @return A boolean confirming such.
	 */
	private boolean checkDirectories(String inputPath)
	{
		String[] names = inputPath.split("\\\\");
		for(int i = 0; i < names.length; i++)
		{
			File f = new File(names[i]);
			
			if(f.exists()) continue;
			else
			{
				if(i != names.length-1) return false;
				else f.mkdir();
			}
		}
		
		return true;
	}
}