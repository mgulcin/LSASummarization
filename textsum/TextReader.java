package textsum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TextReader {
	String wholeText;
	String title;
	String sentenceText;
	
	
	public String getWholeText() {
		return wholeText;
	}

	public String getTitle() {
		return title;
	}

	public String getSentenceText() {
		return sentenceText;
	}

	public int readFile(String fileName){
		int summarySCount = 0;
		
		FileInputStream fstream;
		BufferedReader data;
		String line=new String();
		String text="";
		boolean end=false;
		try{
			fstream = new FileInputStream(fileName);
			data = new BufferedReader(new InputStreamReader(fstream,"Cp1254")); 
			//read first 10 lines of keywords first
			int lim=9;
			while ((line = data.readLine()) != null && lim>0)
			{
				//System.out.println("Line:"+line);
				lim--;
			}
			//read number of sentences in summary
			line = data.readLine();
			summarySCount= line.toCharArray()[0] - '0';
			
			
			//read rest
			while ((line = data.readLine()) != null) {
				//System.out.println("Line:"+line.toString());
				text=text.concat(line+"\n");
			}//while
			wholeText=text;
			//System.out.println("wholeText:\n"+wholeText);
			} //try
		catch (Exception e){
			System.err.println("File input error while reading");
			}//catch
		return summarySCount;
	}
	
	public void decompose()
	{
		String line="";
		int beginindex=0;
		int endindex=0;
		
		endindex=getLine(wholeText,beginindex);
		line=wholeText.substring(beginindex, endindex);//Read title
		beginindex=endindex;
		title= line.trim();
		line="";
		//System.out.println("beginindex:"+beginindex+" endindex:"+endindex);
		//System.out.println("line\n"+line);
		
		while(beginindex!=wholeText.length())
		{
			endindex=getLine(wholeText,beginindex);
			line=line+wholeText.substring(beginindex, endindex);
			beginindex=endindex;
		}
		sentenceText=line;
		//System.out.println("beginindex:"+beginindex+" endindex:"+endindex);
		//System.out.println("line\n"+line);
			
	}
	
	private int getLine(String reportText,int beginindex){
		int index=0;
		for(int i=beginindex;i<wholeText.length();i++)
		{
			if(reportText.charAt(i)=='\n')
				{
					index=i+1;
					break;
				}
		}
		return index;
	}
}
