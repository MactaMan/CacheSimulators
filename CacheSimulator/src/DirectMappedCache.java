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
		this.dataSize = dataSize;
		this.numEntries = numEntries;
		s = new CacheEntrySize(this.dataSize, LEAST_USED_SIZE);
		// For 32 bits, offset = 0 bits
		// 64 bits, 1 bit
		// 96 bits, between 1 and 2 rounded to 2
		// 128 bits, 2 bits
		// So do dataSize bits/32
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
		System.out.println(
				"Total bits used: " + ((1 + tagSize + s.getDataSize() + s.getLeastUsedSize()) * this.numEntries));
	}

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

	/**
	 * Tag size is
	 * 
	 * @return
	 */
	public int calculateTagSize() {
		return this.ADDRESS_SIZE - s.getLeastUsedSize() - offsetSize - indexSize;
	}

	/**
	 * Returns the delay for a miss
	 */
	@Override
	protected int getMissDelay() {
		// TODO Auto-generated method stub
		return 1 + 10 + dataSize / 8;
	}

}
