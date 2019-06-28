package nationalcipher.cipher.decrypt.complete;

import javax.swing.JDialog;
import javax.swing.JPanel;

import nationalcipher.cipher.base.other.Hutton;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.SimulatedAnnealing;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.tools.KeyManipulation;
import nationalcipher.ui.IApplication;
import nationalcipher.ui.NationalCipherUI;

public class HuttonAttack extends CipherAttack {

	
	public HuttonAttack() {
		super("Hutton");
		this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING, DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		HuttonTask task = new HuttonTask(text, app);
		
		//Settings grab
		
		if(method == DecryptionMethod.SIMULATED_ANNEALING) {
			app.getProgress().addMaxValue(app.getSettings().getSAIteration());
			task.run();
		}
		
		else if(method == DecryptionMethod.BRUTE_FORCE) {
			for(int i = 3; i <= 3; i++)
				for(int j = 3; j <= 3; j++)
					KeyIterator.iterateHutton(task::onIteration, i, j);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public class HuttonTask extends SimulatedAnnealing {

		public int period;
		public String bestKey1, bestMaximaKey1, lastKey1;
		public String bestKey2, bestMaximaKey2, lastKey2;
		
		public HuttonTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}
		
		public void onIteration(String key1, String key2) {
			this.lastSolution = new Solution(Hutton.decode(this.cipherText, key1, key2), this.getLanguage());
			if(key1.equals("WE")) {
				this.lastSolution.setKeyString(key1 + " " + key2);
				this.out().println("LAST %s", this.lastSolution);	
			}
			this.addSolution(this.lastSolution);
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString(key1 + " " + key2);
				this.bestSolution.bakeSolution();
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
		
		@Override
		public Solution generateKey() {
			this.bestMaximaKey1 = "FADODA";
			this.bestMaximaKey2 = "JUPITER";
			this.lastKey1 = this.bestMaximaKey1;
			this.lastKey2 = this.bestMaximaKey2;
			return new Solution(Hutton.decode(this.cipherText, this.bestMaximaKey1, this.bestMaximaKey2), this.getLanguage());
		}

		@Override
		public Solution modifyKey(double temp, int count, double lastDF) {
			if(count % 2 == 0)
				this.lastKey1 = KeyManipulation.modifyHuttonKey1(this.bestMaximaKey1);
			else
				this.lastKey2 = KeyManipulation.modifyHuttonKey2(this.bestMaximaKey2);
			return new Solution(Hutton.decode(this.cipherText, this.bestMaximaKey1, this.bestMaximaKey2), this.getLanguage());
		}

		@Override
		public void storeKey() {
			this.bestMaximaKey1 = this.lastKey1;
			this.bestMaximaKey2 = this.lastKey2;
		}

		@Override
		public void solutionFound() {
			this.bestKey1 = this.bestMaximaKey1;
			this.bestKey2 = this.bestMaximaKey2;
			this.bestSolution.setKeyString("%s %s, p:%d", this.bestKey1, this.bestKey2, this.period);
			this.bestSolution.bakeSolution();
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
			NationalCipherUI.BEST_SOULTION = this.bestSolution.getText();
			this.getProgress().setValue(0);
			return false;
		}
	}
}
