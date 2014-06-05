package test.neo.objective;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.data.note.Interval;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;
import neo.objective.harmony.ChordType;
import neo.objective.harmony.HarmonicObjective;
import neo.objective.melody.MelodicObjective;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MelodicObjectiveTest {

	private Objective melodicObjective;
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
		harmonies.add(Examples.getChord(0,6, 0));
		harmonies.add(Examples.getChord(6,6, 1));
		harmonies.add(Examples.getChord(12,12, 11));
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies));
		double melodicValue = melodicObjective.evaluate();
		System.out.println(melodicValue);
		double expected = (Interval.KLEINE_SECONDE.getMelodicValue() + Interval.KLEIN_SEPTIEM.getMelodicValue()) / (harmonies.size() - 1);
		Assert.assertEquals(expected, melodicValue, 0);
	}

}
