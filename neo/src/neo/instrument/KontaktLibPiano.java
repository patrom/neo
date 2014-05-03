package neo.instrument;

public class KontaktLibPiano extends Instrument {

	public KontaktLibPiano(int voice, int channel) {
		super(voice, channel);
		setLowest(12);
		setHighest(108);
	}
}
