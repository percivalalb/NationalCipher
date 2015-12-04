package nationalcipher.cipher;

import java.util.Arrays;

public class ADFGVX {

	public static String encode(String plainText, String keysquare, int[] order, String adfgvx) {
		String cipherText = "";
		
		for(int i = 0; i < plainText.length(); i++) {
			char c = plainText.charAt(i);
			if(c == 'J') c = 'I';
			
			int charIndex = keysquare.indexOf(c);
			int row = (int)Math.floor(charIndex / adfgvx.length());
			int column = charIndex % adfgvx.length();
			
			cipherText += adfgvx.charAt(row);
			cipherText += adfgvx.charAt(column);
		}
		
		return ColumnarRow.encode(cipherText, order);
	}
	
	public static String decode(String cipherText, String keysquare, String key) {
		return decode(cipherText, keysquare, key, "ADFGVX");
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int[] order) {
		return decode(cipherText, keysquare, order, "ADFGVX");
	}
	
	public static String decode(String cipherText, String keysquare, String key, String adfgvx) {
		int[] order = new int[key.length()];
		
		char[] charArray = key.toCharArray();
		Arrays.sort(charArray);
		for(int i = 0; i < charArray.length; i++)
			order[key.indexOf(charArray[i])] = i;
		
		return new String(decode(cipherText.toCharArray(), keysquare, order, adfgvx));
	}
	
	public static char[] decode(char[] cipherText, String keysquare, int[] order, String adfgvx) {
		return decodeTransformed(ColumnarRow.decode(cipherText, order), keysquare, adfgvx);
	}
	
	public static char[] decodeTransformed(char[] untransformedText, String keysquare) {
		return decodeTransformed(untransformedText, keysquare, "ADFGVX");
	}

	public static char[] decodeTransformed(char[] untransformedText, String keysquare, String adfgvx) {
		char[] plainText = new char[untransformedText.length];
		
		for(int i = 0; i < untransformedText.length; i += 2) {
			char c1 = untransformedText[i];
			char c2 = untransformedText[i + 1];
			
			int row = adfgvx.indexOf(c1);
			int column = adfgvx.indexOf(c2);
			if(row != -1 && column != -1)
				plainText[i / 2] = keysquare.charAt(row * adfgvx.length() + column);
		}
		
		return plainText;
	}	
}