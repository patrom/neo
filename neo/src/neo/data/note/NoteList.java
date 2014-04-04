package neo.data.note;

import java.util.ArrayList;
import java.util.List;

import neo.objective.harmony.Chord;

public class NoteList {
	
	private int position;
	private List<NotePos> notes;
	
	public NoteList(int position, List<NotePos> notes) {
		super();
		this.position = position;
		this.notes = notes;
	}
	
	public int position(){
		return position;
	}
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
	public List<NotePos> getNotes() {
		return notes;
	}
	public void setNotes(List<NotePos> notes) {
		this.notes = notes;
	}
	
	public Chord toChord(){
		Chord chord = new Chord();
		for (NotePos note : notes) {
			chord.addPitchClass(note.getPitchClass());
		}
		return chord;
	}

	public double getBeat(int divider) {
		return Math.floor(position/divider);
	}
	
//	public List<NotePos> getAllNotes(){
//		List<NotePos> allNotes = new ArrayList<>();
//		for (NotePos notePos : notes) {
//			allNotes.add(notePos);
//		}	
//		return allNotes;
//	}
	
}
