package neo.data.harmony;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class HarmonyTest {

	@Test
	public void testTransposeSecond() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(1);
		assertEquals(1, harmony.getNotes().get(0).getPitchClass());
		assertEquals(5, harmony.getNotes().get(1).getPitchClass());
		assertEquals(8, harmony.getNotes().get(2).getPitchClass());
		assertTrue(harmony.getChord().getPitchClassMultiSet().contains(1));
	}
	
	@Test
	public void testTransposeSixth() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(8);
		assertEquals(8, harmony.getNotes().get(0).getPitchClass());
		assertEquals(0, harmony.getNotes().get(1).getPitchClass());
		assertEquals(3, harmony.getNotes().get(2).getPitchClass());
		assertTrue(harmony.getChord().getPitchClassMultiSet().contains(8));
	}
	
	
	@Test
	public void testMutatePitchSpaceStrategy() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(8);
		assertEquals(8, harmony.getNotes().get(0).getPitchClass());
		assertEquals(0, harmony.getNotes().get(1).getPitchClass());
		assertEquals(3, harmony.getNotes().get(2).getPitchClass());
		harmony.mutatePitchSpaceStrategy();
	}

}
