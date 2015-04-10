package textsum.data;

import java.util.Vector;

public class WordGroups {

	private Vector<Word> wordGroupElement = new Vector<Word>();

	public Vector<Word> getWordGroupElement() {
		return wordGroupElement;
	}

	public void setWordGroupElement(Vector<Word> wordGroupElement) {
		this.wordGroupElement = wordGroupElement;
	}

	public String toString()
	{
		StringBuilder buf = new StringBuilder();
		for ( Word w : this.wordGroupElement )
			buf.append(" ").append( w );
		return buf.toString();
	}
}
