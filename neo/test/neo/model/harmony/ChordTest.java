package neo.model.harmony;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChordTest {

	@Test
	public void testTriadType() {
		Chord chord = new Chord(-1);
		chord.addPitchClass(11);
		chord.addPitchClass(4);
		chord.addPitchClass(5);
		assertEquals(ChordType.CH3, chord.getChordType());
	}
	
	@Test
	public void testMajorTriadType() {
		Chord chord = new Chord(2);
		chord.addPitchClass(2);
		chord.addPitchClass(6);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MAJOR, chord.getChordType());
	}
	
	@Test
	public void testMajorInv1TriadType() {
		Chord chord = new Chord(6);
		chord.addPitchClass(2);
		chord.addPitchClass(6);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MAJOR_1, chord.getChordType());
	}
	
	@Test
	public void testMajorInv2TriadType() {
		Chord chord = new Chord(9);
		chord.addPitchClass(2);
		chord.addPitchClass(6);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MAJOR_2, chord.getChordType());
	}
	
	@Test
	public void testMinorTriadType() {
		Chord chord = new Chord(2);
		chord.addPitchClass(2);
		chord.addPitchClass(5);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MINOR, chord.getChordType());
	}
	
	@Test
	public void testMinorInv1TriadType() {
		Chord chord = new Chord(5);
		chord.addPitchClass(2);
		chord.addPitchClass(5);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MINOR_1, chord.getChordType());
	}
	
	@Test
	public void testMinorInv2TriadType() {
		Chord chord = new Chord(9);
		chord.addPitchClass(2);
		chord.addPitchClass(5);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.MINOR_2, chord.getChordType());
	}
	
	@Test
	public void testCH3Type() {
		Chord chord = new Chord(-1);
		chord.addPitchClass(2);
		chord.addPitchClass(0);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.CH3, chord.getChordType());
	}
	
	@Test
	public void testDOMType() {
		Chord chord = new Chord(-1);
		chord.addPitchClass(11);
		chord.addPitchClass(7);
		chord.addPitchClass(5);
		assertEquals("Chord type wrong", ChordType.DOM, chord.getChordType());
	}
	
	@Test
	public void testCH2Type() {
		Chord chord = new Chord(-1);
		chord.addPitchClass(2);
		chord.addPitchClass(2);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.CH2, chord.getChordType());
	}
	
	@Test
	public void testMajor7Type() {
		Chord chord = new Chord(0);
		chord.addPitchClass(0);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		chord.addPitchClass(11);
		assertEquals("Chord type wrong", ChordType.MAJOR7, chord.getChordType());
	}
	
	@Test
	public void testMajor7Inv1Type() {
		Chord chord = new Chord(4);
		chord.addPitchClass(0);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		chord.addPitchClass(11);
		assertEquals("Chord type wrong", ChordType.MAJOR7_1, chord.getChordType());
	}
	
	@Test
	public void testMajor7Inv2Type() {
		Chord chord = new Chord(7);
		chord.addPitchClass(0);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		chord.addPitchClass(11);
		assertEquals("Chord type wrong", ChordType.MAJOR7_2, chord.getChordType());
	}
	
	@Test
	public void testMajor7Inv3Type() {
		Chord chord = new Chord(11);
		chord.addPitchClass(0);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		chord.addPitchClass(11);
		assertEquals("Chord type wrong", ChordType.MAJOR7_3, chord.getChordType());
	}
	
	@Test
	public void testMinor7Type() {
		Chord chord = new Chord(0);
		chord.addPitchClass(0);
		chord.addPitchClass(3);
		chord.addPitchClass(7);
		chord.addPitchClass(10);
		assertEquals("Chord type wrong", ChordType.MINOR7, chord.getChordType());
	}
	
	@Test
	public void testMinor7Inv1Type() {
		Chord chord = new Chord(3);
		chord.addPitchClass(0);
		chord.addPitchClass(3);
		chord.addPitchClass(7);
		chord.addPitchClass(10);
		assertEquals("Chord type wrong", ChordType.MINOR7_1, chord.getChordType());
	}
	
	@Test
	public void testMinor7Inv2Type() {
		Chord chord = new Chord(7);
		chord.addPitchClass(0);
		chord.addPitchClass(3);
		chord.addPitchClass(7);
		chord.addPitchClass(10);
		assertEquals("Chord type wrong", ChordType.MINOR7_2, chord.getChordType());
	}
	
	@Test
	public void testMinor7Inv3Type() {
		Chord chord = new Chord(10);
		chord.addPitchClass(0);
		chord.addPitchClass(3);
		chord.addPitchClass(7);
		chord.addPitchClass(10);
		assertEquals("Chord type wrong", ChordType.MINOR7_3, chord.getChordType());
	}

}
