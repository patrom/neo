package neo.objective.melody;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import jm.music.data.Note;
import jm.music.data.Phrase;
import jm.music.tools.NoteListException;
import jm.music.tools.QuantisationException;
import neo.data.note.Interval;
import neo.data.note.NotePos;

import org.apache.commons.lang.ArrayUtils;

public class MelodicFunctions {
	
	private static Logger LOGGER = Logger.getLogger(MelodicFunctions.class.getName());
	
	public static List<Double> getMelodicWeights2(List<NotePos> melody, int windowSize){
		int size = melody.size();
		NotePos[] notes = new NotePos[size];
		for (int i = 0; i < size; i++) {
			notes[i] = melody.get(i);
		}
		return melodicWindow(notes, windowSize);
	}
	
	public static List<Double> melodicWindow(NotePos[] notes, int windowSize){
		int length = notes.length - windowSize + 1;	
		List<Double> values = new ArrayList<Double>();
		for (int i = 0; i < length; i++) {
			NotePos[] melody = new NotePos[windowSize];
			for (int j = 0; j < windowSize; j++) {
				melody[j] = notes[i + j];
			}
			values.add(computeMelodicValueWindow(melody));
		}
		return values; 
	}

	private static double computeMelodicValueWindow(NotePos[] melody ) {
		List<Double> values = new ArrayList<Double>();
		for (int j = 0; j < melody.length - 1; j++) {
				NotePos note = melody[0];
				NotePos nextNote = melody[j + 1];

				int difference = nextNote.getPitch() - note.getPitch();
				Interval interval = Interval.getEnumInterval(difference);
					
				double positionWeigtht = (nextNote.getPositionWeight() + note.getPositionWeight()) / 2;
				double innerMetricWeight = (nextNote.getInnerMetricWeight() + note.getInnerMetricWeight())/2;
//				double dynamic = ((note2.getDynamic() + note.getDynamic())/2) / 127d;//max midi TODO
				double rhythmicWeight = (positionWeigtht + innerMetricWeight) / 2;
				
				double intervalValue = (interval.getMelodicValue() * 0.6) + (interval.getMelodicValue() * rhythmicWeight * 0.4);
				values.add(intervalValue); 		
		}
		return (values.isEmpty())?0.0:Collections.min(values);
	}
	
	public static double getIntervalVariation(Phrase phrase){
		Note[] melody = phrase.getNoteArray();
		Set<Interval> intervalSet = new HashSet<Interval>();
		for (int i = 0; i < melody.length - 1; i++) {
			int difference = (melody[i + 1].getPitch() - melody[i].getPitch())%12;
			Interval interval = Interval.getEnumInterval(difference);
			intervalSet.add(interval);
		}
		int intervalCount = melody.length - 1;
        return (double)intervalSet.size() / (double)intervalCount;
	}

	
	public static double getMelodicValue(Phrase phrase){
		Note[] melody = phrase.getNoteArray();
		double sum = 0.0;
		int count = 0;
		for (int i = 0; i < melody.length - 1; i++) {
			int difference = (melody[i + 1].getPitch() - melody[i].getPitch())%12;
			Interval interval = Interval.getEnumInterval(difference);
			LOGGER.info(interval + ": "+  interval.getMelodicValue() + "," );
			sum = sum + interval.getMelodicValue();
			count++;
		}
		LOGGER.info("count :" + count);
		return sum/count;
	}
	
	public static double getMelodicValue(NotePos[] melody){
		double sum = 0.0;
		int count = 0;
		for (int i = 0; i < melody.length - 1; i++) {
			int difference = (melody[i + 1].getPitch() - melody[i].getPitch())%12;
			Interval interval = Interval.getEnumInterval(difference);
//			LOGGER.info(interval + ": "+  interval.getMelodicValue() + "," );
			sum = sum + interval.getMelodicValue();
			count++;
		}
//		LOGGER.info("count :" + count);
		return sum/count;
	}
	
	public static double[] getMelodicWeights(NotePos[] melody, int allowIntervalsBelowValue){
		List<Double> list = new ArrayList<Double>();
		int count = 0;
		for (int i = 0; i < melody.length - 1; i++) {
			if (!melody[i + 1].isRest()) {
				int difference = (melody[i + 1].getPitch() - melody[i].getPitch());
				if (difference > allowIntervalsBelowValue) {
					count++;
					list.add(-1.0);
				} else {
					Interval interval = Interval.getEnumInterval(difference % 12);
					switch (interval.getInterval()) {//don't count note repetitions and octaves
					case 0:
					case 12:
						break;
					default:
						count++;
						list.add(interval.getMelodicValue());
						break;
					}
				}	
			}
		}
		Double[] melodicWeights = new Double[count];
		melodicWeights = list.toArray(melodicWeights);
		return ArrayUtils.toPrimitive(melodicWeights);
	}
	
	public static double[] getMelodicWeights2(Note[] melody, int allowIntervalsBelowValue){
		List<Double> list = new ArrayList<Double>();
		int count = 0;
		for (int i = 0; i < melody.length - 1; i++) {
			int difference = (melody[i + 1].getPitch() - melody[i].getPitch());
			if (difference > allowIntervalsBelowValue) {
				count++;
				list.add(-1.0);
			} else {
				Interval interval = Interval.getEnumInterval(difference % 12);
				switch (interval.getInterval()) {//don't count note repetitions and octaves
				case 0:
				case 12:
					break;
				default:
					count++;
					list.add(interval.getMelodicValue());
					break;
				}
			}	
		}
		Double[] melodicWeights = new Double[count];
		melodicWeights = list.toArray(melodicWeights);
		return ArrayUtils.toPrimitive(melodicWeights);
	}
	
	public static double getMelodicContour(Phrase phrase){
		Note[] melody = phrase.getNoteArray();
		double sum = 0.0;
		int count = 0;
		for (int i = 0; i < melody.length - 1; i++) {
			int difference = (melody[i + 1].getPitch() - melody[i].getPitch())%12;
			LOGGER.info("difference : "+  difference + "," );
			sum = sum + difference;
			count++;
		}
//		LOGGER.info("count :" + count);
		return sum;
	}
	
	public static List<Integer> getValueChanges(Phrase phrase){
		Note[] melody = phrase.getNoteArray();
		Map<Integer, Integer> map = new TreeMap<Integer,Integer>();
		List<Integer> valueChanges = new ArrayList<Integer>();
		for (Note note : melody) {
			int pitch = note.getPitch();// mod 12 octaaf equivalentie?
			if (map.containsKey(pitch)) {
				int count = map.get(pitch);
				valueChanges.add(count);//notes between events
				addOnePositionToMapValues(map);
				map.put(pitch, 0);
			}else{
				valueChanges.add(-1);//-1 = *
				addOnePositionToMapValues(map);
				map.put(pitch, 0);
			}
		}
		return valueChanges;
	}
	
	public static  <T> List<Integer> getValueChangesGeneric(T[] array){
		Map<T, Integer> map = new TreeMap<T,Integer>();
		List<Integer> valueChanges = new ArrayList<Integer>();
		for (T value : array) {
			if (map.containsKey(value)) {
				Integer count = map.get(value);
				valueChanges.add(count);//notes between events
				addOnePositionToMapValuesGeneric(map);
				map.put(value, 0);
			}else{
				valueChanges.add(-1);//-1 = *
				addOnePositionToMapValuesGeneric(map);
				map.put(value, 0);
			}
		}
		return valueChanges;
	}
	
	private static <T> void addOnePositionToMapValuesGeneric(Map<T, Integer> map) {
		for (T key : map.keySet()) {
			int value = map.get(key) + 1;
			map.put(key, value);
		}
	}
	
	public static List<Integer> getValueChangesMax(Phrase phrase){
		List<Integer> valueChanges = getValueChanges(phrase);
		List<Integer> newValueChanges = new ArrayList<Integer>();
		Integer max = Collections.max(valueChanges);
		for (Integer integer : valueChanges) {
			if (integer.equals(-1)) {
				newValueChanges.add(max + 1);
			} else {
				newValueChanges.add(integer);
			}
		}
		return newValueChanges;
	}
	
	public static List<Integer> getDifferentValueChanges(Phrase phrase){
		Note[] melody = phrase.getNoteArray();
		Map<Integer, Set<Integer>> map = new TreeMap<Integer,Set<Integer>>();
		List<Integer> valueChanges = new ArrayList<Integer>();
		for (Note note : melody) {
			int pitch = note.getPitch();
			if (map.containsKey(pitch)) {
				Set<Integer> set = map.get(pitch);
				valueChanges.add(set.size());//different notes between events
				addPitchToMap(map, pitch);
				set = new HashSet<Integer>();
				map.put(pitch, set);
			}else{
				valueChanges.add(-1);//-1 = *
				addPitchToMap(map, pitch);
				Set<Integer> set = new HashSet<Integer>();
				map.put(pitch, set);
			}
		}
		return valueChanges;
	}

	private static void addOnePositionToMapValues(Map<Integer, Integer> map) {
		for (Integer key : map.keySet()) {
			int value = map.get(key) + 1;
			map.put(key, value);
		}
	}
	
	private static void addPitchToMap(Map<Integer, Set<Integer>> map, int pitch){
		for (Integer key : map.keySet()) {
			Set<Integer> set = map.get(key);
			set.add(pitch);
			map.put(key, set);
		}
	}
	
	public static <T> double countZeroValueChanges(T[] array){
		List<Integer> valueChanges = getValueChangesGeneric(array);
		double newValues = 0;
		for (Integer integer : valueChanges) {
			if (integer == 0) {
				newValues++;
			}
		}
		return newValues/array.length;
	}
	
	public static <T> double countNewValueChanges(T[] array){
		List<Integer> valueChanges = getValueChangesGeneric(array);
		double newValues = 0;
		for (Integer integer : valueChanges) {
			if (integer == -1) {
				newValues++;
			}
		}
		return newValues/array.length;
	}

}
