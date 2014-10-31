package neo.util;

import java.util.List;
import java.util.Random;

public class RandomUtil {
	
	private static Random random = new Random();
	
	public static <T> T getRandomFromList(List<T> list) {
		return list.get(randomInt(0, list.size()));
	}
	
	public static int randomInt(int origin, int boundExclusive){
		return random.ints(origin, boundExclusive).findFirst().getAsInt();
	}
	
}
