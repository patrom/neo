package neo.variation.nonchordtone.suspension;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.variation.AbstractVariationTest;
import neo.variation.pattern.FreeSuspensionVariationPattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class, VariationConfig.class}, loader = SpringApplicationContextLoader.class)
public class FreeSuspensionScaleDownTest extends AbstractVariationTest{

	@Autowired
	private FreeSuspensionScaleDown freeSuspensionScaleDown;
	@Autowired
	private FreeSuspensionVariationPattern freeSuspensionVariationPattern;
	private double[][] suspensionPattern =  new double[][]{{0.5, 0.5}};
	
	@Before
	public void setUp() throws Exception {
		variation = freeSuspensionScaleDown;
		variationPattern = freeSuspensionVariationPattern;
		pattern = suspensionPattern;
		setVariation();
	}

	@Test
	public void testCreateVariation() {
		Note firstNote = note().pc(2).pitch(62).pos(0).len(12).ocatve(5).build();
		List<Note> notes = variation.createVariation(firstNote, null);
		assertEquals(firstNote.getPitch() + 2, notes.get(0).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(1).getPitch());
		
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
