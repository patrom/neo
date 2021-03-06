package neo.out.instrument;

import neo.midi.GeneralMidi;

public class Instrument {

	protected int voice;
	protected int lowest;
	protected int highest;
	protected int channel;
	protected boolean keySwitch;
	protected GeneralMidi generalMidi;
	//musicXML properties
	protected String instrumentName;
	protected String instrumentSound;
	protected String virtualLibrary = "Sibelius 7 Sounds";
	protected String virtualName;
	protected String clef = "G";
	
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
	public String getInstrumentName() {
		return instrumentName;
	}
	public void setInstrumentName(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	public String getInstrumentSound() {
		return instrumentSound;
	}
	public void setInstrumentSound(String instrumentSound) {
		this.instrumentSound = instrumentSound;
	}
	public String getVirtualLibrary() {
		return virtualLibrary;
	}
	public void setVirtualLibrary(String virtualLibrary) {
		this.virtualLibrary = virtualLibrary;
	}
	public String getVirtualName() {
		return virtualName;
	}
	public void setVirtualName(String virtualName) {
		this.virtualName = virtualName;
	}
	public String getClef() {
		return clef;
	}
	public void setClef(String clef) {
		this.clef = clef;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((instrumentName == null) ? 0 : instrumentName.hashCode());
		result = prime * result
				+ ((instrumentSound == null) ? 0 : instrumentSound.hashCode());
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
		Instrument other = (Instrument) obj;
		if (instrumentName == null) {
			if (other.instrumentName != null)
				return false;
		} else if (!instrumentName.equals(other.instrumentName))
			return false;
		if (instrumentSound == null) {
			if (other.instrumentSound != null)
				return false;
		} else if (!instrumentSound.equals(other.instrumentSound))
			return false;
		return true;
	}
	
}
