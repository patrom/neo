package neo.data.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.data.melody.HarmonicMelody;
import neo.data.melody.HarmonicMelodyBuilder;
import neo.data.note.Note;

public class HarmonyBuilder {
	
	protected int position;
	protected List<Note> notes = new ArrayList<>();
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
			notes.add(note);
		}
		return this;
	}
	
	public HarmonyBuilder notes(List<Note> notes){
		this.notes = notes;
		return this;
	}
	
	public HarmonyBuilder positionWeight(double positionWeight){
		this.positionWeight = positionWeight;
		return this;
	}
	
	public HarmonyBuilder melodyBuilders(HarmonicMelody harmonicMelody){
		this.harmonicMelodies.add(harmonicMelody);
		return this;
	}

	public Harmony build(){
		Harmony harmony = new Harmony(position, length, notes);
		Integer[] range = {6};
		harmony.setPitchSpaceStrategy(harmony.new UniformPitchSpace(range));
		harmony.setPositionWeight(positionWeight);
		List<HarmonicMelody> temp = new ArrayList<>();
		for (Note note : notes) {
			if (harmonicMelodyExists(note)) {
				harmonicMelodies.stream().filter(harmonicMelody -> harmonicMelody.getVoice() == note.getVoice())
					.flatMap(harmonicMelody -> harmonicMelody.getNotes().stream()).forEach(n -> n.setPitchClass(note.getPitchClass()));
			} else {
				Note newNote = new Note(note.getPitchClass(), note.getVoice(), note.getPosition(), note.getLength());
				HarmonicMelody harmonicMelody = new HarmonicMelody(newNote, note.getVoice());
				temp.add(harmonicMelody);
			}
		}
		harmonicMelodies.addAll(temp);
		harmony.setHarmonicMelodies(harmonicMelodies);
		return harmony;
	}
	
	private boolean harmonicMelodyExists(Note note){
		return harmonicMelodies.stream().anyMatch(harmonicMelody -> harmonicMelody.getVoice() == note.getVoice());
	}

	public int getPosition() {
		return position;
	}

	public int getLength() {
		return length;
	}
	
}
