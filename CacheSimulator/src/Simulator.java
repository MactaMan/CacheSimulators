

public class Simulator {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int TestGroup[] = new int[]
				{
					16, 20, 24, 28, 32, 36, 60, 64, 56, 60, 64, 68,
					56, 60, 64, 72, 76, 92, 96, 100, 104, 108, 112, 
					120, 124, 128, 144, 148
				};
		
		int RealGroup[] = new int[]
				{
					4, 8, 12, 16, 20, 32, 36, 40, 44, 20, 32, 36, 
					40, 44, 64, 68, 4, 8, 12, 92, 96, 100, 104, 108, 
					112, 100, 112, 116, 120, 128, 140, 144
				};
		//Simulate various sizes of direct caches
		System.out.println("Sizes of Direct Mapped Caches");
		printSize(1, 4, 1);
		printSize(2, 4, 1);
		printSize(4, 4, 1);
		printSize(8, 4, 1);
		printSize(16, 4, 1);// 16 is max size
		System.out.println();
		printSize(1, 8, 1);
		printSize(2, 8, 1);
		printSize(4, 8, 1);
		printSize(8, 8, 1); // 8 is max size
		System.out.println();
		printSize(1, 16, 1);
		printSize(2, 16, 1);
		printSize(4, 16, 1); // 4 is max size
		System.out.println();
		printSize(1, 32, 1);
		printSize(2, 32, 1);
		System.out.println();
		printSize(1, 64, 1);
		System.out.println();
		
		
		//rows then bits per block
		Cache direct = new DirectMappedCache(4, 128);
		simulateCache(direct, TestGroup);

		Cache fullyAssociative = new FullyAssociativeCache(3, 256);
		simulateCache(fullyAssociative, TestGroup);
		
		Cache setAssociative = new SetAssociativeCache(1, 256, 3);
		simulateCache(setAssociative, TestGroup);
		
		
		
	}
	public static void simulateCache(Cache c, int[] address)
	{
		System.out.println("Initializing Cache with one runthrough of addresses . . .");
		readArrayOfAddresses(c, address);
		System.out.println("Finished. Starting real runthrough . . .\n\n");
		double delay = readArrayOfAddresses(c, address);
		double CPI = delay/address.length;
		c.printCache();
		System.out.println("Delay: " + delay + "\tCPI: " + CPI + "\n----------------------------------------------------------------\n\n");
	}
	
	private static int readArrayOfAddresses(Cache c, int[] address)
	{
		int delay = 0;
		for(int i = 0; i < address.length; i++)
		{
			delay += c.readAddress(address[i]);
		}
		return delay;
	}
	
	public static void printSize(int rows, int dataSize, int waysAssociative) {
		System.out.println("Entries: " + rows + "\tData Size: " + dataSize + "\tWays Associative: " + waysAssociative + "\tSize: " + Cache.getSizeWith(rows, dataSize, waysAssociative));
	}

}
