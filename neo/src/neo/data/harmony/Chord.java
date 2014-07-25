package neo.data.harmony;

import java.util.Set;

import neo.data.setclass.PcSetUnorderedProperties;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class Chord {
//	private Set<Integer> pitchClassSet = new TreeSet<Integer>();
	private Multiset<Integer> pitchClassMultiSet = TreeMultiset.create();
	private ChordType chordType;
	private int voiceLeadingZone;
	private double weight;
	private String setClassName;

	public double getWeight() {
		return getChordType().getDissonance();
	}

	public ChordType getChordType() {
		this.chordType = extractChordType();
		return chordType;
	}
	
	public Multiset<Integer> getPitchClassMultiSet() {
		return pitchClassMultiSet;
	}
	
	public Set<Integer> getPitchClassSet() {
		return pitchClassMultiSet.elementSet();
	}

	public void addPitchClass(Integer pitchClass){
		pitchClassMultiSet.add(pitchClass);
	}
	
	public String getForteName() {
		Set<Integer> pitchClassSet = getPitchClassSet();
		int[] set = new int[pitchClassSet.size()];
		int i = 0;
		for (Integer pc : pitchClassSet) {
			set[i] = pc;
			i++;
		}
		PcSetUnorderedProperties pcSetCalculator = new PcSetUnorderedProperties(set);
		this.setClassName = pcSetCalculator.getForteName();
		return setClassName;
	}

	private ChordType extractChordType() {
		Set<Integer> pitchClassSet = getPitchClassSet();
		Integer[] chord = pitchClassSet.toArray(new Integer[pitchClassSet.size()]);
		switch (chord.length) {
		case 0:
			return ChordType.CH0;
		case 1:
			return ChordType.CH1;
		case 2:
			return ChordType.CH2;
		case 3:
			return getTriadicChordType(chord);
		case 4:
			return getTetraChordType(chord);
		case 5:
			return ChordType.CH5;
		default:
			throw new IllegalArgumentException("chord type doesn't exist?");
		}
	}

	private ChordType getTetraChordType(Integer[] chord) {
		int firstInterval = chord[1] - chord[0];
		int secondInterval = chord[2] - chord[1];
		int thirdInterval = chord[3] - chord[2];
		switch (firstInterval) {
		case 1:
			//maj7
			if (secondInterval == 4 && thirdInterval == 3) {
				return ChordType.MAJOR7;
			} 
			break;
		case 2:
			//dom7 - min7
			if (secondInterval == 4 && thirdInterval == 3) {
				return ChordType.DOM7;
			} else if (secondInterval == 3 && thirdInterval == 4) {
				return ChordType.MINOR7;
			} 
			break;
		case 3:
			if (secondInterval == 2) {
				if (thirdInterval == 3) {
					return chordType.MINOR7;
				} else if(thirdInterval == 4){
					return chordType.DOM7;
				}
			} else if (secondInterval == 3) {
				if (thirdInterval == 3) {
					return chordType.DIM;
				} else if(thirdInterval == 4){
					return chordType.HALFDIM7;
				} else if(thirdInterval == 2){
					return chordType.DOM7;
				}
			}else if (secondInterval == 4) {
				if (thirdInterval == 3) {
					return chordType.MINOR7;
				} else if(thirdInterval == 2){
					return chordType.HALFDIM7;
				} else if(thirdInterval == 1){
					return chordType.MAJOR7;
				}
			}
			break;
		case 4:
			if (secondInterval == 1) {
				if (thirdInterval == 4) {
					return chordType.MAJOR7;
				} 
			} else if (secondInterval == 2) {
				if (thirdInterval == 3) {
					return chordType.HALFDIM7;
				} 
			}else if (secondInterval == 3) {
				if (thirdInterval == 3) {
					return chordType.DOM7;
				} else if(thirdInterval == 4){
					return chordType.MAJOR7;
				} else if(thirdInterval == 2){
					return chordType.MINOR7;
				}
			}
			break;
		}
		return ChordType.CH4;	
	}

	private static ChordType getTriadicChordType(Integer[] chord) {
		int firstInterval = chord[1] - chord[0];
		if (firstInterval == 1 || firstInterval == 2) {
			return ChordType.CH3;
		}
		int secondInterval = chord[2] - chord[1];
		if (firstInterval == 3) {
			if (secondInterval == 3 || secondInterval == 6) {
				return ChordType.HALFDIM;
			} else if (secondInterval == 4) {
				return ChordType.MINOR;
			} else if (secondInterval == 5) {
				return ChordType.MAJOR;
			}
		} else if (firstInterval == 4) {
			if (secondInterval == 3) {
				return ChordType.MAJOR;
			} else if (secondInterval == 4) {
				return ChordType.AUGM;
			} else if (secondInterval == 5) {
				return ChordType.MINOR;
			}
		} else if (firstInterval == 5) {
			if (secondInterval == 3) {
				return ChordType.MINOR;
			} else if (secondInterval == 4) {
				return ChordType.MAJOR;
			}
		} else if (firstInterval == 6 && secondInterval == 3) {
			return ChordType.HALFDIM;
		}
		return ChordType.CH3;
	}

}
