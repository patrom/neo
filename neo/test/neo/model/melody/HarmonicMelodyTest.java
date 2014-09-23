package neo.model.melody;

import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.List;

import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;

import org.junit.Before;
import org.junit.Test;

public class HarmonicMelodyTest {
	
	private HarmonicMelody harmonicMelody;

	@Before
	public void setUp() {
		harmonicMelody = harmonicMelody()
				.notes(note().pc(0).pitch(60).ocatve(5).build(), note().pc(2).pitch(0).ocatve(5).build())
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
	}

	@Test
	public void testUpdateMelodyNotesInt() {
		harmonicMelody.randomUpdateMelodyNotes(4);
		assertEquals(4, harmonicMelody.getMelodyNotes().get(1).getPitchClass());
	}

	@Test
	public void testUpdateMelodyPitchesToHarmonyPitch() {
		harmonicMelody.updateMelodyPitchesToHarmonyPitch();
		assertEquals(62, harmonicMelody.getMelodyNotes().get(1).getPitch());
	}

}
