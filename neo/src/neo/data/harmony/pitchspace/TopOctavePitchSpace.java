package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public class TopOctavePitchSpace extends PitchSpaceStrategy {

	public TopOctavePitchSpace(List<NotePos> notes, Integer[] octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
	}


	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
		for (int i = 1; i < size; i++) {
			NotePos lowestNote = notes.get(i);
			lowestNote.setPitch(lowestNote.getPitch() - 12);
		}
	}

}
