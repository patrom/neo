package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.HarmonicMelodyBuilder;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.util.RandomUtil;

public abstract class Generator {

	protected List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
	protected int[][] positions;
	protected Map<Integer, Double> rhythmWeightValues;
	protected MusicProperties musicProperties;
	protected List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
	
	public Generator(int[][] positions, MusicProperties musicProperties) {
		this.positions = positions;
		this.musicProperties = musicProperties;
		generateRhythmWeights();
	}
	
	public abstract List<Harmony> generateHarmonies();
	
	public Motive generateMotive() {
		generateHarmonyBuilders();
		return new Motive(generateHarmonies(), musicProperties);
	}
	
	private void generateHarmonyBuilders() {
		for (int i = 0; i < positions.length - 1; i++) {
			harmonyBuilders.add(harmony().pos(positions[i][0]).len(positions[i + 1][0] - positions[i][0]));
		}
	}

//	protected HarmonicMelody getHarmonicMelodyForNote(Note harmonyNote){
//		Optional<HarmonicMelody> optional = getHarmonicMelodyForVoiceAndPosition(harmonyNote.getVoice(), harmonyNote.getPosition());
//		if (optional.isPresent()) {
//			return optional.get();
//		} else {
//			Note newNote = harmonyNote.copy();
//			newNote.setPositionWeight(calculatePositionWeight( harmonyNote.getPosition(),  harmonyNote.getLength()));
//			return new HarmonicMelody(newNote, harmonyNote.getVoice(), harmonyNote.getPosition());
//		}
//	}
	
	protected Optional<HarmonicMelody> getHarmonicMelodyForVoiceAndPosition(int voice, int position) {
		return harmonicMelodies.stream()
				.filter(harmonicMelody -> harmonicMelody.getVoice() == voice && harmonicMelody.getPosition() == position)
				.findFirst();
	}
	
	protected HarmonicMelody getHarmonicMelody(int position, int voice, int length, int pitchClass) {
		Optional<HarmonicMelody> optional = getHarmonicMelodyForVoiceAndPosition(voice, position);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			Note harmonyNote = new Note(pitchClass, voice , position, length);
			harmonyNote.setPositionWeight(calculatePositionWeight(harmonyNote.getPosition(),  harmonyNote.getLength()));
			return new HarmonicMelody(harmonyNote, harmonyNote.getVoice(), harmonyNote.getPosition());
		}
	}
	
	protected int[] expandChordToSize(int[] chord, int size) {
		int[] newChord = new int[size];
		for (int i = 0; i < chord.length; i++) {
			newChord[i] = chord[i];
		}
		for (int i = chord.length; i < size; i++) {
			int randomIndex = RandomUtil.random(chord.length);
			newChord[i] = chord[randomIndex];
		}
		return newChord;
	}
	
//	private HarmonicMelody copyHarmonicMelody(HarmonicMelody harmonicMelody) {
//		Note harmonyNote = harmonicMelody.getHarmonyNote();
//		List<Note> newNotes = copyNotes(harmonicMelody.getMelodyNotes());
//		//Set only first pitch class to harmony note, others random (repeated note problem)
//		newNotes.forEach(n -> n.setPitchClass(musicProperties.getScale().pickRandomPitchClass()));
//		newNotes.get(0).setPitchClass(harmonyNote.getPitchClass());
//		return new HarmonicMelody(harmonyNote.copy(), newNotes, harmonicMelody.getVoice(), harmonicMelody.getPosition());
//	}
	
//	private List<Note> copyNotes(List<Note> notesToCopy) {
//		List<Note> newNotes = new ArrayList<Note>();
//		int size = notesToCopy.size();
//		for (int i = 0; i < size; i++) {	
//			Note note = notesToCopy.get(i);
//			Note newNote = note.copy();
//			newNote.setPositionWeight(calculatePositionWeight(note.getPosition(), note.getLength()));
//			newNotes.add(newNote);
//		}
//		return newNotes;
//	}
	
//	protected List<HarmonicMelody> getHarmonicMelodies(List<Note> notes) {
//		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
//		for (Note note : notes) {
//			HarmonicMelody harmonicMelody = getHarmonicMelodyForNote(note);
//			harmonicMelodies.add(harmonicMelody);
//		}
//		return harmonicMelodies;
//	}
	
	
	
//	protected List<Integer> generatePitchClasses() {
//		IntStream.generate(new Scale(Scale.MAJOR_SCALE)::pickRandomPitchClass)
//			.limit(musicProperties.getChordSize());
//		List<Integer> chordPitchClasses = new ArrayList<>();
//		for (int j = 0; j < musicProperties.getChordSize(); j++) {
//			int pitchClass = musicProperties.getScale().pickRandomPitchClass();
//			chordPitchClasses.add(pitchClass);
//		}
//		return chordPitchClasses;
//	}
//
//	protected List<Note> generateNotes(int position, int length , List<Integer> chordPitchClasses) {
//		List<Note> notePositions = new ArrayList<>();
//		int voice = chordPitchClasses.size() - 1;
//		for (Integer pc : chordPitchClasses) {
//			Note notePos = new Note(pc, voice , position, length);
//			notePositions.add(notePos);
//			voice--;
//		}
//		return notePositions;
//	}

	protected double calculatePositionWeight(int position, int length) {
		double totalWeight = 0;
		for (int j = 0; j < length; j = j + musicProperties.getMinimumLength()) {
			totalWeight = totalWeight + rhythmWeightValues.get(position + j);
		}
		return totalWeight;
	}
	
	private void generateRhythmWeights() {
		rhythmWeightValues = RhythmWeight.generateRhythmWeight(positions.length - 1, musicProperties.getMeasureWeights());
	}
	
	public List<HarmonicMelody> generateHarmonicMelodiesForVoice(int[] harmonyPosition, int maxLength, int voice){
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		int endNote = 0;
		for (int i = 0; i < harmonyPosition.length - 2; i++) {
			HarmonicMelodyBuilder harmonicMelodyBuilder = harmonicMelody().voice(voice).pos(harmonyPosition[i]);
			List<Note> notes = new ArrayList<>();
			while (endNote < harmonyPosition[i + 1]) {
				int harmonyLength = harmonyPosition[i + 1] - endNote;
				int notePosition = 0;
				if (harmonyLength > 0) {
					notePosition = RandomUtil.randomInt(0, (harmonyLength/musicProperties.getMinimumLength()));
				}
				int pos = (notePosition * musicProperties.getMinimumLength()) + endNote;
				int l = notePosition + (harmonyPosition[i + 2])/musicProperties.getMinimumLength();
				if ( l < maxLength) {
					maxLength = l;
				}
				int noteLength = RandomUtil.randomInt(1, maxLength);
				int length = noteLength * musicProperties.getMinimumLength();
				endNote = pos + length;
				notes.add(NoteBuilder.note().pos(pos).len(length).build());
			}
			harmonicMelodies.add(harmonicMelodyBuilder.notes(notes).harmonyNote(NoteBuilder.note().build()).build());
		}
		return harmonicMelodies;
	}
	
	public void generateHarmonicMelodiesForVoice(int[][] harmonies, int melodyVoice) {
        for (int i = 0; i < harmonies.length - 1; i++) {
            if (harmonies[i].length > 1) {
                List<Note> notes = new ArrayList<>();
                for (int j = 1; j < harmonies[i].length - 1; j++) {
                    notes.add(note().voice(melodyVoice).pos(harmonies[i][j]).len(harmonies[i][j + 1] - harmonies[i][j]).build());
                }
                Note harmonyNote = note().pos(harmonies[i][0]).len(harmonies[i + 1][0] - harmonies[i][0]).build();
                HarmonicMelody harmonicMelody = harmonicMelody()
                		.harmonyNote(harmonyNote)
                		.voice(melodyVoice)
                		.pos(harmonies[i][0])
                		.notes(notes)
                		.build();
                harmonicMelodies.add(harmonicMelody);
            }
        }
	}

	public List<HarmonicMelody> generateRandomHarmonicMelody(int[] harmonyPosition, int maxLength, int voice){
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		List<Note> notes = new ArrayList<>();
		int endNote = 0;
		for (int i = 0; i < harmonyPosition.length - 2; i++) {
			HarmonicMelodyBuilder harmonicMelodyBuilder = harmonicMelody().voice(voice).pos(harmonyPosition[i]);
			while (endNote < harmonyPosition[i + 1]) {
				int harmonyLength = harmonyPosition[i + 1] - endNote;
				int notePosition = 0;
				if (harmonyLength > 0) {
					notePosition = RandomUtil.randomInt(0, (harmonyLength/musicProperties.getMinimumLength()));
				}
				int pos = (notePosition * musicProperties.getMinimumLength()) + endNote;
				int l = notePosition + (harmonyPosition[i + 2])/musicProperties.getMinimumLength();
				if (l < maxLength) {
					maxLength = l;
				}
				int noteLength = RandomUtil.randomInt(1, maxLength);
				int length = noteLength * musicProperties.getMinimumLength();
				endNote = pos + length;
				notes.add(NoteBuilder.note().pos(pos).len(length).build());
			}
			harmonicMelodies.add(harmonicMelodyBuilder.notes(notes).build());
		}
		return harmonicMelodies;
	}

}
