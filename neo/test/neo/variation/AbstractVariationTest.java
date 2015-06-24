package neo.variation;

import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.model.note.Note;
import neo.model.note.Scale;
import neo.variation.nonchordtone.Variation;
import neo.variation.pattern.VariationPattern;

public abstract class AbstractVariationTest {
	
	protected Variation variation;
	protected VariationPattern variationPattern;
	protected double[][] pattern;

	protected void setVariation() {
		variation.setScales(Collections.singletonList(new Scale(Scale.MAJOR_SCALE)));
		variationPattern.setPatterns(pattern);
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		variationPattern.setNoteLengths(allowedLengths);
		variationPattern.setSecondNoteLengths(allowedLengths);
		variation.setVariationPattern(variationPattern);
	}
	
	protected List<Note> testNotAllowedLength(){
		Note firstNote = note().pc(4).pitch(64).pos(0).len(3).ocatve(5).build();
		Note secondNote = note().pc(0).pitch(60).pos(3).len(9).ocatve(5).build();
		return variation.createVariation(firstNote, secondNote);
	}
}
