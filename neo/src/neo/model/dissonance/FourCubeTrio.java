package neo.model.dissonance;

import org.springframework.stereotype.Component;

import neo.model.harmony.Chord;

@Component(value="FourCubeTrio")
public class FourCubeTrio implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getForteName()) {
			case "4-25"://dom7b5
				return 1.0;
			case "4-26"://m7
				return 1.0;
			case "4-27"://dom7, halfdim7
				return 1.0;
			case "4-28"://dim
				return 1.0;
			
			case "3-11":
				return 0.99;
			case "3-12":
				return 0.99;
		}
		return 0;
	}
}
