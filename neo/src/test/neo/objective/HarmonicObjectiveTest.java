package test.neo.objective;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.objective.Objective;
import neo.objective.harmony.ChordType;
import neo.objective.harmony.HarmonicObjective;

import org.junit.Test;

public class HarmonicObjectiveTest{
	
	private Objective harmonicObjective;

	@Test
	public void testEvaluate() {
		List<Harmony> list = new ArrayList<>();
		list.add(Examples.getChord(0,6, 0,4,7));
		list.add(Examples.getChord(6,6, 1,4,6));
		list.add(Examples.getChord(12,12, 11,2,7));
		list.add(Examples.getChord(24,12, 0,4,9));
		harmonicObjective = new HarmonicObjective(new Motive(list));
		double harmonicValue = harmonicObjective.evaluate();
		double expectedValue = (((2 * ChordType.MAJOR.getDissonance()) + ChordType.MINOR.getDissonance()) + ChordType.CH3.getDissonance())/4;
		assertEquals("Wrong harmonic value of chords", expectedValue, harmonicValue, 0);
	}

}
