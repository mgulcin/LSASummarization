
package textsum;

import textsum.data.*;
import textsum.morphology.*;

import java.util.Vector;
import java.text.BreakIterator;

public class SentenceSeperator {
	
	public Vector <String> ParagrafIntoCumle (String paragraf)
	{	
		//System.out.println("Paragraf:"+paragraf);
		Vector<String> cumleler=new Vector<String>();
		Vector<String> cumleler2=new Vector<String>();
		
		BreakIterator brkit = BreakIterator.getSentenceInstance();
		brkit.setText(paragraf);
		int start = brkit.first();
		int end = brkit.next();
		//System.out.println("Text:"+start+" "+end);

		while (end != BreakIterator.DONE) 
		{
			//System.out.println("Text:"+start+" "+end);
			String sentence = paragraf.substring(start, end);	
			System.out.println("sentence: "+sentence);
			
			int quotation=0;
			for(int i=0;i<sentence.length();i++)
			{
				if(sentence.charAt(i)=='"')quotation++;
				//if(sentence.charAt(i)=='"')quotation++;
			}
			
			if( quotation%2 > 0 )
			{
				end = brkit.next();

			}
			else
			{
				cumleler.add(sentence);
				
				//System.out.println("Cumle: "+sentence);
				start = end;
				end = brkit.next();	

			}
		}
		
		/*for(String cumle:cumleler)
		{
			
			int quotation=0;
			for(int i=0;i<cumle.length();i++)
			{
				if(cumle.charAt(i)=='�')quotation++;
				if(cumle.charAt(i)=='�')quotation++;
			}
			//if(cumle.startsWith("�")) quotation++;
			//if(cumle.endsWith("�")) quotation++;
			
			//System.out.println("bul :" + cumle+" ->"+quotation);
			
			if(quotation==1)
			{
				if(cumle.endsWith(" ") )
				{
					int lenson =cumle .length();
					String str;
					if(cumle.startsWith("�"))
					{
						str = cumle.substring(1,lenson - 2);
						str = str+" "+ cumle.substring(lenson - 2,lenson - 1);
						cumleler2.add(str);
					}
					else if(cumle.endsWith("�"))
					{
						str = cumle.substring(0,lenson - 3);
						str = str+" "+ cumle.substring(lenson - 2,lenson - 1);
						cumleler2.add(str);
					}
				}
				else
				{
					int lenson =cumle .length();
					String str;
					if(cumle.startsWith("�"))
					{
						str = cumle.substring(1,lenson - 1);
						str = str+" "+ cumle.substring(lenson - 1,lenson);
						cumleler2.add(str);
					}
					else if(cumle.endsWith("�"))
					{
						str = cumle.substring(0,lenson - 2);
						str = str+" "+ cumle.substring(lenson - 1,lenson);
						cumleler2.add(str);
					}
				}
			}
			else if(quotation==2)
			{
				//ba�� sonu " ise
				if(cumle.startsWith("�")&&cumle.endsWith("�"))
				{
					if(cumle.endsWith(" ") )
					{
						int lenson =cumle .length();
						String str = cumle.substring(1,lenson - 3);
						str = str+" "+ cumle.substring(lenson - 2,lenson - 1);
						cumleler2.add(str);
					}
					else
					{
						int lenson =cumle.length();
						String str = cumle.substring(0,lenson - 1);
						str = str+" "+ cumle.substring(lenson - 1,lenson);
						cumleler2.add(str);
					}
				}
				//di�erleri
				if(cumle.endsWith(" ") )
				{
					int lenson =cumle .length();
					String str;
					str = cumle.substring(0,lenson - 2);
					str = str+" "+ cumle.substring(lenson - 2,lenson - 1);
					cumleler2.add(str);
				}
				else
				{
					int lenson =cumle.length();
					String str = cumle.substring(0,lenson - 1);
					str = str+" "+ cumle.substring(lenson - 1,lenson);
					cumleler2.add(str);
				}
			}
			else
			{
				if(cumle.endsWith(" ") )
				{
					int lenson =cumle .length();
					String str;
					str = cumle.substring(0,lenson - 2);
					str = str+" "+ cumle.substring(lenson - 2,lenson - 1);
					cumleler2.add(str);
				}
				else
				{
					int lenson =cumle.length();
					String str = cumle.substring(0,lenson - 1);
					str = str+" "+ cumle.substring(lenson - 1,lenson);
					cumleler2.add(str);
				}
			}
		}
		*/
		for(int i=0;i<cumleler.size();i++)
		{
			//System.out.println("bul :" +i+"-> "+ cumleler.get(i));
			if(cumleler.get(i).endsWith(" ") )
			{
				int lenson =cumleler.get(i) .length();
				String str;
				str = cumleler.get(i).substring(0,lenson - 2);
				str = str+" "+ cumleler.get(i).substring(lenson - 2,lenson - 1);
				cumleler2.add(str);
			}
			else
			{
				int lenson =cumleler.get(i) .length();
				String str = cumleler.get(i).substring(0,lenson - 1);
				str = str+" "+ cumleler.get(i).substring(lenson - 1,lenson);
				cumleler2.add(str);
			}
		}
		
		for(int i=0;i<cumleler2.size();i++)
			System.out.println("buldum :" + cumleler2.get(i));
		
		return cumleler2;
	}
	public Vector<Sentence> sentenceSeparator(String sentenceText) 
	{
		//System.out.println("sentenceText: "+sentenceText);
		int var=0;
		Word denemeWord;
		Vector<Sentence> sentences = new Vector<Sentence>();
		Vector<Sentence> sss = new Vector<Sentence>();
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		
		BreakIterator brkit = BreakIterator.getSentenceInstance();
		brkit.setText(sentenceText);
		// iterate across the string
		int start = brkit.first();
		int end = brkit.next();
		while (end != BreakIterator.DONE) 
		{
			String sentence = sentenceText.substring(start, end);
			//System.out.println("sentence-brkit: "+sentence);
			
			// string turunden cumleler elimde
			start = end;
			end = brkit.next();

			/*
			int len2 = sentence.length();
			
			for (int y = 0; y < len2; y++) 
			{
				if (sentence.charAt(y) == ';' ) 
				{
					var=1;
					String[] wordpart = sentence.split(" ");
					int m;
					for (m = 1; m < wordpart.length; m++) 
					{
						if (wordpart[m].endsWith(";")) 
						{
							int len4 = wordpart[m].length();
							String exactword = wordpart[m].substring(0,len4 - 1);
							denemeWord = ma.analyzeWord(exactword);
							//System.out.println("denemeWord:"+denemeWord);
							if (denemeWord.getMorphList().get(0).getRootType().equals("FIIL"))
							{	
								String[] partSent = sentence.split(";");
							
								for(int i=0;i<partSent.length;i++)
								{	
									Sentence b = new Sentence();
									String[] partSentArray = partSent[i].split(" ");
									for(int d=0;d<partSentArray.length;d++)
									{	
										if((d==partSentArray.length-1) && (partSentArray[d].endsWith(".")) )
										{
											int lenson = partSentArray[d].length();
											
											partSentArray[d] = partSentArray[d].substring(0,
													lenson - 1);
										}
										//System.out.println("bosmuki:::"+partSentArray[d]);
										if(partSentArray[d].equals("")==false)
										{
										Word a ;
										a = ma.analyzeWord(partSentArray[d]);
										b.getWords().add(a);
										}
									}
									//System.out.println("cumlebbb:  "+ b.toString());
									Sentence ssent = new Sentence();
									for(int k=0;k<b.getWords().size();k++)
										ssent.getWords().add(b.getWords().get(k));
									sentences.add(ssent);
								}		
							}
						}
					}
				}
				
			}*/
			if(var==0)
			{
				Sentence b = new Sentence();
				String[] partSentArray = sentence.split(" ");
				for(int d=0;d<partSentArray.length;d++)
				{	
					Word a ;
					a = ma.analyzeWord(partSentArray[d]);
					b.getWords().add(a);
				}
				Sentence ssent = new Sentence();
				for(int k=0;k<b.getWords().size();k++)
					ssent.getWords().add(b.getWords().get(k));
				sentences.add(ssent);
				
			}
		}				
		for(int l=0;l<sentences.size();l++)
		{ 	
			Sentence cumle=new Sentence();
			for(int f=0;f<sentences.get(l).getWords().size();f++)
			{
				String den = sentences.get(l).getWords().get(f).toString();
				if(den.endsWith(","))
				{
					int len4 = den.length();
					String exactpunc = ",";
					Word punc;
					punc = ma.analyzeWord(exactpunc);
					String exactden = den.substring(0,len4 - 1);
					Word exactwordden;
					exactwordden = ma.analyzeWord(exactden);
					cumle.getWords().add(exactwordden);
					cumle.getWords().add(punc);
				}
				else{
					Word vv;
					vv=ma.analyzeWord(den);
					//System.out.println("vv new Word: "+vv);
					cumle.getWords().add(vv);
				}
			}
			//System.out.println("cumle:"+ cumle.toString());
			sss.add(cumle);
		}
	return sss;
	}

}
