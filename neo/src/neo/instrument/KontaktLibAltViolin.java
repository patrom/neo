package neo.instrument;

public class KontaktLibAltViolin extends Instrument {

	public KontaktLibAltViolin(int voice, int channel) {
		super(voice, channel);
		setLowest(48);
		setHighest(72);
		setKeySwitch(true);
	}

	@Override
	public int getPerformanceValue(Performance performance) {
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