package neo.data.setclass;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.data.note.NotePos;
import neo.midi.MidiConverter;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.midi.MidiParserTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PcSetUnorderedPropertiesTest extends AbstractTest{

	private PcSetUnorderedProperties pcSetUnorderedProperties;
	private MidiInfo midiInfo;
	
	@Before
	public void setUp() throws InvalidMidiDataException, IOException {
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		melodies = midiInfo.getMelodies();
	}
	
	@Test
	public void testGetForteName() {
		Map<Integer, List<NotePos>> chords = MidiConverter.extractNoteMap(melodies);
		for (List<NotePos> notes : chords.values()) {
			int i = 0;
			int[] set = new int[notes.size()];
			for (NotePos notePos : notes) {
				set[i] = notePos.getPitchClass();
				i++;
			}
			pcSetUnorderedProperties = new PcSetUnorderedProperties(set);
			LOGGER.info(pcSetUnorderedProperties.getForteName());
		}
	}
	
	@Test
	public void testForteName() {
		int[] set = new int[3];
		set[0] = 0;
		set[1] = 4;
		set[2] = 7;
		pcSetUnorderedProperties = new PcSetUnorderedProperties(set);
		Assert.assertEquals("3-11", pcSetUnorderedProperties.getForteName());
	}

}
