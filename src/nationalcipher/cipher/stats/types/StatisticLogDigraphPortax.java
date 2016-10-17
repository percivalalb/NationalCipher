package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticLogDigraphPortax extends TextStatistic {

	public StatisticLogDigraphPortax(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = PolyalphabeticIdentifier.calculatePTX(this.text);
	}
}