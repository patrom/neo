package neo.data.note;

public class NoteBuilder {
	
	private int pitchClass;
	private int position;
	private int length;
	private double positionWeight;

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
		return note;
	}

	public int getPosition() {
		return position;
	}

	public int getLength() {
		return length;
	}

}
