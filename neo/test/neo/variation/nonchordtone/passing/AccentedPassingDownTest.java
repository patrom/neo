package neo.variation.nonchordtone.passing;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.model.note.Scale;
import neo.variation.nonchordtone.Variation;
import neo.variation.pattern.VariationPattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class, VariationConfig.class}, loader = SpringApplicationContextLoader.class)
public class AccentedPassingDownTest {

	@Autowired
	private AccentedPassingDown accentedPassingDown;
	@Autowired
	@Qualifier(value="AnticipationVariationPattern")
	private VariationPattern variationPattern;

	@Before
	public void setUp() throws Exception {
	
	}

	private void setVariation(Variation variation) {
		variation.setScales(Collections.singletonList(new Scale(Scale.MAJOR_SCALE)));
		variationPattern.setPatterns(new double[][]{{0.5, 0.5}});
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		variationPattern.setNoteLengths(allowedLengths);
		variation.setVariationPattern(variationPattern);
	}

	@Test
	public void testCreateVariation() {
		setVariation(accentedPassingDown);
		Note firstNote = note().pc(4).pitch(64).pos(0).len(12).ocatve(5).build();
		Note secondNote = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).build();
		List<Note> notes = accentedPassingDown.createVariation(firstNote, secondNote);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(secondNote.getPitch() + 2, notes.get(1).getPitch());
		assertEquals(secondNote.getPitch(), notes.get(2).getPitch());
		
		assertEquals(12, notes.get(0).getLength());
		assertEquals(6, notes.get(1).getLength());
		assertEquals(6, notes.get(2).getLength());
	}

}
