package textsum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Vector;

import textsum.data.Sentence;
import textsum.data.Word;
import textsum.morphology.MorphologicalAnalyzer;

public class TextToSentence {

	public static void main(String[] args) {
		for(int nameIndex=1;nameIndex<51;nameIndex++)
		{
			//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\haberler\\haber"+nameIndex+".txt";
			String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\MakaleSet2\\"+nameIndex+".txt";
			String text = readFile(fileName);
			//System.out.println("Text:\n"+text);
			
			Vector<String>sentences = decompose(text);
			/*System.out.println("***************");
			for(String s:sentences)
			{
				System.out.println(s);
			}*/
			
			
			Vector<Vector<String>> words = findWords(sentences);
			/*System.out.println("***************");
			for(Vector<String> snt:words)
			{
				System.out.print("[ ");
				for(String word:snt)
				{
					System.out.print(word+" , ");
				}
				System.out.println(" ]");
			}
			*/
			
			//return words - not string
			Vector<Vector<Word>> wordsVec = getWordsVector(words);
			
			// return sentences-not string
			Vector<Sentence> sentenceVec = getSentenceVector(wordsVec,sentences);
		
			
			// add these sentences and words to TextInfo ti
		}
	}


	public static Vector<Sentence> getSentenceVector(
			Vector<Vector<Word>> wordsVec, Vector<String> sentences) {
		Vector<Sentence> sentenceVec = new Vector<Sentence>();
		for(int i=0;i<wordsVec.size();i++)
		{
			Vector<Word> wVec = wordsVec.elementAt(i); 
			Sentence sent = new Sentence();
			sent.setWords(wVec);
			sent.setSentence(sentences.elementAt(i));
			sentenceVec.add(sent);
		}		
		
		return sentenceVec;
	}


	public static Vector<Vector<Word>> getWordsVector(Vector<Vector<String>> words) {
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		Vector<Vector<Word>> wordsVec = new Vector<Vector<Word>>();  
		for(Vector<String> snt:words)
		{
			Vector<Word> wVec = new Vector<Word>();
			for(String word:snt)
			{
				Word w = ma.analyzeWord(word);
				wVec.add(w);
				//System.out.println(wVec.toString());
			}
			wordsVec.add(wVec);
		}
		return wordsVec;
	}


	public static Vector<Vector<String>> findWords(Vector<String> sentences) {
		Vector<Vector<String>> words = new Vector<Vector<String>>(); 
		for(String s:sentences)
		{
			int beginindex=0;
			int endindex=0;
			Vector<String> wordVec = new Vector<String>();
			String word="";
			while(beginindex != s.length())
			{
				endindex=getWord(s,beginindex);
				
				//System.out.println("sentence:\n"+s);
				//System.out.println(beginindex+" , "+endindex);
				
				if(endindex==0) endindex = s.length();
				word=s.substring(beginindex, endindex-1);		
				//System.out.println(word);
				
				char unwanted[] = {',','.','!','?','"','”','“','(',')',':','‘','’',' '};
				word = removeUnwantedChars(word,unwanted,13);
				//System.out.println("word:"+word);
				
				wordVec.add(word);
	
				beginindex = endindex;
			}
			
			words.add(wordVec);
		}
		return words;
	}


	private static String removeUnwantedChars(String word, char[] unwanted,int size) {
		String dummy = "";
		boolean found ;
		for(int i=0;i<word.length();i++)
		{
			found = false;
			for(int j=0;j<size;j++)
			{
				if(word.charAt(i)==unwanted[j])
				{
					found = true;
					break;
				}
			}
			
			if (found==false)dummy = dummy + word.charAt(i);
		}
		return dummy;
	}


	public static int readSummarySentenceCount(String fileName)
	{
		FileInputStream fstream;
		BufferedReader data;
		String line=new String();
		int summarySCount=0;
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
			summarySCount = line.toCharArray()[0] - '0';

		} //try
		catch (Exception e){
			System.err.println("File input error while reading");
		}//catch
		return summarySCount;

	}
	public static String readFile(String fileName){
		FileInputStream fstream;
		BufferedReader data;
		String line=new String();
		String text="";
		boolean end=false;
		try{
			fstream = new FileInputStream(fileName);
			data = new BufferedReader(new InputStreamReader(fstream,"Cp1254")); 
/*			//read first 10 lines of keywords first
			int lim=9;
			while ((line = data.readLine()) != null && lim>0)
			{
				//System.out.println("Line:"+line);
				lim--;
			}
			//read number of sentences in summary
			line = data.readLine();
			int summarySCount= line.toCharArray()[0] - '0';
*/
			line = data.readLine();//read title
			//read rest
			while ((line = data.readLine()) != null) {
				//System.out.println("Line:"+line.toString());
				text=text.concat(line.trim())+" ";
			}//while

		} //try
		catch (Exception e){
			System.err.println("File input error while reading");
		}//catch
		return text;
	}

	public static Vector<String> decompose(String text)
	{
		int beginindex=0;
		int endindex=0;
		Vector<String> sentences = new Vector<String>();

		/*	no more needed!!!	
		 *  endindex=getLine(text, beginindex);
		 * 	String title=text.substring(beginindex, endindex);//Read title
		 * 	beginindex=endindex;
		 * 	String line="";
		 */

	/*	String line = text.substring(beginindex,text.length());
		String[] sentenceArray = line.split(".");
		for(String s:sentenceArray)
		{
			System.out.println("S: "+s);
			sentences.add(s);
		}*/
		
		String line = "";
		String sentence = "";
		int countTotal = 0;
		BreakIterator brkit = BreakIterator.getSentenceInstance();		
		brkit.setText(text);
		int start = brkit.first();
		int end = brkit.next();

		while (end != BreakIterator.DONE) 
		{
			line = text.substring(start, end);
			int count = getCount('“',line) + getCount('”',line)+getCount('"',line);
			countTotal = countTotal + count;
			//System.out.println("line: "+line.trim()+" \ncount: "+count+" \ncountTotal: "+countTotal);
			if(countTotal%2!=0)
			{
				beginindex=endindex;
				sentence=sentence.trim()+" "+line.trim();
				
				start = end;
				end = brkit.next();	
			}
			else
			{
				beginindex=endindex;
				sentence = sentence.trim()+line.trim();
				sentences.add(sentence.trim());
				sentence="";
				countTotal = 0;
				
				start = end;
				end = brkit.next();	
			}
		}
		/*
		while(beginindex!=text.length())
		{
			endindex=getSentence(text,beginindex);
			line=text.substring(beginindex, endindex);
			int count = getCount('“',line) + getCount('”',line)+getCount('"',line);
			countTotal = countTotal + count;
			System.out.println("line: "+line+" \ncount: "+count+" \ncountTotal: "+countTotal);
			if(countTotal%2!=0)
			{
				beginindex=endindex;
				sentence=sentence+line.trim();
			}
			else
			{
				beginindex=endindex;
				sentence = sentence+line.trim();
				sentences.add(sentence.trim());
				sentence="";
				countTotal = 0;
			}
		}
		*/
		
		return sentences;
	}
	
	private static int getCount(char c, String line) {
		int count = 0;
		for(int i=0;i<line.length();i++)
		{
			if(line.charAt(i)==c)
			{
				count = count+1;
			}
		}	
		return count;
	}


	public static int getLine(String text,int beginindex){
		int index=0;
		for(int i=beginindex;i<text.length();i++)
		{
			if(text.charAt(i)=='\n')
				{
					index=i+1;
					break;
				}
		}
		return index;
	}

	public static int getWord(String sentence,int beginindex){
		//System.out.println("Sentence: "+sentence.substring(beginindex, sentence.length()));
		int index=0;
		for(int i=beginindex;i<sentence.length();i++)
		{
			//System.out.print(sentence.charAt(i));
			if(sentence.charAt(i)==' ' || sentence.charAt(i)=='.' 
				|| sentence.charAt(i)=='?' || sentence.charAt(i)=='!')
			{
				index=i+1;
				break;
			}
		}
		return index;		
	}

	/*
	public static int getSentence(String text,int beginindex){
		int index=0;
		for(int i=beginindex;i<text.length();i++)
		{
			if(text.charAt(i)=='.')
			{
				index=i+1;
				if()
				{
					//Doç. Prof. Dr. vs var demek
					;
				}
				else 
				{
					break;
				}
			}
		}
		return index;
	}
	*/


}
