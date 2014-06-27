package neo.objective.harmony;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

import org.junit.Before;
import org.junit.Test;

public class HarmonicObjectiveTest{
	
	private Objective harmonicObjective;
	private MusicProperties musicProperties;
	private Map<Integer, Double> rhythmWeightValues;
	
	@Before
	public void setup() {
		musicProperties = new MusicProperties();
		musicProperties.setMinimumLength(6);
		rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.0);
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
	}

	@Test
	public void testEvaluate() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(6).notes(0,4,7).weight(1.0).build());
		harmonies.add(harmony().pos(6).len(6).notes(1,4,6).weight(0.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).weight(1.0).build());
		harmonicObjective = new HarmonicObjective(musicProperties, new Motive(harmonies));
		double harmonicValue = harmonicObjective.evaluate();
		System.out.println(harmonicValue);
		double expectedValue = ((ChordType.MAJOR.getDissonance() * rhythmWeightValues.get(0)) + (ChordType.MAJOR.getDissonance() * rhythmWeightValues.get(12)) 
				+ (ChordType.CH3.getDissonance() * rhythmWeightValues.get(6)))/3;
		assertEquals("Wrong harmonic value of chords", expectedValue, harmonicValue, 0);
	}

}
