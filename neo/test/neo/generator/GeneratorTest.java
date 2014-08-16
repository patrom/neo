package neo.generator;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.Scale;
import neo.evaluation.HarmonyProperties;
import neo.evaluation.MelodyProperties;
import neo.evaluation.MusicProperties;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	
	private Generator generator;
	private int chordSize;
	
	@Before
	public void setup() {
		chordSize = 4;
		Integer[] octave = {5};
		MusicProperties props = new MusicProperties();
		props.setScale(new Scale(Scale.MAJOR_SCALE));
		props.setMinimumLength(6);
		Map<Integer, Double> rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		rhythmWeightValues.put(30, 0.5);
		rhythmWeightValues.put(36, 1.0);
		rhythmWeightValues.put(42, 0.5);
		props.setRhythmWeightValues(rhythmWeightValues);
		props.setOctaveHighestPitchClass(octave);
		props.setChordSize(chordSize);
		generator = new Generator(props);
	}

	@Test
	public void testGenerateMotive() {
		Motive motive = generator.generateMotive();
	}
	
	@Test
	public void testGenerateMelody() {
		List<Harmony> harmonies = generator.generateHarmonies();
		List<Melody> melody = generator.generateMelodies(harmonies);
		assertEquals(4, melody.size());
		assertEquals(4, harmonies.get(0).getHarmonicMelodies().size());
	}
	
	@Test
	public void calculateBeatValuesTest() {
		double weight = generator.calculatePositionWeight(0, 18);
		assertEquals(2.5, weight, 0);
	}

}
