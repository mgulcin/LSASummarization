package textsum;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.Vector;

import textsum.data.Sentence;
import textsum.data.Word;

public class CompareResults {

	public static Vector<String> readOrg(String fileName)
	{
		FileInputStream fstream;
		BufferedReader data;
		String line0=new String();
		String text="";
		
		try{
			fstream = new FileInputStream(fileName);
			data = new BufferedReader(new InputStreamReader(fstream,"Cp1254")); 

			//read rest
			while ((line0 = data.readLine()) != null) {
				//System.out.println("Line:"+line0.toString());
				text=text.concat(line0.trim())+" ";
			}//while

		} //try
		catch (Exception e){
			System.err.println("File input error while reading");
		}//catch

		//System.out.println("text:\n"+text);
		int beginindex=0;
		int endindex=0;
		Vector<String> sentences = new Vector<String>();
		
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
	/*
	public static Vector<String> readFile(String fileName){
		FileInputStream fstream;
		BufferedReader data;
		String line=new String();
		String text="";
		Vector<String> summarySentences = new Vector<String>();
		
		try{
			fstream = new FileInputStream(fileName);
			data = new BufferedReader(new InputStreamReader(fstream,"Cp1254")); 
			while ((line = data.readLine()) != null) {
				//System.out.println("Line:"+line.toString());
				text=text.concat(line+"\n");
			}//while
			} //try
		catch (Exception e){
			System.err.println("File input error while reading");
			}//catch
		//System.out.println("wholeText:\n"+text);
		
		//split text to sentences
		BreakIterator brkit = BreakIterator.getSentenceInstance();
		brkit.setText(text);
		int start = brkit.first();
		int end = brkit.next();
		//System.out.println("Text:"+start+" "+end);

		while (end != BreakIterator.DONE) 
		{
			//System.out.println("Text:"+start+" "+end);
			String sentence = text.substring(start, end);	
			summarySentences.add(sentence.trim());
			//System.out.println("Cumle: "+sentence);
			start = end;
			end = brkit.next();	
		}
		
		return summarySentences;
	}	
*/	

	public static Vector<SummaryType> readFileOut(String fileName){
		FileInputStream fstream;
		BufferedReader data;
		String line=new String();
		int valType=0;
		int algType=0;
		boolean found = false;
		Vector<SummaryType> allST = new Vector<SummaryType>();
		try{
			fstream = new FileInputStream(fileName);
			data = new BufferedReader(new InputStreamReader(fstream,"Utf-8")); 
			while ((line = data.readLine()) != null) {
				//System.out.println("Line:"+line.toString());
				if(found==true)
				{
					//read sentences add to SummaryType Vector
					if(line.startsWith("Summary")){
						SummaryType st = new SummaryType();
						st.valType = valType;
						st.algType = algType;
						Vector<String> sentences = new Vector<String>();
						st.sentences = sentences;
						allST.add(st);
					}else if(line.startsWith("ValType")) {
						valType=0;
						algType=0;
						found = false;
					}else if(line.startsWith("AlgType")){
						algType=0;
						found = false;
					}else{
						allST.lastElement().sentences.add(line.trim());
					}
					
				}
				
				if(found ==false)
				{
					if(line.startsWith("ValType"))
					{
						String[] parts = line.split(" ");
						//valType = parts[1];
						for(int i=0;i<parts[1].length();i++)
						{
							valType = valType*(10) + (parts[1].charAt(i)-'0');
						}
					}
					else if(line.startsWith("AlgType"))
					{
						String[] parts = line.split(" ");
						//algType = parts[1];
						for(int i=0;i<parts[1].length();i++)
						{
							algType = algType*(10) + (parts[1].charAt(i)-'0');
						}
						found = true;
					}
				}
			}//while
			} //try
		catch (Exception e){
			System.err.println("File input error while reading");
			}//catch
		return allST;
	}

	
	public static void main(String[] args) {
		int valTypeSize  = 8;
		int algTypeSize = 1;//7;
		int  results[][];//valtype(8diff) x algtype(7diff)--for each doc
		
		results = new int[valTypeSize][algTypeSize];
		for(int i=0;i<8;i++) 
		{
			for(int j=0;j<algTypeSize;j++) 	results[i][j]=0;
		}
		
		double  resultsTotal[][];//valtype(8diff) x algtype(7diff)--for all docs(match/expected)
		resultsTotal = new double[valTypeSize][algTypeSize];
		for(int i=0;i<valTypeSize;i++) 
		{
			for(int j=0;j<algTypeSize;j++) 	resultsTotal[i][j]=0;
		}
		
		double resultsValType[] = new double[valTypeSize];
		for(int i=0;i<valTypeSize;i++) resultsValType[i]=0;
		
		double resultsAlgType[] = new double[algTypeSize];
		for(int i=0;i<algTypeSize;i++) resultsAlgType[i]=0;

		
		int minNumber = 1;//0;
		int maxNumber = 51;//1;//120;
		for(int nameIndex=minNumber;nameIndex<maxNumber;nameIndex++)
		{
			//System.out.println(" nameIndex: "+nameIndex);
			//String fileNameIn="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\haberozetler\\ozet"+nameIndex+".txt";
			//String fileNameIn="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\MakaleOzetlerSet2\\"+nameIndex+".txt";
			//String fileNameIn="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\submitted-abstract.txt";
			String fileNameIn="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\MakaleOzetlerSet2\\"+nameIndex+".txt";
			
			//Vector<String> summarySentencesIn = readFile(fileNameIn);
			Vector<String> summarySentencesIn = readOrg(fileNameIn);
			/*for(String s:summarySentencesIn)
			{
				System.out.println(s);
			}*/
			
			//String fileNameOut="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\dataset-haberler-sonucMy\\haber"+nameIndex+".txt";
			//String fileNameOut="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\submitted-result.txt";
			String fileNameOut="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\dataset-MakaleSet2-sonucMy\\textRank2\\"+nameIndex+".txt";
			
			Vector<SummaryType> summarySentencesOut = readFileOut(fileNameOut);
			/*System.out.println("******************************");
			for(SummaryType s:summarySentencesOut)
			{
				for(String str:s.sentences)
				{
					System.out.println(str);
				}
			}*/
			
			
			System.out.println("Index: "+nameIndex);
			double count = summarySentencesIn.size();
			System.out.println("count: "+count);
			for(SummaryType st:summarySentencesOut)
			{
				//results[st.valType][st.algType]= compareSentences(summarySentencesIn,st.sentences);
				results[st.valType][st.algType] = compare(summarySentencesIn,st.sentences);
				//double count = st.sentences.size();
				//double count = summarySentencesIn.size();
				//System.out.println("count: "+count);
				resultsTotal[st.valType][st.algType] +=  ((double)results[st.valType][st.algType])/count;
			}
			
/*			//print:
			for(String sentence:summarySentencesIn)
			{
				System.out.println(sentence);
			}
			System.out.println("**********************");

			for(SummaryType st:summarySentencesOut)
			{
				System.out.println("valType: "+st.valType);
				System.out.println("algType: "+st.algType);
				for(String s:st.sentences)
				{
					System.out.println(s);
				}	
				
				System.out.println("ResultCount: "+results[st.valType][st.algType]+"\n");
			}
*/
		}
		
		//prints resultsTotal
		System.out.println("ResultsTotal:");
		for(int i=0;i<valTypeSize;i++)
		{
			for(int j=0;j<algTypeSize;j++)
			{
				System.out.print(resultsTotal[i][j]+" ");
			}
			System.out.println();
		}

		for(int i=0;i<valTypeSize;i++)
		{
			double resultsVal=0;
			for(int j=0;j<algTypeSize;j++)
			{
				resultsVal = resultsVal + resultsTotal[i][j];
			}
			resultsVal = resultsVal/algTypeSize;	
			resultsValType[i] = resultsValType[i] + resultsVal;
		}
		
		for(int j=0;j<algTypeSize;j++)
		{
			double resultsAlg=0;
			for(int i=0;i<valTypeSize;i++)
			{
				resultsAlg = resultsAlg + resultsTotal[i][j];
			}
			resultsAlg = resultsAlg/valTypeSize;
			resultsAlgType[j] = resultsAlgType[j] + resultsAlg;
		}
		
		System.out.println("ValType:");
		for(int i=0;i<valTypeSize;i++) System.out.println(i+ " , "+ resultsValType[i]/(maxNumber-minNumber));
	
		System.out.println("AlgType:");
		for(int i=0;i<algTypeSize;i++) System.out.println(i+ " , "+ resultsAlgType[i]/(maxNumber-minNumber));
		
	}


	private static int compare(Vector<String> sentencesIn,Vector<String> sentencesOut)
	{
		int count = 0;
		Vector<Vector<String>> wordsIn = findWords(sentencesIn);
		Vector<Vector<String>> wordsOut = findWords(sentencesOut);
/*		
		for(Vector<String> wIn:wordsIn)
		{
			for(String s:wIn)
			{
				System.out.print(s+" ");
			}
			System.out.println();
		}
		
		System.out.println("****************************");

		for(Vector<String> wOut:wordsOut)
		{
			for(String s:wOut)
			{
				System.out.print(s+" ");
			}
			System.out.println();
		}
		*/
		for(Vector<String> wIn:wordsIn)
		{
			for(Vector<String> wOut:wordsOut)
			{
				if(wIn.size()==wOut.size())
				{
					//the sentences can be same
					boolean isEqual = false;
					
					int eqCount = 0;
					for(int k=0;k<wIn.size();k++)
					{
						if(wIn.elementAt(k).equals(wOut.elementAt(k)))
							eqCount++;
					}
					if(eqCount == wIn.size()) isEqual=true;
					
					if(isEqual)
					{
						count++;
						break;
					}
				}
				else
				{
					//the sentences can not be same, since their length is not equal
				}
			}
		}
				
		return count;
	}

	private static int compareSentences(Vector<String> sentencesIn,Vector<String> sentencesOut) 
	{
		int count = 0;
		for(int i=0;i<sentencesIn.size();i++)
		{
			String in = sentencesIn.elementAt(i);
			System.out.println("in:\n"+in);
			Vector<String> inWords = getWords(in);
			for(int j=0;j<sentencesOut.size();j++)
			{
				String out = sentencesOut.elementAt(j);
				System.out.println("out:\n"+out);
				
				Vector<String> outWords = getWords(out);
				
				//System.out.println(i + " , "+inWords.size()+ " ; " + j + " , "+outWords.size()  );
				if(inWords.size()==outWords.size()) 
				{
					boolean isEqual = false;
					int eqCount = 0;
					for(int k=0;k<inWords.size();k++)
					{
						if(inWords.elementAt(k).equals(outWords.elementAt(k)))
							eqCount++;
					}
					if(eqCount == inWords.size()) isEqual=true;
					
					if(isEqual)
					{
						count++;
						break;
					}
				}
			}
		}
		return count;
	}



	private static Vector<String> getWords(String sentence) {
		Vector<String> words = new Vector<String>();
		String[] partSentArray = sentence.split(" ");
		for(int d=0;d<partSentArray.length;d++)
		{	
			words.add(partSentArray[d]);
		}
	
		return words;
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
				word=s.substring(beginindex, endindex);		
				//System.out.println(word);
				
				char unwanted[] = {',','.','!','?','"','”','“','(',')',':','‘','’',' ','*'};
				word = removeUnwantedChars(word,unwanted,14);
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
}
