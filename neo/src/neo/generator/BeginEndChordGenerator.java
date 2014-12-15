package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;

public class BeginEndChordGenerator  extends Generator{

	private int[] beginPitchClasses;
	private int[] endPitchClasses;
	
	public BeginEndChordGenerator(int[][] positions, MusicProperties musicProperties) {
		super(positions, musicProperties);
	}

	@Override
	public void generateHarmonyBuilders() {
		for (int i = 0; i < positions.length - 1; i++) {
			HarmonyBuilder harmonyBuilder = harmony().pos(positions[i][0]).len(positions[i + 1][0] - positions[i][0]);
			if (i == 0) {
				harmonyBuilder.pitchClasses(beginPitchClasses);
			}
			if(i == positions.length - 2){
				harmonyBuilder.pitchClasses(endPitchClasses);
			}
			harmonyBuilders.add(harmonyBuilder);
		}
	}
	
	public void setBeginPitchClasses(int[] beginPitchClasses) {
		this.beginPitchClasses = beginPitchClasses;
	}
	
	public void setEndPitchClasses(int[] endPitchClasses) {
		this.endPitchClasses = endPitchClasses;
	}

	@Override
	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder :harmonyBuilders) {
			List<Integer> chordPitchClasses;
			if (harmonyBuilder.containsNotes()) {
				chordPitchClasses = harmonyBuilder.getPitchClasses();
			} else {
				chordPitchClasses = generatePitchClasses();
			}
			List<Note> harmonyNotes = generateNotes(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), chordPitchClasses);
			List<HarmonicMelody> harmonicMelodies = getHarmonicMelodies(harmonyNotes);
			Harmony harmony = new Harmony(harmonyBuilder.getPosition(), harmonyBuilder.getLength(), harmonicMelodies);
			double totalWeight = calculatePositionWeight(harmonyBuilder.getPosition(), harmonyBuilder.getLength());
			harmony.setPositionWeight(totalWeight);
			harmony.setPitchSpace(new UniformPitchSpace(musicProperties.getOctaveHighestPitchClassRange()));
			harmonies.add(harmony);		
		}
		return harmonies;
	}

}
