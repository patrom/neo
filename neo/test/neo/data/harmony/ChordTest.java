package neo.data.harmony;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import junit.framework.Assert;
import neo.objective.harmony.Chord;
import neo.objective.harmony.ChordType;

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
		assertEquals("Chord type wrong",  ChordType.CH3, chord.getChordType());
	}
	
	@Test
	public void testMajorTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(2);
		chord.addPitchClass(7);
		assertEquals("Chord type wrong",  ChordType.MAJOR, chord.getChordType());
	}
	
	@Test
	public void testMinorTriadType() {
		Chord chord = new Chord();
		chord.addPitchClass(11);
		chord.addPitchClass(4);
		chord.addPitchClass(7);
		assertEquals("Chord type wrong",  ChordType.MINOR, chord.getChordType());
	}

}
