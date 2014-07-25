package neo.data.melody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.data.note.NotePos;


public class Melody {
	
	private List<NotePos> notePositions = new ArrayList<NotePos>();
	private int length;

	public Melody(List<NotePos> notePositions) {
		this.notePositions.addAll(notePositions);
	}
	
	public Melody(List<NotePos> notePositions, int length) {
		this.notePositions.addAll(notePositions);
		this.length = length;
	}

	public List<NotePos> getNotes() {
		return notePositions;
	}
	
	public List<NotePos> getNotesWithoutRests() {
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

	public int getLength() {
		return length;
	}

}
