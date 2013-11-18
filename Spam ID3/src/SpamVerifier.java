import java.io.IOException;
import java.util.ArrayList;

public class SpamVerifier 
{
	private ID3 id3;
	private SpamDomain spamDomain;
	private Tree decisionTreeID3;
	private Menu menu;
	private MailClassifier classifier;
	
	public SpamVerifier()
	{
		setMenu(new Menu());
	}

	public void mainCycle()
	{
		for(;;)
		{
			menu.showMenu();
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
		String basePath;
		String namePath;
		String testPath;
		switch(option)
		{
			case 1:
				basePath = "spambaseSmall.data";
				namePath = "spamnames.txt";
				testPath = "testMessages.data";
				spamDomain = new SpamDomain(basePath, testPath, namePath);
				id3 = new ID3(spamDomain.getValueTable(), convertToWords(spamDomain.getWords(), spamDomain.getCharacters()));
				id3.applyID3();
				classifier = new MailClassifier(id3, spamDomain.getTestMessagesTable().getRowInfo());
				classifier.classifyMails();
				break;
			case 2:
				basePath = "spambaseMedium.data";
				namePath = "spamnames.txt";
				testPath = "testMessages.data";
				spamDomain = new SpamDomain(basePath, testPath, namePath);
				id3 = new ID3(spamDomain.getValueTable(), convertToWords(spamDomain.getWords(), spamDomain.getCharacters()));
				id3.applyID3();
				classifier = new MailClassifier(id3, spamDomain.getTestMessagesTable().getRowInfo());
				classifier.classifyMails();
				break;
			case 3:
				basePath = "spambase.data";
				namePath = "spamnames.txt";
				testPath = "testMessages.data";
				spamDomain = new SpamDomain(basePath, testPath, namePath);
				id3 = new ID3(spamDomain.getValueTable(), convertToWords(spamDomain.getWords(), spamDomain.getCharacters()));
				id3.applyID3();
				classifier = new MailClassifier(id3, spamDomain.getTestMessagesTable().getRowInfo());
				classifier.classifyMails();
				break;
			default:
				break;
		}
	}
	
	private ArrayList<String> convertToWords(ArrayList<String> a, ArrayList<Character> b)
	{
		ArrayList<String> ret = new ArrayList<String>();
		for(int i = 0; i < a.size(); i++) ret.add(a.get(i));
		for(int i = 0; i < b.size(); i++) ret.add(b.get(i).toString());

		return ret;
	}
	
	//getters and setters
	
	public ID3 getID3() {
		return id3;
	}
	public void setID3(ID3 id3) {
		this.id3 = id3;
	}
	public SpamDomain getSpamDomain() {
		return spamDomain;
	}
	public void setSpamDomain(SpamDomain spamDomain) {
		this.spamDomain = spamDomain;
	}
	public Tree getDecisionTreeID3() {
		return decisionTreeID3;
	}
	public void setDecisionTreeID3(Tree decisionTreeID3) {
		this.decisionTreeID3 = decisionTreeID3;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
}
