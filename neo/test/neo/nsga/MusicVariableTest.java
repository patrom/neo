package neo.nsga;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static neo.data.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;

import org.junit.Before;
import org.junit.Test;

public class MusicVariableTest {

	private List<Harmony> harmonies = new ArrayList<>();
	private List<Melody> melodies = new ArrayList<>();
	private Motive motive;
	
	@Before
	public void setup() {
		List<Note> melodyNotes = new ArrayList<>();
		melodyNotes.add(note().pc(0).pos(0).len(12).build());
		melodyNotes.add(note().pc(2).pos(12).len(12).build());
		int voice = 1;
		Harmony harmony = harmony().pos(0).len(24).notes(4, 2).positionWeight(3.0).build();
		HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNotes, harmony, voice);
		Melody melody = new Melody(harmonicMelody, voice);
		melodies.add(melody);
		harmonies.add(harmony);
		motive = new Motive(harmonies, melodies);
	}


	@Test
	public void testCloneMotives() {
		MusicVariable musicVariable = new MusicVariable(motive);
		Motive clonedMotive = musicVariable.cloneMotives(motive);
		assertEquals(harmonies.size(), clonedMotive.getHarmonies().size());
		assertEquals(melodies.get(0).getNotes().size(), clonedMotive.getMelodies().get(0).getNotes().size());
	}

}
