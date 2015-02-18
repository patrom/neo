package neo.out.instrument;

import neo.midi.GeneralMidi;

public class KontaktLibPiano extends Instrument {

	public KontaktLibPiano(int voice, int channel) {
		super(voice, channel);
		setLowest(40);
		setHighest(108);
		setGeneralMidi(GeneralMidi.PIANO);
	}
}
