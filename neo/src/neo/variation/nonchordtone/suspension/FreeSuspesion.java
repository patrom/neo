package neo.variation.nonchordtone.suspension;

import neo.model.note.Scale;
import neo.variation.nonchordtone.Variation;

public abstract class FreeSuspesion extends Variation {
	
	public FreeSuspesion() {
		scales.add(new Scale(Scale.MIXOLYDIAN_SCALE));
		scales.add(new Scale(Scale.LYDIAN_SCALE));
		profile = 70;
		excludedVoices.add(0);
		excludedVoices.add(1);
	}

}
