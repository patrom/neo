package neo.nsga;

import java.util.logging.Logger;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.util.JMException;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.FitnessObjectiveValues;
import neo.evaluation.MusicProperties;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunction;
import net.sourceforge.jFuzzyLogic.membership.MembershipFunctionTriangular;
import net.sourceforge.jFuzzyLogic.membership.Value;

public class MusicProblem extends Problem {

	private static Logger LOGGER = Logger.getLogger(MusicProblem.class
			.getName());
	
	private FitnessEvaluationTemplate fitnessEvaluationTemplate;

	private MusicProperties properties;

	private MembershipFunction melodyMembershipFunction;
	private MembershipFunction harmonyMembershipFunction;

	public MusicProblem(MusicProperties inputProps) throws ClassNotFoundException {
		numberOfVariables_ = 1;
		numberOfObjectives_ = 5;
		numberOfConstraints_ = 0;
		problemName_ = "MusicProblem";

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		this.properties = inputProps;
		this.melodyMembershipFunction = new MembershipFunctionTriangular(
				new Value(0.0), new Value(inputProps.getMelodyConsDissValue()),
				new Value(1.0));
		this.harmonyMembershipFunction = new MembershipFunctionTriangular(
				new Value(0.0),
				new Value(inputProps.getHarmonyConsDissValue()), new Value(1.0));
	}

	@Override
	public void evaluate(Solution solution) throws JMException {
		Variable[] variables = solution.getDecisionVariables();
		fitnessEvaluationTemplate = new FitnessEvaluationTemplate(
				properties, ((MusicVariable) variables[0]).getMotive());
		// FugaDecorator decorator = new FugaDecorator(controller, 12, 48);
		// DebussyDecorator decorator = new DebussyDecorator(controller);

		FitnessObjectiveValues objectives = fitnessEvaluationTemplate.evaluate();

		double harmonyObjective = 1 - (objectives.getHarmony());
		solution.setObjective(0, harmonyObjective);// harmony
		if (objectives.getVoiceleading() < 4) {
			solution.setObjective(1, 0);
		} else {
			solution.setObjective(1, objectives.getVoiceleading());
		}
		double melodyObjective = 1 - melodyMembershipFunction.membership(1 - objectives.getMelody());
		solution.setObjective(2, melodyObjective);// melody
		// solution.setObjective(4, 1 - objectives[4]);//tonality

		// solution.setObjective(4, objectives[7]);//repetition of notes (0 =
		// repeat notes)

		// objectives[0] = 1 - totalHarmony;
		// objectives[1] = voiceLeadingMean;
		// objectives[2] = 1 - melodicValue;
		// objectives[3] = rhythmicValue;
		// objectives[4] = 1 - tonalityValue;
		// //constraints
		// objectives[5] = lowestIntervalRegisterValue;
		// objectives[6] = repetitionsPitchesMean; //only for small motives (5 -
		// 10 notes)
		// objectives[7] = repetitionsrhythmsMean; //only for small motives (5 -
		// 10 notes)

		MusicSolution musicSolution = (MusicSolution) solution;
		// musicSolution.setHarmony(objectives[0]);
		musicSolution.setHarmony(harmonyObjective);
		musicSolution.setVoiceLeading(objectives.getVoiceleading());
		musicSolution.setMelody(1 - objectives.getMelody());
		// musicSolution.setMelody(melodyObjective);
		// musicSolution.setRhythm(rhythmObjective);
		// musicSolution.setTonality(objectives[4]);
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
