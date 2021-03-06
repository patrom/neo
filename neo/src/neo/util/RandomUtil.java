package neo.util;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomUtil {
	
	private static Random random = new Random();
	
	public static <T> T getRandomFromList(List<T> list) {
		return list.get(randomInt(0, list.size()));
	}
	
	public static int getRandomFromIntArray(int[] array){
		return array[random(array.length)];
	}
	
	public static double[] getRandomFromDoubleArray(double[][] array){
		return array[random(array.length)];
	}
	
	public static int randomInt(int origin, int boundExclusive){
		return random.ints(origin, boundExclusive).findFirst().getAsInt();
	}
	
	public static int random(int size){
		return random.nextInt(size);
	}
	
	public static IntStream range(int size){
		int start = randomInt(0, size);
		int end = randomInt(start + 1, size + 1);
		return IntStream.range(start, end);
	}
	
	public static boolean toggleSelection(){
		return random.nextBoolean();
	}
	
}
