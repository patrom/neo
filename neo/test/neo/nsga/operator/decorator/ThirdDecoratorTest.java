package neo.nsga.operator.decorator;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.function.Consumer;

import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.model.note.Scale;

import org.junit.Before;
import org.junit.Test;

public class ThirdDecoratorTest {
	
	private ThirdDecorator thirdDecorator = new ThirdDecorator();
	private Consumer<Note> noteConsumer;

	@Before
	public void setUp() throws Exception {
		Scale scale = new Scale(Scale.MAJOR_SCALE);
		int step = 2;
		noteConsumer = note -> note.setPitchClass(scale.pickLowerStepFromScale(note.getPitchClass(), step));
	}

	@Test
	public void testDecorateHarmony() {
		Harmony harmony = harmony().pos(0).len(12).build();
		HarmonicMelody harmonicMelody = harmonicMelody()
				.harmonyNote(note().pc(0).ocatve(5).build())
				.voice(0)
				.notes(note().voice(0).pc(4).pos(0).len(6).build(), 
					   note().voice(0).pc(5).pos(6).len(12).build())
				.build();
		harmony.addHarmonicMelody(harmonicMelody);
		thirdDecorator.decorateHarmony(harmony, 0, 1, noteConsumer);
		Note note = harmony.getHarmonicMelodies().get(1).getMelodyNotes().get(0);
		assertEquals(1, note.getVoice());
		assertEquals(0, note.getPitchClass());
	}

	@Test
	public void testDecorateHarmonicMelody() {
		HarmonicMelody harmonicMelody = harmonicMelody()
				.harmonyNote(note().pc(0).ocatve(5).build())
				.voice(0)
				.notes(note().voice(0).pc(4).pos(0).len(6).build(), 
					   note().voice(0).pc(5).pos(6).len(12).build())
				.build();
		HarmonicMelody decorated = thirdDecorator.updateCopyHarmonicMelody(harmonicMelody, 1, noteConsumer);
		assertEquals(0, decorated.getMelodyNotes().get(0).getPitchClass());
		assertEquals(2, decorated.getMelodyNotes().get(1).getPitchClass());
		assertEquals(9, decorated.getHarmonyNote().getPitchClass());
	}

}
