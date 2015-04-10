
package textsum.morphology;

import net.zemberek.erisim.Zemberek;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.ek.Ek;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;

import textsum.data.*;
import java.util.Vector;

public class MorphologicalAnalyzer
{
	Zemberek zemberek;

	public MorphologicalAnalyzer()
	{
		this.zemberek = new Zemberek(new TurkiyeTurkcesi());
	}
	/**
	@param String wordText
	@return Word
	 */
	public Word analyzeWord( Word word )
	{
		//word.Text is already set and morphsVector is not created yet
		// zembereğe sor, listeyi al
		Vector<ZemberekMorphology> morphs = new Vector<ZemberekMorphology>();
		String wordText = word.getWordText();
		Kelime[] cozumler = this.zemberek.kelimeCozumle(wordText);
		// zembereğin listesini, morphs'a ekle tek tek
		for ( Kelime kelime :  cozumler )
		{
			ZemberekMorphology morph = new ZemberekMorphology();
			morph.setRoot( kelime.kok().icerik() );
			morph.setRootType( kelime.kok().tip().toString() );
			for ( Ek ek : kelime.ekler() )
				morph.getAffixes().add( ek.toString() );

			morphs.add( morph );
		}
		if(cozumler.length==0) 
		{
			ZemberekMorphology morph = new ZemberekMorphology();
			morph.setRoot(wordText);
			//morph.setRootType(rootType);
			morphs.add(morph);
		}

		word.setMorphList(morphs);
		return word;
	}

	/**
	@param String wordText
	@return Word
	 */
	public Word analyzeWord( String wordText )
	{
		Word word = new Word();

		word.setWordText( wordText );

		// zembereğe sor, listeyi al
		Vector<ZemberekMorphology> morphs = new Vector<ZemberekMorphology>();
		if(false)//Tr
		{
			Kelime[] cozumler = this.zemberek.kelimeCozumle(wordText);
			// zembereğin listesini, morphs'a ekle tek tek
			for ( Kelime kelime :  cozumler )
			{
				ZemberekMorphology morph = new ZemberekMorphology();
				morph.setRoot( kelime.kok().icerik() );
				morph.setRootType( kelime.kok().tip().toString() );
				for ( Ek ek : kelime.ekler() )
					morph.getAffixes().add( ek.toString() );

				morphs.add( morph );
			}

			if(cozumler.length==0) 
			{
				ZemberekMorphology morph = new ZemberekMorphology();
				morph.setRoot(wordText);
				morph.setRootType("DUMMY");//!!!
				morphs.add(morph);
			}

		}
		else if(true)//Eng
		{
			Stemmer s = new Stemmer();
			String lowerWord = wordText.toLowerCase();
			for (int c = 0; c < lowerWord.length(); c++)
			{
				s.add(lowerWord.charAt(c));
			}
			s.stem();
			String wText = s.toString();
			
			ZemberekMorphology morph = new ZemberekMorphology();
			morph.setRoot(wText);
			morph.setRootType("DUMMY");//!!!
			morphs.add(morph);

		}
		//System.out.println("last: "+morphs.lastElement());	
		word.setMorphList(morphs);
		return word;
	}
	//	
	//	/**
	//	@param String wordText
	//	@return Word
	//	*/
	//	public Word analyzeWord( String wordText )
	//	{
	//		Word word = new Word();
	//		
	//		word.setWordText( wordText );
	//		
	//		// zembere�e sor, listeyi al
	//		Vector<ZemberekMorphology> morphs = new Vector<ZemberekMorphology>();
	////lalalaa
	//		Kelime[] cozumler = this.zemberek.kelimeCozumle(wordText);
	//		if( cozumler.length == 0 )
	//		{
	//			String wordRoot = this.askExternalDictionary(wordText);
	//			this.zemberek.dilBilgisi().kokler().ekle(new Kok(wordRoot, KelimeTipi.ISIM));
	//
	//			cozumler = this.zemberek.kelimeCozumle(wordText);
	//		}
	////lalalaa
	//		
	//		// zembere�in listesini, morphs'a ekle tek tek
	//		for ( Kelime kelime :  cozumler )
	//		{
	//			ZemberekMorphology morph = new ZemberekMorphology();
	//			morph.setRoot( kelime.kok().icerik() );
	//			morph.setRootType( kelime.kok().tip().toString() );
	//			for ( Ek ek : kelime.ekler() )
	//				morph.getAffixes().add( ek.toString() );
	//			
	//			morphs.add( morph );
	//		}
	//		
	//		// morfolojilerden ayarlamalari yap
	//		for ( ZemberekMorphology morph : morphs )
	//			this.reduceMorphology( morph );
	//		
	//		Vector<ZemberekMorphology> wordMorphs = word.getMorphList();
	//		HashSet<String> seenMorphs = new HashSet<String>();
	//		
	//		// Ayn�s�ndan daha �nce eklemedi�imiz ve bo� olmayan morfolojileri sil
	//		for ( ZemberekMorphology morph : morphs )
	//		{
	//			if ( morph.getAffixes() == null )
	//				continue;
	//
	//			String s = morph.toString(); // bu methodu ZemberekMorphology sagliyor
	//			if ( seenMorphs.contains( s ) == false )
	//			{
	//				seenMorphs.add( s );
	//				wordMorphs.add( morph );
	//			}
	//		}
	//
	//	/*	if ( "kitle".equals(wordText) || wordMorphs.isEmpty() )
	//		{
	//			//if ( ",".equals(wordText) == false && "kitle".equals(wordText) == false )
	//				//System.out.println("[MorphAnaly] \"" + wordText + "\" i�in hi� morph bulunamam��, kendisini k�k niyetine ekliyorum");
	//			
	//			ZemberekMorphology morph = new ZemberekMorphology();
	//			
	//			morph.setRoot( wordText );
	//			morph.setRootType( "SAHTE_RADIOREAD_KOK" );
	//			
	//			wordMorphs.clear();
	//			wordMorphs.add( morph );
	//		}*/
	//		
	//		// bitti
	//		return word;
	//	}
	//	
	//	/**
	//	Zemberekten gelen veride bu donusumler yap�lacak, ard�ndan gecerlilik kontrol edilecek:
	//	+ ISIM_SAHIPLIK_O_I = ISIM_TAMLAMA_I
	//	+ ISIM_BELIRTME_I = ISIM_TAMLAMA_I
	//	+ ISIM_SAHIPLIK_SEN_IN = ISIM_TAMLAMA_IN
	//	+ ISIM_SAHIPLIK_ONLAR_LERI = ISIM_COGUL_LER + ISIM_TAMLAMA_I
	//	+ ISIM_KISI_ONLAR_LER = ISIM_COGUL_LER 
	//
	//	Gecerlilik kontrolleri:
	//	+ 2 ISIM_TAMLAMA_IN ardarda gelemez, gelirse sil
	//	+ ISIM_TAMLAMA_IN ISIM_KALMA_DE ardarda gelemez, gelirse sil
	//	+ ISIM_TAMLAMA_IN ISIM_KALMA_DEN ardarda gelemez, gelirse sil
	//	
	//	@param ZemberekMorphology morph
	//	*/
	//	private void reduceMorphology( ZemberekMorphology morph )
	//	{
	//		Vector<String> affixes = morph.getAffixes();
	//		Vector<String> affixes2 = new Vector<String>();
	//
	//		// degistirilecekler
	//		for ( String affix : affixes )
	//		{
	//			/*if ( "ISIM_SAHIPLIK_O_I".equals( affix ) )
	//				affixes2.add( "ISIM_TAMLAMA_I" );
	//			else if ( "ISIM_BELIRTME_I".equals( affix ) )
	//				affixes2.add( "ISIM_TAMLAMA_I" );
	//			else if ( "ISIM_SAHIPLIK_SEN_IN".equals( affix ) )
	//				affixes2.add( "ISIM_TAMLAMA_IN" );
	//			else if ( "ISIM_SAHIPLIK_ONLAR_LERI".equals( affix ) )
	//			{
	//				affixes2.add( "ISIM_COGUL_LER" );
	//				affixes2.add( "ISIM_TAMLAMA_I" );
	//			}
	//			else if ( "ISIM_KISI_ONLAR_LER".equals( affix ) )
	//				affixes2.add( "ISIM_COGUL_LER" );
	//			else*/
	//				affixes2.add( affix );
	//		}
	//		
	//		// gecerlilik kontrolleri
	//		int i;
	//		affixes = affixes2;
	//
	//		/*if ( "alt�n".equals( morph.getRoot() )
	//			|| "�st�n".equals( morph.getRoot() ) )
	//		{
	//			morph.setAffixes( null ); // bu morfolojide hata var demek bu, iptal et, edemezsen de Exception'a haz�r ol demek
	//			return;
	//		}*/
	//
	//	/*	for ( i = 1; i < affixes.size(); i++ )
	//		{
	//			if ( "ISIM_TAMLAMA_IN".equals( affixes.get(i-1) )
	//				&& "ISIM_TAMLAMA_IN".equals( affixes.get(i) ) )
	//			{
	//				morph.setAffixes( null ); // bu morfolojide hata var demek bu, iptal et, edemezsen de Exception'a haz�r ol demek
	//				return;
	//			}
	//			else if ( "ISIM_TAMLAMA_IN".equals( affixes.get(i-1) )
	//				&& "ISIM_KALMA_DE".equals( affixes.get(i) ) )
	//			{
	//				morph.setAffixes( null ); // bu morfolojide hata var demek bu, iptal et, edemezsen de Exception'a haz�r ol demek
	//				return;
	//			}
	//			else if ( "ISIM_TAMLAMA_IN".equals( affixes.get(i-1) )
	//					&& "ISIM_CIKMA_DEN".equals( affixes.get(i) ) )
	//				{
	//					morph.setAffixes( null ); // bu morfolojide hata var demek bu, iptal et, edemezsen de Exception'a haz�r ol demek
	//					return;
	//				}
	//		}*/
	//		
	//		// tamam bence
	//		morph.setAffixes( affixes2 );
	//	}
	//	
	//	public String askExternalDictionary (String wordText)
	//	{
	//		//String wordTextKok=askZargan( wordText );
	//		//return wordTextKok;
	//
	//		String wordTextKok = wordText;
	//		/*
	//		 * if ( "fibrogland�ler".equals(wordText) )
	//			wordTextKok = "fibrogland�ler";
	//		*/
	//		
	//		return wordTextKok;
	//	}

}

