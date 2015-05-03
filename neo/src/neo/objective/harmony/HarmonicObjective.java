package neo.objective.harmony;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import neo.model.Motive;
import neo.model.dissonance.Dissonance;
import neo.model.harmony.Harmony;
import neo.objective.Objective;

@Component
public class HarmonicObjective extends Objective {
	
	@Autowired 
	@Qualifier(value="AllTriads")
	private Dissonance dissonance;

	@Override
	public double evaluate(Motive motive) {
		List<Harmony> harmonies = motive.getHarmonies();
		double positionWeightTotal = harmonies.stream().mapToDouble(harmony ->  harmony.getPositionWeight()).sum();
		double sumChordPositionWeight = harmonies.stream()
				.mapToDouble(harmony -> dissonance.getDissonance(harmony.getChord()) * harmony.getPositionWeight()).sum();
		double chordPositionWeight = sumChordPositionWeight/positionWeightTotal;
		double sumChordInnerMetricWeight = harmonies.stream()
				.mapToDouble(harmony -> dissonance.getDissonance(harmony.getChord()) * harmony.getInnerMetricWeight()).sum();
		double chordInnerMetricWeight = sumChordInnerMetricWeight/harmonies.size();
		return (chordPositionWeight + chordInnerMetricWeight)/2;
	}

}
