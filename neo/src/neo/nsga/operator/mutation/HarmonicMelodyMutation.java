package neo.nsga.operator.mutation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.util.RandomUtil;

public class HarmonicMelodyMutation {

	private List<Integer> allowedMutationIndexes;
	
	public Optional<HarmonicMelody> randomHarmonicMelodyWithMultipleNotes(Harmony harmony) {
		return harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> harmonicMelody.getMelodyNotes().size() > 1 && allowedMutationIndexes.contains(harmonicMelody.getVoice()))
			.findAny();
	}
	
	public HarmonicMelody randomHarmonicMelody(Harmony harmony) {
		List<HarmonicMelody> harmonicMelodies =  harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> allowedMutationIndexes.contains(harmonicMelody.getVoice())).collect(Collectors.toList());
		return RandomUtil.getRandomFromList(harmonicMelodies);
	}

	public List<Integer> getAllowedMutationIndexes() {
		return allowedMutationIndexes;
	}

	public void setAllowedMutationIndexes(List<Integer> allowedMutationIndexes) {
		this.allowedMutationIndexes = allowedMutationIndexes;
	}
	
}
