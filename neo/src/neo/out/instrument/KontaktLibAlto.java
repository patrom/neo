package neo.out.instrument;

import neo.midi.GeneralMidi;

public class KontaktLibAlto extends Instrument {

	public KontaktLibAlto(int voice, int channel) {
		super(voice, channel);
		setLowest(50);
		setHighest(73);
		setGeneralMidi(GeneralMidi.CHOIR);
	}
}
