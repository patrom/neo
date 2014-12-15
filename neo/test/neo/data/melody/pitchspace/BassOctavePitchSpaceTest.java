package neo.data.melody.pitchspace;



import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.BassOctavePitchSpace;
import neo.model.melody.pitchspace.PitchSpace;
import neo.model.note.Note;

import org.junit.Test;

public class BassOctavePitchSpaceTest extends PitchSpaceTest{

	@Test
	public void testTranslateToPitchSpace() {
		List<HarmonicMelody> harmonicMelodies = getHarmonicMelodies(1,2,3,4);
		Harmony harmony = new Harmony(0, 30, harmonicMelodies);
		PitchSpace pitchSpace = new BassOctavePitchSpace(range, instruments);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", harmonicMelodies.get(3).getHarmonyNote().getPitch(), 64);
		assertEquals("pitch not correct", harmonicMelodies.get(2).getHarmonyNote().getPitch(), 63);
		assertEquals("pitch not correct", harmonicMelodies.get(1).getHarmonyNote().getPitch(), 50);
		assertEquals("pitch not correct", harmonicMelodies.get(0).getHarmonyNote().getPitch(), 37);
	}

}
