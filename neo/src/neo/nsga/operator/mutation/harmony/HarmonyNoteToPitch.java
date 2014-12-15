package neo.nsga.operator.mutation.harmony;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

@Component(value="harmonyNoteToPitch")
public class HarmonyNoteToPitch extends AbstractMutation{

	@Autowired
	public HarmonyNoteToPitch(HashMap<String, Object> parameters) {
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
			mutateHarmonyNoteToRandomPitch(motive.getHarmonies());
			mutateHarmonyNoteToPreviousPitch(motive.getHarmonies());
			mutateHarmonyNoteToNextPitch(motive.getHarmonies());
		} 
	}

	private void mutateHarmonyNoteToRandomPitch(List<Harmony> harmonies) {
		Harmony harmony = harmonyMutation.randomHarmony(harmonies);
		Optional<HarmonicMelody> harmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		if (harmonicMelody.isPresent()) {
			harmonicMelody.get().mutateHarmonyNoteToRandomPitch(musicProperties.getScale());
		}
	}
	
	private void mutateHarmonyNoteToPreviousPitch(List<Harmony> harmonies) {
		Harmony harmony = harmonyMutation.randomHarmony(harmonies);
		Optional<HarmonicMelody> harmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		if (harmonicMelody.isPresent()) {
			harmonicMelody.get().mutateHarmonyPreviousNoteToPitch(musicProperties.getScale());
		}
	}
	
	private void mutateHarmonyNoteToNextPitch(List<Harmony> harmonies) {
		Harmony harmony = harmonyMutation.randomHarmony(harmonies);
		Optional<HarmonicMelody> harmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		if (harmonicMelody.isPresent()) {
			harmonicMelody.get().mutateHarmonyPreviousNoteToPitch(musicProperties.getScale());
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
		Double probability = (Double) getParameter("probabilityHarmonyNoteToPitch");
		if (probability == null) {
			Configuration.logger_.severe("probabilityHarmonyNoteToPitch: probability not " +
			"specified");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}
		doMutation(probability.doubleValue(), solution);
		return solution;
	} 
}


