package neo.data.harmony.pitchspace;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.note.NotePos;

import org.junit.Test;

public class TopOctavePitchSpaceTest {

	private PitchSpaceStrategy topOctavePitchStrategy;
	private List<NotePos> notes = new ArrayList<>();

	@Test
	public void testTranslateToPitchSpace() {
		notes.add(new NotePos(1, 0, 0, 12));
		notes.add(new NotePos(2, 1, 0, 12));
		notes.add(new NotePos(3, 2, 0, 12));
		notes.add(new NotePos(4, 3, 0, 12));
		Integer[] range = {6};
		topOctavePitchStrategy = new TopOctavePitchSpace(notes, range);
		topOctavePitchStrategy.translateToPitchSpace();
		assertEquals("pitch not correct", notes.get(3).getPitch(), 28);
		assertEquals("pitch not correct", notes.get(2).getPitch(), 39);
		assertEquals("pitch not correct", notes.get(1).getPitch(), 50);
		assertEquals("pitch not correct", notes.get(0).getPitch(), 73);
	}
}
