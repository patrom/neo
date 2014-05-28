package test.neo.data.harmony.pitchspace;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;

import org.junit.Before;
import org.junit.Test;

public class UniformPitchSpaceTest {
	
	private PitchSpaceStrategy uniformPitchStrategy;
	private List<NotePos> notes = new ArrayList<>();

	@Test
	public void testTranslateToPitchSpaceNoCrossing() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(2, 1, 0, 12));
		notes.add(new NotePos(3, 2, 0, 12));
		notes.add(new NotePos(4, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 40);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossing() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(3, 1, 0, 12));
		notes.add(new NotePos(2, 2, 0, 12));
		notes.add(new NotePos(4, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 52);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 63);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossingBorder() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(2, 1, 0, 12));
		notes.add(new NotePos(4, 2, 0, 12));
		notes.add(new NotePos(3, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 52);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossingBorderTop() {
		notes.add(new NotePos(3, 0, 0, 12));
		notes.add(new NotePos(2, 1, 0, 12));
		notes.add(new NotePos(4, 2, 0, 12));
		notes.add(new NotePos(1, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 61);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 64);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 74);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 75);
	}
	
	@Test
	public void testTranslateToPitchSpaceDoubling() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(2, 1, 0, 12));
		notes.add(new NotePos(2, 2, 0, 12));
		notes.add(new NotePos(3, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceDoublingCrossing() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(3, 1, 0, 12));
		notes.add(new NotePos(2, 2, 0, 12));
		notes.add(new NotePos(3, 3, 0, 12));
		uniformPitchStrategy = new UniformPitchSpace(notes, 6);
		uniformPitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 63);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}

}
