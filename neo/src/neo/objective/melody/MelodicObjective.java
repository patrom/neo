package neo.objective.melody;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import neo.data.Motive;
import neo.data.melody.Melody;
import neo.data.note.Interval;
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class MelodicObjective extends Objective {

	public MelodicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		List<Interval> intervals = new ArrayList<>();
		for(Melody melody: motive.getMelodies()){
			Object[] notePositions =  melody.getNotes().toArray();
			for (int j = 0; j < notePositions.length - 1; j++) {
				NotePos note = (NotePos) notePositions[j];
				NotePos nextNote = (NotePos) notePositions[j + 1];
	
				int difference = nextNote.getPitch() - note.getPitch();
				Interval interval = Interval.getEnumInterval(difference);
				intervals.add(interval); 		
			}
		}
		OptionalDouble optional = intervals.stream().mapToDouble(interval -> interval.getMelodicValue()).average();
		if (optional.isPresent()) {
			return optional.getAsDouble();
		}
		return optional.orElse(0.0);
	}
			
}
