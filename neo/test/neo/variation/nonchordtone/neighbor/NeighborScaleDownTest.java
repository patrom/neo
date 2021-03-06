package neo.variation.nonchordtone.neighbor;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.variation.AbstractVariationTest;
import neo.variation.pattern.NeigborVariationPattern;

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
public class NeighborScaleDownTest extends AbstractVariationTest {
	
	@Autowired
	private NeighborScaleDown neigborScaleDown;
	@Autowired
	private NeighborScaleUp neighborScaleUp;
	@Autowired
	@Qualifier(value="NeigborVariationPattern")
	private NeigborVariationPattern neigborVariationPattern;
	private double[][] neighborPattern = new double[][]{{0.5, 0.25, 0.25}};
	private double[] patt = new double[]{0.5, 0.25, 0.25};
	
	@Before
	public void setUp() throws Exception {
		variationPattern = neigborVariationPattern;
		super.pattern = neighborPattern;
	}

	@Test
	public void testCreateVariation() {
		variation = neigborScaleDown;
		setVariation();
		Note firstNote = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).build();
		List<Note> notes = variation.createVariation(firstNote ,null);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(firstNote.getPitch() - 2, notes.get(1).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}

	@Test
	public void testGenerateNeigborNote() {
		variation = neigborScaleDown;
		setVariation();
		Note note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).build();
		List<Note> notes = neigborScaleDown.generateNeighborNote(note, 11, 59, patt);
		assertEquals(note.getPitch(), notes.get(0).getPitch());
		assertEquals(note.getPitch() - 1, notes.get(1).getPitch());
		assertEquals(note.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}
	
	@Test
	public void testCreateVariationUp() {
		variation = neighborScaleUp;
		setVariation();
		Note firstNote = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).build();
		List<Note> notes = variation.createVariation(firstNote, null);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(firstNote.getPitch() + 2, notes.get(1).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}
	
	@Test
	public void testCreateVariationNotAllowedLength() {
		variation = neighborScaleUp;
		setVariation();
		List<Note> notes = testNotAllowedLength();
		assertTrue(notes.size() == 1);
		assertEquals(64, notes.get(0).getPitch());
	}

}
