package utils;

import java.util.HashSet;
import java.util.Random;


public class Generator {
	public static final String UPPER_SEQUENCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LOWER_SEQUENCE = UPPER_SEQUENCE.toLowerCase();
	private static int MYPAGE_ID_INDENTIFIER = 1;
	private static int FRIENDREL_ID_INDENTIFIER = 1;
	private static int ACCESS_ID_INDENTIFIER = 1;
	private static Random random = new Random();
	
	public static int genMyPageId()
	{
		int Id =  MYPAGE_ID_INDENTIFIER;
		MYPAGE_ID_INDENTIFIER++;
		return Id;
	}
	
	public static int genFriendRelId()
	{
		int Id =  FRIENDREL_ID_INDENTIFIER;
		FRIENDREL_ID_INDENTIFIER++;
		return Id;
	}
	
	public static int genAccessId()
	{
		int Id =  ACCESS_ID_INDENTIFIER;
		ACCESS_ID_INDENTIFIER++;
		return Id;
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
		String s = genRandomString(10,20);
		System.out.println(s + 
				"  --> " + s.length());
		while (true) {
			int ranNum = genRanInt(10, 20);
			if (ranNum == 10) {
				System.out.println(ranNum);
				break;
			}
			if (ranNum == 20) {
				System.out.println(ranNum);
				break;
			}
		}
	}
}
