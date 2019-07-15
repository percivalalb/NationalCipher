package nationalcipher.api;

import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.Solution;

public interface IAttackMethod<K> {

    ICipher<K> getCipher();

    /**
     * Decrypts the cipher using the given key checks if it is better than the
     * the best solution and if so updates it.
     * @param tracker The results tracker
     * @param key The key to decrypt using
     */
    default void decryptAndUpdate(DecryptionTracker tracker, K key) {
        Solution solution = this.toSolution(tracker, key);

        synchronized (tracker) {
            if (this.isBetterThanBest(tracker, solution)) {
                this.updateBestSolution(tracker, solution, key);
            }
        }
        
        tracker.lastSolution = solution;
    }
    
    default Solution toSolution(DecryptionTracker tracker, K key) {
        return new Solution(this.getCipher().decodeEfficently(tracker.getCipherText(), tracker.getPlainTextHolder(), key), tracker.getLanguage());
    }

    default boolean isBetterThanBest(DecryptionTracker tracker, Solution solution) {
        return solution.isBetterThan(tracker.bestSolution);
    }

    /**
     * Updates the best solution. Converts the key to a readable form, copies the
     * plaintext array, prints out the solution and updates the solution panel UI.
     * 
     * @param tracker  The
     *                 {@link nationalcipher.cipher.decrypt.methods.DecryptionTracker}
     *                 instances that tracks best solutions
     * @param solution The solution to become the best solution
     * @param key      The key to generate this solution
     */
    default void updateBestSolution(DecryptionTracker tracker, Solution solution, K key) {
        tracker.bestSolution = solution;
        tracker.bestSolution.setKeyString(this.getCipher().prettifyKey(key));
        tracker.bestSolution.bakeSolution();
        tracker.addSolution(tracker.bestSolution);
        tracker.out().println(tracker.bestSolution.toString());
        tracker.getKeyPanel().updateSolution(tracker.bestSolution);
    }
}
