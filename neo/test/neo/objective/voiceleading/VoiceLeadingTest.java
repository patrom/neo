package neo.objective.voiceleading;

import static neo.data.harmony.HarmonyBuilder.harmony;

import java.util.logging.Logger;

import neo.AbstractTest;
import neo.data.harmony.Harmony;
import neo.print.Display;

import org.junit.Test;

import com.google.common.collect.Multiset;

public class VoiceLeadingTest extends AbstractTest{
	
	@Test
	public void testMajorToMajorVoiceLeading() {
		Harmony majorHarmonySource = harmony().notes(0,4,7).build();
		Harmony majorHarmonyTarget = harmony().notes(0,4,7).build();
		compareVoiceLeading(majorHarmonySource, majorHarmonyTarget);
	}

	@Test
	public void testMajorToMinorVoiceLeading() {
		Harmony majorHarmony = harmony().notes(0,4,7).build();
		Harmony minorHarmony = harmony().notes(0,3,7).build();
		compareVoiceLeading(majorHarmony, minorHarmony);
	}
	
	@Test
	public void testDom7ToMajorVoiceLeading() {
		Harmony majorHarmonySource = harmony().notes(0,4,7,10).build();
		Harmony majorHarmonyTarget = harmony().notes(0,4,7).build();
		compareVoiceLeading(majorHarmonySource, majorHarmonyTarget);
	}
	
	@Test
	public void testMin7ToMajorVoiceLeading() {
		Harmony majorHarmonySource = harmony().notes(0,3,7,10).build();
		Harmony majorHarmonyTarget = harmony().notes(0,4,7).build();
		compareVoiceLeading(majorHarmonySource, majorHarmonyTarget);
	}
	
	private void compareVoiceLeading(Harmony source, Harmony target) {
		Multiset<Integer> sourceSet = source.getChord().getPitchClassMultiSet();
		for (int i = 0; i < 12; i++) {
			VoiceLeadingSize minimalVoiceLeadingSize = VoiceLeading.caculateSize(sourceSet, target.getChord().getPitchClassMultiSet());
			print(minimalVoiceLeadingSize);
			target.transpose(1);
		}
	}

	private void print(VoiceLeadingSize minimalVoiceLeadingSize) {
		LOGGER.info(minimalVoiceLeadingSize.getVlSource().toString());
		LOGGER.info(minimalVoiceLeadingSize.getVlTarget().toString());
		LOGGER.info(String.valueOf(minimalVoiceLeadingSize.getSize()));
	}

}