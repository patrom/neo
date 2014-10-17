package neo.model;

import static junit.framework.Assert.assertEquals;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import neo.AbstractTest;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;

import org.junit.Before;
import org.junit.Test;

public class MotiveTest extends AbstractTest{
	
	private Motive motive;

	@Before
	public void setUp(){
		List<Harmony> harmonies = new ArrayList<>();
		HarmonicMelody harmonicMelody = harmonicMelody()
					.harmonyNote(note().pc(0).ocatve(5).build())
					.voice(0)
					.notes(note().voice(0).pc(4).pos(0).len(6).build(), 
						   note().voice(0).pc(5).pos(6).len(12).build())
					.build();
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		harmonicMelodies.add(harmonicMelody);
		Harmony harmony = new Harmony(0, 12, harmonicMelodies , musicProperties.getOctaveHighestPitchClass());
		harmonies.add(harmony);
		motive = new Motive(harmonies);
		musicProperties.setChordSize(3);
		motive.setMusicProperties(musicProperties);
	}
	
	@Test
	public void testGetMelodies(){
		List<Melody> melodies = motive.getMelodies();
		assertFalse(melodies.isEmpty());
		assertEquals(2, melodies.get(0).getMelodieNotes().size());
		assertEquals(5, melodies.get(0).getMelodieNotes().get(1).getPitchClass());
	}
	
	@Test
	public void updateInnerMetricWeightNotes(){
		musicProperties.setMinimumLength(6);
		List<Note> notes = new ArrayList<>();
		notes.add(NoteBuilder.note().pos(6).build());
		notes.add(NoteBuilder.note().pos(12).build());
		notes.add(NoteBuilder.note().pos(18).build());
//		notes.add(NoteBuilder.note().pos(24).build());
		notes.add(NoteBuilder.note().pos(36).build());
		
//		List<Note> notes = new ArrayList<>();
//		notes.add(NoteBuilder.note().pos(0).build());
//		notes.add(NoteBuilder.note().pos(6).build());
//		notes.add(NoteBuilder.note().pos(9).build());
//		notes.add(NoteBuilder.note().pos(12).build());
//		notes.add(NoteBuilder.note().pos(18).build());
//		notes.add(NoteBuilder.note().pos(30).build());
//		notes.add(NoteBuilder.note().pos(36).build());
		motive.updateInnerMetricWeightNotes(notes);
		
//		INFO: {0=17.0, 2=17.0, 3=8.0, 4=13.0, 6=21.0, 10=4.0, 12=4.0}
		System.out.println(notes);
	}
	
	

}
