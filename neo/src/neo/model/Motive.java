package neo.model;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import neo.generator.MusicProperties;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.objective.meter.InnerMetricWeight;

public class Motive {

	private List<Harmony> harmonies;
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
	
	public void setMusicProperties(MusicProperties musicProperties) {
		this.musicProperties = musicProperties;
	}

	public MusicProperties getMusicProperties() {
		return musicProperties;
	}
	
	private List<Melody> extractMelodies(){
		List<Melody> melodies = new ArrayList<>();
		harmonies.stream().forEach(harmony -> harmony.translateToPitchSpace());
		for (int i = 0; i < musicProperties.getChordSize(); i++) {
			Melody melody = new Melody(getMelodyForVoice(i), i);
			melodies.add(melody);
		}
		return melodies;
	}
	
	public void updateInnerMetricWeightMelodies() {
		List<Melody> melodies = extractMelodies();
		for (Melody melody : melodies) {
			List<Note> notes = melody.getMelodieNotes();
			updateInnerMetricWeightNotes(notes);
		}
	}

	protected void updateInnerMetricWeightNotes(List<Note> notes) {
		Map<Integer, Double> normalizedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(notes, musicProperties.getMinimumLength());
		for (Note note : notes) {
			Integer key = note.getPosition()/musicProperties.getMinimumLength();
			if (normalizedMap.containsKey(key)) {
				Double innerMetricValue = normalizedMap.get(key);
				note.setInnerMetricWeight(innerMetricValue);
			}
		}
	}
	
	public void updateInnerMetricWeightHarmonies() {
		int[] harmonicRhythm = extractHarmonicRhythm();
		Map<Integer, Double> normalizedMap = InnerMetricWeight.getNormalizedInnerMetricWeight(harmonicRhythm, musicProperties.getMinimumLength());
		for (Harmony harmony : harmonies) {
			Integer key = harmony.getPosition()/musicProperties.getMinimumLength();
			if (normalizedMap.containsKey(key)) {
				Double innerMetricValue = normalizedMap.get(key);
				harmony.setInnerMetricWeight(innerMetricValue);
			}
		}
	}

	private int[] extractHarmonicRhythm() {
		int[] rhythm = new int[harmonies.size()];
		for (int i = 0; i < rhythm.length; i++) {
			Harmony harmony = harmonies.get(i);
			rhythm[i] = harmony.getPosition();
		}
		return rhythm;
	}
	
}
