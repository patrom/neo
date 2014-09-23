package neo.nsga;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;

import org.junit.Before;
import org.junit.Test;

public class MusicVariableTest {

	private List<Harmony> harmonies = new ArrayList<>();
	private Motive motive;
	
	@Before
	public void setup() {
		List<Note> melodyNotes = new ArrayList<>();
		melodyNotes.add(note().pc(0).pos(0).len(12).build());
		melodyNotes.add(note().pc(2).pos(12).len(12).build());
		int voice = 1;
		Harmony harmony = harmony().pos(0).len(24).positionWeight(3.0).build();
		HarmonicMelody harmonicMelody = harmonicMelody().notes(melodyNotes).harmonyNote(note().pc(0).build()).voice(voice).build();
		harmony.addHarmonicMelody(harmonicMelody);
		harmonies.add(harmony);
		motive = new Motive(harmonies);
	}


	@Test
	public void testCloneMotives() {
		MusicVariable musicVariable = new MusicVariable(motive);
		Motive clonedMotive = musicVariable.cloneMotives(motive);
		assertEquals(harmonies.size(), clonedMotive.getHarmonies().size());
	}

}
