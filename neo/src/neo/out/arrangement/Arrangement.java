package neo.out.arrangement;

import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import neo.model.note.Note;
import neo.model.note.NoteBuilder;


public class Arrangement {
	
	private Accompagnement accompagnement;
	
	
	public Arrangement(Accompagnement accompagnement) {
		this.accompagnement = accompagnement;
	}

	public List<Note> applyFixedPattern(List<Note> notes, int rhythmicLength){
		List<Note> rhythmicNotes = new ArrayList<>();
		for (Note note : notes) {
			int size = note.getLength()/rhythmicLength;
			for (int i = 0; i < size; i++) {
				rhythmicNotes.add(note().pos(note.getPosition() + (i * rhythmicLength))
										.len(rhythmicLength)
										.pitch(note.getPitch())
										.voice(note.getVoice())
										.build());
			}
		}
		return rhythmicNotes;
	}
	
	public void transpose(List<Note> notes, int step){
		notes.forEach(n -> n.setPitch(n.getPitch() + step));
	}
	
	public List<Note> applyFixedPattern(List<Note> notes, int[] pattern){
		List<Note> rhythmicNotes = new ArrayList<>();
		for (Note note : notes) {
			rhythmicNotes.addAll(applyPattern(note, pattern));
		}
		return rhythmicNotes;
	}
	
	public List<Note> applyPattern(Note note, int[] rhythmicPattern){
		List<Note> rhythmicNotes = new ArrayList<>();
		int patternLength = 0;
		for (int i = 0; i < rhythmicPattern.length - 1; i++) {
			patternLength = patternLength + rhythmicPattern[i];
			if (note.getLength() >= patternLength) {
				rhythmicNotes.add(note().pos(note.getPosition() + patternLength)
						.len(rhythmicPattern[i + 1] - rhythmicPattern[i])
						.pitch(note.getPitch())
						.voice(note.getVoice())
						.build());
			} else {
				break;
			}
		}
		return rhythmicNotes;
	}
	
	public List<Note> harmonyAccompagnement(Map<Integer, List<Note>> harmonyPositions, 	Integer[] compPattern) {
		List<Note> transformedList = new ArrayList<>();
		for (Entry<Integer, List<Note>> harmonyPosition : harmonyPositions.entrySet()) {
			List<Note> harmonyNotes = harmonyPosition.getValue();
			List<List<Note>> rhythmNotes = accompagnement.applyAccompagnement(harmonyNotes);
			for (int i = 0; i < compPattern.length - 1; i++) {
				List<Note> compNotes = rhythmNotes.get(i % rhythmNotes.size());
				for (Note note : compNotes) {
					Note compNote = NoteBuilder.note()
							.pos(harmonyPosition.getKey() + compPattern[i])
							.pc(note.getPitchClass())
							.pitch(note.getPitch())
							.len(compPattern[i + 1] - compPattern[i]).build();
					transformedList.add(compNote);
				}
			}
		}
		return transformedList;
	}
	
	public List<Note> accompagnement(Map<Integer, List<Note>> harmonyPositions, Integer[] compPattern) {
		Accompagnement[] compStrategy = {Accompagnement::chordal, Accompagnement::arpeggio};
		List<Note> transformedList = new ArrayList<>();
		int c = 0;
		for (Entry<Integer, List<Note>> harmonyPosition : harmonyPositions.entrySet()) {
			List<Note> harmonyNotes = harmonyPosition.getValue();
			Accompagnement compStr = compStrategy[c];
			List<List<Note>> rhythmNotes = compStr.applyAccompagnement(harmonyNotes);
			for (int i = 0; i < compPattern.length - 1; i++) {
				List<Note> compNotes = rhythmNotes.get(i % rhythmNotes.size());
				for (Note note : compNotes) {
					Note compNote = NoteBuilder.note()
							.pos(harmonyPosition.getKey() + compPattern[i])
							.pc(note.getPitchClass())
							.pitch(note.getPitch())
							.len(compPattern[i + 1] - compPattern[i]).build();
					transformedList.add(compNote);
				}
			}
			c++;
		}
		return transformedList;
	}
	
}
