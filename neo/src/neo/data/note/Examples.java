package neo.data.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Examples {

	public static void main(String[] args) {
		List<NoteList> list = new ArrayList<>();
		list.add(getChord(12, 11,2,7));
		list.stream().forEach(n -> System.out.println(n.getPosition()));
	
	}
	
	public static NoteList getChord (int position, int... pitchClass) {
		List<NotePos> list = new ArrayList<>();
		for (int i = 0; i < pitchClass.length; i++) {
			NotePos notePos = new NotePos(pitchClass[i] , i , position);
			list.add(notePos);
		}
		NoteList noteList = new NoteList(position , list);
		return noteList;
	}
}
