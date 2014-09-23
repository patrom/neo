package neo.evaluation;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import neo.generator.MusicProperties;
import neo.log.LogConfig;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.objective.Objective;
import neo.objective.harmony.HarmonicObjective;
import neo.objective.melody.InnerMetricWeight;
import neo.objective.melody.MelodicObjective;
import neo.objective.voiceleading.VoiceLeadingObjective;

public class FitnessEvaluationTemplate {

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTemplate.class.getName());

	private Objective harmonicObjective;
	private Objective melodicObjective;
	private Objective voiceLeadingObjective;

	private int minimumLength;

	public FitnessEvaluationTemplate(MusicProperties properties, Motive motive) {
		minimumLength = properties.getMinimumLength();
		updateInnerMetricWeightMelodies(motive.getMelodies());
		updateInnerMetricWeightHarmonies(motive.getHarmonies());
		harmonicObjective = new HarmonicObjective(properties, motive);
		melodicObjective = new MelodicObjective(properties, motive);
		voiceLeadingObjective = new VoiceLeadingObjective(properties, motive);
		

		LogConfig.configureLogger(Level.INFO);
	}
	
	public FitnessObjectiveValues evaluate() {
//		calculateNotePositionValues();
		return evaluateObjectives();
	}

	private FitnessObjectiveValues evaluateObjectives() {
		//harmony
		double harmonyMean = harmonicObjective.evaluate();
		if (harmonyMean > 1.0) {
			System.out.println();
		}
		LOGGER.info("mean harmonicValues: " + harmonyMean);

		//voice leading
		double voiceLeading = voiceLeadingObjective.evaluate();
		LOGGER.info("max voiceLeadingSize: " + voiceLeading);
		
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("melodicValue = " + melodicValue);
		
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
		calculateInnerMetricValues();
	}

	private void calculateInnerMetricValues() {

//		List<Integer> positions = noteList.stream().mapToInt(n -> n.getPosition()).collect(Collectors.toList());
//		Map<Integer, Double> map = applyInnerMetricWeight(sentences);
//		LOGGER.fine("Inner metric map: " + map.toString());
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

//	private void applyDynamicTemplate(List<Harmony> noteList, int beat, boolean even) {
//		int e = even?2:3;
//		int doubleBeat = beat * e;
//		int maxValue = 0;
//		for (Harmony list : noteList) {
//			int position = list.getPosition();		
//			//add to every note
//			int valueToAdd = rhythmTemplateValue + DEFAULT_NOTE_ON_VALUE;
//			//add to notes on the accented beats
//			if(position % beat == 0){	
//				valueToAdd = valueToAdd + rhythmTemplateValue;
//			}
//			if(position % doubleBeat == 0){	
//				valueToAdd = valueToAdd + rhythmTemplateValue;
//			}
//			for (NotePos note : list.getNotes()) {
//				note.setPositionWeight(valueToAdd);
//			}
//			if (valueToAdd > maxValue) {
//				maxValue = valueToAdd;
//			}
//		}
//		
//		//normalize values
//		for (Harmony list : noteList) {
//			List<NotePos> notes = list.getNotes();
//			for (NotePos note : notes) {
//				double normalizedValue = note.getPositionWeight() / maxValue;
//				note.setPositionWeight(normalizedValue);
//			}
//		}
//	}
	
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

