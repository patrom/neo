package neo.objective.melody;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import neo.data.note.NoteBuilder;
import neo.data.note.NotePos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InnerMetricWeightTest {
	
	private static Logger LOGGER = Logger.getLogger(InnerMetricWeightTest.class.getName());
	private double[] rhythmPattern = {6, 3, 3, 6, 12, 6, 12};
	private int minimumRhythmicValue = 3;
	
	@Before
	public void setUp() {
	}

	@Test
	public void testGetLocalMeters() {
		Integer[] onSetArr = {0, 2, 3, 4, 6, 10, 12};
		List<List<Integer>> localMeters = InnerMetricWeight.getLocalMeters(onSetArr);
		List<Integer> localMeter = new ArrayList<>();
		localMeter.add(0);
		localMeter.add(3);
		localMeter.add(6);
		assertTrue(localMeters.contains(localMeter));
	}

	@Test
	public void testGetNormalizedInnerMetricWeight() {
		Map<Integer, Double> normilazedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(rhythmPattern , minimumRhythmicValue);
		LOGGER.info(normilazedMap.toString());
	}
	
	@Test
	public void testGetNormalizedInnerMetricWeightNotes() {
		List<NotePos> notes = createMelody();
		Map<Integer, Double> normilazedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(notes, minimumRhythmicValue);
		LOGGER.info(normilazedMap.toString());
	}

	private List<NotePos> createMelody() {
		List<NotePos> notes = new ArrayList<>();
		notes.add(NoteBuilder.note().pos(0).build());
		notes.add(NoteBuilder.note().pos(6).build());
		notes.add(NoteBuilder.note().pos(9).build());
		notes.add(NoteBuilder.note().pos(12).build());
		notes.add(NoteBuilder.note().pos(18).build());
		notes.add(NoteBuilder.note().pos(30).build());
		notes.add(NoteBuilder.note().pos(36).build());
		return notes;
	}
	
	@Test
	public void testExtractOnsetNotes() {
		List<NotePos> notes = createMelody();
		Integer[] onSetArr = InnerMetricWeight.extractOnsetNotes(notes, minimumRhythmicValue);
		Integer[] expected = {0, 2, 3, 4, 6, 10, 12};
		assertArrayEquals(expected, onSetArr);
	}

	@Test
	public void testExtractOnset() {
		Integer[] onSetArr = InnerMetricWeight.extractOnset(rhythmPattern , minimumRhythmicValue);
		Integer[] expected = {0, 2, 3, 4, 6, 10, 12};
		assertArrayEquals(expected, onSetArr);
	}
	
	@Test
	public void testGetInnerMetricWeight() {
		Integer[] onSetArr = {0, 2, 3, 4, 6, 10, 12};
		List<List<Integer>> localMeters = InnerMetricWeight.getLocalMeters(onSetArr);
		Map<Integer, Double> innerMetricWeights = InnerMetricWeight.getInnerMetricWeight(localMeters, onSetArr);
		LOGGER.info(innerMetricWeights.toString());
		Assert.assertEquals(21.0, innerMetricWeights.get(6).doubleValue(), 0);
	}

}
