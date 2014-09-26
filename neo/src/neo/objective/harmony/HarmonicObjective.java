package neo.objective.harmony;

import java.util.List;

import org.springframework.stereotype.Component;

import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.objective.Objective;

@Component
public class HarmonicObjective extends Objective {

	@Override
	public double evaluate(Motive motive) {
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
