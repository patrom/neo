package neo.generator;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;

public class RandomNotesGenerator extends Generator{

	public RandomNotesGenerator(int[][] positions,
			MusicProperties musicProperties) {
		super(positions, musicProperties);
	}

//	@Override
//	public List<Harmony> generateHarmonies(){
//		List<Harmony> harmonies = new ArrayList<>();
//		for (HarmonyBuilder harmonyBuilder : harmonyBuilders) {
//			List<Integer> chordPitchClasses = generatePitchClasses();
//			List<Note> harmonyNotes = generateNotes(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), chordPitchClasses);
//			List<HarmonicMelody> harmonicMelodies = getHarmonicMelodies(harmonyNotes);
//			Harmony harmony = new Harmony(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), harmonicMelodies);
//			double totalWeight = calculatePositionWeight(harmonyBuilder.getPosition(), harmonyBuilder.getLength());
//			harmony.setPositionWeight(totalWeight);
//			harmony.setPitchSpace(new UniformPitchSpace(musicProperties.getOctaveLowestPitchClassRange(), musicProperties.getInstruments()));
//			harmonies.add(harmony);		
//		}
//		return harmonies;
//	}
	
	@Override
	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder : harmonyBuilders) {
			List<HarmonicMelody> harmonicMelodies = new ArrayList<>(); 
			for (int voice = 0; voice < musicProperties.getChordSize(); voice++) {
				int pitchClass = musicProperties.getScale().pickRandomPitchClass();
				HarmonicMelody harmonicMelody = getHarmonicMelody(harmonyBuilder.getPosition(), voice, harmonyBuilder.getLength(), pitchClass);
				harmonicMelody.updateHarmonyAndMelodyNotes(pitchClass, n -> n.setPitchClass(musicProperties.getScale().pickRandomPitchClass()));
				harmonicMelodies.add(harmonicMelody);
			}
			Harmony harmony = new Harmony(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), harmonicMelodies);
			double totalWeight = calculatePositionWeight(harmonyBuilder.getPosition(), harmonyBuilder.getLength());
			harmony.setPositionWeight(totalWeight);
			harmony.setPitchSpace(new UniformPitchSpace(musicProperties.getOctaveLowestPitchClassRange(), musicProperties.getInstruments()));
			harmonies.add(harmony);	
		}
		return harmonies;
	}
	
}
