import java.util.ArrayList;

public abstract class Cache {
	protected final int MAX_SIZE = 840; // bits
	protected final int ADDRESS_SIZE = 16; // bits
	/*
	 * Note that data is in 4 sequential bytes (32 bits) So can increase the data
	 * size but must be powers of 2
	 * 
	 */
	protected final int MIN_DATA_BLOCK_SIZE = 32; //bits
	/*
	 * Data is read at 1 byte at a time.
	 */
	protected final int DATA_READ_SIZE = 8; // bits
	
	/*
	 * Holds entries in the cache. Ex
	 * ________________________________________________ 
	 * |Valid: 1, Tag: 20, Data:
	 * Doesn't matter, (optional) Least recently used)
	 */
	protected ArrayList<ArrayList<CacheEntry>> table;

	/**
	 * Create a new cache.
	 */
	public Cache() {
		table = new ArrayList<ArrayList<CacheEntry>>();
	}

	/**
	 * Reads the address and returns the cycles it took to read that address. Hit -
	 * 1 Miss - 1 + 10 additional + 1*(number of bytes in each entry)
	 * 
	 * @param address
	 * @return
	 */
	public abstract int readAddress(int address);

	protected abstract int getMissDelay();

	/**
	 * Prints a copy paste friendly representation of the cache.
	 */
	public void printCache() {
		System.out.println("State of the Cache: ");
		System.out.println("VB\tTag\tData\tLRU");
		for (int i = 0; i < table.size(); i++) {
			ArrayList<CacheEntry> entries = table.get(i);
			for (int j = 0; j < entries.size(); j++) {
				entries.get(j).printEntry();
			}
		}
	}

	/**
	 * Helper method for getting logBase2 of something
	 * 
	 * @param arg
	 * @return
	 */
	protected static double logBase2(double arg) {
		return Math.log(arg) / Math.log(2.0);
	}
}
