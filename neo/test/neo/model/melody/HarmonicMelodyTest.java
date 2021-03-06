package neo.model.melody;

import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.model.note.Scale;

import org.junit.Before;
import org.junit.Test;

public class HarmonicMelodyTest {
	
	private HarmonicMelody harmonicMelody;

	@Before
	public void setUp() {
		harmonicMelody = harmonicMelody().voice(0)
				.notes(note().pc(0).pitch(60).ocatve(5).build(), note().pc(2).pitch(62).ocatve(5).build())
				.harmonyNote(note().pc(0).pitch(60).ocatve(5).build())
				.build();
	}

	@Test
	public void testGetNonChordNotes() {
		List<Note> nonChordNotes = harmonicMelody.getNonChordNotes();
		assertEquals(1, nonChordNotes.size());
	}

	@Test
	public void testGetChordNotes() {
		List<Note> chordNotes = harmonicMelody.getChordNotes();
		assertEquals(1, chordNotes.size());
	}

	@Test
	public void testUpdateMelodyNotesIntInt() {
		harmonicMelody.updateMelodyNotes(2, 4);
		assertEquals(4, harmonicMelody.getMelodyNotes().get(1).getPitchClass());
		assertTrue(harmonicMelody.getMelodyNotes().stream().noneMatch(n -> n.getPitch() == 0));
	}

	@Test
	public void testRandomUpdateMelodyNotes() {
		harmonicMelody.randomUpdateMelodyNotes(4);
		assertEquals(true, harmonicMelody.getMelodyNotes().contains(NoteBuilder.note().pc(4).build()));
	}
	
	@Test
	public void testRandomUpdateMelodyNotesAllNonChordTone() {
		Note harmonyNote = note().pc(0).pitch(60).ocatve(5).build();
		harmonicMelody = harmonicMelody()
				.notes(note().pc(0).pitch(60).ocatve(5).build(), note().pc(2).pitch(62).ocatve(5).build())
				.harmonyNote(harmonyNote)
				.build();
		harmonicMelody.randomUpdateMelodyNotes(4);
		assertEquals(true, harmonicMelody.getMelodyNotes().contains(harmonyNote));
		assertTrue(harmonicMelody.getMelodyNotes().stream().noneMatch(n -> n.getPitch() == 0));
	}

	@Test
	public void testUpdateMelodyPitchesToHarmonyPitch() {
		Note harmonyNote = note().pc(0).pitch(60).ocatve(5).build();
		harmonicMelody = harmonicMelody()
				.notes(note().pc(0).pitch(0).ocatve(5).build(), note().pc(2).pitch(0).ocatve(5).build())
				.harmonyNote(harmonyNote)
				.build();
		harmonicMelody.updateMelodyPitchesToHarmonyPitch();
		assertEquals(harmonyNote.getPitch(), harmonicMelody.getMelodyNotes().get(0).getPitch());
		assertTrue(harmonicMelody.getMelodyNotes().stream().noneMatch(n -> n.getPitch() == 0));
	}
	
	@Test
	public void testMutateMelodyNoteToHarmonyNote() {  
		HarmonicMelody harmonicMelody = harmonicMelody().voice(0).pos(0)
				.harmonyNote(note().pc(0).pos(0).build())
				.notes(note().pc(3).pos(0).len(6).build(),
					   note().pc(0).pos(6).len(12).build(),
					   note().pc(1).pos(18).len(6).build()).build();
		harmonicMelody.mutateMelodyNoteToHarmonyNote(2);
		assertTrue(harmonicMelody.getMelodyNotes().stream().map(note -> note.getPitchClass()).anyMatch(pc -> pc == 2));
	}
	
	@Test
	public void testMutateHarmonyNextNoteToPitch() {  
		harmonicMelody.mutateHarmonyNextNoteToPitch(new Scale(Scale.MAJOR_SCALE));
		assertEquals(2, harmonicMelody.getHarmonyNote().getPitchClass());
	}
	
	@Test
	public void testMutateHarmonyPreviousNoteToPitch() {  
		harmonicMelody.mutateHarmonyPreviousNoteToPitch(new Scale(Scale.MAJOR_SCALE));
		assertEquals(11, harmonicMelody.getHarmonyNote().getPitchClass());
	}
	
	@Test
	public void testUpdateHarmonyAndMelodyNotes() {  
		harmonicMelody.updateHarmonyAndMelodyNotes(4, n -> n.setPitchClass(3));
		assertEquals(4, harmonicMelody.getMelodyNotes().get(0).getPitchClass());
		assertEquals(3, harmonicMelody.getMelodyNotes().get(1).getPitchClass());
	}
	
	@Test
	public void testMutateHarmonyNoteToRandomPitch() {  
		harmonicMelody.mutateHarmonyNoteToRandomPitch(new Scale(Scale.MAJOR_SCALE));
		assertTrue(harmonicMelody.getMelodyNotes().stream()
				.anyMatch(note -> note.getPitchClass() == harmonicMelody.getHarmonyNote().getPitchClass()));
	}
	
	@Test
	public void testCopy(){
		HarmonicMelody copy = harmonicMelody.copy(1);
		assertEquals(copy.getVoice(), 1);
		Note note = copy.getMelodyNotes().get(0);
		assertEquals(note.getVoice(), 1);
		assertEquals(copy.getHarmonyNote().getVoice(), 1);
	}

}
