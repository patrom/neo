package neo.variation;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.model.note.Note;
import neo.model.note.Scale;
import neo.variation.Embellisher;
import neo.variation.VariationSelector;
import neo.variation.nonchordtone.neighbor.NeighborScaleDown;
import neo.variation.nonchordtone.neighbor.NeighborScaleUp;
import neo.variation.nonchordtone.passing.ChromaticPassingUp;
import neo.variation.pattern.VariationPattern;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
	private NeighborScaleUp neighborScaleUp;
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
	}

	@Test
	public void testEmbellish() {
		chromaticPassingUp.setScales(Collections.singletonList(new Scale(Scale.MAJOR_SCALE)));
		variationPattern.setPatterns(new double[][]{{0.5, 0.5}});
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		variationPattern.setNoteLengths(allowedLengths);
		chromaticPassingUp.setVariationPattern(variationPattern);
		when(variationSelector.getVariation()).thenReturn(chromaticPassingUp);
		
		List<Note> notes = new ArrayList<>();
		Note note = note().pc(0).pitch(60).pos(0).len(12).ocatve(5).build();
		notes.add(note);
		note = note().pc(2).pitch(62).pos(12).len(12).ocatve(5).build();
		notes.add(note);
		note = note().pc(4).pitch(64).pos(24).len(12).ocatve(5).build();
		notes.add(note);
		List<Note> embellishedMelody = embellishing.embellish(notes);
		for (Note note2 : embellishedMelody) {
			System.out.println(note2);
		}
	}

}
