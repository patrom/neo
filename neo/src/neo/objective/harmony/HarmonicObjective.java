package neo.objective.harmony;

import java.util.List;

import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.objective.Objective;

public class HarmonicObjective extends Objective {

	public HarmonicObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		List<Harmony> harmonies = motive.getHarmonies();
		double positionWeightTotal = harmonies.stream().mapToDouble(harmony ->  harmony.getPositionWeight()).sum();
		double sumChordPositionWeight = harmonies.stream()
				.mapToDouble(harmony -> harmony.getChordWeight() * harmony.getPositionWeight()).sum();
		double chordPositionWeight = sumChordPositionWeight/positionWeightTotal;
		double sumChordInnerMetricWeight = harmonies.stream()
				.mapToDouble(harmony -> harmony.getChordWeight() * harmony.getInnerMetricWeight()).sum();
		double chordInnerMetricWeight = sumChordInnerMetricWeight/harmonies.size();
		return (chordPositionWeight + chordInnerMetricWeight)/2;
	}

}
