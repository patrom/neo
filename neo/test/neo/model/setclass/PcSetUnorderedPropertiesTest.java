package neo.model.setclass;

import static java.util.stream.Collectors.toSet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.midi.MidiConverter;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.midi.MidiParserTest;
import neo.model.note.Note;
import neo.model.setclass.PcSetUnorderedProperties;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PcSetUnorderedPropertiesTest extends AbstractTest{

	private PcSetUnorderedProperties pcSetUnorderedProperties;
	private MidiInfo midiInfo;
	
	@Before
	public void setUp() throws InvalidMidiDataException, IOException {
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/Bach-choral227deel1.mid").getPath());
		melodies = midiInfo.getMelodies();
	}
	
	@Test
	public void testGetForteName() {
		Map<Integer, List<Note>> chords = MidiConverter.extractNoteMap(melodies);
		chords.forEach((i,chord) -> {
			java.util.Set<Integer> pitchClasses = chord.stream()
					.map(note -> note.getPitchClass())
					.collect(toSet());
			if (pitchClasses.size() > 2) {// set class minimum size is 3
				Integer[] integerArray = pitchClasses.toArray(new Integer[pitchClasses.size()]);
				int[] set = ArrayUtils.toPrimitive(integerArray);
				pcSetUnorderedProperties = new PcSetUnorderedProperties(set);
				LOGGER.info(pcSetUnorderedProperties.getForteName());
			}
		});
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
