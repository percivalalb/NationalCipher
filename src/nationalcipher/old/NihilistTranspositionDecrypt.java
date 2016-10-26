package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.math.MathUtil;
import javalibrary.string.StringTransformer;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.transposition.NihilistTransposition;
import nationalcipher.cipher.decrypt.methods.InternalDecryption;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.NihilistTranspositionKey;
import nationalcipher.ui.KeyPanel;

public class NihilistTranspositionDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Nihilist Transposition";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		NihilistTranspositionTask task = new NihilistTranspositionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			
			List<Integer> factors = MathUtil.getSquareFactors(text.length());
			factors.remove((Integer)1);
			output.println("" + factors);
			
			for(int keyLength = 4; keyLength <= 4; ++keyLength)
				progress.addMaxValue(MathUtil.factorialBig(keyLength));
			
			if(!factors.isEmpty()) {
				
				for(int factor : factors) {
					Creator.iterateNihilistTransposition(task, (int)Math.sqrt(factor), factor);
				}
	
				
				output.println(task.getBestSolution());
			}
			else
				output.println("NOT SQUARE FACTORS");
			
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	public class NihilistTranspositionTask extends InternalDecryption implements NihilistTranspositionKey {
		
		public NihilistTranspositionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(int[] order, int blocks) {
			String str = new String(this.cipherText);
			String plainText = "";
			for(int i = 0; i < this.cipherText.length / blocks; i++) {
				plainText += new String(NihilistTransposition.decode(str.substring(i * blocks, (i + 1) * blocks).toCharArray(), order));
			}
			
			String finalStr = "";
			for(int i = 0; i < order.length; i++)
				finalStr += StringTransformer.getEveryNthChar(plainText, i, order.length);
			
			Solution otherSolution = new Solution(finalStr.toCharArray(), this.settings.getLanguage()).setKeyString(Arrays.toString(order) + " Read columns");
			
			this.lastSolution = new Solution(plainText.toCharArray(), this.settings.getLanguage()).setKeyString(Arrays.toString(order) + " Read rows");;
			
			if(otherSolution.score >= this.lastSolution.score)
				this.lastSolution = otherSolution;
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}