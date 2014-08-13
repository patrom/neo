package neo.generator;

import static java.util.stream.Collectors.groupingBy;
import static neo.data.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;
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
	private List<HarmonyProperties> harmonyProperties;
	private Map<Integer, List<MelodyProperties>> melodyPropertiesMap;
	private MusicProperties musicProperties;
	
	public Generator(MusicProperties properties) {
		this.scale = properties.getScale();
		this.rhythmWeightValues = properties.getRhythmWeightValues();
		this.minimumLength = properties.getMinimumLength();
		this.chordSize = properties.getChordSize();
		this.octaveHighestNote = properties.getOctaveHighestPitchClass();
		this.harmonyProperties = properties.getHarmonyProperties();
		this.melodyPropertiesMap = properties.getMelodyPropertiesMap();
		this.musicProperties = properties;
	}

	public Motive generateMotive() {
		List<Harmony> harmonies = generateHarmonies();
		List<Melody> melodies = generateMelodies(harmonies);
		Motive motive = new Motive(harmonies, melodies);
		motive.setMusicProperties(musicProperties);
		return motive;
	}
	
	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyProperties harmonyProperties : harmonyProperties) {
			List<Integer> chordPitchClasses = generatePitchClasses();
			List<Note> notes = generateNotes(harmonyProperties.getPosition(), harmonyProperties.getLength(), chordPitchClasses);
			double totalWeight = calculatePositionWeight(harmonyProperties.getPosition(), harmonyProperties.getLength());
			Harmony harmony = harmony()
					.pos(harmonyProperties.getPosition())
					.len(harmonyProperties.getLength())
					.allNotes(notes)
					.pitchSpace(new UniformPitchSpace(notes, octaveHighestNote))
					.positionWeight(totalWeight)
					.build();
			notes.stream().forEach(note -> note.setHarmony(harmony));
			harmonies.add(harmony);		
		}
		return harmonies;
	}
	
	public List<Melody> generateMelodies(List<Harmony> harmonies){
		List<Melody> melodies = new ArrayList<Melody>();
		Map<Integer, List<Note>> melodyMap = harmonies.stream().flatMap(harmony -> harmony.getNotes().stream()).collect(groupingBy(note -> note.getVoice()));
		for (Entry<Integer, List<Note>> entry: melodyMap.entrySet()) {
			int voice = entry.getKey();
			List<HarmonicMelody> HarmonicMelodies = new ArrayList<>();
			if (melodyPropertiesMap.containsKey(voice)) {
				int melodyIndex = 0;
				List<MelodyProperties> melodyProperties = melodyPropertiesMap.get(voice);
				MelodyProperties melodyProperty =  melodyProperties.get(melodyIndex);
				for (Note harmonyNote: entry.getValue()) {
					List<Note> melodyNotes = new ArrayList<>();
					while ((melodyProperty.getPosition() - harmonyNote.getPosition()) < harmonyNote.getLength()) {
						Note melodyNote = new Note(harmonyNote.getPitchClass(), voice , melodyProperty.getPosition(), melodyProperty.getLength());
						melodyNotes.add(melodyNote);
						melodyIndex++;
						if (melodyIndex < melodyProperties.size()) {//last melody note
							melodyProperty = melodyProperties.get(melodyIndex);
						} else {
							break;
						}
					}
					HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNotes, harmonyNote.getHarmony(), voice);
					HarmonicMelodies.add(harmonicMelody);
				}
			} else {
				for (Note harmonyNote: entry.getValue()) {
					Note melodyNote = new Note(harmonyNote.getPitchClass(), voice , harmonyNote.getPosition(), harmonyNote.getLength());
					HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNote, harmonyNote.getHarmony(), voice);
					HarmonicMelodies.add(harmonicMelody);
				}
			}
			Melody melody = new Melody(HarmonicMelodies, voice);
			melodies.add(melody);
		}
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

	private int getHarmonyPitchClassForMelodyNote(Note melodyNote,
			Harmony harmony) {
		List<Note> notes = harmony.getNotes();
		for (Note harmonyNote : notes) {
			if (harmonyNote.getVoice() == melodyNote.getVoice()) {
				return harmonyNote.getPitchClass();
			}
		}
		throw new IllegalStateException("Harmony should contain melody pitch class");
	}

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
		int chordSize = 4;
		Integer[] octave = {5};
		MusicProperties props = new MusicProperties();
		props.setScale(new Scale(Scale.MAJOR_SCALE));
		props.setOctaveHighestPitchClass(octave);
		props.setChordSize(chordSize);
		Generator generator = new Generator(props);
		Motive motive = generator.generateMotive();
		List<Harmony> harmonies = motive.getHarmonies();
		harmonies.forEach(h ->  LOGGER.info(h.getChord().getChordType() + ", "));
		harmonies.forEach(h -> 	LOGGER.info(h.getChord().getPitchClassMultiSet() + ", "));
		harmonies.forEach(h ->  LOGGER.info(h.getChord().getPitchClassSet() + ", "));
		harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		Score score = ScoreUtilities.createScoreMelodies(motive.getMelodies(), 60);
		View.notate(score);
		Write.midi(score, "midi/test.mid");
		
		List<neo.instrument.Instrument> ranges = new ArrayList<>();
//		ranges.add(new KontaktLibViolin(0, 1));
//		ranges.add(new KontaktLibViolin(1, 2));
//		ranges.add(new KontaktLibAltViolin(2, 2));
//		ranges.add(new KontaktLibCello(3, 3));
		ranges.add(new KontaktLibPiano(0,0));
		ranges.add(new KontaktLibPiano(1,0));
		ranges.add(new KontaktLibPiano(2,0));
		ranges.add(new KontaktLibPiano(3,0));
		
		Sequence seq = MidiDevicesUtil.createSequence(motive.getMelodies(), ranges);
		float tempo = 80f;
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
		
	}
	
}
