package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public abstract class PitchSpaceStrategy {

	protected List<NotePos> notes;
	protected int octaveHighestPitchClass;
	protected int size;
	
	public PitchSpaceStrategy(List<NotePos> notes, int octaveHighestPitchClass) {
		this.notes = notes;
		this.octaveHighestPitchClass = octaveHighestPitchClass;
		this.size = notes.size();
	}

	public abstract void translateToPitchSpace();
	
	public int getOctaveHighestPitchClass() {
		return octaveHighestPitchClass;
	}

	protected void setPitchClassFirstNote() {
		int size = notes.size();
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

}
