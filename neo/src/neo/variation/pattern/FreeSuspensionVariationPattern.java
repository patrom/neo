package neo.variation.pattern;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component(value="FreeSuspensionVariationPattern")
public class FreeSuspensionVariationPattern extends VariationPattern {

	public FreeSuspensionVariationPattern() {
		setPatterns(new double[][]{{0.25, 0.75},{0.5, 0.5},{0.75, 0.25}});//,{2.0/3.0, 1.0/3.0}
		List<Integer> allowedLengths = new ArrayList<>();
		allowedLengths.add(12);
		allowedLengths.add(24);
		setNoteLengths(allowedLengths);
	}
	
}