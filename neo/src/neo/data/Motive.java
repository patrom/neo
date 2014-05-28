package neo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.NotePos;

public class Motive {

	private List<Harmony> harmonies;
	private List<Melody> melodies = new ArrayList<>();
		
	public Motive(List<Harmony> harmonies) {
		this.harmonies = harmonies;
	}

	public List<Harmony> getHarmonies() {
		return harmonies;
	}
	
	public List<Melody> getMelodies() {
		return extractMelodies();
	}
	
	private List<Melody> extractMelodies() {
		List<NotePos> allNotes = new ArrayList<>();
		for (Harmony list : harmonies) {
			for (NotePos notePos : list.getNotes()) {
				allNotes.add(notePos);
			}
		}
		Map<Integer, List<NotePos>> melodyMap = allNotes.stream().collect(Collectors.groupingBy(n -> n.getVoice()));
		for (Entry<Integer, List<NotePos>> entry: melodyMap.entrySet()) {
			Melody motive = new Melody(entry.getValue(), 0);
			melodies.add(motive);
		}
		return melodies;
	}
	
}
