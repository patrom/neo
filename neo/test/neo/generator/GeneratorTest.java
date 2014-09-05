package neo.generator;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static neo.data.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.data.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.AbstractTest;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.HarmonyBuilder;
import neo.data.melody.HarmonicMelody;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;

import org.junit.Before;
import org.junit.Test;

public class GeneratorTest extends AbstractTest{
	
	private Generator generator;
	private int chordSize;
	
	@Before
	public void setUp(){
		MusicProperties musicProperties = new MusicProperties();
		List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
		harmonyBuilders.add(harmony().pos(0).len(12));
		harmonyBuilders.add(harmony().pos(12).len(12));
		harmonyBuilders.add(harmony().pos(24).len(12));
		musicProperties.setHarmonyBuilders(harmonyBuilders);
		
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		harmonicMelodies.add(harmonicMelody().voice(2).pos(0)
				.notes(note().voice(2).pos(0).len(6).build(), 
					   note().voice(2).pos(6).len(12).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(12)
				.notes(note().voice(2).pos(18).len(6).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(24)
				.notes(note().voice(2).pos(24).len(6).build(),
						note().voice(2).pos(30).len(6).build()).build());
		musicProperties.setHarmonicMelodies(harmonicMelodies);
		musicProperties.setChordSize(3);
		
		chordSize = 4;
		Integer[] octave = {5};
		musicProperties.setScale(new Scale(Scale.MAJOR_SCALE));
		musicProperties.setMinimumLength(6);
		Map<Integer, Double> rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		rhythmWeightValues.put(30, 0.5);
		rhythmWeightValues.put(36, 1.0);
		rhythmWeightValues.put(42, 0.5);
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
		musicProperties.setOctaveHighestPitchClass(octave);
		musicProperties.setChordSize(chordSize);
		generator = new Generator(musicProperties);
	}

	@Test
	public void testGenerateHarmony() {
		List<Harmony> harmonies = generator.generateHarmonies();
		assertEquals(3, harmonies.size());
	}
	
	@Test
	public void calculateBeatValuesTest() {
		double weight = generator.calculatePositionWeight(0, 18);
		assertEquals(2.5, weight, 0);
	}

}
