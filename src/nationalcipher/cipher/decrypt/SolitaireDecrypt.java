package nationalcipher.cipher.decrypt;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.fitness.NGramData;
import javalibrary.fitness.TextFitness;
import javalibrary.lib.OSIdentifier;
import javalibrary.math.ArrayHelper;
import javalibrary.math.MathHelper;
import javalibrary.string.StringTransformer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.UINew;
import nationalcipher.cipher.Columnar;
import nationalcipher.cipher.ColumnarRow;
import nationalcipher.cipher.Solitaire;
import nationalcipher.cipher.Vigenere;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator.AMSCOKey;
import nationalcipher.cipher.tools.Creator.RedefenceKey;
import nationalcipher.cipher.tools.Creator.VigereneKey;
import nationalcipher.cipher.tools.InternalDecryption;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.KeyGeneration;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SimulatedAnnealing;
import nationalcipher.cipher.tools.SubOptionPanel;

public class SolitaireDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Solitaire";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		int charDecode = Math.min(text.length(), SettingParse.getInteger(this.charactersToDecode));
		if(charDecode < 0 || charDecode > text.length())
			charDecode = text.length();
		
		output.println("Suggested fitness looking for: " + TextFitness.getEstimatedFitness(charDecode, settings.getLanguage()));
		final SolitaireTask task = new SolitaireTask(ArrayHelper.copyOfRange(text.toCharArray(), 0, charDecode), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			String textOrder = this.passKeyIterateOrder.getText();
			String[] cards = textOrder.split(",");
			int[] order = new int[cards.length];
			Arrays.fill(order, -1);
			int countComplete = 0;
			List<Integer> emptyIndex = new ArrayList<Integer>();
			
			
			for(int i = 0; i < cards.length; i++) {
				String card = cards[i];
				if(card.equals("*") || card.equalsIgnoreCase("x")) {
					emptyIndex.add(i);
					countComplete++;
					continue;
				}
				
				try {
					int no = Integer.valueOf(card);
					order[i] = no;
					continue;
				}
				catch(NumberFormatException e) {}
	
				if(card.length() == 2) {
					List<Character> starter = Arrays.asList('A','2','3','4','5','6','7','8','9','T','J','Q','K');
					List<Character> end = Arrays.asList('♣', '♦', '♥', '♠', 'c', 'd', 'h', 's');
	
					if(starter.contains(card.charAt(0)) && end.contains(card.charAt(1))) {
						int no = starter.indexOf(card.charAt(0)) + end.indexOf(card.charAt(1)) % 4 * 13;
						order[i] = no;
					}
				}
				else {
					
				}

			}
			output.println("Deck Size: %d", order.length);
			output.println("Current known order: " + StringTransformer.joinWith(order, ","));
			output.println("No of unknowns (%d), permutations - %s", countComplete, MathHelper.factorialBig(countComplete));
			
			if(countComplete == 0) {
				output.print("Decrypting...\n%s", Solitaire.decode(text.toCharArray(), order));
			}
			else {
				task.incompleteOrder = order;
				task.emptyIndex = emptyIndex.toArray(new Integer[0]);
				List<Integer> all = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53));
				for(int o : order)
					all.remove((Integer)o);
			
				int[] unknowns = new int[all.size() - 1];
	
			
				for(int i = 1; i < all.size(); i++)
					unknowns[i] = all.get(i);
				
				output.println("Left: %s", Arrays.toString(unknowns));
		
				progress.addMaxValue(MathHelper.factorialBig(countComplete));
				

				for(int i = 0; i < all.size(); i++) {
					Thread thread = new Thread(new Runnable() {
						
						@Override
						public void run() {
							iterateAMSCO(task, all.get(i), unknowns);
						}
					});
						
					
					Creator.iterateAMSCO(task, unknowns);
				}


			}
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			int[] range = SettingParse.getIntegerRange(this.rangeBox);
			int minLength = range[0];
			int maxLength = range[1];
			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length)
				progress.addMaxValue(TWENTY_SIX.pow(length));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateVigerene(task, keyLength);
			
			output.println(task.getBestSolution());
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static void iterateAMSCO(AMSCOKey task, int start, int[] order) {
		iterateAMSCO(task, start, order, order.length, 0);
	}
	
	private static void iterateAMSCO(AMSCOKey task, int start, int[] arr, int length, int pos) {
		if(length - pos == 1)
			task.onIteration(ArrayHelper.concat(new int[] {start}, arr));
		else
		    for(int i = pos; i < length; i++) {
		        int h = arr[pos];
		        int j = arr[i];
		        arr[pos] = j;
		        arr[i] = h;
		            
		        iterateAMSCO(task, start, arr, length, pos + 1);
		        arr[pos] = h;
		    	arr[i] = j;
		    }
	}
	
	private JTextField rangeBox = new JTextField("2-5");
	private JTextField passKeyStartingOrder = new JTextField("0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
	private JTextField charactersToDecode = new JTextField("100");
	
	private JTextField passKeyIterateOrder = new JTextField("0,*,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53");
	private JComboBox<String> directionOption = new JComboBox<String>(new String[] {"Columns", "Rows"});
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		dialog.setMinimumSize(new Dimension(800, 200));
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		((AbstractDocument)this.charactersToDecode.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		((AbstractDocument)this.passKeyIterateOrder.getDocument()).setDocumentFilter(new DocumentUtil.DocumentCardInput());
		JLabel suitOrder = new JLabel("♣ ♦ ♥ ♠");
		suitOrder.setFont(suitOrder.getFont().deriveFont(20F));
		panel.add(new SubOptionPanel("Passkey length range:", this.rangeBox, 800));
		panel.add(new SubOptionPanel("Passkey starting order:", this.passKeyStartingOrder, 800));
		panel.add(new SubOptionPanel("Max character decode:", this.charactersToDecode, 800));
		panel.add(new SubOptionPanel("Known key:", this.passKeyIterateOrder, 800));
		panel.add(new SubOptionPanel("Suit order:", suitOrder, 300).add(new JLabel("Club | Diamond | Heart | Spade")));
		panel.add(new SubOptionPanel("Read off?", this.directionOption));
	}

	public class SolitaireTask extends InternalDecryption implements VigereneKey, AMSCOKey {

		public int[] bestKey1, bestMaximaKey1, lastKey1;
		public int[] incompleteOrder;
		public Integer[] emptyIndex;
		public NGramData quadgramData;
		
		public SolitaireTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.quadgramData = settings.getLanguage().getQuadgramData();
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Solitaire.decode(this.text, Solitaire.createCardOrder(key)), this.settings.getLanguage()).setKeyString(key);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}

		@Override
		public void onIteration(int[] order) {
			for(int i = 0; i < this.emptyIndex.length; i++) {
				this.incompleteOrder[this.emptyIndex[i]] = order[i];
			}
			
			//this.output.println(Arrays.toString(order));
			this.lastSolution = new Solution(decode(this.text, this.incompleteOrder, this.bestSolution.score, quadgramData), this.score);
			
			if(this.lastSolution.score > this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(Arrays.toString(this.incompleteOrder));
				this.keyPanel.updateSolution(this.bestSolution);
				this.output.println("%s", this.bestSolution);
			}
			
			//this.keyPanel.iterations.setText("" + this.iteration++);
			//this.progress.increase();
		}

		public double score = 0.0D;
		
		public char[] decode(char[] cipherText, int[] cardOrder, double bestScore, NGramData quadgramData) {
			score = 0;
			char[] plainText = new char[cipherText.length];
			
			for(int i = 0; i < cipherText.length; i++)  {

				cardOrder = Solitaire.nextKeyStream(cardOrder);
				plainText[i] = (char)((52 + (cipherText[i] - 'A') - (Solitaire.keyStreamNumber + 1)) % 26 + 'A');
				
				if(i > 2) {
					score += TextFitness.scoreWord(new String(plainText, i - 3, 4), quadgramData);
					if(score < bestScore)
						break;
				}
			}


			return plainText;
		}
	}
	
	
	
	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
