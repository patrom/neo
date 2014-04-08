package test.neo.midi;

import java.util.List;
import java.util.Map;

import jm.music.data.Score;
import jm.util.View;
import neo.data.note.Motive;
import neo.data.note.NoteList;
import neo.data.note.NotePos;
import neo.generator.Generator;
import neo.midi.MidiParser;
import neo.score.ScoreUtilities;

import org.junit.Test;

import test.neo.AbstractTest;

public class MidiParserTest extends AbstractTest {

	@Test
	public void testExtractNoteList() {
		List<NoteList> noteList = MidiParser.extractNoteList(motives);
		noteList.forEach(n -> System.out.println(n.getPosition() + ": " + n.getNotes()));
	}

	@Test
	public void testExtractNoteMap() {
		Map<Integer, List<NotePos>> chords = MidiParser.extractNoteMap(motives);
		chords.forEach((k, n) -> System.out.println(k + ": " + n));
	}

}
