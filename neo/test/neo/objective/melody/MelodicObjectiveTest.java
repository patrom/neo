package neo.objective.melody;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import neo.AbstractTest;
import neo.DefaultConfig;
import neo.generator.MusicProperties;
import neo.model.dissonance.Dissonance;
import neo.model.harmony.Chord;
import neo.model.harmony.ChordType;
import neo.model.note.Interval;
import neo.model.note.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class MelodicObjectiveTest extends AbstractTest {

	@Autowired
	private MelodicObjective melodicObjective;
	@Autowired
	@Qualifier(value="TonalDissonance")
	private Dissonance dissonance;
	private double totalWeight;
	private List<Note> melodyNotes;
	
	private Random random = new Random();
	
	@Before
	public void setup() {
		musicProperties.setMinimumLength(6);
		melodyNotes = new ArrayList<>();
	}
	
	private List<Note> generateRandomMelody(){
		int length = 10;
		IntStream intStream = random.ints(60, 79);
		List<Integer> randomPitches = intStream
				.limit(length)
				.boxed()
				.collect(Collectors.toList());
		List<Note> notes = new ArrayList<Note>();
		for (int i = 0; i < length; i++) {
			Note note = note().pitch(randomPitches.get(i)).pos(i * 6).len(6).positionWeight(1.0).build();
			notes.add(note);
		}
		return notes;
	}
	
	@Test
	public void testMelody(){
		melodyNotes = generateRandomMelody();
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test melody :" + melodicValue);
	}
	
	@Test
	public void test_E_C() {
		melodyNotes.add(note().pitch(64).positionWeight(3.0).build());
		melodyNotes.add(note().pitch(60).positionWeight(3.0).build());
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test_E_C :" + melodicValue);
		double expected = Interval.GROTE_TERTS.getMelodicValue();
		assertEquals(expected, melodicValue, 0.001);
	}
	
	@Test
	public void test_E_D_C() {
		melodyNotes.add(note().pitch(64).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(62).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(60).positionWeight(3.0).build());
		totalWeight = 7.5;
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test_E_D_C :" + melodicValue);
		double expected = (Interval.GROTE_SECONDE.getMelodicValue() * (1.5 + 1.5) 
				+ (Interval.GROTE_SECONDE.getMelodicValue() * (1.5 + 3)))/totalWeight;
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_E__D_C() {
		melodyNotes.add(note().pitch(64).positionWeight(2.5).build());
		melodyNotes.add(note().pitch(62).positionWeight(0.5).build());
		melodyNotes.add(note().pitch(60).positionWeight(3.0).build());
		totalWeight = 6.5;
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test_E__D_C :" + melodicValue);
		double expected = (Interval.GROTE_SECONDE.getMelodicValue() * (2.5 + 0.5) + Interval.GROTE_SECONDE.getMelodicValue() * (0.5 + 3))/totalWeight;
		assertEquals(expected, melodicValue, 0);
	}
	
	
	@Test
	public void test_E_D_C_D() {
		melodyNotes.add(note().pitch(64).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(62).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(60).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(62).positionWeight(1.5).build());
		totalWeight = 9.0;
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test_E_D_C_D :" + melodicValue);
		double expected = 3 * (Interval.GROTE_SECONDE.getMelodicValue() * (1.5 + 1.5))/totalWeight;
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_C_D_E_F() {
		melodyNotes.add(note().pitch(60).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(62).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(64).positionWeight(1.5).build());
		melodyNotes.add(note().pitch(65).positionWeight(1.5).build());
		totalWeight = 9.0;
		double melodicValue = melodicObjective.evaluateMelody(melodyNotes, 1);
		LOGGER.info("test_C_D_E_F :" + melodicValue);
		double expected = ((2 * (Interval.GROTE_SECONDE.getMelodicValue() * (1.5 + 1.5))) + (Interval.KLEINE_SECONDE.getMelodicValue() * (1.5 + 1.5)))/totalWeight;
		assertEquals(expected, melodicValue, 0.001);
	}
	
	@Test
	public void testExtractNotesOnLevel(){
		List<Note> notes = new ArrayList<>();
		notes.add(note().pc(0).pos(0).len(6).positionWeight(1.0).build());
		notes.add(note().pc(2).pos(6).len(3).positionWeight(0.5).build());
		notes.add(note().pc(3).pos(9).len(3).positionWeight(1.0).build());//max finds only 1
		notes.add(note().pc(4).pos(12).len(6).positionWeight(0.5).build());
		notes.add(note().pc(5).pos(18).len(6).positionWeight(1.0).build());
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
		double value = melodicObjective.evaluateTriadicValueMelody(notes);
		assertEquals(0.99, value, 0);
	}
	
	@Test
	public void testEvaluateMelody() {
		List<Note> notes = new ArrayList<>();
		notes.add(note().pitch(60).pc(0).pos(0).len(6).positionWeight(2.0).build());
		notes.add(note().pitch(62).pc(2).pos(6).len(6).positionWeight(2.0).build());
		notes.add(note().pitch(64).pc(4).pos(12).len(6).positionWeight(1.0).build());
		double value = melodicObjective.evaluateMelody(notes, 1);
		LOGGER.info("Melody value: " + value);
		assertEquals(1.0, value, 0);
	}



}
