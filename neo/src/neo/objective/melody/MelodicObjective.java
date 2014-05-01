package neo.objective.melody;

import java.util.List;

import neo.data.Motive;
import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.objective.Objective;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class MelodicObjective extends Objective {

	public MelodicObjective(Motive motive) {
		super(motive);
	}

	@Override
	public double evaluate() {
		double total = 0.0;
		int count = 0;
		for(Melody melody : motive.getMelodies()){
			List<NotePos> notePositions = melody.getNotes();
			notePositions.stream().forEach(n -> System.out.print( "," + n.getPitch()));
			System.out.println();
			if (notePositions.size() > 1) {
				List<Double> listWeights;
				if (notePositions.size() > 4) {
					listWeights = MelodicFunctions.getMelodicWeights2(notePositions, 4);
				} else {
					listWeights = MelodicFunctions.getMelodicWeights2(notePositions, notePositions.size());
				}
				double[] melodicWeights = listToArray(listWeights);
//				LOGGER.fine(Arrays.toString(melodicWeights));
				DescriptiveStatistics stats = new DescriptiveStatistics(melodicWeights);
				// Compute some statistics
				double mean = stats.getGeometricMean();
//					double mean = stats.getStandardDeviation();
//				LOGGER.fine("melodicValue mean: " + mean);
//				LOGGER.fine("melodicValue standarddeviation: " + stats.getStandardDeviation());
				if (!Double.isNaN(mean)) {//when melody contains no intervals (note repeat, octave)
					total = total + mean;
					count++;
				}
			}
		}
		return total/count;
	}
		
		private double[] listToArray(List<Double> values) {
			Double[] valuesArray = new Double[values.size()];
			valuesArray = values.toArray(valuesArray);
			double[] v = ArrayUtils.toPrimitive(valuesArray);
			return v;
		}

}
