package neo.model.harmony;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.out.instrument.Ensemble;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibCello;

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
	public void updateNotesWithInstrumentConstraints() {
		HarmonyBuilder harmonyBuilder = harmony();
		harmonyBuilder.melodyBuilder(harmonicMelody().voice(0)
				.harmonyNote(note().pitch(20).build())
				.notes(note().pitch(36).build(),
					   note().pitch(38).build(),
					   note().pitch(25).build())
				.build());
		Harmony harmony = harmonyBuilder.build();
		List<Instrument> instruments = Collections.singletonList(new KontaktLibCello(0, 1));
		harmony.updateNotesWithInstrumentConstraints(instruments);
		assertEquals(44, harmony.getNotes().get(0).getPitch());
		List<Note> melodyNotes = harmony.getHarmonicMelodies().get(0).getMelodyNotes();
		assertEquals(36, melodyNotes.get(0).getPitch());
		assertEquals(38, melodyNotes.get(1).getPitch());
		assertEquals(37, melodyNotes.get(2).getPitch());
	}
	
	@Test
	public void testSearchBestChord() {
		HarmonyBuilder harmonyBuilder = harmony();
		harmonyBuilder.melodyBuilder(harmonicMelody().voice(0).pos(0)
				.harmonyNote(note().pc(0).pos(0).build())
				.notes(note().pc(0).pos(0).len(6).build(),
					   note().pc(1).pos(6).len(12).build(),
					   note().pc(3).pos(18).len(6).build()).build());
		harmonyBuilder.melodyBuilder(harmonicMelody().voice(1).pos(0)
				.harmonyNote(note().pc(4).pos(0).build())
				.notes(note().pc(4).pos(0).len(12).build(),
					   note().pc(5).pos(12).len(12).build()).build());
		harmonyBuilder.melodyBuilder(harmonicMelody().voice(2).pos(0)
				.harmonyNote(note().pc(7).pos(0).build())
				.notes(note().pc(7).pos(0).len(24).build()).build());
		Harmony harmony = harmonyBuilder.build();
		harmony.searchBestChord();
		assertTrue(harmony.getNotes().contains(NoteBuilder.note().pc(0).build()));
		assertTrue(harmony.getNotes().contains(NoteBuilder.note().pc(4).build()));
		assertTrue(harmony.getNotes().contains(NoteBuilder.note().pc(7).build()));
	}
	
}
