package neo.objective.melody;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static neo.data.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import neo.AbstractTest;
import neo.data.Motive;
import neo.data.harmony.ChordType;
import neo.data.harmony.Harmony;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Interval;
import neo.data.note.Note;

import org.junit.Before;
import org.junit.Test;

public class MelodicObjectiveTest extends AbstractTest {

	private MelodicObjective melodicObjective;
	private double totalWeight;
	private List<Harmony> harmonies;
	private Motive motive;
	
	@Before
	public void setup() {
		musicProperties.setMinimumLength(6);
		harmonies = new ArrayList<>();
	}
	
	@Test
	public void test_E_C() {
		List<Note> melodyNotes = new ArrayList<>();
		melodyNotes.add(note().pc(4).pos(0).len(24).positionWeight(3.0).build());
		melodyNotes.add(note().pc(0).pos(24).len(24).positionWeight(3.0).build());
		int voice = 1;
		HarmonicMelody harmonicMelody = new HarmonicMelody(melodyNotes, null, voice);
		Melody melody = new Melody(harmonicMelody, voice);
		melodies.add(melody);
		
		harmonies.add(harmony().pos(0).len(24).notes(4).positionWeight(3.0).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).positionWeight(3.0).build());
		totalWeight = 6.0;
		motive = new Motive(harmonies, melodies);
		melodicObjective = new MelodicObjective(musicProperties, motive);
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("test_E_C :" + melodicValue);
		double expected = (Interval.GROTE_TERTS.getMelodicValue() * ((3 + 3)/totalWeight)) / (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_E_D_C() {
		harmonies.add(harmony().pos(0).len(12).notes(4).positionWeight(1.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).positionWeight(1.5).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).positionWeight(3.0).build());
		totalWeight = 6.0;
		motive = new Motive(harmonies, melodies);
		melodicObjective = new MelodicObjective(musicProperties, motive);
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("test_E_D_C :" + melodicValue);
		double expected = ((Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight)) + (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 3)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 3)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_E__D_C() {
		harmonies.add(harmony().pos(0).len(18).notes(4).positionWeight(2.5).build());
		harmonies.add(harmony().pos(18).len(6).notes(2).positionWeight(0.5).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).positionWeight(3.0).build());
		totalWeight = 6.0;
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies, melodies));
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("test_E__D_C :" + melodicValue);
		double expected = ((Interval.GROTE_SECONDE.getMelodicValue() * ((2.5 + 0.5)/totalWeight)) + (Interval.GROTE_SECONDE.getMelodicValue() * ((0.5 + 3)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((2.5 + 3)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	
	@Test
	public void test_E_D_C_D() {
		harmonies.add(harmony().pos(0).len(12).notes(4).positionWeight(1.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).positionWeight(1.5).build());
		harmonies.add(harmony().pos(24).len(12).notes(0).positionWeight(1.5).build());
		harmonies.add(harmony().pos(36).len(12).notes(2).positionWeight(1.5).build());
		totalWeight = 6.0;
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies, melodies));
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("test_E_D_C_D :" + melodicValue);
		double expected = ((4 * (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))) +  (Interval.UNISONO.getMelodicValue() * ((1.5 + 1.5)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_C_D_E_F() {
		harmonies.add(harmony().pos(0).len(12).notes(0).positionWeight(1.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).positionWeight(1.5).build());
		harmonies.add(harmony().pos(24).len(12).notes(4).positionWeight(1.5).build());
		harmonies.add(harmony().pos(36).len(12).notes(5).positionWeight(1.5).build());
		totalWeight = 6.0;
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies, melodies));
		double melodicValue = melodicObjective.evaluate();
		LOGGER.info("test_C_D_E_F :" + melodicValue);
		double expected = (((2 * (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))) + (Interval.KLEINE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)) + (Interval.KLEINE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))
				+ (Interval.KWART.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0.001);
	}
	
	@Test
	public void testEvaluateMelody() {
		List<Note> notes = new ArrayList<>();
		notes.add(note().pc(0).pos(0).len(6).positionWeight(1.0).build());
		notes.add(note().pc(2).pos(6).len(6).positionWeight(0.5).build());
		notes.add(note().pc(4).pos(12).len(6).positionWeight(1.0).build());
		notes.add(note().pc(2).pos(18).len(6).positionWeight(0.5).build());
		melodicObjective = new MelodicObjective(musicProperties, null);
		double value = melodicObjective.evaluateMelody(notes, 1);
		LOGGER.info("Melody value: " + value);
	}
	
	@Test
	public void testExtractNotesOnLevel(){
		List<Note> notes = new ArrayList<>();
		notes.add(note().pc(0).pos(0).len(6).positionWeight(1.0).build());
		notes.add(note().pc(2).pos(6).len(3).positionWeight(0.5).build());
		notes.add(note().pc(3).pos(9).len(3).positionWeight(1.0).build());//max finds only 1
		notes.add(note().pc(4).pos(12).len(6).positionWeight(0.5).build());
		notes.add(note().pc(5).pos(18).len(6).positionWeight(1.0).build());
		melodicObjective = new MelodicObjective(musicProperties, null);
		notes = melodicObjective.extractNotesOnLevel(notes, 1);
		assertEquals(2, notes.size());
		assertEquals(5, notes.get(1).getPitchClass());
		notes = melodicObjective.extractNotesOnLevel(notes, 2);
		assertEquals(1, notes.size());
		assertEquals(0, notes.get(0).getPitchClass());
	}
	
	@Test
	public void testEvaluateTriadicValueMelody() {
		List<Note> notes = new ArrayList<>();
		notes.add(note().pc(0).pos(0).len(6).positionWeight(1.0).build());
		notes.add(note().pc(4).pos(6).len(6).positionWeight(0.5).build());
		notes.add(note().pc(7).pos(12).len(6).positionWeight(1.0).build());
		notes.add(note().pc(0).pos(18).len(6).positionWeight(0.5).build());
		melodicObjective = new MelodicObjective(musicProperties, null);
		double value = melodicObjective.evaluateTriadicValueMelody(notes);
		assertEquals(ChordType.MAJOR.getDissonance(), value, 0);
	}



}
