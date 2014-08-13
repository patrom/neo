package neo.data.harmony;

import java.util.Collections;
import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.harmony.pitchspace.BassOctavePitchSpace;
import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.TopOctavePitchSpace;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.Note;
import neo.data.note.Scale;

public class Harmony implements Comparable<Harmony>{
	
	protected int position;
	protected List<Note> notes;
	private PitchSpaceStrategy pitchSpaceStrategy;
	private Chord chord;
	private int length;
	private double positionWeight;
	
	public Harmony(int position, int length, List<Note> notes, PitchSpaceStrategy pitchSpaceStrategy) {
		this.position = position;
		this.length = length;
		this.notes = notes;
		this.pitchSpaceStrategy = pitchSpaceStrategy;
		toChord();
	}
	
	public int getPosition() {
		return position;
	}
	
	public List<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}
	
	private void toChord(){
		this.chord = new Chord();
		for (Note note : notes) {
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

	public double getChordWeight() {
		return chord.getWeight();
	}

	public Chord getChord() {
		return chord;
	}

	public int getLength() {
		return length;
	}
	
	public Note mutateNoteToPreviousPitchFromScale(Scale scale){
		int noteIndex = PseudoRandom.randInt(0, notes.size() - 1);
		Note note = notes.get(noteIndex);
		int newPitchClass = scale.pickPreviousPitchFromScale(note.getPitchClass());
		note.setPitchClass(newPitchClass);
		toChord();
		return note;
	}
	
	public void mutatePitchSpaceStrategy(){
		pitchSpaceStrategy.mutateOctaveHighestPitchClass();
		int i = PseudoRandom.randInt(0, 2);
		switch (i) {
		case 0:
			if (!(pitchSpaceStrategy instanceof UniformPitchSpace)) {
				this.pitchSpaceStrategy = new UniformPitchSpace(notes, pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		case 1:
			if (!(pitchSpaceStrategy instanceof BassOctavePitchSpace)) {
				this.pitchSpaceStrategy = new BassOctavePitchSpace(notes, pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		case 2:
			if (!(pitchSpaceStrategy instanceof TopOctavePitchSpace)) {
				this.pitchSpaceStrategy = new TopOctavePitchSpace(notes, pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		}
	}

	public double getPositionWeight() {
		return positionWeight;
	}

	public void setPositionWeight(double positionWeight) {
		for (Note notePos : notes) {
			notePos.setPositionWeight(positionWeight);
		}
		this.positionWeight = positionWeight;
	}
	
	public void transpose(int t){
		notes.stream().forEach(note -> note.setPitchClass((note.getPitchClass() + t) % 12));
		toChord();
	}

	@Override
	public int compareTo(Harmony harmony) {
		if (getPosition() < harmony.getPosition()) {
			return -1;
		} if (getPosition() > harmony.getPosition()) {
			return 1;
		} else {
			return 0;
		}
	}

}
