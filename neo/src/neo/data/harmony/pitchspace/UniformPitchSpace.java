package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public class UniformPitchSpace extends PitchSpaceStrategy {

	public UniformPitchSpace(List<NotePos> notes, int octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
	}

	@Override
	public void translateToPitchSpace() {
		int size = notes.size();
		NotePos firstNote = notes.get(0);
		firstNote.setPitch(firstNote.getPitchClass() + (12 * octaveHighestPitchClass));
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
