package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;

public class RandomNotesGenerator extends Generator{

	public RandomNotesGenerator(int[][] positions,
			MusicProperties musicProperties) {
		super(positions, musicProperties);
	}

	@Override
	public void generateHarmonyBuilders(){
		for (int i = 0; i < positions.length - 1; i++) {
			harmonyBuilders.add(harmony().pos(positions[i][0]).len(positions[i + 1][0] - positions[i][0]));
		}
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
			for (int i = 0; i < musicProperties.getChordSize(); i++) {
				int pitchClass = musicProperties.getScale().pickRandomPitchClass();
				Note harmonyNote = new Note(pitchClass, i , harmonyBuilder.getPosition(), harmonyBuilder.getLength());
				HarmonicMelody harmonicMelody = getHarmonicMelody(harmonyBuilder.getPosition(), i, harmonyNote);
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
