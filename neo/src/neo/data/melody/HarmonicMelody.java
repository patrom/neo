package neo.data.melody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import neo.data.harmony.Harmony;
import neo.data.note.Note;

public class HarmonicMelody {

	private List<Note> notes = new ArrayList<>();
	private int voice;
	
	public HarmonicMelody(List<Note> notes, int voice) {
		this.notes = notes;
		this.voice = voice;
	}
	
	public HarmonicMelody(Note note, int voice) {
		this.notes.add(note);
		this.voice = voice;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public int getVoice() {
		return voice;
	}
	
}
