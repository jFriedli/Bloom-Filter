package BloomFilterDist;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	// File with various words
	final static String FILE = "./src/main/resources/words.txt";

	// False positive probability
	final static double p = 0.01;

	// How many random words to generate for false positive test
	final static int numberOfRandomWordsToCheck = 50000;

	public static void main(String[] args) throws FileNotFoundException {

		// Read in FILE and load words
		File file = new File(FILE);
		Scanner scanner = new Scanner(file);

		Set<String> words = new HashSet<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			for (String word : line.split("\\s")) {
				if (!word.isEmpty())
					words.add(word);
			}
		}

		// Create Bloom filter and fill it with words
		BloomFilter bloomFilter = new BloomFilter(words.size(), p);
		for (String word : words) {
			bloomFilter.add(word);
		}

		// Positives
		int positives = 0;
		for (String word : words) {
			positives += (bloomFilter.exists(word)) ? 1 : 0;
		}
		System.out.println("Positives: " + (100f / words.size()) * positives + "%");

		// False Positives
		int falsePositive = 0;
		List<String> randomWords = getRandomWords(numberOfRandomWordsToCheck);
		for (String word : randomWords) {
			falsePositive += (!words.contains(word) && bloomFilter.exists(word)) ? 1 : 0;
		}
		System.out.println("False Positives: " + ((100f / numberOfRandomWordsToCheck) * falsePositive) + "%");
	}

	/**
	 * Generate a list of random strings consisting of lower case characters from 3
	 * - 10 characters
	 * 
	 * @param nOfWords - how many words should be generated
	 * @return List of random words between 3 - 10 characters
	 */
	private static List<String> getRandomWords(int nOfWords) {
		List<String> words = new ArrayList<>();
		int min = 3;
		int max = 10;

		for (int i = 0; i < nOfWords; i++) {
			words.add(generateRandomString(getRandomIntInRange(min, max)));
		}

		return words;
	}

	/**
	 * Generate a random string with length wordLength consisting of all lower case
	 * characters of the alphabet
	 * 
	 * @param wordLength
	 * @return - random string in given length
	 */
	private static String generateRandomString(int wordLength) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int nAlphabet = alphabet.length();
		String word = "";
		
		// Construct string
		for (int i = 0; i < wordLength; i++) {
			int random = getRandomIntInRange(0, nAlphabet - 1);
			word += alphabet.substring(random, random + 1);
		}

		return word;

	}

	/**
	 * Get random integer between min - max
	 * @param min - minimum possible integer
	 * @param max - maximum possible integer
	 * @return random integer between min - max
	 */
	private static int getRandomIntInRange(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max + 1);
	}
}
