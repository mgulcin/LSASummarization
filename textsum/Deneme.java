package textsum;

import java.util.Vector;

import textsum.data.Sentence;
import textsum.data.Word;

public class Deneme {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		int totalSentences = 0;
		int totalWords = 0;
		TextReader reader=new TextReader();
		for(int nameIndex=1;nameIndex<51;nameIndex++)
		{
			//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\haberler\\haber"+nameIndex+".txt";
			String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\MakaleSet2\\"+nameIndex+".txt";


			TextToSentence ts = new TextToSentence();
			String text = ts.readFile(fileName);
			Vector<String> sentencesStr = ts.decompose(text);
			Vector<Vector<String>> words = ts.findWords(sentencesStr);
			Vector<Vector<Word>> wordsVec = ts.getWordsVector(words);
			Vector<Sentence> sentenceVec = ts.getSentenceVector(wordsVec,sentencesStr);
			
			totalSentences = totalSentences + sentenceVec.size();
			for(int i=0;i<wordsVec.size();i++)
			{
				Vector<Word> word = wordsVec.elementAt(i);
				totalWords = totalWords + word.size();
			}
			
		}
		
		System.out.println("totalSentences:"+totalSentences);
		System.out.println("totalWords:"+totalWords);
	}

}
