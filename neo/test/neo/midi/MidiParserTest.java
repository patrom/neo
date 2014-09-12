package neo.midi;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;

import jm.music.data.Score;
import jm.util.View;
import neo.AbstractTest;
import neo.evaluation.MusicProperties;
import neo.print.ScoreUtilities;

import org.junit.Before;
import org.junit.Test;

public class MidiParserTest extends AbstractTest {
	
	private MidiInfo midiInfo;
	
	@Before
	public void setUp() throws InvalidMidiDataException, IOException{
		musicProperties = new MusicProperties();
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		melodies = midiInfo.getMelodies();
	}
	
	@Test
	public void testMidiInfo() {
		melodies.forEach(n -> System.out.println(n.getNotes()));
		String timeSignature = midiInfo.getTimeSignature();
		System.out.println(timeSignature);
		System.out.println(midiInfo.getTempo());
		String[] split = timeSignature.split("/");
	}

}
