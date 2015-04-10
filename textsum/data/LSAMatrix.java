package textsum.data;

import java.util.HashMap;
import java.util.Vector;


public class LSAMatrix {
	public double [][]matrix;
	public int rowSize,columnSize;

	public double[] s;
	public double[][] U,S, V, VTranspose;


	public LSAMatrix(int m,int n)
	{
		//allocate memory
		matrix = new double[m][n];
		rowSize = m;
		columnSize = n;
	}
	public double[][] getMatrix() 
	{
		return matrix;
	}

	public void setMatrix(double[][] matrix) 
	{
		this.matrix = matrix;
	}

	public int getRowSize() {
		return rowSize;
	}
	public void setRowSize(int rowSize) {
		this.rowSize = rowSize;
	}
	public int getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}
	public void printU(Integer k){
		System.out.println("U:");
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<k;j++)
			{
				//System.out.format("%f%n", pi);                 //  -->  "3.141593"
				// System.out.format("%.3f%n", pi);               //  -->  "3.142"

				//System.out.print(U[i][j]+" ");
				System.out.format("%.3f ",U[i][j]);
			}
			System.out.println("");
		}
	}

	public void printS(Integer k){
		System.out.println("S:");
		for(int i=0;i<k;i++)
		{
			for(int j=0;j<k;j++)
			{
				System.out.format("%.3f ",S[i][j]);
				/*if(i==j)
				{
					//System.out.print(S[i][j]+" ");
					System.out.format("%.3f ",S[i][j]);
					break;
				}*/
			}
			System.out.println("");
		}
		/*
		for(int i=0;i<k1*k1;i++)
		{
			//System.out.print(S[i][j]+" ");
			System.out.format("%.3f ",s[i]);
			if((i%(k1)) == (k1-1))System.out.println("");
		}
		 */
	}

	public void printVT(Integer k){ 
		System.out.println("VTranspose:");

		for(int j=0;j<k;j++)
		{		
			for(int i=0;i<columnSize;i++)
			{
				//System.out.print(VTranspose[i][j]+" ");
				System.out.format("%.3f ",VTranspose[j][i]);
			}
			System.out.println("");
		}
	}

	public void setValsMyTfIdf(TextInfo ti)
	{
		/* 
		 * 1- Get tf-idf calculated matrix
		 * 2- Calculate avg val of tf-idf vals
		 * 3- If (tf-idf)< 0.5*avg(tf-idf) then val = 0;		
		 */
		// first set matrix(tf-idf)
		setValsTfIdf(ti);
		//calculate avg(tf-idf)
		double avg = 0;
		int count = 0;//number of vals > 0
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(matrix[i][j]>0) 
				{
					count ++;
					avg = avg + matrix[i][j];
				}
			}
		}
		avg = avg / count;
		//avg = avg / (rowSize*columnSize);
		//System.out.println("Avg: "+avg+" Count: "+count);
		//System.out.println("Avg: "+avg);
		
		// set vals
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(matrix[i][j]<0.5*avg) matrix[i][j]=0;
			}
		}
		
	}
	
	public void setValsWordGroups(TextInfo ti)
	{
		// Can be key phrase, or cluster of words
		//not implemented!!!!
	}
	
	public void setValsWordType(TextInfo ti)
	{
		// type = 0--> word type noun
		int type = 0;
		if(type == 0)
		{
			// find the words whose roots are noun
			Vector<Word> wordsNouns= new Vector<Word>();
			Vector<Word> differentWords = ti.getDifferentWords();
		
			for(Word diffWord:differentWords)
			{
				
				if(diffWord.getWordType().equals("ISIM"))			
				{
					//System.out.println("word.root: "+diffWord.getMorph().getRootType()+"word: "+diffWord.toString());
					wordsNouns.add(diffWord);
				}
			}
			
			Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();

			//create lsamatrix
			for(int i=0;i<wordsNouns.size();i++)
			{
				Word wd = wordsNouns.elementAt(i);
				//System.out.println("Word: "+wd.toString());
				for(int j=0;j<wordLocalHashMap.size();j++)
				{
					Integer number = wordLocalHashMap.elementAt(j).get(wd.getMorph().getRoot());
					if(number==null) number = 0;
					matrix[i][j]=number;
				}
			}

		}
		else ;
	}
	
	public void setValsRootType(TextInfo ti)
	{
		// type = 0--> root type noun
		int type = 0;
		if(type == 0)
		{
			// find the words whose roots are noun
			Vector<Word> wordsNouns= new Vector<Word>();
			Vector<Word> differentWords = ti.getDifferentWords();
		
			for(Word diffWord:differentWords)
			{
				//System.out.println("word: "+diffWord);
				//System.out.println("word-morph: "+diffWord.getMorph());
				if(diffWord.getMorph().getRootType().equals("ISIM"))			
				{
					//System.out.println("word.root: "+diffWord.getMorph().getRootType()+"word: "+diffWord.toString());
					wordsNouns.add(diffWord);
				}
			}
			
			Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();

			//create lsamatrix
			for(int i=0;i<wordsNouns.size();i++)
			{
				Word wd = wordsNouns.elementAt(i);
				//System.out.println("Word: "+wd.toString());
				for(int j=0;j<wordLocalHashMap.size();j++)
				{
					Integer number = wordLocalHashMap.elementAt(j).get(wd.getMorph().getRoot());
					if(number==null) number = 0;
					matrix[i][j]=number;
				}
			}
		}
		else ;
	}
	public void setValsLogEntropy(TextInfo ti)
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
		 *  local(i,j)= log2(1+f(i,j));//burası yok sanırım: *g(i)
		 *  
		 *  calculate val = global*local
		 *  then do normalization --> val/wordcount in sentence
		 */
		
		// first set matrix(number of occurence of the term)
		setValsOccurence(ti);
		
		//number of times that the term(i) appears (Total of f(i,j) for term i).
		double[] totalFreq = new double[rowSize];
		for(int i=0;i<rowSize;i++)
		{
			double count = 0;
			for(int j=0;j<columnSize;j++)
			{
				if(matrix[i][j]!=0) count=count+matrix[i][j];
			}
			totalFreq[i]=count;
		}
		//calculate p(i,j)
		double[][] totalProb = new double[rowSize][columnSize];
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(totalFreq[i]==0) System.out.println("OFF-HATA");
				
				double p=matrix[i][j]/totalFreq[i];
				totalProb[i][j] = p;
			}
		}
		
		// set summ vals-- -for all j: p(i,j)log2(p(i,j))
		double[]totalSumm = new double[rowSize];
		for(int i=0;i<rowSize;i++)
		{
			double s=0;
			for(int j=0;j<columnSize;j++)
			{
				double logVal = Math.log(totalProb[i][j])/Math.log(2);
				if(logVal==0) logVal=1;//System.out.println("OFF-HATA2 "+ i+ " "+j);
				s= s + totalProb[i][j]*(totalProb[i][j]/(logVal));
			}
			totalSumm[i]=s;
		}
		
		// calculate global: global(i) = 1 + ((summ)/log2(n))
		double[] global = new double[rowSize];
		for(int i=0;i<rowSize;i++)
		{
			double log2N = Math.log10(columnSize)/Math.log10(2);
			if(log2N==0) System.out.println("OFF-HATA3");
			double s = 1 + totalSumm[i]/log2N ;
			
			global[i]=s;
		}
		
		//calculate local: local(i)= log2(1+f(i,j)) // burası yok*g(i)
		double[][] local = new double[rowSize][columnSize];
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				double log2Val = Math.log10(1+matrix[i][j])/Math.log10(2);
				//double s = log2Val*global[i] ;
				local[i][j]=log2Val;
			}
		}
		
		//  calculate val = global*local
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				matrix[i][j] = global[i]*local[i][j];
			}
		}
		//do normalization
		// calculate number of words in a sentence
		double[] wordCount = new double[columnSize];
		
		for(int j=0;j<columnSize;j++)
		{
			wordCount[j] = 0;
			for(int i=0;i<rowSize;i++)
			{
				wordCount[j]= wordCount[j] + matrix[i][j];
			}
		}
		
		//then do normalization
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(wordCount[j]==0) System.out.println("OFF-HATA4");
				matrix[i][j] = matrix[i][j] / (wordCount[j]+1);
			}
		}

		/*
		//----------------------------------------------------//
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
		*/	
	}
	
	public void setValsTfIdf(TextInfo ti)
	{
		/*
		 *    1. Term Frequency: tf_{i,j} = (n_{i,j} ) / (sum of all k n_{k,j}) 
		 *     1.1. ni,j is the number of occurrences of the considered term (ti) in document dj, 
		 *     1.2. the denominator is the sum of number of occurrences of all terms in document dj.
		 *    2. Inverse Document Frequency: idf_{i} = log( (|D|) / (|{d: t(i) is element of d}|))
		 *     2.1. | D | : total number of documents in the corpus
		 *     2.2. |{d: t(i) is element of d}| : number of documents where the term ti appears (that is n_{i,j} \neq 0). 
		 *     		If the term is not in the corpus, this will lead to a division-by-zero. It is therefore common to use 1 + |{d: t(i) is element of d}|
		 *    3. tf-idf(i,j) = tf(i,j) x idf(i) 
		 */
		// first set matrix(number of occurence of the term)
		setValsOccurence(ti);
		
		//calculate the sum of number of occurrences of all terms in document dj
		double[] totalCount = new double[columnSize];
		for(int j=0;j<columnSize;j++)
		{
			double count=0;
			for(int i=0;i<rowSize;i++)
			{
				count=count + matrix[i][j];
			}
			totalCount[j]=count;
		}
		//calculate tf
		double[][] tf = new double[rowSize][columnSize];
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(totalCount[j]==0) System.out.println("HATA");
				tf[i][j] = matrix[i][j]/(totalCount[j]+1);
			}
		}
		
		//number of all documents
		double D = columnSize;
		//number of documents where the term ti appears (that is n_{i,j} != 0).
		double[] totalD = new double[rowSize];
		for(int i=0;i<rowSize;i++)
		{
			double count = 0;
			for(int j=0;j<columnSize;j++)
			{
				if(matrix[i][j]!=0) count++;
			}
			
			if(count==0)
			{
				//how can this occur?CONTROL!!
				System.out.println("ERROR2-CONTROL; index: "+ i);
				System.out.println("word is : "+ti.getDifferentWords().elementAt(i)+" ");
				for(int j=0;j<columnSize;j++)
				{
					System.out.print(matrix[i][j]+" ");
				}
			}
			
			totalD[i]=count;//+1 for error mentioned above in comments
		}
		//calculate idf
		double[] idf = new double[rowSize];
		for(int i=0;i<rowSize;i++)
		{
			if(totalD[i]==0) System.out.println("HATA2");
			idf[i]=Math.log(D/totalD[i]);
		}
		
		// calculate tf-idf : tf-idf(i,j) = tf(i,j) x idf(i) 
		//double[][] tfidf = new double[rowSize][columnSize];
		for(int i=0;i<rowSize;i++)
		{
			double count = 0;
			for(int j=0;j<columnSize;j++)
			{
				//tfidf[i][j] = tf[i][j]*idf[i];
				matrix[i][j] = tf[i][j]*idf[i];
			}
		}
		
		
		/*
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
		*/
	}
	
	public void setValsBinary(TextInfo ti)
	{
		setValsOccurence(ti);
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{
				if(matrix[i][j]>0) matrix[i][j]=1;
			}
		}
	}
	
	public void setValsOccurence(TextInfo ti)
	{
		//matrix = rows:words x columns:sentences
		Vector<Word> wordsAll = ti.getDifferentWords();//kelime kökleri var sadece
		Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();
		for(int i=0;i<wordsAll.size();i++)
		{
			Word wd = wordsAll.elementAt(i);
			//System.out.println("Word: "+wd.toString());
			for(int j=0;j<wordLocalHashMap.size();j++)
			{
				//Integer number = wordLocalHashMap.elementAt(j).get(wd.getMorph().getRoot());
				Integer number = wordLocalHashMap.elementAt(j).get(wd.getWordText());
				if(number==null) number = 0;
				matrix[i][j]=number;
			}

		}
	}
	
	
	public void setVals(TextInfo ti,int type )
	{
		//type=0--> number of occurence, type=1-->if occured or not(1/0)
		//type=2--> tf-idf, type=3-->log-entropy
		//type=4--> root type, type=5-->word type
		//type=6--> keywords/keyphrases
		switch(type)
		{
			case 0:setValsOccurence(ti);
			       //normalize
				   for(int i=0;i<ti.getNumberOfDifferentWords();i++)
				   {
					   for(int j=0;j<ti.getWordsVector().size();j++)
					   {
						 matrix[i][j]= matrix[i][j]/ti.getWordsVector().elementAt(j).size();  
					   }
				   }
				   break;
			case 1:setValsBinary(ti);break;
			case 2:setValsTfIdf(ti);break;
			case 3:setValsLogEntropy(ti);break;
			case 4:setValsRootType(ti);break;
			case 5:setValsWordType(ti);break;
			case 6:setValsWordGroups(ti);break;
			case 7:setValsMyTfIdf(ti);break;
			default: break;
		
		}
	}

	/*public void setVals(TextInfo ti)
	{
		Vector<Vector<Word>> wordsVector = ti.getWordsVector();
		HashMap<String, Integer> wordGlobalHashMap = ti.getWordGlobalHashMap();
		Vector<Word> wordsAll = ti.getDifferentWords();

		// create vector<Word> that has all words in the text-no repetation
		Vector<Word> wordsAll = new Vector<Word>();
		HashMap<String, Integer> wordRepetation = new HashMap<String, Integer>();
		for(Vector<Word> wdVector: wordsVector)
		{
			for(Word wd:wdVector)
			{

				if(wd.getMorph()!=null)
				{
					//System.out.println("Word Added: "+wd.toString());
					if(wordRepetation.get(wd.getMorph().getRoot())!=null)
					{
						;
					}
					else 
					{
						wordRepetation.put(wd.getMorph().getRoot(),1);
						wordsAll.addElement(wd);

					}
				}
			}
		}
		 

		//create lsamatrix
		Vector<HashMap<String, Integer>> wordLocalHashMap = ti.getWordLocalHashMap();

		for(int i=0;i<wordsAll.size();i++)
		{
			Word wd = wordsAll.elementAt(i);
			//System.out.println("Word: "+wd.toString());
			for(int j=0;j<wordLocalHashMap.size();j++)
			{
				Integer number = wordLocalHashMap.elementAt(j).get(wd.getMorph().getRoot());
				if(number==null) number = 0;
				matrix[i][j]=number;
			}

		}
	}//end of function
*/
	public void setVals(double[] vals,int rowSize,int columnSize)
	{
		for(int i=0;i<rowSize;i++)
		{
			for(int j=0;j<columnSize;j++)
			{

				matrix[i][j]=vals[j+i*columnSize];
			}
		}
	}//end of function

	public void setVals(double[][] vals,int rowSize,int columnSize)
	{
		matrix = vals;
//		for(int i=0;i<rowSize;i++)
//		{
//			for(int j=0;j<columnSize;j++)
//			{
//
//				matrix[i][j]=vals[i][j];
//			}
//		}
	}//end of function
	
}
