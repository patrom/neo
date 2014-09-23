package neo.nsga;

import jmetal.core.Problem;
import jmetal.core.SolutionType;
import jmetal.core.Variable;
import neo.data.Motive;

public class MusicSolutionType extends SolutionType {

	private Motive motive;
	
	public MusicSolutionType(Problem problem, Motive motive) {
		super(problem);
		problem.setSolutionType(this);
		this.motive = motive;
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
