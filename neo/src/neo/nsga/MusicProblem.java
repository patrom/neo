package neo.nsga;

import java.util.logging.Logger;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.FitnessObjectiveValues;
import neo.generator.MusicProperties;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.membership.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicProblem extends Problem {

	private static Logger LOGGER = Logger.getLogger(MusicProblem.class.getName());
	
	@Autowired
	private FitnessEvaluationTemplate fitnessEvaluationTemplate;

	private MembershipFunction melodyMembershipFunction;
	private MembershipFunction harmonyMembershipFunction;
	
	@Autowired
	public MusicProblem(MusicProperties properties) throws ClassNotFoundException {
		numberOfVariables_ = 1;
		numberOfObjectives_ = 5;
		numberOfConstraints_ = 0;
		problemName_ = "MusicProblem";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];
		
		this.melodyMembershipFunction = new MembershipFunctionTriangular(
				new Value(0.0), new Value(properties.getMelodyConsDissValue()),
				new Value(1.0));
		this.harmonyMembershipFunction = new MembershipFunctionTriangular(
				new Value(0.0),
				new Value(properties.getHarmonyConsDissValue()), new Value(1.0));
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		Variable[] variables = solution.getDecisionVariables();
//		fitnessEvaluationTemplate = new FitnessEvaluationTemplate(
//				properties, ((MusicVariable) variables[0]).getMotive());
		// FugaDecorator decorator = new FugaDecorator(controller, 12, 48);
		// DebussyDecorator decorator = new DebussyDecorator(controller);

		FitnessObjectiveValues objectives = fitnessEvaluationTemplate.evaluate(((MusicVariable) variables[0]).getMotive());

		double harmonyObjective = 1 - (objectives.getHarmony());
		solution.setObjective(0, harmonyObjective);// harmony
		if (objectives.getVoiceleading() < 4) {
			solution.setObjective(1, 0);
		} else {
			solution.setObjective(1, objectives.getVoiceleading());
		}
		double melodyObjective = 1 - melodyMembershipFunction.membership(1 - objectives.getMelody());
		solution.setObjective(2, melodyObjective);// melody
		double tonality = 1 - objectives.getTonality();
		solution.setObjective(3, tonality);

		// //constraints
		// objectives[5] = lowestIntervalRegisterValue;
		// objectives[6] = repetitionsPitchesMean; //only for small motives (5 -
		// 10 notes)
		// objectives[7] = repetitionsrhythmsMean; //only for small motives (5 -
		// 10 notes)

		MusicSolution musicSolution = (MusicSolution) solution;
		musicSolution.setHarmony(harmonyObjective);
		musicSolution.setVoiceLeading(objectives.getVoiceleading());
		musicSolution.setMelody(1 - objectives.getMelody());
		musicSolution.setTonality(tonality);
		// musicSolution.setConstraintLowestInterval(objectives[5]);
		// musicSolution.setConstraintRhythm(objectives[6]);
		// musicSolution.setConstraintRepetition(objectives[7]);

	}

	@Override
	public void evaluateConstraints(Solution solution) throws JMException {
		solution.setNumberOfViolatedConstraint(0);
		solution.setOverallConstraintViolation(0);
		// MusicSolution musicSolution = (MusicSolution) solution;
		// int violation = 0 ;
		// double total = 0.0;
		// harmony
		// if (musicSolution.getObjective(0) > 0.2) {
		// total += solution.getObjective(0);
		// violation++ ;
		// }
		// //lowest interval
		// if (musicSolution.getConstraintLowestInterval() > 0.0) {
		// total += musicSolution.getConstraintLowestInterval();
		// violation++ ;
		// }
		// //rhythm
		// if (musicSolution.getConstraintRhythm() > 0.7) {
		// total += musicSolution.getConstraintRhythm();
		// violation++ ;
		// }
		// //repetition
		// if (musicSolution.getConstraintRepetition() > 0.7) {
		// total += musicSolution.getConstraintRepetition();
		// violation++ ;
		// }
		//
		// solution.setOverallConstraintViolation(total);
		// solution.setNumberOfViolatedConstraint(violation) ;
	}

}
