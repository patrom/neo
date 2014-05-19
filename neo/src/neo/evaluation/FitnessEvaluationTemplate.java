package neo.evaluation;

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

import jm.music.data.Note;
import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.objective.InnerMetricWeight;
import neo.objective.Objective;
import neo.objective.harmony.Chord;
import neo.objective.harmony.HarmonicObjective;
import neo.objective.melody.MelodicFunctions;
import neo.objective.melody.MelodicObjective;
import neo.objective.voiceleading.VoiceLeading;
import neo.objective.voiceleading.VoiceLeadingObjective;
import neo.objective.voiceleading.VoiceLeadingSize;
import neo.score.LogConfig;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class FitnessEvaluationTemplate {

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTemplate.class.getName());
	
	private static final int DYNAMIC_FACTOR = 3;
	private static final double DYNAMIC = Note.DEFAULT_DYNAMIC;
	private static final int DEFAULT_NOTE_ON_VALUE = 10;//note on accent
	private int innerMetricFactor;

	//input parameters
	private int rhythmTemplateValue;

	private int numerator = 4;

	private Objective harmonicObjective;
	private Objective melodicObjective;
	private Objective voiceLeadingObjective;
	private Motive motive;

	public FitnessEvaluationTemplate(MusicProperties properties, Motive motive) {
		this.motive = motive;
		this.numerator = properties.getNumerator();
		harmonicObjective = new HarmonicObjective(properties, motive);
		melodicObjective = new MelodicObjective(motive);
		voiceLeadingObjective = new VoiceLeadingObjective(properties, motive);
		
		try {
			LogConfig.configureLogger(Level.INFO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FitnessObjectiveValues evaluate() {
//		calculateNotePositionValues();
		return evaluateObjectives();
	}


	private FitnessObjectiveValues evaluateObjectives() {
		//harmony
		double harmonyMean = evaluateHarmony();
		LOGGER.info("mean harmonicValues: " + harmonyMean);

		//voice leading
		double voiceLeading = evaluateVL();
		LOGGER.fine("max voiceLeadingSize: " + voiceLeading);
		
//		double melodicValue = evaluateMelody();
//		if (Double.isNaN(melodicValue)) {
//			melodicValue = Double.MAX_VALUE;
//		}
//		LOGGER.fine("melodicValue = " + melodicValue);
		
//		double rhythmicValue = evaluateRhythm(sentences, numerator);
//		LOGGER.fine("rhythmicValue = " + rhythmicValue);
//
//		double tonalityValue = evaluateMajorMinorTonality(sentences);
//		LOGGER.fine("tonalityValue = " + tonalityValue);
		
		FitnessObjectiveValues fitnessObjectives = new FitnessObjectiveValues();
		fitnessObjectives.setHarmony(harmonyMean);
//		fitnessObjectives.setMelody(melodicValue);
		fitnessObjectives.setVoiceleading(voiceLeading);
//		objectives[3] = rhythmicValue;
//		objectives[4] = 1 - tonalityValue;
		//constraints
//		objectives[5] = lowestIntervalRegisterValue;
//		objectives[6] = repetitionsrhythmsMean;	//only for small motives (5 - 10 notes)
//		objectives[7] = repetitionsPitchesMean;	//only for small motives (5 - 10 notes)
		return fitnessObjectives;	
	}

	private void calculateNotePositionValues() {
		calculateBeatValues();//change to apply rhythmic template;
		calculateInnerMetricValues();
	}

	private void calculateInnerMetricValues() {
//		List<Integer> positions = noteList.stream().mapToInt(n -> n.getPosition()).collect(Collectors.toList());
//		Map<Integer, Double> map = applyInnerMetricWeight(sentences);
//		LOGGER.fine("Inner metric map: " + map.toString());
	}
	
	private void calculateBeatValues() {
		List<Harmony> harmonies = motive.getHarmonies();
		switch (numerator) {//4/4 = 4 ; 2/4 = 2 ; 3/4 = 3 ; 6/8 = 6
		case 2:
			applyDynamicTemplate(harmonies, 6, true);
			break;
		case 3:
			applyDynamicTemplate(harmonies, 12, false);	
			break;
		case 4:
			applyDynamicTemplate(harmonies, 12, true);
			break;
		case 6:
			applyDynamicTemplate(harmonies, 6, false);
			break;
		default:
			break;
		}
	}

	private void applyDynamicTemplate(List<Harmony> noteList, int beat, boolean even) {
		int e = even?2:3;
		int doubleBeat = beat * e;
		int maxValue = 0;
		for (Harmony list : noteList) {
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
		for (Harmony list : noteList) {
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
	
	private double evaluateHarmony() {
		return harmonicObjective.evaluate();
	}

	private double evaluateVL(){
		return voiceLeadingObjective.evaluate();
	}
	
	private double evaluateMelody() {
		return melodicObjective.evaluate();
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

}

