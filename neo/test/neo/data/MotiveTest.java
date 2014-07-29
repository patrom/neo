package neo.data;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.note.NotePos;

import org.junit.Before;
import org.junit.Test;

public class MotiveTest {
	
	private Motive motive;

	@Before
	public void setUp(){
		List<Harmony> harmonies = new ArrayList<>();
		motive = new Motive(harmonies);
	}

	@Test
	public void testConcatNotesWithSamePitchEnd() {
		List<NotePos> notes = new ArrayList<>();
		NotePos note = new NotePos(0, 0, 0, 6);
		note.setPitch(60);
		notes.add(note);
		note = new NotePos(0, 0, 6, 6);
		note.setPitch(60);
		notes.add(note);
		note = new NotePos(0, 0, 12, 12);
		note.setPitch(60);
		notes.add(note);
		List<NotePos> notePositions = motive.concatNotesWithSamePitch(notes);
		assertEquals(1, notePositions.size());
		assertEquals(24, notePositions.get(0).getLength());
	}
	
	@Test
	public void testConcatNotesWithSamePitch() {
		List<NotePos> notes = new ArrayList<>();
		NotePos note = new NotePos(0, 0, 0, 6);
		note.setPitch(60);
		notes.add(note);
		note = new NotePos(0, 0, 6, 6);
		note.setPitch(60);
		notes.add(note);
		note = new NotePos(2, 0, 12, 12);
		note.setPitch(62);
		notes.add(note);
		List<NotePos> notePositions = motive.concatNotesWithSamePitch(notes);
		assertEquals(2, notePositions.size());
		assertEquals(12, notePositions.get(0).getLength());
	}
	
	@Test
	public void testConcatNotesWithSamePitchNoConcat() {
		List<NotePos> notes = new ArrayList<>();
		NotePos note = new NotePos(0, 0, 0, 6);
		note.setPitch(60);
		notes.add(note);
		note = new NotePos(1, 0, 6, 6);
		note.setPitch(61);
		notes.add(note);
		note = new NotePos(2, 0, 12, 12);
		note.setPitch(62);
		notes.add(note);
		List<NotePos> notePositions = motive.concatNotesWithSamePitch(notes);
		assertEquals(3, notePositions.size());
		assertEquals(6, notePositions.get(0).getLength());
	}

}
