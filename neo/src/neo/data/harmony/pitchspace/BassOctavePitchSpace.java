package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public class BassOctavePitchSpace extends PitchSpaceStrategy {

	public BassOctavePitchSpace(List<NotePos> notes, Integer[] octaveHighestPitchClasses) {
		super(notes, octaveHighestPitchClasses);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		NotePos lowestNote = notes.get(size - 1);
		lowestNote.setPitch(lowestNote.getPitch() - 12);
	}

}
