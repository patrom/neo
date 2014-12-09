package neo.nsga.operator.mutation.harmony;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="swapHarmonyNotes")
public class SwapHarmonyNotes extends AbstractMutation{
	
	@Autowired
	public SwapHarmonyNotes(HashMap<String, Object> parameters) {
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
			swapHarmonyNotes(motive.getHarmonies());
		} 
	}

	private void swapHarmonyNotes(List<Harmony> harmonies) {
		Harmony harmony = harmonyMutation.randomHarmony(harmonies);
		Optional<HarmonicMelody> harmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		Optional<HarmonicMelody> switchHarmonicMelody = harmonicMelodyMutation.randomHarmonicMelody(harmony);
		if (harmonicMelody.isPresent() && switchHarmonicMelody.isPresent() &&
				harmonicMelody.get().getVoice() != switchHarmonicMelody.get().getVoice()) {
			Note harmonyNote = harmonicMelody.get().getHarmonyNote();
			Note switchHarmonyNote = switchHarmonicMelody.get().getHarmonyNote();
			if (!harmonyNote.equals(switchHarmonyNote)) {
				int harmonyPitchClass = harmonyNote.getPitchClass();
				int switchHarmonyPitchClass = switchHarmonyNote.getPitchClass();
				harmonicMelody.get().updateMelodyNotes(harmonyPitchClass, switchHarmonyPitchClass);
				harmonyNote.setPitchClass(switchHarmonyPitchClass);
				switchHarmonicMelody.get().updateMelodyNotes(switchHarmonyPitchClass, harmonyPitchClass);
				switchHarmonyNote.setPitchClass(harmonyPitchClass);
//				harmony.toChord();
			}
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
