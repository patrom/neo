package neo.midi;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import neo.instrument.KontaktLibPiano;

import org.junit.Before;
import org.junit.Test;

public class PlayTest {
	
	private MidiInfo midiInfo;

	@Before
	public void setUp() throws InvalidMidiDataException, IOException {
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
	}

	@Test
	public void testPlayOnKontakt() throws InvalidMidiDataException, IOException {
		Play.playOnKontakt(midiInfo.getMelodies(), new KontaktLibPiano(0,0), midiInfo.getTempo());
	}
	
	@Test
	public void testPlayOnKontaktListOfMelodyInstrumentFloat() throws InvalidMidiDataException, IOException {
		Play.playMidiFilesOnKontaktFor("/", new KontaktLibPiano(0,0));
	}

}
