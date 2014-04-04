package neo.generator;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import neo.data.note.NotePos;

public class Generator {

	public static void main(String[] args) {
		int chordSize = 3;
		int beat = 6;
		List<Integer> rhythmTemplate = Arrays.asList(0,1,2,3);
		Map<Integer, List<NotePos>> chords = generateChords(rhythmTemplate, chordSize, beat);
		chords.forEach((k, n) -> System.out.println(k + ": " + n));
		
	}

	private static Map<Integer, List<NotePos>> generateChords(List<Integer> rhythmTemplate ,int chordSize, int beat) {
		Random random = new Random();
		Map<Integer, List<NotePos>> chords = new TreeMap<>();
		for (Integer position : rhythmTemplate) {
			int voice = 0;
			List<Integer> chordPitchClasses = random.ints(0, 12).limit(chordSize).boxed().collect(toList());
			List<NotePos> notePositions = new ArrayList<>();
			for (Integer pc : chordPitchClasses) {
				NotePos notePos = new NotePos(pc, voice , position * beat);
				notePositions.add(notePos);
				voice++;
			}
			chords.put(position, notePositions);
		}
		return chords;
	}
}
