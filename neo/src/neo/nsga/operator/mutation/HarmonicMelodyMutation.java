package neo.nsga.operator.mutation;

import java.util.Optional;

import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.util.RandomUtil;

public class HarmonicMelodyMutation {

	private int[] allowedMutationIndexes;
	
	public int getMutationIndex() {
		int index = RandomUtil.random(allowedMutationIndexes.length);
		return allowedMutationIndexes[index];
	}
	
	public HarmonicMelody randomHarmonicMelodyWithMultipleNotes(Harmony harmony) {
		Optional<HarmonicMelody> optional = harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> harmonicMelody.getMelodyNotes().size() > 1 && harmonicMelody.getVoice() == getMutationIndex())
			.findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public HarmonicMelody randomHarmonicMelody(Harmony harmony) {
		Optional<HarmonicMelody> optional = harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> harmonicMelody.getVoice() == getMutationIndex())
			.findAny();
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public int[] getAllowedMutationIndexes() {
		return allowedMutationIndexes;
	}

	public void setAllowedMutationIndexes(int[] allowedMutationIndexes) {
		this.allowedMutationIndexes = allowedMutationIndexes;
	}
	
}
