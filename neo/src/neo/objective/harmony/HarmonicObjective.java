package neo.objective.harmony;

import java.util.OptionalDouble;

import neo.data.Motive;
import neo.objective.Objective;

public class HarmonicObjective extends Objective {

	@Override
	public double evaluate(Motive motive) {
		OptionalDouble optionalDouble = motive.getHarmonies().stream().map(n -> n.toChord()).mapToDouble(ch -> ch.getWeight()).average();
//		return motive.getHarmonies().map(n -> n.toChord()).mapToDouble(ch -> ch.getWeight()).peek(System.out::println).sum();
		return optionalDouble.getAsDouble();
	}

}
