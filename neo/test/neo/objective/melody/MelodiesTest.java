package neo.objective.melody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import neo.AbstractTest;
import neo.DefaultConfig;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.generator.MusicProperties;
import neo.midi.MelodyInstrument;
import neo.midi.MidiConverter;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
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
public class MelodiesTest extends AbstractTest{
	
	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTemplate.class.getName());
	
	private List<File> midiFiles;
	@Autowired
	private MidiParser midiParser;
	
	@Before
	public void setUp() throws IOException, InvalidMidiDataException {
		midiFiles = Files.list(new File(MelodiesTest.class.getResource("/melodies").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
	}

	@Test
	public void testMelodies() throws InvalidMidiDataException, IOException {
		for (File file : midiFiles) {
			MidiInfo midiInfo = midiParser.readMidi(file);
			LOGGER.fine(file.getName());
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			MidiConverter.updatePositionNotes(melodies, midiInfo.getTimeSignature());
			for (MelodyInstrument melodyInstrument : melodies) {
				List<Note> notes = melodyInstrument.getNotes();
				MelodicObjective melodicObjective = new MelodicObjective();
				double value = melodicObjective.evaluateMelody(notes, 1);
				LOGGER.info("Intervals : " + value);
				value = melodicObjective.evaluateTriadicValueMelody(notes);
				LOGGER.info("Triadic value: " + value);
				List<Note> filteredNotes = melodicObjective.filterNotesWithWeightEqualToOrGreaterThan(notes, 0.5);
				double filteredValue = melodicObjective.evaluateMelody(filteredNotes, 1);
				LOGGER.info("filteredValue : " + filteredValue);
//				List<Note> notesLevel2 = melodicObjective.extractNotesOnLevel(notes, 2);
//				LOGGER.info("notesLevel2 : " + notesLevel2);
//				Score score = ScoreUtilities.createScoreMelodies(melodies);
//				View.notate(score);
//				jm.util.Play.midi(score, false);
			}
		}
	}

}
