package neo.data.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.Note;

public class HarmonyBuilder {
	
	protected int position;
	protected List<Note> notes = new ArrayList<>();
	private PitchSpaceStrategy pitchSpaceStrategy;
	private int length;
	private double positionWeight;

	public static HarmonyBuilder harmony(){
		return new HarmonyBuilder();
	}
	
	public HarmonyBuilder pos(int position){
		this.position = position;
		return this;
	}
	
	public HarmonyBuilder len(int length){
		this.length = length;
		return this;
	}
	
	public HarmonyBuilder notes(int ... pitchClass){
		for (int i = 0; i < pitchClass.length; i++) {
			Note notePos = new Note(pitchClass[i] , i , position, length);
			notes.add(notePos);
		}
		return this;
	}
	
	public HarmonyBuilder allNotes(List<Note> notes){
		this.notes.addAll(notes);
		return this;
	}
	
	public HarmonyBuilder positionWeight(double positionWeight){
		this.positionWeight = positionWeight;
		return this;
	}
	
	public HarmonyBuilder pitchSpace(PitchSpaceStrategy pitchSpaceStrategy){
		this.pitchSpaceStrategy = pitchSpaceStrategy;
		return this;
	}
	
	public Harmony build(){
		if (this.pitchSpaceStrategy == null) {
			Integer[] range = {6};
			this.pitchSpaceStrategy = new UniformPitchSpace(notes, range);
		}
		Harmony harmony = new Harmony(position, length, notes, pitchSpaceStrategy);
		harmony.setPositionWeight(positionWeight);
		return harmony;
	}

	public int getPosition() {
		return position;
	}

	public int getLength() {
		return length;
	}
	
}
