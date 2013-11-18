import java.io.*;

public class FileData
{
	/**
	 * The name of the file.
	 */
	private String name;
	
	/**
	 * The hash associated with the file, in its String representation.
	 */
	private String checkSum;
	
	/**
	 * The hash associated with the file.
	 */
	private byte[] sha256;
	
	/**
	 * The size of the file.
	 */
	private long size;
	
	/**
	 * The file itself.
	 */
	private File file;
	
	/**
	 * the number of peers associated with the file.
	 */
	private int nPeers;
	
	/**
	 * Constructor for FileData.
	 * @param _name The name to be given to the file.
	 * @param _checkSum The file's hash in a String representation.
	 * @param _size The of the file in Bytes.
	 * @param _file The file pointer associated with the file.
	 * @param shaBuf The hash itself.
	 */
	public FileData(String _name, String _checkSum, long _size, File _file, byte[] shaBuf)
	{
		setSha256(shaBuf);
		setName(_name);
		setCheckSum(_checkSum);
		setSize(_size);
		setFile(_file); 
	}
	
	/**
	 * Default constructor for FileData.
	 */
	public FileData() {}
	
	/**
	 * Increments the number of peers by one.
	 */
	public void augmentPeers()
	{
		nPeers++;
	}
	
	/**
	 * Gets the name of the file.
	 * @return The name.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the String representation of the hash.
	 * @return The hash.
	 */
	public String getCheckSum()
	{
		return checkSum;
	}
	
	/**
	 * Gets the size of the file in bytes.
	 * @return The size in bytes.
	 */
	public long getSize() 
	{
		return size;
	}
	
	/**
	 * Gets the File Pointer to the file.
	 * @return The file pointer.
	 */
	public File getFile() 
	{
		return file;
	}
	
	/**
	 * Sets the name of the file.
	 * @param _name The name to be set.
	 */
	public void setName(String _name)
	{
		this.name = _name;
	}
	
	/**
	 * Sets the String Representation of the hash for this file. 
	 * @param _checkSum The string representation itself. 
	 */
	public void setCheckSum(String _checkSum)
	{
		this.checkSum = _checkSum;
	}

	/**
	 * Sets the size in bytes for this file.
	 * @param size The size to be set.
	 */
	public void setSize(long size) 
	{
		this.size = size;
	}

	/**
	 * Sets the file pointer.
	 * @param file The file pointer itself.
	 */
	public void setFile(File file) 
	{
		this.file = file;
	}

	/**
	 * Gets the byte array with the hash information.
	 * @return The hash information itself.
	 */
	public byte[] getSha256() {
		return sha256;
	}

	/**
	 * Sets the byte array with the hash information.
	 * @param sha256 The hash itself.
	 */
	public void setSha256(byte[] sha256) {
		this.sha256 = sha256;
	}

	/**
	 * Gets the number of peers in the network for this file.
	 * @return The number of peers.
	 */
	public int getnPeers() {
		return nPeers;
	}

	/**
	 * Sets the number of peers for this file.
	 * @param nPeers The number of peers to be set.
	 */
	public void setnPeers(int nPeers) {
		this.nPeers = nPeers;
	}
}
