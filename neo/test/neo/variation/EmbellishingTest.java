package neo.variation;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.model.note.Scale;
import neo.variation.nonchordtone.Variation;
import neo.variation.nonchordtone.neighbor.NeighborScaleDown;
import neo.variation.nonchordtone.passing.ChromaticPassingUp;
import neo.variation.nonchordtone.suspension.Suspension;
import neo.variation.pattern.VariationPattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class, VariationConfig.class}, loader = SpringApplicationContextLoader.class)
public class EmbellishingTest {
	
	@Autowired
	@InjectMocks
	private Embellisher embellishing;
	@Autowired
	private NeighborScaleDown neighborScaleDown;
	@Autowired
	private Suspension suspension;
	@Autowired
	private ChromaticPassingUp chromaticPassingUp;
	@Autowired
	@Qualifier(value="NeigborVariationPattern")
	private VariationPattern variationPattern;
	
	@Mock
	private VariationSelector variationSelector;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		neighborScaleDown.setExcludedVoices(Collections.singletonList(0));
		suspension.setSecondNoteChanged(false);
	}

	@Test
	public void testEmbellishChromaticPassingUp() {
		setVariation(chromaticPassingUp, new double[][]{{0.5, 0.5}});
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(0).pitch(60).pos(0).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		note = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		note = note().pc(4).pitch(64).pos(24).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		assertTrue(embellishedMelody.size() == 5);
		assertEquals(60, embellishedMelody.get(0).getPitch());
		assertEquals(61, embellishedMelody.get(1).getPitch());
		assertEquals(62, embellishedMelody.get(2).getPitch());
		assertEquals(63, embellishedMelody.get(3).getPitch());
		assertEquals(64, embellishedMelody.get(4).getPitch());
		
		assertEquals(6, embellishedMelody.get(0).getLength());
		assertEquals(6, embellishedMelody.get(1).getLength());
		assertEquals(6, embellishedMelody.get(2).getLength());
		assertEquals(6, embellishedMelody.get(3).getLength());
		assertEquals(12, embellishedMelody.get(4).getLength());
	}
	
	@Test
	public void testEmbellishNeigborScaleDown() {
		setVariation(neighborScaleDown, new double[][]{{0.5, 0.25, 0.25}});
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(4).pitch(64).pos(0).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		assertTrue(embellishedMelody.size() == 4);
		assertEquals(64, embellishedMelody.get(0).getPitch());
		assertEquals(62, embellishedMelody.get(1).getPitch());
		assertEquals(64, embellishedMelody.get(2).getPitch());
		assertEquals(60, embellishedMelody.get(3).getPitch());
		
		assertEquals(6, embellishedMelody.get(0).getLength());
		assertEquals(3, embellishedMelody.get(1).getLength());
		assertEquals(3, embellishedMelody.get(2).getLength());
		assertEquals(12, embellishedMelody.get(3).getLength());
	}
	
	@Test
	public void testEmbellishSuspension() {
		setVariation(suspension, new double[][]{{0.5, 0.5}});
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(2).pitch(62).pos(0).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		assertTrue(embellishedMelody.size() == 2);
		assertEquals(62, embellishedMelody.get(0).getPitch());
		assertEquals(60, embellishedMelody.get(1).getPitch());
		
		assertEquals(18, embellishedMelody.get(0).getLength());
		assertEquals(6, embellishedMelody.get(1).getLength());
	}
	
	@Test
	public void testEmbellishSuspensionNotAllowedLength() {
		setVariation(suspension, new double[][]{{0.5, 0.5}});
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(2).pitch(62).pos(0).len(3).ocatve(5).voice(3).build();
		notes.add(note);
		note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).voice(3).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		assertTrue(embellishedMelody.size() == 2);
		assertEquals(62, embellishedMelody.get(0).getPitch());
		assertEquals(60, embellishedMelody.get(1).getPitch());
		
		assertEquals(3, embellishedMelody.get(0).getLength());
		assertEquals(12, embellishedMelody.get(1).getLength());
	}

	@Test
	public void testEmbellishExcludeVoice() {
		setVariation(neighborScaleDown, new double[][]{{0.5, 0.25, 0.25}});
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(4).pitch(64).pos(0).len(12).ocatve(5).voice(0).build();
		notes.add(note);
		note = note().pc(0).pitch(60).pos(12).len(12).ocatve(5).voice(0).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		assertTrue(embellishedMelody.size() == 2);
		assertEquals(64, embellishedMelody.get(0).getPitch());
		assertEquals(60, embellishedMelody.get(1).getPitch());
		
		assertEquals(12, embellishedMelody.get(0).getLength());
		assertEquals(12, embellishedMelody.get(1).getLength());
	}

	private void setVariation(Variation variation, double[][] pattern) {
		variation.setScales(Collections.singletonList(new Scale(Scale.MAJOR_SCALE)));
		variationPattern.setPatterns(pattern);
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		variationPattern.setNoteLengths(allowedLengths);
		variationPattern.setSecondNoteLengths(allowedLengths);
		variation.setVariationPattern(variationPattern);
		when(variationSelector.selectVariation(Mockito.anyInt())).thenReturn(variation);
	}

}
