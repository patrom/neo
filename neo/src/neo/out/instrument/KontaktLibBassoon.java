package neo.out.instrument;


public class KontaktLibBassoon extends Instrument {

	public KontaktLibBassoon(int voice, int channel) {
		super(voice, channel);
		setLowest(34);
		setHighest(70);
		setKeySwitch(true);
	}

	@Override
	public int getPerformanceValue(Articulation performance) {
		switch (performance) {
		case LEGATO:
			return 24;
		case MARCATO:
			return 24;
		case PIZZICATO:
			return 29;
		case STACCATO:
			return 27;
		case PORTATO:
			return 24;
		default:
			return super.getPerformanceValue(performance);
		}
	}
}
