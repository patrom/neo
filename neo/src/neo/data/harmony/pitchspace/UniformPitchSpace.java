package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.Note;

public class UniformPitchSpace extends PitchSpaceStrategy {

	public UniformPitchSpace(List<Note> notes, Integer[] octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
	}

}
