package neo.data.melody.pitchspace;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.model.harmony.Harmony;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.pitchspace.PitchSpace;
import neo.model.melody.pitchspace.UniformPitchSpace;
import neo.model.note.Note;

import org.junit.Test;

public class UniformPitchSpaceTest {
	
	private List<Note> notes = new ArrayList<>();
	private Integer[] range = {6};
	
	@Test
	public void testTranslateToPitchSpaceSamePitchClass() {
		notes.add(new Note(0, 0, 0, 12));
		notes.add(new Note(0, 1, 0, 12));
		notes.add(new Note(0, 2, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(2).getPitch(), 72);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 72);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 72);
	}

	@Test
	public void testTranslateToPitchSpaceNoCrossing() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(3, 2, 0, 12));
		notes.add(new Note(4, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 40);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossing() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(3, 1, 0, 12));
		notes.add(new Note(2, 2, 0, 12));
		notes.add(new Note(4, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 52);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 63);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossingBorder() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(4, 2, 0, 12));
		notes.add(new Note(3, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 52);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceCrossingBorderTop() {
		notes.add(new Note(3, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(4, 2, 0, 12));
		notes.add(new Note(1, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 61);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 64);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 74);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 75);
	}
	
	@Test
	public void testTranslateToPitchSpaceDoubling() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(2, 2, 0, 12));
		notes.add(new Note(3, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
	
	@Test
	public void testTranslateToPitchSpaceDoublingCrossing() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(3, 1, 0, 12));
		notes.add(new Note(2, 2, 0, 12));
		notes.add(new Note(3, 3, 0, 12));
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new UniformPitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 63);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}

}
