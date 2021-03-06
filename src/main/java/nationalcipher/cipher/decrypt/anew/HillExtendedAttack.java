package nationalcipher.cipher.decrypt.anew;

import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JPanel;

import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.fitness.ChiSquared;
import javalibrary.list.DynamicResultList;
import javalibrary.math.MathUtil;
import javalibrary.math.matrics.Matrix;
import nationalcipher.cipher.base.anew.HillExtendedCipher;
import nationalcipher.cipher.base.keys.BiKey;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.anew.HillAttack.HillSection;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.DecryptionTracker;
import nationalcipher.cipher.decrypt.methods.KeyIterator;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.setting.SettingTypes;
import nationalcipher.ui.IApplication;

public class HillExtendedAttack extends CipherAttack<BiKey<Matrix, Matrix>, HillExtendedCipher> {

    private int[] sizeRange = new int[] { 2, 4 };
    
    public HillExtendedAttack() {
        super(new HillExtendedCipher(), "Hill Extended");
        this.setAttackMethods(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.PERIODIC_KEY);
        this.addSetting(SettingTypes.createIntRange("size_range", 2, 4, 2, 100, 1, (values, cipher) -> {HillExtendedAttack.this.sizeRange = values;}));
    }

    @Override
    public void createSettingsUI(JDialog dialog, JPanel panel) {
        super.createSettingsUI(dialog, panel);
    }

    @Override
    public DecryptionTracker attemptAttack(CharSequence text, DecryptionMethod method, IApplication app) {
        HillTracker tracker = new HillTracker(text, app);

        switch (method) {
        case PERIODIC_KEY:
            for (int size = this.sizeRange[0]; size <= this.sizeRange[1]; size++) {
                tracker.resultList.clear();
                if (tracker.getCipherText().length() % size != 0) {
                    this.output(tracker, "Matrix size of %d is not possible, length of text is not a multiple.", size);
                    continue;
                }
                tracker.size = size;
                tracker.lengthSub = tracker.getCipherText().length() / size;

                KeyIterator.iterateIntegerArray(row -> this.iterateMatrixRows(tracker, row), size + 1, 26, true);
                tracker.resultList.sort();
                
                if (tracker.resultList.size() < size) {
                    this.output(tracker, "Did not find enought key columns that produces good characters %d/%d", tracker.resultList.size(), size);
                } else {
                    KeyIterator.iterateObject(row -> this.iteratePossibleRows(tracker, row), size, tracker.resultList.toArray(new HillSection[0]));
                }
            }
            return tracker;
        default:
            return super.attemptAttack(text, method, app);
        }
    }
    
    @Override
    public void decryptAndUpdate(DecryptionTracker tracker, BiKey<Matrix, Matrix> key) {
        try {
            super.decryptAndUpdate(tracker, key);
        } catch (MatrixNoInverse | MatrixNotSquareException e) {
            return;
        }
    }

    public boolean iterateMatrixRows(HillTracker tracker, Integer[] row) {
        if (MathUtil.allDivisibleBy(row, 0, tracker.size, 2, 13)) {
            return true;
        }

        char[] decrypted = new char[tracker.lengthSub];

        for (int i = 0; i < tracker.getCipherText().length(); i += tracker.size) {
            int total = 0;
            for (int s = 0; s < tracker.size; s++) {
                total += row[s] * (tracker.getCipherText().charAt(i + s) - 'A');
            }

            decrypted[i / tracker.size] = (char) ((total + (26 - row[tracker.size])) % 26 + 'A');
        }

        double score = ChiSquared.calculate(decrypted, tracker.getLanguage());

        if (tracker.resultList.add(new HillSection(score, decrypted, Arrays.copyOf(row, row.length)))) {
            if (score < 80D) {
                this.output(tracker, "%s, %f, %s", Arrays.toString(row), score, new String(decrypted));
            }
        }
        return true;
    }
    
    public boolean iteratePossibleRows(HillTracker tracker, HillSection[] data) {
        // Create key matrices from data
        int[] inverseData = new int[tracker.size * tracker.size];
        int[] extendedData = new int[tracker.size];
        for (int s = 0; s < tracker.size; s++) {
            HillSection hillResult = data[s];
            for (int n = 0; n < tracker.size; n++) {
                inverseData[s * tracker.size + n] = hillResult.inverseCol[n];
            }
            extendedData[s] = hillResult.inverseCol[tracker.size];
        }
        
        Matrix inverseMatrix = new Matrix(inverseData, tracker.size);
        
        if (!inverseMatrix.hasInverseMod(26)) {
            return true;
        }
        
        for (int s = 0; s < tracker.size; s++) {
            HillSection hillResult = data[s];
            for (int i = 0; i < tracker.lengthSub; i++) {
                tracker.getPlainTextHolder(false)[i * tracker.size + s] = hillResult.decrypted[i];
            }
        }

        tracker.lastSolution = new Solution(tracker.getPlainTextHolder(false), tracker.getLanguage());

        this.updateIfBetterThanBest(tracker, tracker.lastSolution, () -> BiKey.of(inverseMatrix.inverseMod(26), new Matrix(extendedData, tracker.size, 1)));
        return true;
    }

    public class HillTracker extends DecryptionTracker {

        private int size;
        private int lengthSub;
        private DynamicResultList<HillSection> resultList;

        public HillTracker(CharSequence text, IApplication app) {
            super(text, app);
            this.resultList = new DynamicResultList<HillSection>(8);
        }
    }
}
