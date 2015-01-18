package neo.out.instrument;

public class KontaktLibBass extends Instrument {

	public KontaktLibBass(int voice, int channel) {
		super(voice, channel);
		setLowest(37);
		setHighest(58);
	}
}
