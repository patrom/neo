package neo.arrangement;

import static neo.data.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;

import neo.data.note.Note;
import neo.data.note.NoteBuilder;

public class Arrangement {

	public static void main(String[] args) {
		List<Note> notes = new ArrayList<>();
		notes.add(note().pos(0).len(12).pitch(60).build());
		notes.add(note().pos(12).len(18).pitch(61).build());
		List<Note> rhythmicNotes = Arrangement.applyFixedPattern(notes, 6);
		System.out.println(rhythmicNotes);
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
}
