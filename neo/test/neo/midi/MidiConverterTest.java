package neo.midi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.data.harmony.Harmony;
import neo.data.note.NotePos;

import org.junit.Before;
import org.junit.Test;

public class MidiConverterTest extends AbstractTest {
	
	private MidiInfo midiInfo;
	
	@Before
	public void setUp() throws InvalidMidiDataException, IOException{
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		melodies = midiInfo.getMelodies();
	}

	@Test
	public void testExtractNoteList() {
		List<Harmony> harmonies = MidiConverter.extractHarmony(melodies, 5);
		harmonies.forEach(n -> LOGGER.info(n.getPosition() + ": " + n.getNotes()));
	}

	@Test
	public void testExtractNoteMap() {
		Map<Integer, List<NotePos>> chords = MidiConverter.extractNoteMap(melodies);
		chords.forEach((k, n) -> LOGGER.info(k + ": " + n));
	}
	
	@Test
	public void testUpdatePositionNotes(){
		MidiConverter.updatePositionNotes(melodies, midiInfo.getTimeSignature());
	}

}
