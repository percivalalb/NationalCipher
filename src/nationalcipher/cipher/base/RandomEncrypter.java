package nationalcipher.cipher.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nationalcipher.cipher.base.other.ADFGX;
import nationalcipher.cipher.base.other.Bifid;
import nationalcipher.cipher.base.other.ConjugatedBifid;
import nationalcipher.cipher.base.other.Digrafid;
import nationalcipher.cipher.base.other.Hill;
import nationalcipher.cipher.base.other.Homophonic;
import nationalcipher.cipher.base.other.Morbit;
import nationalcipher.cipher.base.other.PeriodicGromark;
import nationalcipher.cipher.base.other.Playfair;
import nationalcipher.cipher.base.other.Pollux;
import nationalcipher.cipher.base.other.SeriatedPlayfair;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Trifid;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.base.substitution.Bazeries;
import nationalcipher.cipher.base.substitution.Beaufort;
import nationalcipher.cipher.base.substitution.BeaufortAutokey;
import nationalcipher.cipher.base.substitution.BeaufortNicodemus;
import nationalcipher.cipher.base.substitution.BeaufortProgressiveKey;
import nationalcipher.cipher.base.substitution.BeaufortSlidefair;
import nationalcipher.cipher.base.substitution.Caesar;
import nationalcipher.cipher.base.substitution.Enigma;
import nationalcipher.cipher.base.substitution.FourSquare;
import nationalcipher.cipher.base.substitution.FractionatedMorse;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.base.substitution.NihilistSubstitution;
import nationalcipher.cipher.base.substitution.Porta;
import nationalcipher.cipher.base.substitution.PortaAutokey;
import nationalcipher.cipher.base.substitution.PortaNicodemus;
import nationalcipher.cipher.base.substitution.PortaProgressiveKey;
import nationalcipher.cipher.base.substitution.Portax;
import nationalcipher.cipher.base.substitution.QuagmireI;
import nationalcipher.cipher.base.substitution.QuagmireII;
import nationalcipher.cipher.base.substitution.QuagmireIII;
import nationalcipher.cipher.base.substitution.QuagmireIV;
import nationalcipher.cipher.base.substitution.RunningKey;
import nationalcipher.cipher.base.substitution.TriSquare;
import nationalcipher.cipher.base.substitution.TwoSquare;
import nationalcipher.cipher.base.substitution.Variant;
import nationalcipher.cipher.base.substitution.VariantAutokey;
import nationalcipher.cipher.base.substitution.VariantNicodemus;
import nationalcipher.cipher.base.substitution.VariantProgressiveKey;
import nationalcipher.cipher.base.substitution.VariantSlidefair;
import nationalcipher.cipher.base.substitution.Vigenere;
import nationalcipher.cipher.base.substitution.VigenereAutokey;
import nationalcipher.cipher.base.substitution.VigenereNicodemus;
import nationalcipher.cipher.base.substitution.VigenereProgressiveKey;
import nationalcipher.cipher.base.substitution.VigenereSlidefair;
import nationalcipher.cipher.base.transposition.AMSCO;
import nationalcipher.cipher.base.transposition.Cadenus;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.base.transposition.Myszkowski;
import nationalcipher.cipher.base.transposition.NihilistTransposition;
import nationalcipher.cipher.base.transposition.Phillips;
import nationalcipher.cipher.base.transposition.RailFence;
import nationalcipher.cipher.base.transposition.Redefence;
import nationalcipher.cipher.transposition.RouteTransposition;

public class RandomEncrypter {

	public static Map<String, IRandEncrypter> ciphers = new HashMap<String, IRandEncrypter>();
	public static Map<String, Integer> ciphersDifficulty = new HashMap<String, Integer>();
	
	public static IRandEncrypter getFromName(String name) {
		return ciphers.get(name);
	}
	
	public static IRandEncrypter getDifficulty(String name) {
		return ciphers.get(name);
	}
	
	public static List<String> getAllWithDifficulty(int maxDifficulty) {
		List<String> below = new ArrayList<String>();
		for(String key : ciphersDifficulty.keySet())
			if(ciphersDifficulty.get(key) <= maxDifficulty)
				for(int i = 0; i < Math.log(ciphersDifficulty.get(key)) / Math.log(2) + 1; i++) 
					below.add(key);

		return below;
	}
	
	//By Default difficulty is 5 (medium)
	public static void registerEncrypter(IRandEncrypter randEncrypter) {
		registerEncrypter(randEncrypter, 5);
	}
	
	/**
	 * 
	 * @param randEncrypter
	 * @param difficulty Integer 1 to 10 inclusive, 1 being easier to decrypt that 10
	 */
	public static void registerEncrypter(IRandEncrypter randEncrypter, int difficulty) {
		ciphers.put(randEncrypter.getClass().getSimpleName(), randEncrypter);
		ciphersDifficulty.put(randEncrypter.getClass().getSimpleName(), difficulty);
	}
	
	static {
		registerEncrypter(new ADFGX(), 10);
		registerEncrypter(new Affine(), 1);
		registerEncrypter(new AMSCO(), 3);	
		registerEncrypter(new Bazeries(), 3);
		registerEncrypter(new Beaufort(), 2);
		registerEncrypter(new BeaufortAutokey(), 2);
		registerEncrypter(new BeaufortNicodemus(), 3);
		registerEncrypter(new BeaufortProgressiveKey(), 4);
		registerEncrypter(new BeaufortSlidefair(), 2);
		registerEncrypter(new Bifid(), 5);
		registerEncrypter(new Caesar(), 1);
		registerEncrypter(new Cadenus(), 7);
		registerEncrypter(new ColumnarTransposition(), 2);
		registerEncrypter(new ConjugatedBifid(), 6);
		registerEncrypter(new Digrafid(), 5);
		registerEncrypter(new Enigma(), 8);
		registerEncrypter(new FourSquare(), 7);
		registerEncrypter(new FractionatedMorse(), 5);
		registerEncrypter(new Hill(), 7);
		registerEncrypter(new Homophonic(), 5);
		registerEncrypter(new Keyword(), 1);
		registerEncrypter(new Morbit());
		registerEncrypter(new Myszkowski(), 3);
		registerEncrypter(new NihilistSubstitution(), 4);
		registerEncrypter(new NihilistTransposition(), 6);
		registerEncrypter(new PeriodicGromark());
		registerEncrypter(new Phillips(), 7);
		registerEncrypter(new Playfair(), 8);
		registerEncrypter(new Pollux(), 5);
		registerEncrypter(new Porta(), 2);
		registerEncrypter(new PortaAutokey(), 2);
		registerEncrypter(new PortaNicodemus(), 3);
		registerEncrypter(new PortaProgressiveKey(), 4);
		registerEncrypter(new Portax(), 3);
		registerEncrypter(new QuagmireI(), 6);
		registerEncrypter(new QuagmireII(), 6);
		registerEncrypter(new QuagmireIII(), 7);
		registerEncrypter(new QuagmireIV(), 8);
		registerEncrypter(new RailFence(), 1);
		//registerEncrypter(new Redefence(), 2);
		registerEncrypter(new RouteTransposition(), 4);
		registerEncrypter(new RunningKey(), 10);
		registerEncrypter(new SeriatedPlayfair(), 8);
		registerEncrypter(new Solitaire(), 10);
		//TODO registerEncrypter(new Swagman());
		registerEncrypter(new Trifid(), 8);
		registerEncrypter(new TwoSquare(), 7);
		registerEncrypter(new TriSquare(), 9);
		registerEncrypter(new Variant(), 2);
		registerEncrypter(new VariantAutokey(), 2);
		registerEncrypter(new VariantNicodemus(), 3);
		registerEncrypter(new VariantProgressiveKey(), 4);
		registerEncrypter(new VariantSlidefair(), 2);
		registerEncrypter(new Vigenere(), 2);
		registerEncrypter(new VigenereAutokey(), 2);
		registerEncrypter(new VigenereNicodemus(), 3);
		registerEncrypter(new VigenereProgressiveKey(), 4);
		registerEncrypter(new VigenereSlidefair(), 2);
	}
}
