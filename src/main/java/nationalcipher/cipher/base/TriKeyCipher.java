package nationalcipher.cipher.base;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.function.Function;

import javax.annotation.Nullable;

import nationalcipher.api.ICipher;
import nationalcipher.api.IKeyType;
import nationalcipher.api.IKeyType.IKeyBuilder;
import nationalcipher.cipher.base.keys.TriKey;

public abstract class TriKeyCipher<F, S, T, A extends IKeyBuilder<F>, B extends IKeyBuilder<S>, C extends IKeyBuilder<T>> implements ICipher<TriKey<F, S, T>> {

    private final IKeyType<F> firstType;
    private final IKeyType<S> secondType;
    private final IKeyType<T> thirdType;
    private IKeyType<F> firstTypeLimit;
    private IKeyType<S> secondTypeLimit;
    private IKeyType<T> thirdTypeLimit;
    private final A firstKeyBuilder;
    private final B secondKeyBuilder;
    private final C thirdKeyBuilder;

    public TriKeyCipher(A firstKey, B secondKey, C thirdKey) {
        this.firstType = firstKey.create();
        this.secondType = secondKey.create();
        this.thirdType = thirdKey.create();
        this.firstTypeLimit = this.limitDomainForFirstKey(firstKey).create();
        this.secondTypeLimit = this.limitDomainForSecondKey(secondKey).create();
        this.thirdTypeLimit = this.limitDomainForThirdKey(thirdKey).create();
        this.firstKeyBuilder = firstKey;
        this.secondKeyBuilder = secondKey;
        this.thirdKeyBuilder = thirdKey;
    }

    @Override
    public boolean isValid(TriKey<F, S, T> key) {
        return this.firstType.isValid(key.getFirstKey()) && this.secondType.isValid(key.getSecondKey()) && this.thirdType.isValid(key.getThirdKey());
    }

    @Override
    public TriKey<F, S, T> randomiseKey() {
        TriKey<F, S, T> key = TriKey.empty();
        return key.setFirst(this.firstTypeLimit.randomise()).setSecond(this.secondTypeLimit.randomise()).setThird(this.thirdTypeLimit.randomise());
    }

    @Override
    public void iterateKeys(KeyFunction<TriKey<F, S, T>> consumer) {
//        TriKey<F, S, T> key = TriKey.empty();
//        this.firstTypeLimit.iterateKeys(null, f -> {
//            key.setFirst(f);
//            this.secondTypeLimit.iterateKeys(key, s -> {
//                key.setSecond(s);
//                this.thirdTypeLimit.iterateKeys(key, t -> consumer.accept(key.setThird(t)));
//            });
//        });
        TriKey<F, S, T> key = TriKey.empty();
        this.firstTypeLimit.iterateKeys(f -> {
            key.setFirst(f);
            return this.secondTypeLimit.iterateKeys(s -> {
                key.setSecond(s);
                return this.thirdTypeLimit.iterateKeys(t -> consumer.apply(key.setThird(t).clone()));
            });
        });
    }

    @Override
    public TriKey<F, S, T> alterKey(TriKey<F, S, T> key, double temp, int count) {
        return TriKey.of(this.firstType.alterKey(key.getFirstKey()), this.secondType.alterKey(key.getSecondKey()), this.thirdType.alterKey(key.getThirdKey()));
    }

    @Override
    public BigInteger getNumOfKeys() {
        return this.firstTypeLimit.getNumOfKeys().multiply(this.secondTypeLimit.getNumOfKeys()).multiply(this.thirdTypeLimit.getNumOfKeys());
    }
    
    @Override
    public String prettifyKey(TriKey<F, S, T> key) {
        return String.join(" ",  this.firstType.prettifyKey(key.getFirstKey()), this.secondType.prettifyKey(key.getSecondKey()), this.thirdType.prettifyKey(key.getThirdKey()));
    }
    
    @Override
    public TriKey<F, S, T> parseKey(String input) throws ParseException {
        String[] parts = input.split(" ");
        if (parts.length != 3) {
            throw new ParseException(input, 0);
        }
        
        return TriKey.of(this.firstType.parse(parts[0]), this.secondType.parse(parts[1]), this.thirdType.parse(parts[2]));
    }
    
    @Nullable
    public String getHelp() {
        return String.join(" ",  this.firstType.getHelp(), this.secondType.getHelp(), this.thirdType.getHelp());
    }
    
    public IKeyBuilder<F> limitDomainForFirstKey(A firstKey) {
        return firstKey;
    }
    
    public IKeyBuilder<S> limitDomainForSecondKey(B secondKey) {
        return secondKey;
    }
    
    public IKeyBuilder<T> limitDomainForThirdKey(C thirdKey) {
        return thirdKey;
    }
    
    public void setFirstKeyDomain(Function<A, IKeyBuilder<F>> firstKeyFunc) {
        this.firstTypeLimit = firstKeyFunc.apply(this.firstKeyBuilder).create();
    }
    
    public void setSecondKeyDomain(Function<B, IKeyBuilder<S>> secondKeyFunc) {
        this.secondTypeLimit = secondKeyFunc.apply(this.secondKeyBuilder).create();
    }
    
    public void setThirdKeyDomain(Function<C, IKeyBuilder<T>> thirdKeyFunc) {
        this.thirdTypeLimit = thirdKeyFunc.apply(this.thirdKeyBuilder).create();
    }
    
    public IKeyType<F> getFirstKeyType() {
        return this.firstTypeLimit;
    }
    
    public IKeyType<S> getSecondKeyType() {
        return this.secondTypeLimit;
    }
    
    public IKeyType<T> getThirdKeyType() {
        return this.thirdTypeLimit;
    }
}
