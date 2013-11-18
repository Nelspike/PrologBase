import java.util.ArrayList;


public class TreeVerifier {

	private SimpleNode rootNode;
	private String treeRepresentation;
	
	public TreeVerifier(SimpleNode r)
	{
		rootNode = r;
		System.out.println("Root Starting\n");
		verifyTree(rootNode, 0, "");
		System.out.println(treeRepresentation);
	}
	
	private void verifyTree(SimpleNode r, int depth, String tree)
	{
		Node[] children = r.children;
		String indentStr = "";
		String tokensStr = "";
		ArrayList<String> tokens = r.getTokens();
		
		for(int i = 0; i < depth; i++)
		{
			indentStr += "  ";
		}
		
		if(tokens.size() != 0)
		{
			tokensStr = indentStr + r.toString() + "(";
			
			for(int i = 0; i < tokens.size(); i++)
			{
				if(i != tokens.size()-1) tokensStr += tokens.get(i) + ", ";
				else tokensStr += tokens.get(i);
			}
			
			tokensStr += ")" + '\n';
			treeRepresentation+=tokensStr;
		}
		else
		{
			tokensStr = indentStr + r.toString() + "()" + '\n';
			treeRepresentation+=tokensStr;
		}
		
		if(children == null || children.length == 0 )
		{
			return;
		}
		else
		{
			for(int i = 0; i < children.length; i++)
			{
				SimpleNode s = (SimpleNode) children[i];
				verifyTree(s, depth+1, tree + treeRepresentation);
			}
		}
	}
}
