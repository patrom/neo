package neo.objective.harmony;

import java.util.OptionalDouble;

import neo.data.Motive;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class HarmonicObjective extends Objective {

	public HarmonicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		OptionalDouble optionalDouble = motive.getHarmonies().stream()
				.mapToDouble(harmony -> harmony.getWeight() * harmony.getPositionWeight()).average();
		return optionalDouble.getAsDouble();
	}

}
