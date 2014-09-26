package neo.objective.tonality;

import java.util.List;

import org.springframework.stereotype.Component;

import neo.model.Motive;
import neo.model.melody.Melody;
import neo.objective.Objective;

@Component
public class TonalityObjective extends Objective {

	@Override
	public double evaluate(Motive motive) {
		return evaluateMajorMinorTonality(motive.getMelodies());
	}
	
	private double evaluateMajorMinorTonality(List<Melody> melodies) {
		double major = TonalityFunctions.getMaxCorrelationTonality(melodies, TonalityFunctions.vectorMajorTemplate);
		double minor = TonalityFunctions.getMaxCorrelationTonality(melodies, TonalityFunctions.vectorMinorTemplate);
		if (major > minor) {
			return major;
		} else {
			return minor;
		}
	}

}
