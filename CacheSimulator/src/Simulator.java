

public class Simulator {

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int TestGroup[] = new int[]
				{
					16, 20, 24, 28, 32, 36, 60, 64, 56, 60, 64, 68,
					56, 60, 64, 72, 76, 92, 96, 100, 104, 108, 112, 
					120, 124, 128, 144, 148
				};
		
		
		
		//rows then bits per block
		Cache direct = new DirectMappedCache(4, 128);
		
		
		readArrayOfAddresses(direct, TestGroup);
		System.out.println("\n\n");
		int delay = readArrayOfAddresses(direct, TestGroup);
		direct.printCache();
		System.out.println(delay);
		
		
		//8/64 is 15 hits out of 28
		//3/256 is 23 hits out of 28
		//17/32 is 5 hits out of 28
		Cache fullyAssociative = new FullyAssociativeCache(3, 256);
		readArrayOfAddresses(fullyAssociative, TestGroup);
		System.out.println("\n\n");
		delay = readArrayOfAddresses(fullyAssociative, TestGroup);
		fullyAssociative.printCache();
	}
	
	public static int readArrayOfAddresses(Cache c, int[] address)
	{
		int delay = 0;
		for(int i = 0; i < address.length; i++)
		{
			delay += c.readAddress(address[i]);
		}
		return delay;
	}

}
