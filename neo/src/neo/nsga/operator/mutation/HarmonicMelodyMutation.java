package neo.nsga.operator.mutation;

import java.util.List;
import java.util.Optional;

import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;

public class HarmonicMelodyMutation {

	private List<Integer> allowedMutationIndexes;
	
	public Optional<HarmonicMelody> randomHarmonicMelodyWithMultipleNotes(Harmony harmony) {
		return harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> harmonicMelody.getMelodyNotes().size() > 1 && allowedMutationIndexes.contains(harmonicMelody.getVoice()))
			.findAny();
	}
	
	public Optional<HarmonicMelody> randomHarmonicMelody(Harmony harmony) {
		return harmony.getHarmonicMelodies().stream()
			.filter(harmonicMelody -> allowedMutationIndexes.contains(harmonicMelody.getVoice()))
			.findAny();
	}

	public List<Integer> getAllowedMutationIndexes() {
		return allowedMutationIndexes;
	}

	public void setAllowedMutationIndexes(List<Integer> allowedMutationIndexes) {
		this.allowedMutationIndexes = allowedMutationIndexes;
	}
	
}
