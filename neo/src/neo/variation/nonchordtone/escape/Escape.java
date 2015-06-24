package neo.variation.nonchordtone.escape;

import neo.variation.nonchordtone.Variation;

public abstract class Escape extends Variation {

	public Escape() {
		excludedVoices.add(0);
		excludedVoices.add(1);
	}

}
