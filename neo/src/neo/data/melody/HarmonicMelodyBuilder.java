package neo.data.melody;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.Harmony.PitchSpaceStrategy;
import neo.data.note.Note;

public class HarmonicMelodyBuilder {

	private int position;
	private List<Note> notes = new ArrayList<>();
	private PitchSpaceStrategy pitchSpaceStrategy;
	private int voice;
	
	public static HarmonicMelodyBuilder harmonicMelody(){
		return new HarmonicMelodyBuilder();
	}
	
	public HarmonicMelodyBuilder pos(int position){
		this.position = position;
		return this;
	}
	
	public HarmonicMelodyBuilder notes(List<Note> notes){
		this.notes.addAll(notes);
		return this;
	}
	
	public HarmonicMelodyBuilder notes(Note ... note){
		for (int i = 0; i < note.length; i++) {
			notes.add(note[i]);
		}
		return this;
	}
	
	public HarmonicMelodyBuilder voice(int voice){
		this.voice = voice;
		return this;
	}
	
	public HarmonicMelody build(){
		HarmonicMelody harmonicMelody = new HarmonicMelody(notes, voice);
		return harmonicMelody;
	}
	
}
