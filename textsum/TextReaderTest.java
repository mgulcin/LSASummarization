package textsum;

public class TextReaderTest {
	public static void main( String[] args )
	{
		String fileName="C:\\Documents and Settings\\mozsoy\\My Documents\\haber1.txt";
		TextReader reader=new TextReader();
		
		reader.readFile(fileName);
		reader.decompose();
		
		System.out.println("WholeText:\n"+reader.getWholeText());
		System.out.println("Title: "+reader.getTitle());
		System.out.println("SentenceText: \n"+reader.getSentenceText());
	}
}
