package nationalcipher.cipher.decrypt.anew;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import nationalcipher.cipher.base.anew.ProgressiveCipher;
import nationalcipher.cipher.base.keys.TriKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.IKeySearchAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;
import nationalcipher.cipher.util.CipherUtils;
import nationalcipher.ui.IApplication;

public class ProgressiveKeyAttack extends CipherAttack<TriKey<String, Integer, Integer>, ProgressiveCipher> implements IKeySearchAttack<TriKey<String, Integer, Integer>> {

    public JSpinner[] rangeSpinner1;
    public JSpinner[] rangeSpinner2;
    public JSpinner[] rangeSpinner3;

    public ProgressiveKeyAttack(ProgressiveCipher cipher, String displayName) {
        super(cipher, displayName);
        this.setAttackMethods(DecryptionMethod.PERIODIC_KEY);
        this.rangeSpinner1 = JSpinnerUtil.createRangeSpinners(2, 15, 2, 100, 1);
        this.rangeSpinner2 = JSpinnerUtil.createRangeSpinners(1, 30, 1, 100, 1);
        this.rangeSpinner3 = JSpinnerUtil.createRangeSpinners(1, 25, 1, 100, 1);
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period Range:", this.rangeSpinner1));
        panel.add(new SubOptionPanel("Prog Period:", this.rangeSpinner2));
        panel.add(new SubOptionPanel("Prog Key:", this.rangeSpinner3));
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        int[] periodRange = SettingParse.getIntegerRange(this.rangeSpinner1);
        int[] progPeriodRange = SettingParse.getIntegerRange(this.rangeSpinner2);
        int[] progKeyRange = SettingParse.getIntegerRange(this.rangeSpinner3);
        this.getCipher().setFirstKeyDomain(builder -> builder.setRange(periodRange));
        this.getCipher().setSecondKeyDomain(builder -> builder.setRange(progPeriodRange));
        this.getCipher().setThirdKeyDomain(builder -> builder.setRange(progKeyRange));
        
        switch (method) {
        case PERIODIC_KEY:
            // Settings grab
            app.getProgress().setIndeterminate(true);
            DecryptionTracker tracker = new DecryptionTracker(text, app);
            for (int i = progPeriodRange[0]; i <= progPeriodRange[1]; i++) {
                for (int j = progKeyRange[0]; j <= progKeyRange[1]; j++) {
                    this.periodicKey.setSecond(i);
                    this.periodicKey.setThird(j);

                    this.tryKeySearch(tracker, periodRange[0], periodRange[1]);
                }
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }

    private TriKey<String, Integer, Integer> periodicKey = TriKey.empty();

    @Override
    public TriKey<String, Integer, Integer> useStringGetKey(DecryptionTracker tracker, String periodicPart) {
        return CipherUtils.optionalParallel(this.periodicKey::clone, ()->this.periodicKey, tracker).setFirst(periodicPart);
    }
}