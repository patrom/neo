package neo.data.harmony;

import java.util.List;

import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.note.NotePos;
import neo.objective.harmony.Chord;

public class Harmony {
	
	protected int position;
	protected List<NotePos> notes;
	private PitchSpaceStrategy pitchSpaceStrategy;
	private Chord chord;
	private int length;
	
	public Harmony(int position, int length, List<NotePos> notes, PitchSpaceStrategy pitchSpaceStrategy) {
		this.position = position;
		this.length = length;
		this.notes = notes;
		this.pitchSpaceStrategy = pitchSpaceStrategy;
		this.pitchSpaceStrategy.setNotes(notes);
		toChord();
	}
	
	public int getPosition() {
		return position;
	}
	
	public List<NotePos> getNotes() {
		return notes;
	}
	public void setNotes(List<NotePos> notes) {
		this.notes = notes;
	}
	
	private void toChord(){
		this.chord = new Chord();
		for (NotePos note : notes) {
			chord.addPitchClass(note.getPitchClass());
		}
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

	public double getWeight() {
		return chord.getWeight();
	}

	public Chord getChord() {
		return chord;
	}

	public int getLength() {
		return length;
	}
	
}
