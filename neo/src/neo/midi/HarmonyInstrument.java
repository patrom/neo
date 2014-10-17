package neo.midi;

import java.util.ArrayList;
import java.util.List;

import neo.model.note.Note;

public class HarmonyInstrument implements Comparable<HarmonyInstrument>{

	private List<Note> notes = new ArrayList<>();
	private int position;
	
	public void addNote(Note note){
		notes.add(note);
	}

	public List<Note> getNotes() {
		return notes;
	}
	
	
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public int compareTo(HarmonyInstrument harmonyInstrument) {
		if (getPosition() < harmonyInstrument.getPosition()) {
			return -1;
		} if (getPosition() > harmonyInstrument.getPosition()) {
			return 1;
		} else {
			return 0;
		}
	}
}
