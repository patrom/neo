package neo.model.harmony;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;

import org.junit.Test;

public class HarmonyTest {

	@Test
	public void testTransposeSecond() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(1);
		assertEquals(1, harmony.getNotes().get(0).getPitchClass());
		assertEquals(5, harmony.getNotes().get(1).getPitchClass());
		assertEquals(8, harmony.getNotes().get(2).getPitchClass());
		assertTrue(harmony.getChord().getPitchClassMultiSet().contains(1));
	}
	
	@Test
	public void testTransposeSixth() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(8);
		assertEquals(8, harmony.getNotes().get(0).getPitchClass());
		assertEquals(0, harmony.getNotes().get(1).getPitchClass());
		assertEquals(3, harmony.getNotes().get(2).getPitchClass());
		assertTrue(harmony.getChord().getPitchClassMultiSet().contains(8));
	}
	
	
	@Test
	public void testMutatePitchSpaceStrategy() {
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.transpose(8);
		assertEquals(8, harmony.getNotes().get(0).getPitchClass());
		assertEquals(0, harmony.getNotes().get(1).getPitchClass());
		assertEquals(3, harmony.getNotes().get(2).getPitchClass());
		harmony.mutatePitchSpace();
	}
	
	@Test
	public void testSwitchHarmonyNote(){
		Harmony harmony = harmony().notes(0,4,7).build();
		harmony.swapHarmonyNotes();
		List<Note> notes = harmony.getNotes();
		assertTrue(notes.contains(note().pc(0).build()));
		assertTrue(notes.contains(note().pc(4).build()));
		assertTrue(notes.contains(note().pc(7).build()));
		for (HarmonicMelody harmonicMelody  : harmony.getHarmonicMelodies()) {
			assertTrue(harmonicMelody.getMelodyNotes().contains(harmonicMelody.getHarmonyNote()));
		}
	}
	
//	@Test random octave range
//	public void testTranslateToPitchSpace() { 
//		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
//		harmonicMelodies.add(harmonicMelody().voice(0).pos(0).harmonyNote(note().pc(0).voice(0).pos(0).build())
//				.notes(note().pc(0).voice(0).pos(0).len(6).build(), 
//					   note().pc(3).voice(0).pos(6).len(12).build()).build());
//		harmonicMelodies.add(harmonicMelody().voice(1).pos(12).harmonyNote(note().pc(4).voice(2).pos(0).build())
//				.notes(note().pc(4).voice(2).pos(18).len(6).build()).build());
//		harmonicMelodies.add(harmonicMelody().voice(2).pos(24).harmonyNote(note().pc(7).voice(1).pos(0).build())
//				.notes(note().pc(7).voice(1).pos(24).len(6).build(),
//						note().pc(2).voice(1).pos(30).len(6).build()).build());
//		Harmony harmony = new Harmony(0, 24, harmonicMelodies);
//
//		harmony.translateToPitchSpace();//is random
//		assertEquals(60, harmony.getNotes().get(0).getPitch());
//		assertEquals(52, harmony.getNotes().get(1).getPitch());
//		assertEquals(43, harmony.getNotes().get(2).getPitch());
//		assertEquals(60, harmony.getHarmonicMelodies().get(0).getMelodyNotes().get(0).getPitch());
//		assertEquals(63, harmony.getHarmonicMelodies().get(0).getMelodyNotes().get(1).getPitch());
//	}
	
}
