package neo.generator;

import static java.util.stream.Collectors.groupingBy;
import static neo.data.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import jmetal.util.PseudoRandom;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.HarmonyBuilder;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.data.note.NoteBuilder;
import neo.data.note.Scale;
import neo.evaluation.HarmonyProperties;
import neo.evaluation.MelodyProperties;
import neo.evaluation.MusicProperties;
import neo.instrument.KontaktLibPiano;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.print.ScoreUtilities;

public class Generator {
	
	private static Logger LOGGER = Logger.getLogger(Generator.class.getName());
	
	private Scale scale;
	private Map<Integer, Double> rhythmWeightValues;
	private int minimumLength;
	private int chordSize;
	private Integer[] octaveHighestNote;
	private List<HarmonyBuilder> harmonyBuilders;
	private MusicProperties musicProperties;
	
	public Generator(MusicProperties properties) {
		this.scale = properties.getScale();
		this.rhythmWeightValues = properties.getRhythmWeightValues();
		this.minimumLength = properties.getMinimumLength();
		this.chordSize = properties.getChordSize();
		this.octaveHighestNote = properties.getOctaveHighestPitchClass();
		this.harmonyBuilders = properties.getHarmonyBuilders();
		this.musicProperties = properties;
	}

	public Motive generateMotive() {
		Motive motive = new Motive(generateHarmonies());
		motive.setMusicProperties(musicProperties);
		return motive;
	}
	
	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder : harmonyBuilders) {
			List<Integer> chordPitchClasses = generatePitchClasses();
			List<Note> notes = generateNotes(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), chordPitchClasses);
			double totalWeight = calculatePositionWeight(harmonyBuilder.getPosition(), harmonyBuilder.getLength());
			Harmony harmony = harmonyBuilder
					.notes(notes)
					.positionWeight(totalWeight)
					.build();
			harmony.setPitchSpaceStrategy(harmony.new UniformPitchSpace(octaveHighestNote));
			harmonies.add(harmony);		
		}
		return harmonies;
	}
	
	public List<Melody> generateMelodies(List<Harmony> harmonies){
		List<Melody> melodies = new ArrayList<Melody>();
		int melodyIndex = 0;
		for (int voice = 0; voice < harmonies.size(); voice++) {
			Harmony harmony = harmonies.get(voice);
			List<Note> melodyNotes = new ArrayList<>();
			
			HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNotes, voice);
			harmony.addHarmonicMelody(harmonicMelody);
		}
		
		
		
		
//		Map<Integer, List<Note>> melodyMap = harmonies.stream()
//			.flatMap(harmony -> harmony.getNotes().stream())
//			.collect(groupingBy(note -> note.getVoice()));
//		for (Entry<Integer, List<Note>> entry: melodyMap.entrySet()) {
//			int voice = entry.getKey();
//			List<HarmonicMelody> HarmonicMelodies = new ArrayList<>();
//			if (melodyPropertiesMap.containsKey(voice)) {
//				int melodyIndex = 0;
//				List<MelodyProperties> melodyProperties = melodyPropertiesMap.get(voice);
//				MelodyProperties melodyProperty =  melodyProperties.get(melodyIndex);
//				for (Note harmonyNote: entry.getValue()) {
//					Harmony harmony = harmonyNote.getHarmony();
//					List<Note> melodyNotes = new ArrayList<>();
//					while ((melodyProperty.getPosition() - harmonyNote.getPosition()) < harmonyNote.getLength()) {
//						Note melodyNote = new Note(harmonyNote.getPitchClass(), voice , melodyProperty.getPosition(), melodyProperty.getLength());
//						melodyNote.setHarmony(harmony);
//						melodyNotes.add(melodyNote);
//						melodyIndex++;
//						if (melodyIndex < melodyProperties.size()) {//last melody note
//							melodyProperty = melodyProperties.get(melodyIndex);
//						} else {
//							break;
//						}
//					}
//					HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNotes, harmony, voice);
//					HarmonicMelodies.add(harmonicMelody);
//					harmony.addHarmonicMelody(harmonicMelody);
//				}
//			} else {
//				for (Note harmonyNote: entry.getValue()) {
//					Harmony harmony = harmonyNote.getHarmony();
//					Note melodyNote = new Note(harmonyNote.getPitchClass(), voice , harmonyNote.getPosition(), harmonyNote.getLength());
//					melodyNote.setHarmony(harmony);
//					HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNote, harmony, voice);
//					HarmonicMelodies.add(harmonicMelody);
//					harmony.addHarmonicMelody(harmonicMelody);
//				}
//			}
//			Melody melody = new Melody(HarmonicMelodies, voice);
//			melodies.add(melody);
//		}
		return melodies;
	}
	
//	public void updateMelodyToHarmony(List<Harmony> harmonies, Melody melody){
//		int melodyIndex = 0;
//		List<NotePos> melodyNotes = melody.getNotes();
//		NotePos melodyNote = melodyNotes.get(melodyIndex);
//		harmonyLoop:
//		for (int harmonyIndex = 0; harmonyIndex < harmonies.size(); harmonyIndex++) {
//			Harmony harmony = harmonies.get(harmonyIndex);
//			int pc = getHarmonyPitchClassForMelodyNote(melodyNote, harmony);
//			while ((melodyNote.getPosition() - harmony.getPosition()) < harmony.getLength()) {
//				melodyNote.setHarmony(harmony);
//				melodyNote.setPitchClass(pc);
//				melodyIndex++;
//				if (melodyIndex < melodyNotes.size()) {//last melody note
//					melodyNote = melodyNotes.get(melodyIndex);
//				} else {
//					break harmonyLoop;
//				}
//			}
//		}
//	}


	private List<Integer> generatePitchClasses() {
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
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 0).peek(h -> System.out.println(h.getNotes())).collect(Collectors.toList());
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 1).peek(h -> System.out.println(h.getNotes())).collect(Collectors.toList());
		harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 2).peek(h -> System.out.println(h.getNotes())).collect(Collectors.toList());
		//mutate
		Harmony harmony = harmonies.get(0);
		Note note = harmony.getNotes().get(1);
		int oldPC = note.getPitchClass();
		note.setPitchClass(11);
		harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		harmony.getHarmonicMelodies().stream().flatMap(h -> h.getNotes().stream()).filter(n -> n.getPitchClass() == oldPC)
			.forEach(n -> n.setPitchClass(11));
		harmony.getHarmonicMelodies().stream()	
			.peek(h -> System.out.println(h.getNotes())).collect(Collectors.toList());
		
		//find Non chord
		System.out.println(harmony.getNotes());
		List<Note> hnotes = harmony.getHarmonicMelodies().stream().flatMap(h -> h.getNotes().stream()).filter(n -> !harmony.getPitchClasses().contains(n.getPitchClass()))
			.collect(Collectors.toList());
		System.out.println(hnotes);
		
		// melody
		List<Note> melody = harmonies.stream().flatMap(h -> h.getHarmonicMelodies().stream()).filter(h -> h.getVoice() == 0).flatMap(h -> h.getNotes().stream()).sorted().collect(Collectors.toList());
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
