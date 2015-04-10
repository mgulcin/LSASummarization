package textsum;

import java.io.IOException;
import java.security.acl.Group;
import java.util.Vector;

import javax.naming.LimitExceededException;

import textsum.data.LSAMatrix;
import textsum.data.SVD;
import textsum.data.Sentence;
import textsum.data.TextInfo;

public class LSADecomposer {
	private SVD svdElement;
	private LSAMatrix lsaElement;

	public LSAMatrix getLsaElement() {
		return lsaElement;
	}

	public void setLsaElement(LSAMatrix matrix){
		lsaElement=matrix;
	}

	private boolean isElementOfSelectedSentences(Vector<Integer> selectedSentenceIndex, int indexMaxVal)
	{
		boolean selected = false;
		for(Integer sel: selectedSentenceIndex)
		{
			if(sel.intValue() == indexMaxVal) 
			{
				selected = true;
				break;
			}

		}
		return selected;

	}

	public void SVDDecomposition()
	{
		if(svdElement==null) svdElement=new SVD();
		svdElement.SVDDecompose(lsaElement.matrix);
		lsaElement.U = svdElement.getU().getArray();//mxn
		lsaElement.V = svdElement.getV().getArray();//nxn
		lsaElement.VTranspose = svdElement.getVT().getArray();//nxn
		lsaElement.S = svdElement.getS().getArray();
		lsaElement.s = svdElement.getS().getColumnPackedCopy();//nxn->n (diagonal)
	}

	public void SVDDecomposition(int rank)
	{		
		svdElement.SVDDecompose(lsaElement.matrix);
		lsaElement.U = svdElement.getU().getArray();//mxn
		lsaElement.V = svdElement.getV().getArray();//nxn
		lsaElement.VTranspose = svdElement.getVT().getArray();//nxn
		lsaElement.S = svdElement.getS().getArray();
		lsaElement.s = svdElement.getS().getColumnPackedCopy();//nxn->n (diagonal)

		//calculate APrime
		svdElement.calculateMainPrime(rank, lsaElement.rowSize, lsaElement.columnSize);
		//Print results
		int k= Math.min(lsaElement.rowSize,lsaElement.columnSize);
		lsaElement.printU(k);
		lsaElement.printS(k);
		lsaElement.printVT(k);
	}

	public boolean isElement(int j,Vector<Integer> vec)
	{
		boolean found=false;
		for(int i=0;i<vec.size();i++)
		{
			if(vec.elementAt(i).intValue()==j) 
			{
				found = true;
				break;
			}
		}
		return found;
	}
	public Vector<Integer> summarizerGongLui(int summarySentenceCount)
	{
		/*
		 * Summary'de kaç tane cümlenin yer alacağı input olarak verilir.
		 * 1) VT matrixinde her rowdaki en büyük değer bulunur
		 * 2) Bu değerin hangi columNo'ya(sentence) denk geldiği bulunur.
		 * 3) Bu cümle daha önceden zaten eklenmediyse, indexi vektöre eklenir. 
		 * 4) Cümle zaten eklendiyse, en büyük 2. değer bulunarak aynı işlem yapılır.--bunu ben ekledim!!
		 */
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);
		//System.out.println("k1:"+k1);

		int dummyMaxValIndex=-1;
		boolean chooseNextMax=false;
		for(int i=0;i<lsaElement.columnSize;i++)
		{
			if(summarySentenceCount>0)
			{
				double maxVal=-999;
				int indexMaxVal = -1;
				//for(int j=0;j<columnSize;j++)
				for(int j=0;j<k1;j++)
				{
					if( lsaElement.VTranspose[i][j] > maxVal ) 
					{
						if(chooseNextMax && j==dummyMaxValIndex)
						{
							// Step 4-Added by me
							//choose second max.., so do not use this value, go on
						}
						else
						{
							maxVal = lsaElement.VTranspose[i][j];
							indexMaxVal = j;
						}
					}
				}

				//a sentence selected
				if(indexMaxVal!=-1 && isElementOfSelectedSentences(selectedSentenceIndex,indexMaxVal) == false)
				{
					/*System.out.println("MaxVal :"+maxVal);
					System.out.println("MaxValIndex :"+indexMaxVal);*/

					selectedSentenceIndex.add(indexMaxVal);
					chooseNextMax=false;
					summarySentenceCount--;
				}
				else if(indexMaxVal!=-1)
				{
					/* a sentence is selected but is already in selected list
					 * ->then do step4-->Added by me
					 */
					chooseNextMax=true;
					dummyMaxValIndex = indexMaxVal;
				}
			}
		}
		return selectedSentenceIndex;
	}

	public Vector<Integer> summarizerJezek(int summarySentenceCount)
	{
		/*
		 *  1)val = v(j,i)*v(j*i) * eps(i) * eps(i) --> eps S vektöründeki değer
		 *  2)s(j) = sqrt(total(from 1-to n) val )
		 * 	3)sk = length of the vector of jth sentence in modified latent vector space (salience score of summarization)
		 *  +)n=number of dimensions in new space(independent on the number of sentences in the summary)
		 *  n: "In our experiments we chose the dimensions whose singular values didn’t fall under the half
		 *  of the highest singular value (but it is possible to set a different strategy)."
		 *  5)higher s valued sentence inserted to summary first
		 */
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();
		Vector<Double> sentenceScore = new Vector<Double>();
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		/* 
		 * decide n: singular values didn’t fall under the half of the highest singular value
		 */
		int n = 1; 
		double maxSVal = lsaElement.S[0][0];
		for(int i=1;i<lsaElement.columnSize;i++)
		{
			if(lsaElement.S[i][i]>=maxSVal*0.5) n++;
			else break;
		}

		//for(int j=0;j<k1;j++)//herbir cümle için yapılacak,k1 cümle sayısı olmalı
		for(int j=0;j<lsaElement.columnSize;j++)
		{
			double totalVal=0;
			double val =0;
			//for(int i=0;i<rowSize;i++)
			for(int i=0;i<n;i++)
			{
				//val = lsaElement.V[j][i]*lsaElement.V[j][i]*lsaElement.s[i*k1+i]*lsaElement.s[i*k1+i];
				val = lsaElement.V[j][i]*lsaElement.V[j][i]*lsaElement.S[i][i]*lsaElement.S[i][i];
				totalVal = totalVal+val;

				/*System.out.println("V[j][i] : "+V[j][i]);
				System.out.println("S[i][i] : "+S[i][i]);
				System.out.println("val : "+val);
				System.out.println("totalVal : "+totalVal);*/
			}
			sentenceScore.add(Math.sqrt(totalVal));
		}
		/*//print sentence score
		for(int i=0;i<sentenceScore.size();i++)
		{
			System.out.println("SentenceScore: "+i+" "+sentenceScore.elementAt(i));
		}*/


		for(int j=summarySentenceCount;j>0;j--)
		{
			// find the highest val in sentencescore vector, 
			// and add its index to selected SentenceIndexes
			double maxVal=-999;
			int index = -1;
			for(int i=0;i<sentenceScore.size();i++)
			{
				if(sentenceScore.elementAt(i)>maxVal)
				{
					maxVal=sentenceScore.elementAt(i);
					index = i;
				}
			}

			if(index !=-1)
			{
				selectedSentenceIndex.add(index);
				sentenceScore.removeElementAt(index);
			}
		}

		return selectedSentenceIndex;

		/* Bu kod yanlış Jezekin algoritmasını yanlış anladığım için yazıldı.
		 * Önemsememek en iyisi!!
		boolean JezekHata = false;// use Jezek alg. else Liu's alg.
		if(JezekHata)
		{
			//multiply S and VT,update VT
			Matrix nVT = new Matrix(VTranspose);
			int sL = (int) Math.sqrt(s.length);
			double[][] ns = new double[sL][sL];
			for(int i=0;i<sL;i++)
			{
				for(int j=0;j<sL;j++)
				{
					ns[i][j]=0;
				}
				ns[i][i]=s[i*sL];
			}

			Matrix nS = new Matrix(ns);
			nS.times(nVT);
			VTranspose = nS.getArray();
			/--*--//multiply S and VT,update VT**************************
			for(int i=0;i<columnSize;i++)
			{
				for(int j=0;j<k;j++)
				{
					VTranspose[i][j] = VTranspose[i][j]*s[i];
				}
			}*--/
		}
		 */
	}

	public Vector<Integer> summarizerMurray(int summarySentenceCount)
	{
		/*
		 * Following the Gong and Liu approach, 
		 * but rather than extracting the best sentence for each topic, 
		 * 
		 * the n best sentences are extracted, with n determined by 
		 * the corresponding singular values from matrix S . 
		 * The number of sentences in the summary that will come from 
		 * the first topic is determined by the percentage 
		 * that the largest singular value represents out of the sum of 
		 * all singular values, and so on for each topic. 
		 * 
		 * Thus, dimensionality reduction is no longer tied to summary length 
		 * and more than one sentence per topic can be chosen. 
		 * Using this method, the level of dimensionality reduction 
		 * is essentially learned from the data.
		 */


		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();

		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);
		//int[] sRatio=new int[k1];
		//int[] sRatio=new int[rank];--S in size ı kadar olmalı
		double[] sRatio=new double[lsaElement.columnSize];
		double sTotal = 0;

		//for(int i=0;i<k1;i++)
		for(int i=0;i<lsaElement.columnSize;i++)
		{
			sTotal =sTotal+ lsaElement.S[i][i];
		}

		//for(int i=0;i<k1;i++)
		for(int i=0;i<lsaElement.columnSize;i++)
		{
			sRatio[i]=(lsaElement.S[i][i]/sTotal);
			//System.out.println("i: "+i+" "+sRatio[i]);
		}

		Vector<Integer> usedIndexes = new Vector<Integer>();
		//for(int i=0;i<k1;i++)
		for(int i=0;i<lsaElement.columnSize;i++)
		{
			while(summarySentenceCount>0 && sRatio[i]>0)
			{
				//collect sRatio[i] sentences with highest values from each row 
				double maxVal=-999;
				int indexMaxVal = -1;
				for(int j=0;j<lsaElement.columnSize;j++)
				{
					if( lsaElement.VTranspose[i][j] > maxVal && isElement(j,usedIndexes)==false ) 
					{
						maxVal = lsaElement.VTranspose[i][j];
						indexMaxVal = j;
					}
				}

				//a sentence selected
				if(indexMaxVal!=-1 && isElementOfSelectedSentences(selectedSentenceIndex,indexMaxVal) == false)
				{
					selectedSentenceIndex.add(indexMaxVal);
					summarySentenceCount--;
					usedIndexes.add(indexMaxVal);
				}

				sRatio[i]--;
			}
		}


		return selectedSentenceIndex;
	}


	public Vector<Integer> summarizerCross(int summarySentenceCount)
	{
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();		
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		// concepts x selected sentences
		double VT[][]=new double[k1][lsaElement.columnSize];
		for(int i=0;i<k1;i++)
		{
			double avg=0;
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				avg = avg + lsaElement.VTranspose[i][j];
			}
			avg = avg / lsaElement.columnSize;
			//System.out.println("concept: "+i+" avg: "+avg);
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(lsaElement.VTranspose[i][j]<avg)
				{
					VT[i][j]=0;
				}
				else
				{
					VT[i][j]=lsaElement.VTranspose[i][j];
				}
			}
		}

		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(lsaElement.VTranspose[i][j]+" ");
			}
			System.out.println();
		}*/
		/*System.out.println("**********************");
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(VT[i][j]+" ");
			}
			System.out.println();
		}*/

		double sentenceScore[]=new double[lsaElement.columnSize]; 
		for(int j=0;j<lsaElement.columnSize;j++)
		{
			sentenceScore[j]=0;
			for(int i=0;i<k1;i++)
			{
				sentenceScore[j]=sentenceScore[j]+VT[i][j];
			}
		}

		/*System.out.print("Sentence Scores: ");
		for(int i=0;i<lsaElement.columnSize;i++)
		{
			System.out.print(i+": "+sentenceScore[i]+" , ");
		}
		System.out.println(" ");*/

		for(int j=summarySentenceCount;j>0;j--)
		{
			// find the highest val in sentencescore vector, 
			// and add its index to selected SentenceIndexes
			double maxVal=-999;
			int index = -1;
			for(int i=0;i<lsaElement.columnSize;i++)
			{
				if(sentenceScore[i]>maxVal)
				{
					maxVal=sentenceScore[i];
					index = i;
				}
			}

			if(index !=-1)
			{
				selectedSentenceIndex.add(index);
				sentenceScore[index]=-1000;//bundan emin değilim
			}
		}
		return selectedSentenceIndex;
	}

	public Vector<Integer> summarizerTopicalize(int summarySentenceCount)
	{
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();		
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		// concepts x selected sentences
		double VT[][]=new double[k1][lsaElement.columnSize];
		for(int i=0;i<k1;i++)
		{
			double avg=0;
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				avg = avg + lsaElement.VTranspose[i][j];
			}
			avg = avg / lsaElement.columnSize;
			//System.out.println("concept: "+i+" avg: "+avg);
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(lsaElement.VTranspose[i][j]<avg)
				{
					VT[i][j]=0;
				}
				else
				{
					VT[i][j]= 1;//lsaElement.VTranspose[i][j];
				}
			}
		}

		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(lsaElement.VTranspose[i][j]+" ");
			}
			System.out.println();
		}*/
		/*System.out.println("**********************");
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.format("%.3f ",VT[i][j]);
				//System.out.print(VT[i][j]+" ");
			}
			System.out.println();
		}*/

		//decide topics??
		class Limits
		{
			public int start;
			public int end;
		};
		Vector<Vector<Limits>> topics = new Vector<Vector<Limits>>();
		for(int i=0;i<k1;i++)
		{
			Vector<Limits> limitsVector = new  Vector<Limits>();
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(VT[i][j]==1)
				{
					if(limitsVector.size()==0)
					{
						Limits l= new Limits();
						l.start=j;
						l.end=j;
						limitsVector.add(l);
					}
					else if(VT[i][j-1]==0)
					{
						Limits l= new Limits();
						l.start=j;
						l.end=j;
						limitsVector.add(l);
					}
					else if(VT[i][j-1]==1)
					{
						Limits l2 = limitsVector.lastElement();
						l2.end=j;
						int index = limitsVector.size()-1;
						limitsVector.setElementAt(l2, index);
					}
					else System.out.println("Error in Topicalize? ");
				}
			}
			topics.add(limitsVector);
		}
		//		
		//		for(int i=0;i<topics.size();i++)
		//		{
		//			Vector<Limits> limits = topics.elementAt(i);
		//			System.out.println("Concept: "+i);
		//			for(int j=0;j<limits.size();j++)
		//			{
		//				Limits lim = limits.elementAt(j);
		//				System.out.println("start: "+lim.start+" end: "+lim.end);
		//			}
		//			System.out.println();
		//		}
		//		
		/*en az 3 tane ardarda gelen(gruplanmışlar) kalsın, diğerlerini sil*/
		int groupCount=3;
		for(int i=0;i<topics.size();i++)
		{
			Vector<Limits> limits = topics.elementAt(i);
			for(int j=0;j<limits.size();j++)
			{
				Limits lim = limits.elementAt(j);
				if((lim.end-lim.start)+1 < groupCount)
				{
					limits.removeElementAt(j);
					j--;
				}

			}
		}
		/*
		Print concepts with at least groupCount elements
		for(int i=0;i<topics.size();i++)
		{
			Vector<Limits> limits = topics.elementAt(i);
			System.out.println("Concept: "+i);
			for(int j=0;j<limits.size();j++)
			{
				Limits lim = limits.elementAt(j);
				System.out.println("start: "+lim.start+" end: "+lim.end);
			}
			System.out.println();
		}
		 */
		/*Collect most significant sentence(who has the highest score) of each group */
		class Groups
		{
			public int sentenceIndex;
			public double value;
		};

		Vector<Vector<Groups>> groups= new Vector<Vector<Groups>>();
		for(int i=0;i<topics.size();i++)
		{
			Vector<Groups> gr = new Vector<Groups>();
			Vector<Limits> limits = topics.elementAt(i);
			for(int j=0;j<limits.size();j++)
			{
				Limits lim = limits.elementAt(j);
				double highestVal=-999;
				int highestValIndex = -1;

				for(int k=lim.start;k<=lim.end;k++)
				{
					if(lsaElement.VTranspose[i][k]>highestVal)
					{
						highestVal = lsaElement.VTranspose[i][k];
						highestValIndex = k;
					}
				}
				Groups g= new Groups();
				g.sentenceIndex = highestValIndex;
				g.value=highestVal;

				gr.add(g);
			}
			groups.add(gr);
		}
		//		/*Print results of groups*/
		//		for(int i=0;i<groups.size();i++)
		//		{
		//			Vector<Groups> gr = groups.elementAt(i);
		//			System.out.println("Group: "+i);
		//			for(int j=0;j<gr.size();j++)
		//			{
		//				Groups g = gr.elementAt(j);
		//				System.out.println("SentenceIndex: "+g.sentenceIndex+" SentenceVal: "+g.value);
		//			}
		//		}
		//		
		//System.out.println("groups.size "+ groups.size() + " , summCount: "+summarySentenceCount);
		/*Collect most significant sentences*/
		for(int i=0;i<summarySentenceCount;i++)
		{
			Vector<Groups> gr = groups.elementAt(i);
			int mostImportantSentenceofGroup = -1;
			double mostImportantValofGroup = -999;
			for(int j=0;j<gr.size();j++)
			{
				Groups g = gr.elementAt(j);
				if(g.value>=mostImportantValofGroup)
				{
					mostImportantSentenceofGroup = g.sentenceIndex;
					mostImportantValofGroup = g.value;
				}
			}
			selectedSentenceIndex.add(mostImportantSentenceofGroup);
		}
		return selectedSentenceIndex;
	}

	public Vector<Integer> summarizerTopicalize2(int summarySentenceCount)
	{
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();		
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		// concepts x selected sentences
		double VT[][]=new double[k1][lsaElement.columnSize];

		// find average of ach row
		// if a value in a cell < avg, set that cell to -999(meaning removed)
		// aim is to remove outlier/noise
		for(int i=0;i<k1;i++)
		{
			double avg=0;
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				avg = avg + lsaElement.VTranspose[i][j];
			}
			avg = avg / lsaElement.columnSize;
			//System.out.println("concept: "+i+" avg: "+avg);
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(lsaElement.VTranspose[i][j]<avg)
				{
					VT[i][j]=-999;
				}
				else
				{
					//VT[i][j]= 1;
					VT[i][j]= lsaElement.VTranspose[i][j];
				}
			}
		}


		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(lsaElement.VTranspose[i][j]+" ");
			}
			System.out.println();
		}*/
		//System.out.println("**********************");
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.format("%.3f ",VT[i][j]);
				//System.out.print(VT[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("**********************");*/

		//decide topics??
		// create concept x concept matrix, 
		// and find how many matches are there among concepts
		double concepts[][]=new double[k1][k1];
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				concepts[i][j]=0;
			}
		}
		// walk on the concepts x sentences matrix
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(VT[i][j]!=-999)
				{
					// control all concepts' jth cell
					for(int l=0;l<k1;l++)
					{
						if(VT[l][j]!=-999)
						{
							concepts[i][l] = concepts[i][l]+1;
						}
					}
				}
			}
		}

		//print resulting concepts matrix
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				System.out.print(concepts[i][j]+" ");
			}
			System.out.println();
		}*/

		//calculate total score of each concept
		double conceptScores[]=new double[k1];
		for(int i=0;i<k1;i++)
		{
			conceptScores[i] = 0;
			for(int j=0;j<k1;j++)
			{
				conceptScores[i]=conceptScores[i]+concepts[i][j];
			}
		}

		/*for(int j=0;j<k1;j++)
		{
			System.out.print(conceptScores[j]+" ");
		}
		System.out.println();*/

		// select max conceptScores, 
		// and get the largest valued sentences of the selected concepts
		int count = summarySentenceCount;
		int maxConceptValIndex[]=new int[count];
		double maxConceptVal[]=new double[count];
		for(int i=0;i<count;i++)
		{
			maxConceptValIndex[i]=-1;
			maxConceptVal[i] = -1;
		}

		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<count;j++)
			{
				if(conceptScores[i]>maxConceptVal[j])
				{
					maxConceptVal[j] = conceptScores[i];
					maxConceptValIndex[j] = i;
					break;
				}
			}
		}
		/*for(int j=0;j<count;j++)
		{
			System.out.println("maxConceptVal: "+maxConceptVal[j]+" maxConceptValIndex: "+maxValIndex[j]);
		}
		 */

		// collect the max valued sentences of the concepts with maxValIndex
		for(int i=0;i<count;i++)
		{
			int index = maxConceptValIndex[i];
			//select the sentence with highest val, in the concept with the index val
			boolean findNextMax=true;
			while(findNextMax)
			{
				double maxVal = -1000;
				int maxValIndex = -1;
				for(int j=0;j<lsaElement.columnSize;j++)
				{
					if(lsaElement.VTranspose[i][j]>=maxVal)
					{
						maxVal = lsaElement.VTranspose[i][j];
						maxValIndex = j;
					}
				}

				//control if sentence is already selected, if not insert to resulting index vector
				if(!isElement(maxValIndex, selectedSentenceIndex))
				{
					//System.out.println("selectedIndex: "+maxValIndex+" ");
					findNextMax=false;
					selectedSentenceIndex.add(maxValIndex);
					summarySentenceCount--;
				}
				else
				{
					//need to find second max valued sentence of concept at index
					//set VT[i][maxValIndex] =-1001,so that, 
					//if second/third max is needed, it will not be found again
					lsaElement.VTranspose[i][maxValIndex]=-1001;
					findNextMax=true;
				}
			}
		}

		return selectedSentenceIndex;
	}


	public Vector<Integer> summarizerTopicalize3(int summarySentenceCount)
	{
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();		
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		// concepts x selected sentences
		double VT[][]=new double[k1][lsaElement.columnSize];

		// find average of ach row
		// if a value in a cell < avg, set that cell to -999(meaning removed)
		// aim is to remove outlier/noise
		for(int i=0;i<k1;i++)
		{
			double avg=0;
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				avg = avg + lsaElement.VTranspose[i][j];
			}
			avg = avg / lsaElement.columnSize;
			//System.out.println("concept: "+i+" avg: "+avg);
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(lsaElement.VTranspose[i][j]<avg)
				{
					VT[i][j]=-999;
				}
				else
				{
					//VT[i][j]= 1;
					VT[i][j]= lsaElement.VTranspose[i][j];
				}
			}
		}


		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(lsaElement.VTranspose[i][j]+" ");
			}
			System.out.println();
		}*/
		//System.out.println("**********************");
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.format("%.3f ",VT[i][j]);
				//System.out.print(VT[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("**********************");*/

		//decide topics??
		// create concept x concept matrix, 
		// and find how many matches are there among concepts
		double concepts[][]=new double[k1][k1];
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				concepts[i][j]=0;
			}
		}
		// walk on the concepts x sentences matrix
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(VT[i][j]!=-999)
				{
					// control all concepts' jth cell
					for(int l=0;l<k1;l++)
					{
						if(VT[l][j]!=-999)
						{
							concepts[i][l] = concepts[i][l]+VT[l][j];
						}
					}
				}
			}
		}

		//print resulting concepts matrix
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				System.out.print(concepts[i][j]+" ");
			}
			System.out.println();
		}*/

		//calculate total score of each concept
		double conceptScores[]=new double[k1];
		for(int i=0;i<k1;i++)
		{
			conceptScores[i] = 0;
			for(int j=0;j<k1;j++)
			{
				conceptScores[i]=conceptScores[i]+concepts[i][j];
			}
		}
		/*
		for(int j=0;j<k1;j++)
		{
			System.out.print(conceptScores[j]+" ");
		}
		System.out.println();*/

		// select max conceptScores, 
		// and get the largest valued sentences of the selected concepts
		int count = summarySentenceCount;
		int maxConceptValIndex[]=new int[count];
		double maxConceptVal[]=new double[count];
		for(int i=0;i<count;i++)
		{
			maxConceptValIndex[i]=-1;
			maxConceptVal[i] = -1;
		}

		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<count;j++)
			{
				if(conceptScores[i]>maxConceptVal[j])
				{
					maxConceptVal[j] = conceptScores[i];
					maxConceptValIndex[j] = i;
					break;
				}
			}
		}
		/*for(int j=0;j<count;j++)
		{
			System.out.println("maxConceptVal: "+maxConceptVal[j]+" maxConceptValIndex: "+maxValIndex[j]);
		}
		 */

		// collect the max valued sentences of the concepts with maxValIndex
		for(int i=0;i<count;i++)
		{
			int index = maxConceptValIndex[i];
			//select the sentence with highest val, in the concept with the index val
			boolean findNextMax=true;
			while(findNextMax)
			{
				double maxVal = -1000;
				int maxValIndex = -1;
				for(int j=0;j<lsaElement.columnSize;j++)
				{
					if(lsaElement.VTranspose[i][j]>=maxVal)
					{
						maxVal = lsaElement.VTranspose[i][j];
						maxValIndex = j;
					}
				}

				//control if sentence is already selected, if not insert to resulting index vector
				if(!isElement(maxValIndex, selectedSentenceIndex))
				{
					//System.out.println("selectedIndex: "+maxValIndex+" ");
					findNextMax=false;
					selectedSentenceIndex.add(maxValIndex);
					summarySentenceCount--;
				}
				else
				{
					//need to find second max valued sentence of concept at index
					//set VT[i][maxValIndex] =-1001,so that, 
					//if second/third max is needed, it will not be found again
					lsaElement.VTranspose[i][maxValIndex]=-1001;
					findNextMax=true;
				}
			}
		}

		return selectedSentenceIndex;
	}


	public Vector<Integer> summarizerTopicalizeTextRank(int summarySentenceCount)
	{
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();		
		int k1= Math.min(lsaElement.rowSize,lsaElement.columnSize);

		// concepts x selected sentences
		double VT[][]=new double[k1][lsaElement.columnSize];

		// find average of ach row
		// if a value in a cell < avg, set that cell to -999(meaning removed)
		// aim is to remove outlier/noise
		for(int i=0;i<k1;i++)
		{
			double avg=0;
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				avg = avg + lsaElement.VTranspose[i][j];
			}
			avg = avg / lsaElement.columnSize;
			//System.out.println("concept: "+i+" avg: "+avg);
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(lsaElement.VTranspose[i][j]<avg)
				{
					VT[i][j]=-999;
				}
				else
				{
					//VT[i][j]= 1;
					VT[i][j]= lsaElement.VTranspose[i][j];
				}
			}
		}


		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.print(lsaElement.VTranspose[i][j]+" ");
			}
			System.out.println();
		}*/
		//System.out.println("**********************");
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				System.out.format("%.3f ",VT[i][j]);
				//System.out.print(VT[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("**********************");*/

		//decide topics??
		// create concept x concept matrix, 
		// and find how many matches are there among concepts
		double concepts[][]=new double[k1][k1];
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				concepts[i][j]=0;
			}
		}
		// walk on the concepts x sentences matrix
		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<lsaElement.columnSize;j++)
			{
				if(VT[i][j]!=-999)
				{
					// control all concepts' jth cell
					for(int l=0;l<k1;l++)
					{
						if(VT[l][j]!=-999)
						{
							concepts[i][l] = concepts[i][l]+VT[l][j];
						}
					}
				}
			}
		}

		//print resulting concepts matrix
		/*for(int i=0;i<k1;i++)
		{
			for(int j=0;j<k1;j++)
			{
				System.out.print(concepts[i][j]+" ");
			}
			System.out.println();
		}*/
		////////////////////

		//first set diagonal to 1
		for(int i=0;i<k1;i++)
		{
			concepts[i][i] = 1;
		}

		//run text rank algorithm
		//calculateTextRank(concepts,k1);
		calculateTextRank2(concepts,k1);

		/*	System.out.println("Result:");
		for(int i=0;i<k1;i++)
		{
			System.out.print(concepts[i][i]+" ");
		}
		System.out.println();*/


		//calculate total score of each concept
		double conceptScores[]=new double[k1];
		for(int i=0;i<k1;i++)
		{
			conceptScores[i] = concepts[i][i];
		}

		System.out.println("ConceptScore:");
		for(int j=0;j<k1;j++)
		{
			System.out.print(conceptScores[j]+" ");
		}
		System.out.println();

		// select max conceptScores, 
		// and get the largest valued sentences of the selected concepts
		int count = summarySentenceCount;
		int maxConceptValIndex[]=new int[count];
		double maxConceptVal[]=new double[count];
		for(int i=0;i<count;i++)
		{
			maxConceptValIndex[i]=-1;
			maxConceptVal[i] = -1;
		}

		for(int i=0;i<k1;i++)
		{
			for(int j=0;j<count;j++)
			{
				if(conceptScores[i]>maxConceptVal[j])
				{
					maxConceptVal[j] = conceptScores[i];
					maxConceptValIndex[j] = i;
					break;
				}
			}
		}
		/*for(int j=0;j<count;j++)
		{
			System.out.println("maxConceptVal: "+maxConceptVal[j]+" maxConceptValIndex: "+maxValIndex[j]);
		}
		 */

		// collect the max valued sentences of the concepts with maxValIndex
		for(int i=0;i<count;i++)
		{
			int index = maxConceptValIndex[i];
			//select the sentence with highest val, in the concept with the index val
			boolean findNextMax=true;
			while(findNextMax)
			{
				double maxVal = -1000;
				int maxValIndex = -1;
				for(int j=0;j<lsaElement.columnSize;j++)
				{
					if(lsaElement.VTranspose[i][j]>=maxVal)
					{
						maxVal = lsaElement.VTranspose[i][j];
						maxValIndex = j;
					}
				}

				//control if sentence is already selected, if not insert to resulting index vector
				if(!isElement(maxValIndex, selectedSentenceIndex))
				{
					//System.out.println("selectedIndex: "+maxValIndex+" ");
					findNextMax=false;
					selectedSentenceIndex.add(maxValIndex);
					summarySentenceCount--;
				}
				else
				{
					//need to find second max valued sentence of concept at index
					//set VT[i][maxValIndex] =-1001,so that, 
					//if second/third max is needed, it will not be found again
					lsaElement.VTranspose[i][maxValIndex]=-1001;
					findNextMax=true;
				}
			}
		}
		////////////////////

		return selectedSentenceIndex;
	}
	public void calculateTextRank(double[][] concepts,int k1) 
	{
		// then run text rank formula for each vertex
		/*
		 * WS(Vi)= (1-d)+ d* (For all Vj which are In-edges of Vi)Total(wji/(For each vk out-edges of Vj Total of wjk))* WS(Vj)
		 *
		 * if (WS(Vi) at time t+1- WS(Vi) at time t) <= convergenceThresh--> 
		 */

		double convergenceThresh = 0.0001;
		double d = 0.85;

		//For each concept
		for(int index = 0; index < k1; index++)
		{
			double oldVal = concepts[index][index];
			double newVal = 0;

			//			System.out.println("index: "+index+" , OldVal: "+oldVal);

			boolean cont= true;
			int iterCount = 0;
			while(cont)
			{
				//find inEdges
				Vector<Integer> inEdgesIndex = new Vector<Integer>();
				for(int i=0;i<k1;i++)
				{
					if(i!=index && concepts[index][i]> 0)
					{
						inEdgesIndex.add(i);
					}
				}

				/*		System.out.println("inEdge index:");
				for (Integer inedges : inEdgesIndex) 
				{
					System.out.print(inedges+" ");
				}
				System.out.println();*/

				// find out edges of each inedges-and total weights
				// inedges: outedges total weight 
				Vector<Double> outEdgesTotalWeight = new Vector<Double>();
				for(int i=0;i<inEdgesIndex.size();i++)
				{
					double val = 0;
					int ind = inEdgesIndex.elementAt(i);
					for(int j=0;j<k1;j++)
					{
						if(j!=ind && concepts[ind][j]> 0)
						{
							val = val + concepts[ind][j];
						}
					}
					outEdgesTotalWeight.add(val);

				}

				//				System.out.println("outEdges Weight:");
				//				for (Double dW:outEdgesTotalWeight) 
				//				{
				//					System.out.print(dW+" ");
				//				}
				//				System.out.println();
				//				
				//				
				//find total in-edges score/totals of these outedges 
				Vector<Double> inEdgesScore = new Vector<Double>();
				for(int i=0;i<inEdgesIndex.size();i++)
				{
					int ind = inEdgesIndex.elementAt(i);
					double val = (concepts[index][ind]) / (outEdgesTotalWeight.elementAt(i));
					val = val * concepts[ind][ind];

					inEdgesScore.add(val);
				}

				//				System.out.println("inEdges Score:");
				//				for (Double dW:inEdgesScore) 
				//				{
				//					System.out.print(dW+" ");
				//				}
				//				System.out.println();
				//				
				// find total of in-edges
				double total = 0;
				for(int i=0;i<inEdgesScore.size();i++)
				{
					total = total + inEdgesScore.elementAt(i);
				}

				//				System.out.println("Total: "+total);

				// find new val
				newVal = (1-d) + d * total; 
				//				System.out.println("NewVal: "+newVal);

				if(newVal-oldVal<=convergenceThresh)
				{
					concepts[index][index] = newVal;
					//					System.out.println("Concept Index: "+ index + " , iterCount: "+iterCount);
					cont = false;
				}
				else
				{
					oldVal = newVal;
					iterCount++;
					cont = true;
				}
			}

		}

	}

	public void calculateTextRank2(double[][] concepts,int k1) 
	{
		// then run text rank formula for each vertex
		/*
		 * WS(Vi)= (1-d)+ d* (For all Vj which are In-edges of Vi)Total(wji/(For each vk out-edges of Vj Total of wjk))* WS(Vj)
		 *
		 * if (WS(Vi) at time t+1- WS(Vi) at time t) <= convergenceThresh--> 
		 */

		//double convergenceThresh = 0.0001;
		double convergenceThresh = 0.03;
		double d = 0.85;

		//For each concept
		//set old and new vals
		double[] oldVals = new double[k1];
		double[] newVals = new double[k1];

		for(int i=0;i<k1;i++)
		{
			oldVals[i] = concepts[i][i];
			newVals[i] = 0;
		}
		boolean cont = true;
		while(cont)
		{
			// calculate ws for each concept
			for(int index = 0; index < k1; index++)
			{
				//find inEdges
				Vector<Integer> inEdgesIndex = new Vector<Integer>();
				for(int i=0;i<k1;i++)
				{
					if(i!=index && concepts[index][i]> 0)
					{
						inEdgesIndex.add(i);
					}
				}

				/*		System.out.println("inEdge index:");
				for (Integer inedges : inEdgesIndex) 
				{
					System.out.print(inedges+" ");
				}
				System.out.println();*/

				// find out edges of each inedges-and total weights
				// inedges: outedges total weight 
				Vector<Double> outEdgesTotalWeight = new Vector<Double>();
				for(int i=0;i<inEdgesIndex.size();i++)
				{
					double val = 0;
					int ind = inEdgesIndex.elementAt(i);
					for(int j=0;j<k1;j++)
					{
						if(j!=ind && concepts[ind][j]> 0)
						{
							val = val + concepts[ind][j];
						}
					}
					outEdgesTotalWeight.add(val);

				}
				/*
				System.out.println("outEdges Weight:");
				for (Double dW:outEdgesTotalWeight) 
				{
					System.out.print(dW+" ");
				}
				System.out.println();*/


				//find total in-edges score/totals of these outedges 
				Vector<Double> inEdgesScore = new Vector<Double>();
				for(int i=0;i<inEdgesIndex.size();i++)
				{
					int ind = inEdgesIndex.elementAt(i);
					double val = (concepts[index][ind]) / (outEdgesTotalWeight.elementAt(i));
					val = val * concepts[ind][ind];

					inEdgesScore.add(val);
				}

				/*				System.out.println("inEdges Score:");
				for (Double dW:inEdgesScore) 
				{
					System.out.print(dW+" ");
				}
				System.out.println();
				 */
				// find total of in-edges
				double total = 0;
				for(int i=0;i<inEdgesScore.size();i++)
				{
					total = total + inEdgesScore.elementAt(i);
				}

				//System.out.println("Total: "+total);

				// find new val
				newVals[index] = (1-d) + d * total; 

				System.out.println("index: "+index);
				System.out.println("NewVal: "+newVals[index]);
				System.out.println("OldVal: "+oldVals[index]);
				System.out.println("new-old: "+(newVals[index]-oldVals[index]));
			}


			boolean c = false;
			for(int i=0;i<k1;i++)
			{
				if((newVals[i]-oldVals[i]) > convergenceThresh)
				{
					oldVals[i]=newVals[i];
					concepts[i][i]=newVals[i];
					c = true;
					System.out.println("*******************************index:"+i);
					
				/*	try {
						System.in.read();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/

				}
				else
				{
					oldVals[i]=newVals[i];
					concepts[i][i]=newVals[i];
				}
			}

			if(c==true) cont = true;
			else cont=false;
		}

	}


	public Vector<Integer> getSelectedIndex(int type,int summarySentenceCount)
	{
		/*
		 * type=0--> Gong&Liu
		 * type=1--> Jezek
		 * type=2--> Murray
		 * type=3--> Cross
		 * type=4--> Topicalize
		 * type=5--> Topicalize2
		 * type=6--> Topicalize3
		 */
		switch(type)
		{
		case 0: return summarizerGongLui(summarySentenceCount);
		case 1: return summarizerJezek(summarySentenceCount);
		case 2: return summarizerMurray(summarySentenceCount);
		case 3: return summarizerCross(summarySentenceCount);
		case 4: return summarizerTopicalize(summarySentenceCount);
		case 5: return summarizerTopicalize2(summarySentenceCount);
		case 6: return summarizerTopicalize3(summarySentenceCount);
		case 7: return summarizerTopicalizeTextRank(summarySentenceCount);
		default: return null;
		}
	}

	public Vector<Sentence> getSummary(LSAMatrix lsaMatrix,TextInfo ti,int rank,int summarySentenceCount,int type)
	{
		//not used rank and summarySentenceScore!!!!
		//System.out.println("getSummary");
		Vector<Sentence> summary = new Vector<Sentence>();
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();

		//set matrix values and do SVD
		lsaElement = lsaMatrix;
		SVDDecomposition();	//SVDDecomposition(rank);
		//System.out.println("SVD Decomposition done");
		//get the indexes of selectedSentences; // type=0--> Gong&Liu, type=1--> Jezek, type=2--> Murray ...
		selectedSentenceIndex = getSelectedIndex(type,summarySentenceCount);

		//get the sentences
		for(Integer index:selectedSentenceIndex)
		{
			//System.out.println("Selected sentence index: "+index);
			if(index>=0)
			{
				Sentence s = ti.getSentenceVector().elementAt(index);
				summary.add(s);
			}
		}

		return summary;
	}

	public Vector<Sentence> getSummary(TextInfo ti,int rank,int summarySentenceCount,int type)
	{
		// type=0--> Gong&Liu, type=1--> Jezek, type=2--> Murray
		//not used rank and summarySentenceScore!!!!
		//System.out.println("getSummary");
		Vector<Sentence> summary = new Vector<Sentence>();
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();

		//set matrix values and do SVD
		lsaElement.setVals(ti,0);//number of occurence(terms)
		SVDDecomposition();	//SVDDecomposition(rank);
		//get the indexes of selectedSentences;
		selectedSentenceIndex = getSelectedIndex(type,summarySentenceCount);

		//get the sentences
		for(Integer index:selectedSentenceIndex)
		{
			Sentence s = ti.getSentenceVector().elementAt(index);
			summary.add(s);
		}
		/*
		///select  sentence
		Vector<Integer> selectedSentenceIndex = new Vector<Integer>();
		int k =summarySentenceCount;//(int) (ti.getSentenceVector().size()*0.25+0.5);


		//use Jezek
		Vector<Double> sentenceScore = new Vector<Double>();
		boolean Jezek = false;
		if(Jezek)
		{
			/--*
		 *  val = v(j,i)*v(j*i) * eps(i) * eps(i) --> eps S vekt�r�ndeki de�er
		 *  s(j) = sqrt(total(from 1-to n) val )
		 * 	sk = length of the vector of jth sentence in modified latent vector space (salience score of summarization)
		 *  n=number of dimensions in new space(independent on the number of sentences in the summary)
		 *  higher s valued sentence inserted to summary first
		 *--/
			int k1= Math.min(rowSize,columnSize);
			for(int j=0;j<k1;j++)
			{
				double totalVal=0;
				double val =0;
				//for(int i=0;i<rowSize;i++)
				for(int i=0;i<rank;i++)
				{
					val = V[j][i]*V[j][i]*s[i*k1+i]*s[i*k1+i];
					totalVal = totalVal+val;

					/--*System.out.println("V[j][i] : "+V[j][i]);
					System.out.println("s[i*k1+i] : "+s[i*k1+i]);
					System.out.println("val : "+val);
					System.out.println("totalVal : "+totalVal);*--/
				}
				sentenceScore.add(Math.sqrt(totalVal));
			}
			//print sentence score
			for(int i=0;i<sentenceScore.size();i++)
			{
				System.out.println("SentenceScore: "+sentenceScore.elementAt(i));
			}

		}


		boolean Murray =false;
		if(Jezek)
		{
			int k1= Math.min(rowSize,columnSize);
			while(k>0)
			{
				double maxVal=-999;
				int indexMaxVal = -1;
				for(int i=0;i<k1;i++)
				{
					if( sentenceScore.elementAt(i) > maxVal ) 
					{
						maxVal = sentenceScore.elementAt(i);
						indexMaxVal = i;
					}
				}

				//a sentence selected
				if(indexMaxVal!=-1 && isElementSelectedSentences(selectedSentenceIndex,indexMaxVal) == false)
				{
					System.out.println("MaxVal :"+maxVal);
					System.out.println("MaxValIndex :"+indexMaxVal);

					selectedSentenceIndex.add(indexMaxVal);
					sentenceScore.set(indexMaxVal, 0.0);

					Sentence s = ti.getSentenceVector().elementAt(indexMaxVal);
					summary.add(s);
					k--;
				}
			}
		}
		else if(Murray)
		{
			/--*
		 * n best sentences are extracted, 
		 * with n determined by the corresponding singular values from matrix S . 
		 * The number of sentences in the summary that will come from the first topic 
		 * is determined by the percentage that the largest singular value represents 
		 * out of the sum of all singular values, and so on for each topic. 
		 *--/
			int k1= Math.min(rowSize,columnSize);
			//int[] sRatio=new int[k1];
			int[] sRatio=new int[rank];
			double sTotal = 0;

			//for(int i=0;i<k1;i++)
			for(int i=0;i<rank;i++)
			{
				sTotal =sTotal+ s[i*k1+i];
			}

			//for(int i=0;i<k1;i++)
			for(int i=0;i<rank;i++)
			{
				sRatio[i]=(int)((s[i*k1+i]/sTotal)+0.5);
			}

			/--*for(int i=0;i<k1;i++)
			{
				System.out.println("(s[i*k1+i]/sTotal): "+(s[i*k1+i]/sTotal));
				System.out.println("sRatio[i]: "+sRatio[i]);
			}*--/

			///orjinalinde yok ben ekledim:)
			//Oranlar� hep 0 oluyorsa 1. i�in 1 olsun
			boolean allZero = true;
			//for(int i=0;i<k1;i++)
			for(int i=0;i<rank;i++)
			{
				if(sRatio[i]>0) {
					allZero=false;
					break;
				}
			}
			if(allZero==true)sRatio[0]=1;
			///

			//for(int i=0;i<k1;i++)
			for(int i=0;i<rank;i++)
			{
				while(sRatio[i]>0)
				{
					//collect sRatio[i] sentences with highest values from each row 
					double maxVal=-999;
					int indexMaxVal = -1;
					for(int j=0;j<columnSize;j++)
					{
						if( VTranspose[i][j] > maxVal ) 
						{
							maxVal = VTranspose[i][j];
							indexMaxVal = j;
						}
					}


					//a sentence selected
					if(indexMaxVal!=-1 && isElementSelectedSentences(selectedSentenceIndex,indexMaxVal) == false)
					{
						System.out.println("MaxVal :"+maxVal);
						System.out.println("MaxValIndex :"+indexMaxVal);

						selectedSentenceIndex.add(indexMaxVal);

						Sentence s = ti.getSentenceVector().elementAt(indexMaxVal);
						summary.add(s);
					}

					sRatio[i]--;
				}
			}

		}
		else
		{
			int k1= Math.min(rowSize,columnSize);
			System.out.println("k1:"+k1);
			for(int i=0;i<columnSize;i++)
			{
				if(k>0)
				{
					double maxVal=-999;
					int indexMaxVal = -1;
					for(int j=0;j<k1;j++)
					{
						if( VTranspose[i][j] > maxVal ) 
						{
							maxVal = VTranspose[i][j];
							indexMaxVal = j;
						}
					}


					//a sentence selected
					if(indexMaxVal!=-1 && isElementSelectedSentences(selectedSentenceIndex,indexMaxVal) == false)
					{
						System.out.println("MaxVal :"+maxVal);
						System.out.println("MaxValIndex :"+indexMaxVal);

						selectedSentenceIndex.add(indexMaxVal);

						Sentence s = ti.getSentenceVector().elementAt(indexMaxVal);
						summary.add(s);
						k--;
					}
				}
			}
		}
		 */
		return summary;
	}//end of getsummary


}
