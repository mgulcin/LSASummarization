package textsum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import textsum.data.LSAMatrix;
import textsum.data.Sentence;
import textsum.data.TextInfo;
import textsum.data.Word;
import textsum.morphology.MorphologicalAnalyzer;

public class Summarizer {
	public static void addStopWord(TextInfo ti)
	{
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		//http://www.ranks.nl/stopwords/turkish.html
		ti.addStopWord(ma.analyzeWord("acaba"));
		ti.addStopWord(ma.analyzeWord("altmış"));
		ti.addStopWord(ma.analyzeWord("altı"));
		ti.addStopWord(ma.analyzeWord("ama"));
		ti.addStopWord(ma.analyzeWord("bana"));
		ti.addStopWord(ma.analyzeWord("bazı"));
		ti.addStopWord(ma.analyzeWord("belki"));
		ti.addStopWord(ma.analyzeWord("ben"));
		ti.addStopWord(ma.analyzeWord("beni"));
		ti.addStopWord(ma.analyzeWord("benim"));
		ti.addStopWord(ma.analyzeWord("beş"));
		ti.addStopWord(ma.analyzeWord("bin"));
		ti.addStopWord(ma.analyzeWord("bir"));
		ti.addStopWord(ma.analyzeWord("bana"));
		ti.addStopWord(ma.analyzeWord("biri"));
		ti.addStopWord(ma.analyzeWord("birkaç"));
		ti.addStopWord(ma.analyzeWord("birkez"));
		ti.addStopWord(ma.analyzeWord("birşey"));
		ti.addStopWord(ma.analyzeWord("birşeyi"));
		ti.addStopWord(ma.analyzeWord("biz"));
		ti.addStopWord(ma.analyzeWord("bizden"));
		ti.addStopWord(ma.analyzeWord("bizi"));
		ti.addStopWord(ma.analyzeWord("bizim"));
		ti.addStopWord(ma.analyzeWord("bu"));
		ti.addStopWord(ma.analyzeWord("buna"));
		ti.addStopWord(ma.analyzeWord("bundan"));
		ti.addStopWord(ma.analyzeWord("bunu"));
		ti.addStopWord(ma.analyzeWord("bunun"));
		ti.addStopWord(ma.analyzeWord("da"));
		ti.addStopWord(ma.analyzeWord("daha"));
		ti.addStopWord(ma.analyzeWord("dahi"));
		ti.addStopWord(ma.analyzeWord("de"));
		ti.addStopWord(ma.analyzeWord("defa"));
		ti.addStopWord(ma.analyzeWord("diye"));
		ti.addStopWord(ma.analyzeWord("doksan"));
		ti.addStopWord(ma.analyzeWord("dokuz"));
		ti.addStopWord(ma.analyzeWord("dört"));
		ti.addStopWord(ma.analyzeWord("elli"));
		ti.addStopWord(ma.analyzeWord("en"));
		ti.addStopWord(ma.analyzeWord("gibi"));
		ti.addStopWord(ma.analyzeWord("hem"));
		ti.addStopWord(ma.analyzeWord("hep"));
		ti.addStopWord(ma.analyzeWord("hepsi"));
		ti.addStopWord(ma.analyzeWord("her"));
		ti.addStopWord(ma.analyzeWord("hiç"));
		ti.addStopWord(ma.analyzeWord("iki"));
		ti.addStopWord(ma.analyzeWord("ile"));
		ti.addStopWord(ma.analyzeWord("mi"));
		ti.addStopWord(ma.analyzeWord("ise"));
		ti.addStopWord(ma.analyzeWord("için"));
		ti.addStopWord(ma.analyzeWord("katrilyon"));
		ti.addStopWord(ma.analyzeWord("kez"));
		ti.addStopWord(ma.analyzeWord("ki"));
		ti.addStopWord(ma.analyzeWord("kim"));
		ti.addStopWord(ma.analyzeWord("kimden"));
		ti.addStopWord(ma.analyzeWord("kime"));
		ti.addStopWord(ma.analyzeWord("kimi"));
		ti.addStopWord(ma.analyzeWord("kırk"));
		ti.addStopWord(ma.analyzeWord("milyar"));
		ti.addStopWord(ma.analyzeWord("milyon"));
		ti.addStopWord(ma.analyzeWord("mu"));
		ti.addStopWord(ma.analyzeWord("mı"));
		ti.addStopWord(ma.analyzeWord("mü"));
		ti.addStopWord(ma.analyzeWord("nasıl"));
		ti.addStopWord(ma.analyzeWord("ne"));
		ti.addStopWord(ma.analyzeWord("neden"));
		ti.addStopWord(ma.analyzeWord("nerde"));
		ti.addStopWord(ma.analyzeWord("nerede"));
		ti.addStopWord(ma.analyzeWord("nereye"));
		ti.addStopWord(ma.analyzeWord("niye"));
		ti.addStopWord(ma.analyzeWord("niçin"));
		ti.addStopWord(ma.analyzeWord("on"));
		ti.addStopWord(ma.analyzeWord("ona"));
		ti.addStopWord(ma.analyzeWord("ondan"));
		ti.addStopWord(ma.analyzeWord("onlar"));
		ti.addStopWord(ma.analyzeWord("onlardan"));
		ti.addStopWord(ma.analyzeWord("onları"));
		ti.addStopWord(ma.analyzeWord("onların"));
		ti.addStopWord(ma.analyzeWord("onu"));
		ti.addStopWord(ma.analyzeWord("otuz"));
		ti.addStopWord(ma.analyzeWord("sanki"));
		ti.addStopWord(ma.analyzeWord("sekiz"));
		ti.addStopWord(ma.analyzeWord("seksen"));
		ti.addStopWord(ma.analyzeWord("sen"));
		ti.addStopWord(ma.analyzeWord("senden"));
		ti.addStopWord(ma.analyzeWord("seni"));
		ti.addStopWord(ma.analyzeWord("senin"));
		ti.addStopWord(ma.analyzeWord("siz"));
		ti.addStopWord(ma.analyzeWord("sizden"));
		ti.addStopWord(ma.analyzeWord("sizi"));
		ti.addStopWord(ma.analyzeWord("sizin"));
		ti.addStopWord(ma.analyzeWord("trilyon"));
		ti.addStopWord(ma.analyzeWord("tüm"));
		ti.addStopWord(ma.analyzeWord("ve"));
		ti.addStopWord(ma.analyzeWord("veya"));
		ti.addStopWord(ma.analyzeWord("ya"));
		ti.addStopWord(ma.analyzeWord("yani"));
		ti.addStopWord(ma.analyzeWord("yedi"));
		ti.addStopWord(ma.analyzeWord("yetmiş"));
		ti.addStopWord(ma.analyzeWord("yirmi"));
		ti.addStopWord(ma.analyzeWord("yüz"));
		ti.addStopWord(ma.analyzeWord("çok"));
		ti.addStopWord(ma.analyzeWord("çünkç"));
		ti.addStopWord(ma.analyzeWord("üç"));
		ti.addStopWord(ma.analyzeWord("şey"));
		ti.addStopWord(ma.analyzeWord("şeyden"));
		ti.addStopWord(ma.analyzeWord("şeyi"));
		ti.addStopWord(ma.analyzeWord("şeyler"));
		ti.addStopWord(ma.analyzeWord("şu"));
		ti.addStopWord(ma.analyzeWord("şuna"));
		ti.addStopWord(ma.analyzeWord("şunda"));
		ti.addStopWord(ma.analyzeWord("şundan"));
		ti.addStopWord(ma.analyzeWord("şunu"));

		// added by makbule
		ti.addStopWord(ma.analyzeWord("değil"));
		ti.addStopWord(ma.analyzeWord("şöyle"));
		ti.addStopWord(ma.analyzeWord("böyle"));
		ti.addStopWord(ma.analyzeWord("etmek"));
		ti.addStopWord(ma.analyzeWord("eylemek"));
		ti.addStopWord(ma.analyzeWord("olmak"));
		ti.addStopWord(ma.analyzeWord("kılmak"));
		ti.addStopWord(ma.analyzeWord("kadar"));
		ti.addStopWord(ma.analyzeWord("kendi"));
		ti.addStopWord(ma.analyzeWord("."));
		ti.addStopWord(ma.analyzeWord(","));
		ti.addStopWord(ma.analyzeWord(";"));
		ti.addStopWord(ma.analyzeWord(":"));
		ti.addStopWord(ma.analyzeWord(" "));
		ti.addStopWord(ma.analyzeWord("\n"));
		ti.addStopWord(ma.analyzeWord("\""));
		ti.addStopWord(ma.analyzeWord("'"));
		ti.addStopWord(ma.analyzeWord("`"));
	}

	public static void printMatrix(TextInfo ti,double rowSize,double columnSize,double[][] matrix)
	{	
		System.out.println("Initial Matrix:");
		for(int i=0;i<rowSize;i++)
		{
			//System.out.println("****************lsaMatrix:"+lsaMatrix.getRowSize()+" "+ti.getDifferentWords());
			//System.out.print(ti.getDifferentWordsNouns().elementAt(i)+" ");
			System.out.print(ti.getDifferentWords().elementAt(i)+" ");	
			for(int j=0;j<columnSize;j++)
			{	
				System.out.format("%.3f",matrix[i][j]);
				System.out.print(" ");
			}

			System.out.println("");
		} 
	}
	public static void printMatrix(double rowSize,double columnSize,double[][] matrix,String fileoutPath) throws IOException
	{
		FileOutputStream fos; 
		//DataOutputStream dos;
		//File file= new File(fileoutPath);
		fos = new FileOutputStream(fileoutPath);
		PrintStream ps = new PrintStream(fos);
		//dos=new DataOutputStream(fos);

		//BufferedWriter out = new BufferedWriter(new FileWriter(fileoutPath,true));

		//System.out.println("Matrix:");
		for(int i=0;i<rowSize;i++)
		{
			//System.out.println("****************lsaMatrix:"+lsaMatrix.getRowSize()+" "+ti.getDifferentWords());
			//System.out.print(ti.getDifferentWordsNouns().elementAt(i)+" ");
			//System.out.print(ti.getDifferentWords().elementAt(i)+" ");	
			for(int j=0;j<columnSize;j++)
			{	
				//System.out.format("%.3f",matrix[i][j]);
				//System.out.print(" ");

				// Print 
				ps.printf("%.3f",matrix[i][j]);
				ps.print(";");

				//dos.writeDouble(matrix[i][j]);
				//dos.writeChars(" ");
			}

			//System.out.println("");
			//dos.writeChars("");
			ps.println("");
		}
		// Close our output stream
		fos.close();
	}
	public static void main( String[] args) throws IOException
	{
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\bilimTeknik1.txt";
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\gonencErcanTez1.txt";
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\haber6.txt";
		//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\usb-060909\\LSA\\LSA\\TextSum\\input\\bt2.txt";
		//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset-MakaleSet1-my\\1.txt";
		//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\haberler\\haber2.txt";
		TextReader reader=new TextReader();

		//reader.title = "Deneme";
		//reader.sentenceText = "Adam köpeğini yürüyüşe çıkardı. Adam köpeğini parka götürdü. Köpek parka gitti.";
		//reader.sentenceText ="Ali k�z�n�n babas�d�r. Fatma o�lunun annesidir. K�z ve o�ul karde�tir."; //Karde�lerin anne ve babalar�ndan biri ayn�d�r.";
		//reader.sentenceText = "Ali annesine bir gül verdi. Annesi güldü. Annesi ona teşekkür etti. ";

		for(int nameIndex=44;nameIndex<45;nameIndex++)
		{
			//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\haberler\\haber"+nameIndex+".txt";
			String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset\\MakaleSet2\\"+nameIndex+".txt";
			//int summarySentenceCount = reader.readFile(fileName);
			//reader.decompose();

			//System.out.println("WholeText:\n"+reader.getWholeText());
			//System.out.println("Title: "+reader.getTitle());
			//System.out.println("SentenceText: \n"+reader.getSentenceText());
			//System.out.println("SummarySentenceCount: \n"+summarySentenceCount);

			TextToSentence ts = new TextToSentence();
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
			//ti.setTitle(title);
			//ti.setTitleWordsVector(titleWordsVector);
			ti.setSentenceVector(sentenceVec);
			ti.setWordsVector(wordsVec);
			
			TextSum textsum = new TextSum();
			//textsum.getText(ti,reader);
			//textsum.splitText(ti);
			textsum.wordGlobalFrequency(ti);
			textsum.wordLocalFrequency(ti);
			textsum.findDiffWords(ti);
			textsum.calculateNoOfDiffWords(ti);
			//textsum.calculateSentenceScore(ti);
			Vector<Sentence> sentences = ti.getSentenceVector();

/*			System.out.println("Size: " +sentences.size());
			for(int i=0;i<sentences.size();i++)
			{
				System.out.println(i+" "+sentences.elementAt(i));
			}
*/
			//String fileoutPath ="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\dataset-haberler-sonucMy\\haber"+nameIndex+".txt";
			String fileoutPath ="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\dataset-MakaleSet2-sonucMy\\textRank2\\"+nameIndex+".txt";
			FileOutputStream fos; 
			//DataOutputStream dos;
			//File file= new File(fileoutPath);
			fos = new FileOutputStream(fileoutPath);
			PrintStream ps = new PrintStream(fos);

			System.out.println("nameIndex: "+nameIndex);
			for(int i=0;i<sentences.size();i++)
			{
				ps.println(i+" "+sentences.elementAt(i));
				System.out.println(i+" "+sentences.elementAt(i));
			}



			/* Create lsaMatrix and set its values */
			int rowSize=ti.getNumberOfDifferentWords();
			int columnSize=ti.getSentenceVector().size();
			LSAMatrix lsaMatrix = new LSAMatrix(rowSize,columnSize);

			for(int valType=0;valType<8;valType++)
			{
				ps.println("ValType: "+valType);
				/* SetVals is called with 2 params, text info and type
				 * type=0--> number of occurence, type=1-->if occured or not(1/0)
				 * type=2--> tf-idf, type=3-->log-entropy
				 * type=4--> root type, type=5-->word type
				 * type=6--> keywords/keyphrases
				 * type=7--> MY_tf-idf : if tfidf < 0.5 avg(tf-idf) val = 0;
				 */
				lsaMatrix.setVals(ti,valType);


				//print the initial matrix
				//String fileoutPath ="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset-MakaleSet1-my\\1.v2-deneme.txt";
				//printMatrix(lsaMatrix.getRowSize(),lsaMatrix.getColumnSize(),lsaMatrix.getMatrix(),fileoutPath);
				//System.out.println("RowSize: "+lsaMatrix.getRowSize()+" ColumnSize: "+lsaMatrix.getColumnSize());

				/// Do SVD decomposition and get the summary
				LSADecomposer lsd = new LSADecomposer(); 

				for(int type=7; type<8;type++)
				{
					//ps.println("AlgType: "+type);
					// getSummary(): type=0--> Gong&Liu, type=1--> Jezek, type=2--> Murray
					// 				 type=3--> Cross(use rows and columns)
					//				 type=4--> Topicalize
					//				 type=5--> Topicalize2(concept count)	
					//				 type=6--> Topicalize3(concept valtotal)
					//int type = 0;
					if(summarySentenceCount==0) summarySentenceCount = 3;
					Vector<Sentence> summary = lsd.getSummary(lsaMatrix,ti, 2, summarySentenceCount, type);
					
					/*System.out.println("Summary");
					for(Sentence s:summary)
					{
						System.out.println(s);
					}*/

					ps.println("Summary");
					for(Sentence s:summary)
					{
						ps.println(s);
					}


				}
			/*	lsd.getLsaElement().printU(2);
				lsd.getLsaElement().printVT(2);
				lsd.getLsaElement().printS(2);*/
			}
			
			fos.close();
			
			/*Do not use printMatrix with words, for matrix vals set acc cluster or word type*/
			//printMatrix(ti,lsaMatrix.getRowSize(),lsaMatrix.getColumnSize(),lsaMatrix.getMatrix());
			/*
		lsd.getLsaElement().printU(2);
		lsd.getLsaElement().printVT(2);
		lsd.getLsaElement().printS(2);
			 */
		}
	}
}
