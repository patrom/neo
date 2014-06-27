package neo.data.setclass;

import java.util.List;
import java.util.Map;

import neo.AbstractTest;
import neo.data.note.NotePos;
import neo.data.setclass.PcSetUnorderedProperties;
import neo.midi.MidiParser;

import org.junit.Test;

public class PcSetUnorderedPropertiesTest extends AbstractTest{

	private PcSetUnorderedProperties pcSetUnorderedProperties;
	
	@Test
	public void testGetForteName() {
		Map<Integer, List<NotePos>> chords = MidiParser.extractNoteMap(motives);
		for (List<NotePos> notes : chords.values()) {
			int i = 0;
			int[] set = new int[notes.size()];
			for (NotePos notePos : notes) {
				set[i] = notePos.getPitchClass();
				i++;
			}
			pcSetUnorderedProperties = new PcSetUnorderedProperties(set);
			System.out.println(pcSetUnorderedProperties.getForteName());
		}
	}

}
