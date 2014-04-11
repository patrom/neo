package neo.nsga;

import java.util.ArrayList;
import java.util.List;

import jm.music.data.Note;
import jmetal.base.Variable;
import neo.data.note.NoteList;

public abstract class MusicVariable extends Variable{
	//TODO configuration
	private static final int DYNAMIC = Note.DEFAULT_DYNAMIC;
	
	protected List<NoteList> melodies = new ArrayList<NoteList>();

	public abstract List<NoteList> getMelodies() ;

	public abstract void setMelodies(List<NoteList> melodies);
	
}
