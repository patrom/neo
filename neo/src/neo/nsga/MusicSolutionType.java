package neo.nsga;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.evaluation.MusicProperties;
import neo.generator.Generator;

public class MusicSolutionType extends SolutionType {

	private Generator generator;
	
	public MusicSolutionType(Problem problem, MusicProperties musicProperties) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		generator = new Generator(musicProperties);
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		Motive motive = generator.generateMotive();
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
