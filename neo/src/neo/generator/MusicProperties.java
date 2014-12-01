package neo.generator;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.model.note.Scale;
import neo.out.instrument.Ensemble;
import neo.out.instrument.Instrument;

import org.springframework.stereotype.Component;

@Component
public class MusicProperties {
	
	private int harmonyBeatDivider = 12;
	private float tempo = 90;
	private Map<Integer, Double> rhythmWeightValues = new TreeMap<>(); //Must match length of harmonies based on division by minimumLength.
	private int minimumLength = 6;
	private int chordSize;
	private Integer[] octaveHighestPitchClassRange = {5,6};
	private boolean outerBoundaryIncluded = true;
	private double[] filterLevels = {0.5};
	private List<Instrument> instruments;
	
	//tonality
	private Scale scale = new Scale(Scale.MAJOR_SCALE);
	private Scale melodyScale = new Scale(Scale.MAJOR_SCALE);
	
	//harmony
	private double harmonyConsDissValue = 0.3;
	private int allowChordsOfPitchesOrHigher = 3;
	
	//melody
	private double melodyConsDissValue = 0.2;//hoe lager, hoe cons - stapsgewijs
	
	//voice leading
	private double voiceLeadingConsDissValue;
	private String voiceLeadingStrategy;

	//score
	private int numerator = 3;
	private int denominator = 4;
	private int keySignature = 0;
	
	//MOGA
	private int populationSize;
	private int maxEvaluations;
	private double crossoverProbability;
	private double mutationProbability;
	
	private int[][] harmonies;
	private double[] measureWeights;
	
	public void threeFour(){
//		int[][] harmonies = {{0,12,18,24},{36,30,36,42,60},{72, 72,78,84},{108}};//first = harmony position / other melodies (last = length)
//		this.harmonies = harmonies;
		double[] measureWeights = {1.0, 0.5, 0.75, 0.5, 0.75, 0.5};// measure must correspond to minimumlength!
		this.measureWeights = measureWeights;
		this.numerator = 3;
		this.denominator = 4;
		this.instruments = Ensemble.getStringQuartet();
		this.chordSize = instruments.size();
	}
	
	public void fourFour(){
//		int[][] harmonies = {{0,6,18,24},{48},{96},{144},{192}};
//		this.harmonies = harmonies;
		double[] measureWeights = {1.0, 0.5, 0.75, 0.5, 1.0, 0.5, 0.75, 0.5};
		this.measureWeights = measureWeights;
	    this.numerator = 4;
	    this.denominator = 4;
	    this.instruments = Ensemble.getStringQuartet();
		this.chordSize = instruments.size();
	}
	
	public String getVoiceLeadingStrategy() {
		return voiceLeadingStrategy;
	}
	public void setVoiceLeadingStrategy(String voiceLeadingStrategy) {
		this.voiceLeadingStrategy = voiceLeadingStrategy;
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
	public double[] getFilterLevels() {
		return filterLevels;
	}
	public void setFilterLevels(double[] filterLevels) {
		this.filterLevels = filterLevels;
	}
	public int getDenominator() {
		return denominator;
	}
	public void setDenominator(int denominator) {
		this.denominator = denominator;
	}
	public int getKeySignature() {
		return keySignature;
	}
	public void setKeySignature(int keySignature) {
		this.keySignature = keySignature;
	}

	public Map<Integer, Double> getRhythmWeightValues() {
		return rhythmWeightValues;
	}

	public void setRhythmWeightValues(Map<Integer, Double> rhythmWeightValues) {
		this.rhythmWeightValues = rhythmWeightValues;
	}

	public double[] getMeasureWeights() {
		return measureWeights;
	}

	public void setMeasureWeights(double[] measureWeights) {
		this.measureWeights = measureWeights;
	}

	public int[][] getHarmonies() {
		return harmonies;
	}

	public void setHarmonies(int[][] harmonies) {
		this.harmonies = harmonies;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}
	
}
