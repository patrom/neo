package neo.objective.voiceleading;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

import org.junit.Before;
import org.junit.Test;

public class VoiceLeadingObjectiveTest {
	
	private Objective voiceLeadingObjective;
	private MusicProperties musicProperties;

	@Before
	public void setUp() {
		musicProperties = new MusicProperties();
		musicProperties.setHarmonyBeatDivider(12);
	}

	@Test
	public void testMajorToMajorFirstInv() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(12).notes(0,4,7).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).build());
		voiceLeadingObjective = new VoiceLeadingObjective(musicProperties, new Motive(harmonies));
		double VoiceLeadingValue = voiceLeadingObjective.evaluate();
		System.out.println(VoiceLeadingValue);
		assertEquals("Wrong VoiceLeading value", 3, VoiceLeadingValue, 0);
	}
	
	@Test
	public void testMajorToMajorRootPosition() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(12).notes(0,4,7).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).build());
		voiceLeadingObjective = new VoiceLeadingObjective(musicProperties, new Motive(harmonies));
		double VoiceLeadingValue = voiceLeadingObjective.evaluate();
		System.out.println(VoiceLeadingValue);
		assertEquals("Wrong VoiceLeading value", 3, VoiceLeadingValue, 0);
	}

}
