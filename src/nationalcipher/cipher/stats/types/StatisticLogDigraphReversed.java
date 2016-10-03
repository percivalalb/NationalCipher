package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphReversed extends TextStatistic {

	public StatisticLogDigraphReversed(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateRDI(this.text);
	}
}
