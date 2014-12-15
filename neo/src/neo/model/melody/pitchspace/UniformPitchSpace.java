package neo.model.melody.pitchspace;

import java.util.List;

import neo.out.instrument.Instrument;


public class UniformPitchSpace extends PitchSpace {

	public UniformPitchSpace(Integer[] octaveHighestPitchClassRange,
			List<Instrument> instruments) {
		super(octaveHighestPitchClassRange, instruments);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
	}

}
