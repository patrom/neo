package neo.model.dissonance;

import org.springframework.stereotype.Component;

import neo.model.harmony.Chord;

@Component(value="DoubleTriadDissonance")
public class DoubleTriadDissonance implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getForteName()) {
		case "3-11":
			return 1.0;//min - maj
		case "4-20"://maj7
			return 1.0;
		case "4-26"://m7
			return 1.0;
		case "5-27":
			return 1.0;
		case "5-34":
			return 1.0;
		case "6-32":
			return 1.0;
		case "6-33":
			return 1.0;
		case "6-Z26":
			return 1.0;
	}
	return 0;
	}

}
