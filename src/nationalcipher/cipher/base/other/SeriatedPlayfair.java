package nationalcipher.cipher.base.other;

import javalibrary.math.MathUtil;
import javalibrary.util.RandomUtil;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class SeriatedPlayfair implements IRandEncrypter {

	
	public static String encode(String plainText, String keysquare, int period) {
		
		plainText = plainText.replaceAll("J", "I");
		while(true) {
			boolean valid = true;

			for(int i = 0; i < plainText.length() && valid; i += period * 2) {
				int min = Math.min(period, (plainText.length() - i + 1) / 2);

				for(int j = 0; j < Math.min(min, (plainText.length() - i) / 2); j++) {
					char a = plainText.charAt(i + j);
					char b = plainText.charAt(i + j + min);
					if(a == b) {
						char nullChar = 'X';
						if(a == 'X')
							nullChar = 'Q';
						plainText = plainText.substring(0, i + min + j) + nullChar + plainText.substring(i + min + j, plainText.length());
						valid = false;
						break;
					}
				}
			}
			
			if(valid)
				break;
		}
		
		if(plainText.length() % 2 != 0)
			plainText += 'X';

		
		char[] cipherText = new char[plainText.length()];
		
		for(int i = 0; i < plainText.length(); i += period * 2) {
			int min = Math.min(period, (plainText.length() - i) / 2);

			for(int j = 0; j < min; j++) {
				char a = plainText.charAt(i + j);
				char b = plainText.charAt(i + j + min);
				
				 
				int i1 = keysquare.indexOf(a);
			    int i2 = keysquare.indexOf(b);
			    int row1 = (int)Math.floor(i1 / 5);
			    int col1 = i1 % 5;
			    int row2 = (int)Math.floor(i2 / 5);
			    int col2 = i2 % 5;
			        
			    char c, d;
			        
			    if(row1 == row2) {
			    	c = keysquare.charAt(row1 * 5 + MathUtil.mod(col1 + 1, 5));
			    	d = keysquare.charAt(row2 * 5 + MathUtil.mod(col2 + 1, 5));
			    }
			    else if(col1 == col2) {
			    	c = keysquare.charAt(MathUtil.mod(row1 + 1, 5) * 5 + col1);
			    	d = keysquare.charAt(MathUtil.mod(row2 + 1, 5) * 5 + col2);
			    }
			    else {
			        c = keysquare.charAt(row1 * 5 + col2);
			        d = keysquare.charAt(row2 * 5 + col1);
			    }
			    
			    if(c == d) {
			    	System.out.println("ERROR " + a + "" + b  + " " + c + "" + d);
			    }
			        
			    cipherText[i + j] = c;
			    cipherText[i + j + min] = d;
			}
		}
		
		return new String(cipherText);
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int period) {
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i += period * 2) {
			int min = Math.min(period, (cipherText.length - i) / 2);

			for(int j = 0; j < min; j++) {
				char a = cipherText[i + j];
				char b = cipherText[i + j + min];
				
				int i1 = keysquare.indexOf(a);
			    int i2 = keysquare.indexOf(b);
			    int row1 = i1 / 5;
			    int col1 = i1 % 5;
			    int row2 = i2 / 5;
			    int col2 = i2 % 5;
			        
			    char c, d;
			        
			    if(row1 == row2) {
			    	c = keysquare.charAt(row1 * 5 + MathUtil.mod(col1 - 1, 5));
			    	d = keysquare.charAt(row2 * 5 + MathUtil.mod(col2 - 1, 5));
			    }
			    else if(col1 == col2) {
			    	c = keysquare.charAt(MathUtil.mod(row1 - 1, 5) * 5 + col1);
			        d = keysquare.charAt(MathUtil.mod(row2 - 1, 5) * 5 + col2);
			    }
			    else {
			    	c = keysquare.charAt(row1 * 5 + col2);
			        d = keysquare.charAt(row2 * 5 + col1);
			    }
			        
			    plainText[i + j] = c;
			    plainText[i + j + min] = d;
			}
		}
		
		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey25(), RandomUtil.pickRandomInt(2, 15));
	}
}