package neo.variation.nonchordtone.neighbor;

import java.util.Collections;
import java.util.List;

import neo.model.note.Note;
import neo.util.RandomUtil;

import org.springframework.stereotype.Component;

@Component
public class ChromaticNeigborDown extends Neighbor{
	
	
	@Override
	public List<Note> createVariation(Note note, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(note.getLength())) {
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			int pitchClass = note.getPitchClass() - 1;
			int pitch = note.getPitch() - 1;
			return generateNeighborNote(note, pitchClass, pitch, pattern);
		} else {
			return Collections.singletonList(note.copy());
		}
	}
}
