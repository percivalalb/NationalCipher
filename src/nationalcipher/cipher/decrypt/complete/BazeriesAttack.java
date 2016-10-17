package nationalcipher.cipher.decrypt.complete;

import nationalcipher.cipher.Bazeries;
import nationalcipher.cipher.decrypt.complete.methods.InternalDecryption;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator;
import nationalcipher.cipher.decrypt.complete.methods.KeyIterator.IntegerKey;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.ui.IApplication;

public class BazeriesAttack extends CipherAttack {

	public BazeriesAttack() {
		super("Bazeries");
		this.setAttackMethods(DecryptionMethod.BRUTE_FORCE);
	}
	
	@Override
	public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
		BazeriesTask task = new BazeriesTask(text, app);
		
		if(method == DecryptionMethod.BRUTE_FORCE) {
			app.getProgress().addMaxValue(1000000);
			KeyIterator.iterateIntegerKey(task, 1, 1000000, 1);
		}
		
		app.out().println(task.getBestSolution());
	}
	
	public static class BazeriesTask extends InternalDecryption implements IntegerKey {

		public BazeriesTask(String text, IApplication app) {
			super(text.toCharArray(), app);
		}

		@Override
		public void onIteration(int no) {
			this.lastSolution = new Solution(Bazeries.decode(this.cipherText, no), this.getLanguage());
			
			if(this.lastSolution.score >= this.bestSolution.score) {
				this.bestSolution = this.lastSolution;
				this.bestSolution.setKeyString("%d", no);
				this.out().println("%s", this.bestSolution);	
				this.getKeyPanel().updateSolution(this.bestSolution);
			}
			
			this.getKeyPanel().updateIteration(this.iteration++);
			this.getProgress().increase();
		}
	}
}