package neo.model.rhythm;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;



import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import neo.model.note.Note;
import net.sourceforge.jFuzzyLogic.fcl.FclParser.if_clause_return;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RhythmWeight {
	
	private List<Note> notes;

	private static final double ARTICULATION_WEIGHT = 1.0;
	private static final double DYNAMIC_WEIGHT = 1.0;
	private static final double DIASTEMATY_WEIGHT = 1.0;

	protected double getMinimumNoteValue() {
		updateNotesLength();
		return notes.stream().mapToDouble(note -> note.getLength()).min().getAsDouble();
	}
	
	protected void updateNotesLength(){
		notes.forEach(note -> note.setLength(Integer.MAX_VALUE));
		int size = notes.size() - 1;
		for (int i = 0; i < size; i++) {
			Note note = notes.get(i);
			Note nextNote = notes.get(i + 1);
			note.setLength(nextNote.getPosition() - note.getPosition());
		}
	}

	protected void updateRhythmWeightSounds(double min) {
		updateNotesLength();
		notes.get(notes.size() - 1).setLength((int) min);
		notes.forEach(note -> note.setPositionWeight(note.getPositionWeight() + (note.getLength()/min))); 
	}

	protected void updateRhythmWeightPitch(List<Note> pitches, double min) {
		int size = pitches.size() - 1;
		for (int i = 0; i < size; i++) {
			Note note = pitches.get(i);
			Note nextNote = pitches.get(i + 1);
			int length = nextNote.getPosition() - note.getPosition();
			note.setPositionWeight(note.getPositionWeight() + (length/min));
		}
	}

	protected List<Note> extractDifferentPitches() {
		List<Note> pitches = new ArrayList<>();
		int prevPitch = 0;
		for (Note note : notes) {
			if (note.getPitch() != prevPitch) {
				pitches.add(note);
			}
			prevPitch = note.getPitch();
		}
		return pitches;
	}

	protected void updateRhythmWeightDiastematy(List<Note> pitches) {
		int size = pitches.size() - 1;
		boolean positive = false;
		boolean negative = false;
		for (int i = 0; i < size; i++) {
			Note note = pitches.get(i);
			Note nextNote = pitches.get(i + 1);
			int interval = nextNote.getPitch() - note.getPitch();
			if (i == 0) {
				note.setPositionWeight(note.getPositionWeight() + DIASTEMATY_WEIGHT);
			}
			if (interval > 0) {
				positive = true;
				if (negative) {
					//change keel
					note.setPositionWeight(note.getPositionWeight() + DIASTEMATY_WEIGHT);
					negative = false;
				}
			}else{
				negative = true;
				if (positive) {
					//change crest
					note.setPositionWeight(note.getPositionWeight() + DIASTEMATY_WEIGHT);
					positive = false;
				}
			}
		}
	}

	protected void updateRhythmWeightDynamics() {
		notes.forEach(note -> {
			if (!note.getDynamic().equals(Note.DEFAULT_DYNAMIC)){
				note.setPositionWeight(note.getPositionWeight() + DYNAMIC_WEIGHT);
			}
			
		});
	}

	protected void updateRhythmWeightArticulation() {
		notes.forEach(note -> {
			if (!note.getArticulation().equals(Note.DEFAULT_ARTICULATION)){
				note.setPositionWeight(note.getPositionWeight() + ARTICULATION_WEIGHT);
			}
		});
	}
	
	public void updateRhythmWeight(){
		double min = getMinimumNoteValue();
		updateRhythmWeightSounds(min);
		List<Note> pitches = extractDifferentPitches();
		updateRhythmWeightPitch(pitches, min);
		updateRhythmWeightDiastematy(pitches);
		updateRhythmWeightDynamics();
		updateRhythmWeightArticulation();
	}
	
	public List<Note> filterRhythmWeigths(double minimumWeight){
		return notes.stream().filter(note -> note.getPositionWeight() >= minimumWeight).collect(Collectors.toList());
	}
	
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
}
