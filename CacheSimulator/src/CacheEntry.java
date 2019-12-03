
public class CacheEntry {

	final int VALID_BIT_SIZE = 1; // bits
	final int ADDRESS_SIZE = 16; // bits
	private int dataSize; // bits
	private int leastUsedSize; // bits

	// The valid bit for this entry
	private int validBit;
	// The tag for this entry
	private int tag;
	// The leastUsed bits for this entry
	// Higher means less priority.
	private int leastUsed;

	/**
	 * Creates a new Cache entry with the given CacheEntrySize, validBit, tag, and
	 * leastUsed counter leastUsed is optional
	 * 
	 * @param c
	 * @param validBit
	 * @param tag
	 * @param leastUsed
	 */
	public CacheEntry(CacheEntrySize c, int validBit, int tag, int leastUsed) {
		this.dataSize = c.getDataSize();
		this.leastUsedSize = c.getLeastUsedSize();

		this.validBit = validBit;
		this.tag = tag;
		this.leastUsed = leastUsed;
	}

	/**
	 * Creates a new Cache entry with the given CacheEntrySize, validBit, tag
	 * 
	 * @param c
	 * @param validBit
	 * @param tag
	 * @param leastUsed
	 */
	public CacheEntry(CacheEntrySize c, int validBit, int tag) {
		this.dataSize = c.getDataSize();
		this.leastUsedSize = c.getLeastUsedSize();
		if (leastUsedSize != 0)
			throw new IllegalArgumentException(
					"leastUsedSize must be 0 if there is not a leastUsed section of the entry.");

		this.validBit = validBit;
		this.tag = tag;
	}

	public int getValidBit() {
		return this.validBit;
	}

	public int getTag() {
		return this.tag;
	}

	public int getLeastUsed() {
		return this.leastUsed;
	}

	public void setLeastUsed(int num) {
		this.leastUsed = num;
	}

	public void incrementLeastUsed() {
		this.leastUsed += 1;
	}

	/**
	 * Returns the entry's size
	 * 
	 * @return
	 */
	public int getEntrySize() {
		return VALID_BIT_SIZE + ADDRESS_SIZE + dataSize + leastUsedSize;
	}

	/**
	 * Prints this entry separated by tabs
	 */
	public void printEntry() {
		System.out.print(validBit + "\t" + tag + "\t" + "?data?");
		if (leastUsedSize > 0) {
			System.out.print("\t" + leastUsed);
		}
		System.out.println();
	}

}

/**
 * Small helper class to allow for easier reusability.
 * 
 * @author carter
 *
 */
class CacheEntrySize {
	private int dataSize; // bits
	private int leastUsedSize; // bits

	public CacheEntrySize(int dataSize, int leastUsedSize) {
		this.dataSize = dataSize;
		this.leastUsedSize = leastUsedSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public int getLeastUsedSize() {
		return leastUsedSize;
	}

}
