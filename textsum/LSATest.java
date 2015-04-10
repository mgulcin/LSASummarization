package textsum;

import textsum.data.LSAMatrix;

public class LSATest {

	public static void main( String[] args )
	{
		LSAMatrix lsaMatrix = new LSAMatrix(6,7);
		double[] matrixValArray={1,1,0,1,0,1,0,
								 0,0,0,0,1,1,0,
								 0,0,0,0,0,0,1,
								 0,0,0,0,0,0,0,
								 0,0,0,0,0,0,0,
								 0,0,0,0,0,0,0};
		
		lsaMatrix.setVals(matrixValArray,6,7);
		
		double [][]matrix=lsaMatrix.getMatrix();
		System.out.println("Initial Matrix:");
		for(int i=0;i<lsaMatrix.getRowSize();i++)
		{

			for(int j=0;j<lsaMatrix.getColumnSize();j++)
			{
				System.out.format("%.3f",matrix[i][j]);
				System.out.print(" ");
			}
			System.out.println("");
		}
		
		LSADecomposer lsaD = new LSADecomposer();
		lsaD.setLsaElement(lsaMatrix);
		lsaD.SVDDecomposition(2);
		//lsaMatrix.SVDDecomposition(2);
	}
}
