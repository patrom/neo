package neo.data.melody.pitchspace;


public class UniformPitchSpace extends PitchSpace {

	public UniformPitchSpace(Integer[] octaveHighestPitchClassRange) {
		super(octaveHighestPitchClassRange);
	}

	@Override
	public void translateToPitchSpace() {
		setUniformPitchSpace();
	}

}
