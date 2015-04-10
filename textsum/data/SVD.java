package textsum.data;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class SVD {
	Matrix Main;
	Matrix U,S,V,VT,MainPrime;
	SingularValueDecomposition svd;
	Integer rank;
	
	public void SVDDecompose(double[][] matrix) {
		Main=new Matrix(matrix);
		svd = new SingularValueDecomposition(Main);
		
		U = svd.getU();//mxn
		V = svd.getV();//nxn
		VT = V.transpose();
		S = svd.getS();//nxn
	}
	
	public void calculateMainPrime(Integer k,Integer rowSize, Integer columnSize){
		rank = k;
		Matrix UMatrix = new Matrix(rowSize,rank,0.);
		Matrix VTMatrix = new Matrix(rank,columnSize,0.);
		Matrix SMatrix = new Matrix(rank,rank,0.);
	            
		UMatrix.setMatrix(0, rowSize-1, 0, rank-1, U);
		VTMatrix.setMatrix(0, rank-1, 0, columnSize-1, VT);
		SMatrix.setMatrix(0, rank-1, 0, rank-1, S);
		
		MainPrime = UMatrix.times(SMatrix.times(VTMatrix));
	}
		
	public Matrix getMain() {
		return Main;
	}
	public void setMain(Matrix main) {
		Main = main;
	}
	public Matrix getU() {
		return U;
	}
	public void setU(Matrix u) {
		U = u;
	}
	public Matrix getS() {
		return S;
	}
	public void setS(Matrix s) {
		S = s;
	}
	public Matrix getV() {
		return V;
	}
	public void setV(Matrix v) {
		V = v;
	}
	public Matrix getVT() {
		return VT;
	}
	public void setVT(Matrix vt) {
		VT = vt;
	}
	public Matrix getMainPrime() {
		return MainPrime;
	}
	public void setMainPrime(Matrix mainPrime) {
		MainPrime = mainPrime;
	}
	public SingularValueDecomposition getSvd() {
		return svd;
	}
	public void setSvd(SingularValueDecomposition svd) {
		this.svd = svd;
	}
	
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
