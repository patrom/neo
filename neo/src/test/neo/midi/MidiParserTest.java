package test.neo.midi;

import java.util.List;
import java.util.Map;

import neo.data.harmony.Harmony;
import neo.data.note.NotePos;
import neo.midi.MidiParser;

import org.junit.Test;

import test.neo.AbstractTest;

public class MidiParserTest extends AbstractTest {

	@Test
	public void testExtractNoteList() {
		List<Harmony> noteList = MidiParser.extractHarmony(motives, 5);
		noteList.forEach(n -> System.out.println(n.getPosition() + ": " + n.getNotes()));
	}

	@Test
	public void testExtractNoteMap() {
		Map<Integer, List<NotePos>> chords = MidiParser.extractNoteMap(motives);
		chords.forEach((k, n) -> System.out.println(k + ": " + n));
	}

}
