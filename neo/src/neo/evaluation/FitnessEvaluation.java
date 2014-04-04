package neo.evaluation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import jm.music.data.Note;
import neo.data.note.Examples;
import neo.data.note.Motive;
import neo.data.note.NoteList;
import neo.data.note.NotePos;
import neo.midi.MidiParser;
import neo.objective.InnerMetricWeight;
import neo.objective.harmony.Chord;
import neo.objective.melody.MelodicFunctions;
import neo.objective.voiceleading.VoiceLeading;
import neo.objective.voiceleading.VoiceLeadingSize;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class FitnessEvaluation {

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluation.class.getName());
	
	private static final int DYNAMIC_FACTOR = 3;
	private static final double DYNAMIC = Note.DEFAULT_DYNAMIC;
	private static final int DEFAULT_NOTE_ON_VALUE = 10;//note on accent

	private static final double DEFAULT_REGISTER_VALUE = 1.0;//lowest interval C(48)-E(52)

	private static final int UPPER_LIMIT_PITCH = 84;
	//constants
	private final int REST = Integer.MIN_VALUE;
	
	//input parameters
	private int rhythmTemplateValue;
	private int innerMetricFactor;
	private int numerator = 4;

	private MusicProperties properties;

	public MusicProperties getProperties() {
		return properties;
	}

	public FitnessEvaluation(MusicProperties properties) {
		this.properties = properties;
		this.numerator = properties.getNumerator();
		this.rhythmTemplateValue = properties.getRhythmTemplateValue();
	}
	
	public FitnessEvaluation() {
	}

	public static void main(String[] args) {
		List<NoteList> list = new ArrayList<>();
		list.add(Examples.getChord(0, 0,4,7));
		list.add(Examples.getChord(6, 1,4,6));
		list.add(Examples.getChord(12, 11,2,7));
		list.add(Examples.getChord(24, 0,4,9));
		FitnessEvaluation fitnessEvaluation = new FitnessEvaluation();
		double[] objectives = fitnessEvaluation.evaluate(list);
		LOGGER.info("test" + "\n"  
				+ "Harmony: " + objectives[0] + ", " 
				+ "VoiceLeading: " + objectives[1] + ", " 
				+ "Melody: " + objectives[2] + ", "
				+ "Rhythm: " + objectives[3] + ", "
				+ "tonality: " + objectives[4] + "\n");
//				+ "Constraints: lowest interval register: " + objectives[5] + ", "
//				+ "repetitions Pitches: " + objectives[6] + ", "
//				+ "repetitions rhythms: " + objectives[7]);
	}
	
	public double[] evaluate(List<NoteList> list) {
		double[] objectives = new double[5];
			
		switch (numerator) {//4/4 = 4 ; 2/4 = 2 ; 3/4 = 3 ; 6/8 = 6
		case 2:
			applyDynamicTemplate(list, 6, true);
			break;
		case 3:
			applyDynamicTemplate(list, 12, false);	
			break;
		case 4:
			applyDynamicTemplate(list, 12, true);
			break;
		case 6:
			applyDynamicTemplate(list, 6, false);
			break;
		default:
			break;
		}
//		
//		List<Integer> positions = noteList.stream().mapToInt(n -> n.getPosition()).collect(Collectors.toList());
//		Map<Integer, Double> map = applyInnerMetricWeight(sentences);
//		LOGGER.fine("Inner metric map: " + map.toString());

		//harmony
		double harmonyMean = evaluateHarmony(list);
		LOGGER.fine("mean harmonicValues: " + harmonyMean);

		//voice leading
		double voiceLeading = evaluateVL(list, 12);
		LOGGER.fine("max voiceLeadingSize: " + voiceLeading);
		
		double melodicValue = evaluateMelody(list);
		if (Double.isNaN(melodicValue)) {
			melodicValue = Double.MAX_VALUE;
		}
		LOGGER.fine("melodicValue = " + melodicValue);
		
//		double rhythmicValue = evaluateRhythm(sentences, numerator);
//		LOGGER.fine("rhythmicValue = " + rhythmicValue);
//
//		double tonalityValue = evaluateMajorMinorTonality(sentences);
//		LOGGER.fine("tonalityValue = " + tonalityValue);
		
		objectives[0] = harmonyMean;
		objectives[1] = voiceLeading;
		objectives[2] = melodicValue;
//		objectives[3] = rhythmicValue;
//		objectives[4] = 1 - tonalityValue;
		//constraints
//		objectives[5] = lowestIntervalRegisterValue;
//		objectives[6] = repetitionsrhythmsMean;	//only for small motives (5 - 10 notes)
//		objectives[7] = repetitionsPitchesMean;	//only for small motives (5 - 10 notes)
		return objectives;	
	}


	private void applyDynamicTemplate(List<NoteList> noteList, int beat, boolean even) {
		int e = even?2:3;
		int doubleBeat = beat * e;
		int maxValue = 0;
		for (NoteList list : noteList) {
			int position = list.getPosition();		
			//add to every note
			int valueToAdd = rhythmTemplateValue + DEFAULT_NOTE_ON_VALUE;
			//add to notes on the accented beats
			if(position % beat == 0){	
				valueToAdd = valueToAdd + rhythmTemplateValue;
			}
			if(position % doubleBeat == 0){	
				valueToAdd = valueToAdd + rhythmTemplateValue;
			}
			for (NotePos note : list.getNotes()) {
				note.setPositionWeight(valueToAdd);
			}
			if (valueToAdd > maxValue) {
				maxValue = valueToAdd;
			}
		}
		
		//normalize values
		for (NoteList list : noteList) {
			List<NotePos> notes = list.getNotes();
			for (NotePos note : notes) {
				double normalizedValue = note.getPositionWeight() / maxValue;
				note.setPositionWeight(normalizedValue);
			}
		}
	}
	
//	private Map<Integer, Double> applyInnerMetricWeight(List<NoteList> noteList) {
//		Map<Integer, Double> melodiesMap = new TreeMap<Integer, Double>();
//		for (NoteList list : noteList) {
//			List<NotePos> notes = list.getNotes();
//			Map<Integer, Double> map = getInnerMetricMap(sentence);
////			LOGGER.fine(map);
//			if (!map.isEmpty()) {
//				Set<Integer> onsets = map.keySet();
//				for (Integer onset : onsets) {
//					for (NotePos note : notes) {
//						if (note.getPosition() == onset) {
//							double value = map.get(onset);
//							note.setInnerMetricWeight(value);
//							break;
//						}
//					}
//					if (melodiesMap.containsKey(onset)) {
//						double value = melodiesMap.get(onset);
//						double newValue = value + map.get(onset);
//						melodiesMap.put(onset, newValue);
//					} else {
//						melodiesMap.put(onset, map.get(onset));
//					}
//				}
//			}
//		}
//		return melodiesMap;
//	}
	
	public Map<Integer, Double> getInnerMetricMap(List<Integer> positions, int length){
		Integer[] onSet = InnerMetricWeight.extractPositions(positions, length);
		NavigableMap<Integer, Double> map1 = InnerMetricWeight.getNormalizedInnerMetricWeight(onSet);
		Map<Integer, Double> m = map1.subMap(length, length * 2);
		Set<Integer> keys = m.keySet();
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (Integer key : keys) {
			map.put(key - length + positions.get(0), m.get(key));
		}
		return map;
	}
	
	private double evaluateHarmony(List<NoteList> noteList) {
		OptionalDouble optionalDouble = noteList.stream().map(n -> n.toChord()).mapToDouble(ch -> ch.getWeight()).average();
//		return noteList.stream().map(n -> n.toChord()).mapToDouble(ch -> ch.getWeight()).peek(System.out::println).sum();
		return optionalDouble.getAsDouble();
	}

	private double evaluateVL(List<NoteList> noteList, int beatDivider){
		Map<Double, List<NoteList>> map = noteList.stream().collect(Collectors.groupingBy(ch -> ch.getBeat(beatDivider)));
		Map<Double, Chord> bestChordMap = new TreeMap<>();
		for (Entry<Double, List<NoteList>> entry: map.entrySet()) {
			List<NoteList> list = entry.getValue();
			Optional<Chord> bestChord = list.stream().map(t -> t.toChord()).max(Comparator.comparing(Chord::getWeight));
			if(bestChord.isPresent()) {
				bestChordMap.put(entry.getKey(), bestChord.get());
			}
		}
		Chord[] chords = new Chord[bestChordMap.size()];
		chords = bestChordMap.values().toArray(chords);
//		map.forEach((k,v) -> v.stream().map(ch -> ch.toChord()));
		bestChordMap.forEach((p,ch) -> System.out.println(p + ": " + ch.getChordType()));
		int totalSize = 0;
		for(int i = 0; i < chords.length - 1; i++){
			VoiceLeadingSize minimalVoiceLeadingSize = VoiceLeading.caculateSize(chords[i].getPitchClassMultiSet(), chords[i + 1].getPitchClassMultiSet());
			System.out.print(minimalVoiceLeadingSize.getVlSource());
			System.out.print(minimalVoiceLeadingSize.getVlTarget());
			System.out.print(minimalVoiceLeadingSize.getSize());
			System.out.println();
			totalSize = totalSize + minimalVoiceLeadingSize.getSize();
		}
		return totalSize/(chords.length - 1);
	}
	
	private double evaluateMelody(List<NoteList> noteList) {
		List<NotePos> allNotes = new ArrayList<>();
		for (NoteList list : noteList) {
			for (NotePos notePos : list.getNotes()) {
				allNotes.add(notePos);
			}
		}
		Map<Integer, List<NotePos>> melodies = allNotes.stream().collect(Collectors.groupingBy(n -> n.getVoice()));
		melodies.forEach((p, ch) -> System.out.println(p + ": " +ch));
		double total = 0.0;
		int count = 0;
		for(Entry<Integer, List<NotePos>> voices : melodies.entrySet()){
			List<NotePos> notePositions = voices.getValue();
			notePositions.stream().forEach(n -> System.out.print( "," + n.getPitchClass()));
			System.out.println();
			if (notePositions.size() > 1) {
				List<Double> listWeights;
				if (notePositions.size() > 4) {
					listWeights = MelodicFunctions.getMelodicWeights2(notePositions, 4);
				} else {
					listWeights = MelodicFunctions.getMelodicWeights2(notePositions, notePositions.size());
				}
				double[] melodicWeights = listToArray(listWeights);
				LOGGER.fine(Arrays.toString(melodicWeights));
				DescriptiveStatistics stats = new DescriptiveStatistics(melodicWeights);
				// Compute some statistics
				double mean = stats.getGeometricMean();
//					double mean = stats.getStandardDeviation();
				LOGGER.fine("melodicValue mean: " + mean);
				LOGGER.fine("melodicValue standarddeviation: " + stats.getStandardDeviation());
				if (!Double.isNaN(mean)) {//when melody contains no intervals (note repeat, octave)
					total = total + mean;
					count++;
			}
		}
		}
		return total/count;
	}
		
		public double[] listToArray(List<Double> values) {
			Double[] valuesArray = new Double[values.size()];
			valuesArray = values.toArray(valuesArray);
			double[] v = ArrayUtils.toPrimitive(valuesArray);
			return v;
		}
	
//	private double evaluateRhythm(List<MusicalStructure> sentences, int numerator) {
//		double total = 0;
//		double count = 0;
//		for (MusicalStructure musicalStructure : sentences) {
//			Map<Integer, Double> map = getInnerMetricMap(musicalStructure);
//			if (!map.isEmpty()) {
//				double metricCoherence = RhythmicFunctions.calculateMetricCoherenceValue(map, numerator);
//				LOGGER.finer("metricCoherence: " + metricCoherence);
//				if (metricCoherence != Double.MAX_VALUE) {
//					total = total + metricCoherence;
//					count++;
//				}
//			}		
//		}
//		return (count == 0)?0.0:total/count;
//	}
//	
//	private double evaluateMajorMinorTonality(List<MusicalStructure> sentences) {
//		double major = TonalityFunctions.getMaxCorrelationTonality(sentences, TonalityFunctions.vectorMajorTemplate);
//		double minor = TonalityFunctions.getMaxCorrelationTonality(sentences, TonalityFunctions.vectorMinorTemplate);
//		if (major > minor) {
//			return major;
//		} else {
//			return minor;
//		}
//	}
		
		private static void configureLogger(Level level) throws IOException {
			Logger topLogger = Logger.getLogger("");
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(level);
			topLogger.addHandler(ch);
			topLogger.setLevel(level);
			FileHandler fileTxt = new FileHandler("Logging.txt");
			SimpleFormatter formatterTxt = new SimpleFormatter();
			fileTxt.setFormatter(formatterTxt);
			topLogger.addHandler(fileTxt);
		}

}

