package neo.model.dissonance;

import org.springframework.stereotype.Component;

import neo.model.harmony.Chord;
@Component(value="SmoothVoiceLeading")
public class SmoothVoiceLeading implements Dissonance {

	@Override
	public double getDissonance(Chord chord) {
		switch (chord.getForteName()) {
			case "3-6":
				return 1.0;
			case "3-1":
				return 1.0;
		}
		return 0;
	}


}
