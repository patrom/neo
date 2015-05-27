package neo.variation.nonchordtone.neighbor;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

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
public class NeighborScaleDownTest {
	
	@Autowired
	private NeighborScaleDown neigborScaleDown;
	@Autowired
	private NeighborScaleUp neighborScaleUp;
	@Autowired
	@Qualifier(value="NeigborVariationPattern")
	private VariationPattern variationPattern;
	private double[] pattern = {0.5, 0.25, 0.25};

	@Before
	public void setUp() throws Exception {
	
	}

	private void setVariation(Variation variation) {
		variation.setScales(Collections.singletonList(new Scale(Scale.MAJOR_SCALE)));
		variationPattern.setPatterns(new double[][]{{0.5, 0.25, 0.25}});
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		variationPattern.setNoteLengths(allowedLengths);
		variation.setVariationPattern(variationPattern);
	}

	@Test
	public void testCreateVariation() {
		setVariation(neigborScaleDown);
		Note firstNote = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).build();
		List<Note> notes = neigborScaleDown.createVariation(firstNote ,null);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(firstNote.getPitch() - 2, notes.get(1).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}

	@Test
	public void testGenerateNeigborNote() {
		setVariation(neigborScaleDown);
		Note note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).build();
		List<Note> notes = neigborScaleDown.generateNeighborNote(note, 11, 59, pattern);
		assertEquals(note.getPitch(), notes.get(0).getPitch());
		assertEquals(note.getPitch() - 1, notes.get(1).getPitch());
		assertEquals(note.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}
	
	@Test
	public void testCreateVariationUp() {
		setVariation(neighborScaleUp);
		Note firstNote = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).build();
		List<Note> notes = neighborScaleUp.createVariation(firstNote, null);
		assertEquals(firstNote.getPitch(), notes.get(0).getPitch());
		assertEquals(firstNote.getPitch() + 2, notes.get(1).getPitch());
		assertEquals(firstNote.getPitch(), notes.get(2).getPitch());
		
		assertEquals(6, notes.get(0).getLength());
		assertEquals(3, notes.get(1).getLength());
		assertEquals(3, notes.get(2).getLength());
	}

}
