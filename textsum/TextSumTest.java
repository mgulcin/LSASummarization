package textsum;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;

import textsum.data.LSAMatrix;
import textsum.data.Sentence;
import textsum.data.Summary;
import textsum.data.TextInfo;
import textsum.data.Word;
import textsum.morphology.MorphologicalAnalyzer;

public class TextSumTest {
	
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

	}
	
	public static void main( String[] args ) throws IOException
	{
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\bilimTeknik1.txt";
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\gonencErcanTez1.txt";
		//String fileName="C:\\Documents and Settings\\mozsoy\\Desktop\\LSA\\LSA\\proje_doc\\haber6.txt";
		//String fileName="C:\\Documents and Settings\\Owner\\Desktop\\usb-060909\\LSA\\LSA\\TextSum\\input\\bt2.txt";
		String fileName="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset-MakaleSet1-my\\1.txt";
		TextReader reader=new TextReader();
		
		//reader.title = "Deneme";
		//reader.sentenceText = "Adam k�pe�ini y�r�y��e ��kard�. Adam k�pe�ini parka g�t�rd�. K�pek parka gitti.";
		//reader.sentenceText ="Ali k�z�n�n babas�d�r. Fatma o�lunun annesidir. K�z ve o�ul karde�tir."; //Karde�lerin anne ve babalar�ndan biri ayn�d�r.";
		//reader.sentenceText = "Ali annesine bir gül verdi. Annesi güldü. Annesi ona teşekkür etti. ";
		
		//reader.wholeText = "K���k Prens Frans�z yazar ve pilot Antoine de Saint-Exup�ry'nin en �nl� roman�. Kitap 1943'te yay�mlanm��t�r. Roman New York'ta bir otel odas�nda yaz�lm��t�r. Kitapta Exup�ry'nin �izimleri de bulunur. Basit bir �ocuk kitab� gibi g�r�nen ama asl�nda ya�am, sevgi ve a�k hakk�nda derin anlamlar i�eren K���k Prens'te bir �ocu�un g�z�nden b�y�klerin d�nyas� anlat�l�r. Sahra ��l�'ne d��en pilotun K���k Prens'le kar��la�mas� ile ba�layan kitapta K���k Prens'in a�z�ndan Saint-Exup�ry, insanlar�n hatalar�n� ve aptall�klar�n�, b�y�d�kleri zaman unuttuklar� basit �ocuk bak���n� vurgular.";
		//reader.title = "K���k Prens";
		//1 reader.sentenceText = "K���k Prens Frans�z yazar ve pilot Antoine de Saint-Exup�ry'nin en �nl� roman�. Kitap 1943'te yay�mlanm��t�r. Roman New York'ta bir otel odas�nda yaz�lm��t�r. Kitapta Exup�ry'nin �izimleri de bulunur. Basit bir �ocuk kitab� gibi g�r�nen ama asl�nda ya�am, sevgi ve a�k hakk�nda derin anlamlar i�eren K���k Prens'te bir �ocu�un g�z�nden b�y�klerin d�nyas� anlat�l�r. Sahra ��l�'ne d��en pilotun K���k Prens'le kar��la�mas� ile ba�layan kitapta K���k Prens'in a�z�ndan Saint-Exup�ry, insanlar�n hatalar�n� ve aptall�klar�n�, b�y�d�kleri zaman unuttuklar� basit �ocuk bak���n� vurgular.";
		//2 reader.sentenceText = "Kitapta K���k Prens'in ya�ad��� asteroidi (B612) bulan bir T�rk astronomdur. Hatta bu astronom asteroidi uluslararas� bir kongrede anlat�r ama fesli kafas� ve do�ulu giysilerinden dolay� kimse onu dinlemez, ama bir T�rk diktat�r�n k�yafet devrimi yap�p herkesi Avrupal� gibi giyinmeye zorlamas�ndan sonra ayn� astronom bu defa modern k�yafetlerle kongreye kat�l�r ve herkes ikna olur. Atat�rk'� bir diktat�r olarak tan�tan bu sat�rlar y�z�nden uzun y�llar T�rk okuyucusu kitab� sans�rl� okudu. Yine bu y�zden kitap, ele�tirilere maruz kalabilece�i gerek�esiyle 2005 y�l�nda ilk��retim ��rencilerine �nerilmek �zere haz�rlanm�� olan 100 Temel Eser aras�ndan ��kar�ld�. D�nya �ap�nda �ok okunan ve �ok sevilen bu kitab�n yazar� Saint Exup�ry, kitab� yazd�ktan alt� y�l sonra Le Petit Prince adl� bir u�akla ke�if u�u�u yaparken Akdeniz �zerinde kaybolur ve bir daha kendisinden haber al�namaz. Fransa'da �ok sevilen K���k Prens'in resmi 50 frankl�k banknotlar�n �zerine bas�lm��t�r. ";
		//3 reader.sentenceText ="Bu kitab� bir b�y��e sunuyor olmamdan dolay� �ocuk okurlar�m�n beni ho� g�rmelerini dilerim. Bunu yapmam�n �ok ciddi bir nedeni var: O. Benim d�nyadaki en iyi arkada��m. �kinci nedenim de �u: Bu adam her �eyi anl�yor, �ocuk kitaplar�n� bile. ���nc� bir nedenim daha var: Fransa'da ya��yor �u anda, a� ve ���yor. Biraz y�re�inin �s�t�lmas� ona iyi gelir. E�er b�t�n bu nedenler size yeterli gelmiyorsa, o zaman ben de bu kitab� onun �ocuklu�una arma�an ederim. B�t�n b�y�klerin bir zamanlar �ocuk oldu�unu biliyoruz: pek az� bunu hat�rlasa da... Neyse, sunu�umu ��ylece de�i�tiriyorum:Leon Werth'in �ocuklu�una...";
		//4	reader.sentenceText = "Alt� ya��mdayken, ilk �a��n ormanlar�n� anlatan \"Ger�ek �yk�ler\" adl� bir kitapta �ok g�zel bir resim g�rm��t�m. Bir boa y�lan� av�n� yutmak �zereyken resmedilmi�ti ��te bu �izimin bir kopyas�: Resim1. Kitapta �unlar yaz�l�yd�: \"Boa y�lan� av�n� �i�nemeden, b�t�n olarak yutar ve hareket edemez hale gelir, Sonra da onu sindirebilmek i�in alt� ay boyunca uyur\". Bu orman maceralar� �zerinde uzun uzun d���nd�m, sonra renkli bir kalemle ilk resmimi yapmay� ba�ard�m. Resim1 No�lu resmim i�te ��yle bir �eydi: Resim2. �aheserimi b�y�klere g�sterdim ve korkup korkmad�klar�n� sordum. Ama onlar:\"Korkmak m�, Bir �apkadan niye korkal�m ki\" dediler. Oysa �izdi�im resim bir �apkaya ait de�ildi. Koca bir fili sindirmekte olan bir boa y�lan�n� �izmi�tim ben. Neyse, b�y�kler anlayabilsin diye ba�ka bir resim daha �izdim. Bu kez boa y�lan�n�n midesindeki fili a��k se�ik g�stermi�tim. �u b�y�klere hep a��klama yapmak gerekiyor. �kinci resmim ise ��yle bir �ey oldu: Resim3.";
		//5 reader.sentenceText = "Bu kez b�y�klerin cevab� boa y�lan�n� i�ten ya da d��tan �izmeyi bir yana b�rak�p, co�rafya, tarih, aritmetik ve gramerle ilgilenmemi tavsiye etmek oldu. B�ylece alt� ya��mdayken resim kariyerimi terk etmek zorunda kald�m. �lk iki resmimin ba�ar�s�z olmas� beni hayal k�r�kl���na u�ratm��t�. B�y�kler kendi ba�lar�na hi�bir �eyi anlayam�yor, �ocuklar ise ayn� �eyin tekrar tekrar anlat�lmas�ndan s�k�l�yorlard�. Bu y�zden ba�ka bir meslek se�mek zorunda kald�m ve pilot oldum. D�nyan�n hemen hemen her yerine u�tum. Do�rusunu isterseniz, co�rafya bilgilerim �ok i�ime yarad�. �imdi bir bak��ta �in ile Arizona�y� birbirinden ay�rabiliyorum. Ayr�ca gece vakti kayboldu�unuzda co�rafya �ok i�inize yarar. Neyse, mesle�im gere�i , ya�am�m boyunca bir�ok �nemli insanla bir arada bulundum. B�y�klerle �ok vakit ge�irdim. Ama korkar�m bu yak�n ili�kiler bile benim onlar hakk�ndaki d���ncelerimi de�i�tirmedi. Ne zaman yeterince zeki oldu�unu d���nd���m biriyle kar��la�sam, ona hemen 1 No�lu resmimi g�sterdim. (Bu resmi hep yan�mda ta��yordum, ��nk� ilk deneyimimdi.) Bakal�m onu ger�ekten anlayabilecek miydi. Ama hepsi bunun bir �apka oldu�unu s�ylediler. Bu y�zden ben de boa y�lanlar�ndan, ilk �a�daki ormanlardan, ya da y�ld�zlardan bahsetmeyi b�rak�p onlar�n seviyesine indim. Onlarla bri�, golf, politika ve boyun ba�lar� hakk�nda konu�tum. B�ylece bu yeti�kinler benim gibi duyarl� biriyle kar��la�t�klar� i�in mutlu oldular.";
		
		reader.readFile(fileName);
		reader.decompose();
		
		//System.out.println("WholeText:\n"+reader.getWholeText());
		//System.out.println("Title: "+reader.getTitle());
		//System.out.println("SentenceText: \n"+reader.getSentenceText());
		
		
		TextInfo ti = new TextInfo();
		addStopWord(ti);

		
		TextSum textsum = new TextSum();
		textsum.getText(ti,reader);
		textsum.splitText(ti);
		textsum.wordGlobalFrequency(ti);
		textsum.wordLocalFrequency(ti);
		textsum.findDiffWords(ti);
		textsum.calculateNoOfDiffWords(ti);
		//textsum.findDiffWordsNouns(ti);
		//textsum.calculateNoOfDiffWordsNouns(ti);
		//textsum.calculateSentenceScore(ti);
		Vector<Sentence> sentences = ti.getSentenceVector();
		/*
		System.out.println("Size: " +sentences.size());
		for(int i=0;i<sentences.size();i++)
		{
			System.out.println(i+" "+sentences.elementAt(i));
		}
		*/
		
		
		
		 /* herbir kelime için*/
		 int rowSize=ti.getNumberOfDifferentWords();
		 int columnSize=ti.getSentenceVector().size();
		 LSAMatrix lsaMatrix = new LSAMatrix(rowSize,columnSize);
		 lsaMatrix.setVals(ti,0);
		
		
		/**herbir "isim(noun)için"
		 LSAMatrix lsaMatrix = new LSAMatrix(ti.getNumberOfDifferentWordsNouns(),ti.getSentenceVector().size());
		 lsaMatrix.setValsNouns(ti);
		 */
		
		/**clustered words*//*
		LSAMatrix lsaMatrix = new LSAMatrix(3,3);
		double[] matrixValArray={2,2,1,
								 1,1,1,
								 1,1,1};
		
		lsaMatrix.setVals(matrixValArray,3,3);*/
		 
		 
		// cells: how many times term appeared in sentence
		double [][]matrix=lsaMatrix.getMatrix();
		
		//cells: if term appears in sentence/not
		boolean appearance=false;
		if(appearance)
		{
			for(int i=0;i<ti.getNumberOfDifferentWords();i++)
			{
				for(int j=0;j<ti.getSentenceVector().size();j++)
				{
					if(matrix[i][j]>0) matrix[i][j]=1;
				}
			}
		}
		
		//cells: tf-idf
		boolean tf_idf  = false;
		if(tf_idf)
		{
			int D = ti.getSentenceVector().size();//number of documents
			
			double[] totalTF = new double[D];
			for(int j=0;j<ti.getSentenceVector().size();j++)
			{
				double totalTFVal=0;//sum of number of occurrences of all terms in document dj
				for(int i2=0;i2<ti.getNumberOfDifferentWords();i2++)
				{
					totalTFVal += matrix[i2][j];
				}
				totalTF[j]=totalTFVal;
			}
			
			
			for(int i=0;i<ti.getNumberOfDifferentWords();i++)
			{
				double d = 0; //number of documents where the term ti appears
				for(int j=0;j<ti.getSentenceVector().size();j++)
				{
					if(matrix[i][j]>0) d++;
				}
				double idf ;
				if(d==0) idf =0;
				else idf = Math.log(D/d);
		
				
				for(int j=0;j<ti.getSentenceVector().size();j++)
				{
					
					double tf;
					if (totalTF[j]==0) tf =0;
					else tf = matrix[i][j]/totalTF[j];//the number of occurrences of the considered term (ti) in document dj/totalTF

					matrix[i][j] = tf * idf;
				}
			}
		}
	

		//cells: logEntropy
		boolean logEntropy  = false;
		if(logEntropy)
		{
			/*
			 *  Essential Dimensions of Latent Semantic Indexing (LSI) by April Kontostathis
			 *  i: term index; j: document index; n: number of documents; 
			 * 	p(i, j): probability of term i is appeared in document j.
			 * 	f(i,j): number of times term i is appeared in document j.
			 *  summ-for all j: p(i,j)log2(p(i,j))
			 *  global(i) = 1 + ((summ)/log2(n))
			 *  p(i,j) = f(i,j)/total(f(i,j)) for all sentences
			 *  
			 *  local(i)= log2(1+f(i,j))*g(i)
			 *  
			 *  calculate val = global*local
			 *  then do normalization --> val/wordcount in sentence
			 */
			int termCount = ti.getNumberOfDifferentWords();
			int sentCount = lsaMatrix.getColumnSize();
			double[] global = new double[termCount];
			double[] total = new double[termCount];
			double[][] local = new double[termCount][sentCount];
			double[][] p = new double[termCount][sentCount];
			double[] totalFreq = new double[termCount];
			for(int i=0;i<termCount;i++)
			{
				for(int j=0;j<sentCount;j++)
				{
					totalFreq[i] = totalFreq[i] + matrix[i][j];
				}
			}
			
			for(int i=0;i<termCount;i++)
			{
				for(int j=0;j<sentCount;j++)
				{
					if(totalFreq[i]==0) p[i][j]=0;
					else p[i][j] = matrix[i][j]/totalFreq[i];
				}
			}
			
			for(int i=0;i<termCount;i++)
			{
				for(int j=0;j<sentCount;j++)
				{
					double log2Total = 0;
					if(p[i][j]!=0)log2Total = Math.log((double)p[i][j])/Math.log(2);
					total[i] = total[i] + p[i][j]*log2Total;
				}
			}
			
			
			for(int i=0;i<termCount;i++)
			{
				//global
				double log2N = Math.log((double)sentCount)/Math.log(2);
				global[i] = 1+(total[i]/log2N);
				
				//local
				for(int j=0;j<sentCount;j++)
				{
					double log2Local = Math.log(1+matrix[i][j])/Math.log(2);
					local[i][j] = log2Local*global[i];
				}
			}
			
			double[] wordCount = new double[sentCount];

			for(int j=0;j<sentCount;j++)
			{
				for(int i=0;i<termCount;i++)
				{
					wordCount[j]= wordCount[j] + matrix[i][j];
				}
			}
			
			for(int i=0;i<termCount;i++)
			{
				for(int j=0;j<sentCount;j++)
				{
					matrix[i][j] = global[i]*local[i][j];
					//then do normalization
					matrix[i][j] = matrix[i][j] / wordCount[j];
				}
			}
		}

		
		/*////print the initial matrix
		
		String fileoutPath ="C:\\Documents and Settings\\Owner\\Desktop\\dataset-TEZ\\Dataset-MakaleSet1-my\\1-TfIdf.txt";
		FileOutputStream fos; 
	    //DataOutputStream dos;
	    //File file= new File(fileoutPath);
	    fos = new FileOutputStream(fileoutPath);
	    PrintStream ps = new PrintStream(fos);
	    //dos=new DataOutputStream(fos);
	    
		//BufferedWriter out = new BufferedWriter(new FileWriter(fileoutPath,true));
	      
		System.out.println("Initial Matrix:");
		System.out.println("****************lsaMatrix:"+lsaMatrix.getRowSize()+" "+ti.getDifferentWordsNouns());
		for(int i=0;i<lsaMatrix.getRowSize();i++)
		{
			//System.out.println("****************lsaMatrix:"+lsaMatrix.getRowSize()+" "+ti.getDifferentWords());
			//System.out.print(ti.getDifferentWordsNouns().elementAt(i)+" ");
			//System.out.print(ti.getDifferentWords().elementAt(i)+" ");
			
			for(int j=0;j<lsaMatrix.getColumnSize();j++)
			{
				
				System.out.format("%.3f",matrix[i][j]);
				System.out.print(" ");
			
				// Print 
			    ps.printf("%.3f",matrix[i][j]);
			    ps.print(" ");
			    
				//dos.writeDouble(matrix[i][j]);
				//dos.writeChars(" ");
			}
			
			System.out.println("");
			//dos.writeChars("");
			ps.println("");
		}
		 // Close our output stream
	    fos.close();*/
		
		
		/// Do SVD decomposition and get the summary
//		Vector<Sentence> summary = lsaMatrix.getSummary(ti,2,(int) (ti.getSentenceVector().size()*0.25+0.5));
		//Vector<Sentence> summary = lsaMatrix.getSummary(ti,2,2);
		LSADecomposer lsd = new LSADecomposer();
		Vector<Sentence> summary = lsd.getSummary(lsaMatrix,ti, 2, 2, 2);
	    System.out.println("Summary");
		for(Sentence s:summary)
		{
			System.out.println(s);
		}
		
		
		//Vector<Sentence> sentences = ti.getSentenceVector();
		//Vector<Integer> sentenceScoreVector = ti.getSentenceScoreVector();
		//Vector<Vector<Word>> wordsVector = ti.getWordsVector();
		//HashMap<String, Integer> wordGlobalHashMap = ti.getWordGlobalHashMap();
		//Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();
		//System.out.println("Size: " +sentences.size());
		
		/*Summary sum=new Summary();
		int limit = (int)(sentences.size()*0.25+0.5);
		if(limit==0) limit=1;
		sum.setSentenceCountLimit(limit);
		System.out.println("SentenceCountLimit:"+sum.getSentenceCountLimit());
		
		textsum.getSummary(ti, sum);
		System.out.println("Summary:\n"+sum.getSummary());*/
		
		/*System.out.println("Sentences:"+sentences.size());
		for(Sentence sentence:sentences)
		{
			Vector<Word> words = sentence.getWords();
			for(Word word:words)
			{
				System.out.println("SonucWordsMorph:"+word.getMorphList());	
			}
		}*/
		
		/*System.out.println("WordsVector:"+wordsVector.size());
		for(Vector<Word> words:wordsVector)
		{
			System.out.println("----------------New sentence------------------");
			for(Word word:words)
			{
				System.out.println("SonucWordText:"+word.getWordText());	
			}
		}*/
		/*for(Vector<Word> words:wordsVector)
		{
			for(Word word:words)
			{
				System.out.println("SonucWordText:"+word.getWordText());	
				Integer val = wordGlobalHashMap.get(word.getWordText());
				System.out.println("GlobalWordInteger:"+val);
			}
		}*/
		
		/*for(int i=0; i<wordsVector.size();i++)
		{
			Vector<Word> words = wordsVector.elementAt(i);
			HashMap<String, Integer> wordLocalHash = wordLocalHashMap.get(i);
			for(Word word:words)
			{
				System.out.println("SonucWordText:"+word.getWordText());	
				Integer val = wordLocalHash.get(word.getWordText());
				System.out.println("LocalWordInteger:"+val);
			}
			System.out.println("--------------------");
		}*/
		
		/*System.out.println("SentenceScoreSize:"+sentenceScoreVector.size());
		for(Integer sentenceScore:sentenceScoreVector)
		{
			System.out.println("SentenceScore:"+sentenceScore);
		}*/
		
		/*for(int i=0;i<sentences.size();i++)
		{
			System.out.println("index:"+i);
			System.out.println("Sentence:"+sentences.elementAt(i));
			System.out.println("SentenceScore:"+sentenceScoreVector.elementAt(i));
		}*/
		
		
	}
}
