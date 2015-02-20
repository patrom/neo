package neo.generator;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.perle.AxisDyadArray;
import neo.model.perle.CyclicSet;
import neo.model.perle.IntervalCycle;
import neo.util.RandomUtil;

public class PerleChordGenerator extends Generator {
	
	private AxisDyadArray axisDyadArray = new AxisDyadArray(new CyclicSet(IntervalCycle.P_IC7, 0), 0,
			new CyclicSet(IntervalCycle.P_IC7, 2), 0);

	public PerleChordGenerator(int[] positions, MusicProperties musicProperties) {
		super(positions, musicProperties);
	}

	protected int[] pickRandomChord() {
		List<List<Integer>> chords = axisDyadArray.getAllAxisDyadChords();
		List<Integer> chord = RandomUtil.getRandomFromList(chords);
		int[] pitchClasses = new int[musicProperties.getChordSize()];
		for (int i = 0; i < pitchClasses.length; i++) {
			pitchClasses[i] = chord.get(i);
		}
		return pitchClasses;
	}
	
	@Override
	public List<Harmony> generateHarmonies(){
		List<Harmony> harmonies = new ArrayList<>();
		for (HarmonyBuilder harmonyBuilder : harmonyBuilders) {
			int[] chord = pickRandomChord();
			List<HarmonicMelody> harmonicMelodies = new ArrayList<>(); 
			for (int voice = 0; voice < musicProperties.getChordSize(); voice++) {
				HarmonicMelody harmonicMelody = getHarmonicMelody(
						harmonyBuilder.getPosition(), voice, harmonyBuilder.getLength(), chord[voice]);
				harmonicMelody.updateHarmonyAndMelodyNotes(chord[voice], n -> n.setPitchClass(RandomUtil.getRandomFromIntArray(chord)));
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
