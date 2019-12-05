/**
 * Author: Carter Call
 * Dec 2019
 */

import java.util.ArrayList;

public abstract class Cache {
	protected final int MAX_SIZE = 840; // bits
	protected final static int ADDRESS_SIZE = 16; // bits
	/*
	 * Note that data is in 4 sequential bytes (32 bits) So can increase the data
	 * size but must be powers of 2
	 * 
	 */
	protected final int MIN_DATA_BLOCK_SIZE = 4; //bits
	/*
	 * Data is read at 1 byte at a time.
	 */
	protected final int DATA_READ_SIZE = 1; 
	
	/*
	 * Holds entries in the cache. Ex
	 * ________________________________________________ 
	 * |Valid: 1, Tag: 20, Data:Doesn't matter, (optional) Least recently used)
	 */
	protected ArrayList<ArrayList<CacheEntry>> table;

	/**
	 * Create a new cache.
	 */
	public Cache() {
		table = new ArrayList<ArrayList<CacheEntry>>();
	}

	/**
	 * Reads the address and returns the cycles it took to read that address. 
	 * Hit - 1 
	 * Miss - 1 + 10 additional + 1*(number of bytes in entry)
	 * 
	 * @param address
	 * @return
	 */
	public abstract int readAddress(int address);

	/**
	 * Returns the delay for a miss
	 */
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
			if(entries.size() > 1 && (i + 1) < table.size())
			{
				System.out.println("-------------------");
			}
		}
	}
	
	/**
	 * Calculates what the size of a cache with the given data is.
	 * For fully associative, set numEntries to 1
	 * For Direct mapped set waysAssociative to 1
	 * For set associative, num entries is the rows, waysAssociative is number of entries in every row
	 * @param numEntries
	 * @param dataSize in bytes
	 * @param waysAssociative must be 1 or larger
	 * @return
	 */
	static public int getSizeWith(int numEntries, int dataSize, int waysAssociative)
	{
		int size = 0;
		int offsetSize = (int) Math.ceil(logBase2(dataSize));
		int indexSize = (int) Math.ceil(logBase2(numEntries));
		int LRUSize = (int) Math.ceil(logBase2(waysAssociative));
		int tagSize = ADDRESS_SIZE - offsetSize - indexSize;
		size = (1 + tagSize + (dataSize*8) + LRUSize) * numEntries * waysAssociative;
		
		return size;
		
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
