package neo.objective.harmony;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.AbstractTest;
import neo.data.Motive;
import neo.data.harmony.ChordType;
import neo.data.harmony.Harmony;
import neo.objective.Objective;

import org.junit.Before;
import org.junit.Test;

public class HarmonicObjectiveTest extends AbstractTest{
	
	private Objective harmonicObjective;
	
	@Before
	public void setup() {
		musicProperties.setMinimumLength(6);
	}

	@Test
	public void testEvaluate() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(6).notes(0,4,7).positionWeight(1.0).build());
		harmonies.add(harmony().pos(6).len(6).notes(1,4,6).positionWeight(0.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).positionWeight(1.0).build());
		harmonicObjective = new HarmonicObjective(musicProperties, new Motive(harmonies));
		double harmonicValue = harmonicObjective.evaluate();
		LOGGER.info("harmonicValue : " + harmonicValue);
		double expectedValue = ((ChordType.MAJOR.getDissonance() * 1.0) + (ChordType.MAJOR.getDissonance() * 1.0) 
				+ (ChordType.CH3.getDissonance() * 0.5))/3;
		assertEquals("Wrong harmonic value of chords", expectedValue, harmonicValue, 0);
	}

}
