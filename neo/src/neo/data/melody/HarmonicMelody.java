package neo.data.melody;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import jmetal.util.PseudoRandom;
import neo.data.note.Note;

public class HarmonicMelody {

	private List<Note> melodyNotes = new ArrayList<>();
	private int voice;
	private int position;
	private Note harmonyNote;
	private Random random = new Random();
	
	public HarmonicMelody(Note harmonyNote, List<Note> melodyNotes, int voice, int position) {
		this.harmonyNote = harmonyNote;
		this.melodyNotes = melodyNotes;
		this.voice = voice;
		this.position = position;
	}
	
	public HarmonicMelody(Note note, int voice, int position) {
		this.harmonyNote = note;
		this.melodyNotes.add(note.copy());
		this.voice = voice;
		this.position = position;
	}

	public List<Note> getMelodyNotes() {
		return melodyNotes;
	}

	public int getVoice() {
		return voice;
	}

	public int getPosition() {
		return position;
	}

	public Note getHarmonyNote() {
		return harmonyNote;
	}
	
	public List<Note> getNonChordNotes(){
		return melodyNotes.stream()
			.filter(note -> note.getPitchClass() != harmonyNote.getPitchClass())
			.collect(toList());
	}
	
	public List<Note> getChordNotes(){
		return melodyNotes.stream()
			.filter(note -> note.getPitchClass() == harmonyNote.getPitchClass())
			.collect(toList());
	}
	
	public void updateMelodyNotes(int oldPitchClass, int newPitchClass){
		Consumer<Note> updatePitchClass = (Note note) -> note.setPitchClass(newPitchClass);
		melodyNotes.stream()
			.filter(n -> n.getPitchClass() == oldPitchClass)
			.forEach(updatePitchClass);
	}
	
	public void randomUpdateMelodyNotes(int newPitchClass){
		List<Note> nonChordNotes = getNonChordNotes();
		Note note = null;
		if (!nonChordNotes.isEmpty()) {
			note = randomNote(nonChordNotes);
		} else {
			//change 1 chord note
			note = randomNote(melodyNotes);
		}
		note.setPitchClass(newPitchClass);
	}
	
	public void updateMelodyPitchesToHarmonyPitch(){
		melodyNotes.forEach(melodyNote -> {
			melodyNote.setOctave(harmonyNote.getOctave());
			melodyNote.setPitch((harmonyNote.getOctave() * 12) + melodyNote.getPitchClass());
		});
	}
	
	private Note randomNote(List<Note> notes) {
		int indexNote = random.ints(0, notes.size()).findFirst().getAsInt();
		return notes.get(indexNote);
	}
	
}
