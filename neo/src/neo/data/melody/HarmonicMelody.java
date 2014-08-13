package neo.data.melody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import neo.data.harmony.Harmony;
import neo.data.note.Note;

public class HarmonicMelody implements Comparable<HarmonicMelody>{

	private List<Note> notes = new ArrayList<>();
	private int voice;
	private Harmony harmony;
	
	public HarmonicMelody(List<Note> notes, Harmony harmony, int voice) {
		this.notes = notes;
		this.voice = voice;
		this.harmony = harmony;
	}
	
	public HarmonicMelody(Note note, Harmony harmony, int voice) {
		this.notes.add(note);
		this.voice = voice;
		this.harmony = harmony;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public int getLength() {
		return harmony.getLength();
	}

	public int getVoice() {
		return voice;
	}

	public int getPosition() {
		return harmony.getPosition();
	}

	public Harmony getHarmony() {
		return harmony;
	}
	
	public void updateNotes(){
		Optional<Note> optionalNote = harmony.getNotes().stream().filter(note -> note.getVoice() == this.voice).findFirst();
		int octave = optionalNote.get().getOctave();
		notes.stream().forEach(note -> note.setPitch(note.getPitchClass() + (octave * 12)));
	}

	@Override
	public int compareTo(HarmonicMelody harmonicMelody) {
		if (getPosition() < harmonicMelody.getPosition()) {
			return -1;
		} if (getPosition() > harmonicMelody.getPosition()) {
			return 1;
		} else {
			return 0;
		}
	}
}
