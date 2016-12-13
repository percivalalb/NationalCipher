package nationalcipher.cipher.base.enigma;

import java.util.Arrays;

import javalibrary.util.ArrayUtil;

public class EnigmaMachine {

	//TODO CHANGE ALL THE PUBLIC FIELDS TO PRIVATE
	public String name;
	
	public int[][] rotors;
	public int[][] rotorsInverse;
	public int[][] notches;
	public int rotorCount;
	
	public int[][] reflector;
	public String[] reflectorNames;
	public int reflectorCount;
	
	public int[] etw;
	public int[] etwInverse;
	
	public int[][] thinRotor;
	public int[][] thinRotorInverse;
	public String[] thinRotorNames;
	public int thinRotorCount;
	
	public boolean canPlugboard;
	public boolean canUhr;
	public boolean hasThinRotor;
	public boolean stepping;
	
	public EnigmaMachine(String name) {
		this.name = name;
		this.canPlugboard = false;
		this.canUhr = false;
		this.stepping = true;
		this.hasThinRotor = false;
	}
	
	public final void setRotors(String... input) {
		int[][] normal = new int[input.length][26];
		int[][] inverse = new int[normal.length][26];
		
		for(int r = 0; r < input.length; r++) {
			for(int i = 0; i < 26; i++) {
				normal[r][i] = input[r].charAt(i) - 'A';
				inverse[r][normal[r][i]] = i;
			}
		}
		
		this.rotors = normal;
		this.rotorsInverse = inverse;
		this.rotorCount = input.length;
	}
	
	public final void setNotches(int[][] input) {
		this.notches = input;
	}
	
	public final void setNotches(String... input) {
		int[][] normal = new int[input.length][];
		
		for(int r = 0; r < input.length; r++)  {
			normal[r] = new int[input[r].length()];
			for(int i = 0; i < normal[r].length; i++) 
				normal[r][i] = input[r].charAt(i) - 'A';
		}

		this.notches = normal;
	}
	
	public final void setReflectors(String... input) {
		int[][] normal = new int[input.length][26];
		
		for(int r = 0; r < input.length; r++)
			for(int i = 0; i < input[r].length(); i++) 
				normal[r][i] = input[r].charAt(i) - 'A';
			
		this.reflector = normal;
		this.reflectorCount = input.length;
	}
	
	public final void setReflectorNames(String... input) {
		this.reflectorNames = input;
		this.reflectorCount = input.length;
	}

	public void setETW(String input) {
		this.etw = new int[26];
		for(int i = 0; i < input.length(); i++) 
			this.etw[i] = input.charAt(i) - 'A';
		
		int[] inverse = new int[26];
		for(int i = 0; i < 26; i++)
			inverse[this.etw[i]] = i;
			
		this.etwInverse = inverse;
	}
	
	public final void setThinRotors(String... input) {
		int[][] normal = new int[input.length][26];
		int[][] inverse = new int[normal.length][26];
		
		for(int r = 0; r < input.length; r++) {
			for(int i = 0; i < 26; i++) {
				normal[r][i] = input[r].charAt(i) - 'A';
				inverse[r][normal[r][i]] = i;
			}
		}
		
		this.thinRotor = normal;
		this.thinRotorInverse = inverse;
		this.thinRotorCount = input.length;
	}
	
	public final void setThinRotorNames(String... input) {
		this.thinRotorNames = input;
		this.thinRotorCount = input.length;
	}
	
	public final int getNumberOfRotors() {
		return this.rotorCount;
	}
	
	public final int getNumberOfReflectors() {
		return this.reflectorCount;
	}
	
	public final int getNumberOfThinRotors() {
		return this.thinRotorCount;
	}
	
	public final boolean canPlugboard() {
		return this.canPlugboard;
	}
	
	public final boolean canUhr() {
		return this.canUhr;
	}
	
	public final boolean hasThinRotor() {
		return this.hasThinRotor;
	}
	
	/**
	 * True is the classic Ratchets setting and False is cogs
	 * @return
	 */
	public final boolean getStepping() {
		return this.stepping;
	}
	
	public boolean isSolvable() {
		return true;
	}
	
	public EnigmaMachine createWithPlugboard(int[]... input) {
		EnigmaMachine copy = new EnigmaPlugboard(this.name);
		EnigmaMachine.copy(this, copy);
		
		int[] plugBoardArray = new int[26];
		for(int i = 0; i < 26; i++) plugBoardArray[i] = i;
		
		for(int[] swap : input) {
			if(swap[0] == swap[1]) continue;
			
			plugBoardArray[swap[0]] = swap[1];
			plugBoardArray[swap[1]] = swap[0];
		}
		
		copy.etw = plugBoardArray;
		copy.etwInverse = plugBoardArray;
		
		return copy;
	}
	
	public EnigmaMachine createWithUhr(int setting, char[][] input) {
		EnigmaMachine copy = new EnigmaUhr(this.name, input);
		EnigmaMachine.copy(this, copy);

		int[] plugBoardArray = new int[26];
		for(int i = 0; i < 26; i++) plugBoardArray[i] = i;
		for(char[] swap : input) {
			if(swap[0] == 0 || swap[1] == 0) continue;
			int uhrIndex = swap[2] - '0';
			
			int aWire = (uhrIndex * 4 + setting) % 40;
			int bWirePosition = (EnigmaLib.AB_WIRING[aWire] + (40 - setting)) % 40;
			plugBoardArray[swap[0] - 'A'] = input[EnigmaLib.B_PLUG_ORDER[bWirePosition / 4]][1];

			int bWire = (EnigmaLib.B_PLUG_ORDER_INDEXED[uhrIndex] * 4 + setting) % 40;
			int aWirePosition = (EnigmaLib.AB_WIRING_INDEXED[bWire] + (40 - setting)) % 40;
			plugBoardArray[swap[1] - 'A'] = input[aWirePosition / 4][0];
		}
		
		int[] inverse = new int[26];
		for(int i = 0; i < 26; i++)
			inverse[plugBoardArray[i]] = i;
		
		copy.etwInverse = inverse;
		copy.etw = plugBoardArray;
		
		return copy;
	}
	
	public EnigmaMachine createWithUhr(int setting, String... input) {
		if(input.length == 10) {
			char[][] raw = new char[10][3];
			for(int i = 0; i < input.length; i++)
				raw[i] = input[i].toCharArray();
			return createWithUhr(setting, raw);
		}
		return null;
	}
	
	public static void copy(EnigmaMachine orignal, EnigmaMachine copy) {
		copy.rotors = orignal.rotors;
		copy.rotorsInverse = orignal.rotorsInverse;
		copy.rotorCount = orignal.rotorCount;
		copy.notches = orignal.notches;
		copy.reflector = orignal.reflector;
		copy.reflectorNames = orignal.reflectorNames;
		copy.reflectorCount = orignal.reflectorCount;
		copy.etw = orignal.etw;
		copy.etwInverse = orignal.etwInverse;
		copy.thinRotor = orignal.thinRotor;
		copy.thinRotorInverse = orignal.thinRotorInverse;
		copy.thinRotorCount = orignal.thinRotorCount;
		copy.canPlugboard = orignal.canPlugboard;
		copy.canUhr = orignal.canUhr;
		copy.hasThinRotor = orignal.hasThinRotor;
		copy.stepping = orignal.stepping;
	}
	
	public static class EnigmaPlugboard extends EnigmaMachine {

		public int plugCount;
		
		public EnigmaPlugboard(String name) {
			super(name);
		}
		
		@Override
		public String toString() {
			//char[] plugs = new char[Math.max(this.plugCount * 3 - 1, 0)];
			//Arrays.fill(plugs, ' ');
			//for(int p = 0; p < this.plugCount; p++) {
			//	plugs[p * 3] = plugboard[p][0];
			//	plugs[p * 3 + 1] = plugboard[p][1];
			//}
			
			return String.format("%s, Plugboard:%s", this.name, Arrays.toString(this.etw));
		} 
	}
	
	public static class EnigmaUhr extends EnigmaMachine {

		public char[][] input;
		
		public EnigmaUhr(String name, char[][] input) {
			super(name);
			this.input = input;
		}
		
		@Override
		public String toString() {
			return String.format("%s, Plugboard:%s", this.name, Arrays.toString(this.etw));
		} 
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
