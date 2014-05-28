package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public abstract class PitchSpaceStrategy {

	protected List<NotePos> notes;
	protected int octaveHighestPitchClass;
	
	public PitchSpaceStrategy(List<NotePos> notes, int octaveHighestPitchClass) {
		this.notes = notes;
		this.octaveHighestPitchClass = octaveHighestPitchClass;
	}

	public abstract void translateToPitchSpace();
	
	public int getOctaveHighestPitchClass() {
		return octaveHighestPitchClass;
	}

}
