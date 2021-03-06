package neo.nsga.operator.decorator;

import org.springframework.stereotype.Component;

import neo.model.Motive;
import neo.nsga.MusicVariable;
import jmetal.core.Solution;

@Component
public class FugaDecorator implements Decorator{

	@Override
	public void decorate(Solution solution) {
		Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
		System.out.println("decorated");
	}

}
