package test.neo.objective;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;
import neo.objective.harmony.ChordType;
import neo.objective.harmony.HarmonicObjective;

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
		rhythmWeightValues.put(18, 0.5);
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
	}

	@Test
	public void testEvaluate() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(Examples.getChord(0,6, 0,4,7));
		harmonies.add(Examples.getChord(6,6, 1,4,6));
		harmonies.add(Examples.getChord(12,12, 11,2,7));
		harmonicObjective = new HarmonicObjective(musicProperties, new Motive(harmonies));
		double harmonicValue = harmonicObjective.evaluate();
		System.out.println(harmonicValue);
		double expectedValue = ((2 * ChordType.MAJOR.getDissonance() * rhythmWeightValues.get(0)) + (ChordType.MAJOR.getDissonance() * rhythmWeightValues.get(6)) 
				+ (ChordType.CH3.getDissonance() * rhythmWeightValues.get(18)))/rhythmWeightValues.size();
		assertEquals("Wrong harmonic value of chords", expectedValue, harmonicValue, 0);
	}

}
