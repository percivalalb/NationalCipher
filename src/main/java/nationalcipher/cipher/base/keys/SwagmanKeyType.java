package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import javalibrary.util.ListUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;
import nationalcipher.cipher.base.KeyFunction;
import nationalcipher.cipher.tools.KeyGeneration;

public class SwagmanKeyType implements IKeyType<int[]> {

    // Both inclusive
    private final int min, max;

    private SwagmanKeyType(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int[] randomise() {
        return KeyGeneration.createSwagmanKey(RandomUtil.pickRandomInt(this.min, this.max));
    }

    @Override
    public boolean isValid(int[] key) {
        double sizeD = Math.sqrt(key.length);
        // Is square
        if (sizeD != Math.floor(sizeD)) {
            return false;
        }

        int size = (int) sizeD;

        for (int row = 0; row < size; row++) {
            List<Integer> test = ListUtil.randomRange(0, size - 1);
            for (int col = 0; col < size; col++) {
                if (!test.remove((Integer) key[row * size + col])) {
                    return false;
                }
            }
        }

        for (int col = 0; col < size; col++) {
            List<Integer> test = ListUtil.randomRange(0, size - 1);
            for (int row = 0; row < size; row++) {
                if (!test.remove((Integer) key[row * size + col])) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean iterateKeys(KeyFunction<int[]> consumer) {
        for (int length = this.min; length <= this.max; length++) {
            // TODO
        }
        
        return true;
    }

    @Override
    public int[] alterKey(int[] key) {
        return key;
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.ZERO;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<int[]> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();

        private Builder() {
        }

        @Override
        public Builder setMin(int min) {
            this.min = Optional.of(min);
            return this;
        }

        @Override
        public Builder setMax(int max) {
            this.max = Optional.of(max);
            return this;
        }
        
        @Override
        public Builder setRange(int min, int max) {
            return this.setMin(min).setMax(max);
        }

        @Override
        public Builder setSize(int size) {
            return this.setRange(size, size);
        }

        @Override
        public SwagmanKeyType create() {
            SwagmanKeyType handler = new SwagmanKeyType(this.min.orElse(3), this.max.orElse(5));
            return handler;
        }

    }
}
