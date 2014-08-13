package neo.data;

import java.util.List;

import jmetal.util.PseudoRandom;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.evaluation.MusicProperties;

public class Motive {

	private List<Harmony> harmonies;
	private List<Melody> melodies;
	private MusicProperties musicProperties;
		
	public Motive(List<Harmony> harmonies, List<Melody> melodies) {
		this.harmonies = harmonies;
		this.melodies = melodies;
	}

	public List<Harmony> getHarmonies() {
		return harmonies;
	}
	
	public List<Melody> getMelodies() {
		return melodies;
	}
	
	public void mutateHarmonyNoteToPreviousPitchFromScale(){
		int harmonyIndex = PseudoRandom.randInt(0, harmonies.size() - 1);
		Harmony harmony = harmonies.get(harmonyIndex);
		Note mutatedNote = harmony.mutateNoteToPreviousPitchFromScale(musicProperties.getScale());
		melodies.stream()
			.flatMap(melody -> melody.getHarmonicMelodies().stream())
			.filter(harmMelody -> harmMelody.getPosition() == harmony.getPosition())
			.flatMap(harmMelody -> harmMelody.getNotes().stream())
			.filter(note -> note.getVoice() == mutatedNote.getVoice())
			.forEach(note -> note.setPitchClass(mutatedNote.getPitchClass()));
		//update only chord tones!!
	}
	
	public void mutateHarmonyPitchSpaceStrategy(){
		int harmonyIndex = PseudoRandom.randInt(0, harmonies.size() - 1);
		Harmony harmony = harmonies.get(harmonyIndex);
		harmony.mutatePitchSpaceStrategy();
	}
	
	public void updatePitches(){
		harmonies.forEach(harmony -> harmony.translateToPitchSpace());
		//update melodies
	}

	public void setMusicProperties(MusicProperties musicProperties) {
		this.musicProperties = musicProperties;
	}

	public MusicProperties getMusicProperties() {
		return musicProperties;
	}
	
//	private List<Melody> extractMelodies(){
//		melodies.clear();
//		harmonies.stream().forEach(harmony -> harmony.translateToPitchSpace());
//		Map<Integer, List<NotePos>> melodyMap = harmonies.stream().flatMap(harmony -> harmony.getNotes().stream()).collect(groupingBy(note -> note.getVoice()));
//		for (Entry<Integer, List<NotePos>> entry: melodyMap.entrySet()) {
//			List<NotePos> notes = entry.getValue();
//			Melody melody = new Melody(notes);
//			melodies.add(melody);
//		}
//		return melodies;
//	}
//	
//	private List<Melody> extractMelodies_concat(List<Harmony> harmonies) {
//		melodies.clear();
//		List<NotePos> allNotes = new ArrayList<>();
//		for (Harmony list : harmonies) {
//			for (NotePos notePos : list.getNotes()) {
//				allNotes.add(notePos);
//			}
//		}
//		Map<Integer, List<NotePos>> melodyMap = allNotes.stream().collect(groupingBy(n -> n.getVoice()));
//		for (Entry<Integer, List<NotePos>> entry: melodyMap.entrySet()) {
//			List<NotePos> notes = entry.getValue();
//			List<NotePos> clonedNotes = new ArrayList<>();
//			for (NotePos notePos : notes) {
//				try {
//					clonedNotes.add((NotePos) notePos.clone());
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//				}
//			}
//			List<NotePos> notePositions = concatNotesWithSamePitch(clonedNotes);
//			Melody melody = new Melody(notePositions);
//			melodies.add(melody);
//		}
//		return melodies;
//	}
//
//	public List<NotePos> concatNotesWithSamePitch(List<NotePos> notePositions) {
//		List<NotePos> notesToRemove = new ArrayList<>();
//		int size = notePositions.size() - 1;
//		for (int i = 0; i < size; i++) {
//			NotePos note = notePositions.get(i);
//			NotePos nextNote = notePositions.get(i + 1);
//			int j = 1;
//			while (note.samePitch(nextNote)) {
//				note.setLength(note.getLength() + nextNote.getLength());
//				notesToRemove.add(nextNote);
//				if ((i + j) < size) {
//					j++;
//					nextNote = notePositions.get(i + j);
//				}else {
//					i = notePositions.indexOf(nextNote) - 1;
//					break;
//				}
//			}
//		}
//		notePositions.removeAll(notesToRemove);
//		return notePositions;
//	}
	
}
