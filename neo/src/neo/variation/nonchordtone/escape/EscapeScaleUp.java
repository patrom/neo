package neo.variation.nonchordtone.escape;

import java.util.Collections;
import java.util.List;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;
import neo.util.Util;

import org.springframework.stereotype.Component;
@Component
public class EscapeScaleUp extends Escape {

	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(firstNote.getLength())) {
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			Scale scale = RandomUtil.getRandomFromList(scales);
			int newPitchClass = scale.pickNextPitchFromScale(firstNote.getPitchClass());
			int ic = Util.intervalClass(newPitchClass - firstNote.getPitchClass());
			int newPitch = firstNote.getPitch() + ic;
			return generateNonChordNote(firstNote, newPitchClass, newPitch, pattern);
		} else {
			return Collections.singletonList(firstNote.copy());
		}
	}

}
