package neo.nsga.operator.mutation.pitch;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import jmetal.core.Solution;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.pitchspace.BassOctavePitchSpace;
import neo.model.melody.pitchspace.MiddleOctavePitchSpace;
import neo.model.melody.pitchspace.PitchSpace;
import neo.model.melody.pitchspace.TopOctavePitchSpace;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.nsga.MusicVariable;
import neo.nsga.operator.mutation.melody.OneNoteMutation;
import neo.util.RandomUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="pitchSpaceMutation")
public class PitchSpaceMutation extends Mutation {
	
	@Autowired
	private MusicProperties musicProperties;
	
	@Autowired
	public PitchSpaceMutation(HashMap<String, Object> parameters) {
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
			List<Harmony> harmonies = motive.getHarmonies();
			IntStream ints = RandomUtil.range(harmonies.size());
			PitchSpace pitchSpace = randomPitchSpace();
			ints.forEach(i -> {
				Harmony harmony = harmonies.get(i);
				harmony.setPitchSpace(pitchSpace);
			});
			LOGGER.fine("pitch space mutated");
		} 
	}
	
	public PitchSpace randomPitchSpace(){
		PitchSpace pitchSpace = null;
		switch (RandomUtil.randomInt(0, 4)) {
		case 0:
			pitchSpace = new UniformPitchSpace(musicProperties.getOctaveHighestPitchClassRange(), musicProperties.getInstruments());
			break;
		case 1:
			pitchSpace = new BassOctavePitchSpace(musicProperties.getOctaveHighestPitchClassRange(), musicProperties.getInstruments());
			break;
		case 2:
			pitchSpace = new TopOctavePitchSpace(musicProperties.getOctaveHighestPitchClassRange(), musicProperties.getInstruments());
			break;
		case 3:
			pitchSpace = new  MiddleOctavePitchSpace(musicProperties.getOctaveHighestPitchClassRange(), musicProperties.getInstruments());
			break;
		}
		return pitchSpace;
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

