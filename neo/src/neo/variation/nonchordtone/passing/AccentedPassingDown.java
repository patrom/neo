package neo.variation.nonchordtone.passing;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;
import neo.util.Util;

@Component
public class AccentedPassingDown extends Passing {
	
	public AccentedPassingDown() {
		excludedVoices.add(0);
	}

	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(secondNote.getLength())) {
			secondNoteChanged = true;
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			Scale scale = RandomUtil.getRandomFromList(scales);
			int newPitchClass = scale.pickPreviousPitchFromScale(firstNote.getPitchClass());
			int ic = Util.intervalClass(newPitchClass - firstNote.getPitchClass());
			int newPitch = firstNote.getPitch() - ic;
			List<Note> notes =  generateAccentedNonChordNote(secondNote, newPitchClass, newPitch, pattern);
			notes.add(0, firstNote);
			return notes;
		} else {
			return Collections.singletonList(firstNote.copy());
		}
	}

}
