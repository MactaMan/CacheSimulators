/**
 * Author: Carter Call
 * Dec 2019
 */

import java.util.ArrayList;

public class DirectMappedCache extends Cache {

	private final int LEAST_USED_SIZE = 0;
	private int dataSize; // bits
	private int numEntries;
	private int offsetSize;
	private int indexSize;
	private int tagSize;

	private CacheEntrySize s;

	public DirectMappedCache(int numEntries, int dataSize) {
		super();
		if(logBase2(numEntries) != (int)logBase2(numEntries))
		{
			throw new NumberFormatException("Number of rows for DirectMapped must be a power of 2");
		}
		this.dataSize = dataSize;
		this.numEntries = numEntries;
		s = new CacheEntrySize(this.dataSize, LEAST_USED_SIZE);

		offsetSize = (int) Math.ceil(logBase2(dataSize / DATA_READ_SIZE));
		indexSize = (int) Math.ceil(logBase2(numEntries));
		tagSize = calculateTagSize();
		System.out.println("OffsetSize: " + offsetSize + " -IndexSize: " + indexSize + " -TagSize: " + tagSize);
		
		// Populate the cache with invalid entries
		for (int i = 0; i < numEntries; i++) {
			ArrayList<CacheEntry> e = new ArrayList<CacheEntry>();
			// Add a new entry with invalid bit
			e.add(new CacheEntry(s, 0, 0));
			this.table.add(e);
		}
		
		int totalBits = (1 + tagSize + s.getDataSizeBits() + s.getLeastUsedSize()) * this.numEntries;
		System.out.println("Total bits used: " + totalBits);
		if(totalBits > this.MAX_SIZE)
			System.out.println("\n\nWARNING: CACHE SIZE[" + totalBits + "] LARGER THAN MAX SIZE["+ this.MAX_SIZE + "]\n\n");
	}

	/**
	 * Reads the given address into the cache.
	 */
	@Override
	public int readAddress(int address) {
		// Offset is the first (offsetSize) bits
		int offset = address % (int) Math.pow(2, offsetSize);

		// Index is the NEXT (indexSize) bits so divide by 2^offsetSize
		// Then mod by numEntries to get rid of everything above it
		int index = (address / (int) Math.pow(2, offsetSize)) % numEntries;
		// Tag is the rest so divide by 2^(offsetSize + indexSize)
		// Tag is address / Number of bytes in datablock * 4
		int tag = (address) / (int) Math.pow(2, offsetSize + indexSize);
		System.out.print("Address: " + address + "\tOffset: " + offset + "\tIndex: " + index + "\tTag: " + tag);
		// System.out.print("Reading Address - Tag[" + tag + "] - Index[" + index + "] -
		// Offset[" + offset + "]:");

		ArrayList<CacheEntry> row = table.get(index);
		// Should only be 1 entry for that row since it is direct mapped
		if (row.get(0).getTag() == tag && row.get(0).getValidBit() == 1) {
			System.out.println(" Hit");
			return 1;
		} else {
			// Replace the entry with new one
			System.out.println(" Miss");
			row.remove(0);
			row.add(new CacheEntry(s, 1, tag));
			return getMissDelay();
		}
	}

	public int calculateTagSize() {
		return this.ADDRESS_SIZE - offsetSize - indexSize;
	}

	/**
	 * Returns the delay for a miss
	 */
	@Override
	protected int getMissDelay() {
		return 1 + 10 + dataSize;
	}

}
