package neo.variation.nonchordtone.anticipation;

import java.util.ArrayList;
import java.util.List;

import neo.model.note.Note;
import neo.util.RandomUtil;
import neo.variation.nonchordtone.Variation;

import org.springframework.stereotype.Component;

@Component
public class Anticipation extends Variation {
	
	public Anticipation() {
		profile = 10;
		excludedVoices.add(0);
	}

	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		if (variationPattern.getNoteLengths().contains(firstNote.getLength())) {
			double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
			return generateNonChordNote(firstNote, secondNote.getPitchClass(), secondNote.getPitch(), pattern);
		} 
		List<Note> notes = new ArrayList<>();
		notes.add(firstNote);
		notes.add(secondNote);
		return notes;
	}

}
