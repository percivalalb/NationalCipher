package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.dict.Dictionary;
import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.other.Bifid;
import nationalcipher.cipher.decrypt.complete.methods.DictionaryAttack;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.Long25Key;
import nationalcipher.cipher.decrypt.complete.methods.SimulatedAnnealing;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.KeySquareManipulation;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.UINew;

public class BifidAttack extends CipherAttack {

	public JSpinner spinner;
	
	public BifidAttack() {
		super("Bifid");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.DICTIONARY);
		this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] {0}, ArrayUtil.rangeInt(2, 101)));
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		panel.add(new SubOptionPanel("Period:", this.spinner));
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		BifidTask task = new BifidTask(text, app);
		
		//Settings grab
		task.period = SettingParse.getInteger(this.spinner);
		
		if(method == DecryptionMethod.DICTIONARY) {
			app.getProgress().addMaxValue(Dictionary.wordCount());
			for(String word : Dictionary.words)
				task.onIteration(DictionaryAttack.createLong26Key(word, app.getSettings().getKeywordFiller(), 'J'));
		}
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(26);
			//KeyIterator.iterateLong26Key(task);
		}
		else if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class BifidTask extends SimulatedAnnealing implements Long25Key {

		public int period;
		public String bestKey, bestMaximaKey, lastKey;
		
		public BifidTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(String key) {
			this.lastSolution = new Solution(Bifid.decode(this.cipherText, key, this.period), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%s, p:%d", key, this.period);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey = KeySquareManipulation.generateRandKeySquare();
			return new Solution(Bifid.decode(this.cipherText, this.bestMaximaKey, this.period), this.getLanguage());
		}

		@Override
		public Solution modifyKey(int count) {
			this.lastKey = KeySquareManipulation.modifyKey(this.bestMaximaKey);
			return new Solution(Bifid.decode(this.cipherText, this.lastKey, this.period), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey = this.lastKey;
		}

		@Override
		public void solutionFound() {
			this.bestKey = this.bestMaximaKey;
			this.bestSolution.setKeyString(this.bestKey);
			this.getKeyPanel().updateSolution(this.bestSolution);
		}
		
		@Override
		public void onIteration() {
			this.getProgress().increase();
			this.getKeyPanel().updateIteration(this.iteration++);
		}

		@Override
		public boolean endIteration() {
			this.out().println("%s", this.bestSolution);
			UINew.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}