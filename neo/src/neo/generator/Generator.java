package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.HarmonicMelodyBuilder;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.model.note.Scale;
import neo.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Generator {
	
	private static Logger LOGGER = Logger.getLogger(Generator.class.getName());
	
	@Autowired
	private MusicProperties musicProperties;
	
	private List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
	private Map<Integer, Double> rhythmWeightValues;
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();

	public Motive generateMotive() {
		Motive motive = new Motive(generateHarmonies());
		motive.setMusicProperties(musicProperties);
		return motive;
	}

	public void generateRhythmWeights(int bars, double[] measureWeights) {
		rhythmWeightValues = RhythmWeight.generateRhythmWeight(bars, measureWeights);
	}
	
	private HarmonicMelody getHarmonicMelodyForNote(Note harmonyNote){
		Optional<HarmonicMelody> optional = harmonicMelodies.stream()
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
		for (HarmonyBuilder harmonyBuilder : harmonyBuilders) {
			List<Integer> chordPitchClasses = generatePitchClasses();
			List<Note> notes = generateNotes(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), chordPitchClasses);
			List<HarmonicMelody> harmonicMelodies = getHarmonicMelodies(notes);
			Harmony harmony = new Harmony(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), harmonicMelodies, musicProperties.getOctaveHighestPitchClassRange());
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
			totalWeight = totalWeight + rhythmWeightValues.get(position + j);
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
	
//	private void prepareHarmonies() {
//		int[][] harmonies = musicProperties.getHarmonies();
//		generateHarmonicMelodiesForVoice(harmonies, 3);
//        harmonyBuilders = generateHarmonyBuilders(harmonies);
//        rhythmWeightValues = RhythmWeight.generateRhythmWeight(harmonies.length - 1, musicProperties.getMeasureWeights());
//    }

	public void generateHarmonicMelodiesForVoice(int[][] harmonies, int melodyVoice) {
        for (int i = 0; i < harmonies.length; i++) {
            if (harmonies[i].length > 1) {
                List<Note> notes = new ArrayList<>();
                for (int j = 1; j < harmonies[i].length - 1; j++) {
                    notes.add(note().voice(melodyVoice).pos(harmonies[i][j]).len(harmonies[i][j + 1] - harmonies[i][j]).build());
                }
                harmonicMelodies.add(harmonicMelody()
                		.harmonyNote(note().build())
                		.voice(melodyVoice)
                		.pos(harmonies[i][0])
                		.notes(notes)
                		.build());
            }
        }
	}

	
	public void generateHarmonyBuilders(int[][] positions){
		for (int i = 0; i < positions.length - 1; i++) {
			harmonyBuilders.add(harmony().pos(positions[i][0]).len(positions[i + 1][0] - positions[i][0]));
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
	
	public List<HarmonyBuilder> getHarmonyBuilders() {
		return harmonyBuilders;
	}

	public void setHarmonyBuilders(List<HarmonyBuilder> harmonyBuilders) {
		this.harmonyBuilders = harmonyBuilders;
	}

	public Map<Integer, Double> getRhythmWeightValues() {
		return rhythmWeightValues;
	}

	public void setRhythmWeightValues(Map<Integer, Double> rhythmWeightValues) {
		this.rhythmWeightValues = rhythmWeightValues;
	}

	public List<HarmonicMelody> getHarmonicMelodies() {
		return harmonicMelodies;
	}

	public void setHarmonicMelodies(List<HarmonicMelody> harmonicMelodies) {
		this.harmonicMelodies = harmonicMelodies;
	}

}
