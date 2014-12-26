package neo.model.perle;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neo.util.Util;

public class AxisDyadArray {

	private CyclicSet topCyclicSet;
	private CyclicSet bottomCyclicSet;
	private int[] topCycle;
	private int[] bottomCycle;
	private boolean differenceAlignment = false;
	
	public AxisDyadArray(CyclicSet topCyclicSet, int startTopCyclicSet, CyclicSet bottomCyclicSet, int startBottomCyclicSet) {
		this.topCyclicSet = topCyclicSet;
		this.bottomCyclicSet = bottomCyclicSet;
		
		BigInteger topLength = BigInteger.valueOf(topCyclicSet.getCyclicSet().length);
		BigInteger bottomLength = BigInteger.valueOf(bottomCyclicSet.getCyclicSet().length);
		BigInteger lcm = Util.lcm(topLength, bottomLength);
		
		topCycle = expandCyclicSet(topCyclicSet, topLength, lcm);
		bottomCycle = expandCyclicSet(bottomCyclicSet, bottomLength, lcm);
		
		if ((startTopCyclicSet + startBottomCyclicSet) % 2 == 0) {
			differenceAlignment = true;
		}
		if (startTopCyclicSet != 0) {
			this.topCycle = Util.rotateArray(topCycle, startTopCyclicSet);
		}
		if (startBottomCyclicSet != 0) {
			this.bottomCycle = Util.rotateArray(bottomCycle, startBottomCyclicSet);
		}
	}

	public String getName() {
		return topCyclicSet.getName() + "/" + bottomCyclicSet.getName();
	}
	
	public String getIntervalSystem(){
		return topCyclicSet.getCyclicInterval() + "," + bottomCyclicSet.getCyclicInterval();
	}
	
	public int getSynopticMode(){
		int intervalSystemDifference = getIntervalSystemDifference();
		if (intervalSystemDifference > 6) {
			return 12 - intervalSystemDifference;
		} else {
			return intervalSystemDifference;
		}
	}
	
	public int getSynopticKey(){
		int intervalSystemSum = getIntervalSystemSum();
		if (intervalSystemSum > 6) {
			return 12 - intervalSystemSum;
		} else {
			return intervalSystemSum;
		}
	}
	
	public boolean isDifferenceAlignment(){
		return differenceAlignment;
	}
	
	public int[] getDifferences(){
		if (isDifferenceAlignment()) {
			int[] differences = new int[3];
			differences[0] = getSecondaryDifference(0);
			differences[1] = getAxisDyadDifference();
			differences[2] = getSecondaryDifference(2);
			return differences;
		}
		return null;
	}
	
	public int[] getSums(){
		if (!isDifferenceAlignment()) {
			int[] sums = new int[3];
			sums[0] = getSecondarySum(0);
			sums[1] = getAxisDyadSum();
			sums[2] = getSecondarySum(2);
			return sums;
		}
		return null;
	}
	
	public String getMode(){
		return ((12 + getAxisDyadDifference() - getSecondaryDifference(0)) % 12) + 
				"," + ((12 + getAxisDyadDifference() - getSecondaryDifference(2)) % 12);
	}
	
	public String getKey(){
		return ((getAxisDyadSum() + getSecondarySum(0)) % 12) 
				+ "," + ((getAxisDyadSum() + getSecondarySum(2)) % 12);
	}
	
	public int getAxisDyadDifference(){
		return (12 + topCycle[1] - bottomCycle[1]) % 12;
	}
	
	public int getAxisDyadSum(){
		return (topCycle[1] + bottomCycle[1]) % 12;
	}
	
	public int getSecondaryDifference(int position){
		return (12 + bottomCycle[position] - topCycle[position]) % 12;
	}
	
	public int getSecondarySum(int position){
		return (bottomCycle[position] + topCycle[position]) % 12;
	}
	
	/**
	 * @param axisDyadPosition position in array
	 */
	public List<Integer> getAxisDyadChord(int axisDyadPosition){
		List<Integer> pitchClasses = new ArrayList<>();
		pitchClasses.add(topCycle[axisDyadPosition - 1]);
		pitchClasses.add(topCycle[axisDyadPosition]);
		pitchClasses.add(topCycle[(axisDyadPosition + 1) % topCycle.length]);
		pitchClasses.add(bottomCycle[axisDyadPosition - 1]);
		pitchClasses.add(bottomCycle[axisDyadPosition]);
		pitchClasses.add(bottomCycle[(axisDyadPosition + 1) % bottomCycle.length]);
		return pitchClasses;
	}
	
	/**
	 * @param axisDyadPosition position in array
	 */
	public List<Integer> getCyclicChord(int axisDyadPosition){
		if (!isAxisDyadPosition(axisDyadPosition)) {
			throw new IllegalArgumentException("Not an axis dyad position");
		}
		List<Integer> pitchClasses = new ArrayList<>();
		pitchClasses.add(topCycle[axisDyadPosition - 1]);
		pitchClasses.add(topCycle[(axisDyadPosition + 1) % topCycle.length]);
		pitchClasses.add(bottomCycle[axisDyadPosition - 1]);
		pitchClasses.add(bottomCycle[(axisDyadPosition + 1) % bottomCycle.length]);
		return pitchClasses;
	}
	
	/**
	 * @param axisDyadPosition position in array
	 */
	public List<Integer> getSumTetraChordLeft(int axisDyadPosition){
		if (!isAxisDyadPosition(axisDyadPosition)) {
			throw new IllegalArgumentException("Not an axis dyad position");
		}
		List<Integer> pitchClasses = new ArrayList<>();
		pitchClasses.add(topCycle[axisDyadPosition - 1]);
		pitchClasses.add(topCycle[axisDyadPosition]);
		pitchClasses.add(bottomCycle[axisDyadPosition - 1]);
		pitchClasses.add(bottomCycle[axisDyadPosition]);
		return pitchClasses;
	}
	
	/**
	 * @param axisDyadPosition position in array
	 */
	public List<Integer> getSumTetraChordRight(int axisDyadPosition){
		if (!isAxisDyadPosition(axisDyadPosition)) {
			throw new IllegalArgumentException("Not an axis dyad position");
		}
		List<Integer> pitchClasses = new ArrayList<>();
		pitchClasses.add(topCycle[axisDyadPosition]);
		pitchClasses.add(topCycle[(axisDyadPosition + 1) % topCycle.length]);
		pitchClasses.add(bottomCycle[axisDyadPosition]);
		pitchClasses.add(bottomCycle[(axisDyadPosition + 1) % bottomCycle.length]);
		return pitchClasses;
	}
	
	public List<List<Integer>> getAllAxisDyadChords(){
		List<List<Integer>> allChords = new ArrayList<List<Integer>>();
		for (int i = 1; i < topCycle.length; i = i + 2) {
			allChords.add(getAxisDyadChord(i));
		}
		return allChords;
	}
	
	public List<List<Integer>> getAllCyclicChords(){
		List<List<Integer>> allChords = new ArrayList<List<Integer>>();
		for (int i = 1; i < topCycle.length; i = i + 2) {
			allChords.add(getCyclicChord(i));
		}
		return allChords;
	}
	
	public List<List<Integer>> getAllSumTetraChordsLeft(){
		List<List<Integer>> allChords = new ArrayList<List<Integer>>();
		for (int i = 1; i < topCycle.length; i = i + 2) {
			allChords.add(getSumTetraChordLeft(i));
		}
		return allChords;
	}
	
	public List<List<Integer>> getAllSumTetraChordsRight(){
		List<List<Integer>> allChords = new ArrayList<List<Integer>>();
		for (int i = 1; i < topCycle.length; i = i + 2) {
			allChords.add(getSumTetraChordRight(i));
		}
		return allChords;
	}

	
	public String printArray(){
		StringBuilder builder = new StringBuilder();
		builder.append(Arrays.toString(topCycle));
		builder.append(System.getProperty("line.separator"));
		builder.append(Arrays.toString(bottomCycle));
		return builder.toString();
	}
	
	private int[] expandCyclicSet(CyclicSet cyclicSet, BigInteger length,
			BigInteger lcm) {
		int factor = lcm.divide(length).intValue();
		int[] cycle = new int[lcm.intValue()];
		if (factor > 1) {
			for (int i = 0; i < lcm.intValue(); i++) {
				cycle[i] = cyclicSet.getCyclicSet()[i % length.intValue()];
			}
		} else {
			cycle = cyclicSet.getCyclicSet();
		}
		return cycle;
	}
	
	public int getAggregateSum(){
		return (topCyclicSet.getLeftTonicSum() + topCyclicSet.getRightTonicSum()
				+ bottomCyclicSet.getLeftTonicSum() + bottomCyclicSet.getRightTonicSum()) % 12;
	}
	
	private boolean isAxisDyadPosition(int position){
		return position % 2 == 1 && topCycle.length > position;
	}
	
	private int getIntervalSystemSum(){
		return (topCyclicSet.getCyclicInterval() + bottomCyclicSet.getCyclicInterval()) % 12;
	}
	
	private int getIntervalSystemDifference(){
		return Math.abs(topCyclicSet.getCyclicInterval() - bottomCyclicSet.getCyclicInterval());
	}
	
	public int getTonality(){
		switch (getAggregateSum()) {
			case 0:
			case 4:
			case 8:	
				return 0;
			case 2:
			case 6:
			case 10:	
				return 2;
			default:
				return 1;
		}
	}
	
	public boolean isCognateSet(){
		if (isDifferenceAlignment()) {
			int[] differences = getDifferences();
			return (differences[0] == differences[1]) && (differences[1] == differences[2]);
		}else{
			int[] sums = getSums();
			return (sums[0] == sums[1]) && (sums[1] == sums[2]);
		}
	}
	
	public void transpose(int step){
		topCyclicSet.transpose(step);
		topCycle = topCyclicSet.getCyclicSet();
		bottomCyclicSet.transpose(step);
		bottomCycle = bottomCyclicSet.getCyclicSet();
	}
	
	public void semiTranspose(int step){
		topCyclicSet.semiTranspose(step);
		topCycle = topCyclicSet.getCyclicSet();
		bottomCyclicSet.semiTranspose(step);
		bottomCycle = bottomCyclicSet.getCyclicSet();
	}
	
	public void inverse(int step){
		//TODO
	}
	
	public boolean containsAxisDyadChord(List<Integer> chord){
		if (chord.isEmpty()) {
			return false;
		}
		List<List<Integer>> axisDyadChords = getAllAxisDyadChords();
//		return axisDyadChords.stream()
//				.anyMatch(list -> list.containsAll(chord) && chord.containsAll(list));
		return axisDyadChords.stream()
				.anyMatch(list -> list.containsAll(chord));
	}
	
	public boolean containsAllAxisDyadChords(List<List<Integer>> chords){
		if (chords.isEmpty()) {
			return false;
		}
		return chords.stream().allMatch(chord -> {return containsAxisDyadChord(chord);});
//		System.out.println("match: " + match);
//		for (List<Integer> chord : chords) {
//			if (!containsAxisDyadChord(chord)) {
//				return false;
//			}
//		}
//		return true;
	}
	
	public boolean containsSumTetraChord(List<Integer> chord){
		List<List<Integer>> sumTetraChords = getAllSumTetraChordsLeft();
		sumTetraChords.addAll(getAllSumTetraChordsRight());
		return sumTetraChords.stream()
				.anyMatch(list -> list.containsAll(chord) && chord.containsAll(list));
	}
	
	public boolean containsAllSumTetraChords(List<List<Integer>> chords){
		if (chords.isEmpty()) {
			return false;
		}
		for (List<Integer> chord : chords) {
			if (!containsSumTetraChord(chord)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean containsCyclicChord(List<Integer> chord){
		List<List<Integer>> cyclicChord = getAllCyclicChords();
		return cyclicChord.stream()
				.anyMatch(list -> list.containsAll(chord) && chord.containsAll(list));
	}
	
	public boolean containsAllCyclicChords(List<List<Integer>> chords){
		if (chords.isEmpty()) {
			return false;
		}
//		boolean match = chords.stream().allMatch(chord -> {return containsCyclicChord(chord);});
//		System.out.println("match: " + match);
		for (List<Integer> chord : chords) {
			if (!containsCyclicChord(chord)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(getName() + ", ");
		builder.append("IS: " + getIntervalSystem() + ", ");
		builder.append("Mode: " + getMode() + ", ");
		builder.append("Key: " + getKey() + ", ");
		if (isDifferenceAlignment()) {
			builder.append("Diff: " + Arrays.toString(getDifferences()) + ", ");
		}else{
			builder.append("Sum: " + Arrays.toString(getSums()) + ", ");
		}
		builder.append("SM: " + getSynopticMode() + ", ");
		builder.append("SK: " + getSynopticKey() + ", ");
		builder.append("Tonality: " + getTonality() + ", " );
		builder.append("Cognate: " + isCognateSet());
		builder.append(System.getProperty("line.separator"));
		return builder.toString();
	}
	
}
