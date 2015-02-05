package neo.midi;

import static java.util.stream.Collectors.groupingBy;
import static neo.midi.HarmonyCollector.toHarmonyCollector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import neo.model.harmony.Harmony;
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
	
	public List<HarmonyPosition> getHarmonyPositions(int chordSize){
		List<Note> harmonyNotes = new ArrayList<>();
		for (int i = 0; i < chordSize; i++) {
			harmonyNotes.addAll(melodies.get(i).getNotes());
		}
		return harmonyNotes.stream()
		 .filter(note -> note.getVoice() != 0)
		 .collect(Collectors.collectingAndThen(
				 	groupingBy(note -> note.getPosition(), HarmonyCollector.toHarmonyCollector()),
				 			(value) -> { 
				 				return value.values().stream()
				 						.flatMap(h -> h.stream())
				 						.sorted()
				 						.collect(Collectors.toList());}
				 	));
	}
	
	public List<HarmonyPosition> getHarmonyPositionsForVoice(int voice){
		List<Note> harmonyNotes = new ArrayList<>();
		harmonyNotes.addAll(melodies.get(voice).getNotes());
		return harmonyNotes.stream()
		 .filter(note -> note.getVoice() != 0)
		 .collect(Collectors.collectingAndThen(
				 	groupingBy(note -> note.getPosition(), HarmonyCollector.toHarmonyCollector()),
				 			(value) -> { 
				 				return value.values().stream()
				 						.flatMap(h -> h.stream())
				 						.sorted()
				 						.collect(Collectors.toList());}
				 	));
	}
	
}
