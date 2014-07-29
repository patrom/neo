package neo.data.harmony;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ChordTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(4);
		chord.addPitchClass(5);
		assertEquals("Chord type wrong", ChordType.CH3, chord.getChordType());
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

}