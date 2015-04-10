
package textsum.data;

import java.util.Vector;


public class Sentence
{
	// a sentence can either words or wordgroups, or both
	private String sentence;
	private Vector<Word> words = new Vector<Word>();
	private Vector<WordGroups> wordGroups = new Vector<WordGroups>();

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public Vector<Word> getWords( )
	{
		return this.words;
	}

	public void setWords( Vector<Word> words )
	{
		this.words = words;
	}
	
	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		for ( Word w : this.words )
			buf.append(" ").append( w );
		//buf.append( "." );
		return buf.toString();
	}

	public void setWordGroups(Vector<WordGroups> wordGroups) {
		this.wordGroups = wordGroups;
	}

	public Vector<WordGroups> getWordGroups() {
		return wordGroups;
	}
	
}

