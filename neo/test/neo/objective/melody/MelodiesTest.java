package neo.objective.melody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;

import jdk.nashorn.internal.ir.CallNode.EvalArgs;
import jm.music.data.Score;
import jm.util.View;
import neo.AbstractTest;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.MusicProperties;
import neo.midi.MelodyInstrument;
import neo.midi.MidiConverter;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.print.ScoreUtilities;

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
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			MidiConverter.updatePositionNotes(melodies, midiInfo.getTimeSignature());
			for (MelodyInstrument melodyInstrument : melodies) {
				List<Note> notes = melodyInstrument.getNotes();
				MelodicObjective melodicObjective = new MelodicObjective(musicProperties, null);
				double value = melodicObjective.evaluateMelody(notes, 1);
				LOGGER.info("Intervals : " + value);
				value = melodicObjective.evaluateTriadicValueMelody(notes);
				LOGGER.info("Triadic value: " + value);
				List<Note> filteredNotes = melodicObjective.filterNotesWithPositionWeightAbove(notes, 0.5);
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
