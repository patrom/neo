package neo.data.melody;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import neo.data.note.Note;


public class Melody{
	
	private int voice;
	private List<HarmonicMelody> harmonicMelodies;
	private Comparator<Note> byPosition = (e1, e2) -> Integer.compare(
            e1.getPosition(), e2.getPosition());

	public Melody(List<HarmonicMelody> harmonicMelodies, int voice) {
		this.harmonicMelodies = harmonicMelodies;
		this.voice = voice;
	}
	
	public Melody(HarmonicMelody harmonicMelody, int voice) {
		harmonicMelodies = new ArrayList<>();
		this.harmonicMelodies.add(harmonicMelody);
		this.voice = voice;
	}

	public List<Note> getNotes() {
		return harmonicMelodies.stream().flatMap(h -> h.getNotes().stream()).sorted().collect(toList());
	}
	
	public int getVoice() {
		return voice;
	}

	public List<HarmonicMelody> getHarmonicMelodies() {
		return harmonicMelodies;
	}
	
	public void updateMelodies(){
		harmonicMelodies.stream().forEach(harmonicMelody -> harmonicMelody.updateNotes());
	}

}
