package neo.out.instrument;

public class KontaktLibTenor extends Instrument {

	public KontaktLibTenor(int voice, int channel) {
		super(voice, channel);
		setLowest(49);
		setHighest(68);
	}

}
