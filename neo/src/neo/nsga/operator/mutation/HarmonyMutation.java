package neo.nsga.operator.mutation;

import java.util.List;

import neo.generator.MusicProperties;
import neo.model.harmony.Harmony;
import neo.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HarmonyMutation {
	
	@Autowired
	private MusicProperties musicProperties;

	public Harmony randomHarmony(List<Harmony> harmonies) {
		int harmonyIndex = 0;
		if (musicProperties.isOuterBoundaryIncluded()) {
			harmonyIndex = RandomUtil.randomInt(0, harmonies.size());
		} else {
			harmonyIndex = RandomUtil.randomInt(1, harmonies.size() - 1);
		}
		Harmony harmony = harmonies.get(harmonyIndex);
		return harmony;
	}
}
