package neo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import jm.util.View;
import neo.generator.MusicProperties;
import neo.midi.HarmonyInstrument;
import neo.midi.MelodyInstrument;
import neo.midi.MidiDevicesUtil;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.model.note.Note;
import neo.out.arrangement.Accompagnement;
import neo.out.arrangement.Arrangement;
import neo.out.instrument.Ensemble;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibPiano;
import neo.out.instrument.KontaktLibViolin;
import neo.out.instrument.MidiDevice;
import neo.out.print.ScoreUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(DefaultConfig.class)
public class PlayApplication extends JFrame implements CommandLineRunner{
	
	private static Logger LOGGER = Logger.getLogger(PlayApplication.class.getName());
	
	@Autowired
	private MidiParser midiParser;
	@Autowired
	private MidiDevicesUtil midiDevicesUtil;
	@Autowired
	private MusicProperties musicProperties;
	@Autowired
	private Arrangement arrangement;
	@Autowired
	private ScoreUtilities scoreUtilities;
	
	public static void main(final String[] args) {
	 	SpringApplication app = new SpringApplication(PlayApplication.class);
	    app.setShowBanner(false);
	    app.run(args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		playMidiFilesOnKontaktFor();
	}
	
	public void playMidiFilesOnKontaktFor() throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File("C:/Dev/git/neo/neo/resources/midi").toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = midiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
		
			List<MelodyInstrument> playList = playOnInstruments(midiInfo, Ensemble.getStringQuartet());
			playOnKontakt(melodies, midiInfo.getTempo());
			View.notate(scoreUtilities.createScoreFromMelodyInstrument(playList, midiInfo.getTempo()));
//			write(melodies, "resources/transform/" + midiFile.getName());
//			Score score = new Score();
//			Read.midi(score, midiFile.getAbsolutePath());
//			View.notate(score);
			Thread.sleep(13000);
		}
	}
	
	public List<List<Note>> chordal(List<Note> harmonyNotes) {
		ArrayList<List<Note>> list = new ArrayList<List<Note>>();
		list.add(harmonyNotes);
		return list;
	}

	private List<MelodyInstrument> playOnInstrumentsAccomp(MidiInfo midiInfo) {
		List<MelodyInstrument> playList = new ArrayList<>();
		List<MelodyInstrument> melodies = midiInfo.getMelodies();
		
		//accompagnement
		List<HarmonyInstrument> harmonyPositions = midiInfo.getHarmonyPositions(musicProperties.getChordSize());
		Integer[] compPattern = {6,12,18,24};
		List<Integer[]> compPatterns = new ArrayList<Integer[]>();
		compPatterns.add(compPattern);
		Accompagnement[] compStrategy = {Accompagnement::arpeggio};
		List<Note> accompagnement = arrangement.accompagnement(harmonyPositions, compPatterns, compStrategy);
		MelodyInstrument accomp = new MelodyInstrument(accompagnement, melodies.size() + 1);
		accomp.setInstrument(new KontaktLibPiano(0, 0));
		arrangement.transpose(accomp.getNotes(), -12);
		accomp.addNotes(melodies.get(0).getNotes());//add bass notes
		playList.add(accomp);
//		arrangement.applyFixedPattern(melodies.get(0).getNotes(), 6);
		//harmony
		melodies.get(1).setInstrument(new KontaktLibPiano(0, 0));
		melodies.get(2).setInstrument(new KontaktLibPiano(0, 0));
		arrangement.transpose(melodies.get(3).getNotes(), -12);
		melodies.get(3).setInstrument(new KontaktLibPiano(0, 0));
		//melody
		arrangement.transpose(melodies.get(7).getNotes(), -12);
		melodies.get(7).setInstrument(new KontaktLibViolin(0, 1));
		playList.add(melodies.get(7));
		return playList;
	}
	
	
	private List<MelodyInstrument> playOnInstruments(MidiInfo midiInfo, List<Instrument> instruments) {
		List<MelodyInstrument> playList = new ArrayList<>();
		List<MelodyInstrument> melodies = midiInfo.getMelodies();
		for (int i = 0; i < instruments.size(); i++) {
			melodies.get(i).setInstrument(instruments.get(i));
			playList.add(melodies.get(i));
		}
		return playList;
	}
	
	

	private void playOnKontakt(List<MelodyInstrument> melodies,
			float tempo) throws InvalidMidiDataException {
		Sequence seq = midiDevicesUtil.createSequence(melodies);
		midiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTAKT);
		
	}

	public void playMidiFilesOnKontaktFor(Instrument instrument) throws IOException, InvalidMidiDataException, InterruptedException {
		
//		List<File> midiFiles = Files.list(new File(PlayApplication.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		List<File> midiFiles = Files.list(new File("C:/Dev/git/neo/neo/resources/midi").toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = midiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			melodies.forEach(m -> m.setInstrument(instrument));
			playOnKontakt(melodies, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	public void testPlayOnKontakt() throws InvalidMidiDataException, IOException {
		MidiInfo midiInfo = midiParser.readMidi(PlayApplication.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		playOnKontakt(midiInfo.getMelodies(), midiInfo.getTempo());
	}
	
	private void write(List<MelodyInstrument> melodies, String outputPath) throws InvalidMidiDataException, IOException{
		Sequence seq = midiDevicesUtil.createSequence(melodies);
		midiDevicesUtil.write(seq, outputPath);
	}
	
}
