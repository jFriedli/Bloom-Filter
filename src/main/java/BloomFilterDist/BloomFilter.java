package BloomFilterDist;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class BloomFilter {
	// Array with bits set when an element is added
	boolean[] hashSet;
	
	// Number of words to be expected in the filter
	int n;
	
	// False positive probability
	double p;
	
	// Length of array needed for given n and p
	int m;
	
	// Number of hashing per string needed with given m and n
	int k;

	/**
	 * Create new bloom filter with n and p
	 * @param n - expected number of items
	 * @param p - false positive probability
	 */
	public BloomFilter(int n, double p) {
		this.n = n;
		this.p = p;
		calculateM();
		calculateK();
		hashSet = new boolean[m];

		System.out.println("n: " + n);
		System.out.println("p: " + p);
		System.out.println("m: " + m);
		System.out.println("k: " + k);

	}

	/**
	 * Item exists in filter with probability 1-p
	 * @param s - string to check
	 * @return existence of s in filter
	 */
	public boolean exists(String s) {
		boolean exists = true;
		for (int seed = 0; seed < k && exists; seed++) {
			exists = hashSet[getHashIndex(s, seed)];
		}
		return exists;
	}

	/** 
	 * Add new item to filter
	 * @param s - item to add
	 */
	public void add(String s) {
		for (int i = 0; i < k; i++) {
			int hash = getHashIndex(s, i);
			hashSet[hash] = true;
		}
	}

	/**
	 * Calculate hash index with murmur 3.128 for given string and seed
	 * @param s - string to be hashed
	 * @param seed - current seed
	 * @return hashed index of s
	 */
	private int getHashIndex(String s, int seed) {
		HashFunction murmur3 = Hashing.murmur3_128(seed);
		HashCode hashCode = murmur3.newHasher().putString(s, StandardCharsets.UTF_8).hash();
		return Math.abs(hashCode.asInt() % m);
	}

	/**
	 * Calculate m for given n and p
	 */
	private void calculateM() {
		m = (int) Math.round(((-n * Math.log(p)) / Math.pow(Math.log(2), 2)));
	}

	/**
	 * Calculate k for given m and n
	 */
	private void calculateK() {
		k = (int) Math.round((m / n) * Math.log(2));
	}
}
