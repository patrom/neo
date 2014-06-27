package neo.objective.melody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo.data.Motive;
import neo.data.melody.Melody;
import neo.data.note.Interval;
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class MelodicObjective extends Objective {
	
	private Map<Integer, Double> weights = musicProperties.getRhythmWeightValues();
	private int minimumLength = musicProperties.getMinimumLength();

	public MelodicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		List<Double> intervals = new ArrayList<>();
		double totalWeight = weights.values().stream().mapToDouble(v -> v).sum();
		int countIntervals = 0;
		int maxDepth = 3;
		for(Melody melody: motive.getMelodies()){
			int depth = 1;
			Object[] notePositions =  melody.getNotes().toArray();
			countIntervals = countIntervals + notePositions.length - 1;
			while (depth <= maxDepth) {
				for (int j = 0; j < notePositions.length - depth; j++) {
					NotePos note = (NotePos) notePositions[j];
					NotePos nextNote = (NotePos) notePositions[j + depth];
					
					Double weight = getWeight(note);
					Double nextWeight = getWeight(nextNote);
					double weightValue = (weight + nextWeight)/totalWeight;
					
					int difference = nextNote.getPitch() - note.getPitch();
					Interval interval = Interval.getEnumInterval(difference);
					double intervalWeight = interval.getMelodicValue() * weightValue;
					intervals.add(intervalWeight); 		
				}
				depth++;
			}
		}
		double sum = intervals.stream().mapToDouble(i -> i).sum();
		return sum/countIntervals;
	}

	protected Double getWeight(NotePos note) {
		int position = note.getPosition();
		Double weight = weights.get(position);
		int length = position + note.getLength();
		position = position + minimumLength;
		while (position < length) {
			weight = weight + weights.get(position);
			position = position + minimumLength;
		}
		return weight;
	}
			
}
