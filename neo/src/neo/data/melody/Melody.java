package neo.data.melody;

import static java.util.stream.Collectors.toList;

import java.util.List;

import neo.data.note.Note;


public class Melody{
	
	private int voice;
	private List<HarmonicMelody> harmonicMelodies;

	public Melody(List<HarmonicMelody> harmonicMelodies, int voice) {
		this.harmonicMelodies = harmonicMelodies;
		this.voice = voice;
	}
	
	public List<HarmonicMelody> getHarmonicMelodies() {
		return harmonicMelodies;
	}
	
	public int getVoice() {
		return voice;
	}
	
	public List<Note> getNotes(){
		return harmonicMelodies.stream()
					.flatMap(h -> h.getMelodyNotes().stream())
					.sorted()
					.collect(toList());
	}

}
