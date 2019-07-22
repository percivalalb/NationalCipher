package nationalcipher.cipher.base.keys;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javalibrary.util.RandomUtil;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IRangedKeyBuilder;

public class IntegerGenKeyType implements IKeyType<Integer> {

    // Both inclusive
    private final List<Integer> universe;
    private boolean alterable;

    private IntegerGenKeyType(List<Integer> universe, boolean alterable) {
        this.universe = universe;
        this.alterable = alterable;
    }

    @Override
    public Integer randomise(Object partialKey) {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public boolean isValid(Object partialKey, Integer key) {
        return this.universe.contains(key);
    }

    @Override
    public void iterateKeys(Object partialKey, Consumer<Integer> consumer) {
        this.universe.forEach(consumer);
    }

    @Override
    public Integer alterKey(Object partialKey, Integer key) {
        return RandomUtil.pickRandomElement(this.universe);
    }

    @Override
    public BigInteger getNumOfKeys() {
        return BigInteger.valueOf(this.universe.size());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements IRangedKeyBuilder<Integer> {

        private Optional<Integer> min = Optional.empty();
        private Optional<Integer> max = Optional.empty();
        private Predicate<Integer> filter = null;
        private boolean alterable = false;

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

        public Builder addFilter(Predicate<Integer> filterIn) {
            this.filter = this.filter == null ? filterIn : this.filter.and(filterIn);
            return this;
        }

        public Builder setAlterable() {
            this.alterable = true;
            return this;
        }

        @Override
        public IntegerGenKeyType create() {
            List<Integer> universe = new ArrayList<Integer>();
            int min = this.min.orElse(Integer.MIN_VALUE);
            int max = this.max.orElse(Integer.MAX_VALUE);
            for (int i = min; i <= max; i++) {
                if (this.filter == null || this.filter.test(i)) {
                    universe.add(i);
                }
            }

            IntegerGenKeyType handler = new IntegerGenKeyType(universe, this.alterable);
            return handler;
        }

    }
}