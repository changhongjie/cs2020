package sg.edu.nus.cs2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
/**
 * CS2020 PS6
 * @author Hong Jie
 * MarkovModel implementation for text generator
 */
public class MarkovModel {
	// Order of the model, greater than 0
	private int order;
	// HashMap that maps every kgram to a character array
	private HashMap<String, int[]> kgramMap = new HashMap<>();
	// Default character in the event of no possible character (whitespace in this case)
	private final char NOCHARACTER = (char)(32);
	private Random int_generator = new Random();
	/**
	 * Constructor for Markov Model
	 * @param text the entire input text to be modeled, supposed to be ASCII text only
	 * @param order size of kgrams
	 */
	public MarkovModel(String text, int order) {
		this.order = order;
		
		for (int i=0; i < text.length()-order; i++) {
			
			String s = text.substring(i, i + order);
			Character next = text.charAt(i + order);
			
			if (kgramMap.containsKey(s)) {
				
				int[] charArr = kgramMap.get(s);
				charArr[next.hashCode() % 128] += 1;
				kgramMap.put(s, charArr);
				
			} else {
				
				int[] charArr = new int[128];
				Arrays.fill(charArr, 0);
				charArr[next.hashCode() % 128] = 1;
				kgramMap.put(s, charArr);
			}
		}
	}
	/**
	 * Getter method
	 * @return order of model
	 */
	public int order() {
		return this.order;
	}
	/**
	 * getFrequency method finds the total number of characters associated with kgram
	 * @param kgram
	 * @return number of characters in a kgram's char array
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("Invalid input length");
			
		} else if (!(kgramMap.containsKey(kgram))) {
			throw new IllegalArgumentException(kgram + " not in model");
			
		} else {
			int[] charArr = kgramMap.get(kgram);
			int count = 0;
			for (int val : charArr) {
				count += val;
			}
			return count;
		}
	}
	/**
	 * getFrequency method for finding the number of instances a char is associated with kgram
	 * @param kgram
	 * @param c
	 * @return number of instances of c in kgram's char array
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("Invalid input length");
			
		} else if (!(kgramMap.containsKey(kgram))) {
			throw new IllegalArgumentException(kgram + " not in model");
			
		} else {
			return kgramMap.get(kgram)[c % 128];
		}
	}
	/**
	 * nextCharacter method randomly generates the next character based on kgram
	 * @param kgram
	 * @return char c
	 * Description: Uses Random generator to generate an index of selected char in char array
	 * 				Cumulatively adds up number of characters in char array until index is found
	 * 				Hence, probability of a char c being selected is getFrequency(kgram, c)/getFrequency(kgram)
	 */
	public char nextCharacter(String kgram) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("Invalid input length");
		} 
		int frequencyTotal = getFrequency(kgram);
		if (frequencyTotal == 0) {							// Handle undefined probability
			return NOCHARACTER;
		}
		int num = int_generator.nextInt(frequencyTotal);
		int selectedIndex = 0;
		int charIndex = 32;									// Only include textual characters
		
		while (selectedIndex <= num) {
			charIndex++;
			char c = (char) charIndex;
			selectedIndex += getFrequency(kgram, c);
			if (charIndex >= 128) {							// No more available textual characters
				return NOCHARACTER;
			}	
		}
		return (char) charIndex;
	}
	// Set seed for Random generator
	public void setRandomSeed(long s) {
		int_generator.setSeed(s);
	}
	
}
