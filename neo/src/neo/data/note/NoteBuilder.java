package neo.data.note;

import neo.data.harmony.Harmony;

public class NoteBuilder {
	
	private int pitchClass;
	private int position;
	private int length;
	private double positionWeight;
	private int pitch;
	private int octave;
	private int voice;

	public static NoteBuilder note(){
		return new NoteBuilder();
	}

	public NoteBuilder pc(int pitchClass){
		this.pitchClass = pitchClass;
		return this;
	}
	
	public NoteBuilder pos(int position){
		this.position = position;
		return this;
	}
	
	public NoteBuilder len(int length){
		this.length = length;
		return this;
	}
	
	public NoteBuilder pitch(int pitch){
		this.pitch = pitch;
		return this;
	}
	
	public NoteBuilder ocatve(int octave){
		this.octave = octave;
		return this;
	}
	
	public NoteBuilder voice(int voice){
		this.voice = voice;
		return this;
	}
	
	public NoteBuilder positionWeight(Double positionWeight) {
		this.positionWeight = positionWeight;
		return this;
	}
	
	public Note build(){
		Note note = new Note();
		note.setPitchClass(pitchClass);
		note.setPosition(position);
		note.setPositionWeight(positionWeight);
		note.setLength(length);
		note.setPitch(pitch);
		note.setOctave(octave);
		note.setVoice(voice);
		return note;
	}

	public int getPosition() {
		return position;
	}

	public int getLength() {
		return length;
	}

	public int getVoice() {
		return voice;
	}

}
