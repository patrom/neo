package neo.out.instrument;

import neo.midi.GeneralMidi;

public class KontaktLibSoprano extends Instrument {

	public KontaktLibSoprano(int voice, int channel) {
		super(voice, channel);
		setLowest(60);
		setHighest(79);
		setGeneralMidi(GeneralMidi.CHOIR);
	}
}
