package neo.data.harmony;

import java.util.List;

import neo.data.note.NotePos;
import neo.objective.harmony.Chord;

public abstract class Harmony {
	
	protected int position;
	protected List<NotePos> notes;
	protected int octaveHighestNote;
	
	public Harmony(int position, List<NotePos> notes, int octaveHighestNote) {
		this.position = position;
		this.notes = notes;
		this.octaveHighestNote = octaveHighestNote;
	}
	
	public int position(){
		return position;
	}
	
	public int getPosition() {
		return position;
	}
	
	public int getOctaveHighestNote() {
		return octaveHighestNote;
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
	
	public abstract void translateToPitchSpace();
	
}
