package neo.data.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;
import neo.objective.harmony.Chord;

public class HarmonyBuilder {
	
	protected int position;
	protected List<NotePos> notes = new ArrayList<>();
	private PitchSpaceStrategy pitchSpaceStrategy;
	private Chord chord;
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
			NotePos notePos = new NotePos(pitchClass[i] , i , position, length);
			notes.add(notePos);
		}
		return this;
	}
	
	public HarmonyBuilder weight(double positionWeight){
		this.positionWeight = positionWeight;
		return this;
	}
	
	public Harmony build(){
		Harmony harmony = new Harmony(position, length, notes, new UniformPitchSpace(notes, 6));
		harmony.setPositionWeight(positionWeight);
		return harmony;
	}
	
	public Harmony build(PitchSpaceStrategy pitchSpaceStrategy){
		Harmony harmony = new Harmony(position,length, notes, pitchSpaceStrategy);
		harmony.setPositionWeight(positionWeight);
		return harmony;
	}
	
}
