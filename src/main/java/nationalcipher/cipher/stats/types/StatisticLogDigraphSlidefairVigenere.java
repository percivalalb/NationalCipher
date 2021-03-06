package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphSlidefairVigenere extends TextStatistic<Double> {

    public StatisticLogDigraphSlidefairVigenere(String text) {
        super(text);
    }

    @Override
    public TextStatistic<Double> calculateStatistic() {
        this.value = PolyalphabeticIdentifier.calculateSlidefairVigenereLDI(this.text);
        return this;
    }
}
