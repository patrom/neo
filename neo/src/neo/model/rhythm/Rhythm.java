package neo.model.rhythm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.model.note.Note;

public class Rhythm {
	
	protected List<Note> getTexture(List<Note> chordNotes, List<Note> melody, int[] texture){
		List<Note> textures = new ArrayList<>();
		int size = chordNotes.size();
		for (int i = 0; i < melody.size(); i++) {
			int nextTexture = getNextTexture(texture, i);
			if (nextTexture > 1) {
				List<Note> textureNotes = new ArrayList<Note>();
				Note melodyNote = melody.get(i);
				textureNotes.add(melodyNote);
				int index = getIndexOfNote(melodyNote, chordNotes);
				for (int j = 1; j < nextTexture; j++) {
					 Note note = chordNotes.get(((index - j) + size) % size).copy();
					 note.setVoice(melodyNote.getVoice());
					 note.setPosition(melodyNote.getPosition());
					 note.setLength(melodyNote.getLength());
					 Note lastNote = textureNotes.get(textureNotes.size() - 1);
					 while (lastNote.getPitch() < note.getPitch()) {
						note.setPitch(note.getPitch() - 12);
						note.setOctave(note.getOctave() - 1);
					 }
					 textureNotes.add(note);
				}
				textures.addAll(textureNotes);
			} else {
				textures.addAll(Collections.singletonList(melody.get(0)));
			}
		}
		return textures;
	}
	
	private int getIndexOfNote(Note melodyNote, List<Note> chordNotes) {
		Note noteInList = chordNotes.stream().filter(note -> note.getPitch() == melodyNote.getPitch()).findFirst().get();
		return chordNotes.indexOf(noteInList);
	}

	protected List<Note> getContour(List<Note> chordNotes, List<Note> sounds, int[] contour, int voice){
		List<Note> contours = new ArrayList<>();
		int index = 0;
		int nextContour = 0;
		int size = sounds.size();
		for (int i = 0; i < size; i++) {
			Note sound = sounds.get(i);
			index = index + nextContour;
			Note note = getNextNote(chordNotes, index);
			note.setPosition(sound.getPosition());
			note.setLength(sound.getLength());
			note.setVoice(voice);
			if (!contours.isEmpty()) {
				Note lastNote = contours.get(contours.size() - 1);
				if (nextContour > 0) {
					while (lastNote.getPitch() > note.getPitch()) {
						note.setPitch(note.getPitch() + 12);
						note.setOctave(note.getOctave() + 1);
					}
				} else if (nextContour < 0) {
					while (lastNote.getPitch() < note.getPitch()) {
						note.setPitch(note.getPitch() - 12);
						note.setOctave(note.getOctave() - 1);
					}
				} else if (nextContour == 0) {
					note.setPitch(lastNote.getPitch());
					note.setOctave(lastNote.getOctave());
				}  
			}
			nextContour = getNextContour(contour, i);
			contours.add(note);
		}
		return contours;
	}
	
	protected List<Note> getSounds(int[] sound){
		List<Note> sounds = new ArrayList<>();
		for (int i = 0; i < sound.length - 1; i++) {
			Note note = new Note();
			note.setPosition(sound[i]);
			note.setLength(sound[i + 1] - sound[i]);
			sounds.add(note);
		}
		return sounds;
	}

	private int getNextTexture(int[] texture, int i) {
		return texture[i % texture.length];
	}

	private Note getNextNote(List<Note> notes, int index) {
		int size = notes.size();
		return notes.get((index + size) % size).copy();
	}
	
	protected List<Note> getNextNotes(List<Note> notes, int index, int texture) {
		int size = notes.size();
		List<Note> textureNotes = new ArrayList<>();
		Note firstNote = notes.get((index + size) % size).copy();
		textureNotes.add(firstNote);
		for (int i = 1; i < texture; i++) {
			 Note note = notes.get(((index - i) + size) % size).copy();
//			 note.setVoice(note.getVoice() - 1);
			 Note lastNote = textureNotes.get(textureNotes.size() - 1);
			 while (lastNote.getPitch() < note.getPitch()) {
				note.setPitch(note.getPitch() - 12);
				note.setOctave(note.getOctave() - 1);
			 }
			 textureNotes.add(note);
		}
		return textureNotes;
	}
	
	private int getNextContour(int[] contour, int i) {
		return contour[i % contour.length];
	}
}
