package neo.variation.nonchordtone.suspension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.model.note.Note;
import neo.util.RandomUtil;
import neo.variation.nonchordtone.Variation;

import org.springframework.stereotype.Component;


@Component
public class Suspension extends Variation {
	
	@Override
	public List<Note> createVariation(Note firstNote, Note secondNote) {
		List<Note> notes = new ArrayList<>();
		if (variationPattern.getNoteLengths().contains(firstNote.getLength()) 
				&& variationPattern.getSecondNoteLengths().contains(secondNote.getLength())) {
			if (firstNote.getLength() >= secondNote.getLength()) {
				secondNoteChanged = true;
				double[] pattern = RandomUtil.getRandomFromDoubleArray(variationPattern.getPatterns());
				int secondNoteLength = secondNote.getLength();
				int firstNewLength =  firstNote.getLength() + (int)(secondNoteLength * pattern[0]);
				Note copyFirstNote = firstNote.copy();
				copyFirstNote.setLength(firstNewLength);
				notes.add(copyFirstNote);
				
				int secondNewLength = (int) (secondNoteLength * pattern[1]);
				Note copySecondNote = secondNote.copy();
				int position = firstNote.getPosition() + firstNewLength;
				copySecondNote.setPosition(position);
				copySecondNote.setLength(secondNewLength);
				notes.add(copySecondNote);
				return notes;
			}
		} 
		return Collections.singletonList(firstNote.copy());
	}

}
