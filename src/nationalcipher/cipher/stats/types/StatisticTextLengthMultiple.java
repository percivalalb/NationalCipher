package nationalcipher.cipher.stats.types;

import nationalcipher.cipher.stats.DataHolder;
import nationalcipher.cipher.stats.TextStatistic;

public class StatisticTextLengthMultiple extends TextStatistic {

	public StatisticTextLengthMultiple(String text) {
		super(text);
	}

	@Override
	public void calculateStatistic() {
		this.value = this.text.length();
	}
	
	@Override
	public double quantify(DataHolder data) {
		return this.value % data.value == 0 ? 0 : 1000;
	}
}