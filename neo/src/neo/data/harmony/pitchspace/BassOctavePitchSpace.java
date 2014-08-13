package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.Note;

public class BassOctavePitchSpace extends PitchSpaceStrategy {

	public BassOctavePitchSpace(List<Note> notes, Integer[] octaveHighestPitchClasses) {
		super(notes, octaveHighestPitchClasses);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		Note lowestNote = notes.get(size - 1);
		lowestNote.setPitch(lowestNote.getPitch() - 12);
		lowestNote.setOctave(lowestNote.getOctave() - 1);
	}

}
