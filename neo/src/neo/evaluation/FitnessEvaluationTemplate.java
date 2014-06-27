package neo.evaluation;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import jm.music.data.Note;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.note.NotePos;
import neo.log.LogConfig;
import neo.objective.InnerMetricWeight;
import neo.objective.Objective;
import neo.objective.harmony.HarmonicObjective;
import neo.objective.melody.MelodicObjective;
import neo.objective.voiceleading.VoiceLeadingObjective;

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
	private MusicProperties musicProperties;

	public FitnessEvaluationTemplate(MusicProperties properties, Motive motive) {
		this.motive = motive;
		this.numerator = properties.getNumerator();
		harmonicObjective = new HarmonicObjective(properties, motive);
		melodicObjective = new MelodicObjective(properties, motive);
		voiceLeadingObjective = new VoiceLeadingObjective(properties, motive);
		this.musicProperties = properties;
		
		LogConfig.configureLogger(Level.SEVERE);
	}
	
	public FitnessObjectiveValues evaluate() {
//		calculateNotePositionValues();
		return evaluateObjectives();
	}


	private FitnessObjectiveValues evaluateObjectives() {
		//harmony
		double harmonyMean = harmonicObjective.evaluate();
		LOGGER.info("mean harmonicValues: " + harmonyMean);

		//voice leading
		double voiceLeading = voiceLeadingObjective.evaluate();
		LOGGER.fine("max voiceLeadingSize: " + voiceLeading);
		
		double melodicValue = melodicObjective.evaluate();
		LOGGER.fine("melodicValue = " + melodicValue);
		
//		double rhythmicValue = evaluateRhythm(sentences, numerator);
//		LOGGER.fine("rhythmicValue = " + rhythmicValue);
//
//		double tonalityValue = evaluateMajorMinorTonality(sentences);
//		LOGGER.fine("tonalityValue = " + tonalityValue);
		
		FitnessObjectiveValues fitnessObjectives = new FitnessObjectiveValues();
		fitnessObjectives.setHarmony(harmonyMean);
		fitnessObjectives.setMelody(melodicValue);
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
		updateBeatValues();//change to apply rhythmic template;
		calculateInnerMetricValues();
	}

	private void calculateInnerMetricValues() {
//		List<Integer> positions = noteList.stream().mapToInt(n -> n.getPosition()).collect(Collectors.toList());
//		Map<Integer, Double> map = applyInnerMetricWeight(sentences);
//		LOGGER.fine("Inner metric map: " + map.toString());
	}
	
	protected void updateBeatValues() {
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

