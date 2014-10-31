package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.AbstractTest;
import neo.DefaultConfig;
import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.note.NoteBuilder;
import neo.model.note.Scale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class GeneratorTest extends AbstractTest{
	
	@Autowired
	private Generator generator;
	@Autowired
	private MusicProperties musicProperties;

	@Before
	public void setUp(){
		List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
		harmonyBuilders.add(harmony().pos(0).len(12));
		harmonyBuilders.add(harmony().pos(12).len(12));
		harmonyBuilders.add(harmony().pos(24).len(12));
		generator.setHarmonyBuilders(harmonyBuilders);
		
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		harmonicMelodies.add(harmonicMelody().voice(2).pos(0)
				.harmonyNote(NoteBuilder.note().build())
				.notes(note().voice(2).pos(0).len(6).build(), 
					   note().voice(2).pos(6).len(12).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(12)
				.harmonyNote(NoteBuilder.note().build())
				.notes(note().voice(2).pos(18).len(6).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(24)
				.harmonyNote(NoteBuilder.note().build())
				.notes(note().voice(2).pos(24).len(6).build(),
						note().voice(2).pos(30).len(6).build()).build());
		generator.setHarmonicMelodies(harmonicMelodies);
		musicProperties.setChordSize(3);
		
		int chordSize = 4;
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
		generator.setRhythmWeightValues(rhythmWeightValues);
		musicProperties.setOctaveHighestPitchClass(octave);
		musicProperties.setChordSize(chordSize);
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
	
	@Test
	public void generateHarmonicMelodiesForVoiceTest() {
		int[] harmonyPositions = {0,24,48,72,96,120};// last = length
		List<HarmonicMelody> harmonicMelodies = generator.generateHarmonicMelodiesForVoice(harmonyPositions, 3, 2);
		assertEquals(harmonyPositions.length - 2, harmonicMelodies.size());
	}

}
