package sg.edu.nus.cs2020;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
/**
 * CS2020 PS6
 * @author Hong Jie
 * TextGenerator implementation using MarkovModel for generating characters
 */
public class TextGenerator {
	// Variables for storing kgram, input text and generated result
	private String kgram;
	private StringBuilder text = new StringBuilder();
	private StringBuilder result = new StringBuilder();
	private Random int_generator = new Random();
	/**
	 * Constructor
	 * @param k the order of the model
	 * @param n the number of characters to be generated
	 * @param filename
	 * Description: Reads in input text from file and builds Markov Model for generating characters.
	 * 				Randomly selects kgram from text and generates characters in ASCII format, based on occurrence probability.
	 * 				kgram is then updated with the next character and process repeats till n characters are generated. 
	 */
	public TextGenerator(int k, int n, String filename) {
		if (k < 1 || n < 1) {
			throw new IllegalArgumentException("Invalid k or n, must be positive integer");
		}
		
		try {
			FileReader f = new FileReader(filename);
			BufferedReader buff = new BufferedReader(f);
			while (buff.ready()) {
				text.append(buff.readLine() + " ");			// Ensures text has sufficient spaces and output doesn't have stray newlines
			}
			buff.close();
			
		} catch (IOException e) {
			System.out.println("Error in reading file");
			e.printStackTrace();
		}
		
		MarkovModel model = new MarkovModel(text.toString(), k);
		result.ensureCapacity(n);
		// Randomly selects first kgram
		int r = int_generator.nextInt(text.length());
		kgram = text.substring(r, r + k);					

		while (result.length() < n) {

			Character c = model.nextCharacter(kgram);
			result.append(String.valueOf(c));
			// Update kgram by adding c and dropping first letter
			kgram = kgram.subSequence(1, k) + c.toString();	
		}
	}
	
	@Override
	// Returns String representation of the generated text
	public String toString() {
		return result.toString();
	}
	
	public static void main(String[] args) {
		TextGenerator test = new TextGenerator(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2]);
		try {
			FileWriter f = new FileWriter("output.txt");	// Writes output text to output.txt
			BufferedWriter buff = new BufferedWriter(f);
			buff.write(test.toString());
			buff.close();
			
		} catch (IOException e) {
			System.out.println("Error in writing file");
			e.printStackTrace();
		}

	}
}
