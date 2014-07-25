package neo.data.harmony;

import java.util.Collections;
import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.harmony.pitchspace.BassOctavePitchSpace;
import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;
import neo.data.note.Scale;

public class Harmony {
	
	protected int position;
	protected List<NotePos> notes;
	private PitchSpaceStrategy pitchSpaceStrategy;
	private Chord chord;
	private int length;
	private double positionWeight;
	
	public Harmony(int position, int length, List<NotePos> notes, PitchSpaceStrategy pitchSpaceStrategy) {
		this.position = position;
		this.length = length;
		this.notes = notes;
		this.pitchSpaceStrategy = pitchSpaceStrategy;
		toChord();
	}
	
	public int getPosition() {
		return position;
	}
	
	public List<NotePos> getNotes() {
		return Collections.unmodifiableList(notes);
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
	
	public void mutateNoteToPreviousPitchFromScale(Scale scale){
		int noteIndex = PseudoRandom.randInt(0, notes.size() - 1);
		NotePos note = notes.get(noteIndex);
		int newPitchClass = scale.pickPreviousPitchFromScale(note.getPitchClass());
		note.setPitchClass(newPitchClass);
		toChord();
	}
	
	public void mutatePitchSpaceStrategy(){
		int i = PseudoRandom.randInt(0, 1);
		switch (i) {
		case 0:
			if (!(pitchSpaceStrategy instanceof UniformPitchSpace)) {
				this.pitchSpaceStrategy = new UniformPitchSpace(notes, pitchSpaceStrategy.getOctaveHighestPitchClass());
			}
			break;
		case 1:
			if (!(pitchSpaceStrategy instanceof BassOctavePitchSpace)) {
				this.pitchSpaceStrategy = new BassOctavePitchSpace(notes, pitchSpaceStrategy.getOctaveHighestPitchClass());
			}
			break;
		}
	}

	public double getPositionWeight() {
		return positionWeight;
	}

	public void setPositionWeight(double positionWeight) {
		for (NotePos notePos : notes) {
			notePos.setPositionWeight(positionWeight);
		}
		this.positionWeight = positionWeight;
	}
	
	public void transpose(int t){
		notes.stream().forEach(note -> note.setPitchClass((note.getPitchClass() + t) % 12));
		toChord();
	}

}
