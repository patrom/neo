package neo.model.harmony;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ChordTest {

	@Test
	public void testTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(4);
		chord.addPitchClass(5);
		assertEquals(ChordType.CH3, chord.getChordType());
	}
	
	@Test
	public void testMajorTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(2);
		chord.addPitchClass(7);
		assertEquals("Chord type wrong", ChordType.MAJOR, chord.getChordType());
	}
	
	@Test
	public void testMinorTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		assertEquals("Chord type wrong", ChordType.MINOR, chord.getChordType());
	}
	
	@Test
	public void testCH3Type() {
		Chord chord = new Chord();
		chord.addPitchClass(2);
		chord.addPitchClass(0);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.CH3, chord.getChordType());
	}
	
	@Test
	public void testDOMType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(7);
		chord.addPitchClass(5);
		assertEquals("Chord type wrong", ChordType.DOM, chord.getChordType());
	}
	
	@Test
	public void testCH2Type() {
		Chord chord = new Chord();
		chord.addPitchClass(2);
		chord.addPitchClass(2);
		chord.addPitchClass(9);
		assertEquals("Chord type wrong", ChordType.CH2, chord.getChordType());
	}

}
