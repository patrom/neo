package neo.midi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import neo.model.note.Note;

public class MidiInfo {

	private List<MelodyInstrument> melodies;
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
	public List<MelodyInstrument> getMelodies() {
		return melodies;
	}
	public void setMelodies(List<MelodyInstrument> melodies) {
		this.melodies = melodies;
	}
	
	public Map<Integer, List<Note>> getHarmonyPositions(int chordSize){
		List<Note> harmonyNotes = new ArrayList<>();
		for (int i = 0; i < chordSize; i++) {
			harmonyNotes.addAll(melodies.get(i).getNotes());
		}
		return harmonyNotes.stream().collect(Collectors.groupingBy(note -> note.getPosition()));
	}
	
}
