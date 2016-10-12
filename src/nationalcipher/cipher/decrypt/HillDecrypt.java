package nationalcipher.cipher.decrypt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import javalibrary.Output;
import javalibrary.algebra.Equation;
import javalibrary.algebra.Expression;
import javalibrary.algebra.SimultaneousEquations;
import javalibrary.exception.MatrixNoInverse;
import javalibrary.exception.MatrixNotSquareException;
import javalibrary.math.ArrayOperations;
import javalibrary.math.GCD;
import javalibrary.math.MathUtil;
import javalibrary.math.matrics.Matrix;
import javalibrary.string.StringAnalyzer;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ObjectTracker;
import nationalcipher.Settings;
import nationalcipher.cipher.Hill;
import nationalcipher.cipher.base.substitution.Affine;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.IDecrypt;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.tools.Creator;
import nationalcipher.cipher.tools.Creator.HillKey;
import nationalcipher.ui.KeyPanel;
import nationalcipher.ui.UINew;
import nationalcipher.cipher.tools.InternalDecryption;
import nationalcipher.cipher.tools.SettingParse;
import nationalcipher.cipher.tools.SubOptionPanel;

public class HillDecrypt implements IDecrypt {

	@Override
	public String getName() {
		return "Hill";
	}

	@Override
	public List<DecryptionMethod> getDecryptionMethods() {
		return Arrays.asList(DecryptionMethod.BRUTE_FORCE, DecryptionMethod.CALCULATED);
	}
	
	@Override
	public void attemptDecrypt(String text, Settings settings, DecryptionMethod method, Output output, KeyPanel keyPanel, ProgressValue progress) {
		HillTask task = new HillTask(text.toCharArray(), settings, keyPanel, output, progress);
		
		int[] range = SettingParse.getIntegerRange(this.rangeBox);
		int minSize = range[0];
		int maxSize = range[1];
		
		if(method == DecryptionMethod.BRUTE_FORCE) {

			
			BigInteger TWENTY_SIX = BigInteger.valueOf(26);
			
			for(int size = minSize; size <= maxSize; ++size)
				progress.addMaxValue(TWENTY_SIX.pow((int)Math.pow(size, 2)));
			
			for(int size = minSize; size <= maxSize; size++)
				Creator.iterateHill(task, size);

			
			
			//task.sortSolutions();
			//UINew.topSolutions.updateDialog(task.solutions);

			output.println(task.getBestSolution());
		}
		else if(method == DecryptionMethod.CALCULATED) {
			for(int size = minSize; size <= maxSize; size++) {
				Map<String, Integer> chars = StringAnalyzer.getEmbeddedStrings(text, size, size, false);
				List<String> sorted = new ArrayList<String>(chars.keySet());
				Collections.sort(sorted, new StringAnalyzer.SortStringInteger(chars));
				Collections.reverse(sorted);
				
				String[][] commonGrams = new String[][] {new String[] {"TH", "HE"}, TRI_GRAM[directionOption.getSelectedIndex()]};
				int[][] pickPattern = generatePickPattern(size, Math.min(SettingParse.getInteger(this.gramSearchBox), sorted.size()));
				
				for(int i = 0; i < pickPattern.length; i++) {
					int[] pattern = pickPattern[i];
					//for(String[] triGraph : generateTrigraphPattern("THE", "ING", "ENT", "ION", "TIO")) {
					
						//THE ING ENT ION TIO FOR NDE NCE TIS OFT MEN //new String[] {"THE", "ING", "ENT"}
	
		
				
						//output.println("%s --> %s  %s --> %s", language0, sorted0, language1, sorted1);
						// [ a, b ] 
						// [ c, d ]
					
					
							int[] matrixData = new int[0];
							for(int k = 0; k < size; k++) {
								int[][] equations = new int[size][size + 1];
								for(int l = 0; l < size; l++) {
									equations[l] = createEquationFrom(commonGrams[size - 2][l], sorted.get(pattern[l]), k);
								}
								int[] solution = SimultaneousEquations.solveSimEquationsMod(equations, 26);
								matrixData = ArrayUtil.concat(matrixData, solution);
							}
							Matrix matrix = new Matrix(matrixData, size, size);
					
							task.onIteration(matrix);
				
					}
				//}
			}
		}
		else {
			output.println(" Unexpected decryption method provided!");
		}	
	}
	
	public static String[][] generateTrigraphPattern(String... commonTrigraph) {
		int amount = (int)Math.pow(commonTrigraph.length, 3);
		
		String[][] pattern = new String[amount][3];
		
		int count = 0;
		for(int i = 0; i < commonTrigraph.length; i++)
			for(int j = 0; j < commonTrigraph.length; j++) {
				for(int k = 0; k < commonTrigraph.length; k++) {
					pattern[count++] = new String[] {commonTrigraph[i], commonTrigraph[j], commonTrigraph[k]};
				}
			}
		
		return pattern;
	}
	
	public static void recursive(int[][] patterns, ObjectTracker<Integer> count, int postion, int size, int times)  {

		for(int i = 0; i < times; i++) {
			//if(!ArrayUtil.contains(patterns[count.get()], postion, i) || postion == 0) {
				patterns[count.get()][postion] = i;
	
				if(size - postion > 1)
					recursive(patterns, count, postion + 1, size, times);
				
				if(postion + 1 == size) {
					System.out.println(Arrays.toString(patterns[count.get()]));
					count.target += 1;
				}
			//}
			//else
			//	continue;

			

		}
	}
	
	public static int[][] generatePickPattern(int size, int times) { //MathUtil.factorial(times)
		int[][] patterns = new int[(int)Math.pow(times, size)][size];
		//System.out.println(patterns.length);
		//recursive(patterns, ObjectTracker.create(0), 0, size, times);
		
		//for(int i = 0; i < patterns.length; i++)
		//		System.out.println(Arrays.toString(patterns[i]));
		
		
		if(size == 2) {
			int count = 0;
			for(int i = 0; i < times; i++)
				for(int j = 0; j < times; j++) {
					if(i == j) continue;
					patterns[count++] = new int[] {i, j};
				}
			
			return patterns;
		}
		else if(size == 3) {
			int count = 0;
			for(int i = 0; i < times; i++)
				for(int j = 0; j < times; j++) {
					for(int k = 0; k < times; k++) {
						if(i == j || i == k || j == k) continue;
						patterns[count++] = new int[] {i, j, k};
					}
				}
			
			return patterns;
		}
		
		return new int[0][0];
	}
		
	public static int[] createEquationFrom(String plainText, String cipherText, int index) {
		int[] equation = new int[plainText.length() + 1];
		for(int i = 0; i < equation.length - 1; i++)
			equation[i] = plainText.charAt(i) - 'A';
		equation[equation.length - 1] = cipherText.charAt(index) - 'A';
		return equation;
	}
	
	public static void main(String[] args) {
		//generatePickPattern(3, 5); //new double[] {7, 5, -3, 16}, new double[] {3, -5, 2, -8}, new double[] {5, 3, -7, 0}}
		//System.out.println(Arrays.toString(SimultaneousEquations.solveSimEquations(new double[][] {new double[] {0, 0, 0, 4, 12}, new double[] {3, -5, 17, 2, 119}, new double[] {5, 3, -7, 0, -15}, new double[] {15, -5, -7, 2, -25}})));
		int[] matrixData = new int[0];
		for(int i = 0; i < 3; i++) {
			/**Working Combos
			THE :  1.81, AND :  0.73, ING :  0.72, ENT :  0.42, ION :  0.42
			HER :  0.36, FOR :  0.34, THA :  0.33, NTH :  0.33, INT :  0.32
			
			THE, AND, ING Determinant:  493.0
			THE, ING, ENT Determinant:  2523.0
			THE, ING, ION Determinant:  2165.0
			THE, ENT, ION Determinant: -1335.0
			ING, ENT, HER Determinant:  1555.0
			ING, ENT, FOR Determinant: -63.0
			ENT, ION, HER Determinant: -1095.0
			ING, ENT, THA Determinant:  2315.0
			ING, ENT, NTH Determinant:  129.0
			ING, ENT, NTH Determinant:  129.0
			
			**/
			int[] solution = SimultaneousEquations.solveSimEquationsMod(new int[][] {createEquationFrom("THE", "WDD", i), createEquationFrom("AND", "NDA", i), createEquationFrom("THA", "IYB", i)}, 26);
			System.out.println(Arrays.toString(solution));
			matrixData = ArrayUtil.concat(matrixData, solution);
		}
		Matrix matrix = new Matrix(matrixData, 3, 3);
		System.out.println(matrix);
			//[14, 6, 13, 25, 10, 1, 18, 11, 13]
		//THE goes to WDD --- AND goes to NDA --- THA goes to WZD --- ENT goes to RPU --- ING goes to IYB --- ION goes to BPZ
		
		/**
		int[] matrixData = new int[0];
		for(int i = 0; i < 3; i++) {
			int[] solution = solveSimEquationsInMod2x2(new int[][] {createEquationFrom("ENT", "RPU", i), createEquationFrom("THE", "WDD", i), createEquationFrom("ION", "BPZ", i)}, 26);
			matrixData = ArrayUtil.concat(matrixData, solution);
		}
		Matrix matrix = new Matrix(matrixData, 3, 3);
		System.out.println(matrix);**/
	}
	
	
	
	public static int[] solveSimEquationsInMod2x2(int[][] simEquations, int mod) {
		int UNKNOWNS = simEquations.length;
		//for(int i = 0; i < UNKNOWNS; i++)
		//	for(int j = 0 ; j < UNKNOWNS; j++)
		//		if(i != j)
		//			simEquations[i] = ArrayOperations.mod(ArrayOperations.add(simEquations[i], simEquations[j]), mod);
		//printEquations(simEquations);
		
		int multipler = 0;
		int equatIndex = 0;
		int constIndex = 0;
		
		found:
		for(int i = 0; i < UNKNOWNS; i++) {
			for(int j = 0 ; j < UNKNOWNS; j++) {
				int value = simEquations[i][j];
				if(MathUtil.hasMultiplicativeInverse(value, mod)) {
					multipler = MathUtil.getMultiplicativeInverse(value, mod);
					
					equatIndex = i;
					constIndex = j;
					break found;
				}
			
			}
		}
		//System.out.println(equatIndex+ " " + constIndex);
		ArrayOperations.multiply(simEquations[equatIndex], multipler);
		ArrayOperations.mod(simEquations[equatIndex], mod);
		//printEquations(simEquations);
		int[][] productEquations = new int[UNKNOWNS - 1][UNKNOWNS];
		for(int i = 0, realI = 0; i < UNKNOWNS; i++) {
			if(i == equatIndex) continue;
			int[] newEquation = new int[UNKNOWNS];
			int[] target1 = simEquations[equatIndex];
			int[] target2 = simEquations[i];
			int targetMutlipler = target2[constIndex];
			//number of 'a's in second equation
			for(int j = 0, realJ = 0; j < UNKNOWNS + 1; j++) {
				if(j == constIndex) continue;
				newEquation[realJ++] = target2[j] - targetMutlipler * target1[j];
			}
			
			productEquations[realI++] = ArrayOperations.mod(newEquation, mod);//ArrayOperations.mod(ArrayOperations.add(simEquations[equatIndex], simEquations[i]), mod);
		}
		
		int[] solution = null;
		
		if(UNKNOWNS - 1 == 1) {
			//System.out.println(String.format("%da = %d", productEquations[0][0], productEquations[0][1]));
			int inverse = MathUtil.getMultiplicativeInverse(productEquations[0][0], mod);

			solution = new int[] {(productEquations[0][1] * inverse) % mod};
		}
		else
			solution = solveSimEquationsInMod2x2(productEquations, mod);
	
		
		//System.out.println(equatIndex + " " + constIndex);
		//System.out.println("Solution: " + Arrays.toString(solution));
		//System.out.println("Unknowns: " + UNKNOWNS);
		int[] finalSolution = new int[UNKNOWNS];
		int indexGivenSolution = 0;
		for(int i = 0; i < UNKNOWNS; i++) {
			if(i == constIndex)
				finalSolution[i] = -1;
			else
				finalSolution[i] = solution[indexGivenSolution++];
		}
		//System.out.println(Arrays.toString(finalSolution));
		
		for(int subBackIndex = 0; subBackIndex < UNKNOWNS; subBackIndex++) {
			if(!MathUtil.hasMultiplicativeInverse(simEquations[subBackIndex][constIndex], mod)) continue;
			int answer = simEquations[subBackIndex][UNKNOWNS];
			//System.out.println("Answer: " + answer);
			
			for(int i = 0; i < UNKNOWNS; i++) {
				if(i == constIndex) continue;
				answer -= finalSolution[i] * simEquations[subBackIndex][i];
			}
			//Dont require this bit: answer %= mod;
			finalSolution[constIndex] = MathUtil.mod(answer * MathUtil.getMultiplicativeInverse(simEquations[subBackIndex][constIndex], mod), mod);
			break;
		}
		//System.out.println(Arrays.toString(finalSolution));
		
		return finalSolution;
	}
	
	public static void printEquations(int[][] equations) {
		for(int k = 0; k < equations.length; k++) {
			System.out.print(toString(equations[k]));
			if(k == equations.length - 1) System.out.println("");
			else System.out.print(" || ");
		}
	}
	
	public static void printEquations(double[][] equations) {
		for(int k = 0; k < equations.length; k++) {
			System.out.print(toString(equations[k]));
			if(k == equations.length - 1) System.out.println("");
			else System.out.print(" || ");
		}
	}
	
	public static String toString(int[] equation) {
		String build = "";
		char[] s = new char[] {'a', 'b', 'c', 'd', 'e', 'f'};
		for(int i = 0; i < equation.length - 1; i++) {
			build += equation[i] + "" + s[i] + " ";
			if(i != equation.length - 2)
				build += "+ ";
		}
		build += "= " + equation[equation.length - 1];


		return build;
	}
	
	public static String toString(double[] equation) {
		String build = "";
		char[] s = new char[] {'a', 'b', 'c', 'd', 'e', 'f'};
		for(int i = 0; i < equation.length - 1; i++) {
			build += equation[i] + "" + s[i] + " ";
			if(i != equation.length - 2)
				build += "+ ";
		}
		build += "= " + equation[equation.length - 1];


		return build;
	}
	public String[][] TRI_GRAM = new String[][] {new String[] {"THE", "AND", "ING"}, new String[] {"THE", "ING", "ENT"}, new String[] {"THE", "ING", "ION"}, new String[] {"THE", "ENT", "ION"}, new String[] {"ING", "ENT", "HER"}, new String[] {"ING", "ENT", "FOR"}, new String[] {"ENT", "ION", "HER"}, new String[] {"ING", "ENT", "THA"}, new String[] {"ING", "ENT", "NTH"}};
	public String[] TRI_GRAM_DISPLAY = new String[] {"THE AND ING", "THE ING ENT", "THE ING ION", "THE ENT ION", "ING ENT HER", "ING ENT FOR", "ENT ION HER", "ING ENT THA", "ING ENT NTH"};
	
	private JTextField rangeBox = new JTextField("2-3");
	private JTextField gramSearchBox = new JTextField("20");
	private JComboBox<String> directionOption = new JComboBox<String>(TRI_GRAM_DISPLAY);
	
	@Override
	public void createSettingsUI(JDialog dialog, JPanel panel) {
		((AbstractDocument)this.rangeBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerRangeInput(this.rangeBox));
		((AbstractDocument)this.gramSearchBox.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		
		panel.add(new SubOptionPanel("Matrix Size Range:", this.rangeBox));
		panel.add(new SubOptionPanel("N-Gram Search Range:", this.gramSearchBox));
		panel.add(new SubOptionPanel("3x3 Trigram Sets:", this.directionOption));
		
		this.directionOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				directionOption.getSelectedIndex();
			}
		});
		
		dialog.add(panel);
	}

	public static class HillTask extends InternalDecryption implements HillKey {

		public HillTask(char[] text, Settings settings, KeyPanel keyPanel, Output output, ProgressValue progress) {
			super(text, settings, keyPanel, output, progress);
		}

		@Override
		public void onIteration(Matrix matrix) {
			
			try {
				this.lastSolution = new Solution(Hill.decode(this.text, matrix), this.settings.getLanguage()).setKeyString(matrix.toString());
				this.addSolution(this.lastSolution);
				
				if(this.lastSolution.score >= this.bestSolution.score) {
					this.bestSolution = this.lastSolution;
					this.output.println("%s", this.bestSolution);	
					this.keyPanel.updateSolution(this.bestSolution);
				}
			}
			catch(MatrixNoInverse e) {
				return;
			}
			catch(MatrixNotSquareException e) {
				return;
			}
			finally {
				this.keyPanel.updateIteration(this.iteration++);
				this.progress.increase();
			}
		}
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}
}
