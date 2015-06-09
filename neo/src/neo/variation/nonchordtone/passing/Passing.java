package neo.variation.nonchordtone.passing;

import neo.model.note.Scale;
import neo.variation.nonchordtone.Variation;

public abstract class Passing extends Variation {
	
	public Passing() {
		scales.add(new Scale(Scale.OCTATCONIC_HALF));
//		scales.add(new Scale(Scale.LYDIAN_SCALE));
	}
	
}
