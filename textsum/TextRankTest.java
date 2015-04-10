package textsum;

public class TextRankTest {

	public static void main(String[] args) 
	{
		LSADecomposer ld = new LSADecomposer();
		
		int size = 6;
		double[][] matrix = { 
				{1.0,2.0,2.0,0.0,0.0,0.0},
				{2.0,1.0,2.0,0.0,0.0,2.0},
				{2.0,2.0,1.0,2.0,2.0,2.0},
				{0.0,0.0,2.0,1.0,2.0,0.0},
				{0.0,0.0,2.0,2.0,1.0,0.0},
				{0.0,2.0,2.0,0.0,0.0,1.0}
		};
		
		
		
		
		//ld.calculateTextRank(matrix, size);
		ld.calculateTextRank2(matrix, size);
		
		System.out.println("Result:");
		for(int i=0;i<size;i++)
		{
			System.out.print(matrix[i][i]+" ");
		}
		System.out.println();
			

	}

}
