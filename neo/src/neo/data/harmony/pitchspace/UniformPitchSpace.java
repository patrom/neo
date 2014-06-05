package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public class UniformPitchSpace extends PitchSpaceStrategy {

	public UniformPitchSpace(List<NotePos> notes, int octaveHighestPitchClass) {
		super(notes, octaveHighestPitchClass);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
	}

}
