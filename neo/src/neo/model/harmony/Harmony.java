package neo.model.harmony;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import neo.midi.HarmonyCollector;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.BassOctavePitchSpace;
import neo.model.melody.pitchspace.MiddleOctavePitchSpace;
import neo.model.melody.pitchspace.PitchSpace;
import neo.model.melody.pitchspace.TopOctavePitchSpace;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;

public class Harmony implements Comparable<Harmony>{
	
	protected int position;
	private Chord chord;
	private int length;
	private double positionWeight;
	private double innerMetricWeight;
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
	private PitchSpace pitchSpace;
	private Integer[] range;
	
	public Harmony(int position, int length, List<HarmonicMelody> harmonicMelodies, Integer[] range) {
		this.position = position;
		this.length = length;
		this.harmonicMelodies = harmonicMelodies;
		this.range = range;
		this.pitchSpace = new UniformPitchSpace(range);
		this.pitchSpace.setNotes(getNotes());
		toChord();
	}
	
	public void search(){
		Set<Integer> allPositions = harmonicMelodies.stream().flatMap(m -> m.getMelodyNotes().stream())
				.map(note -> note.getPosition())
				.collect(toCollection(TreeSet::new));
		int voice = 0;
		Map<Integer, List<Note>> harmonyPositions = harmonicMelodies.stream()
				.filter(harmonicMelody -> harmonicMelody.getVoice() == voice)
				.flatMap(harmonicMelody -> harmonicMelody.getMelodyNotes().stream())
				.collect(Collectors.collectingAndThen(
						Collectors.groupingBy(note -> note.getPosition(), Collectors.toList()),
			 			(value) -> { 
			 				List<Note> note = null;
			 				for (Integer position : allPositions) {
			 					if (value.containsKey(position)) {
			 						note = value.get(position);
								} else{
									value.put(position, note);
								}
							}
			 				return value;}
			 	));
	}
	
	public List<Note> getNotes() {
		return harmonicMelodies.stream()
				.map(harmonicMelodic -> harmonicMelodic.getHarmonyNote())
				.collect(toList());
	}
	
	public List<Integer> getPitchClasses(){
		return harmonicMelodies.stream()
				.map(harmonicMelodic -> harmonicMelodic.getHarmonyNote().getPitchClass())
				.collect(toList());
	}
	
	private void toChord(){
		this.chord = new Chord();
		harmonicMelodies.stream()
			.map(harmonicMelodic -> harmonicMelodic.getHarmonyNote())
			.forEach(note -> chord.addPitchClass(note.getPitchClass()));
	}

	public void translateToPitchSpace() {
		pitchSpace.translateToPitchSpace();
		this.harmonicMelodies.forEach(harmonicMelody -> harmonicMelody.updateMelodyPitchesToHarmonyPitch());
	}

	public void mutateHarmonyNoteToPreviousPitchFromScale(Scale scale){
		HarmonicMelody harmonicMelody = getRandomHarmonicMelody();
		Note harmonyNote = harmonicMelody.getHarmonyNote();
		int oldPitchClass = harmonyNote.getPitchClass();
		int newPitchClass = scale.pickPreviousPitchFromScale(oldPitchClass);
		harmonicMelody.updateMelodyNotes(oldPitchClass, newPitchClass);
		harmonyNote.setPitchClass(newPitchClass);
		toChord();
	}
	
	public void mutateMelodyNoteRandom(Scale scale){
		List<HarmonicMelody> melodies = harmonicMelodies.stream()
				.filter(harmonicMelody -> harmonicMelody.getMelodyNotes().size() > 1)
				.collect(toList());
		if (!melodies.isEmpty()) {
			HarmonicMelody harmonicMelody = getRandomFromList(melodies);
			int newPitchClass = scale.pickRandomPitchClass();
			harmonicMelody.randomUpdateMelodyNotes(newPitchClass);
		}
	}
	
	public void swapHarmonyNotes(){
		HarmonicMelody harmonicMelody = getRandomHarmonicMelody();
		HarmonicMelody switchHarmonicMelody = getRandomHarmonicMelody();
		Note harmonyNote = harmonicMelody.getHarmonyNote();
		Note switchHarmonyNote = switchHarmonicMelody.getHarmonyNote();
		if (!harmonyNote.equals(switchHarmonyNote)) {
			int harmonyPitchClass = harmonyNote.getPitchClass();
			int switchHarmonyPitchClass = switchHarmonyNote.getPitchClass();
			harmonicMelody.updateMelodyNotes(harmonyPitchClass, switchHarmonyPitchClass);
			harmonyNote.setPitchClass(switchHarmonyPitchClass);
			switchHarmonicMelody.updateMelodyNotes(switchHarmonyPitchClass, harmonyPitchClass);
			switchHarmonyNote.setPitchClass(harmonyPitchClass);
		}
	}

	private HarmonicMelody getRandomHarmonicMelody() {
		int index = RandomUtil.randomInt(0, harmonicMelodies.size());
		return  harmonicMelodies.get(index);
	}
	
	private <T> T getRandomFromList(List<T> list) {
		int index = RandomUtil.randomInt(0, list.size());
		return list.get(index);
	}
	
	public void mutatePitchSpace(){
		int i = RandomUtil.randomInt(0, 4);
		switch (i) {
		case 0:
			this.pitchSpace = new UniformPitchSpace(range);
			break;
		case 1:
			this.pitchSpace = new BassOctavePitchSpace(range);
			break;
		case 2:
			this.pitchSpace = new TopOctavePitchSpace(range);
			break;
		case 3:
			this.pitchSpace =new  MiddleOctavePitchSpace(range);
			break;
		}
		this.pitchSpace.setNotes(getNotes());
	}

	public double getPositionWeight() {
		return positionWeight;
	}

	public void setPositionWeight(double positionWeight) {
		harmonicMelodies.stream()
			.map(harmonicMelodic -> harmonicMelodic.getHarmonyNote())
			.forEach(note -> note.setPositionWeight(positionWeight));
		this.positionWeight = positionWeight;
	}
	
	public void transpose(int t){
		harmonicMelodies.stream()
			.map(harmonicMelodic -> harmonicMelodic.getHarmonyNote())
			.forEach(note -> note.setPitchClass((note.getPitchClass() + t) % 12));
		toChord();
	}

	@Override
	public int compareTo(Harmony harmony) {
		if (getPosition() < harmony.getPosition()) {
			return -1;
		} if (getPosition() > harmony.getPosition()) {
			return 1;
		} else {
			return 0;
		}
	}

	public List<HarmonicMelody> getHarmonicMelodies() {
		return harmonicMelodies;
	}
	
	public void setHarmonicMelodies(List<HarmonicMelody> harmonicMelodies) {
		this.harmonicMelodies = harmonicMelodies;
	}
	
	public void addHarmonicMelody(HarmonicMelody harmonicMelody){
		harmonicMelodies.add(harmonicMelody);
	}
	
	public PitchSpace getPitchSpace() {
		return pitchSpace;
	}
	
	public void setPitchSpace(PitchSpace pitchSpace) {
		this.pitchSpace = pitchSpace;
	}
	
	public int getPosition() {
		return position;
	}
	
	public double getBeat(int divider) {
		return Math.floor(position/divider);
	}

	public double getChordWeight() {
		return chord.getWeight();
	}

	public Chord getChord() {
		return chord;
	}

	public int getLength() {
		return length;
	}

	public double getInnerMetricWeight() {
		return innerMetricWeight;
	}

	public void setInnerMetricWeight(double innerMetricWeight) {
		this.innerMetricWeight = innerMetricWeight;
	}

	public Integer[] getRange() {
		return range;
	}
	
}
