package neo.out.arrangement;

import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;

import neo.model.note.Note;

import org.springframework.stereotype.Component;

@Component
public class Arrangement {
	
	
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
	
	public void transpose(List<Note> notes, int octave){
		notes.forEach(n -> n.setPitch(n.getPitch() * (n.getOctave() + octave)));
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
}
