package neo.nsga;

import java.util.List;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;
import neo.evaluation.RhythmPosition;
import neo.generator.Generator;
import neo.instrument.Instrument;

public class MusicSolutionType extends SolutionType {

	private Scale scale;
	
	public MusicSolutionType(Problem problem, Scale scale) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		this.scale = scale;
	}

	@Override
	public Variable[] createVariables() throws ClassNotFoundException {
		Variable[] variables = new Variable[problem_.getNumberOfVariables()];
		int chordSize = 4;
		int octave = 5;
		RhythmPosition[] rhythmTemplate = new RhythmPosition[8];
		rhythmTemplate[0] = new RhythmPosition(0, 25);
		rhythmTemplate[1] = new RhythmPosition(12, 25);
		rhythmTemplate[2] = new RhythmPosition(18, 25);
		rhythmTemplate[3] = new RhythmPosition(24, 25);
		
		rhythmTemplate[4] = new RhythmPosition(36, 25);
		rhythmTemplate[5] = new RhythmPosition(42, 25);
		rhythmTemplate[6] = new RhythmPosition(48, 25);
		rhythmTemplate[7] = new RhythmPosition(60, 25);
		Motive motive = Generator.generateMotive(scale, rhythmTemplate, chordSize, octave);
		variables[0] = new MusicVariable(motive);
		return variables ;
	}	

}
