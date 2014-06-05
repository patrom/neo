package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public class BassOctavePitchSpace extends PitchSpaceStrategy {

	public BassOctavePitchSpace(List<NotePos> notes, int octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		NotePos lowestNote = notes.get(size - 1);
		lowestNote.setPitch(lowestNote.getPitch() - 12);
	}

}
