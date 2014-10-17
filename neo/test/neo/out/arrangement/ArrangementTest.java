package neo.out.arrangement;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import neo.DefaultConfig;
import neo.midi.HarmonyInstrument;
import neo.model.note.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class ArrangementTest extends JFrame{
	
	@Autowired
	private Arrangement arrangement;
	private List<Note> notes;
	private int[] pattern;

	@Before
	public void setUp() throws Exception {
		notes = new ArrayList<>();
		notes.add(note().pos(0).len(12).pitch(60).build());
		notes.add(note().pos(12).len(36).pitch(61).build());
		
		pattern = new int[4];
		pattern[0] = 0;
		pattern[1] = 6;
		pattern[2] = 18;
		pattern[3] = 24;
	}

	@Test
	public void testApplyFixedPattern() {
		List<Note> rhythmicNotes2 = arrangement.applyFixedPattern(notes, pattern);
		System.out.println(rhythmicNotes2);
	}

	@Test
	public void testTranspose() {
		arrangement.transpose(notes, 1);
	}

	@Test
	public void testApplyPattern() {
		List<Note> rhythmicNotes2 = arrangement.applyFixedPattern(notes, pattern);
		System.out.println(rhythmicNotes2);
	}
	
	@Test
	public void testAccompagnement() {
		Integer[] compPattern = {6,12,18,30};
		List<HarmonyInstrument> harmonyPositions = new ArrayList<>();
		harmonyPositions.add(createHarmonyInstrument(0, notes));
		harmonyPositions.add(createHarmonyInstrument(24, notes));
		List<Integer[]> compPatterns = new ArrayList<Integer[]>();
		compPatterns.add(compPattern);
		Accompagnement[] compStrategy = {Accompagnement::chordal};
		List<Note> accompagnement = arrangement.accompagnement(harmonyPositions, compPatterns, compStrategy);
		assertEquals(12, accompagnement.get(notes.size()).getPosition());
	}

	private HarmonyInstrument createHarmonyInstrument(int position, List<Note> notes) {
		HarmonyInstrument harmonyInstrument = new HarmonyInstrument();
		harmonyInstrument.setNotes(notes);
		harmonyInstrument.setPosition(position);
		return harmonyInstrument;
	}

}
