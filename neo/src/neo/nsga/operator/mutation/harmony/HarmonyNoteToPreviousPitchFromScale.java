package neo.nsga.operator.mutation.harmony;

import java.util.HashMap;
import java.util.List;

import jmetal.core.Solution;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.nsga.MusicVariable;
import neo.nsga.operator.mutation.AbstractMutation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="harmonyNoteToPreviousPitchFromScale")
public class HarmonyNoteToPreviousPitchFromScale extends AbstractMutation{

	@Autowired
	private MusicProperties musicProperties;

	@Autowired
	public HarmonyNoteToPreviousPitchFromScale(HashMap<String, Object> parameters) {
		super(parameters);
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
			mutateHarmonyNoteToPreviousPitchFromScale(motive.getHarmonies());
		} 
	}

	private void mutateHarmonyNoteToPreviousPitchFromScale(
			List<Harmony> harmonies) {
		Harmony harmony = harmonyMutation.randomHarmony(harmonies);
		HarmonicMelody harmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		if (harmonicMelody != null) {
			harmonicMelody.mutateHarmonyNoteToPreviousPitchFromScale(musicProperties.getScale());
//			harmony.toChord();
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
		Double probability = (Double) getParameter("probabilityOneNote");
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


