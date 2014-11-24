package neo.nsga.operator.mutation.melody;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

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
import neo.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="oneNoteMutation")
public class OneNoteMutation extends AbstractMutation {
	
	@Autowired
	private MusicProperties musicProperties;
	
	@Autowired
	public OneNoteMutation(HashMap<String, Object> parameters) {
		super(parameters);
	}

	private static Logger LOGGER = Logger.getLogger(OneNoteMutation.class.getName());

	/**
	 * Perform the mutation operation
	 * @param probability Mutation probability
	 * @param solution The solution to mutate
	 * @throws JMException
	 */
	public void doMutation(double probability, Solution solution) throws JMException {
		if (PseudoRandom.randDouble() < probability) {
			Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
			mutateMelodyNoteRandom( motive.getHarmonies());
			LOGGER.fine("one note mutated");
		} 
	}

	private void mutateMelodyNoteRandom(List<Harmony> harmonies) {
		Harmony harmony = harmonies.get(RandomUtil.random(harmonies.size()));
		HarmonicMelody harmonicMelody = harmonicMelodyMutation.randomHarmonicMelodyWithMultipleNotes(harmony);
		if (harmonicMelody != null) {
			int newPitchClass = musicProperties.getMelodyScale().pickRandomPitchClass();
			harmonicMelody.randomUpdateMelodyNotes(newPitchClass);
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
