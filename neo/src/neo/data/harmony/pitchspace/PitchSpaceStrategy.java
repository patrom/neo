package neo.data.harmony.pitchspace;

import java.util.List;
import java.util.Random;
import java.util.Set;

import jmetal.util.PseudoRandom;
import neo.data.note.NotePos;

public abstract class PitchSpaceStrategy {

	protected List<NotePos> notes;
	protected Integer[] octaveHighestPitchClassRange;
	protected int octaveHighestPitchClass;
	protected int size;
	
	public PitchSpaceStrategy(List<NotePos> notes, Integer[] octaveHighestPitchClasses) {
		this.notes = notes;
		this.octaveHighestPitchClass = octaveHighestPitchClasses[0];
		this.octaveHighestPitchClassRange = octaveHighestPitchClasses;
		this.size = notes.size();
	}

	public abstract void translateToPitchSpace();
	
	public int getOctaveHighestPitchClass() {
		return octaveHighestPitchClass;
	}
	
	public void mutateOctaveHighestPitchClass(){
		this.octaveHighestPitchClass = PseudoRandom.randInt(octaveHighestPitchClassRange[0], octaveHighestPitchClassRange[octaveHighestPitchClassRange.length - 1]);
	}

	protected void setPitchClassFirstNote() {
		NotePos firstNote = notes.get(0);
		firstNote.setPitch(firstNote.getPitchClass() + (12 * octaveHighestPitchClass));
	}
	
	protected void setUniformPitchSpace(){
		setPitchClassFirstNote();
		for (int i = 1; i < size; i++) {
			NotePos prevNote = notes.get(i - 1);
			NotePos note = notes.get(i);
			int prevPc = prevNote.getPitchClass();
			int pc = note.getPitchClass();
			if (pc > prevPc) {
				note.setPitch(prevNote.getPitch() - (12 - (pc - prevPc)));
			} else {
				note.setPitch(prevNote.getPitch() - (prevPc - pc));
			}
		}
	}

	public Integer[] getOctaveHighestPitchClassRange() {
		return octaveHighestPitchClassRange;
	}

}
