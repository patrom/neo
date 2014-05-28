package neo.data.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;

public class Examples {

	public static void main(String[] args) {
		List<Harmony> list = new ArrayList<>();
		list.add(getChord(12, 12, 11,2,7));
		list.stream().forEach(n -> System.out.println(n.getPosition()));
	
	}
	
	public static Harmony getChord (int position, int length, int... pitchClass) {
		List<NotePos> notes = new ArrayList<>();
		for (int i = 0; i < pitchClass.length; i++) {
			NotePos notePos = new NotePos(pitchClass[i] , i , position, length);
			notes.add(notePos);
		}
		Harmony noteList = new Harmony(position , length, notes, new UniformPitchSpace(notes, 6));
		return noteList;
	}
}
