package neo.out.instrument;

public class KontaktLibSoprano extends Instrument {

	public KontaktLibSoprano(int voice, int channel) {
		super(voice, channel);
		setLowest(60);
		setHighest(79);
	}
}
