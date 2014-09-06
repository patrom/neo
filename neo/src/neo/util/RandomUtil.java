package neo.util;

import java.util.Random;

public class RandomUtil {
	
	private static Random random = new Random();
	
	public static int randomInt(int origin, int boundExclusive){
		return random.ints(origin, boundExclusive).findFirst().getAsInt();
	}
}
