package neo.variation.nonchordtone.neighbor;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;
import neo.util.Util;

@Component
public class NeighborScaleDown extends Neighbor {
	
	@Override
	public List<Note> createVariation(Note note, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(note.getLength())) {
			Scale scale = RandomUtil.getRandomFromList(scales);
			int pitchClass = scale.pickPreviousPitchFromScale(note.getPitchClass());
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			int ic = Util.intervalClass(pitchClass - note.getPitchClass());
			int pitch = note.getPitch() - ic;
			return generateNeighborNote(note, pitchClass, pitch, pattern);
		} else {
			return Collections.singletonList(note.copy());
		}
	}
}
