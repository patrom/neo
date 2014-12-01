package neo.out.instrument;

public class KontaktLibPiano extends Instrument {

	public KontaktLibPiano(int voice, int channel) {
		super(voice, channel);
		setLowest(24);
		setHighest(108);
	}
}
