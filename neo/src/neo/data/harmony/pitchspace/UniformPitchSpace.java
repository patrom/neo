package neo.data.harmony.pitchspace;

import neo.data.note.NotePos;

public class UniformPitchSpace extends PitchSpaceStrategy {

	public UniformPitchSpace(int octaveHighestNote) {
		super(octaveHighestNote);
	}

	@Override
	public void translateToPitchSpace() {
		int size = notes.size();
		NotePos firstNote = notes.get(0);
		firstNote.setPitch(firstNote.getPitchClass() + (12 * octaveHighestNote));
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
