package nationalcipher.cipher.decrypt;

import nationalcipher.api.ICipher;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.ui.IApplication;

/**
 * Default implementation of the simulated annealing attack
 * 
 * @author Alex Barter
 *
 * @param <K> The key type
 */
public class SACipherAttack<K, C extends ICipher<K>> extends CipherAttack<K, C> implements ISimulatedAnnealingAttack<K> {

    public SACipherAttack(C cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING);
    }

    @Override
    public void attemptAttack(String text, DecryptionMethod method, IApplication app) {
        switch (method) {
        case SIMULATED_ANNEALING:
            this.trySimulatedAnnealing(new DecryptionTracker(text, app), app.getProgress());
            break;
        default:
            super.attemptAttack(text, method, app);
            break;
        }
    }
}
