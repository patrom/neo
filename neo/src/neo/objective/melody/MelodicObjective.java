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
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class MelodicObjective extends Objective {
	
	private int minimumLength = musicProperties.getMinimumLength();

	public MelodicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
//		double totalWeight = weights.values().stream().mapToDouble(v -> v).sum();
		int maxDistance = 3;
		double totalMelodySum = 0;
		List<Melody> melodies = motive.getMelodies();
		for(Melody melody: melodies){
			Collection<NotePos> notes =  melody.getNotes();
//			notes = filterNotesAbove(notes, 0.5);
//			notes = extractNotesOnLevel(notes, 1);
			double melodyValue = evaluateMelody(notes, maxDistance);
			totalMelodySum = totalMelodySum + melodyValue;
		}
		return totalMelodySum/melodies.size();
	}
	
	protected List<NotePos> filterNotesWithPositionWeightAbove(Collection<NotePos> notes, double filterValue){
		return notes.stream().filter(n -> n.getPositionWeight() > filterValue).collect(toList());
	}

	protected List<NotePos> extractNotesOnLevel(Collection<NotePos> notes, int level) {
		Map<Double, List<NotePos>> unsortMap = notes.stream().collect(groupingBy(n -> n.getBeat(musicProperties.getHarmonyBeatDivider() * level)));
		Map<Double, List<NotePos>> treeMap = new TreeMap<Double, List<NotePos>>(unsortMap);
		List<NotePos> notePosition = new ArrayList<>();
		for (List<NotePos> noteList : treeMap.values()) {
			Optional<NotePos> maxNote = noteList.stream().max(comparing(NotePos::getWeightedSum));
			if (maxNote.isPresent()) {
				notePosition.add(maxNote.get());
			}
		}
		return notePosition;
	}
	
	protected double evaluateTriadicValueMelody(Collection<NotePos> notes) {
		NotePos[] notePositions = notes.toArray(new NotePos[notes.size()]);
		double harmonicValue = 0;
		for (int i = 0; i < notePositions.length - 2; i++) {
				NotePos firstNote = notePositions[i];
				NotePos secondNote = notePositions[i + 1];
				NotePos thirdNote = notePositions[i + 2];
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

	protected double evaluateMelody(Collection<NotePos> notes, int maxDistance) {
		double totalPositionWeigth = notes.stream().mapToDouble(n -> n.getPositionWeight()).sum();
		NotePos[] notePositions = notes.toArray(new NotePos[notes.size()]);
		double melodyIntervalValueSum = 0;
		for (int distance = 1; distance <= maxDistance; distance++) {
			for (int j = 0; j < notePositions.length - distance; j++) {
				NotePos note = notePositions[j];
				NotePos nextNote = notePositions[j + distance];
				double intervalPositionWeightAverage = (note.getPositionWeight() + nextNote.getPositionWeight())/totalPositionWeigth;
				double intervalMelodicValue = getIntervalMelodicValue(note, nextNote);
				double intervalValue = intervalMelodicValue * intervalPositionWeightAverage;
				melodyIntervalValueSum = melodyIntervalValueSum + intervalValue;
			}
		}
		return melodyIntervalValueSum/(notePositions.length - 1);
	}

	private double getIntervalMelodicValue(NotePos note, NotePos nextNote) {
		int difference = nextNote.getPitch() - note.getPitch();
		return Interval.getEnumInterval(difference).getMelodicValue();
	}

}
