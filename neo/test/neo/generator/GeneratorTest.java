package neo.generator;

import static java.util.stream.Collectors.toSet;
import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	
	private Generator generator;
	private int chordSize;
	
	@Before
	public void setup() {
		chordSize = 4;
		Integer[] octave = {5};
		int[] rhythmGeneratorTemplate = {0,12,18,24};
		MusicProperties props = new MusicProperties();
		props.setScale(new Scale(Scale.MAJOR_SCALE));
		props.setRhythmGeneratorTemplate(rhythmGeneratorTemplate);
		props.setMinimumLength(6);
		Map<Integer, Double> rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		props.setRhythmWeightValues(rhythmWeightValues);
		props.setOctaveHighestPitchClass(octave);
		props.setChordSize(chordSize);
		generator = new Generator(props);
	}

	@Test
	public void testGenerateMotive() {
		Motive motive = generator.generateMotive();
		Assert.assertEquals("", chordSize, motive.getMelodies().size());
	}
	
	@Test
	public void calculateBeatValuesTest() {
		Harmony harmony = harmony().pos(0).len(18).notes(0,4,7).build();
		double weight = generator.calculatePositionWeight(harmony);
		assertEquals(2.5, weight, 0);
	}

}
