package neo.objective.objective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import jm.music.data.Phrase;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.objective.voiceleading.VoiceLeadingObjective;

import org.apache.commons.math.stat.correlation.PearsonsCorrelation;



public class InnerMetricWeight {
	
	private static Logger LOGGER = Logger.getLogger(InnerMetricWeight.class.getName());
	
	private static final int BEAT_FACTOR = 2;//1/8 = 2 - 1/16 = 4
	private static final int REPEAT = 3;
	private static final double PULSE = 0.25;//1/8 = 0.5 - 1/16 = 0.25
	private static final double POWER = 2.0;
	private static final int CORRELATION_LENGTH = 32; //fixed length: 4 bars of 1/8 notes!!
	private static final int MINIMUM_SIZE = 3; //size of local meter 

	public static void main2(String[] args) {

		
		double[] rhythmPattern = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
//		double[] rhythmPattern = {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 0.5, 0.5, 0.5, 1.0, 1.0, 1.0, 1.0};
//		double[] rhythmPattern = { 0.5, 1.0, 1.0, 1.0, 0.5, 0.5, 1.0, 0.5, 0.5, 0.5, 0.5,0.5};// syncopated
//		double[] rhythmPattern = {1.0, 0.5, 1.0, 1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5};
//		double[] rhythmPattern = {1.0, 1.0, 0.5,1.0, 0.5, 1.0, 1.0, 0.5,1.0, 0.5,1.0, 1.0, 0.5,1.0, 0.5, 1.0, 1.0, 0.5,1.0, 0.5};
		Note[] testMelody = null;
//		double[] rhythmPattern = RhythmicFunctions.createRandomRhythmPattern2(4);
		int originalLength = rhythmPattern.length;
		LOGGER.info(Arrays.toString(rhythmPattern));
		
//		RhythmicFunctions.playRhythm(rhythmPattern);
//		rhythmPattern = RhythmicFunctions.repeatPattern(rhythmPattern, REPEAT);
		InnerMetricWeight innerMetricWeight = new InnerMetricWeight();
		Integer[] onSetArr = innerMetricWeight.extractOnset(rhythmPattern ,PULSE);
		LOGGER.info(Arrays.toString(onSetArr));
//		List<Integer> onSet = new ArrayList<Integer>();
//		int[] onSet = new int[10];
//		Integer[] onSetArr = {0, 3, 6, 7, 8, 9, 10, 11, 12, 13, 15, 18, 19, 21, 22};
		List<List<Integer>> localMeters = innerMetricWeight.getLocalMeters(onSetArr);
		LOGGER.info(localMeters.toString());
		Map<Integer, Double> map =  innerMetricWeight.getInnerMetricWeight(localMeters, onSetArr);
//		Set<Integer> keys = map.keySet();
//		for (Integer key : keys) {
//			System.out.print(key + ":");
//			System.out.println(map.get(key));
//		}
//		double l = getLength(rhythmPattern);
//		System.out.println("length:" + l);
//		System.out.println(Math.ceil(l));
		double[] innerMetricWeightVector =  innerMetricWeight.createCorrelationVector(map , getLength(rhythmPattern));
		System.out.println(Arrays.toString(innerMetricWeightVector));
//		int start = (int) ((getLength(rhythmPattern)/ REPEAT) * BEAT_FACTOR);
//		int end = (start * 2) ;
//		double[] innerMetricWeightVector = Arrays.copyOfRange(tempInnerMetricWeightVector, start, end);
//		LOGGER.info(Arrays.toString(innerMetricWeightVector));
			
		double[] twoBarWeightVector = new double[16];
		twoBarWeightVector[0] = 100.0;
		twoBarWeightVector[1] = 25.0;
		twoBarWeightVector[2] = 50.0;
		twoBarWeightVector[3] = 25.0;
		twoBarWeightVector[4] = 100.0;
		twoBarWeightVector[5] = 25.0;
		twoBarWeightVector[6] = 50.0;
		twoBarWeightVector[7] = 25.0;
		twoBarWeightVector[8] = 100.0;
		twoBarWeightVector[9] = 25.0;
		twoBarWeightVector[10] = 50.0;
		twoBarWeightVector[11] = 25.0;
		twoBarWeightVector[12] = 100.0;
		twoBarWeightVector[13] = 25.0;
		twoBarWeightVector[14] = 50.0;
		twoBarWeightVector[15] = 25.0;
		
//		twoBarWeightVector[16] = 100.0;
//		twoBarWeightVector[17] = 25.0;
//		twoBarWeightVector[18] = 50.0;
//		twoBarWeightVector[19] = 25.0;
//		twoBarWeightVector[20] = 100.0;
//		twoBarWeightVector[21] = 25.0;
//		twoBarWeightVector[22] = 50.0;
//		twoBarWeightVector[23] = 25.0;
//		twoBarWeightVector[24] = 100.0;
//		twoBarWeightVector[25] = 25.0;
//		twoBarWeightVector[26] = 50.0;
//		twoBarWeightVector[27] = 25.0;
//		twoBarWeightVector[28] = 100.0;
//		twoBarWeightVector[29] = 25.0;
//		twoBarWeightVector[30] = 50.0;
//		twoBarWeightVector[31] = 25.0;
		
		double[] twoBarSixteenthWeightVector = new double[24];
		twoBarSixteenthWeightVector[0] = 100.0;
		twoBarSixteenthWeightVector[1] = 25.0;
		twoBarSixteenthWeightVector[2] = 50.0;
		twoBarSixteenthWeightVector[3] = 25.0;
		twoBarSixteenthWeightVector[4] = 90.0;
		twoBarSixteenthWeightVector[5] = 25.0;
		twoBarSixteenthWeightVector[6] = 50.0;
		twoBarSixteenthWeightVector[7] = 25.0;
		twoBarSixteenthWeightVector[8] = 100.0;
		twoBarSixteenthWeightVector[9] = 25.0;
		twoBarSixteenthWeightVector[10] = 50.0;
		twoBarSixteenthWeightVector[11] = 25.0;
		twoBarSixteenthWeightVector[12] = 90.0;
		twoBarSixteenthWeightVector[13] = 25.0;
		twoBarSixteenthWeightVector[14] = 50.0;
		twoBarSixteenthWeightVector[15] = 25.0;
		
		twoBarSixteenthWeightVector[16] = 100.0;
		twoBarSixteenthWeightVector[17] = 25.0;
		twoBarSixteenthWeightVector[18] = 50.0;
		twoBarSixteenthWeightVector[19] = 25.0;
		twoBarSixteenthWeightVector[20] = 90.0;
		twoBarSixteenthWeightVector[21] = 25.0;
		twoBarSixteenthWeightVector[22] = 50.0;
		twoBarSixteenthWeightVector[23] = 25.0;

		double[] corrVector = Arrays.copyOfRange(twoBarWeightVector, 0, innerMetricWeightVector.length);
		System.out.println(Arrays.toString(corrVector));
		double correlation = new PearsonsCorrelation().correlation(corrVector,innerMetricWeightVector);
		System.out.println("correlation " + correlation);
//		System.out.println("max: " + getMaxValue(innerMetricWeightVector));
		double[] normalizedVector = innerMetricWeight.normalize(innerMetricWeightVector,  innerMetricWeight.getMaxValue(innerMetricWeightVector), originalLength);
		System.out.println(Arrays.toString(normalizedVector));
//		RhythmicFunctions.playRhythmWithDynamic(rhythmPattern, convertToDynamics(normalizedVector));
	}
	
	
	public Map<Integer, Double> applyInnerMetricWeight(Melody melody) {
		Map<Integer, Double> melodiesMap = new TreeMap<Integer, Double>();
		List<Note> notes = melody.getMelodieNotes();
		Map<Integer, Double> map = getInnerMetricMap(notes);
		if (!map.isEmpty()) {
			Set<Integer> onsets = map.keySet();
			for (Integer onset : onsets) {
				for (Note note : notes) {
					if (note.getPosition() == onset) {
						double value = map.get(onset);
						note.setInnerMetricWeight(value);
						break;
					}
				}
				if (melodiesMap.containsKey(onset)) {
					double value = melodiesMap.get(onset);
					double newValue = value + map.get(onset);
					melodiesMap.put(onset, newValue);
				} else {
					melodiesMap.put(onset, map.get(onset));
				}
			}
		}
		return melodiesMap;
	}
	
	protected Map<Integer, Double> getInnerMetricMap(List<Note> notes){
		double[] notePositions = new double[notes.size()];
		int length = 0;
		for (int i = 0; i < notes.size(); i++) {
			Note note = notes.get(i);
			notePositions[i] = note.getPosition();
			length = length + note.getLength();
		}
		Integer[] onSet = extractOnset(notePositions, length);
		NavigableMap<Integer, Double> map1 = getNormalizedInnerMetricWeight(onSet);
		Map<Integer, Double> m = map1.subMap(length, length * 2);
		Set<Integer> keys = m.keySet();
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (Integer key : keys) {
//			map.put(key - length + sentence.getPosition(), m.get(key));
			map.put(key - length, m.get(key));
		}
		return map;
	}

	protected Map<Integer, Double> getInnerMetricMap(List<Integer> positions, int length){
		Integer[] onSet = extractPositions(positions, length);
		NavigableMap<Integer, Double> map1 = getNormalizedInnerMetricWeight(onSet);
		Map<Integer, Double> m = map1.subMap(length, length * 2);
		Set<Integer> keys = m.keySet();
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (Integer key : keys) {
			map.put(key - length + positions.get(0), m.get(key));
		}
		return map;
	}
	
	public static int[] convertToDynamics(double[] vector){
		int[] dynamics = new int[vector.length];
		for (int i = 0; i < vector.length; i++) {
			dynamics[i] = (int)Math.ceil(vector[i]/10);
		}
		return dynamics;
	}
	
	protected double[] normalize(double[] vector, double maxValue, int length){
		double[] normalizedVector = new double[length];
		int j = 0 ;
		for (int i = 0; i < vector.length; i++) {
			if (vector[i] != 0.0) {
				normalizedVector[j] =  Math.round((vector[i] / maxValue) * 100);
				j++;
			} 
		}
		return normalizedVector;
	}
	
	protected double[] normalize(Collection<Double> values){
		double maxValue = Collections.max(values);
		int l = values.size();
		double[] normalizedVector = new double[l];
		int i = 0;
		for (Double value : values) {
			normalizedVector[i] =  Math.round((value / maxValue) * 100);
			i++;
		}
		return normalizedVector;
	}
	
	protected double getMaxValue(double[] numbers){  
	    double maxValue = numbers[0];  
	    for(int i=1;i<numbers.length;i++){  
	        if(numbers[i] > maxValue){  
	            maxValue = numbers[i];  
	        }  
	    }  
	    return maxValue;  
	}  
	
	protected List<List<Integer>> getLocalMeters(Integer[] onSet){
		List<Integer> onSetList = Arrays.asList(onSet);
		List<List<Integer>> localMeters = new ArrayList<List<Integer>>();
//		int[] distanceArr = {1,2,3,4,5,6,7,8,9,10};// pulse = 0.5 of 0.25
		int[] distanceArr = {2,3,4,6,8,9,10,12,14,15,16,18,20,21,22,24,26,27,28,30,32};//atomic beat = 12
		for (int j = 0; j < distanceArr.length - 1; j++) {
			for (int start = 0; start < onSet.length; start++) {
				int i = onSet[start] + distanceArr[j];
				if (!onSetList.contains(i)) {
					continue;
				}else{
					List<Integer> sublist = new ArrayList<Integer>();
					sublist.add(onSet[start]);
					while (onSetList.contains(i)) {
						sublist.add(i);
						i = i + distanceArr[j];
					}
					if (sublist.size() >= MINIMUM_SIZE) {
						if (localMeters.isEmpty()) {//first time
							localMeters.add(sublist);
						} else {
							boolean isSubSet = false;
							for (List<Integer> localMeter : localMeters) {
								if (localMeter.containsAll(sublist)) {
									isSubSet = true;
								}
							}
							if (!isSubSet) {
								localMeters.add(sublist);
							}	
						}	
					}
				}
			}
//			start = 0;
		}
		return localMeters;
	}
	
	protected Map<Integer, Double> getInnerMetricWeight(List<List<Integer>> localMeters, Integer[] onSet){
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (int i = 0; i < onSet.length; i++) {
			for (List<Integer> localMeter : localMeters) {
				if (!localMeter.contains(onSet[i])) {
					continue;
				} else {
					Integer key = onSet[i];
					double value = Math.pow(localMeter.size()-1, POWER);
					if (map.containsKey(key)) {
						double oldValue = map.get(key);
						map.put(key, value + oldValue);
					} else {
						map.put(key, value);
					}
				}
			}
		}
		return map;
	}
	
	private static Map<Integer, Double> normalizeMap(Map<Integer, Double> map){
		double maxValue = Collections.max(map.values());
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			double value = map.get(key);
			double normalizedValue = Math.round((value / maxValue) * 100);
			map.put(key, normalizedValue);
		}
		return map;
	}
	
	protected Map<Integer, Double> getNormalizedInnerMetricWeight(double[] rhythmPattern, double pulse){
		Integer[] onSet = extractOnset(rhythmPattern, pulse);
		List<List<Integer>> localMeters = getLocalMeters(onSet);
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (int i = 0; i < onSet.length; i++) {
			for (List<Integer> localMeter : localMeters) {
				if (!localMeter.contains(onSet[i])) {
					continue;
				} else {
					Integer key = onSet[i];
					double value = Math.pow(localMeter.size()-1, POWER);
					if (map.containsKey(key)) {
						double oldValue = map.get(key);
						map.put(key, value + oldValue);
					} else {
						map.put(key, value);
					}
				}
			}
		}
		if (!map.isEmpty()) {
			return normalizeMap(map);
		} else {
			return Collections.emptyMap();
		}
	}
	
	protected Integer[] extractOnset(double[] rhythmPattern, double pulse){
		Integer[] arr = new Integer[rhythmPattern.length];
		arr[0] = 0;
		double onSet = 0;
		for (int i = 0; i < rhythmPattern.length; i++) {
			double rhythm = rhythmPattern[i];
			onSet = onSet + rhythm / pulse;
			if (i + 1 != rhythmPattern.length) {//don't add last
				arr[i + 1] = (int) onSet;
			}
		}
		return arr;
	}
	
	protected double[] createCorrelationVector(Map<Integer, Double> map, double length){
		double[] innerMetricWeightVector = new double[(int) (length * BEAT_FACTOR)];//1/8 = 2 - 1/16 = 4
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			innerMetricWeightVector[key] = map.get(key);
		}
		return innerMetricWeightVector;
	}
	
	protected static double getLength(double[] rhythmPattern){
		double total = 0;
		for (double d : rhythmPattern) {
			total = total + d;
		}
		return total;
	}

	protected Map<Integer, Double> getNormalizedInnerMetricWeight(List<Note> notes) {
		Integer[] onSet = extractOnset(notes);
		List<List<Integer>> localMeters = getLocalMeters(onSet);
		Map<Integer, Double> map = new TreeMap<Integer, Double>();
		for (int i = 0; i < onSet.length; i++) {
			for (List<Integer> localMeter : localMeters) {
				if (!localMeter.contains(onSet[i])) {
					continue;
				} else {
					Integer key = onSet[i];
					double value = Math.pow(localMeter.size()-1, POWER);
					if (map.containsKey(key)) {
						double oldValue = map.get(key);
						map.put(key, value + oldValue);
					} else {
						map.put(key, value);
					}
				}
			}
		}
		if (!map.isEmpty()) {
			return normalizeMap(map);
		} else {
			return Collections.emptyMap();
		}
	}

	private Integer[] extractOnset(List<Note> notes) {
		int length = notes.size();
		Integer[] arr = new Integer[length];
		arr[0] = 0;
		for (int i = 0; i < length; i++) {
			arr[i] = notes.get(i).getPosition();
		}
		return extendArray(arr);
	}
	
	private Integer[] extendArray(Integer[] rhythmArray) {
		int length = rhythmArray.length * 3;
		Integer[] rArray = new Integer[length];
		for (int i = 0; i < length; i++) {
			rArray[i] = rhythmArray[i % rhythmArray.length];
		}
		return rArray;
	}
	
	protected Integer[] extractOnset(List<Note> notes, int structureLength) {
		int length = notes.size();
		Integer[] arr = new Integer[length * 3];
//		arr[0] = 0;
		for (int i = 0; i < length ; i++) {
			Note note = notes.get(i);
			arr[i] = note.getPosition();
			arr[i + length] = note.getPosition() + structureLength;
			arr[i + (length * 2)] = note.getPosition() + (structureLength * 2);
		}
		return arr;
	}
	
	protected Integer[] extractPositions(List<Integer> positions, int length) {
		Integer[] arr = new Integer[length * 3];
		for (int i = 0; i < length ; i++) {
			int position = positions.get(i);
			arr[i] = position;
			arr[i + length] = position + length;
			arr[i + (length * 2)] = position + (length * 2);
		}
		return arr;
	}
	
	protected NavigableMap<Integer, Double> getNormalizedInnerMetricWeight(Integer[] onSet) {
		List<List<Integer>> localMeters = getLocalMeters(onSet);
		NavigableMap<Integer, Double> map = new TreeMap<Integer, Double>();
		for (int i = 0; i < onSet.length; i++) {
			for (List<Integer> localMeter : localMeters) {
				if (!localMeter.contains(onSet[i])) {
					continue;
				} else {
					Integer key = onSet[i];
					double value = Math.pow(localMeter.size()-1, POWER);
					if (map.containsKey(key)) {
						double oldValue = map.get(key);
						map.put(key, value + oldValue);
					} else {
						map.put(key, value);
					}
				}
			}
		}
		if (!map.isEmpty()) {
			return normalizeNavigableMap(map);
		} else {
			return map;
		}
	}
	
	private NavigableMap<Integer, Double> normalizeNavigableMap(NavigableMap<Integer, Double> map){
		double maxValue = Collections.max(map.values());
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			double value = map.get(key);
			double normalizedValue = value / maxValue;
			map.put(key, normalizedValue);
		}
		return map;
	}
	

	
}
