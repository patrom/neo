package neo.midi;

import java.util.List;

import neo.data.melody.Melody;

public class MidiInfo {

	private List<Melody> melodies;
	private String timeSignature;
	private float tempo;
	
	public String getTimeSignature() {
		return timeSignature;
	}
	public void setTimeSignature(String timeSignature) {
		this.timeSignature = timeSignature;
	}
	public float getTempo() {
		return tempo;
	}
	public void setTempo(float tempo) {
		this.tempo = tempo;
	}
	public List<Melody> getMelodies() {
		return melodies;
	}
	public void setMelodies(List<Melody> melodies) {
		this.melodies = melodies;
	}
	
}
