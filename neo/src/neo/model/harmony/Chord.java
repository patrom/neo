package neo.model.harmony;

import java.util.Set;

import neo.model.setclass.PcSetUnorderedProperties;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class Chord {
	
	private Multiset<Integer> pitchClassMultiSet = TreeMultiset.create();
	private ChordType chordType;
	private int voiceLeadingZone;
	private int bassNote;

	public Chord(int bassNote) {
		this.bassNote = bassNote;
	}

	public ChordType getChordType() {
		if (chordType == null) {
			this.chordType = extractChordType(bassNote);
		}
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
		if (getPitchClassSet().size() < 2) {
			return "";
		} else {
			return getPcSetUnorderedProperties().getForteName();
		}
	}
	
	public String[] getSetClassProperties() {
		return getPcSetUnorderedProperties().getSetClassProperties();
	}
	
	public PcSetUnorderedProperties getPcSetUnorderedProperties(){
		int[] set = toPrimitiveSet(getPitchClassSet());
		return new PcSetUnorderedProperties(set);
	}

	private int[] toPrimitiveSet(Set<Integer> pitchClassSet) {
		int[] set = new int[pitchClassSet.size()];
		int i = 0;
		for (Integer pc : pitchClassSet) {
			set[i] = pc;
			i++;
		}
		return set;
	}

	private ChordType extractChordType(int bassNote) {
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
			return getTriadicChordType(chord, bassNote);
		case 4:
			return getTetraChordType(chord, bassNote);
		case 5:
			return ChordType.CH5;
		case 6:
			return ChordType.CH5;
		default:
			throw new IllegalArgumentException("chord type doesn't exist?");
		}
	}

	private ChordType getTetraChordType(Integer[] chord, int bassNote) {
		int firstInterval = chord[1] - chord[0];
		int secondInterval = chord[2] - chord[1];
		int thirdInterval = chord[3] - chord[2];
		switch (firstInterval) {
		case 1:
			//maj7
			if (secondInterval == 4 && thirdInterval == 3) {
				int chordPosition = Math.abs(chord[1] - bassNote);
				return getMajor7Inversion(chordPosition);
			} 
			break;
		case 2:
			//dom7 - min7
			if (secondInterval == 4 && thirdInterval == 3) {
				return ChordType.DOM7;
			} else if (secondInterval == 3 && thirdInterval == 4) {
				int chordPosition = Math.abs(chord[1] - bassNote);
				return getMinor7Inversion(chordPosition);
			} 
			break;
		case 3:
			if (secondInterval == 2) {
				if (thirdInterval == 3) {
					int chordPosition = Math.abs(chord[2] - bassNote);
					return getMinor7Inversion(chordPosition);
				} else if(thirdInterval == 4){
					return ChordType.DOM7;
				}
			} else if (secondInterval == 3) {
				if (thirdInterval == 3) {
					return ChordType.DIM;
				} else if(thirdInterval == 4){
					return ChordType.HALFDIM7;
				} else if(thirdInterval == 2){
					return ChordType.DOM7;
				}
			}else if (secondInterval == 4) {
				if (thirdInterval == 3) {
					int chordPosition = Math.abs(chord[0] - bassNote);
					return getMinor7Inversion(chordPosition);
				} else if(thirdInterval == 2){
					return ChordType.HALFDIM7;
				} else if(thirdInterval == 1){
					int chordPosition = Math.abs(chord[3] - bassNote);
					return getMajor7Inversion(chordPosition);
				}
			}
			break;
		case 4:
			if (secondInterval == 1) {
				if (thirdInterval == 4) {
					int chordPosition = Math.abs(chord[2] - bassNote);
					return getMajor7Inversion(chordPosition);
				} 
			} else if (secondInterval == 2) {
				if (thirdInterval == 3) {
					return ChordType.HALFDIM7;
				} 
			}else if (secondInterval == 3) {
				if (thirdInterval == 3) {
					return ChordType.DOM7;
				} else if(thirdInterval == 4){
					int chordPosition = Math.abs(chord[0] - bassNote);
					return getMajor7Inversion(chordPosition);
				} else if(thirdInterval == 2){
					int chordPosition = Math.abs(chord[3] - bassNote);
					return getMinor7Inversion(chordPosition);
				}
			}
			break;
		}
		return ChordType.CH4;	
	}

	private ChordType getTriadicChordType(Integer[] chord, int bassNote) {
		int firstInterval = chord[1] - chord[0];
		if (firstInterval == 1) {
			return ChordType.CH3;
		}
		int secondInterval = chord[2] - chord[1];
		if (firstInterval == 2 && secondInterval == 4 ) {
			return ChordType.DOM;
		} else if (firstInterval == 3) {
			if (secondInterval == 3 || secondInterval == 6) {
				return ChordType.DIM;
			} else if (secondInterval == 4) {
				int chordPosition = Math.abs(chord[0] - bassNote);
				return getMinorInversion(chordPosition);
			} else if (secondInterval == 5) {
				int chordPosition = Math.abs(chord[2] - bassNote);
				return getMajorInversion(chordPosition);
			}
		} else if (firstInterval == 4) {
			if (secondInterval == 3) {
				int chordPosition = Math.abs(chord[0] - bassNote);
				return getMajorInversion(chordPosition);
			} else if (secondInterval == 4) {
				return ChordType.AUGM;
			} else if (secondInterval == 5) {
				int chordPosition = Math.abs(chord[2] - bassNote);
				return getMinorInversion(chordPosition);
			} else if (secondInterval == 6) {
				return ChordType.DOM;
			}
		} else if (firstInterval == 5) {
			if (secondInterval == 3) {
				int chordPosition = Math.abs(chord[1] - bassNote);
				return getMinorInversion(chordPosition);
			} else if (secondInterval == 4) {
				int chordPosition = Math.abs(chord[1] - bassNote);
				return getMajorInversion(chordPosition);
			}
		} else if (firstInterval == 6) {
			if (secondInterval == 3) {
				return ChordType.DIM;
			} else if (secondInterval == 2) {
				return ChordType.DOM;
			}
		}
		
		String forteName = getForteName();
		if ("3-9".equals(forteName)) {
			return ChordType.KWARTEN;
		}
		if ("3-4".equals(forteName)) {
			if (firstInterval == 4 && secondInterval == 7 ) {
				return ChordType.MAJOR7_OMIT5;
			}
		}
		if ("3-6".equals(forteName)) {
			if (firstInterval == 2 && secondInterval == 2 ) {
				return ChordType.ADD9;
			}
		}
		if ("3-7".equals(forteName)) {
			if (firstInterval == 3 && secondInterval == 7 ) {
				return ChordType.MINOR7_OMIT5;
			} else if (firstInterval == 7 && secondInterval == 2 ) {
				return ChordType.MINOR7_OMIT5_1;
			}
		}
		return ChordType.CH3;
	}

	private ChordType getMinorInversion(int chordPosition) {
		switch (chordPosition) {
			case 0:
				return ChordType.MINOR;
			case 3:
			case 9:
				return ChordType.MINOR_1;
			case 5:
			case 7:
				return ChordType.MINOR_2;
		}
		throw new IllegalArgumentException("No Inversion found for chord position: " + chordPosition);
	}
	
	private ChordType getMinor7Inversion(int chordPosition) {
		switch (chordPosition) {
			case 0:
				return ChordType.MINOR7;
			case 3:
			case 9:
				return ChordType.MINOR7_1;
			case 5:
			case 7:
				return ChordType.MINOR7_2;
			case 2:
			case 10:
				return ChordType.MINOR7_3;
		}
		throw new IllegalArgumentException("No Inversion found for chord position: " + chordPosition);
	}
	
	private ChordType getMajorInversion(int chordPosition) {
		switch (chordPosition) {
			case 0:
				return ChordType.MAJOR;
			case 4:
			case 8:
				return ChordType.MAJOR_1;
			case 5:
			case 7:
				return ChordType.MAJOR_2;
		}
		throw new IllegalArgumentException("No Inversion found for chord position: " + chordPosition);
	}
	
	private ChordType getMajor7Inversion(int chordPosition) {
		switch (chordPosition) {
			case 0:
				return ChordType.MAJOR7;
			case 4:
			case 8:
				return ChordType.MAJOR7_1;
			case 5:
			case 7:
				return ChordType.MAJOR7_2;
			case 1:
			case 11:
				return ChordType.MAJOR7_3;
		}
		throw new IllegalArgumentException("No Inversion found for chord position: " + chordPosition);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Type: ");
		builder.append(getChordType());
		builder.append(", Set: ");
		builder.append(getForteName());
		builder.append(", bassNote: ");
		builder.append(getbassNote());
		return builder.toString();
	}

	public int getVoiceLeadingZone() {
		return voiceLeadingZone;
	}

	public int getbassNote() {
		return bassNote;
	}

}
