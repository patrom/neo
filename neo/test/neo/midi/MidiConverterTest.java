package neo.midi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.DefaultConfig;
import neo.model.harmony.Harmony;
import neo.model.note.Note;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class MidiConverterTest extends AbstractTest {
	
	private MidiInfo midiInfo;
	@Autowired
	private MidiParser midiParser;
	
	@Before
	public void setUp() throws InvalidMidiDataException, IOException{
		midiInfo = midiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		melodies = midiInfo.getMelodies();
	}

	@Test
	public void testExtractNoteList() {
		Integer[] range = {5};
		List<Harmony> harmonies = MidiConverter.extractHarmony(melodies, range);
		harmonies.forEach(n -> LOGGER.info(n.getPosition() + ": " + n.getNotes()));
	}

	@Test
	public void testExtractNoteMapFromMelodies() {
		Map<Integer, List<Note>> chords = MidiConverter.extractNoteMapFromMelodies(melodies);
		chords.forEach((k, n) -> LOGGER.info(k + ": " + n));
	}
	
	@Test
	public void testUpdatePositionNotes(){
		MidiConverter.updatePositionNotes(melodies, midiInfo.getTimeSignature());
	}

}
