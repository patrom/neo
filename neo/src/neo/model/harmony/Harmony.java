package neo.model.harmony;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.generic.NEW;

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
	
	public void searchBestChord(){
//		Set<Integer> allPositions = harmonicMelodies.stream().flatMap(m -> m.getMelodyNotes().stream())
//				.map(note -> note.getPosition())
//				.collect(toCollection(TreeSet::new));
//		int voice = 0;
		Map<Integer, List<Note>> harmonyPositions = harmonicMelodies.stream()
				.flatMap(harmonicMelody -> harmonicMelody.getMelodyNotes().stream())
				.collect(Collectors.collectingAndThen(
						groupingBy(note -> note.getPosition(), toList()),
			 			(map) -> { 
			 				Map<Integer, List<Note>> sortedMap = new TreeMap<>(map);
			 				List<Note> previous = new ArrayList<>();
			 				for (Entry<Integer, List<Note>> harmonyPosition : sortedMap.entrySet()) {
			 					List<Note> current = harmonyPosition.getValue();
			 					if (previous.isEmpty()) {//first element
			 						previous = new ArrayList<>(current);
								} else {
									List<Note> temp = new ArrayList<>(previous);
				 					for (Note currentNote : current) {
										for (Note previousNote : previous) {
											if (currentNote.getVoice() == previousNote.getVoice()) {
												temp.remove(previousNote);
												temp.add(currentNote);
											}
										}
									}
				 					temp.sort(comparing(Note::getVoice));
				 					harmonyPosition.setValue(temp);
				 					previous = new ArrayList<>(temp);
								}
							}
			 				return sortedMap;}
			 	));
		harmonyPositions.forEach((k,v) -> System.out.println(k + ":" + v));
		
		List<Note> bestChord = null;
		double max = 0;
		for (Entry<Integer, List<Note>> harmonyPosition : harmonyPositions.entrySet()) {
//			harmonyPosition.getValue().stream().map(note -> {
//				Chord chord = new Chord();
//				chord.addPitchClass(note.getPitchClass());
//				return chord;
//			}).max(Comparator.comparing(Chord::getChordType));
			Chord chord = new Chord();
			for (Note note : harmonyPosition.getValue()){
				chord.addPitchClass(note.getPitchClass());
			}
			double dissonance = chord.getChordType().getDissonance();
			if (dissonance > max) {
				max = dissonance;
				bestChord = harmonyPosition.getValue();
			}
		}
		
		//best chord to harmony notes
		for (Note note : bestChord) {
			harmonicMelodies.stream().filter(h -> h.getVoice() == note.getVoice()).forEach(h -> h.setHarmonyNote(note));
		}
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
		HarmonicMelody harmonicMelody = getRandomFromList(harmonicMelodies);
		harmonicMelody.mutateHarmonyNoteToPreviousPitchFromScale(scale);
		toChord();
	}
	
	public void mutateMelodyNoteToHarmonyNote(){
		HarmonicMelody harmonicMelody = getRandomFromList(harmonicMelodies.stream()
				.filter(h -> h.getMelodyNotes().size() > 1)
				.collect(toList()));
		if (harmonicMelody != null) {
			Note harmonyNote = getRandomFromList(getNotes());
			harmonicMelody.mutateMelodyNoteToHarmonyNote(harmonyNote.getPitchClass());
			toChord();
		}
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
		HarmonicMelody harmonicMelody = getRandomFromList(harmonicMelodies);
		HarmonicMelody switchHarmonicMelody = getRandomFromList(harmonicMelodies);
		if (harmonicMelody.getVoice() != switchHarmonicMelody.getVoice()) {
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
	}

	private <T> T getRandomFromList(List<T> list) {
		return RandomUtil.getRandomFromList(list);
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
