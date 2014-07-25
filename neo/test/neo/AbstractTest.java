package neo;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;

import neo.data.melody.Melody;
import neo.evaluation.FitnessObjectiveValues;
import neo.evaluation.MusicProperties;
import neo.log.LogConfig;
import neo.midi.MidiParser;
import neo.print.Display;

import org.junit.Before;

public abstract class AbstractTest {
	
	protected static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());
	
	protected List<Melody> melodies;
	protected FitnessObjectiveValues objectives;
	protected MusicProperties musicProperties;
	private LogConfig LogConfig = new LogConfig();
	
	@Before
	public void abstractSetUp() throws InvalidMidiDataException, IOException{
		musicProperties = new MusicProperties();
//		motives = MidiParser.readMidi("/Users/parm/git/neo/neo/src/test/neo/Bach-choral227 deel1.mid");
	}

	public AbstractTest() {
		LogConfig.configureLogger(Level.FINE);
	}

}
