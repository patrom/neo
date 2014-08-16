package neo.data.harmony.pitchspace;

import static junit.framework.Assert.assertEquals;
import static neo.data.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import neo.data.harmony.Harmony;
import neo.data.harmony.Harmony.BassOctavePitchSpace;
import neo.data.note.Note;

import org.junit.Test;

public class TopOctavePitchSpaceTest {

	private List<Note> notes = new ArrayList<>();

	@Test
	public void testTranslateToPitchSpace() {
		notes.add(new Note(1, 0, 0, 12));
		notes.add(new Note(2, 1, 0, 12));
		notes.add(new Note(3, 2, 0, 12));
		notes.add(new Note(4, 3, 0, 12));
		Integer[] range = {6};
		Harmony harmony = harmony().notes(notes).build();
		harmony.setPitchSpaceStrategy(harmony.new TopOctavePitchSpace(range));
		harmony.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 28);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 39);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 50);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
}
