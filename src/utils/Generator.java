package utils;

import java.util.HashSet;
import java.util.Random;


public class Generator {
	public static final String UPPER_SEQUENCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWER_SEQUENCE = UPPER_SEQUENCE.toLowerCase();

	private static Random random = new Random();
	
	public static float genRandomFloat(float from, float to) {
		return (random.nextFloat()*(to-from) + from);
	}
	
	public static String genRandomString(int lenFrom, int lenTo)
	{
		StringBuilder ranSeqBuilder = new StringBuilder(); 
		
		
		 //select random len from lenFrom to lenTo
		int ranLen = random.nextInt(lenTo - lenFrom + 1) + lenFrom;
		int i = 0;
		while (i < ranLen) {
			int ranPos = random.nextInt(LOWER_SEQUENCE.length());
			char ranChar = LOWER_SEQUENCE.charAt(ranPos);
			ranSeqBuilder.append(ranChar);
			i += 1;
		}
		
		return ranSeqBuilder.toString();
	}
	public static int genRanInt(int from, int to)
	{
		return (random.nextInt(to - from + 1) + from);
	}
	public static void main(String[] args) {
		//Test here
		int i =0;
		while (true) {
			float ranNum = genRandomFloat(100, 10000);
//			if (i++%1000000 == 0) System.out.println (ranNum);
			if (ranNum < 100) {
				System.out.println(ranNum);
				break;
			}
			if (ranNum > 10000) {
				System.out.println(ranNum);
				break;
			}
		}
	}
}
