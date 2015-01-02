package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;
import neo.model.perle.AxisDyadArray;
import neo.model.perle.CyclicSet;
import neo.model.perle.IntervalCycle;
import neo.util.RandomUtil;

public class PerleChordGenerator extends Generator {
	
	private AxisDyadArray axisDyadArray = new AxisDyadArray(new CyclicSet(IntervalCycle.P_IC7, 0), 0,
			new CyclicSet(IntervalCycle.P_IC7, 2), 0);

	public PerleChordGenerator(int[][] positions, MusicProperties musicProperties) {
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

	protected int[] pickRandomChords() {
		List<List<Integer>> chords = axisDyadArray.getAllAxisDyadChords();
		List<Integer> chord = RandomUtil.getRandomFromList(chords);
		int[] pitchClasses = new int[musicProperties.getChordSize()];
		for (int i = 0; i < pitchClasses.length; i++) {
			pitchClasses[i] = chord.get(i);
		}
		return pitchClasses;
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
			harmony.setPitchSpace(new UniformPitchSpace(musicProperties.getOctaveLowestPitchClassRange(), musicProperties.getInstruments()));
			harmonies.add(harmony);		
		}
		return harmonies;
	}

}
