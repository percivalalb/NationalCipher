package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.Quagmire;
import nationalcipher.cipher.tools.KeyGeneration;

public class QuagmireIV implements IRandEncrypter {

	public static void main(String[] args) {
		System.out.println(new String(encode("HARRYTHISISSTRICTLYEYESONLYFIVEMINUTESWITHTHEOLLADYWASENOUGHTOGIVEOURTECHGUYSAHEADACHENOTHINGMUCHSHOWEDUNDERULTRAVIOLETBUTTHEYMANAGEDTOCATCHASCRAPINGOFVARNISHFROMTHEBACKOFTHEPAINTINGANDFOUNDHIGHCONCENTRATIONSOFLEADTHEYDIDNOTEXPECTTHISANDWEARETRYINGTOGETPERMISSIONTOXRAYHERBUTTHECURATORISNOTKEENFORHERTOBEMOVEDAGAINAFTERTHEYEARSOFTRAVELDURINGTHEWAROURAGENTSARETRYINGTOTRACKHERMOVEMENTSDURINGTHATTIMETOSEEWHENSHEMIGHTHAVEBEENBROUGHTTOMONTMARTRESHELEFTPARISONTHETWENTYEIGHTHOFAUGUSTJUSTBEFORETHEOUTBREAKOFWARANDTRAVELLEDINACONVOYOFTHIRTYSEVENTRUCKSTOAPLACECALLEDCHAMBORDIWILLLETYOUKNOWIFIHEARANYMOREABOUTHERTRAVELSASFORSARAHWEFOUNDOUTALITTLEMOREABOUTHERFAMILYONEOFTHEBUCHENWALDNEIGHBOURSMENTIONEDACONNECTIONWITHITALYWHICHISSUGGESTIVEGIVENTHEPERUGGIASTORYTHOUGHEXACTLYWHATTHECONNECTIONMIGHTBEIAMNOTSUREMOREWHENIHAVETIMEPHIL", "SENORYABCDFGHIJKLMPQTUVWXZ", "PERCTIONABDFGHJKLMQSUVWXYZ", "EXTRA", 'S')));
		System.out.println(new String(decode("VBMRFCYISPMPBRRHEICXRREIGDX".toCharArray(), "SENORYABCDFGHIJKLMPQTUVWXZ", "PERCTIONABDFGHJKLMQSUVWXYZ", "EXTRA", 'S')));
	}
	
	public static String encode(String plainText, String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
		return Quagmire.encode(plainText, topKey, gridKey, indicatorKey, indicatorBelow);
	}
	
	public static char[] decode(char[] cipherText,  String topKey, String gridKey, String indicatorKey, char indicatorBelow) {
		return Quagmire.decode(cipherText, topKey, gridKey, indicatorKey, indicatorBelow);
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26(), KeyGeneration.createLongKey26(), KeyGeneration.createShortKey26(2, 15), 'A');
	}
}