package neo.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;
import neo.data.note.Scale;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.MusicProperties;
import neo.instrument.KontaktLibPiano;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.print.ScoreUtilities;

public class Generator {
	
	private static Logger LOGGER = Logger.getLogger(Generator.class.getName());
	
	private Scale scale;
	private int[] rhythmGeneratorTemplate;
	private Map<Integer, Double> rhythmWeightValues;
	private int minimumLength;
	private int chordSize;
	private int octaveHighestNote;
	
	public Generator(MusicProperties properties) {
		this.scale = properties.getScale();
		this.rhythmGeneratorTemplate = properties.getRhythmGeneratorTemplate();
		this.rhythmWeightValues = properties.getRhythmWeightValues();
		this.minimumLength = properties.getMinimumLength();
		this.chordSize = properties.getChordSize();
		this.octaveHighestNote = properties.getOctaveHighestPitchClass();
	}

	public Motive generateMotive() {
		List<Harmony> harmonies = new ArrayList<>();
		for (int i = 0; i < rhythmGeneratorTemplate.length - 1; i++) {	
			List<Integer> chordPitchClasses = generatePitchClasses();
			int length = rhythmGeneratorTemplate[i+1] - rhythmGeneratorTemplate[i];
			List<NotePos> notes = generateNotes(rhythmGeneratorTemplate[i], length, chordPitchClasses);
			Harmony harmony = new Harmony(rhythmGeneratorTemplate[i], length, 
					notes, new UniformPitchSpace(notes, octaveHighestNote));
			
			double totalWeight = calculatePositionWeight(harmony);
			harmony.setPositionWeight(totalWeight);
			harmonies.add(harmony);		
		}
		return new Motive(harmonies);
	}

	private List<Integer> generatePitchClasses() {
		List<Integer> chordPitchClasses = new ArrayList<>();
		for (int j = 0; j < chordSize; j++) {
			int pitchClass = scale.pickRandomFromScale();
			chordPitchClasses.add(pitchClass);
		}
		return chordPitchClasses;
	}

	private List<NotePos> generateNotes(int position, int length , List<Integer> chordPitchClasses) {
		List<NotePos> notePositions = new ArrayList<>();
		int voice = chordPitchClasses.size() - 1;
		for (Integer pc : chordPitchClasses) {
			NotePos notePos = new NotePos(pc, voice , position, length);
			notePositions.add(notePos);
			voice--;
		}
		return notePositions;
	}

	protected double calculatePositionWeight(Harmony harmony) {
		double totalWeight = 0;
		for (int j = 0; j < harmony.getLength(); j = j + minimumLength) {
			totalWeight = totalWeight + rhythmWeightValues.get(harmony.getPosition() + j);
		}
		return totalWeight;
	}
	
	
	public static void main(String[] args) throws InvalidMidiDataException {
		int chordSize = 4;
		int octave = 6;
		int[] rhythmGeneratorTemplate = {0,12,18,24};
		MusicProperties props = new MusicProperties();
		props.setScale(new Scale(Scale.MAJOR_SCALE));
		props.setRhythmGeneratorTemplate(rhythmGeneratorTemplate);
		props.setOctaveHighestPitchClass(octave);
		props.setChordSize(chordSize);
		Generator generator = new Generator(props);
		Motive motive = generator.generateMotive();
		List<Harmony> harmonies = motive.getHarmonies();
		harmonies.forEach(h ->  LOGGER.info(h.getChord().getChordType() + ", "));
		harmonies.forEach(h -> 	LOGGER.info(h.getChord().getPitchClassMultiSet() + ", "));
		harmonies.forEach(h ->  LOGGER.info(h.getChord().getPitchClassSet() + ", "));
		harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		Score score = ScoreUtilities.createScoreMelodies(motive.getMelodies());
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
