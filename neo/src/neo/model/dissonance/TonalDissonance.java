package neo.model.dissonance;

import neo.model.harmony.Chord;

import org.springframework.stereotype.Component;

@Component(value="TonalDissonance")
public class TonalDissonance implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getChordType()) {
			case MAJOR:
				return 0.9;
			case MINOR:
				return 0.9;
			case HALFDIM:
				return 0.8;
			case AUGM:
				return 0.5;
			case DOM:
				return 0.9;
			case CH0:
				return 0.0;
			case CH1:
				return 0.0;
			case CH2:
				return 0.0;
			case CH3:
				return 0.0;
			case CH4:
				return 0.0;
			case CH5:
				return 0.0;
			case MAJOR7:
				return 1.0;	
			case MINOR7:
				return 1.0;
			case DOM7:
				return 1.0;
			case HALFDIM7:
				return 1.0;
			case DIM:
				return 1.0;	
		}
		return 0.0;
	}

}
