package textsum;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import textsum.data.LSAMatrix;
import textsum.data.Sentence;
import textsum.data.TextInfo;
import textsum.data.Word;
import textsum.morphology.MorphologicalAnalyzer;

public class SummarizerEng {
	public static void addStopWord(TextInfo ti)
	{
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		//http://www.ranks.nl/stopwords/turkish.html
		ti.addStopWord(ma.analyzeWord("and"));
		ti.addStopWord(ma.analyzeWord("or"));
		ti.addStopWord(ma.analyzeWord("in"));
		ti.addStopWord(ma.analyzeWord("out"));
		ti.addStopWord(ma.analyzeWord("at"));
		ti.addStopWord(ma.analyzeWord("the"));
		ti.addStopWord(ma.analyzeWord("a"));
		ti.addStopWord(ma.analyzeWord("an"));
		ti.addStopWord(ma.analyzeWord("of"));
		ti.addStopWord(ma.analyzeWord("by"));

		ti.addStopWord(ma.analyzeWord("."));
		ti.addStopWord(ma.analyzeWord(","));
		ti.addStopWord(ma.analyzeWord(";"));
		ti.addStopWord(ma.analyzeWord(":"));
		ti.addStopWord(ma.analyzeWord(" "));
		ti.addStopWord(ma.analyzeWord("\n"));
		ti.addStopWord(ma.analyzeWord("\""));
		ti.addStopWord(ma.analyzeWord("'"));
		ti.addStopWord(ma.analyzeWord("`"));
		ti.addStopWord(ma.analyzeWord("{"));
		ti.addStopWord(ma.analyzeWord("}"));
		ti.addStopWord(ma.analyzeWord("["));
		ti.addStopWord(ma.analyzeWord("]"));

		ti.addStopWord(ma.analyzeWord("0"));
		ti.addStopWord(ma.analyzeWord("1"));
		ti.addStopWord(ma.analyzeWord("2"));
		ti.addStopWord(ma.analyzeWord("3"));
		ti.addStopWord(ma.analyzeWord("4"));
		ti.addStopWord(ma.analyzeWord("5"));
		ti.addStopWord(ma.analyzeWord("6"));
		ti.addStopWord(ma.analyzeWord("7"));
		ti.addStopWord(ma.analyzeWord("8"));
		ti.addStopWord(ma.analyzeWord("9"));


		ti.addStopWord(ma.analyzeWord("0."));
		ti.addStopWord(ma.analyzeWord("1."));
		ti.addStopWord(ma.analyzeWord("2."));
		ti.addStopWord(ma.analyzeWord("3."));
		ti.addStopWord(ma.analyzeWord("4."));
		ti.addStopWord(ma.analyzeWord("5."));
		ti.addStopWord(ma.analyzeWord("6."));
		ti.addStopWord(ma.analyzeWord("7."));
		ti.addStopWord(ma.analyzeWord("8."));
		ti.addStopWord(ma.analyzeWord("9."));
	}
	public static void main(String[] args) throws FileNotFoundException 
	{
		TextReader reader=new TextReader();

		TextToSentence ts = new TextToSentence();
		/*
		 * String text = "Exponential growth in text documents brings the problem of finding out whether a text document meets the needs of a user or not. " +
				"In order to solve this problem, text summarization systems which extract brief information from a given text are generated. " +
				"By just looking at the summary of a document, a user can decide whether that document is an interest for him or her without looking at the whole document.";
		 */
		String fileName = "C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\submitted-input.txt";
		String text = ts.readFile(fileName);
		Vector<String> sentencesStr = ts.decompose(text);
		Vector<Vector<String>> words = ts.findWords(sentencesStr);
		Vector<Vector<Word>> wordsVec = ts.getWordsVector(words);
		Vector<Sentence> sentenceVec = ts.getSentenceVector(wordsVec,sentencesStr);
		//int summarySentenceCount = ts.readSummarySentenceCount(fileName);
		int summarySentenceCount = (int) (sentencesStr.size()*0.10);

		TextInfo ti = new TextInfo();
		addStopWord(ti);
		ti.setSentenceText(text);
		ti.setSentenceVector(sentenceVec);
		ti.setWordsVector(wordsVec);

		TextSum textsum = new TextSum();
		textsum.wordGlobalFrequency(ti);
		textsum.wordLocalFrequency(ti);
		textsum.findDiffWords(ti);
		textsum.calculateNoOfDiffWords(ti);
		//textsum.calculateSentenceScore(ti);
		Vector<Sentence> sentences = ti.getSentenceVector();
		System.out.println("Size: " +sentences.size());
		for(int i=0;i<sentences.size();i++)
		{
			System.out.println(i+" "+sentences.elementAt(i));
		}
		 
		
	/*	String fileoutPath ="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\submitted-result.txt";
		FileOutputStream fos; 
		//DataOutputStream dos;
		//File file= new File(fileoutPath);
		fos = new FileOutputStream(fileoutPath);
		PrintStream ps = new PrintStream(fos);
		for(int i=0;i<sentences.size();i++)
		{
			ps.println(i+" "+sentences.elementAt(i));
			System.out.println(i+" "+sentences.elementAt(i));
		}*/
		
		/* Create lsaMatrix and set its values */
		int rowSize=ti.getNumberOfDifferentWords();
		int columnSize=ti.getSentenceVector().size();
		LSAMatrix lsaMatrix = new LSAMatrix(rowSize,columnSize);

		//for(int valType=0;valType<8;valType++)
		for(int valType=0;valType<1;valType++)
		{
			//ps.println("ValType: "+valType);
			
			/* SetVals is called with 2 params, text info and type
			 * type=0--> number of occurence, type=1-->if occured or not(1/0)
			 * type=2--> tf-idf, type=3-->log-entropy
			 * type=4--> root type, type=5-->word type
			 * type=6--> keywords/keyphrases
			 * type=7--> MY_tf-idf : if tfidf < 0.5 avg(tf-idf) val = 0;
			 */

			lsaMatrix.setVals(ti,valType);

			/// Do SVD decomposition and get the summary
			LSADecomposer lsd = new LSADecomposer();
			//for(int type=0; type<7;type++)
			for(int type=7; type<8;type++)
			{
				//ps.println("AlgType: "+type);
				// getSummary(): type=0--> Gong&Liu, type=1--> Jezek, type=2--> Murray
				// 				 type=3--> Cross(use rows and columns)
				//				 type=4--> Topicalize
				//				 type=5--> Topicalize2(concept count)	
				//				 type=6--> Topicalize3(concept valtotal)
				//				 type=7--> TopicalizeTextRank
				//int type = 0;
				Vector<Sentence> summary = lsd.getSummary(lsaMatrix,ti, 2, summarySentenceCount, type);


			/*	ps.println("Summary");
				for(Sentence s:summary)
				{
					ps.println(s);
				}*/
				
				System.out.println("Summary");
				for(Sentence s:summary)
				{
					System.out.println(s);
				}
			}
		}

	}
}
