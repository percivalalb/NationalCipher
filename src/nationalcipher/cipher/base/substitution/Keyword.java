package nationalcipher.cipher.base.substitution;

import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.tools.KeyGeneration;

public class Keyword implements IRandEncrypter {

	public static String encode(String plainText, String keyword) {
		char[] charArray = plainText.toCharArray();
		
		String cipherText = "";
		
		for(char ch : charArray)
			if(ch >= 'A' && ch <= 'Z')
				cipherText += keyword.charAt(ch - 'A');
		
		return cipherText;
	}

	public static char[] decode(char[] cipherText, String keyword) {
		
		char[] plainText = new char[cipherText.length];
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (char)(keyword.indexOf(cipherText[i]) + 'A');

		return plainText;
	}
	
	public static byte[] decode(char[] cipherText, byte[] plainText, String keyword) {
		
		for(int i = 0; i < cipherText.length; i++)
			plainText[i] = (byte)(keyword.indexOf(cipherText[i]) + 'A');

		return plainText;
	}
	
	
	public static byte[] decodeWithAlphabet(char[] cipherText, byte[] plainText, char[] alphabet, String keyword) {
		
		for(int i = 0; i < cipherText.length; i++) {
			int index = keyword.indexOf(cipherText[i]);
			if(index == -1)
				plainText[i] = '@';
			else
				plainText[i] = (byte)alphabet[index];
		}
			

		return plainText;
	}

	@Override
	public String randomlyEncrypt(String plainText) {
		return encode(plainText, KeyGeneration.createLongKey26());
	}
}
