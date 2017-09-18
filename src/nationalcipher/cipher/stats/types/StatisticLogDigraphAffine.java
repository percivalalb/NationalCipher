package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphAffine extends TextStatistic {

	public StatisticLogDigraphAffine(String text) {
		super(text);
	}

	@Override
	public TextStatistic calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculateAffineLDI(this.text);
		return this;
	}
}
