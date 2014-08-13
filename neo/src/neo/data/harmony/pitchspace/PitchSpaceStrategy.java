package neo.data.harmony.pitchspace;

import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.note.Note;

public abstract class PitchSpaceStrategy {

	protected List<Note> notes;
	protected Integer[] octaveHighestPitchClassRange;
	protected int octaveHighestPitchClass;
	protected int size;
	
	public PitchSpaceStrategy(List<Note> notes, Integer[] octaveHighestPitchClasses) {
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
