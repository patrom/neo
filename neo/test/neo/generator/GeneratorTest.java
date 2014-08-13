package neo.generator;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.HarmonyBuilder;
import neo.data.melody.Melody;
import neo.data.note.NoteBuilder;
import neo.data.note.Scale;
import neo.evaluation.HarmonyProperties;
import neo.evaluation.MelodyProperties;
import neo.evaluation.MusicProperties;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest {
	
	private Generator generator;
	private int chordSize;
	private List<HarmonyProperties> harmonyProperties = new ArrayList<>();
	private Map<Integer, List<MelodyProperties>> melodyPropertiesMap = new TreeMap<>();
	
	@Before
	public void setup() {
		harmonyProperties.add(new HarmonyProperties(0, 12));
		harmonyProperties.add(new HarmonyProperties(12, 12));
		harmonyProperties.add(new HarmonyProperties(24, 12));
		harmonyProperties.add(new HarmonyProperties(36, 12));
		
		List<MelodyProperties> melodyProperties = new ArrayList<>();
		melodyProperties.add(new MelodyProperties(0, 6));
		melodyProperties.add(new MelodyProperties(6, 12));
		melodyProperties.add(new MelodyProperties(18, 6));
		melodyProperties.add(new MelodyProperties(24, 6));
		melodyProperties.add(new MelodyProperties(30, 6));
		melodyProperties.add(new MelodyProperties(36, 12));
		melodyPropertiesMap.put(0, melodyProperties);
		
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
		props.setHarmonyProperties(harmonyProperties);
		props.setMelodyPropertiesMap(melodyPropertiesMap);
		generator = new Generator(props);
	}

	@Test
	public void testGenerateMotive() {
		Motive motive = generator.generateMotive();
		assertEquals(harmonyProperties.size(), motive.getHarmonies().size());
	}
	
	@Test
	public void testGenerateMelody() {
		List<Harmony> harmonies = generator.generateHarmonies();
		List<Melody> melody = generator.generateMelodies(harmonies);
		assertEquals(4, melody.size());
	}
	
//	@Test
//	public void testUpdateMelodyToHarmony() {
//		int voice = 1;
//		Motive motive = generator.generateMotive();
//		Melody melody = generator.generateMelody(motive.getHarmonies(), voice);
//		generator.updateMelodyToHarmony(motive.getHarmonies(), melody);
//		assertEquals(motive.getHarmonies().get(0), melody.getNotes().get(0).getHarmony());
//		
//	}
	
	@Test
	public void calculateBeatValuesTest() {
		double weight = generator.calculatePositionWeight(0, 18);
		assertEquals(2.5, weight, 0);
	}

}
