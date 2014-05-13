package neo.nsga;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;
import neo.generator.Generator;

public class MusicSolutionType extends SolutionType {

	private Scale scale;
	private int[] rhythmGeneratorTemplate;
	
	public MusicSolutionType(Problem problem, MusicProperties musicProperties) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		this.scale = musicProperties.getScale();
		this.rhythmGeneratorTemplate = musicProperties.getRhythmGeneratorTemplate();
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		int chordSize = 4;
		int octave = 5;
		Motive motive = Generator.generateMotive(scale, rhythmGeneratorTemplate, chordSize, octave);
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
