package neo.instrument;

public class KontaktPiano extends Instrument {

	public KontaktPiano(int voice, int channel) {
		super(voice, channel);
		setLowest(12);
		setHighest(108);
	}
}
