package neo.model.rhythm;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFrame;

import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import jm.util.View;
import neo.DefaultConfig;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.model.Motive;
import neo.model.note.Note;
import neo.objective.rhythm.RhythmObjective;
import neo.out.print.ScoreUtilities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class RhythmWeightMelodyTest extends JFrame{

	@Autowired
	private RhythmWeight rhythmWeight;
	@Autowired
	private MidiParser midiParser;
	@Autowired
	private ScoreUtilities scoreUtilities;
	@Autowired
	private RhythmObjective rhythmObjective;

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testRhythmWeight() throws InvalidMidiDataException, IOException {
		List<File> midiFiles = Files.list(new File("C:/Users/prombouts/git/neo/neo/resources/melodies/solo/").toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File file : midiFiles) {
			MidiInfo midiInfo = midiParser.readMidi(file);
			List<Note> notes = midiInfo.getMelodies().get(0).getNotes();
			rhythmWeight.setNotes(notes);
			rhythmWeight.updateRhythmWeight();
			System.out.println(file.getName());
			double profileAverage = rhythmObjective.getProfileAverage(notes, 3.0);
			System.out.println(profileAverage);
//			for (Note note : notes) {
//				System.out.print(note.getPitch() + ", " + note.getPositionWeight() + "; ");
//			}
//			System.out.println();
//			Score score = new Score();
//			Phrase phrase = scoreUtilities.createPhrase(notes);	
//			Part part = new Part(phrase);
//			score.add(part);
//			View.notate(score);
//			Play.midi(score, true);
		}
		
	}

}
