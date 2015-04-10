
package textsum.data;

import java.util.*;


public class ZemberekMorphology //implements Cloneable
{
	private String root = "";
	private String rootType = "";
	private Vector<String> affixes = new Vector<String>();

	
	public String getRoot( )
	{
		return this.root;
	}

	public void setRoot( String root )
	{
		this.root = root;
	}

	public String getRootType( )
	{
		return this.rootType;
	}

	public void setRootType( String rootType )
	{
		this.rootType = rootType;
	}

	public Vector<String> getAffixes( )
	{
		return this.affixes;
	}

	public void setAffixes( Vector<String> affixes )
	{
		this.affixes = affixes;
	}
	
	public String toString( )
	{
		StringBuilder buf = new StringBuilder();
		buf.append( this.root );
		buf.append("+").append( this.rootType );
		for ( String affix : this.affixes )
			buf.append("+").append( affix );
		return buf.toString();
	}
	
	/*
	public ZemberekMorphology clone()
	{
		ZemberekMorphology c = null;
		try {
			c = ( ZemberekMorphology )super.clone();
		}
		catch ( CloneNotSupportedException e )
		{
		}
		
		c.affixes = ( Vector<String> )c.affixes.clone();
		
		
		return c;
	}
	*/
	/*
	public String birlestir()
	{
		Zemberek zemberek;
		zemberek = new Zemberek(new TurkiyeTurkcesi());
	   // Kelime kelime =new Kelime();//= cozumler[0];
	    Kok kok = new Kok(root /--*, KelimeTipi.valueOf( rootType )*--/ ); //zemberek.dil().kokler().kokBul("koyun").get(0);
	    Vector<Object> ofAman = new Vector<Object>();
	    for ( String s_ek : affixes )
	    	ofAman.add( new Ek(s_ek) );
	    String yenikelime = zemberek.kelimeUret(kok, ofAman);
	    System.out.println("\nkok degisimi sonrasi yeni kelime: " + yenikelime);
		return yenikelime;
	}
	*/
}

