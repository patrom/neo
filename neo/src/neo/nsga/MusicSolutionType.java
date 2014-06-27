package neo.nsga;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.evaluation.MusicProperties;
import neo.generator.Generator;

public class MusicSolutionType extends SolutionType {

	private MusicProperties musicProperties;
	
	public MusicSolutionType(Problem problem, MusicProperties musicProperties) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		this.musicProperties = musicProperties;
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		Generator generator = new Generator(musicProperties);
		Motive motive = generator.generateMotive();
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
