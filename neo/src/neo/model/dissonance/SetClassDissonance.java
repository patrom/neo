package neo.model.dissonance;

import neo.model.harmony.Chord;

import org.springframework.stereotype.Component;

@Component(value="SetClassDissonance")
public class SetClassDissonance implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getForteName()) {
			case "3-7":
				return 1.0;
			case "3-8":
				return 1.0;
			case "3-10":
				return 1.0;
			case "3-11":
				return 1.0;
			case "3-12":
				return 1.0;
		}
		return 0;
	}

}
