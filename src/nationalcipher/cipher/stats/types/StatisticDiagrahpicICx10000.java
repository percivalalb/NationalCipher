package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticDiagrahpicICx10000 extends TextStatistic {

	public StatisticDiagrahpicICx10000(String text) {
		super(text);
	}
	
	@Override
	public void calculateStatistic() {
		this.value = StatCalculator.calculateIC(this.text, 2, true) * 10000;
	}
}
