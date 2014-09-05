package neo.data.melody.pitchspace;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.Harmony;
import neo.data.harmony.HarmonyBuilder;
import neo.data.note.Note;

import org.junit.Test;

public class BassOctavePitchSpaceTest {

	private List<Note> notes = new ArrayList<>();

	@Test
	public void testTranslateToPitchSpace() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(3, 2, 0, 12));
		notes.add(new Note(4, 3, 0, 12));
		Integer[] range = {6};
		Harmony harmony = HarmonyBuilder.harmony().notes(notes).build();
		PitchSpace pitchSpace = new BassOctavePitchSpace(range);
		pitchSpace.setNotes(notes);
		harmony.setPitchSpace(pitchSpace);
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 28);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 51);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 62);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}

}
