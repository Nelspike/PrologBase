import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.*;
import java.util.*;


public class DataThread extends Thread
{
	/**
	 * The Client that is executing the thread in question.
	 */
	private Client infoPart;
	
	/**
	 * The Multicast Socket that is used for the Data Control.
	 */
	private MulticastSocket mSocket;
	
	/**
	 * The port that has all the data information flow.
	 */
	private int dataPort;
	
	/**
	 * The type of thread, if it's sending or receiving.
	 */
	private String type;
	
	/**
	 * The hash that has been requested to be downloaded.
	 */
	private byte[] requestedHash;
	
	/**
	 * The hash that has been requested to be uploaded.
	 */
	private byte[] sha256;
	
	/**
	 * The Array Lsit will all the indexes of the chunks to be sent.
	 */
	private ArrayList<Long> chunksToSend;
	
	/**
	 * The boolean array with all of the information of all the chunks that have been recieved.
	 */
	private ArrayList<Boolean> boolArray;
	
	/**
	 * The file associated with this downloading/uploading thread.
	 */
	private File file;
	
	/**
	 * The RandomAccessFile pointer associated with this downloading/uploading thread.
	 */
	private RandomAccessFile fileToMake;
	
	/**
	 * The group address in String
	 */
	private String groupAddress;
	
	/**
	 * The amount of chunks received.
	 */
	private int count;
	
	/**
	 * The file size associated with the file in this thread.
	 */
	private int fileSize;
	
	/**
	 * The number of total chunks to be sent/received.
	 */
	private int nChunks;
	
	/**
	 * The file data with all the information of the found file.
	 */
	private FileData foundFile;
	
	/**
	 * DataThread constructor.
	 * @param info The client that will be associated to the thread.
	 * @param name Name of the Thread.
	 * @param _type Type of Thread. It can be "Receiver" or "Sender".
	 * @param ip IP Address of the multicast group.
	 */
	public DataThread(Client info, String name, String _type, String ip)
	{
		super(name);
		infoPart = info;
		dataPort = 8966;
		count = 1;
		type = _type;
		chunksToSend = new ArrayList<Long>();
		groupAddress = ip;
	}
	
	/**
	 * Receive and parse a packet with chunk and header.
	 * @throws IOException
	 */
	private void recieveData() throws IOException
	{
		boolArray = infoPart.getBooleanArray();
		byte[] message = new byte[1088];
		DatagramPacket packet = new DatagramPacket(message, message.length);
		
		try
		{
			mSocket.receive(packet);
		}
		catch(java.net.SocketTimeoutException e)
		{
			infoPart.sendMessage(formTimeoutMessage());
			return;
		}
		
		byte[] packetContent = packet.getData();
		byte[] shaFromPacket = getShaFromPacket(packetContent);
		
		if(verifyArrayWithSha(shaFromPacket))
		{
			byte[] longChunks = new byte[8];
			System.arraycopy(packetContent, 32, longChunks, 0, 8);

			byte[] longArray = invertArray(longChunks, longChunks.length);
			BigInteger bigInt = new BigInteger(longArray);
			long nChunk = bigInt.longValue();
		
			int index = chunksToSend.indexOf(nChunk);
			
			/*Chunk Integrity Area*/
			
			byte[] md5Chunks = new byte[24];
			System.arraycopy(packetContent, 40, md5Chunks, 0, 24);
			byte[] md5 = invertArray(md5Chunks, md5Chunks.length);
			
			/*Here*/
			
			synchronized(boolArray)
			{
				if(index != -1 || !boolArray.get((int)nChunk))
				{
					byte[] dataArray = new byte[1024];
					System.arraycopy(packetContent, 64, dataArray, 0, 1024);
					
					if(nChunk == nChunks-1)
					{
						fileToMake.seek(bigInt.longValue()*1024);
						int toWrite = (int) (fileSize - nChunk*1024);
						
						if(md5[0] == 'N' && md5[1] == 'O' && md5[2] == ':' && md5[3] == 'J' && md5[4] == 'S' && !verifyNullArray(md5))
						{
							/*Chunk Integrity Area*/
							MessageDigest md = null;
							try {
								md = MessageDigest.getInstance("MD5");
							} catch (NoSuchAlgorithmException e) {
								System.out.println("MD5 doesn't exist!");
							}
							
							md.update(dataArray, 0, toWrite);	
							byte[] md5Digest = md.digest();
							byte[] toCompare = new byte[24];
							
							fillInSendingBytes(toCompare, md5Digest);					
							/*Here*/
							
							if(!Arrays.equals(md5, toCompare)) 
							{
								System.out.println("Chunks are not the same!");
								return;
							}
						}							
						
						boolArray.set((int)nChunk, true);
						chunksToSend.remove(index);
						fileToMake.write(dataArray, 0, toWrite);
					}
					else 
					{
						fileToMake.seek(bigInt.longValue()*1024);
						
						if(md5[0] == 'N' && md5[1] == 'O' && md5[2] == ':' && md5[3] == 'J' && md5[4] == 'S' && !verifyNullArray(md5))
						{
							/*Chunk Integrity Area*/
							MessageDigest md = null;
							try {
								md = MessageDigest.getInstance("MD5");
							} catch (NoSuchAlgorithmException e) {
								System.out.println("MD5 doesn't exist!");
							}
							
							md.update(dataArray, 0, 1024);	
							byte[] md5Digest = md.digest();
							byte[] toCompare = new byte[24];
							
							fillInSendingBytes(toCompare, md5Digest);	
							/*Here*/
							
							if(!Arrays.equals(md5, toCompare)) 
							{
								System.out.println("Chunks are not the same!");
								return;
							}
						}
						boolArray.set((int)nChunk, true);
						chunksToSend.remove(index);
						fileToMake.write(dataArray, 0, 1024);
					}
					
					count++;
				}
			}
		}
		
		infoPart.setBooleanArray(boolArray);
	}

	/**
	 * Form and send a packet with chunk and header. 
	 */
	private void sendData()
	{
		while(chunksToSend.size() != 0)
		{
			DatagramPacket pack = null;
			try {
				pack = formDataPacket();
			} catch (UnknownHostException e1) {
				System.out.println("Cannot form the packet!");
			}
			try 
			{
				mSocket.send(pack);
			} catch (IOException e) {
				System.out.println("Cannot send Data Packet!");
			}
		}
	}
	
	/**
	 * Overriden function of the Thread Class, that will execute everything that is related to this thread.
	 */
	public void run()
	{
		connectToDataPort();
		if(type.equals("Recieving"))
		{
			int iteration = 0;
			int dummySize = nChunks;
			while(count <= nChunks)
			{
				chunksToSend = new ArrayList<Long>();
				if(dummySize >= 1024)
				{
					for(int i = (1024*iteration)+1; i <= (1024*iteration)+1024; i++) chunksToSend.add((long) i-1);
					dummySize -= 1024;
				}
				else
				{
					for(int i = (1024*iteration)+1; i <= (1024*iteration)+dummySize; i++) chunksToSend.add((long) i-1);
					dummySize = 0;
				}
				iteration++;
							
				String getMessage = infoPart.formGetMessage(foundFile, chunksToSend.get(0), chunksToSend.get(chunksToSend.size()-1));
				
				try {
					infoPart.sendMessage(getMessage);
				} catch (IOException e1) {
					System.out.println("Cannot send GET message!");
				}
				while(chunksToSend.size() != 0)
				{
					try {
						recieveData();
					} catch (IOException e) {
						System.out.println("Cannot recieve data!");
					}
				}
			}
			
			try {
				fileToMake.close();
			} catch (IOException e) {
				System.out.println("Can't close the file!");
			}
		}
		else
		{
			sendData();
		}
	}
	
	//Inner Functions
	
	/**
	 * Display on the screen download progress for a file
	 * @param filename File that's being downloaded
	 */
	public void getProgress(String filename)
	{
		NumberFormat formatter = new DecimalFormat("#0.00");
		double progress = ((double)count)/nChunks*100.0;
		System.out.println(filename + " / Progress(%): " + formatter.format(progress) + "%");
	}
	
	/**
	 * Forms a message after the timeout period has been exceeded, with the chunks that are left to be transfered.
	 * @return A String with the automated message.
	 */
	private String formTimeoutMessage()
	{
		String ret = "GET " + infoPart.checkSumString(requestedHash) + " ";
		for(int i = 0; i < chunksToSend.size(); i++)
		{
			ret += chunksToSend.get(i) + ","; 
		}
		
		return ret.substring(0, ret.length()-1);
	}
	
	/**
	 * Verifies if an Array has all of its elements with the value 0 or not.
	 * @param arg The array to be verified.
	 * @return
	 */
	private boolean verifyNullArray(byte[] arg)
	{
		for(int i = 0; i < arg.length; i++)
		{
			if(arg[i] != '0') return false;
		}
		
		return true;
	}
	
	/**
	 * Verifies whether a byte array has all of the information regarding the requested Hash
	 *   equalizing the argument. 
	 * @param arg The Hash to be compared with the requested one.
	 * @return A boolean confirming such comparison.
	 */
	private boolean verifyArrayWithSha(byte[] arg)
	{
		for(int i = 0; i < requestedHash.length; i++)
			if(arg[i] != requestedHash[i]) return false;

		return true;
	}
	
	/**
	 * Gets the Hash information from the received packet.
	 * @param content The content to be analyzed.
	 * @return The byte array with the hash.
	 */
	private byte[] getShaFromPacket(byte[] content)
	{
		byte[] ret = new byte[32];
		
		for(int i = 0; i < 32; i++)
			ret[i] = content[i];
		
		return ret;
	}	

	/**
	 * Connects to the data port.
	 */
	private void connectToDataPort()
	{
		try {
			mSocket = new MulticastSocket(dataPort);
		} catch (IOException e) {
			System.out.println("Cannot connect to the data port!");
		}
		
		try {
			mSocket.setSoTimeout(1000);
		} catch (SocketException e1) {
			System.out.println("Cannot set timeout to the socket!");
		}
		
		try {
			mSocket.joinGroup(InetAddress.getByName(groupAddress));
		} catch (UnknownHostException e) {
			System.out.println("Cannot connect!");
		} catch (IOException e) {
			System.out.println("Cannot connect!");
		}
	}
	
	/**
	 * Forms the Data Packet with all the information needed.
	 * @return The DatagramPacket formed.
	 * @throws UnknownHostException
	 */
	private DatagramPacket formDataPacket() throws UnknownHostException
	{
		ByteBuffer packetBuf = ByteBuffer.allocate(1088);
		packetBuf.order(ByteOrder.LITTLE_ENDIAN);
		byte[] shaToPacket = sha256;
		packetBuf.put(shaToPacket, 0, sha256.length);
		
		Random r = new Random();
		int chunkPos = r.nextInt(chunksToSend.size());

		RandomAccessFile raf = null;
		
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open file!");
		}
		
		try {
			raf.seek(chunksToSend.get(chunkPos)*1024);
		} catch (IOException e) {
			System.out.println("Cannot reach the specified data source!");
		}
		
		byte[] chunk = new byte[1024];
		int readBytes = -1;
		try {
			readBytes = raf.read(chunk, 0, 1024);
		} catch (IOException e) {
			System.out.println("Cannot read from File!");
		}
		
		packetBuf.putLong(chunksToSend.get(chunkPos));

		/* Comentar esta area */
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("MD5 doesn't exist!");
		}
		
		md.update(chunk, 0, readBytes);	
		byte[] md5Digest = md.digest();
		byte[] toSend = new byte[24];
		fillInSendingBytes(toSend, md5Digest);
		byte[] md5 = invertArray(toSend, toSend.length);
		/*Aqui*/
				
		if(readBytes != -1) 
		{
			packetBuf.put(md5, 0, 24);
			packetBuf.put(chunk, 0, readBytes);
		}
		
		chunksToSend.remove(chunkPos);
		
		return new DatagramPacket(packetBuf.array(), packetBuf.array().length, InetAddress.getByName(groupAddress), dataPort);
	}
	
	/**
	 * Fills in the "Reserved" field with a specific signature and the hash from a certain chunk.
	 * @param arg The array to be filled.
	 * @param digested The digested array with the hash information.
	 */
	private void fillInSendingBytes(byte[] arg, byte[] digested)
	{
		arg[0] = 'N'; //Nelson
		arg[1] = 'O'; //Oliveira
		arg[2] = ':'; //:
		arg[3] = 'J'; //João
		arg[4] = 'S'; //Santos
		for(int i = 5; i < 8; i++) arg[i] = 0;
		int j = 0;
		for(int i = 8; i < 24; i++) 
		{
			arg[i] = digested[j];
			j++;
		}
	}
	
	/**
	 * Inverts an Array.
	 * @param arg The array to be inverted.
	 * @param argLength The length of the array.
	 * @return The inverted array.
	 */
	private byte[] invertArray(byte[] arg, int argLength)
	{
		byte[] ret = new byte[argLength];
		int packetSize = argLength-1;
		for(int i = 0; i < argLength; i++)
		{
			ret[i] = arg[packetSize];
			packetSize--;
		}
		
		return ret;
	}

	//Getters and Setters
	
	/**
	 * Sets the Array List of the chunks to be sent.
	 * @param chunksToSend The Array List.
	 */
	public void setChunksToSend(ArrayList<Long> chunksToSend) {
		this.chunksToSend = chunksToSend;
	}
	
	/**
	 * Sets the requested hash.
	 * @param toSet The hash itself.
	 */
	public void setRequestedHash(byte[] toSet)
	{
		requestedHash = toSet;
	}
	
	/**
	 * Sets teh hash of the file assocaited.
	 * @param toSet The hash to be set.
	 */
	public void setSha256(byte[] toSet)
	{
		sha256 = toSet;
	}

	/**
	 * Sets the file associated with this thread.
	 * @param file The file itself.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Sets the RandomAccessFile associated with this thread.
	 * @param fileToMake The RandomAccessFile itself.
	 */
	public void setFileToMake(RandomAccessFile fileToMake) {
		this.fileToMake = fileToMake;
	}

	/**
	 * Sets the file size of the file associated with the thread.
	 * @param fileSize The file size to be set.
	 */
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Sets the total number of chunk of the file to be downloaded/uploaded.
	 * @param nChunks The number of chunks to be sent.
	 */
	public void setnChunks(int nChunks) {
		this.nChunks = nChunks;
	}

	/**
	 * The file data with associated with the found file.
	 * @param foundFile The found file itself.
	 */
	public void setFoundFile(FileData foundFile) {
		this.foundFile = foundFile;
	}
}
