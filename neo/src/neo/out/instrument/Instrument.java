package neo.out.instrument;

import neo.midi.GeneralMidi;

public class Instrument {

	protected int voice;
	protected int lowest;
	protected int highest;
	protected int channel;
	protected boolean keySwitch;
	protected GeneralMidi generalMidi;
	
	public Instrument(int voice, int channel) {
		this.voice = voice;
		this.channel = channel;
	}
	public Instrument() {
	}
	
	public int getPerformanceValue(Articulation performance) {
		if (isKeySwitch()) {
			return 24;
		} else {
			return 0;
		}
	}
	
	public int getVoice() {
		return voice;
	}
	public void setVoice(int voice) {
		this.voice = voice;
	}
	public int getLowest() {
		return lowest;
	}
	public void setLowest(int lowest) {
		this.lowest = lowest;
	}
	public int getHighest() {
		return highest;
	}
	public void setHighest(int highest) {
		this.highest = highest;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public boolean isKeySwitch() {
		return keySwitch;
	}
	public void setKeySwitch(boolean keySwitch) {
		this.keySwitch = keySwitch;
	}
	public GeneralMidi getGeneralMidi() {
		return generalMidi;
	}
	public void setGeneralMidi(GeneralMidi generalMidi) {
		this.generalMidi = generalMidi;
	}
	
}
