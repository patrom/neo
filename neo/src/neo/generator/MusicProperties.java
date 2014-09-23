package neo.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Scale;
import neo.out.instrument.Instrument;

public class MusicProperties {
	
	private int harmonyBeatDivider = 12;
	private float tempo = 60;
	private Map<Integer, Double> rhythmWeightValues = new TreeMap<>(); //Must match length of harmonies based on division by minimumLength.
	private int minimumLength = 6;
	private int chordSize = 3;
	private Integer[] octaveHighestPitchClassRange = {5,6};
	private List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
	private boolean outerBoundaryIncluded = true;
	
	//tonality
	private Scale scale = new Scale(Scale.MAJOR_SCALE);
	private Scale melodyScale = new Scale(Scale.MAJOR_SCALE);;
	
	//harmony
	private double harmonyConsDissValue = 0.3;
	private int allowChordsOfPitchesOrHigher = 3;
	private String harmonyStrategy;
	
	//melody
	private double melodyConsDissValue = 0.2;//hoe lager, hoe cons - stapsgewijs
	
	//voice leading
	private double voiceLeadingConsDissValue;
	private String voiceLeadingStrategy;
	

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
	public Scale getScale() {
		return scale;
	}
	public int getNumerator() {
		return numerator;
	}
	public void setNumerator(int numerator) {
		this.numerator = numerator;
	}
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
	public int getChordSize() {
		return chordSize;
	}
	
	public void setChordSize(int chordSize) {
		this.chordSize = chordSize;
	}
	public Integer[] getOctaveHighestPitchClass() {
		return octaveHighestPitchClassRange;
	}
	
	public void setOctaveHighestPitchClass(Integer[] octaveHighestPitchClass) {
		this.octaveHighestPitchClassRange = octaveHighestPitchClass;
	}
	public void setScale(Scale scale) {
		this.scale = scale;
	}
	
	public List<HarmonyBuilder> getHarmonyBuilders() {
		return harmonyBuilders;
	}
	public void setHarmonyBuilders(List<HarmonyBuilder> harmonyBuilders) {
		this.harmonyBuilders = harmonyBuilders;
	}
	public List<HarmonicMelody> getHarmonicMelodies() {
		return harmonicMelodies;
	}
	public void setHarmonicMelodies(List<HarmonicMelody> harmonicMelodies) {
		this.harmonicMelodies = harmonicMelodies;
	}
	public Integer[] getOctaveHighestPitchClassRange() {
		return octaveHighestPitchClassRange;
	}
	public void setOctaveHighestPitchClassRange(
			Integer[] octaveHighestPitchClassRange) {
		this.octaveHighestPitchClassRange = octaveHighestPitchClassRange;
	}
	public Scale getMelodyScale() {
		return melodyScale;
	}
	public void setMelodyScale(Scale melodyScale) {
		this.melodyScale = melodyScale;
	}
	public boolean isOuterBoundaryIncluded() {
		return outerBoundaryIncluded;
	}
	public void setOuterBoundaryIncluded(boolean outerBoundaryIncluded) {
		this.outerBoundaryIncluded = outerBoundaryIncluded;
	}
	
}
