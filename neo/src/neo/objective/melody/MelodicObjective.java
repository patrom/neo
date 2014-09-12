package neo.objective.melody;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Chord;
import neo.data.harmony.ChordType;
import neo.data.melody.Melody;
import neo.data.note.Interval;
import neo.data.note.Note;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class MelodicObjective extends Objective {
	
	private int minimumLength = musicProperties.getMinimumLength();

	public MelodicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		int maxDistance = 1;
		double totalMelodySum = 0;
		List<Melody> melodies = motive.getMelodies();
		for(Melody melody: melodies){
			List<Note> notes =  melody.getMelodieNotes();
//			notes = filterNotesAbove(notes, 0.5);
//			notes = extractNotesOnLevel(notes, 1);
			double melodyValue = evaluateMelody(notes, maxDistance);
			totalMelodySum = totalMelodySum + melodyValue;
		}
		return totalMelodySum/melodies.size();
	}
	
	protected List<Note> filterNotesWithPositionWeightAbove(Collection<Note> notes, double filterValue){
		return notes.stream().filter(n -> n.getPositionWeight() > filterValue).collect(toList());
	}

	protected List<Note> extractNotesOnLevel(Collection<Note> notes, int level) {
		Map<Double, List<Note>> unsortMap = notes.stream().collect(groupingBy(n -> n.getBeat(musicProperties.getHarmonyBeatDivider() * level)));
		Map<Double, List<Note>> treeMap = new TreeMap<Double, List<Note>>(unsortMap);
		List<Note> notePosition = new ArrayList<>();
		for (List<Note> noteList : treeMap.values()) {
			Optional<Note> maxNote = noteList.stream().max(comparing(Note::getWeightedSum));
			if (maxNote.isPresent()) {
				notePosition.add(maxNote.get());
			}
		}
		return notePosition;
	}
	
	protected double evaluateTriadicValueMelody(Collection<Note> notes) {
		Note[] notePositions = notes.toArray(new Note[notes.size()]);
		double harmonicValue = 0;
		for (int i = 0; i < notePositions.length - 2; i++) {
				Note firstNote = notePositions[i];
				Note secondNote = notePositions[i + 1];
				Note thirdNote = notePositions[i + 2];
				Chord chord = new Chord();
				chord.addPitchClass(firstNote.getPitchClass());
				chord.addPitchClass(secondNote.getPitchClass());
				chord.addPitchClass(thirdNote.getPitchClass());
				if (chord.getChordType().equals(ChordType.MAJOR) || chord.getChordType().equals(ChordType.MINOR)) {
					harmonicValue = harmonicValue + 1.0;
				}
		}
		return (harmonicValue == 0)? 0:harmonicValue/(notePositions.length - 2);
	}

	protected double evaluateMelody(List<Note> notes, int maxDistance) {
		double totalPositionWeigth = 0;
		Note[] notePositions = notes.toArray(new Note[notes.size()]);
		double melodyIntervalValueSum = 0;
		for (int distance = 1; distance <= maxDistance; distance++) {
			for (int j = 0; j < notePositions.length - distance; j++) {
				Note note = notePositions[j];
				Note nextNote = notePositions[j + distance];
				double intervalPositionWeight = (note.getPositionWeight() + nextNote.getPositionWeight());
				totalPositionWeigth = totalPositionWeigth + intervalPositionWeight;
				double intervalMelodicValue = getIntervalMelodicValue(note, nextNote);
				double intervalValue = intervalMelodicValue * intervalPositionWeight;
				melodyIntervalValueSum = melodyIntervalValueSum + intervalValue;
			}
		}
		return melodyIntervalValueSum/totalPositionWeigth;
	}

	private double getIntervalMelodicValue(Note note, Note nextNote) {
		int difference = nextNote.getPitch() - note.getPitch();
		return Interval.getEnumInterval(difference).getMelodicValue();
	}

}
