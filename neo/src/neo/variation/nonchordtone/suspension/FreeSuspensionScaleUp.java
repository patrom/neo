package neo.variation.nonchordtone.suspension;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;
import neo.util.Util;
@Component
public class FreeSuspensionScaleUp extends FreeSuspesion {
	
	public FreeSuspensionScaleUp() {
		profile = 30;
	}

	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(firstNote.getLength())) {
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			Scale scale = RandomUtil.getRandomFromList(scales);
			int newPitchClass = scale.pickPreviousPitchFromScale(firstNote.getPitchClass());
			int ic = Util.intervalClass(newPitchClass - firstNote.getPitchClass());
			int newPitch = firstNote.getPitch() - ic;
			return generateAccentedNonChordNote(firstNote, newPitchClass, newPitch, pattern);
		} else {
			return Collections.singletonList(firstNote);
		}
	}

}
