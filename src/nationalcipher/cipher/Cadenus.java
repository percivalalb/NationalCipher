package nationalcipher.cipher;

import nationalcipher.cipher.manage.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Cadenus implements IRandEncrypter {
	
	public static String encode(String plainText, String key) {
		while(plainText.length() % (key.length() * 25) != 0)
			plainText += 'X';
		
		if(plainText.length() != key.length() * 25) {
			String combinedMulti = "";
			for(int i = 0; i < plainText.length() / (key.length() * 25); i++) {
				combinedMulti += encode(plainText.substring(i * key.length() * 25, (i + 1) * key.length() * 25), key);
			}
			return combinedMulti;
		}
		else {
			int keyLength = key.length();
	
			int[] order = new int[keyLength];
			
			int p = 0;
			for(char ch = 'A'; ch <= 'Z'; ++ch) {
				int index = key.indexOf(ch);
				if(index != -1)
					order[index] = p++;
			}
	
			//Creates grid
			char[] temp_grid = new char[plainText.length()];
	
			for(int j = 0; j < 25; j++) {
				for(int i = 0; i < keyLength; i++) {
					int newColumn = order[i];
					int newIndex = (j + charValue(key.charAt(i))) % 25;
					temp_grid[newIndex * keyLength + newColumn] = plainText.charAt(j * keyLength + i);
				}
			}
			return new String(temp_grid);
		}

	}
	
	public static char[] decode(char[] cipherText, String key) {
		int keyLength = key.length();

		int[] order = new int[keyLength];
		
		int p = 0;
		for(char ch = 'A'; ch <= 'Z'; ++ch) {
			int index = key.indexOf(ch);
			if(index != -1)
				order[p++] = index;
		}
		
		//Creates grid
		char[] grid = new char[cipherText.length];
		
		for(int j = 0; j < 25; j++) {
			for(int i = 0; i < keyLength; i++) {
				int newColumn = order[i];
				int newIndex = (j - charValue(key.charAt(newColumn)) + 25) % 25;
				grid[newIndex * keyLength + newColumn] = cipherText[j * keyLength + i];
			}
		}

		return grid;
	}
	
	public static int charValue(char character) {
		if(character < 'V')
			return character - 65;
		else if(character > 'W')
			return character - 66;
		else
			return 21;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createShortKey26(2, 7));
	}
}
