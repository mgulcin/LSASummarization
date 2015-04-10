
package textsum.data;

import java.util.Vector;


public class Word
{
	private String wordText = "";
	private String wordType = "";
	private Vector<ZemberekMorphology> morphs = new Vector<ZemberekMorphology>();
		
	public String getWordText( )
	{
		return this.wordText;
	}

	public void setWordText( String wordText )
	{
		this.wordText = wordText;
	}

	public String getWordType() {
		return wordType;
	}

	public void setWordType(String wordType) {
		this.wordType = wordType;
	}

	public Vector<ZemberekMorphology> getMorphList( )
	{
		return this.morphs;
	}

	public void setMorphList( Vector<ZemberekMorphology> morphs )
	{
		this.morphs = morphs;
	}

	public ZemberekMorphology getMorph()
	{
		if ( this.morphs.size() > 0 )
			return this.morphs.get(0);
		return null;
	}
	
	public String toString()
	{
		return this.wordText;
	}


}

