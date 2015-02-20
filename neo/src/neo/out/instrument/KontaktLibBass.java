package neo.out.instrument;

import neo.midi.GeneralMidi;

public class KontaktLibBass extends Instrument {

	public KontaktLibBass(int voice, int channel) {
		super(voice, channel);
		setLowest(37);
		setHighest(58);
		setGeneralMidi(GeneralMidi.CHOIR);
	}
}
