package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticMaxBifid3to15 extends TextStatistic {

	public StatisticMaxBifid3to15(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateMaxBifidDiagraphicIC(this.text, 3, 15);
	}
}
