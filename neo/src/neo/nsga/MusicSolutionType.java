package neo.nsga;

import java.util.List;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.evaluation.MusicProperties;
import neo.generator.Generator;
import neo.instrument.Instrument;

public class MusicSolutionType extends SolutionType {

	public List<Instrument> ranges;
	
	public MusicSolutionType(Problem problem, MusicProperties musicProperties) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		this.ranges = musicProperties.getRanges();
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		int chordSize = 4;
		int beat = 6;
		int[] rhythmTemplate = {0, 12, 24,36};
		Motive motive = Generator.generateMotive(rhythmTemplate, chordSize, beat);
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
