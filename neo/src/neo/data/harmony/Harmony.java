package neo.data.harmony;

import java.util.List;

import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.note.NotePos;
import neo.objective.harmony.Chord;

public class Harmony {
	
	protected int position;
	protected List<NotePos> notes;
	private PitchSpaceStrategy pitchSpaceStrategy;
	
	public Harmony(int position, List<NotePos> notes, PitchSpaceStrategy pitchSpaceStrategy) {
		this.position = position;
		this.notes = notes;
		this.pitchSpaceStrategy = pitchSpaceStrategy;
		this.pitchSpaceStrategy.setNotes(notes);
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

	public void translateToPitchSpace() {
		pitchSpaceStrategy.translateToPitchSpace();
	}

	public PitchSpaceStrategy getPitchSpaceStrategy() {
		return pitchSpaceStrategy;
	}
	
}
