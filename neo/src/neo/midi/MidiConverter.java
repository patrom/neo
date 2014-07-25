package neo.midi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.melody.Melody;
import neo.data.note.NotePos;

public class MidiConverter {
	
	private static final double DEFAULT_WEIGHT = 0.25;
	private static Map<Integer, Double> fourFourOrTwoFourRhythmWeightValues = new TreeMap<>();
	private static Map<Integer, Double> fourThreeRhythmWeightValues = new TreeMap<>();
	private static Map<Integer, Double> SixEightRhythmWeightValues = new TreeMap<>();
	
	static{
		for (int i = 0; i < 96; i = i + 6) {
			if (i % 12 == 0) {
				fourFourOrTwoFourRhythmWeightValues.put(i, 1.0);
			} else {
				fourFourOrTwoFourRhythmWeightValues.put(i, 0.5);
			}
		}
		
		for (int i = 0; i < 72; i = i + 6) {
			if (i % 18 == 0) {
				fourThreeRhythmWeightValues.put(i, 1.0);
			} else {
				fourThreeRhythmWeightValues.put(i, 0.5);
			}
		}
		
		for (int i = 0; i < 144; i = i + 6) {
			if (i % 36 == 0) {
				SixEightRhythmWeightValues.put(i, 1.0);
			} else if (i % 18 == 0) {
				SixEightRhythmWeightValues.put(i, 0.75);
			} else {
				SixEightRhythmWeightValues.put(i, 0.5);
			}
		}
	}
	
	public static void updatePositionNotes(List<Melody> melodies, String timeSignature){
		Map<Integer, Double> weights = null;
		if (timeSignature.equals("4/4") || timeSignature.equals("2/4")) {
			weights = fourFourOrTwoFourRhythmWeightValues;
		} else if (timeSignature.equals("3/4")) {
			weights = fourThreeRhythmWeightValues;
		} else if (timeSignature.equals("6/8")) {
			weights = SixEightRhythmWeightValues;
		} else {
			throw new IllegalArgumentException("Weights time signature not implemented :" + timeSignature);
		}
		for (Melody melody : melodies) {
			List<NotePos> notes = melody.getNotes();
			for (NotePos notePos : notes) {
				if (weights.containsKey(notePos.getPosition())) {
					notePos.setPositionWeight(weights.get(notePos.getPosition()));
				} else{ //set default value
					notePos.setPositionWeight(DEFAULT_WEIGHT);
				}
			}
		}
	}

	public static List<Harmony> extractHarmony(List<Melody> melodies, int octave){
		Map<Integer, List<NotePos>> chords = extractNoteMap(melodies);
		List<Harmony> list = new ArrayList<>();
		for (Entry<Integer, List<NotePos>> ch : chords.entrySet()) {
			Harmony noteList = new Harmony(ch.getKey(),ch.getValue().get(0).getLength()
					, ch.getValue(), new UniformPitchSpace(ch.getValue(),octave));
			list.add(noteList);
		}
		return list;
	}

	public static Map<Integer, List<NotePos>> extractNoteMap(List<Melody> melodies) {
		Map<Integer, List<NotePos>> chords = new TreeMap<>();
		Set<Integer> positions = new TreeSet<>();
		for (Melody melody : melodies) {
			List<NotePos> notes = melody.getNotes();
			for (NotePos notePos : notes) {
				positions.add(notePos.getPosition());
			}
		}
		int voice = 0;
		for (Melody melody : melodies) {
			List<NotePos> notes = melody.getNotes();
			int melodyLength = notes.size() - 1;
			Iterator<Integer> iterator = positions.iterator();
			Integer position = iterator.next();
			for (int i = 0; i < melodyLength; i++) {
				NotePos firstNote = notes.get(i);
				NotePos secondNote = notes.get(i + 1);			
				while (position < secondNote.getPosition()) {
					addNoteToChordMap(chords, firstNote, voice);
					position = iterator.next();
				}
			}
			NotePos lastNote = notes.get(melodyLength);
			addNoteToChordMap(chords, lastNote, voice);
			voice++;
		}
		return chords;
	}

	private static void addNoteToChordMap(Map<Integer, List<NotePos>> chords, NotePos note,
			 int voice) {
		int position = note.getPosition();
		List<NotePos> chord = null;
		if (chords.containsKey(position)) {
			chord = chords.get(position);
		} else {
			chord = new ArrayList<>();
		}
		NotePos notePos = new NotePos(note.getPitchClass(), voice , position, note.getLength());
		notePos.setPitch(note.getPitch());
		chord.add(notePos);
		chords.put(position, chord);
	}
	
}
