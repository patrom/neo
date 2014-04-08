package neo.data.note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Motive {
	
	private List<NotePos> notePositions = new ArrayList<NotePos>();
	private int length;

	public Motive() {
		notePositions = new ArrayList<NotePos>();
	}
	
	public Motive(List<NotePos> notePositions, int length) {
		this.notePositions.addAll(notePositions);
		this.length = length;
	}

	public List<NotePos> getNotePositions() {
		return notePositions;
	}
	
	public List<NotePos> getNotePositionsWithoutRests() {
		List<NotePos> positions = new ArrayList<NotePos>();
		for (NotePos note : notePositions) {
			if (!note.isRest()) {
				positions.add(note);
			}
		}
		return positions;
	}
	
	public void addNote(NotePos notePos) {
		notePositions.add(notePos);
		Collections.sort(notePositions);
	}

}
