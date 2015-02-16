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

import neo.model.Motive;
import neo.model.dissonance.Dissonance;
import neo.model.dissonance.TonalDissonance;
import neo.model.harmony.Chord;
import neo.model.harmony.ChordType;
import neo.model.melody.Melody;
import neo.model.note.Interval;
import neo.model.note.Note;
import neo.objective.Objective;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MelodicObjective extends Objective {
	
	@Autowired
	@Qualifier(value="TonalDissonance")
	private Dissonance dissonance;
	
	@Override
	public double evaluate(Motive motive) {
		double totalMelodySum = 0;
		List<Melody> melodies = motive.getMelodies();
		int melodyCount = melodies.size();
		for(Melody melody: melodies){
			List<Note> notes =  melody.getMelodieNotes();
			double melodyValue = evaluateMelody(notes, 2);
//			notes = extractNotesOnLevel(notes, 1);
			for (double level : musicProperties.getFilterLevels()) {
				List<Note> filteredNotes = filterNotesWithWeightEqualToOrGreaterThan(notes, level);
				if ((filteredNotes.size() > 1) && filteredNotes.size() != notes.size()) {
					double value = evaluateMelody(filteredNotes, 1);
					totalMelodySum = totalMelodySum + value;
					melodyCount++;
				}
			}
			totalMelodySum = totalMelodySum + melodyValue;
		}
		return totalMelodySum/melodyCount;
	}
	
	protected List<Note> filterNotesWithWeightEqualToOrGreaterThan(Collection<Note> notes, double filterValue){
		return notes.stream().filter(n -> n.getWeightedSum() > filterValue).collect(toList());
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
				Chord chord = new Chord(firstNote.getPitchClass());
				chord.addPitchClass(firstNote.getPitchClass());
				chord.addPitchClass(secondNote.getPitchClass());
				chord.addPitchClass(thirdNote.getPitchClass());
				if ("3-11".equals(chord.getForteName())) {
					harmonicValue = harmonicValue + dissonance.getDissonance(chord);
				}
		}
		return (harmonicValue == 0)? 0:harmonicValue/(notePositions.length - 2);
	}

	protected double evaluateMelody(List<Note> notes, int maxDistance) {
		if (notes.size() == 1) {
			throw new IllegalStateException("size");
		}
		double totalPositionWeigth = 0;
		Note[] notePositions = notes.toArray(new Note[notes.size()]);
		double melodyIntervalValueSum = 0;
		for (int distance = 1; distance <= maxDistance; distance++) {
			for (int j = 0; j < notePositions.length - distance; j++) {
				Note note = notePositions[j];
				Note nextNote = notePositions[j + distance];
				double intervalPositionWeight = (note.getWeightedSum() + nextNote.getWeightedSum());
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
