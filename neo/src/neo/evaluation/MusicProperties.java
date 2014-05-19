package neo.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.note.Scale;
import neo.instrument.Instrument;

public class MusicProperties {
	
	
	private int harmonyBeatDivider = 12;
	private float tempo;
	private int[] rhythmGeneratorTemplate = {0,12,18,24};
	private Map<Integer, Double> rhythmWeightValues = new TreeMap<>(); //Must match length of harmonies based on division by minimumLength.
	private int minimumLength = 6;
	
	//tonality
	private Scale scale = new Scale(Scale.MAJOR_SCALE);
	
	public MusicProperties() {
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		
		ranges.add(getInstrument(0, 48, 60));
		ranges.add(getInstrument(1, 54, 70));
		ranges.add(getInstrument(2, 60, 74));
		ranges.add(getInstrument(3, 65, 80));
	}
	
	//harmony
	private double harmonyConsDissValue = 0.3;
	private int allowChordsOfPitchesOrHigher = 3;
	private String harmonyStrategy;
	
	//melody
	private double melodyConsDissValue = 0.2;//hoe lager, hoe cons - stapsgewijs
	
	//voice leading
	private double voiceLeadingConsDissValue;
	private String voiceLeadingStrategy;
	
	//rhythm - context
	private double rhythmConsDissValue = 0.4;
	private int rhythmTemplateValue = 5;//add to every note + template accents
	private int innerMetricFactor = 10;// add 100/innerMetricFactor
	private int[] rhythmProfile = {0,0,0,10,10};//whole note (with divisions), half note, quarter note, triplet half note, triplet quarter note.
	

	//generation
	private int numerator = 4;//2/4,4/4,3/4 - 6/8
//	private int voices = 4;
	private int melodyLength = 8;
	private String populationStrategy = "polyphonic";//homophonic or polyphonic
	
	//MOGA
	private int populationSize;
	private int maxEvaluations;
	private double crossoverProbability;
	private double mutationProbability;
	
	private List<Instrument> ranges = new ArrayList<>();
	
	public List<Instrument> getRanges() {
		return ranges;
	}
	public void setRanges(List<Instrument> ranges) {
		this.ranges = ranges;
	}
	public String getHarmonyStrategy() {
		return harmonyStrategy;
	}
	public void setHarmonyStrategy(String harmonyStrategy) {
		this.harmonyStrategy = harmonyStrategy;
	}
	public String getVoiceLeadingStrategy() {
		return voiceLeadingStrategy;
	}
	public void setVoiceLeadingStrategy(String voiceLeadingStrategy) {
		this.voiceLeadingStrategy = voiceLeadingStrategy;
	}
	
	public String getPopulationStrategy() {
		return populationStrategy;
	}
	public void setPopulationStrategy(String populationStrategy) {
		this.populationStrategy = populationStrategy;
	}
	public double getHarmonyConsDissValue() {
		return harmonyConsDissValue;
	}
	public void setHarmonyConsDissValue(double harmonyConsDissValue) {
		this.harmonyConsDissValue = harmonyConsDissValue;
	}
	public int getAllowChordsOfPitchesOrHigher() {
		return allowChordsOfPitchesOrHigher;
	}
	public void setAllowChordsOfPitchesOrHigher(int allowChordsOfPitchesOrHigher) {
		this.allowChordsOfPitchesOrHigher = allowChordsOfPitchesOrHigher;
	}
	public double getMelodyConsDissValue() {
		return melodyConsDissValue;
	}
	public void setMelodyConsDissValue(double melodyConsDissValue) {
		this.melodyConsDissValue = melodyConsDissValue;
	}
	public double getVoiceLeadingConsDissValue() {
		return voiceLeadingConsDissValue;
	}
	public void setVoiceLeadingConsDissValue(double voiceLeadingConsDissValue) {
		this.voiceLeadingConsDissValue = voiceLeadingConsDissValue;
	}
	public int getRhythmTemplateValue() {
		return rhythmTemplateValue;
	}
	public void setRhythmTemplateValue(int rhythmTemplateValue) {
		this.rhythmTemplateValue = rhythmTemplateValue;
	}
	public int getInnerMetricFactor() {
		return innerMetricFactor;
	}
	public void setInnerMetricFactor(int innerMetricFactor) {
		this.innerMetricFactor = innerMetricFactor;
	}
	public Scale getScale() {
		return scale;
	}
	
	public int getNumerator() {
		return numerator;
	}
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}
//	public int getVoices() {
//		return voices;
//	}
//	public void setVoices(int voices) {
//		this.voices = voices;
//	}
	public int getMelodyLength() {
		return melodyLength;
	}
	public void setMelodyLength(int melodyLength) {
		this.melodyLength = melodyLength;
	}
	public int getPopulationSize() {
		return populationSize;
	}
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}
	public int getMaxEvaluations() {
		return maxEvaluations;
	}
	public void setMaxEvaluations(int maxEvaluations) {
		this.maxEvaluations = maxEvaluations;
	}
	public double getCrossoverProbability() {
		return crossoverProbability;
	}
	public void setCrossoverProbability(double crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}
	public double getMutationProbability() {
		return mutationProbability;
	}
	public void setMutationProbability(double mutationProbability) {
		this.mutationProbability = mutationProbability;
	}
	public int[] getRhythmProfile() {
		return rhythmProfile;
	}
	public void setRhythmProfile(int[] rhythmProfile) {
		this.rhythmProfile = rhythmProfile;
	}
	public double getRhythmConsDissValue() {
		return rhythmConsDissValue;
	}
	public void setRhythmConsDissValue(double rhythmConsDissValue) {
		this.rhythmConsDissValue = rhythmConsDissValue;
	}
	
	public static Instrument getInstrument(int voice, int low, int high) {
		Instrument range = new Instrument();
		range.setVoice(voice);
		range.setLowest(low);
		range.setHighest(high);
		return range;
	}
	public int getHarmonyBeatDivider() {
		return harmonyBeatDivider;
	}
	public void setHarmonyBeatDivider(int harmonyBeatDivider) {
		this.harmonyBeatDivider = harmonyBeatDivider;
	}
	public float getTempo() {
		return tempo;
	}
	public void setTempo(float tempo) {
		this.tempo = tempo;
	}
	public int[] getRhythmGeneratorTemplate() {
		return rhythmGeneratorTemplate;
	}
	public void setRhythmGeneratorTemplate(int[] rhythmGeneratorTemplate) {
		this.rhythmGeneratorTemplate = rhythmGeneratorTemplate;
	}
	public Map<Integer, Double> getRhythmWeightValues() {
		return rhythmWeightValues;
	}
	public void setRhythmWeightValues(Map<Integer, Double> rhythmWeightValues) {
		this.rhythmWeightValues = rhythmWeightValues;
	}
	public int getMinimumLength() {
		return minimumLength;
	}
	public void setMinimumLength(int minimumLength) {
		this.minimumLength = minimumLength;
	}
	
}
