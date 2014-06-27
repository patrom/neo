package neo.objective.melody;

import static neo.data.harmony.HarmonyBuilder.harmony;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.note.Interval;
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;

import org.junit.Before;
import org.junit.Test;

public class MelodicObjectiveTest {

	private MelodicObjective melodicObjective;
	private MusicProperties musicProperties;
	private Map<Integer, Double> rhythmWeightValues;
	private double totalWeight;
	private List<Harmony> harmonies;
	private Motive motive;
	
	@Before
	public void setup() {
		musicProperties = new MusicProperties();
		musicProperties.setMinimumLength(6);
		rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		rhythmWeightValues.put(30, 0.5);
		rhythmWeightValues.put(36, 1.0);
		rhythmWeightValues.put(42, 0.5);
		rhythmWeightValues.put(48, 0.0);
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
		totalWeight = rhythmWeightValues.values().stream().mapToDouble(v -> v).sum();
		harmonies = new ArrayList<>();
	}
	
	@Test
	public void weightTest(){
		NotePos note = new NotePos(0,1,0,18);
		melodicObjective = new MelodicObjective(musicProperties, motive);
		Double weight = melodicObjective.getWeight(note);
		assertEquals(2.5, weight, 0);
	}
	
	@Test
	public void test_E_C() {
		harmonies.add(harmony().pos(0).len(24).notes(4).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).build());
		motive = new Motive(harmonies);
		melodicObjective = new MelodicObjective(musicProperties, motive);
		double melodicValue = melodicObjective.evaluate();
		System.out.println("test_E_C :" + melodicValue);
		double expected = (Interval.GROTE_TERTS.getMelodicValue() * ((3 + 3)/totalWeight)) / (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_E_D_C() {
		harmonies.add(harmony().pos(0).len(12).notes(4).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).build());
		motive = new Motive(harmonies);
		melodicObjective = new MelodicObjective(musicProperties, motive);
		double melodicValue = melodicObjective.evaluate();
		System.out.println("test_E_D_C :" + melodicValue);
		double expected = ((Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight)) + (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 3)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 3)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_E__D_C() {
		harmonies.add(harmony().pos(0).len(18).notes(4).build());
		harmonies.add(harmony().pos(18).len(6).notes(2).build());
		harmonies.add(harmony().pos(24).len(24).notes(0).build());
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies));
		double melodicValue = melodicObjective.evaluate();
		System.out.println("test_E__D_C :" + melodicValue);
		double expected = ((Interval.GROTE_SECONDE.getMelodicValue() * ((2.5 + 0.5)/totalWeight)) + (Interval.GROTE_SECONDE.getMelodicValue() * ((0.5 + 3)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((2.5 + 3)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	
	@Test
	public void test_E_D_C_D() {
		harmonies.add(harmony().pos(0).len(12).notes(4).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).build());
		harmonies.add(harmony().pos(24).len(12).notes(0).build());
		harmonies.add(harmony().pos(36).len(12).notes(2).build());
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies));
		double melodicValue = melodicObjective.evaluate();
		System.out.println("test_E_D_C_D :" + melodicValue);
		double expected = ((4 * (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))) +  (Interval.UNISONO.getMelodicValue() * ((1.5 + 1.5)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0);
	}
	
	@Test
	public void test_C_D_E_F() {
		harmonies.add(harmony().pos(0).len(12).notes(0).build());
		harmonies.add(harmony().pos(12).len(12).notes(2).build());
		harmonies.add(harmony().pos(24).len(12).notes(4).build());
		harmonies.add(harmony().pos(36).len(12).notes(5).build());
		melodicObjective = new MelodicObjective(musicProperties, new Motive(harmonies));
		double melodicValue = melodicObjective.evaluate();
		System.out.println("test_C_D_E_F :" + melodicValue);
		double expected = (((2 * (Interval.GROTE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))) + (Interval.KLEINE_SECONDE.getMelodicValue() * ((1.5 + 1.5)/totalWeight))
				+ (Interval.GROTE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)) + (Interval.KLEINE_TERTS.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))
				+ (Interval.KWART.getMelodicValue() * ((1.5 + 1.5)/totalWeight)))/ (harmonies.size() - 1);
		assertEquals(expected, melodicValue, 0.001);
	}



}
