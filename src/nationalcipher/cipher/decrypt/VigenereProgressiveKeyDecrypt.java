package nationalcipher.cipher.decrypt;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import nationalcipher.KeyPanel;
import nationalcipher.Settings;
import nationalcipher.cipher.VigenereAutokey;
import nationalcipher.cipher.VigenereProgressiveKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.KeySearch;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.tools.Creator.VigenereAutoKey;

public class VigenereProgressiveKeyDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Vigenere ProgressiveKey";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.KEY_MANIPULATION);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		VigenereProgressiveKeyTask task = new VigenereProgressiveKeyTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minLength = range[0];
		int maxLength = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int length = minLength; length <= maxLength; ++length)
				progress.addMaxValue(TWENTY_SIX.pow(length));
			
			for(int keyLength = minLength; keyLength <= maxLength; ++keyLength)
				Creator.iterateVigenereAutoKey(task, keyLength);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.KEY_MANIPULATION) {
			int[] range2 = SettingParse.getIntegerRange(this.rangeBox2);
			int[] range3 = SettingParse.getIntegerRange(this.rangeBox3);
			progress.setIndeterminate(true);
			for(int i = range2[0]; i < range2[1]; i++) {
				for(int j = range3[0]; j < range3[1]; j++) {
					task.progPeriod = i;
					task.progIndex = j;

					task.run(minLength, maxLength);
				}
			}
			
			output.println(task.getBestSolution());
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}
	}

	private JTextField rangeBox = new JTextField("2-15");
	private JTextField rangeBox2 = new JTextField("1-30");
	private JTextField rangeBox3 = new JTextField("1-25");
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
        JLabel range = new JLabel("Period Range:");
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		
		JLabel range2 = new JLabel("Prog Period:");
		((AbstractDocument)this.rangeBox2.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox2));
		
		JLabel range3 = new JLabel("Prog Index:");
		((AbstractDocument)this.rangeBox3.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox3));
		
		panel.add(new SubOptionPanel(range, this.rangeBox));
		panel.add(new SubOptionPanel(range2, this.rangeBox2));
		panel.add(new SubOptionPanel(range3, this.rangeBox3));
		
		dialog.add(panel);
	}
	
	public class VigenereProgressiveKeyTask extends KeySearch implements VigenereAutoKey {
		
		public int progPeriod;
		public int progIndex;
		
		public VigenereProgressiveKeyTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
			this.progPeriod = SettingParse.getInteger(rangeBox2);
			this.progIndex = SettingParse.getInteger(rangeBox3);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(VigenereProgressiveKey.decode(this.text, key, this.progPeriod, this.progIndex), this.settings.getLanguage()).setKeyString(key).setKeyString("%s, PP-%d, PI-%d", key, this.progPeriod, this.progIndex);;
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.iterations.setText("" + this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution tryModifiedKey(String key) {
			return new Solution(VigenereProgressiveKey.decode(this.text, key, this.progPeriod, this.progIndex), this.settings.getLanguage()).setKeyString("%s, PP-%d, PI-%d", key, this.progPeriod, this.progIndex);
		}

		@Override
		public void solutionFound() {
			this.output.println("%s", this.bestSolution);
			this.keyPanel.updateSolution(this.bestSolution);
		}

		@Override
		public void onIteration() {
			this.keyPanel.iterations.setText("" + this.iteration++);
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}