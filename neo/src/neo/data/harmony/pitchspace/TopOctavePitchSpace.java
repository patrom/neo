package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.Note;

public class TopOctavePitchSpace extends PitchSpaceStrategy {

	public TopOctavePitchSpace(List<Note> notes, Integer[] octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
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
