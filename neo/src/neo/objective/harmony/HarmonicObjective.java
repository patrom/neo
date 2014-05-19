package neo.objective.harmony;

import java.util.Map;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class HarmonicObjective extends Objective {

	public HarmonicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		Map<Integer, Double> rhythmWeightValues = musicProperties.getRhythmWeightValues();
		int minimumLength = musicProperties.getMinimumLength();
		double totalWeight = 0;
		for (Harmony harmony : motive.getHarmonies()) {
			for (int i = 0; i < harmony.getLength(); i = i + minimumLength) {
				totalWeight = totalWeight + (harmony.getWeight() * rhythmWeightValues.get(harmony.getPosition() + i));
			}
		}
		return totalWeight/rhythmWeightValues.size();
//		OptionalDouble optionalDouble = motive.getHarmonies().stream().mapToDouble(ch -> ch.getWeight()).average();
//		return motive.getHarmonies().map(n -> n.toChord()).mapToDouble(ch -> ch.getWeight()).peek(System.out::println).sum();
//		return optionalDouble.getAsDouble();
	}

}
