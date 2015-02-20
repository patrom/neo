package neo.out.instrument;

import neo.midi.GeneralMidi;


public class KontaktLibCello extends Instrument {

	public KontaktLibCello(int voice, int channel) {
		super(voice, channel);
		setLowest(36);
		setHighest(70);
		setKeySwitch(true);
		setGeneralMidi(GeneralMidi.CELLO);
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
