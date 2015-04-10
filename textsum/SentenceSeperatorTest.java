package textsum;

import java.util.Vector;
import textsum.morphology.*;
import textsum.data.*;


public class SentenceSeperatorTest {
	public static void main( String[] args )
	{
		Vector<Sentence> cumleler = new Vector<Sentence>();
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		SentenceSeperator sss = new SentenceSeperator();
	
		//sss.sentenceSeparator(s.toString());
		String cumle = "Ali geldi. Top düþtü! O geri geldi mi? Neyin var dedi. ";
		//Vector<Sentence> cumle2 = sss.sentenceSeparator(cumle);
		Vector<String> sonuc=sss.ParagrafIntoCumle(cumle);
		for(int i=0;i<sonuc.size();i++)
		{			
			Vector<Sentence> sentence= sss.sentenceSeparator(sonuc.get(i));
			for(int j=0;j<sentence.size();j++)
			{
				Vector<Word> words = sentence.get(j).getWords();
				for(int k=0;k<words.size();k++)
				{
					System.out.println("Sonuc:"+words.get(k).getMorphList());	
				}
			}
		}
	}
}
