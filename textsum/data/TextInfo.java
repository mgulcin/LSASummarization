package textsum.data;

import java.util.HashMap;
import java.util.Vector;

import textsum.morphology.MorphologicalAnalyzer;

public class TextInfo {
	private String title;
	private String sentenceText;
	private Vector<Word> titleWordsVector;
	private Vector<Sentence> sentenceVector;
	private Vector<Integer> sentenceScoreVector;
	private Vector<Vector<Word>> wordsVector;
	private HashMap<String, Integer> wordGlobalHashMap;
	private Vector<HashMap<String, Integer>> wordLocalHashMap;
	private Vector<Word> stopWordsVector;
	private Vector<Word> differentWords;
	private Integer numberOfDifferentWords;

	
	public boolean isElementStopWords(Word wrd)
	{
		//System.out.println(wrd+" "+wrd.getMorph());
		if(wrd.getMorph()==null) return false;//Emin değilim!!!
		String root =  wrd.getMorph().getRoot();
		for(Word w:stopWordsVector)
		{
			if(root.equals(w.getMorph().getRoot()))
				return true;
		}
		return false;
	}
	public Vector<Word> getStopWordsVector() {
		return stopWordsVector;
	}
	public void setStopWordsVector(Vector<Word> stopWordsVector) {
		this.stopWordsVector = stopWordsVector;
	}
	public void addStopWord(Word wd){
		if(stopWordsVector == null) stopWordsVector = new Vector<Word>();
		this.stopWordsVector.add(wd);
	}
	public void addStopWord(String st){
		if(stopWordsVector == null) stopWordsVector = new Vector<Word>();
		MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
		Word wd = ma.analyzeWord(st);
		this.stopWordsVector.add(wd);
	}
	public Vector<Word> getTitleWordsVector() {
		return titleWordsVector;
	}
	public void setTitleWordsVector(Vector<Word> titleWordsVector) {
		this.titleWordsVector = titleWordsVector;
	}
	public void appendTitleWordsVector(Vector<Word> titleWordsVector){
		if(this.titleWordsVector!=null)
		{
			this.titleWordsVector.addAll(titleWordsVector);
		}
		else this.titleWordsVector = titleWordsVector;
	}
	public Vector<Integer> getSentenceScoreVector() {
		return sentenceScoreVector;
	}
	public void setSentenceScoreVector(Vector<Integer> sentenceScore) {
		this.sentenceScoreVector = sentenceScore;
	}
	public HashMap<String, Integer> getWordGlobalHashMap() {
		return wordGlobalHashMap;
	}
	public void setWordGlobalHashMap(HashMap<String, Integer> wordGlobalHashMap) {
		this.wordGlobalHashMap = wordGlobalHashMap;
	}
	public Vector<HashMap<String, Integer>> getWordLocalHashMap() {
		return wordLocalHashMap;
	}
	public void setWordLocalHashMap(Vector<HashMap<String, Integer>> wordLocalHashMap2) {
		this.wordLocalHashMap = wordLocalHashMap2;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSentenceText() {
		return sentenceText;
	}
	public void setSentenceText(String sentenceText) {
		this.sentenceText = sentenceText;
	}
	public Vector<Sentence> getSentenceVector() {
		return sentenceVector;
	}
	public void setSentenceVector(Vector<Sentence> sentenceVector) {
		this.sentenceVector = sentenceVector;
	}
	public void appendSentenceVector(Vector<Sentence> sentenceVector){
		if(this.sentenceVector!=null)
		{
			this.sentenceVector.addAll(sentenceVector);
		}
		else this.sentenceVector = sentenceVector;
	}
	public Vector<Vector<Word>> getWordsVector() {
		return wordsVector;
	}
	public void setWordsVector(Vector<Vector<Word>> wordsVector) {
		this.wordsVector = wordsVector;
	}
	
	public Integer getNumberOfDifferentWords() {
		return numberOfDifferentWords;
	}
	public void setNumberOfDifferentWords(Integer numberOfDifferentWords) {
		this.numberOfDifferentWords = numberOfDifferentWords;
	}
	public Vector<Word> getDifferentWords() {
		return differentWords;
	}
	public void setDifferentWords(Vector<Word> differentWords) {
		this.differentWords = differentWords;
	}
	
}
