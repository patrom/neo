package neo.objective.melody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.MusicProperties;
import neo.midi.MidiConverter;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;

import org.junit.Before;
import org.junit.Test;

public class MelodiesTest extends AbstractTest{
	
	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTemplate.class.getName());
	
	private List<File> midiFiles;
	private MusicProperties musicProperties = new MusicProperties();
	
	@Before
	public void setUp() throws IOException, InvalidMidiDataException {
		midiFiles = Files.list(new File(MelodiesTest.class.getResource("/melodies").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
	}

	@Test
	public void testMelodies() throws InvalidMidiDataException, IOException {
		for (File file : midiFiles) {
			MidiInfo midiInfo = MidiParser.readMidi(file);
			LOGGER.fine(file.getName());
			List<Melody> melodies = midiInfo.getMelodies();
			MidiConverter.updatePositionNotes(melodies, midiInfo.getTimeSignature());
			for (Melody melody : melodies) {
				List<NotePos> notes = melody.getNotes();
				MelodicObjective melodicObjective = new MelodicObjective(musicProperties, null);
				double value = melodicObjective.evaluateMelody(notes, 1);
				LOGGER.info("Intervals : " + value);
				value = melodicObjective.evaluateTriadicValueMelody(notes);
				LOGGER.info("Triadic value: " + value);
				
			}
		}
	}

}
