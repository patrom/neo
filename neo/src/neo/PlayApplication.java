package neo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import neo.midi.MelodyInstrument;
import neo.midi.MidiDevicesUtil;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.model.note.Note;
import neo.out.arrangement.Arrangement;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibPiano;
import neo.out.instrument.KontaktLibViolin;
import neo.out.instrument.MidiDevice;

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
	private Arrangement arrangement;
	
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
		List<File> midiFiles = Files.list(new File(PlayApplication.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = midiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			playOnInstruments(melodies);
			playOnKontakt(melodies, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}

	private void playOnInstruments(List<MelodyInstrument> melodies) {
		List<Note> transformedNotes = transformNotes(melodies.get(0).getNotes());
		melodies.get(0).setNotes(transformedNotes);
		
		melodies.get(0).setInstrument(new KontaktLibPiano(0, 0));
		melodies.get(1).setInstrument(new KontaktLibPiano(0, 0));
		melodies.get(2).setInstrument(new KontaktLibPiano(0, 0));
		
		melodies.get(5).setInstrument(new KontaktLibViolin(0, 1));
	}
	
	private List<Note> transformNotes(List<Note> notes) {
		return arrangement.applyFixedPattern(notes, 6);
	}

	private void playOnKontakt(List<MelodyInstrument> melodies,
			float tempo) throws InvalidMidiDataException {
		Sequence seq = midiDevicesUtil.createSequence(melodies);
		midiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}

	public void playMidiFilesOnKontaktFor(Instrument instrument) throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(PlayApplication.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
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
	
	
}
