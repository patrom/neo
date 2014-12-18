package neo.generator;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;
import neo.util.RandomUtil;

public class TonalChordGenerator extends Generator{

	private List<int[]> chords;
	
	public TonalChordGenerator(int[][] positions, MusicProperties musicProperties) {
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
		int[] chord = RandomUtil.getRandomFromList(chords);
		int size = musicProperties.getChordSize();
		if (chord.length != size) {
			int[] newChord = new int[size];
			for (int i = 0; i < newChord.length - 1; i++) {
				newChord[i] = chord[i];
			}
			for (int i = newChord.length - 1; i < size; i++) {
				int randomIndex = RandomUtil.random(size - 1);
				newChord[i] = chord[randomIndex];
			}
			return newChord;
		}
		return chord;
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

	public void setChords(List<int[]> chords) {
		this.chords = chords;
	}

}
