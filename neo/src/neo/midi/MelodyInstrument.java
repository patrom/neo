package neo.midi;

import java.util.List;

import neo.data.note.Note;
import neo.instrument.Instrument;

public class MelodyInstrument {

	private int voice;
	private List<Note> notes;
	private Instrument instrument;
	
	public MelodyInstrument(List<Note> notes, int voice) {
		this.notes = notes;
		this.voice = voice;
	}
	
	public int getVoice() {
		return voice;
	}
	public void setVoice(int voice) {
		this.voice = voice;
	}
	public List<Note> getNotes() {
		return notes;
	}
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	
}
