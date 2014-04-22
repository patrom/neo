package neo.evaluation;

public class FitnessObjectives {

	private double harmony;
	private double melody;
	private double voiceleading;
	
	public double getHarmony() {
		return harmony;
	}
	public void setHarmony(double harmony) {
		this.harmony = harmony;
	}
	public double getMelody() {
		return melody;
	}
	public void setMelody(double melody) {
		this.melody = melody;
	}
	public double getVoiceleading() {
		return voiceleading;
	}
	public void setVoiceleading(double voiceleading) {
		this.voiceleading = voiceleading;
	}
	@Override
	public String toString() {
		return "FitnessValueObject [harmony=" + harmony + ", melody=" + melody
				+ ", voiceleading=" + voiceleading + "]";
	}
	
}
