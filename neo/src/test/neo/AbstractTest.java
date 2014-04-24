package test.neo;

import java.io.IOException;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.sound.midi.InvalidMidiDataException;

import neo.data.melody.Melody;
import neo.evaluation.FitnessObjectiveValues;
import neo.evaluation.MusicProperties;
import neo.midi.MidiParser;

import org.junit.Before;

public abstract class AbstractTest {
	
	protected List<Melody> motives;
	protected FitnessObjectiveValues objectives;
	protected MusicProperties musicProperties;
	
	@Before
	public void abstractSetUp() throws InvalidMidiDataException, IOException{
		musicProperties = new MusicProperties();
//		motives = MidiParser.readMidi("/Users/parm/git/neo/neo/src/test/neo/Bach-choral227 deel1.mid");
		motives = MidiParser.readMidi("/Users/parm/comp/moga/music/test3.mid");
	}

	public AbstractTest(){
		try {
			configureLogger(Level.INFO);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private  void configureLogger(Level level) throws IOException {
		Logger topLogger = Logger.getLogger("");
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(level);
		topLogger.addHandler(ch);
		topLogger.setLevel(level);
		FileHandler fileTxt = new FileHandler("Logging.txt");
		SimpleFormatter formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		topLogger.addHandler(fileTxt);
	}
}
