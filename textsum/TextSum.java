package textsum;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import textsum.data.Sentence;
import textsum.data.Summary;
import textsum.data.TextInfo;
import textsum.data.Word;
import textsum.morphology.MorphologicalAnalyzer;

public class TextSum {
		
	public void getText(TextInfo ti,TextReader reader)
	{
		//read the text and title from file
		String sentenceText = reader.getSentenceText();
		String title = reader.getTitle();
		
		String[] titleArray=title.split(" ");
		/*for(int i=0;i<array.length;i++)
		{
			System.out.println("Array:"+array[i]);
		}*/
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		Vector<Word> titleWordsVector=new Vector<Word>();
		for(int d=0;d<titleArray.length;d++)
		{	
			Word a ;
			a = ma.analyzeWord(titleArray[d]);
			titleWordsVector.add(a);
		}
		
		ti.setTitleWordsVector(titleWordsVector);
		//TextInfo ti=new TextInfo();
		ti.setSentenceText(sentenceText);
		ti.setTitle(title);
	}

	public void splitText(TextInfo ti) 
	{
		//TextInfo ti=new TextInfo();
		String sentenceText = ti.getSentenceText();
		Vector<Vector<Word>> wordsVector = new Vector<Vector<Word>>();
		SentenceSeperator sss = new SentenceSeperator();
		Vector<String> sonuc=sss.ParagrafIntoCumle(sentenceText);
		
		for(int i=0;i<sonuc.size();i++)
		{			
			Vector<Sentence> sentenceVector= sss.sentenceSeparator(sonuc.get(i));
			//System.out.println("Sentences:"+sentenceVector.size());
			//ti.setSentenceVector(sentenceVector);
			ti.appendSentenceVector(sentenceVector);
			for(int j=0;j<sentenceVector.size();j++)
			{
				Vector<Word> words = sentenceVector.get(j).getWords();
				wordsVector.add(words);
				ti.setWordsVector(wordsVector);
			}
		}
	}
	
	public void calculateNoOfDiffWords(TextInfo ti)
	{
		//ti.setNumberOfDifferentWords(ti.getWordGlobalHashMap().size());
		ti.setNumberOfDifferentWords(ti.getDifferentWords().size());
	}
	
	public void findDiffWords(TextInfo ti)
	{
		Vector<Word> differentWords= new Vector<Word>();
		HashMap<String, Integer> wordsMap = ti.getWordGlobalHashMap(); // kelimelerin rootları tutuluyor
		 MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		
		// System.out.println ( "The elements of HashMap are" ) ; 
	     Set<String> set= wordsMap.keySet() ; 
	     Iterator<String> iter = set.iterator() ; 
	     while ( iter.hasNext())  
	     {
	    	 //  System.out.println (iter.next());
	    	 String str =  iter.next().toString();//str kelime kökünü tutuyor zaten
	    	 Word wrd = ma.analyzeWord(str);
	    	 //Word wrd = new Word();wrd.setWordText(str);//kelime sadece kök şimdi
	    	 //control if the word is an element of stop-words
	    	 if(ti.isElementStopWords(wrd)==false)
	    	 {
	    		 //control if the word is Noun(ass only nouns)
	    		 //System.out.println("rootType:"+wrd+" , "+wrd.getMorph());
	    		// if(wrd.getMorph().getRootType()=="ISIM")
	    		 {
	    			 differentWords.add(wrd);
	    		 }
	    	 }
	      }  
		ti.setDifferentWords(differentWords);
	}
		
	public void wordGlobalFrequency(TextInfo ti)//decide global frequency
	{
		HashMap<String, Integer> wordHashMap = new HashMap<String, Integer>();
		//TextInfo ti=new TextInfo();
		Vector<Vector<Word>> wordsVector= ti.getWordsVector();
		for(int indexSentence=0; indexSentence < wordsVector.size();indexSentence++)
		{
			Vector<Word> words = wordsVector.elementAt(indexSentence);
			//int formatBasedScore= calculateFormatScore(words);
			for(Word word:words)
			{
				if(word.getMorph()!=null)
				{
					//System.out.println("GlobalWordHash:"+ word.getWordText()+"; "+wordHashMap.get(word.getWordText()));
					if(wordHashMap.get(word.getMorph().getRoot())!=null)
					{
						Integer val = wordHashMap.get(word.getMorph().getRoot());
						wordHashMap.put(word.getMorph().getRoot(),val+1);	
					}
					else 
					{
						/*int extraScore = calculateExtraScore(word,ti.getTitleWordsVector());
						int val = (int) (0.5*calculatePositionScore(indexSentence,wordsVector.size()));
						val=val+1*formatBasedScore;
						val=val+1*extraScore;
						wordHashMap.put(word.getMorph().getRoot(),val+1);*/
						wordHashMap.put(word.getMorph().getRoot(),1);
					}
				}
			}
			//System.out.println("-----------------");
		}
		
		ti.setWordGlobalHashMap(wordHashMap);
	}

	///
	public void wordLocalFrequency(TextInfo ti)//decide local frequency
	{
		Vector<HashMap<String, Integer>> wordLocalHashMap=new Vector<HashMap<String,Integer>>();
		//TextInfo ti=new TextInfo();
		Vector<Vector<Word>> wordsVector= ti.getWordsVector();
		
		for(int indexSentence=0; indexSentence < wordsVector.size();indexSentence++)
		{
			Vector<Word> words = wordsVector.elementAt(indexSentence);
			HashMap<String, Integer> wordHashMap = new HashMap<String, Integer>();
			for(Word word:words)
			{
				if(word.getMorph()!=null)
				{
					//System.out.println("LocalWordText:"+word.getWordText()+" ; "+wordHashMap.get(word.getWordText()));
					if(wordHashMap.get(word.getMorph().getRoot())!=null)
					{
						Integer val = wordHashMap.get(word.getMorph().getRoot());
						wordHashMap.put(word.getMorph().getRoot(),val+1);			
					}
					else 
					{
						wordHashMap.put(word.getMorph().getRoot(),1);
					}
				}
			}
			wordLocalHashMap.add(wordHashMap);
			//System.out.println("Count:"+wordLocalHashMap.size());
			//System.out.println("--------------------");
		}
		ti.setWordLocalHashMap(wordLocalHashMap);
	}
	
	public void calculateSentenceScore(TextInfo ti)
	{
		Vector<Vector<Word>> wordsVector = ti.getWordsVector();
		HashMap<String, Integer> wordGlobalHashMap = ti.getWordGlobalHashMap();
		Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();
		
		Vector<Integer> sentenceScoreVector=new Vector<Integer>();
		for(int i=0; i<wordsVector.size();i++)
		{
			Integer relevanceScore=0;
			Vector<Word> words = wordsVector.elementAt(i);
			HashMap<String, Integer> wordLocalHash = wordLocalHashMap.get(i);
			for(Word word:words)
			{	
				if(word.getMorph()!=null)
				{
					Integer val = wordLocalHash.get(word.getMorph().getRoot());
					Integer val2 = wordGlobalHashMap.get(word.getMorph().getRoot());
					relevanceScore=relevanceScore+val*val2;
				}
			}
			relevanceScore=relevanceScore/words.size();//normalize score
			sentenceScoreVector.add(relevanceScore);
		}
		ti.setSentenceScoreVector(sentenceScoreVector);
	}
	
	
	public void getSummary(TextInfo ti,Summary sm)
	{
		int index=0;
		Vector<Integer> sumSentencesIndexVector = new Vector<Integer>();
		Vector<Sentence> sumSentencesVector = new Vector<Sentence>();
		for(int count=0;count<sm.getSentenceCountLimit(); count++)
		{
			Vector<Sentence> sentences = ti.getSentenceVector();
			Vector<Integer> sentenceScoreVector = ti.getSentenceScoreVector();
			/*Vector<Vector<Word>> wordsVector = ti.getWordsVector();
			HashMap<String, Integer> wordGlobalHashMap = ti.getWordGlobalHashMap();
			Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();*/
			/*for(int i=0;i<sentences.size();i++)
			{
				System.out.println("i:"+i);
				System.out.println("sentence:"+sentences.elementAt(i).toString());
				System.out.println("score:"+sentenceScoreVector.elementAt(i));
				System.out.println("*******************************");
			}*/
			index = decideMaxScoreIndex(sentenceScoreVector);
			//System.out.println("Max_index:"+index);
			//System.out.println("*******************************");
			sumSentencesIndexVector.add(index);
			sumSentencesVector.add(sentences.elementAt(index));
			removeSentence(ti,index);
		}
		
		Vector<Sentence> sumSentences=sortSumSentences(sumSentencesIndexVector,sumSentencesVector);
		
		for(Sentence s:sumSentences)
		{
			String sumSentence=s.toString();
			int lenson =sumSentence .length();
			String str = sumSentence.substring(0,lenson - 2);
			str = str+sumSentence.substring(lenson - 1,lenson);
			sm.appendSummary(str);
			//System.out.println("sum:"+sm.getSummary());
		}
	}
	
	private int decideMaxScoreIndex(Vector<Integer> sentenceScoreVector)
	{
		int index=0;
		Integer maxVal=0;
		for(int i=0;i<sentenceScoreVector.size();i++)
		{
			Integer val = sentenceScoreVector.elementAt(i);
			if(val>maxVal) 
			{
				maxVal=val;
				index=i;
			}
		}
		return index;
	}
	
	private void removeSentence(TextInfo ti,int index)
	{
		//remove sentence
		ti.getSentenceVector().remove(index);
		//remove words of the sentence from words vector
		ti.getWordsVector().remove(index);
		//re-set frequency and score
		wordGlobalFrequency(ti);
		wordLocalFrequency(ti);
		calculateSentenceScore(ti);
	
	}
	
	private Vector<Sentence> sortSumSentences(Vector<Integer> sumSentencesIndexVector,Vector<Sentence> sumSentencesVector)
	{
		/*for(Sentence s:sumSentencesVector)
			System.out.println("s:"+s.toString());
		for(Integer i:sumSentencesIndexVector)
			System.out.println("i:"+i);
		*/
		Vector<Sentence> sumSentences = new Vector<Sentence>();
		while(sumSentencesIndexVector.size()>0)
		{
			int min=99999999;
			int index=0;
			for(int i=0;i<sumSentencesIndexVector.size();i++)
			{
				if(sumSentencesIndexVector.elementAt(i)<min)
				{
					min=sumSentencesIndexVector.elementAt(i);
					index=i;
				}
			}
			sumSentences.add(sumSentencesVector.elementAt(index));
			sumSentencesIndexVector.remove(index);
			sumSentencesVector.remove(index);
		}
		
		/*for(Sentence s:sumSentences)
			System.out.println("s--:"+s.toString());*/
		//Collections.sort(sumSentencesIndexVector);
		/*for(Integer i:sumSentencesIndexVector)
			System.out.println("i:"+i);*/
		
		return sumSentences;
	}
	
	private int calculatePositionScore(int index,int size)
	{
		int val=0;
		if(index==0) val=7;
		else if(index==1 || index==2) val=4;
		else if(index==size-1 || index==size-2) val=2;
		return val;
	}
	private int calculateFormatScore(Vector<Word> words)
	{
		int val=0;
		String lastWord = words.elementAt(words.size()-1).getWordText();
		if(lastWord.equals("?") || lastWord.equals("!") ) val=0;
		else val=3;
		
		for(Word word:words)
		{
			if(word.getMorph()!=null)
			{
				String wordStr = word.getMorph().getRoot();
				if( wordStr.equals("\"")
						||	wordStr.equals("ancak")||	wordStr.equals("dedi")
						||	wordStr.equals("ve")||	wordStr.equals("ise")
						||	wordStr.equals("��yle")||	wordStr.equals("b�yle")
						||	wordStr.equals("ben")||	wordStr.equals("sen")
						||	wordStr.equals("o")||	wordStr.equals("biz")
						||	wordStr.equals("siz")||	wordStr.equals("onlar")
				)
				{
					val=val-2;
				}	
			}
			if(word.equals("k�saca")||word.equals("k�sacas�")
					||word.equals("�zetle")||word.equals("sonu�"))
			{
				val=val+5;
			}
		}
		return val;
	}
	private int calculateExtraScore(Word word,Vector<Word> title)
	{
		int val=0;
		for(Word titleWord:title)
		{
			if(titleWord.getMorph()!=null && word.getMorph()!=null)
			{
				if(titleWord.getMorph().getRoot().equals(word.getMorph().getRoot()))
					val=5;
			}
		}
		return val;
	}	
	
}
