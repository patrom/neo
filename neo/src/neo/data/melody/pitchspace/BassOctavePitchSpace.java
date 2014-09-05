package neo.data.melody.pitchspace;

import neo.data.note.Note;

public class BassOctavePitchSpace extends PitchSpace {

	public BassOctavePitchSpace(Integer[] octaveHighestPitchClassRange) {
		super(octaveHighestPitchClassRange);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		Note lowestNote = notes.get(size - 1);
		lowestNote.setPitch(lowestNote.getPitch() - 12);
		lowestNote.setOctave(lowestNote.getOctave() - 1);
	}

}
