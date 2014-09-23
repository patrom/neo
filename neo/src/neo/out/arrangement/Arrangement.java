package neo.out.arrangement;

import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;

import neo.model.note.Note;

public class Arrangement {

	public static void main(String[] args) {
		List<Note> notes = new ArrayList<>();
		notes.add(note().pos(0).len(12).pitch(60).build());
		notes.add(note().pos(12).len(36).pitch(61).build());
		
		List<Note> rhythmicNotes = Arrangement.applyFixedPattern(notes, 6);
		System.out.println(rhythmicNotes);
		int[] pattern = new int[4];
		pattern[0] = 0;
		pattern[1] = 6;
		pattern[2] = 18;
		pattern[3] = 24;
		List<Note> rhythmicNotes2 = Arrangement.applyFixedPattern(notes, pattern);
		System.out.println(rhythmicNotes2);
	}
	
	public static List<Note> applyFixedPattern(List<Note> notes, int rhythmicLength){
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
	
	public static void transpose(List<Note> notes, int octave){
		notes.forEach(n -> n.setPitch(n.getPitch() * (n.getOctave() + octave)));
	}
	
	public static List<Note> applyFixedPattern(List<Note> notes, int[] pattern){
		List<Note> rhythmicNotes = new ArrayList<>();
		for (Note note : notes) {
			rhythmicNotes.addAll(applyPattern(note, pattern));
		}
		return rhythmicNotes;
	}
	
	public static List<Note> applyPattern(Note note, int[] rhythmicPattern){
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
