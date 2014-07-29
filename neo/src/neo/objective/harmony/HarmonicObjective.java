package neo.objective.harmony;

import neo.data.Motive;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class HarmonicObjective extends Objective {

	public HarmonicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		double positionWeightTotal = motive.getHarmonies().stream().mapToDouble(harmony ->  harmony.getPositionWeight()).sum();
		double harmonicValue = motive.getHarmonies().stream()
				.mapToDouble(harmony -> harmony.getChordWeight() * harmony.getPositionWeight()).sum();
		return harmonicValue/positionWeightTotal;
	}

}
