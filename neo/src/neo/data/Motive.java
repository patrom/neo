package neo.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.NotePos;

public class Motive {

	private List<Harmony> harmonies;
	private List<Melody> melodies = new ArrayList<>();
		
	public Motive(List<Harmony> harmonies) {
		this.harmonies = harmonies;
	}

	public List<Harmony> getHarmonies() {
		return harmonies;
	}
	
	public List<Melody> getMelodies() {
		return extractMelodies();
	}
	
	private List<Melody> extractMelodies(){
		melodies.clear();
		List<NotePos> allNotes = new ArrayList<>();
		for (Harmony harmony : harmonies) {
			harmony.translateToPitchSpace();
			for (NotePos notePos : harmony.getNotes()) {
				allNotes.add(notePos);
			}
		}
		Map<Integer, List<NotePos>> melodyMap = allNotes.stream().collect(Collectors.groupingBy(n -> n.getVoice()));
		for (Entry<Integer, List<NotePos>> entry: melodyMap.entrySet()) {
			List<NotePos> notes = entry.getValue();
			Melody melody = new Melody(notes);
			melodies.add(melody);
		}
		return melodies;
	}
	
	private List<Melody> extractMelodies_concat(List<Harmony> harmonies) {
		melodies.clear();
		List<NotePos> allNotes = new ArrayList<>();
		for (Harmony list : harmonies) {
			for (NotePos notePos : list.getNotes()) {
				allNotes.add(notePos);
			}
		}
		Map<Integer, List<NotePos>> melodyMap = allNotes.stream().collect(Collectors.groupingBy(n -> n.getVoice()));
		for (Entry<Integer, List<NotePos>> entry: melodyMap.entrySet()) {
			List<NotePos> notes = entry.getValue();
			List<NotePos> clonedNotes = new ArrayList<>();
			for (NotePos notePos : notes) {
				try {
					clonedNotes.add((NotePos) notePos.clone());
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			List<NotePos> notePositions = concatNotesWithSamePitch(clonedNotes);
			Melody melody = new Melody(notePositions);
			melodies.add(melody);
		}
		return melodies;
	}

	public List<NotePos> concatNotesWithSamePitch(List<NotePos> notePositions) {
		List<NotePos> notesToRemove = new ArrayList<>();
		int size = notePositions.size() - 1;
		for (int i = 0; i < size; i++) {
			NotePos note = notePositions.get(i);
			NotePos nextNote = notePositions.get(i + 1);
			int j = 1;
			while (note.samePitch(nextNote)) {
				note.setLength(note.getLength() + nextNote.getLength());
				notesToRemove.add(nextNote);
				if ((i + j) < size) {
					j++;
					nextNote = notePositions.get(i + j);
				}else {
					i = notePositions.indexOf(nextNote) - 1;
					break;
				}
			}
		}
		notePositions.removeAll(notesToRemove);
		return notePositions;
	}
	
}
