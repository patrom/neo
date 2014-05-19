package test.neo.objective;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;
import neo.objective.voiceleading.VoiceLeadingObjective;

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
		harmonies.add(Examples.getChord(0,12, 0,4,7));
		harmonies.add(Examples.getChord(12,12, 11,2,7));
		voiceLeadingObjective = new VoiceLeadingObjective(musicProperties, new Motive(harmonies));
		double VoiceLeadingValue = voiceLeadingObjective.evaluate();
		System.out.println(VoiceLeadingValue);
		assertEquals("Wrong VoiceLeading value", 3, VoiceLeadingValue, 0);
	}
	
	@Test
	public void testMajorToMajorRootPosition() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(Examples.getChord(0,12, 0,4,7));
		harmonies.add(Examples.getChord(12,12, 7,11,2));
		voiceLeadingObjective = new VoiceLeadingObjective(musicProperties, new Motive(harmonies));
		double VoiceLeadingValue = voiceLeadingObjective.evaluate();
		System.out.println(VoiceLeadingValue);
		assertEquals("Wrong VoiceLeading value", 3, VoiceLeadingValue, 0);
	}

}
