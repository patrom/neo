package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;

public class TonalChordGenerator extends Generator{

	public TonalChordGenerator(int[][] positions,
			MusicProperties musicProperties) {
		super(positions, musicProperties);
	}

	@Override
	public void generateHarmonyBuilders() {
		for (int i = 0; i < positions.length - 1; i++) {
			HarmonyBuilder harmonyBuilder = harmony().pos(positions[i][0]).len(positions[i + 1][0] - positions[i][0]);
			harmonyBuilder.pitchClasses(pickRandomChords());
			harmonyBuilders.add(harmonyBuilder);
		}
	}

	private int[] pickRandomChords() {
		return new int[]{0, 0, 4 ,7};
	}

	@Override
	public List<Harmony> generateHarmonies() {
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder :harmonyBuilders) {
			List<Integer> chordPitchClasses = harmonyBuilder.getPitchClasses();
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
