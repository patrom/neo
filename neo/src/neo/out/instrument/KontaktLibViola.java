package neo.out.instrument;

import neo.midi.GeneralMidi;

public class KontaktLibViola extends Instrument {

	public KontaktLibViola(int voice, int channel) {
		super(voice, channel);
		setLowest(48);
		setHighest(72);
		setKeySwitch(true);
		setGeneralMidi(GeneralMidi.VIOLA);
	}

	@Override
	public int getPerformanceValue(Articulation performance) {
		switch (performance) {
		case LEGATO:
			return 24;
		case MARCATO:
			return 24;
		case PIZZICATO:
			return 29;
		case STACCATO:
			return 27;
		case PORTATO:
			return 24;
		default:
			return super.getPerformanceValue(performance);
		}
	}
}
