package neo.nsga.operator.mutation;

import java.util.HashMap;
import java.util.List;

import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;
import neo.model.harmony.Harmony;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractMutation extends Mutation{

	public AbstractMutation(HashMap<String, Object> parameters) {
		super(parameters);
	}

	protected HarmonicMelodyMutation harmonicMelodyMutation = new HarmonicMelodyMutation();
	
	protected HarmonyMutation harmonyMutation = new HarmonyMutation();
	
	@Override
	public abstract Object execute(Object arg0) throws JMException;

	public void setAllowedMelodyMutationIndexes(int[] allowedIndexes) {
		harmonicMelodyMutation.setAllowedMutationIndexes(allowedIndexes);
	}
	
	public void setAllowedHarmonies(List<Harmony> harmonies) {
		harmonyMutation.randomHarmony(harmonies);
	}
	
	public void setOuterBoundaryIncluded(boolean isOuterBoundaryIncluded) {
		this.harmonyMutation.setOuterBoundaryIncluded(isOuterBoundaryIncluded);
	}
}
