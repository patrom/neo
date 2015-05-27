package neo.variation.nonchordtone.suspension;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.*;

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
public class FreeSuspensionScaleDownTest {

	@Autowired
	private FreeSuspensionScaleDown freeSuspensionScaleDown;
	@Autowired
	@Qualifier(value="FreeSuspensionVariationPattern")
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
		variationPattern.setSecondNoteLengths(allowedLengths);
		variation.setVariationPattern(variationPattern);
	}

	@Test
	public void testCreateVariation() {
		setVariation(freeSuspensionScaleDown);
		Note firstNote = note().pc(2).pitch(62).pos(0).len(12).ocatve(5).build();
		Note secondNote = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).build();
		List<Note> notes = freeSuspensionScaleDown.createVariation(firstNote, secondNote);
		assertEquals(firstNote.getPitch() + 2, notes.get(0).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(1).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(6, notes.get(1).getLength());
	}

}
