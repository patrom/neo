package neo.model.melody.pitchspace;

import neo.model.note.Note;

public class MiddleOctavePitchSpace extends PitchSpace {
	
	public MiddleOctavePitchSpace(Integer[] octaveHighestPitchClassRange) {
		super(octaveHighestPitchClassRange);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		for (int i = 2; i < size; i++) {
			Note note = notes.get(i);
			note.setPitch(note.getPitch() - 12);
			note.setOctave(note.getOctave() - 1);
		}
	}

}
