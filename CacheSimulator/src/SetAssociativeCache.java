import java.util.ArrayList;

public class SetAssociativeCache extends Cache {

	private int dataSize; // bits
	private int numEntries;
	private int offsetSize;
	private int wayAssociative;
	private int indexSize;
	private int tagSize;

	public SetAssociativeCache(int rows, int dataSize, int wayAssociative) {
		super();
		if(logBase2(rows) != (int)logBase2(rows))
		{
			throw new NumberFormatException("Number of rows for set associative must be a power of 2");
		}
		this.numEntries = rows;
		this.dataSize = dataSize;
		this.wayAssociative = wayAssociative;
		s = new CacheEntrySize(dataSize, (int) Math.ceil(logBase2(this.wayAssociative)));

		offsetSize = (int) Math.ceil(logBase2(dataSize / DATA_READ_SIZE));
		indexSize = (int) Math.ceil(logBase2(this.numEntries));
		tagSize = this.ADDRESS_SIZE - offsetSize - indexSize;
		System.out.println("OffsetSize: " + offsetSize + " -IndexSize: " + indexSize + " -TagSize: " + tagSize
				+ " -LRUSize: " + s.getLeastUsedSize());

		// Populate the cache with invalid entries
		for (int i = 0; i < numEntries; i++) {
			ArrayList<CacheEntry> e = new ArrayList<CacheEntry>();
			// Add a new entry with invalid bit
			for(int j = 0; j < wayAssociative; j++)
			{
				e.add(new CacheEntry(s, 0, 0, 0));
			}
			this.table.add(e);
		}
		int totalBits = (1 + tagSize + s.getDataSize() + s.getLeastUsedSize()) * this.numEntries * this.wayAssociative;
		System.out.println("Total bits used: " + totalBits);
		if(totalBits > this.MAX_SIZE)
			System.out.println("\n\nWARNING: CACHE SIZE[" + totalBits + "] LARGER THAN MAX SIZE["+ this.MAX_SIZE + "]\n\n");

	}

	private CacheEntrySize s;

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
			for(int i = 0; i < this.wayAssociative; i++)
			{
				CacheEntry entry = row.get(i);
				if(entry.getValidBit() == 1 && entry.getTag() == tag)
				{
					System.out.println(" Hit");
					if(entry.getLeastUsed() != 0)
					{
						setLRUMini(index, i);
					}
					return 1;
				}
			}
			System.out.println(" Miss");
			// Place wasn't found
			int indexOfLRU = 0;
			
			for (int i = 0; i < this.wayAssociative; i++) {
				CacheEntry entry = row.get(i);

				if (entry.getValidBit() == 0) {
					row.remove(i);
					row.add(i,new CacheEntry(s, 1, tag, 0));
					setLRUMini(index, i);
					return getMissDelay();
				} else {
					if (row.get(indexOfLRU).getLeastUsed() < row.get(i).getLeastUsed()) {
						indexOfLRU = i;
					}

				}

			}
			row.remove(indexOfLRU);
			row.add(indexOfLRU, new CacheEntry(s, 1, tag, 0));
			setLRUMini(index, indexOfLRU);
		
		
		return getMissDelay();
	}

	/**
	 * Set given row of the Cache to the most recently used and properly update
	 * everything else.
	 * 
	 * @param row: the index of the row to set
	 * @param index: The index of the entry in the row to set
	 */
	private void setLRUMini(int row,int index) {
		ArrayList<CacheEntry> outerRow = table.get(row);
		for(int i = 0; i < outerRow.size(); i++)
		{
			CacheEntry entry = outerRow.get(i);
			if (index == i) {
				entry.setLeastUsed(0);
			} else {
				if (entry.getValidBit() == 1)
					entry.incrementLeastUsed();
			}
		}
	}
	
	@Override
	protected int getMissDelay() {
		// TODO Auto-generated method stub
		return 1 + 10 + dataSize/8;
	}

}
