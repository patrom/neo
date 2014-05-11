package neo.data.harmony.pitchspace;

import java.util.List;

import neo.data.note.NotePos;

public abstract class PitchSpaceStrategy {

	protected List<NotePos> notes;
	protected int octaveHighestNote;
	
	public PitchSpaceStrategy(int octaveHighestNote) {
		this.octaveHighestNote = octaveHighestNote;
	}

	public abstract void translateToPitchSpace();

	public void setNotes(List<NotePos> notes) {
		this.notes = notes;
	}
}
