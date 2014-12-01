package neo.model.melody;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;

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
		updateNotesForVoice(voice);
	}
	
	public HarmonicMelody(Note note, int voice, int position) {
		this.harmonyNote = note;
		this.melodyNotes.add(note.copy());
		this.voice = voice;
		this.position = position;
		updateNotesForVoice(voice);
	}
	
	private void updateNotesForVoice(int voice){
		this.melodyNotes.forEach(note -> note.setVoice(voice));
		this.harmonyNote.setVoice(voice);
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
	
	public void mutateHarmonyNoteToPreviousPitchFromScale(Scale scale){
		int oldPitchClass = harmonyNote.getPitchClass();
//		int newPitchClass = scale.pickPreviousPitchFromScale(oldPitchClass);
		int newPitchClass = scale.pickRandomPitchClass();
		updateMelodyNotes(oldPitchClass, newPitchClass);
		harmonyNote.setPitchClass(newPitchClass);
	}
	
	public void mutateMelodyNoteToHarmonyNote(int newPitchClass){
		if (melodyNotes.size() > 1) {
			List<Note> melodyNotesWithoutHarmonyNote = getMelodyNotesWithoutHarmonyNote();
			if (!melodyNotesWithoutHarmonyNote.isEmpty()) {
				randomUpdateNoteInList(newPitchClass, melodyNotesWithoutHarmonyNote);
			} else {//all are harmony notes
				randomUpdateNoteInList(newPitchClass, melodyNotes);
			}
		}
	}

	private void randomUpdateNoteInList(int newPitchClass, List<Note> notes) {
		Note melodyNote = RandomUtil.getRandomFromList(notes);
		melodyNote.setPitchClass(newPitchClass);
	}

	private List<Note> getMelodyNotesWithoutHarmonyNote() {
		List<Note> notes = melodyNotes.stream()
				.filter(note -> harmonyNote.getPitchClass() != note.getPitchClass())
				.collect(toList());
		return notes;
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
		if (melodyNotes.size() != nonChordNotes.size() + 1) {
			// at least 1 note should be a harmony note
			note = randomNote(melodyNotes);
			note.setPitchClass(newPitchClass);
		}  else if(!nonChordNotes.isEmpty()){
			note = randomNote(nonChordNotes);
			note.setPitchClass(newPitchClass);
		}
		
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

	public void setHarmonyNote(Note harmonyNote) {
		this.harmonyNote = harmonyNote;
	}
	
}
