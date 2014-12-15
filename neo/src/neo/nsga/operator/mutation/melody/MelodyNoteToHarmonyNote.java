package neo.nsga.operator.mutation.melody;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import jmetal.core.Solution;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.nsga.MusicVariable;
import neo.nsga.operator.mutation.AbstractMutation;
import neo.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="melodyNoteToHarmonyNote")
public class MelodyNoteToHarmonyNote extends AbstractMutation{
	
	@Autowired
	public MelodyNoteToHarmonyNote(HashMap<String, Object> parameters) {
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
			mutateMelodyNoteToHarmonyNote(motive.getHarmonies());
			LOGGER.fine("one note mutated");
		} 
	}

	protected void mutateMelodyNoteToHarmonyNote(List<Harmony> harmonies) {
		Harmony harmony = harmonies.get(RandomUtil.random(harmonies.size()));
		Optional<HarmonicMelody> harmonicMelody = harmonicMelodyMutation.randomHarmonicMelodyWithMultipleNotes(harmony);
		if (harmonicMelody.isPresent()) {
			Note harmonyNote = RandomUtil.getRandomFromList(harmony.getNotes());
			harmonicMelody.get().mutateMelodyNoteToHarmonyNote(harmonyNote.getPitchClass());
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
		Double probability = (Double) getParameter("probabilityMelodyNoteToHarmonyNote");
		if (probability == null) {
			Configuration.logger_.severe("probabilityMelodyNoteToHarmonyNote: probability not " +
			"specified");
			Class cls = java.lang.String.class;
			String name = cls.getName();
			throw new JMException("Exception in " + name + ".execute()");
		}
		doMutation(probability.doubleValue(), solution);
		return solution;
	} 

}

