package nationalcipher.old;

import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.Output;
import javalibrary.dict.Dictionary;
import javalibrary.swing.ProgressValue;
import nationalcipher.Settings;
import nationalcipher.cipher.base.substitution.Keyword;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.SubstitutionKey;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;

public class SubstitutionDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Substitution";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.CALCULATED, DecryptionMethod.DICTIONARY);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		SubstitutionTask task = new SubstitutionTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
	
			Creator.iterateSubstitution(task);
			
			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			progress.addMaxValue((int)(settings.getSATempStart() / settings.getSATempStep()) * settings.getSACount());
			
			task.run();
		}
		else if(method == DecryptionMethod.CALCULATED) {
			
		}
		else if(method == DecryptionMethod.DICTIONARY) {
			progress.addMaxValue(Dictionary.words.size());
			for(String word : Dictionary.words) {
				String change = "";
				for(char i : word.toCharArray()) {
					if(!change.contains("" + i))
						change += i;
				}
				String regex = new String[]{"ABCDEFGHIJKLMNOPQRSTUVWXYZ", "NOPQRSTUVWXYZABCDEFGHIJKLM", "ZYXWVUTSRQPONMLKJIHGFEDCBA"}[settings.getKeywordCreationId()];
				
				for(char i : regex.toCharArray()) {
					if(!change.contains("" + i))
						change += i;
				}
				
				//output.println(word + " " + change);
				task.onIteration(change);
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}

	public static class SubstitutionTask extends SimulatedAnnealing implements SubstitutionKey {

		public String bestKey = "", bestMaximaKey = "", lastKey = "";
		
		public SubstitutionTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(String keyalpha) {
			this.lastSolution = new Solution(Keyword.decode(this.cipherText, keyalpha), this.settings.getLanguage()).setKeyString(keyalpha);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.output.println("%s", this.bestSolution);	
				this.keyPanel.updateSolution(this.bestSolution);
			}
			
			this.keyPanel.updateIteration(this.iteration++);
			this.progress.increase();
		}

		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKey();
			return new Solution(Keyword.decode(this.cipherText, this.bestMaximaKey), this.settings.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.exchange2letters(this.bestMaximaKey);
			return new Solution(Keyword.decode(this.cipherText, this.lastKey), this.settings.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.keyPanel.fitness.setText("" + this.bestSolution.score);
			this.keyPanel.key.setText(this.bestKey);
		}
		
		@Override
		public void onIteration() {
			this.progress.increase();
			this.keyPanel.updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.output.println("Best Fitness: %f, Key: %s, Plaintext: %s", this.bestSolution.score, this.bestKey, new String(this.bestSolution.getText()));
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.progress.setValue(0);
			return false;
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}