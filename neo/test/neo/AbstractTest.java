package neo;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.sound.midi.InvalidMidiDataException;

import neo.data.melody.Melody;
import neo.evaluation.FitnessObjectiveValues;
import neo.evaluation.MusicProperties;
import neo.log.LogConfig;
import neo.midi.MidiParser;

import org.junit.Before;

public abstract class AbstractTest {
	
	protected List<Melody> motives;
	protected FitnessObjectiveValues objectives;
	protected MusicProperties musicProperties;
	private LogConfig LogConfig = new LogConfig();
	
	@Before
	public void abstractSetUp() throws InvalidMidiDataException, IOException{
		musicProperties = new MusicProperties();
//		motives = MidiParser.readMidi("/Users/parm/git/neo/neo/src/test/neo/Bach-choral227 deel1.mid");
//		motives = MidiParser.readMidi("/Users/parm/comp/moga/music/test3.mid");
	}

	public AbstractTest() {
		LogConfig.configureLogger(Level.INFO);
	}

}
