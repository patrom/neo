package neo.data;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.harmony.Harmony;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.evaluation.MusicProperties;

public class Motive {

	private List<Harmony> harmonies;
	private List<Melody> melodies = new ArrayList<>();
	private MusicProperties musicProperties;
		
	public Motive(List<Harmony> harmonies) {
		this.harmonies = harmonies;
	}

	public List<Harmony> getHarmonies() {
		return harmonies;
	}
	
	public List<Melody> getMelodies() {
		return extractMelodies();
	}
	
	protected List<HarmonicMelody> getMelodyForVoice(int voice){
		return harmonies.stream()
				.flatMap(harmony -> harmony.getHarmonicMelodies().stream())
				.filter(harmonicMelody -> harmonicMelody.getVoice() == voice)
				.collect(toList());
	}
	
	public void mutateHarmonyNoteToPreviousPitchFromScale(){
		Harmony harmony = randomHarmony();
		harmony.mutateNoteToPreviousPitchFromScale(musicProperties.getScale());
		harmony.mutateNoteRandom(musicProperties.getMelodyScale());
	}
	
	public void mutateHarmonyPitchSpace(){
		Harmony harmony = randomHarmony();
		harmony.mutatePitchSpace();
	}
	
	public void mutateNoteToRandom(){
		Harmony harmony = randomHarmony();
		harmony.mutateNoteRandom(musicProperties.getMelodyScale());
	}
	
	private Harmony randomHarmony() {
		int harmonyIndex = PseudoRandom.randInt(0, harmonies.size() - 1);
		Harmony harmony = harmonies.get(harmonyIndex);
		return harmony;
	}

	public void setMusicProperties(MusicProperties musicProperties) {
		this.musicProperties = musicProperties;
	}

	public MusicProperties getMusicProperties() {
		return musicProperties;
	}
	
	private List<Melody> extractMelodies(){
		melodies.clear();
		harmonies.stream().forEach(harmony -> harmony.translateToPitchSpace());
		for (int i = 0; i < musicProperties.getChordSize(); i++) {
			Melody melody = new Melody(getMelodyForVoice(i), i);
			melodies.add(melody);
		}
		return melodies;
	}
	
}
