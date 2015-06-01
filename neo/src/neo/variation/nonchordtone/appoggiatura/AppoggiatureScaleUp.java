package neo.variation.nonchordtone.appoggiatura;

import java.util.Collections;
import java.util.List;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.util.RandomUtil;
import neo.util.Util;

import org.springframework.stereotype.Component;

@Component
public class AppoggiatureScaleUp extends Appoggiature {

	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(firstNote.getLength())) {
			secondNoteChanged = true;
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			Scale scale = RandomUtil.getRandomFromList(scales);
			int newPitchClass = scale.pickNextPitchFromScale(secondNote.getPitchClass());
			int ic = Util.intervalClass(newPitchClass - secondNote.getPitchClass());
			int newPitch = secondNote.getPitch() - ic;
			List<Note> notes = generateAccentedNonChordNote(secondNote, newPitchClass, newPitch, pattern);
			notes.add(0, firstNote);
			return notes;
		} else {
			return Collections.singletonList(firstNote.copy());
		}
	}
}
