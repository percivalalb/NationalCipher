package nationalcipher.cipher.decrypt.anew;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import javalibrary.swing.JSpinnerUtil;
import javalibrary.util.ArrayUtil;
import nationalcipher.cipher.base.anew.BifidCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class BifidAttack extends CipherAttack<BiKey<String, Integer>, BifidCipher> {

    public JSpinner spinner;

    public BifidAttack() {
        super(new BifidCipher(), "Bifid");
        this.setAttackMethods(DecryptionMethod.SIMULATED_ANNEALING); //TODO dictonary attack
        this.spinner = JSpinnerUtil.createSpinner(ArrayUtil.concat(new Integer[] { 0 }, ArrayUtil.createRangeInteger(2, 101)));
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        panel.add(new SubOptionPanel("Period:", this.spinner));
    }

    @Override
    public BiKey<String, Integer> generateIntialKey(DecryptionTracker tracker) {
        return this.getCipher().randomiseKey().setSecond(SettingParse.getInteger(this.spinner));
    }
}
