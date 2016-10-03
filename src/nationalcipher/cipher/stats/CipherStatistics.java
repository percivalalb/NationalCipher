package nationalcipher.cipher.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javalibrary.language.Languages;
import javalibrary.math.Statistics;
import javalibrary.streams.FileReader;
import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.manage.RandomEncrypter;


public class CipherStatistics {

	private static HashMap<String, HashMap<String, DataHolder>> map;
	
	/**
	public static List<List<Object>> getResultsFromStats(String text) {
		List<List<Object>> num_dev = new ArrayList<List<Object>>();
		
		HashMap<String, HashMap<String, DataHolder>> statistic = CipherStatistics.getOtherCipherStatistics();
		HashMap<StatisticType, Double> values = new HashMap<StatisticType, Double>();
		
		
		for(String cipherName : statistic.keySet()) {
			double total = 0.0D;
			HashMap<String, DataHolder> statistics = statistic.get(cipherName);
			if(statistics == null) continue;
			
			
			for(StatisticType type : statistics) 
				if(type instanceof StatisticBaseNumber) {
					if(values.containsKey(type))
						total += (StatisticBaseNumber)type).quantifyType(values.get(type));
					else {
						double value = type.quantifyType(text);
						values.put(type, value);
						total += value;
					}
						
				}
					
				else
					total += type.quantifyType(text);
			
			
			num_dev.put(new ArrayList<Object>(Arrays.asList(cipherName, total)));
		}
		
		return num_dev;
	}
	

	public static void compileStatsForCipher(IRandEncrypter randEncrypt) {
		List<String> list = FileReader.compileTextFromResource("/plainText.txt", true);

		List<StatisticBaseNumber> statTypes = new ArrayList<StatisticBaseNumber>();
		/**statTypes.put(StatisticsRef.IC_x1000, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.IC_MAX_x1000, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.LONG_REPEAT, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.NORMAL_ORDER, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.BIFID_MAX_0, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(200, 0));
		statTypes.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(200, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_REVERSED, new DataHolder(200, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(10, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(10, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_VARIANT, new DataHolder(10, 0));
	//	statTypes.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(10, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(30, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(3, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(30, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(30, 0));
		//statTypes.put(StatisticsRef.LOG_DIAGRAPH_SLIDEFAIR, new DataHolder(30, 0));
	   // statTypes.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(30, 0));
		
		String name = randEncrypt.getClass().getSimpleName();
		String variableName = "";
	    if(!Character.isJavaIdentifierStart(name.charAt(0)))
	    	variableName += "_";
	    for (char c : name.toCharArray())
	        if(!Character.isJavaIdentifierPart(c))
	        	variableName += "_";
	        else
	        	variableName += c;

		
		
		System.out.println("HashMap<String, DataHolder> " + variableName + " = createOrGetList(\""  + name + "\");");
		
		HashMap<StatisticBaseNumber, Double> precalculatedValues = new HashMap<StatisticBaseNumber, Double>();
		for(StatisticBaseNumber type : statTypes) {
			List<Double> values = new ArrayList<Double>();
			
			for(String line : list) {
				String plainText = line;
				
				for(int i = 0; i < type.value; ++i) {
					
				//	System.out.println("en");
					String text = randEncrypt.randomlyEncrypt(plainText);
					//if(precalculatedValues.containsKey(type))
					//	precalculatedValues.get(type);
					double value = type.getValue(text);
					values.add(value);
				}
			}
	
		    Statistics stats = new Statistics(values);
			
			System.out.println(variableName + ".put(new " + type.getClass().getSimpleName() + "(" + String.format("%.2f", stats.getMean()) + ", " + String.format("%.2f", stats.getStandardDeviation()) + "));");
		}
		System.out.println("\nComplete!");
	}**/
	
	public static HashMap<String, HashMap<String, DataHolder>> getOtherCipherStatistics() {
		if(map == null) {
			map = new HashMap<String, HashMap<String, DataHolder>>();
	
			
			HashMap<String, DataHolder> substitution = createOrGetList("Substitution");
			substitution.put(StatisticsRef.IC_x1000, new DataHolder(66.01, 2.71));
			substitution.put(StatisticsRef.IC_MAX_x1000, new DataHolder(68.32, 3.07));
			substitution.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(80.63, 8.91));
			substitution.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(76.80, 6.70));
			substitution.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(77.11, 7.21));
			substitution.put(StatisticsRef.LONG_REPEAT, new DataHolder(23.965610058575958, 2.0102777216085546));
			substitution.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.65217391304348, 1.5680328944763842));
			substitution.put(StatisticsRef.NORMAL_ORDER, new DataHolder(225.3223188405797000000000000, 27.9080248261220500000000000));
			substitution.put(StatisticsRef.BIFID_MAX_0, new DataHolder(271.18, 26.95));
			substitution.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(474.1241846046231, 76.0084566498733));
			substitution.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(66.01746637747263, 8.475323689459344));
			substitution.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(3739.0927263340480000000000000, 1076.8573199253747000000000000));
			substitution.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(428.49, 60.44));
			substitution.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(581.67, 24.57));
			substitution.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(582.17, 25.37));
			substitution.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(563.44, 24.75));
			
			HashMap<String, DataHolder> beaufort = createOrGetList("Beaufort");
			beaufort.put(StatisticsRef.IC_x1000, new DataHolder(42.11, 3.63));
			beaufort.put(StatisticsRef.IC_MAX_x1000, new DataHolder(66.53, 3.21));
			beaufort.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(69.01, 9.56));
			beaufort.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(24.40, 7.77));
			beaufort.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(30.53, 15.76));
			beaufort.put(StatisticsRef.LONG_REPEAT, new DataHolder(10.46, 2.93));
			beaufort.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.57, 11.73));
			beaufort.put(StatisticsRef.NORMAL_ORDER, new DataHolder(223.84, 31.38));
			beaufort.put(StatisticsRef.BIFID_MAX_0, new DataHolder(118.07, 25.77));
			beaufort.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(170.96, 56.25));
			beaufort.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(44.91, 3.53));
			beaufort.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1166.81, 896.87));
			beaufort.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(426.97, 25.89));
			beaufort.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(759.39, 6.56));
			beaufort.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(576.44, 23.69));
			beaufort.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(558.01, 20.38));
			
			HashMap<String, DataHolder> vigenere = createOrGetList("Vigenere");
			vigenere.put(StatisticsRef.IC_x1000, new DataHolder(42.99, 3.75));
			vigenere.put(StatisticsRef.IC_MAX_x1000, new DataHolder(66.55, 3.19));
			vigenere.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(68.97, 9.57));
			vigenere.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(25.09, 8.09));
			vigenere.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(31.01, 15.38));
			vigenere.put(StatisticsRef.LONG_REPEAT, new DataHolder(10.5212546335566250000000000, 2.9517345341344905000000000));
			vigenere.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.8347101449275340000000000, 11.5407782805919010000000000));
			vigenere.put(StatisticsRef.NORMAL_ORDER, new DataHolder(224.0864492753623300000000000, 32.9728235330279060000000000));
			vigenere.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(175.7798110435920000000000000, 58.2320049919340550000000000));
			vigenere.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(45.7087715971444300000000000, 3.5810771476970964000000000));
			vigenere.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1184.3713387205507000000000000, 907.8437292675648000000000000));
			vigenere.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(427.54, 25.46));
			vigenere.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(576.21, 23.67));
			vigenere.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(759.40, 6.53));
			vigenere.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(566.04, 22.52));
			
			HashMap<String, DataHolder> playfair = createOrGetList("Playfair");
			playfair.put(StatisticsRef.IC_x1000, new DataHolder(50.98, 2.98));
			playfair.put(StatisticsRef.IC_MAX_x1000, new DataHolder(53.97, 3.49));
			playfair.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(63.49, 7.51));
			playfair.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(40.38, 3.86));
			playfair.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(76.99, 7.24));
			playfair.put(StatisticsRef.LONG_REPEAT, new DataHolder(13.2216252474114190000000000, 1.2690694983027242000000000));
			playfair.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(30.8497826086956500000000000, 3.7339181673248363000000000));
			playfair.put(StatisticsRef.NORMAL_ORDER, new DataHolder(233.4631159420290000000000000, 32.8259322483317000000000000));
			playfair.put(StatisticsRef.BIFID_MAX_0, new DataHolder(158.46, 22.42));
			playfair.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(253.8848368274454700000000000, 28.3074790701134380000000000));
			playfair.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(52.4302649136329800000000000, 3.3217549175988060000000000));
			playfair.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1210.2630829565220000000000000, 364.5870986315568400000000000));
			playfair.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(449.98, 42.78));
			playfair.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(557.48, 27.85));
			playfair.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(557.91, 27.09));
			playfair.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(543.30, 24.52));
			playfair.put(StatisticsRef.TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			playfair.put(StatisticsRef.DOUBLE_LETTER_EVEN, new DataHolder(false));
			
			HashMap<String, DataHolder> seriatedPlayfair = createOrGetList("Seriated Playfair");
			seriatedPlayfair.put(StatisticsRef.IC_x1000, new DataHolder(49.11, 2.50));
			seriatedPlayfair.put(StatisticsRef.IC_MAX_x1000, new DataHolder(50.98, 3.01));
			seriatedPlayfair.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(61.54, 6.77));
			seriatedPlayfair.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(26.42, 2.85));
			seriatedPlayfair.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(26.67, 3.58));
			seriatedPlayfair.put(StatisticsRef.LONG_REPEAT, new DataHolder(8.59, 1.26));
			seriatedPlayfair.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.44, 2.34));
			seriatedPlayfair.put(StatisticsRef.NORMAL_ORDER, new DataHolder(232.46, 32.85));
			seriatedPlayfair.put(StatisticsRef.BIFID_MAX_0, new DataHolder(150.77, 19.73));
			seriatedPlayfair.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(227.30, 70.01));
			seriatedPlayfair.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(50.70, 3.00));
			seriatedPlayfair.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(552.35, 348.77));
			seriatedPlayfair.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(450.54, 39.54));
			seriatedPlayfair.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(551.36, 28.03));
			seriatedPlayfair.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(552.18, 27.97));
			seriatedPlayfair.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(538.39, 25.16));
			seriatedPlayfair.put(StatisticsRef.TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			seriatedPlayfair.put(StatisticsRef.DOUBLE_LETTER_EVEN_2to40, new DataHolder(false));
			
			HashMap<String, DataHolder> solitaire = createOrGetList("Solitaire");
			solitaire.put(StatisticsRef.IC_x1000, new DataHolder(38.46, 0.42));
			solitaire.put(StatisticsRef.IC_MAX_x1000, new DataHolder(49.92, 5.73));
			solitaire.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(40.03, 1.68));
			solitaire.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(14.79, 0.87));
			solitaire.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(14.78, 1.63));
			solitaire.put(StatisticsRef.LONG_REPEAT, new DataHolder(5.1354845549403230000000000, 0.8892331387414816000000000));
			solitaire.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.6165217391304340000000000, 2.9584395740484672000000000));
			solitaire.put(StatisticsRef.NORMAL_ORDER, new DataHolder(222.4618840579710200000000000, 29.7462382849075200000000000));
			solitaire.put(StatisticsRef.BIFID_MAX_0, new DataHolder(92.37, 10.50));
			solitaire.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(105.2713415046132200000000000, 13.8776832252436170000000000));
			solitaire.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(40.0047256854201400000000000, 2.5816919104277085000000000));
			solitaire.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(233.8118871177359000000000000, 159.8055294692080700000000000));
			solitaire.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(427.60, 12.22));
			solitaire.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(531.78, 32.80));
			solitaire.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(531.75, 33.00));
			solitaire.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(517.67, 28.99));
			
			HashMap<String, DataHolder> swagman = createOrGetList("Swagman");
			swagman.put(StatisticsRef.IC_x1000, new DataHolder(65.81, 2.76));
			swagman.put(StatisticsRef.IC_MAX_x1000, new DataHolder(68.04, 3.04));
			swagman.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(78.30, 7.05));
			swagman.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(45.58, 5.10));
			swagman.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(43.78, 4.60));
			swagman.put(StatisticsRef.LONG_REPEAT, new DataHolder(12.1786457684977110000000000, 1.3282465483966868000000000));
			swagman.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(47.0241304347826060000000000, 4.5797519438332180000000000));
			swagman.put(StatisticsRef.NORMAL_ORDER, new DataHolder(69.8844202898550700000000000, 29.2737289484617600000000000));
			swagman.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(339.8914879313608000000000000, 65.0130912307681700000000000));
			swagman.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(67.0952068842552500000000000, 8.6820672665165700000000000));
			swagman.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1151.3435874362190000000000000, 476.9147087108571000000000000));
			
			HashMap<String, DataHolder> hill = createOrGetList("Hill");
			hill.put(StatisticsRef.IC_x1000, new DataHolder(41.93, 2.51));
			hill.put(StatisticsRef.IC_MAX_x1000, new DataHolder(48.00, 5.03));
			hill.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(56.35, 8.57));
			hill.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(32.82, 3.04));
			hill.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(76.90, 7.25));
			hill.put(StatisticsRef.LONG_REPEAT, new DataHolder(11.0574608954837430000000000, 1.5954853220975898000000000));
			hill.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(21.8755072463768130000000000, 3.4266022534824447000000000));
			hill.put(StatisticsRef.NORMAL_ORDER, new DataHolder(215.5181159420290000000000000, 30.2644808686260550000000000));
			hill.put(StatisticsRef.BIFID_MAX_0, new DataHolder(111.79, 19.61));
			hill.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(206.0340229309999500000000000, 23.6102261329829060000000000));
			hill.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(43.6375430128524700000000000, 3.0024386382334796000000000));
			hill.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(949.5763054281967000000000000, 343.3313308706509600000000000));
			hill.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(435.79, 26.22));
			hill.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(548.25, 30.74));
			hill.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(548.33, 30.53));
			hill.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(530.79, 26.79));
			
			HashMap<String, DataHolder> amsco = createOrGetList("AMSCO");
			amsco.put(StatisticsRef.IC_x1000, new DataHolder(66.00, 2.71));
			amsco.put(StatisticsRef.IC_MAX_x1000, new DataHolder(68.12, 3.15));
			amsco.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(80.85, 7.47));
			amsco.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(46.67, 4.14));
			amsco.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(46.68, 4.95));
			amsco.put(StatisticsRef.LONG_REPEAT, new DataHolder(12.3952488616742400000000000, 1.2195949387679033000000000));
			amsco.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.5107246376811600000000000, 1.7129179433636794000000000));
			amsco.put(StatisticsRef.NORMAL_ORDER, new DataHolder(72.5507246376811600000000000, 30.1082762139631600000000000));
			amsco.put(StatisticsRef.BIFID_MAX_0, new DataHolder(277.92, 31.40));
			amsco.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(307.4907450238189000000000000, 48.8208789511897700000000000));
			amsco.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(66.8789890086337500000000000, 8.6858174374260350000000000));
			amsco.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(930.3247217946657000000000000, 328.8778481774621000000000000));
			amsco.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(691.99, 7.79));
			amsco.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(579.08, 25.30));
			amsco.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(693.02, 7.01));
			amsco.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(565.05, 22.86));
			
			HashMap<String, DataHolder> bifid = createOrGetList("Bifid");
			bifid.put(StatisticsRef.IC_x1000, new DataHolder(45.71, 3.08));
			bifid.put(StatisticsRef.IC_MAX_x1000, new DataHolder(51.69, 4.42));
			bifid.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(60.60, 8.31));
			bifid.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(24.18, 4.71));
			bifid.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(27.41, 14.12));
			bifid.put(StatisticsRef.LONG_REPEAT, new DataHolder(8.47, 1.91));
			bifid.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(47.21, 7.22));
			bifid.put(StatisticsRef.NORMAL_ORDER, new DataHolder(192.41, 27.67));
			bifid.put(StatisticsRef.BIFID_MAX_0, new DataHolder(149.14, 69.68));
			bifid.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(334.40, 126.10));
			bifid.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(48.32, 4.04));
			bifid.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(819.11, 811.16));
			bifid.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(488.75, 29.66));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(538.89, 25.61));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(533.16, 33.17));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(465.85, 22.85));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(533.29, 33.19));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.92, 33.35));
			bifid.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(150.56, 206.81));
			
			HashMap<String, DataHolder> bifid0 = createOrGetList("Bifid P:0");
			bifid0.put(StatisticsRef.IC_x1000, new DataHolder(45.71, 3.06));
			bifid0.put(StatisticsRef.IC_MAX_x1000, new DataHolder(47.56, 3.54));
			bifid0.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(64.95, 8.69));
			bifid0.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(24.30, 3.34));
			bifid0.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(24.70, 3.98));
			bifid0.put(StatisticsRef.LONG_REPEAT, new DataHolder(8.6437355142257400000000000, 1.3075777375681608000000000));
			bifid0.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.1687681159420300000000000, 2.6366692188094520000000000));
			bifid0.put(StatisticsRef.NORMAL_ORDER, new DataHolder(195.6653623188405800000000000, 27.7081302398854880000000000));
			bifid0.put(StatisticsRef.BIFID_MAX_0, new DataHolder(375.0180671883381400000000000, 86.8684127654248400000000000));
			bifid0.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(158.1277213763352400000000000, 24.5793739964456200000000000));
			bifid0.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(47.3077039774720100000000000, 3.7183913405654010000000000));
			bifid0.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(507.1672628980072700000000000, 266.0493779362677000000000000));
			bifid0.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(485.04, 29.57));
			
			HashMap<String, DataHolder> porta = createOrGetList("Porta");
			porta.put(StatisticsRef.IC_x1000, new DataHolder(42.19, 3.78));
			porta.put(StatisticsRef.IC_MAX_x1000, new DataHolder(66.52, 3.20));
			porta.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(68.83, 9.68));
			porta.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(24.35, 7.94));
			porta.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(30.22, 15.37));
			porta.put(StatisticsRef.LONG_REPEAT, new DataHolder(10.47, 3.00));
			porta.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(39.65, 12.03));
			porta.put(StatisticsRef.NORMAL_ORDER, new DataHolder(226.59, 35.47));
			porta.put(StatisticsRef.BIFID_MAX_0, new DataHolder(118.52, 26.41));
			porta.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(170.61, 56.75));
			porta.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(45.02, 3.72));
			porta.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1180.48, 912.31));
			porta.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(424.07, 30.38));
			porta.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(574.33, 23.31));
			porta.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(594.77, 23.25));
			porta.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(759.31, 6.74));
			
			HashMap<String, DataHolder> enigma = createOrGetList("Enigma NOPLUGBOARD");
			enigma.put(StatisticsRef.IC_x1000, new DataHolder(38.50, 0.43));
			enigma.put(StatisticsRef.IC_MAX_x1000, new DataHolder(40.07, 1.69));
			enigma.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(49.87, 5.66));
			enigma.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(14.82, 0.89));
			enigma.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(14.85, 1.72));
			enigma.put(StatisticsRef.LONG_REPEAT, new DataHolder(5.15, 0.90));
			enigma.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.56, 3.09));
			enigma.put(StatisticsRef.NORMAL_ORDER, new DataHolder(248.33, 27.69));
			enigma.put(StatisticsRef.BIFID_MAX_0, new DataHolder(92.89, 10.65));
			enigma.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(105.43, 12.67));
			enigma.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(40.15, 1.95));
			enigma.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(234.66, 160.42));
			enigma.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(418.27, 12.27));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(518.66, 29.14));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(531.92, 33.05));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(464.62, 23.28));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(531.94, 32.90));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(531.81, 32.98));
			enigma.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(149.94, 205.56));
			
			HashMap<String, DataHolder> bazeries = createOrGetList("Bazeries");
			bazeries.put(StatisticsRef.IC_x1000, new DataHolder(66.21, 2.70));
			bazeries.put(StatisticsRef.IC_MAX_x1000, new DataHolder(68.47, 3.22));
			bazeries.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(78.50, 7.52));
			bazeries.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(63.89, 6.21));
			bazeries.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(64.93, 8.15));
			bazeries.put(StatisticsRef.LONG_REPEAT, new DataHolder(19.16, 2.07));
			bazeries.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.40, 1.70));
			bazeries.put(StatisticsRef.NORMAL_ORDER, new DataHolder(238.77, 20.61));
			bazeries.put(StatisticsRef.BIFID_MAX_0, new DataHolder(272.98, 29.53));
			bazeries.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(405.23, 44.16));
			bazeries.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(67.62, 2.96));
			bazeries.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(2133.32, 631.66));
			bazeries.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(485.48, 36.77));
			bazeries.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(579.48, 25.50));
			bazeries.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(578.51, 25.18));
			bazeries.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(555.82, 25.00));
			
			HashMap<String, DataHolder> trifid = createOrGetList("Trifid");
			trifid.put(StatisticsRef.IC_x1000, new DataHolder(39.94, 2.34));
			trifid.put(StatisticsRef.IC_MAX_x1000, new DataHolder(46.20, 4.32));
			trifid.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(54.23, 7.88));
			trifid.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(17.11, 2.63));
			trifid.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(17.60, 3.56));
			trifid.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.41, 1.77));
			trifid.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(48.72, 3.73));
			trifid.put(StatisticsRef.NORMAL_ORDER, new DataHolder(0.10, 4.28));
			trifid.put(StatisticsRef.BIFID_MAX_0, new DataHolder(0.04, 2.17));
			trifid.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(0.05, 3.05));
			trifid.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(0.01, 0.68));
			trifid.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1598.23, 1572.71));
			trifid.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(417.69, 25.51));
			trifid.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(0.00, 0.00));
			trifid.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(0.00, 0.00));
			trifid.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(0.00, 0.00));
			
			HashMap<String, DataHolder> fourSquare = createOrGetList("Four Square");
			fourSquare.put(StatisticsRef.IC_x1000, new DataHolder(46.28, 1.73));
			fourSquare.put(StatisticsRef.IC_MAX_x1000, new DataHolder(54.24, 2.45));
			fourSquare.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(61.97, 6.78));
			fourSquare.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(35.50, 2.92));
			fourSquare.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(77.06, 7.25));
			fourSquare.put(StatisticsRef.LONG_REPEAT, new DataHolder(12.36, 1.17));
			fourSquare.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(21.98, 3.76));
			fourSquare.put(StatisticsRef.NORMAL_ORDER, new DataHolder(231.64, 27.05));
			fourSquare.put(StatisticsRef.BIFID_MAX_0, new DataHolder(135.38, 14.64));
			fourSquare.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(223.96, 21.69));
			fourSquare.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(48.10, 2.46));
			fourSquare.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1082.92, 312.10));
			fourSquare.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(449.68, 28.87));
			fourSquare.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(558.64, 25.79));
			fourSquare.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(558.64, 25.91));
			fourSquare.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(543.91, 23.25));
			fourSquare.put(StatisticsRef.TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			
			HashMap<String, DataHolder> twoSquare = createOrGetList("Two Square");
			twoSquare.put(StatisticsRef.IC_x1000, new DataHolder(46.09, 2.29));
			twoSquare.put(StatisticsRef.IC_MAX_x1000, new DataHolder(52.91, 3.59));
			twoSquare.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(61.05, 7.58));
			twoSquare.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(35.67, 3.08));
			twoSquare.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(76.97, 7.25));
			twoSquare.put(StatisticsRef.LONG_REPEAT, new DataHolder(12.33, 1.17));
			twoSquare.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(22.94, 3.72));
			twoSquare.put(StatisticsRef.NORMAL_ORDER, new DataHolder(190.32, 32.72));
			twoSquare.put(StatisticsRef.BIFID_MAX_0, new DataHolder(134.74, 18.48));
			twoSquare.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(224.40, 23.36));
			twoSquare.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(47.74, 2.79));
			twoSquare.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(1091.40, 326.08));
			twoSquare.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(486.03, 30.99));
			twoSquare.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(557.72, 26.88));
			twoSquare.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(558.48, 27.38));
			twoSquare.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(541.36, 24.30));
			twoSquare.put(StatisticsRef.TEXT_LENGTH_MULTIPLE, new DataHolder(2));
			
			HashMap<String, DataHolder> portax = createOrGetList("Portax");
			portax.put(StatisticsRef.IC_x1000, new DataHolder(42.56, 1.28));
			portax.put(StatisticsRef.IC_MAX_x1000, new DataHolder(48.14, 3.73));
			portax.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(56.56, 9.21));
			portax.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(19.78, 2.95));
			portax.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(21.08, 3.66));
			portax.put(StatisticsRef.LONG_REPEAT, new DataHolder(7.06, 1.46));
			portax.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(46.95, 3.65));
			portax.put(StatisticsRef.NORMAL_ORDER, new DataHolder(220.65, 16.14));
			portax.put(StatisticsRef.BIFID_MAX_0, new DataHolder(114.37, 13.63));
			portax.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(166.64, 51.45));
			portax.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(44.96, 2.38));
			portax.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(490.75, 653.48));
			portax.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(437.86, 14.46));
			portax.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(544.10, 27.50));
			portax.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(544.55, 27.59));
			portax.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(523.71, 25.50));
			portax.put(StatisticsRef.LOG_DIAGRAPH_SLIDEFAIR, new DataHolder(542.32, 28.57));
			portax.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(651.59, 42.82));
			
			HashMap<String, DataHolder> vigenereAutokey = createOrGetList("Vigenere Autokey");
			vigenereAutokey.put(StatisticsRef.IC_x1000, new DataHolder(39.83, 0.72));
			vigenereAutokey.put(StatisticsRef.IC_MAX_x1000, new DataHolder(41.49, 1.95));
			vigenereAutokey.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(65.64, 8.77));
			vigenereAutokey.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(16.90, 1.67));
			vigenereAutokey.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(16.89, 2.28));
			vigenereAutokey.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.23, 1.24));
			vigenereAutokey.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.65, 2.83));
			vigenereAutokey.put(StatisticsRef.NORMAL_ORDER, new DataHolder(195.42, 22.27));
			vigenereAutokey.put(StatisticsRef.BIFID_MAX_0, new DataHolder(99.95, 10.71));
			vigenereAutokey.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(115.96, 16.05));
			vigenereAutokey.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(41.50, 2.25));
			vigenereAutokey.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(347.07, 297.28));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(443.37, 13.38));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_REVERSED, new DataHolder(154.42, 211.65));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(536.48, 32.42));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(537.40, 32.03));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_VARIANT, new DataHolder(537.22, 31.73));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(520.94, 28.25));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(533.24, 33.04));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(464.16, 23.12));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(533.34, 33.19));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(759.68, 6.68));
			vigenereAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(148.22, 203.25));
			
			HashMap<String, DataHolder> beaufortAutokey = createOrGetList("Beaufort Autokey");
			beaufortAutokey.put(StatisticsRef.IC_x1000, new DataHolder(39.79, 0.76));
			beaufortAutokey.put(StatisticsRef.IC_MAX_x1000, new DataHolder(41.38, 1.90));
			beaufortAutokey.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(51.57, 5.69));
			beaufortAutokey.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(16.72, 1.61));
			beaufortAutokey.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(16.70, 2.19));
			beaufortAutokey.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.09, 1.22));
			beaufortAutokey.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.73, 3.10));
			beaufortAutokey.put(StatisticsRef.NORMAL_ORDER, new DataHolder(206.71, 25.04));
			beaufortAutokey.put(StatisticsRef.BIFID_MAX_0, new DataHolder(98.74, 11.10));
			beaufortAutokey.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(115.28, 16.56));
			beaufortAutokey.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(41.49, 2.11));
			beaufortAutokey.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(338.71, 289.97));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(439.99, 12.54));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(519.96, 28.57));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(759.68, 6.70));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(464.34, 23.59));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(567.86, 24.67));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.45, 33.42));
			beaufortAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(155.88, 213.67));


			HashMap<String, DataHolder> portaAutokey = createOrGetList("Porta Autokey move to right");
			portaAutokey.put(StatisticsRef.IC_x1000, new DataHolder(39.32, 0.69));
			portaAutokey.put(StatisticsRef.IC_MAX_x1000, new DataHolder(40.98, 1.92));
			portaAutokey.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(50.74, 5.63));
			portaAutokey.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(16.28, 1.60));
			portaAutokey.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(16.30, 2.22));
			portaAutokey.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.02, 1.30));
			portaAutokey.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.57, 3.01));
			portaAutokey.put(StatisticsRef.NORMAL_ORDER, new DataHolder(224.66, 28.04));
			portaAutokey.put(StatisticsRef.BIFID_MAX_0, new DataHolder(96.43, 11.10));
			portaAutokey.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(113.80, 16.20));
			portaAutokey.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(40.85, 1.69));
			portaAutokey.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(338.97, 289.56));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(429.15, 14.56));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(523.10, 28.50));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(532.19, 32.92));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(467.17, 22.21));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(532.37, 33.16));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(532.25, 33.11));
			portaAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(147.77, 202.64));
			
			
			HashMap<String, DataHolder> variantAutokey = createOrGetList("Variant Autokey");
			variantAutokey.put(StatisticsRef.IC_x1000, new DataHolder(39.78, 0.77));
			variantAutokey.put(StatisticsRef.IC_MAX_x1000, new DataHolder(41.39, 1.88));
			variantAutokey.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(51.57, 5.77));
			variantAutokey.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(16.74, 1.62));
			variantAutokey.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(16.71, 2.18));
			variantAutokey.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.09, 1.23));
			variantAutokey.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(49.75, 3.02));
			variantAutokey.put(StatisticsRef.NORMAL_ORDER, new DataHolder(204.75, 24.62));
			variantAutokey.put(StatisticsRef.BIFID_MAX_0, new DataHolder(98.86, 11.57));
			variantAutokey.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(115.05, 16.44));
			variantAutokey.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(41.48, 2.09));
			variantAutokey.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(338.61, 287.27));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(440.75, 12.90));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(520.13, 28.65));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_BEAUFORT, new DataHolder(567.88, 25.07));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_PORTA, new DataHolder(463.56, 22.81));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VARIANT, new DataHolder(759.68, 6.69));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_AUTOKEY_VIGENERE, new DataHolder(533.70, 33.23));
			variantAutokey.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(156.43, 214.49));
			
			HashMap<String, DataHolder> vigenereSlidefair = createOrGetList("Vigenere Slidefair");
			vigenereSlidefair.put(StatisticsRef.IC_x1000, new DataHolder(40.24, 1.96));
			vigenereSlidefair.put(StatisticsRef.IC_MAX_x1000, new DataHolder(57.03, 7.28));
			vigenereSlidefair.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(56.91, 10.99));
			vigenereSlidefair.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(18.60, 3.15));
			vigenereSlidefair.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(25.44, 8.01));
			vigenereSlidefair.put(StatisticsRef.LONG_REPEAT, new DataHolder(6.73, 1.37));
			vigenereSlidefair.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(40.21, 6.22));
			vigenereSlidefair.put(StatisticsRef.NORMAL_ORDER, new DataHolder(239.96, 37.39));
			vigenereSlidefair.put(StatisticsRef.BIFID_MAX_0, new DataHolder(104.69, 16.41));
			vigenereSlidefair.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(138.08, 32.85));
			vigenereSlidefair.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(42.93, 2.63));
			vigenereSlidefair.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(473.19, 291.01));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(421.81, 24.95));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(561.08, 27.13));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(609.08, 44.72));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(544.63, 25.42));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH_SLIDEFAIR, new DataHolder(748.12, 7.88));
			vigenereSlidefair.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(432.88, 20.27));

			HashMap<String, DataHolder> beaufortSlidefair = createOrGetList("Beaufort Slidefair");
			beaufortSlidefair.put(StatisticsRef.IC_x1000, new DataHolder(41.90, 3.46));
			beaufortSlidefair.put(StatisticsRef.IC_MAX_x1000, new DataHolder(56.86, 7.39));
			beaufortSlidefair.put(StatisticsRef.IC_KAPPA_x1000, new DataHolder(56.98, 11.21));
			beaufortSlidefair.put(StatisticsRef.DIAGRAPHIC_IC_x10000, new DataHolder(19.59, 4.39));
			beaufortSlidefair.put(StatisticsRef.DIAGRAPHIC_EVEN_IC_x10000, new DataHolder(24.34, 8.20));
			beaufortSlidefair.put(StatisticsRef.LONG_REPEAT, new DataHolder(7.04, 1.58));
			beaufortSlidefair.put(StatisticsRef.LONG_REPEAT_ODD_PERCENTAGE, new DataHolder(44.29, 3.91));
			beaufortSlidefair.put(StatisticsRef.NORMAL_ORDER, new DataHolder(235.88, 29.91));
			beaufortSlidefair.put(StatisticsRef.BIFID_MAX_0, new DataHolder(113.57, 23.16));
			beaufortSlidefair.put(StatisticsRef.BIFID_MAX_3to15, new DataHolder(159.95, 56.30));
			beaufortSlidefair.put(StatisticsRef.NICODEMUS_MAX_3to15, new DataHolder(45.60, 3.83));
			beaufortSlidefair.put(StatisticsRef.TRIFID_MAX_3to15, new DataHolder(545.84, 350.39));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH, new DataHolder(419.43, 25.37));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH_BEAUFORT, new DataHolder(607.34, 45.32));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH_VIGENERE, new DataHolder(559.09, 27.18));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH_PORTA, new DataHolder(542.33, 25.24));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH_SLIDEFAIR, new DataHolder(747.08, 8.51));
			beaufortSlidefair.put(StatisticsRef.LOG_DIAGRAPH_PORTAX, new DataHolder(431.79, 23.68));
			
			/**
			normalEnglish.put(new StatisticRange(StatisticType.MAX_IOC, 73.0D, 11.0D));
			normalEnglish.put(new StatisticRange(StatisticType.MAX_KAPPA, 95.0D, 19.0D));
			normalEnglish.put(new StatisticRange(StatisticType.DIGRAPHIC_IOC, 72.0D, 18.0D));
			normalEnglish.put(new StatisticRange(StatisticType.EVEN_DIGRAPHIC_IOC, 73.0D, 24.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LONG_REPEAT_3, 22.0D, 5.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LONG_REPEAT_ODD, 50.0D, 6.0D));
			normalEnglish.put(new StatisticRange(StatisticType.LOG_DIGRAPH, 756.0D, 13.0D));
			normalEnglish.put(new StatisticRange(StatisticType.SINGLE_LETTER_DIGRAPH, 303.0D, 23.0D));**/
			
		}
		return map;
	}
	
	public static HashMap<String, DataHolder> createOrGetList(String key) {
		if(!map.containsKey(key))
			map.put(key, new HashMap<String, DataHolder>());
		return map.get(key);
	}
}
