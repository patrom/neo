package neo.data.harmony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jmetal.util.PseudoRandom;
import neo.data.melody.HarmonicMelody;
import neo.data.note.Note;
import neo.data.note.Scale;

public class Harmony implements Comparable<Harmony>{
	
	protected int position;
	protected List<Note> notes;
	private PitchSpaceStrategy pitchSpaceStrategy;
	private Chord chord;
	private int length;
	private double positionWeight;
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
	
	public Harmony(int position, int length, List<Note> notes) {
		this.position = position;
		this.length = length;
		this.notes = notes;
		toChord();
	}
	
	public int getPosition() {
		return position;
	}
	
	public List<Note> getNotes() {
		return Collections.unmodifiableList(notes);
	}
	
	public List<Integer> getPitchClasses(){
		return notes.stream().map(note -> note.getPitchClass()).collect(Collectors.toList());
	}
	
	private void toChord(){
		this.chord = new Chord();
		for (Note note : notes) {
			chord.addPitchClass(note.getPitchClass());
		}
	}

	public double getBeat(int divider) {
		return Math.floor(position/divider);
	}

	public void translateToPitchSpace() {
		pitchSpaceStrategy.translateToPitchSpace();
	}

	public PitchSpaceStrategy getPitchSpaceStrategy() {
		return pitchSpaceStrategy;
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
	
	public void mutateNoteToPreviousPitchFromScale(Scale scale){
		int noteIndex = PseudoRandom.randInt(0, notes.size() - 1);
		Note note = notes.get(noteIndex);
		int newPitchClass = scale.pickPreviousPitchFromScale(note.getPitchClass());
		note.setPitchClass(newPitchClass);
		note.setPitch((note.getOctave() * 12) + newPitchClass);
		toChord();
		updateHarmonicMelodies(note);
	}
	
	protected void updateHarmonicMelodies(Note updateNote){
//		harmonicMelodies.stream()
//			.flatMap(harmMelody -> harmMelody.getNotes().stream())
//			.peek(n -> System.out.print(n.getPitchClass()))
//			.filter(note -> note.isChordNote() && note.getVoice() == updateNote.getVoice())
//			.peek(n -> System.out.print(n.getPitchClass()))
//			.forEach(note -> note.updateNote(updateNote));
	}
	
	public void mutatePitchSpaceStrategy(){
		pitchSpaceStrategy.mutateOctaveHighestPitchClass();
		int i = PseudoRandom.randInt(0, 2);
		switch (i) {
		case 0:
			if (!(pitchSpaceStrategy instanceof UniformPitchSpace)) {
				this.pitchSpaceStrategy = new UniformPitchSpace(pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		case 1:
			if (!(pitchSpaceStrategy instanceof BassOctavePitchSpace)) {
				this.pitchSpaceStrategy = new BassOctavePitchSpace(pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		case 2:
			if (!(pitchSpaceStrategy instanceof TopOctavePitchSpace)) {
				this.pitchSpaceStrategy = new TopOctavePitchSpace(pitchSpaceStrategy.getOctaveHighestPitchClassRange());
			}
			break;
		}
	}

	public double getPositionWeight() {
		return positionWeight;
	}

	public void setPositionWeight(double positionWeight) {
		for (Note notePos : notes) {
			notePos.setPositionWeight(positionWeight);
		}
		this.positionWeight = positionWeight;
	}
	
	public void transpose(int t){
		notes.stream().forEach(note -> note.setPitchClass((note.getPitchClass() + t) % 12));
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
	
	public abstract class PitchSpaceStrategy {

		protected Integer[] octaveHighestPitchClassRange;
		protected int octaveHighestPitchClass;
		protected int size;

		public PitchSpaceStrategy(Integer[] octaveHighestPitchClasses) {
			this.octaveHighestPitchClass = octaveHighestPitchClasses[0];
			this.octaveHighestPitchClassRange = octaveHighestPitchClasses;
			this.size = notes.size();
		}

		public abstract void translateToPitchSpace();
		
		public int getOctaveHighestPitchClass() {
			return octaveHighestPitchClass;
		}
		
		public void mutateOctaveHighestPitchClass(){
			this.octaveHighestPitchClass = PseudoRandom.randInt(octaveHighestPitchClassRange[0], octaveHighestPitchClassRange[octaveHighestPitchClassRange.length - 1]);
		}

		protected void setPitchClassFirstNote() {
			Note firstNote = notes.get(0);
			firstNote.setPitch(firstNote.getPitchClass() + (12 * octaveHighestPitchClass));
			firstNote.setOctave(octaveHighestPitchClass);
		}
		
		protected void setUniformPitchSpace(){
			setPitchClassFirstNote();
			for (int i = 1; i < size; i++) {
				Note prevNote = notes.get(i - 1);
				Note note = notes.get(i);
				int prevPc = prevNote.getPitchClass();
				int pc = note.getPitchClass();
				if (pc > prevPc) {
					note.setPitch(prevNote.getPitch() - (12 - (pc - prevPc)));
					note.setOctave(prevNote.getOctave() - 1);
				} else {
					note.setPitch(prevNote.getPitch() - (prevPc - pc));
					note.setOctave(prevNote.getOctave());
				}
			}
		}

		public Integer[] getOctaveHighestPitchClassRange() {
			return octaveHighestPitchClassRange;
		}

	}
	
	public class UniformPitchSpace extends PitchSpaceStrategy {

		public UniformPitchSpace(Integer[] octaveHighestPitchClasses) {
			super(octaveHighestPitchClasses);
		}

		@Override
		public void translateToPitchSpace() {
			setUniformPitchSpace();
		}

	}

	
	public class BassOctavePitchSpace extends PitchSpaceStrategy {

		public BassOctavePitchSpace(Integer[] octaveHighestPitchClasses) {
			super(octaveHighestPitchClasses);
		}

		@Override
		public void translateToPitchSpace() {
			setUniformPitchSpace();
			Note lowestNote = notes.get(size - 1);
			lowestNote.setPitch(lowestNote.getPitch() - 12);
			lowestNote.setOctave(lowestNote.getOctave() - 1);
		}

	}
	
	public class TopOctavePitchSpace extends PitchSpaceStrategy {

		public TopOctavePitchSpace(Integer[] octaveHighestPitchClasses) {
			super(octaveHighestPitchClasses);
		}

		@Override
		public void translateToPitchSpace() {
			setUniformPitchSpace();
			for (int i = 1; i < size; i++) {
				Note note = notes.get(i);
				note.setPitch(note.getPitch() - 12);
				note.setOctave(note.getOctave() - 1);
			}
		}

	}

	public void setPitchSpaceStrategy(PitchSpaceStrategy pitchSpaceStrategy) {
		this.pitchSpaceStrategy = pitchSpaceStrategy;
	}
	
}
