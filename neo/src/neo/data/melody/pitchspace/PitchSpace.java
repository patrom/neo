package neo.data.melody.pitchspace;

import java.util.List;
import java.util.Random;

import jmetal.util.PseudoRandom;
import neo.data.note.Note;
import neo.util.RandomUtil;

public abstract class PitchSpace {

	protected Integer[] octaveHighestPitchClassRange;
	protected int octaveHighestPitchClass;
	protected int size;
	protected List<Note> notes;

	public PitchSpace(Integer[] octaveHighestPitchClassRange) {
		this.octaveHighestPitchClassRange = octaveHighestPitchClassRange;
		this.octaveHighestPitchClass = randomIntRange();
	}

	public abstract void translateToPitchSpace();
	
	public int getOctaveHighestPitchClass() {
		return octaveHighestPitchClass;
	}
	
	public void setNotes(List<Note> notes) {
		this.notes = notes;
		this.size = notes.size();
	}
	
	public void mutateOctaveHighestPitchClass(){
		this.octaveHighestPitchClass = randomIntRange();
	}

	private int randomIntRange() {
		return RandomUtil.randomInt(octaveHighestPitchClassRange[0], octaveHighestPitchClassRange[octaveHighestPitchClassRange.length - 1] + 1);
	}

	protected void setPitchClassFirstNote() {
		Note firstNote = notes.get(0);
		firstNote.setPitch(firstNote.getPitchClass() + (12 * octaveHighestPitchClass));
		firstNote.setOctave(octaveHighestPitchClass);
	}
	
	protected void setUniformPitchSpace(){
		setPitchClassFirstNote();
		for (int i = 1; i < size; i++) {
			Note prevNote = notes.get(i - 1);
			Note note = notes.get(i);
			int prevPc = prevNote.getPitchClass();
			int pc = note.getPitchClass();
			if (pc > prevPc) {
				note.setPitch(prevNote.getPitch() - (12 - (pc - prevPc)));
				note.setOctave(prevNote.getOctave() - 1);
			} else {
				note.setPitch(prevNote.getPitch() - (prevPc - pc));
				note.setOctave(prevNote.getOctave());
			}
		}
	}

	public Integer[] getOctaveHighestPitchClassRange() {
		return octaveHighestPitchClassRange;
	}

}
