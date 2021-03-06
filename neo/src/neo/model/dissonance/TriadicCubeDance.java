package neo.model.dissonance;

import org.springframework.stereotype.Component;

import neo.model.harmony.Chord;

@Component(value="TriadicCubeDance")
public class TriadicCubeDance implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getChordType()) {
			case MAJOR:
				return 1.0;
			case MINOR:
				return 1.0;
			case AUGM:
				return 1.0;
		}
		return 0.0;
	}

}
