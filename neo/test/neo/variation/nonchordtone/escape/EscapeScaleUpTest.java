package neo.variation.nonchordtone.escape;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.*;

import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.variation.AbstractVariationTest;
import neo.variation.nonchordtone.anticipation.Anticipation;
import neo.variation.pattern.AnticipationVariationPattern;
import neo.variation.pattern.EscapeVariationPattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class, VariationConfig.class}, loader = SpringApplicationContextLoader.class)
public class EscapeScaleUpTest extends AbstractVariationTest{

	@Autowired
	private EscapeScaleUp escapeScaleUp;
	@Autowired
	private EscapeVariationPattern escapeVariationPattern;
	private double[][] escapePattern =  new double[][]{{0.5, 0.5}};

	@Before
	public void setUp() throws Exception {
		variation = escapeScaleUp;
		variationPattern = escapeVariationPattern;
		pattern = escapePattern;
		setVariation();
	}
	
	@Test
	public void testCreateVariation() {
		Note firstNote = note().pc(4).pitch(64).pos(0).len(12).ocatve(5).build();
		List<Note> notes = variation.createVariation(firstNote, null);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(firstNote.getPitch() + 1, notes.get(1).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(6, notes.get(1).getLength());
	}
	
	@Test
	public void testCreateVariationNotAllowedLength() {
		List<Note> notes = testNotAllowedLength();
		assertTrue(notes.size() == 1);
		assertEquals(64, notes.get(0).getPitch());
	}
	
}
