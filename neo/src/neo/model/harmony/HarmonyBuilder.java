package neo.model.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;

public class HarmonyBuilder {
	
	protected int position;
	protected List<HarmonicMelody> melodyNotes = new ArrayList<>();
	private int length;
	private double positionWeight;
	private List<HarmonicMelody> harmonicMelodies = new ArrayList<>();

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
			Note note = new Note(pitchClass[i] , i , position, length);
			HarmonicMelody harmonicMelody = new HarmonicMelody(note, i, position);
			melodyNotes.add(harmonicMelody);	
		}
		return this;
	}
	
	public HarmonyBuilder notes(List<Note> notes){
		for (int i = 0; i < notes.size(); i++) {
			HarmonicMelody harmonicMelody = new HarmonicMelody(notes.get(i) ,i, position);
			melodyNotes.add(harmonicMelody);
		}
		return this;
	}
	
	public HarmonyBuilder positionWeight(double positionWeight){
		this.positionWeight = positionWeight;
		return this;
	}
	
	public HarmonyBuilder melodyBuilder(HarmonicMelody harmonicMelody){
		this.harmonicMelodies.add(harmonicMelody);
		return this;
	}

	public Harmony build(){
		Integer[] range = {5, 6};
		Harmony harmony = new Harmony(position, length, melodyNotes, range);
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
