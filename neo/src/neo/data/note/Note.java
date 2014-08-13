package neo.data.note;

import neo.data.harmony.Harmony;
import neo.instrument.Performance;


public class Note implements Comparable<Note>, Cloneable{

	/** The pitch value which indicates a rest. */
	public static final int REST = Integer.MIN_VALUE;
	   /** default dynamic*/
    public static final int DEFAULT_DYNAMIC = 85;

	private int pitch;
	private int dynamic = DEFAULT_DYNAMIC;
	private double rhythmValue;
	private double duration;

	protected int length;
	protected int position;

	private double positionWeight;
	private double innerMetricWeight;

	private int octave;
	private int pitchClass;
	private int voice;
	private Harmony harmony;
	
	private Performance performance = Performance.LEGATO;

	public double getBeat(int divider) {
		return Math.floor(position / divider);
	}

	public Note() {
	}

	public Note(int pitchClass, int voice, int position, int length) {
		this.pitchClass = pitchClass;
		this.voice = voice;
		this.position = position;
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public int getDynamic() {
		return dynamic;
	}

	public void setDynamic(int dynamic) {
		this.dynamic = dynamic;
	}

	public double getRhythmValue() {
		return rhythmValue;
	}

	public void setRhythmValue(double rhythmValue) {
		this.rhythmValue = rhythmValue;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public boolean isRest() {
		if (this.getPitch() == REST)
			return true;
		else
			return false;
	}

	public boolean samePitch(Note note) {
		return this.getPitch() == note.getPitch();
	}

	public double getWeightedSum() {
		return positionWeight;
	}

	public int getOctave() {
		return octave;
	}

	public void setOctave(int octave) {
		this.octave = octave;
	}

	public int getPitchClass() {
		return pitchClass;
	}

	public void setPitchClass(int pitchClass) {
		this.pitchClass = pitchClass;
	}

	public int getVoice() {
		return voice;
	}

	public void setVoice(int voice) {
		this.voice = voice;
	}

	@Override
	public String toString() {
		return "np[p=" + ((pitch == Integer.MIN_VALUE) ? "Rest":pitch) + ", pc=" + pitchClass
		+ ", v=" + voice + ", pos=" + position +  ", l=" + length + ", pos w="
		+ positionWeight + ", i w=" + innerMetricWeight + "]";
	}

	public double getPositionWeight() {
		return positionWeight;
	}

	public void setPositionWeight(double positionWeight) {
		this.positionWeight = positionWeight;
	}

	public double getInnerMetricWeight() {
		return innerMetricWeight;
	}

	public void setInnerMetricWeight(double innerMetricWeight) {
		this.innerMetricWeight = innerMetricWeight;
	}

	public int compareTo(Note note) {
		if (getPosition() < note.getPosition()) {
			return -1;
		} if (getPosition() > note.getPosition()) {
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pitchClass;
		result = prime * result + position;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (pitchClass != other.pitchClass)
			return false;
		if (position != other.position)
			return false;
		return true;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Performance getPerformance() {
		return performance;
	}
	
	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public Harmony getHarmony() {
		return harmony;
	}

	public void setHarmony(Harmony harmony) {
		this.harmony = harmony;
	}

}
