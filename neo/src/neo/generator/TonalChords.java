package neo.generator;

import java.util.ArrayList;
import java.util.List;

public class TonalChords {

	public static List<int[]> getTriads(){
		List<int[]> chords = new ArrayList<>();
		chords.add(new int[]{0, 4 ,7});
		chords.add(new int[]{2, 5 ,9});
		chords.add(new int[]{4, 7 ,11});
		chords.add(new int[]{5, 9 ,0});
		chords.add(new int[]{7, 11 ,2});
		chords.add(new int[]{9, 0 ,4});
		chords.add(new int[]{11, 4 ,5});
		return chords;
	}
	
	public static List<int[]> getSeventhChords(){
		List<int[]> chords = new ArrayList<>();
		chords.add(new int[]{0, 4 ,7, 11});
		chords.add(new int[]{2, 5 ,9, 0});
		chords.add(new int[]{4, 7 ,11, 2});
		chords.add(new int[]{5, 9 ,0, 4});
		chords.add(new int[]{7, 11 ,2, 5});
		chords.add(new int[]{9, 0 ,4, 7});
		chords.add(new int[]{11, 4 ,5 ,9});
		return chords;
	}
	
	public static List<int[]> getTriadsAndSeventhChords(){
		List<int[]> chords = new ArrayList<>();
		chords.addAll(getTriads());
		chords.addAll(getSeventhChords());
		return chords;
	}
}
