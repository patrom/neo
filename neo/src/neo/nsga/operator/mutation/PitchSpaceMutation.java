package neo.nsga.operator.mutation;

import java.util.List;
import java.util.logging.Logger;

import jmetal.base.Solution;
import jmetal.base.operator.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.nsga.MusicVariable;

public class PitchSpaceMutation extends Mutation {
	
	private static Logger LOGGER = Logger.getLogger(OneNoteMutation.class.getName());

	public PitchSpaceMutation() {	
	} 

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		if (PseudoRandom.randDouble() < probability) {
			Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
			List<Harmony> harmonies = motive.getHarmonies();
			int harmonyIndex = PseudoRandom.randInt(0, harmonies.size() - 1);
			Harmony harmony = harmonies.get(harmonyIndex);
			harmony.mutatePitchSpaceStrategy();
			LOGGER.fine("pitch space mutated");
		} 
	}

	
	/**
	 * Executes the operation
	 * @param object An object containing a solution to mutate
	 * @return An object containing the mutated solution
	 * @throws JMException 
	 */
	public Object execute(Object object) throws JMException {
		Solution solution = (Solution) object;

		Double probability = (Double) getParameter("probabilityPitchSpace");
		if (probability == null) {
			Configuration.logger_.severe("probabilityOneNote: probability not " +
			"specified");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}

		doMutation(probability.doubleValue(), solution);
		return solution;
	} 

}

