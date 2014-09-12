package neo.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.sound.midi.InvalidMidiDataException;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.HarmonyBuilder;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;
import neo.objective.melody.InnerMetricWeight;

public class Generator {
	
	private static Logger LOGGER = Logger.getLogger(Generator.class.getName());
	
	private Scale scale;
	private Map<Integer, Double> rhythmWeightValues;
	private int minimumLength;
	private int chordSize;
	private List<HarmonyBuilder> harmonyBuilders;
	private List<HarmonicMelody> harmonicMelodies;
	private MusicProperties musicProperties;
	
	public Generator(MusicProperties properties) {
		this.scale = properties.getScale();
		this.rhythmWeightValues = properties.getRhythmWeightValues();
		this.minimumLength = properties.getMinimumLength();
		this.chordSize = properties.getChordSize();
		this.harmonyBuilders = properties.getHarmonyBuilders();
		this.musicProperties = properties;
		this.harmonicMelodies = properties.getHarmonicMelodies();
	}

	public Motive generateMotive() {
		Motive motive = new Motive(generateHarmonies());
		motive.setMusicProperties(musicProperties);
		updateInnerMetricWeightMelodies(motive.getMelodies());
		updateInnerMetricWeightHarmonies(motive.getHarmonies());
		return motive;
	}

	private void updateInnerMetricWeightHarmonies(List<Harmony> harmonies) {
		int[] harmonicRhythm = extractHarmonicRhythm(harmonies);
		Map<Integer, Double> normalizedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(harmonicRhythm, minimumLength);
		for (Harmony harmony : harmonies) {
			Integer key = harmony.getPosition()/minimumLength;
			if (normalizedMap.containsKey(key)) {
				Double innerMetricValue = normalizedMap.get(key);
				harmony.setInnerMetricWeight(innerMetricValue);
			}
		}
	}

	private int[] extractHarmonicRhythm(List<Harmony> harmonies) {
		int[] rhythm = new int[harmonies.size()];
		for (int i = 0; i < rhythm.length; i++) {
			Harmony harmony = harmonies.get(i);
			rhythm[i] = harmony.getPosition();
		}
		return rhythm;
	}

	private void updateInnerMetricWeightMelodies(List<Melody> melodies) {
		for (Melody melody : melodies) {
			List<Note> notes = melody.getMelodieNotes();
			Map<Integer, Double> normalizedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(notes, minimumLength);
			for (Note note : notes) {
				Integer key = note.getPosition()/minimumLength;
				if (normalizedMap.containsKey(key)) {
					Double innerMetricValue = normalizedMap.get(key);
					note.setInnerMetricWeight(innerMetricValue);
				}
			}
		}
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
		IntStream.generate(new Scale(Scale.MAJOR_SCALE)::pickRandomFromScale)
			.limit(chordSize);
		List<Integer> chordPitchClasses = new ArrayList<>();
		for (int j = 0; j < chordSize; j++) {
			int pitchClass = scale.pickRandomFromScale();
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
		for (int j = 0; j < length; j = j + minimumLength) {
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
	
	public static void main(String[] args) throws InvalidMidiDataException {
		int chordSize = 3;
		Integer[] octave = {5};
		MusicProperties props = new MusicProperties();
		props.setScale(new Scale(Scale.MAJOR_SCALE));
		props.setOctaveHighestPitchClass(octave);
		props.setChordSize(chordSize);
		Generator generator = new Generator(props);
//		Motive motive = generator.generateMotive();
		List<Harmony> harmonies = generator.generateHarmonies();
//		harmonies.forEach(h ->  LOGGER.info(h.getChord().getChordType() + ", "));
//		harmonies.forEach(h -> 	LOGGER.info(h.getChord().getPitchClassMultiSet() + ", "));
//		harmonies.forEach(h ->  LOGGER.info(h.getChord().getPitchClassSet() + ", "));
		harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 0).peek(h -> System.out.println(h.getMelodyNotes())).collect(Collectors.toList());
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 1).peek(h -> System.out.println(h.getMelodyNotes())).collect(Collectors.toList());
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 2).peek(h -> System.out.println(h.getMelodyNotes())).collect(Collectors.toList());
		//mutate
		Harmony harmony = harmonies.get(0);
		Note note = harmony.getNotes().get(1);
		int oldPC = note.getPitchClass();
		note.setPitchClass(11);
		harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		harmony.getHarmonicMelodies().stream().flatMap(h -> h.getMelodyNotes().stream()).filter(n -> n.getPitchClass() == oldPC)
			.forEach(n -> n.setPitchClass(11));
		harmony.getHarmonicMelodies().stream()	
			.peek(h -> System.out.println(h.getMelodyNotes())).collect(Collectors.toList());
		
		//find Non chord
		System.out.println(harmony.getNotes());
		List<Note> hnotes = harmony.getHarmonicMelodies().stream().flatMap(h -> h.getMelodyNotes().stream()).filter(n -> !harmony.getPitchClasses().contains(n.getPitchClass()))
			.collect(Collectors.toList());
		System.out.println(hnotes);
		
		// melody
		List<Note> melody = harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 0).flatMap(h -> h.getMelodyNotes().stream()).sorted().collect(Collectors.toList());
		System.out.println(melody);
//		Score score = ScoreUtilities.createScoreMelodies(motive.getMelodies(), 60);
//		View.notate(score);
//		Write.midi(score, "midi/test.mid");
//		
//		List<neo.instrument.Instrument> ranges = new ArrayList<>();
////		ranges.add(new KontaktLibViolin(0, 1));
////		ranges.add(new KontaktLibViolin(1, 2));
////		ranges.add(new KontaktLibAltViolin(2, 2));
////		ranges.add(new KontaktLibCello(3, 3));
//		ranges.add(new KontaktLibPiano(0,0));
//		ranges.add(new KontaktLibPiano(1,0));
//		ranges.add(new KontaktLibPiano(2,0));
//		ranges.add(new KontaktLibPiano(3,0));
//		
//		Sequence seq = MidiDevicesUtil.createSequence(motive.getMelodies(), ranges);
//		float tempo = 80f;
//		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
		
	}
	
}
