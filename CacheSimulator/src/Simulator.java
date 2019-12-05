/**
 * Author: Carter Call
 * Dec 2019
 */

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

		// combinations of row number and data block size in bytes to simulate
		int DirectCaches[][] = new int[][]
				{
					{1, 32}, {1, 64}, 
					{2, 16}, {2, 32}, 
					{4, 8}, {4, 16}, 
					{8, 4}, {8, 8}, 
					{16, 4}
				};
		int FullyCaches[][] = new int[][]
		{
			{1, 32}, {1, 64}, 
			{2, 16}, {2, 32}, 
			{3, 16}, {3, 32}, 
			{4, 8}, {4, 16}, 
			{5, 4}, {5, 8}, {5, 16}, 
			{6, 4}, {6, 8}, 
			{7, 4}, {7, 8}, 
			{8, 4}, {8, 8}, 
			{9, 4}, {9, 8}, 
			{10, 4}, {10, 8},
			{11, 4}, 
			{12, 4}, 
			{13, 4}, 
			{14, 4}, 
			{15, 4}, 
			{16, 4}, 
		};
		int SetCaches[][] = new int[][]
		{
			{2, 4, 8}, {2, 8, 5}, {2, 16, 2},
			{4, 4, 4}, {4, 8, 2},
			{8, 4, 2},
			{16, 4, 1}
			
		};
		
		
		System.out.println("\n\n\n\n\n\n--++==SIMULATING DIRECT MAPPED CACHES==++--\n\n\n\n\n\n");
		for(int i = 0; i < DirectCaches.length; i++)
		{
			System.out.println(DirectCaches[i][0] + "," + DirectCaches[i][1]);
			Cache direct = new DirectMappedCache(DirectCaches[i][0], DirectCaches[i][1]);
			simulateCache(direct, RealGroup);
		}
		System.out.println("\n\n\n\n\n\n--++==SIMULATING FULLY ASSOCIATIVE CACHES==++--\n\n\n\n\n\n");

		for(int i = 0; i < FullyCaches.length; i++)
		{
			System.out.println(FullyCaches[i][0] + "," + FullyCaches[i][1]);
			Cache full = new FullyAssociativeCache(FullyCaches[i][0], FullyCaches[i][1]);
			simulateCache(full, RealGroup);
		}
		System.out.println("\n\n\n\n\n\n--++==SIMULATING SET ASSOCIATIVE CACHES==++--\n\n\n\n\n\n");

		
		for(int i = 0; i < SetCaches.length; i++)
		{
			System.out.println(SetCaches[i][0] + "," + SetCaches[i][1] + "," + SetCaches[i][2]);
			Cache set = new SetAssociativeCache(SetCaches[i][0], SetCaches[i][1], SetCaches[i][2]);
			simulateCache(set, RealGroup);
		}
		
		
	}
	/**
	 * Simulates the given cache for the given addresses and prints results along the way
	 * @param c
	 * @param address
	 */
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
	/**
	 * Reads all addresses into the given cache.
	 * @param c
	 * @param address
	 * @return
	 */
	private static int readArrayOfAddresses(Cache c, int[] address)
	{
		int delay = 0;
		for(int i = 0; i < address.length; i++)
		{
			delay += c.readAddress(address[i]);
		}
		return delay;
	}
	/**
	 * Prints what the size of a cache with the given parameters would be.
	 * @param rows
	 * @param dataSize
	 * @param waysAssociative
	 */
	public static void printSize(int rows, int dataSize, int waysAssociative) {
		System.out.println("Entries: " + rows + "\tData Size: " + dataSize + "\tWays Associative: " + waysAssociative + "\tSize: " + Cache.getSizeWith(rows, dataSize, waysAssociative));
	}
	/**
	 * Prints various direct mapped cache sizes.
	 */
	public static void printDirectMappedCaches()
	{
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
	}
	/**
	 * Prints various fully associative cache sizes.
	 */
	public static void printFullyAssociativeCaches()
	{
		System.out.println("Sizes of Fully Associative Caches");
		printSize(1, 4, 1);
		printSize(1, 8, 1);
		printSize(1, 16, 1);
		printSize(1, 32, 1);// size 268
		printSize(1, 64, 1);
		System.out.println();
		printSize(1, 4, 2);
		printSize(1, 8, 2);
		printSize(1, 16, 2); // size 284
		printSize(1, 32, 2);
		System.out.println();
		printSize(1, 4, 3);
		printSize(1, 8, 3);
		printSize(1, 16, 3); // size 429
		printSize(1, 32, 3); 
		System.out.println();
		printSize(1, 4, 4);
		printSize(1, 8, 4); // size 320
		printSize(1, 16, 4);
		System.out.println();
		printSize(1, 4, 5); // size 250
		printSize(1, 8, 5);
		printSize(1, 16, 5);
		System.out.println(); // the rest are all greater than 250
		printSize(1, 4, 6);
		printSize(1, 8, 6);
		System.out.println();
		printSize(1, 4, 7);
		printSize(1, 8, 7);
		System.out.println();
		printSize(1, 4, 8);
		printSize(1, 8, 8);
		System.out.println();
		printSize(1, 4, 9);
		printSize(1, 8, 9);
		System.out.println();
		printSize(1, 4, 10);
		printSize(1, 8, 10);
		System.out.println();
		printSize(1, 4, 11);
		System.out.println();
		printSize(1, 4, 12);
		System.out.println();
		printSize(1, 4, 13);
		System.out.println();
		printSize(1, 4, 14);
		System.out.println();
		printSize(1, 4, 15);
		System.out.println();
		printSize(1, 4, 16);
		System.out.println();
	}
	/**
	 * Prints various set associative cache sizes.
	 */
	public static void printSetAssociativeCaches()
	{
		System.out.println("Sizes of Set Associative Caches");
		printSize(2, 4, 8);
		printSize(2, 8, 5);
		printSize(2, 16, 2);
		printSize(2, 32, 1);
		//printSize(2, 64, 1);
		System.out.println();
		printSize(4, 4, 4);
		printSize(4, 8, 2);
		printSize(4, 16, 1);
		//printSize(4, 32, 1); 
		System.out.println();
		printSize(8, 4, 2);
		printSize(8, 8, 1);
		//printSize(8, 16, 1); 
		System.out.println();
		printSize(16, 4, 1);
		//printSize(16, 8, 1);
		//System.out.println();
		//printSize(32, 4, 1);
		System.out.println();
	}

}
