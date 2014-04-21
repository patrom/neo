package neo.nsga;

import java.util.List;

import jmetal.base.Problem;
import jmetal.base.SolutionType;
import jmetal.base.Variable;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.generator.Generator;
import neo.instrument.Instrument;

public class MusicSolutionType extends SolutionType {

	public int[] scale;
	public List<Instrument> ranges;
	public int melodyLength ;
	private int[] profile;
	private String strategy;
	private int length;
	
	public MusicSolutionType(Problem problem, int melodyLenth, int[] scale, int[] profile, String strategy, List<Instrument> ranges, int length) {
		super(problem);
		problem.variableType_ = new Class[problem.getNumberOfVariables()];
		problem.setSolutionType(this);
		this.scale = scale;
		this.melodyLength = melodyLenth;
		this.ranges = ranges;
		this.profile = profile;
		this.strategy = strategy;
		this.length = length;
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
