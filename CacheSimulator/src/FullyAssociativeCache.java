import java.util.ArrayList;

public class FullyAssociativeCache extends Cache {

	private int dataSize; // bits
	private int numEntries;
	private int offsetSize;
	private final int INDEX_SIZE = 0;
	private int tagSize;

	private CacheEntrySize s;

	public FullyAssociativeCache(int numEntries, int dataSize) {
		super();
		this.numEntries = numEntries;
		this.dataSize = dataSize;
		s = new CacheEntrySize(dataSize, (int) Math.ceil(logBase2(this.numEntries)));

		offsetSize = (int) Math.ceil(logBase2(dataSize / DATA_READ_SIZE));
		tagSize = this.ADDRESS_SIZE - offsetSize - INDEX_SIZE;
		System.out
				.println("OffsetSize: " + offsetSize + " -LRUSize: " + s.getLeastUsedSize() + " -TagSize: " + tagSize);
		// Populate the cache with invalid entries
		for (int i = 0; i < numEntries; i++) {
			ArrayList<CacheEntry> e = new ArrayList<CacheEntry>();
			// Add a new entry with invalid bit
			e.add(new CacheEntry(s, 0, 0, 0));
			this.table.add(e);
		}
		int totalBits = (1 + tagSize + s.getDataSize() + s.getLeastUsedSize()) * this.numEntries;
		System.out.println("Total bits used: " + totalBits);
		if(totalBits > this.MAX_SIZE)
			System.out.println("\n\nWARNING: CACHE SIZE[" + totalBits + "] LARGER THAN MAX SIZE["+ this.MAX_SIZE + "]\n\n");

	}

	@Override
	public int readAddress(int address) {
		// Offset is the first (offsetSize) bits
		int offset = address % (int) Math.pow(2, offsetSize);
		// Tag is the rest so divide by 2^(offsetSize + indexSize)
		// Tag is address / Number of bytes in datablock * 4
		int tag = (address) / (int) Math.pow(2, offsetSize);
		System.out.print("Address: " + address + "\tOffset: " + offset + "\tTag: " + tag);
		for (int i = 0; i < table.size(); i++) {
			ArrayList<CacheEntry> row = table.get(i);

			// Should only be 1 entry for that row since it is FullyAssociative
			if (row.get(0).getTag() == tag && row.get(0).getValidBit() == 1) {
				System.out.println(" Hit");
				if(row.get(0).getLeastUsed() != 0)
				{
					setLRU(i);
				}
				return 1;
			}

		}
		System.out.println(" Miss");
		// Place wasn't found
		int indexOfLRU = 0;
		for (int i = 0; i < table.size(); i++) {
			ArrayList<CacheEntry> row = table.get(i);
			// Should only be 1 entry for that row since it is FullyAssociative
			if (row.get(0).getValidBit() == 0) {
				row.remove(0);
				row.add(new CacheEntry(s, 1, tag, 0));
				setLRU(i);
				return getMissDelay();
			} else {
				if (table.get(indexOfLRU).get(0).getLeastUsed() < row.get(0).getLeastUsed()) {
					indexOfLRU = i;
				}

			}

		}
		table.get(indexOfLRU).remove(0);
		table.get(indexOfLRU).add(new CacheEntry(s, 1, tag, 0));
		setLRU(indexOfLRU);

		return getMissDelay();
	}

	/**
	 * Set given row of the Cache to the most recently used and properly update
	 * everything else.
	 * 
	 * @param index
	 */
	private void setLRU(int index) {
		for (int i = 0; i < table.size(); i++) {
			CacheEntry entry = table.get(i).get(0);
			if (index == i) {
				entry.setLeastUsed(0);
			} else {
				if (entry.getValidBit() == 1)
					entry.incrementLeastUsed();
			}
		}
	}

	/**
	 * Returns the delay for a miss
	 */
	@Override
	protected int getMissDelay() {
		return 1 + 10 + dataSize / 8;
	}

}
