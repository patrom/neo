package neo.data.melody;

import java.util.List;

import neo.data.note.Note;


public class Melody{
	
	private int voice;
	private List<Note> notes;

	public Melody(List<Note> notes, int voice) {
		this.notes = notes;
		this.voice = voice;
	}
	
	public List<Note> getNotes() {
		return notes;
	}
	
	public int getVoice() {
		return voice;
	}

}
