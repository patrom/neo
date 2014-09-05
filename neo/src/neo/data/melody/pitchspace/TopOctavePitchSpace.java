package neo.data.melody.pitchspace;

import neo.data.note.Note;

public class TopOctavePitchSpace extends PitchSpace {
	
	public TopOctavePitchSpace(Integer[] octaveHighestPitchClassRange) {
		super(octaveHighestPitchClassRange);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		for (int i = 1; i < size; i++) {
			Note note = notes.get(i);
			note.setPitch(note.getPitch() - 12);
			note.setOctave(note.getOctave() - 1);
		}
	}

}
