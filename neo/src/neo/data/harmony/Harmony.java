package neo.data.harmony;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.pitchspace.BassOctavePitchSpace;
import neo.data.melody.pitchspace.MiddleOctavePitchSpace;
import neo.data.melody.pitchspace.PitchSpace;
import neo.data.melody.pitchspace.TopOctavePitchSpace;
import neo.data.melody.pitchspace.UniformPitchSpace;
import neo.data.note.Note;
import neo.data.note.Scale;

public class Harmony implements Comparable<Harmony>{
	
	protected int position;
	private Chord chord;
	private int length;
	private double positionWeight;
	private double innerMetricWeight;
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
	private PitchSpace pitchSpace;
	private Integer[] range = {5, 6};
	
	public Harmony(int position, int length, List<HarmonicMelody> harmonicMelodies) {
		this.position = position;
		this.length = length;
		this.harmonicMelodies = harmonicMelodies;
		this.pitchSpace = new UniformPitchSpace(range);
		this.pitchSpace.setNotes(getNotes());
		toChord();
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

	public void mutateNoteToPreviousPitchFromScale(Scale scale){
		HarmonicMelody harmonicMelody = getRandomHarmonicMelody();
		Note harmonyNote = harmonicMelody.getHarmonyNote();
		int oldPitchClass = harmonyNote.getPitchClass();
		int newPitchClass = scale.pickPreviousPitchFromScale(oldPitchClass);
		harmonicMelody.updateMelodyNotes(oldPitchClass, newPitchClass);
		harmonyNote.setPitchClass(newPitchClass);
		toChord();
	}
	
	public void mutateNoteRandom(Scale scale){
		List<HarmonicMelody> melodies = harmonicMelodies.stream()
				.filter(harmonicMelody -> harmonicMelody.getMelodyNotes().size() > 1)
				.collect(toList());
		if (!melodies.isEmpty()) {
			HarmonicMelody harmonicMelody = getRandomFromList(melodies);
			int newPitchClass = scale.pickRandomFromScale();
			harmonicMelody.updateMelodyNotes(newPitchClass);
		}
	}

	private HarmonicMelody getRandomHarmonicMelody() {
		int index = PseudoRandom.randInt(0, harmonicMelodies.size() - 1);
		return  harmonicMelodies.get(index);
	}
	
	private <T> T getRandomFromList(List<T> list) {
		int index = PseudoRandom.randInt(0, list.size() - 1);
		return list.get(index);
	}
	
	public void mutatePitchSpace(){
		int i = PseudoRandom.randInt(0, 3);
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
	
}
