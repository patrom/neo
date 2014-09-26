package neo.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.model.note.Scale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Generator {
	
	private static Logger LOGGER = Logger.getLogger(Generator.class.getName());
	
	@Autowired
	private MusicProperties musicProperties;

	public Motive generateMotive() {
		Motive motive = new Motive(generateHarmonies());
		motive.setMusicProperties(musicProperties);
		return motive;
	}
	
	private HarmonicMelody getHarmonicMelodyForNote(Note harmonyNote){
		Optional<HarmonicMelody> optional = musicProperties.getHarmonicMelodies().stream()
				.filter(harmonicMelody -> harmonicMelody.getVoice() == harmonyNote.getVoice() && harmonicMelody.getPosition() == harmonyNote.getPosition())
				.findFirst();
		if (optional.isPresent()) {
			return copyHarmonicMelody(optional.get(), harmonyNote);
		} else {
			Note newNote = harmonyNote.copy();
			newNote.setPositionWeight(calculatePositionWeight( harmonyNote.getPosition(),  harmonyNote.getLength()));
			return new HarmonicMelody(newNote, harmonyNote.getVoice(), harmonyNote.getPosition());
		}
	}

	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder : musicProperties.getHarmonyBuilders()) {
			List<Integer> chordPitchClasses = generatePitchClasses();
			List<Note> notes = generateNotes(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), chordPitchClasses);
			List<HarmonicMelody> harmonicMelodies = getHarmonicMelodies(notes);
			Harmony harmony = new Harmony(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), harmonicMelodies);
			double totalWeight = calculatePositionWeight(harmonyBuilder.getPosition(), harmonyBuilder.getLength());
			harmony.setPositionWeight(totalWeight);
			harmonies.add(harmony);		
		}
		return harmonies;
	}

	private List<HarmonicMelody> getHarmonicMelodies(List<Note> notes) {
		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
		for (Note note : notes) {
			HarmonicMelody harmonicMelody = getHarmonicMelodyForNote(note);
			harmonicMelodies.add(harmonicMelody);
		}
		return harmonicMelodies;
	}
	
	private List<Integer> generatePitchClasses() {
		IntStream.generate(new Scale(Scale.MAJOR_SCALE)::pickRandomPitchClass)
			.limit(musicProperties.getChordSize());
		List<Integer> chordPitchClasses = new ArrayList<>();
		for (int j = 0; j < musicProperties.getChordSize(); j++) {
			int pitchClass = musicProperties.getScale().pickRandomPitchClass();
			chordPitchClasses.add(pitchClass);
		}
		return chordPitchClasses;
	}

	private List<Note> generateNotes(int position, int length , List<Integer> chordPitchClasses) {
		List<Note> notePositions = new ArrayList<>();
		int voice = chordPitchClasses.size() - 1;
		for (Integer pc : chordPitchClasses) {
			Note notePos = new Note(pc, voice , position, length);
			notePositions.add(notePos);
			voice--;
		}
		return notePositions;
	}

	protected double calculatePositionWeight(int position, int length) {
		double totalWeight = 0;
		for (int j = 0; j < length; j = j + musicProperties.getMinimumLength()) {
			totalWeight = totalWeight + musicProperties.getRhythmWeightValues().get(position + j);
		}
		return totalWeight;
	}
	
	private HarmonicMelody copyHarmonicMelody(HarmonicMelody harmonicMelody, Note note) {
		List<Note> newNotes = copyNotes(harmonicMelody.getMelodyNotes());
		newNotes.forEach(n -> n.setPitchClass(note.getPitchClass()));
		return new HarmonicMelody(note.copy(), newNotes, harmonicMelody.getVoice(), harmonicMelody.getPosition());
	}
	
	private List<Note> copyNotes(List<Note> notesToCopy) {
		List<Note> newNotes = new ArrayList<Note>();
		int size = notesToCopy.size();
		for (int i = 0; i < size; i++) {	
			Note note = notesToCopy.get(i);
			Note newNote = note.copy();
			newNote.setPositionWeight(calculatePositionWeight(note.getPosition(), note.getLength()));
			newNotes.add(newNote);
		}
		return newNotes;
	}
	
}
