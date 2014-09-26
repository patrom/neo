package neo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.swing.JFrame;

import neo.evaluation.FitnessObjectiveValues;
import neo.generator.MusicProperties;
import neo.midi.MelodyInstrument;

import org.junit.Before;

public abstract class AbstractTest extends JFrame{
	
	protected static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());
	
	protected List<MelodyInstrument> melodies = new ArrayList<>();
	protected FitnessObjectiveValues objectives;
	protected MusicProperties musicProperties;
	
	@Before
	public void abstractSetUp() throws InvalidMidiDataException, IOException{
		musicProperties = new MusicProperties();
//		motives = MidiParser.readMidi("/Users/parm/git/neo/neo/src/test/neo/Bach-choral227 deel1.mid");
	}

}
