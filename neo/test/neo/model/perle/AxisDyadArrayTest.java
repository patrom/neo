package neo.model.perle;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AxisDyadArrayTest {
	
	private AxisDyadArray axisDyadArray;

	@Before
	public void setUp() throws Exception {
		 axisDyadArray = new AxisDyadArray(new CyclicSet(IntervalCycle.P_IC7, 0), 0,
				new CyclicSet(IntervalCycle.P_IC7, 2), 0);
	}
	
	@Test
	public void allAxisDyadArray(){
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(10);
		
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 24; j++) {
				AxisDyadArray axisDyadArray = new AxisDyadArray(new CyclicSet(IntervalCycle.P_IC7, 0), 0,
						new CyclicSet(IntervalCycle.P_IC7, i), j);
				System.out.print(axisDyadArray);
				System.out.println(axisDyadArray.printArray());
				System.out.println(axisDyadArray.containsSumTetraChord(chord));
			}
		}
	}
	
	@Test
	public void allArraysInRange(){
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(10);
		
		EnumSet<IntervalCycle> set = EnumSet.range(IntervalCycle.P_IC1, IntervalCycle.P_IC7);
		for (IntervalCycle intervalCycle : set) {
			for (IntervalCycle intervalCycle2 : set) {
				for (int i = 0; i < intervalCycle2.getIntervalCycle().length; i++) {
//					for (int j = 0; j < intervalCycle.getIntervalCycle().length; j++) {
						AxisDyadArray axisDyadArray = new AxisDyadArray(new CyclicSet(intervalCycle, 0), 0,
								new CyclicSet(intervalCycle2, i), 0);
//						System.out.print(axisDyadArray);
//						if (axisDyadArray.containsSumTetraChord(chord)) {
//							System.out.print(axisDyadArray);
//							System.out.println();
//						}
						if (axisDyadArray.isCognateSet()) {
							System.out.print(axisDyadArray);
							System.out.println();
						}
//						System.out.println(axisDyadArray.printArray());
//					}
//					System.out.println("-----------------------------------------------------------------------");
				}
//				System.out.println("------------------------------------------------------");
			}
		}
	}

	@Test
	public void testGetName() {
		assertEquals("p0p7/p2p9", axisDyadArray.getName());
	}

	@Test
	public void testGetIntervalSystem() {
		assertEquals("7,7", axisDyadArray.getIntervalSystem());
	}

	@Test
	public void testGetSynopticMode() {
		assertEquals(0, axisDyadArray.getSynopticMode());
	}

	@Test
	public void testGetSynopticKey() {
		assertEquals(0, axisDyadArray.getSynopticMode());
	}

	@Test
	public void testIsDifferenceAlignment() {
		assertTrue(axisDyadArray.isDifferenceAlignment());
	}

	@Test
	public void testGetDifferences() {
		assertArrayEquals(new int[]{2, 0, 2}, axisDyadArray.getDifferences());
	}

	@Test
	public void testGetSums() {
		assertNull(axisDyadArray.getSums());
	}

	@Test
	public void testGetMode() {
		assertEquals("10,10", axisDyadArray.getMode());
	}

	@Test
	public void testGetKey() {
		assertEquals("2,4", axisDyadArray.getKey());
	}

	@Test
	public void testGetAxisDyadChord() {
		List<Integer> axisDyadChord = axisDyadArray.getAxisDyadChord(3);
		List<Integer> chord = new ArrayList<>();
		chord.add(9);
		chord.add(5);
		chord.add(7);
		chord.add(4);
		chord.add(2);
		assertTrue(axisDyadChord.containsAll(chord));
	}

	@Test
	public void testGetCyclicChord() {
		List<Integer> cyclicChord = axisDyadArray.getCyclicChord(3);
		List<Integer> chord = new ArrayList<>();
		chord.add(9);
		chord.add(7);
		chord.add(4);
		chord.add(2);
		assertTrue(cyclicChord.containsAll(chord));
	}

	@Test
	public void testGetSumTetraChordLeft() {
		List<Integer> sumTetraChord = axisDyadArray.getSumTetraChordLeft(3);
		List<Integer> chord = new ArrayList<>();
		chord.add(9);
		chord.add(7);
		chord.add(5);
		assertTrue(sumTetraChord.containsAll(chord));
	}

	@Test
	public void testGetSumTetraChordRight() {
		List<Integer> sumTetraChord = axisDyadArray.getSumTetraChordRight(3);
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(5);
		assertTrue(sumTetraChord.containsAll(chord));
	}

	@Test
	public void testGetAllAxisDyadChords() {
		List<List<Integer>> axisDyadChords = axisDyadArray.getAllAxisDyadChords();
		assertEquals(12, axisDyadChords.size());
	}

	@Test
	public void testGetAllCyclicChords() {
		List<List<Integer>> cyclicChords = axisDyadArray.getAllCyclicChords();
		assertEquals(12, cyclicChords.size());
	}

	@Test
	public void testGetAllSumTetraChordsLeft() {
		List<List<Integer>> sumTetraChords = axisDyadArray.getAllSumTetraChordsLeft();
		assertEquals(12, sumTetraChords.size());
	}

	@Test
	public void testGetAllSumTetraChordsRight() {
		List<List<Integer>> sumTetraChords = axisDyadArray.getAllSumTetraChordsRight();
		assertEquals(12, sumTetraChords.size());
	}

	@Test
	public void testGetAggregateSum() {
		assertEquals(6, axisDyadArray.getAggregateSum());
	}

	@Test
	public void testGetTonality() {
		assertEquals(2, axisDyadArray.getTonality());
	}

	@Test
	public void testIsCognateSet() {
		assertFalse(axisDyadArray.isCognateSet());
	}

	@Test
	public void testTranspose() {
		axisDyadArray.transpose(1);
		assertEquals("p2p9/p4p11", axisDyadArray.getName());
	}

	@Test
	public void testSemiTranspose() {
		axisDyadArray.semiTranspose(1);
		assertEquals("i3i10/i5i0", axisDyadArray.getName());
	}

	@Test
	public void testInverse() {
		//TODO: implement
		axisDyadArray.inverse(1);
		assertEquals("p2p9/p4p11", axisDyadArray.getName());
	}

	@Test
	public void testContainsAxisDyadChord() {
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(7);
		chord.add(5);
		assertFalse(axisDyadArray.containsAxisDyadChord(chord));
		chord.add(9);
		assertTrue(axisDyadArray.containsAxisDyadChord(chord));
	}
	
	@Test
	public void testContainsAllAxisDyadChord() {
		ArrayList<List<Integer>> chords = new ArrayList<List<Integer>>();
		List<Integer> chord = new ArrayList<>();
		chord.add(9);
		chord.add(3);
		chord.add(4);
		chord.add(11);
		chord.add(6);
		chords.add(chord);
		
		chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(7);
		chord.add(5);
		chord.add(9);
		chords.add(chord);
		assertTrue(axisDyadArray.containsAllAxisDyadChord(chords));
	}

	@Test
	public void testContainsSumTetraChord() {
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		assertFalse(axisDyadArray.containsSumTetraChord(chord));
		chord.add(10);
		assertTrue(axisDyadArray.containsSumTetraChord(chord));
	}

	@Test
	public void testContainsCyclicChord() {
		List<Integer> chord = new ArrayList<>();
		chord.add(2);
		chord.add(4);
		chord.add(7);
		assertFalse(axisDyadArray.containsCyclicChord(chord));
		chord.add(9);
		assertTrue(axisDyadArray.containsCyclicChord(chord));
	}

}
