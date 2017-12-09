package nationalcipher.cipher.decrypt.keyfinder;

import java.util.Arrays;
import java.util.Set;

import javalibrary.string.StringTransformer;
import nationalcipher.cipher.tools.KeyGeneration;

public class GeneralPeriodicKey {

	public String plainText;
	public String cipherText;
	public int period;
	public String[] keyText;
	
	public GeneralPeriodicKey(String plainText, String cipherText, String keyText) {
		this.plainText = plainText;
		this.cipherText = cipherText;
		this.keyText = keyText.split(", ");
		this.period = this.keyText.length;
	}
	
	
	public static void main(String[] args) {
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println(KeyGeneration.createLongKey26());
		System.out.println(KeyGeneration.createLongKey26());
		String cipherText = "YKUFRQHUDDQRGZPPISMXOOEYZUDOGMPPRLZQAERLOPNLOVKTVFNXOYZCQDZOVDVECXUAIBKVGLBEJRVPSLARHDTWOBNIMLWDFGGTAUQGONBLRFLNMGMDRPYBVIGKXVXDBOQXUGBUVOKLQLPGHROVAMGIUKRDPHDOQEOEIGIUKXIVDLYJLZIPEGELWVFDBMDBSGZKCGSYSOOOIOEOBOPTEMMKHCROCVNLWDJOKSIGWCODTJGZCVLSYOYVTOURIWMHFDLZQKUAIIAWBOOAMKHDNGEQBTOEICUGKFGYUCZIESQPUGAKANFMELCLZTMVINRRIRDBOBVGHJBQYHGCKMSJILPIZQJLXEELPZJSNOYCQIMJMDHKIPIAWBOAOBMBOLWESOGLYVOABXCENDBOQXUGBUVNJCDNVBQUUQGVAJTCLIOXGBJVPUBAUTDSQYCTMNNTRHDAUFEKVPFBSNVYVWAXKZQJAAMMRIZOVJGMMUMXELJTHYBAJVCPKLWHMRBVCMWIGKQRNVLYROVAONGUKWBOXGGDMVTGJJOKEUMCTGDYDHPAYTVGNKAUBSFNDMYGNEEBODYLSIVMWEIUHOIYXIIXRZILMINLCRNYBODTQGKACUKPJNVLYROVGSBIGCRKFYRNLDPIAEGEEZSHVGMROREIAWGMEFFKAILLHWPUMTIPJLBEQBADDORIELFILZDJSNCTQCTMEGRJAAVQQWEIAVTIBNFWAOFTGLYDDLEERFYBGUOVCMWTLLDQAWYZOQSQENAPVPYRHGBKXNTOUXTXWXOXGNISDDMOIRVXRICLWKDZEIWINWCUNDYJSNQYWWIRVXSYBOTNVHFLAUTECBGYBODTKGBSTLLYIUDELGTIAKCVTIYKNGLLQMQIINGXKAOHURMNQSINWTLXXSGAAQBHSGIUQQCPLNATQBHNNJECMKCRIPGXIVHGMDJOKPQJWYBXLIUODKUQMSZSDYROXAIMMSIFLXYEISEHTOACTDJAOXGKSMVANPIRDRYRUOBROTHJCDRDJQNNTOULKGHHSSCGKFNYZROVGSBIGCRKZUKSOEOZGGKUMCBKUAPBLWISCKEIDPFERPSEJHJCGXPGRPUOIKRSOIDKBEBYZKDLYNTAPXIEKAEFRGBBLIYVDDTJRICERAQOVCMWUOIRENVCQROPGAKVVVSRICNBOHXDOBIRWEMPDBAOQVZGNKRIPJICEHQMWBOPIUCQIYJTGBUHELQGFSYSOYEIIESQPXIELZOODBONLPAGJJOKQIGCJKONIGDCGQKXKLZOIRVDAKPAUKWXIEAILLHWMDJOKPQJWIZOQVHPCVUCQOOUZMRTOHJCPVRFLGAQTQANTODDTACNNNZIIAZEYETUFEANATGXUQGAUTNSBKIEFIIAZEBOQNIRDNOIZYNHDOGIEEGSLNZLYFXOIHGTPGYHXYZIGTHWWETIRQNFFBQNPDYODRXSQGHQGYGZYIDMBVGXPOGZRRGZCPIRLEUMCTGDMNKQDKRVFIHCSWDTUERPAXIPOPNHJQENTPXIGVUSYQZGUNTRHMIUUWYJPIPQUNKOUPUFENFWAGXUQGAUBTNWCZKDPIGDKYUQGHEOGCTMEGXSGZGOIYOKPGMJDUEQVJOKDDPOGGXNDSJAFLEYVVDIVALDZGNAUXECFRGBROVSGVIWPEXFPGPLTUEIWKEYWXOXGYQRIHJCTDCMULMLOKBIGPXGUUWYJEDLZLXRJEMDCMRNZDSQUQEPETUKBNPQGZCTCTGGGFIDRVPGHKSFAWQWTEFKOXEPROVHBAKUCRCLZUMCBPBMWJOKQIGCJMBMXASACNMPEIEAIYSUYBDIGEGBOXGZIGWBKXKQKFLNHEBVRIIMNCUFEUHCDDQNTTOKTQMUQKMHGURLGSKUOCLRICSGJTYSMZWRWMKPWEZQMDSMDIMXWULNEZJOVKWWJYKYRFBFOJYVNGPIOKQOYVGIRGNQPXINKQYAEHYIAWBOEMSKRSLZSQPOVTIDDTWWJNFCQNQAGBCULZEDNVYBALDQGKVAIISKMEZJTKGBMAEKOIVSRGMPXDLBGDLZQMPIAQRGZIUJLLZQHCVOBUQFMPJLRFLNDTWQNWYBMAAISDPOYLOVGCJEUOLZQUAHOIAWBONKELZNNQIGBRGZGJJOKQIGCJQRNVDVYKQHGBOEKOVTIGDCGQKXYYDILROVRQDEVHBVNQOMDMVZTMJOKCBZEGZRYTLYZAEMCBBUZHQNPYVYGG";
		String plainText = "WITHRESOLVEANDTREPIDATIONMYDEARFATHERINLAWOBEDIENTLYCONCLUDEDHISAFFAIRSINBRITANNIAANDRETURNEDTOROMEBYTHEENDOFTHEYEARASORDEREDBYTHEEMPERORHEFEAREDTHESAMETERRIBLEENDASMETEDOUTBYDOMITIANTOBOTHARULENUSRUSITCUSANDHERNNIUSSENECIOBUTTHEGODSWEREGENEROUSANDREMARKABLYTHELOSSOFTHECODEVREMAINEDASECRETKNOWNONLYTOAFEWLOYALCOMRADESFROMTHENINTHPUBLICLYCHOOSINGTOREGARDHISDOGGEDPURSUITOFTHEABUILAASAMARKOFCOURAGETHEEMPERORSPAREDHIMTHEFATEOFOTHERDISGRACEDGENERALSANDTOTHESURPRISEOFSOMEHEWASAWARDEDTRIUMPHALDECORATIONSASTATUEWASERECTEDBYTHESENATEONTHEORDEROFTHEEMPERORBUTTAKENILLAFRICOLAWASSENTTOLIVEQUIETLYONHISFAMILYESTATEWHEREHEWASTENDEDBYTHEEMPERORSOWNPHYSICIANSHISDEATHWASAGRIEVOUSSHOCKTOMEANDAPAINFULEVENTTOALLHISFRIENDSITWASFELTASAREALLOSSEVENBYTHOSETOWHOMHEWASNOTPERSONALLYKNOWNNUMBERSMOREOVEROFTHEPOPULACEANDTHEBUSYMASSESCAMETOHISHOUSEANDINPUBLICPLACESANDWHEREVERKNOTSOFTALKERSWEREASSEMBLEDHISNAMEWASONALLLIPSNORDIDASINGLESOULONHEARINGOFHISDEATHREGOICEATTHENEWSORFORGETITQUICKLYTHISSYMPATHYWASINCREASEDBYTHEWIDESPREADRUMOURTHATHEHADBEENREMOVEDBYPOISONONTHEEMPERORSCOMMANDDOMITIANLOSTNOTIMEINAPPOINTINGTHEAMBITIOUSSALUSTIUSLUCULLUSASTHENEWGOVERNOROFTHEPROVINCEHEWASCHARGEDWITHSECURINGTHEGRAGILEPEACEWITHCALEDONIAANDHEADEDTHERETOCONFRONTCALGACUSITWASONLYTHENTHATTHELOSSOFTHECODEXWASREVEALEDSALUSTIUSWROTEOFHISSHOCKATTHENEWSINALETTERTOCATODATEDTHEFIFTHDAYBEFORETHEWALENDSOFMAYINTHEYEAROFTHECONSULSHIPSOFMARCUSARRECINUSCLEMENSIIANDLUCIUSBAEBIUSHONORATUSWEAREEXPRESSLYCHARGEDBYOURMOSTMUNIFICENTANDGREATEMPERORDOMITIANTOSECUREPAXROMANAINTHENORTHERNKINGDOMSANDYETYOUWRITETOMEOFTHELOSSOFTHECODEMOCCULTORUMYOURLEADERAPRICOLAHASALREADYPAIDTHEPRICEOFSUCHALOSSBUTIFYOUDONOTRECOVERTHECODEGBEFORETHEPASSINGOFTHEYEARTHENYOUMAYBESURETHATYOUWILLJOINHIMYOUTELLMETHATINTELLIGENCEFROMACAPTUREDSPYSUGGESTSTHATTHETRAITORCALFACUSHASTHECODEXANDISMASSINGHISSUPPORTERSINTHENORTHWHILETHEBARBARIANSTHEMSELVESAREUNLIKELYTOMAKEMUCHOFTHECODEXCALGACUSISANEDUCATEDROMANANDHEMUSTBESTOPPEDBEFOREHECANDESTROYTHESECURITYOFTHEIMPERIALCIGHERSTHECODEXWILLBERECOVEREDORYOUYOURFAMILYANDEVERYONEYOUKNOWWILLPAYTHEPRICEANDSOTOTHESEVENTHPARTOFTHETRUESTORYOFAGRICOLAINWHICHTHEFATEOFTHENINTHLEGIONANDOFTHECODEXITSELFWILLINTIMEBEREVEALEDUNTILTHENITWILLBEGUARDEDBYTHEANCIENTBABYLONIANGODDESSOFWAR";
		String key = "AHOVCJQXELSZGNUBTPWDKRYIMF, GOUBIPWDKRYFMTAHNVCJQXEZSL, RYFMTAWOVZJQXELSHGNUBKPIDC, IPWDKRYFMBOHNVCJQXELSZATUG, CJQXELSZGNUBIPWDTRYFMHAVOK, OVCJQXELSWGNUBIPHDKRYFMTAZ, LSZGNUBIPTEKRYFMWAHOVCJDXQ, AHOVCJQXEISZGNUBLPWDKRYFMT, ELSZGUNBIXPDKRYFWTAHOVCJQM, MTAHOVXJQRCLSZGNEBIPWDKFYU, OVCJQXPLSZGNUBIEWDKRYFMTAH, RYFMTAHOVCKQXELSWGNUBIPZDJ, TAHOVCJQXELSZGNUMIPWDKRYFB, ELSZGNUBIPWDKRYFMTAHOVCXQJ, MTAHOVCJQXELSZGNFBIPWDKRYU";
		
		String key2 = "DLMCFNOIPBHQKRSTUJVWXYEZAG, AIJZCKLFMDENHOPQRGSTUVBYXW, WEFVYGHBIZAJDKLMSCOPQRXUTN, EMNDGOPJQCIRLSTUVKWXYZFABH, AIJZCKLFMWENHOPQDGSTUVBYXR, WEFVYGHBIZAJDKLMNCOPQRXSTU, EMNDGOPJQHIRLSTUVKWXYZFCBA";
		GeneralPeriodicKey gpk = new GeneralPeriodicKey(plainText, "GEWVMWVNTDMNCWDRAELKXFLAERAVQOSMFHIZOQXTKMQSLICPXKEOLILQBSAPFHEVTJMGDXDNZXOKODGKSDHSRKWVOQRGTDIRAEVOJXHAWKILAAORRTOGUPXOEZAAOXKAPXHAZDLRISDEGJWQMAHLTHSOGWOIGITDIGREJTSSRDSESLYOLAHIJXRTDIMAGKAJMSIEQDXPOYOHDPLEEROEPTRAWXMWFLUXKEYVESLDDWPRAEGYWFAJHRNAHTDIYEOWEHAKIYLWWWOUYOYBSUUOAIPLRUPXHAWKILAESSXLZLARIKAZROEHHAPLAPXKEBHADEGBAINKTHRWXIJKLNPLEOIZAPIROWHADSROILSJSLKRJENEMUWWENCTDIFUPEWWCRNPLEOXDRXSANHVIZICHIDRAHAJEUEWSFWVRUJHFEZHMAXENWVQQERAALTDEDEWWIJGTEZHPWXTAVQOBFOHXVFWWTARHDPSRAMQFKVCAHGEYOPHEWEOMSWAVOIITDMQGHMKAXKIOSNWWXBNISYYHMEWSESQAYSULPHOBCEWVVACSWDIQTDIYBMWTAHAHSFAHWHETZIPLAFYUYNMGCIGIJWPAGWIKRSUWWEIXHAHHCGTLWXHSYENYEURUECNEQEZISEKQEZXOZISLKCAJVRVWVEISWEKTENEWEZZEDMFLAHEOMJNAHFKVXNZIROIDOLIRWXLOJWISEVAHVEWHBCKRCAVQEZEBKYWTDIRAJHRARCAXRTDICWFOEOMNPLHLWWTLEUTKJTDIIDWPOCFXTPLEJIATOICPMRNDESIIUEWPLUARRNMEZMWIOINYVBPPIDSMWHWQONIVEYYRAQRDEJIAHDMOGOPVDNOTOOMWIKRCETKENENZXHLHWUOAKAPXHACZENIRAEOLUYPPSZHWXIZSQTQRDAVVTWRDEWKOSXHAAKOHIAOWHMXPYEWSOSIRAHWHAWONXRFYSMLYWIJKTDIBMQWTXIGOERGEWUEWPLUMQTARSEZHAJHWKYODXYRJXKRKYGDEEAPXENCLNZEYOMQTDETPMPEPLEEVLNPIRYISTIMGDXQOPGAPGKAJCTDMQGQWEBYOBQXTDIBCWRHWVGLULARIKIFECGIGAHSCWPVOYOEPMQTDIMEHGLASFPLHOYIAJGDNUSUCIWMAECDEUTOLOSMQGPLEZIHPOIAYEELAWIJXKENIGESQIZSNPMPACMNAXKEQWWEPOBAEPNSELAQBQXLTIEYJIHDOSMAHLPHSMWGBTKKEPXKEBYLHGRVAVACIPALWFNSPTDIOIEQICSVAVQMARTEJLAIVICLWIPMSERWHAMRXIVTERTAVHSPWTKTOAUELKRJWAELHLDVAELKXWOHSSALHRA", key2);
		gpk.populate();
		gpk.isQuagmire1();
	
	}
	
	public boolean isQuagmire1() {
		double CEIL = 0.40D;
		
		double value = 0;
		for(int p = 0; p < this.period; p++) {
			double value2 = same(p);
					System.out.println(p + " " + value);
			value = Math.max(value, value2);
		}
		System.out.println(value);
		
		return value > CEIL;
	}
	
	public double same(int indexStart) {
		String base = this.keyText[indexStart];
		
		int finalSame = 0;
		for(int p = 1; p < this.period; p++) {

			int bestSame = 0;
			for(int s = 0; s < 26; s++) {
				int same = 0;
				
				for(int i = 0; i < 26; i++) {
					if(this.keyText[(indexStart + p) % this.period].charAt((i + s) % 26) == base.charAt(i))
						same++;
				}
				bestSame = Math.max(same, bestSame);
			}
			
			finalSame += bestSame;
		}
		
		return finalSame / (26D * this.period);
	}
	
	public void populate() {
		int rowsMin = this.cipherText.length() / this.period;
		int colLeft = this.cipherText.length() % this.period;
		
		for(int p = 0; p < this.period; p++) {
			byte[] colText = new byte[26];
			//Arrays.fill(colText, 0);
			for(int i = 0; i < rowsMin + (colLeft > p ? 1 : 0); i++)
				colText[(this.plainText.charAt(i * this.period + p) - 'A')] = 1;

			System.out.println(Arrays.toString(colText));
			
			//task.setCipherText(colText);
			//task.run();
		}
	}
	
	
	
	public static void findQuagmireKey() {
		
	}
}