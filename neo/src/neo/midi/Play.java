package neo.midi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import neo.instrument.Ensemble;
import neo.instrument.Instrument;
import neo.instrument.KontaktLibPiano;
import neo.instrument.KontaktLibViolin;
import neo.instrument.MidiDevice;

public class Play {
	
	private static Logger LOGGER = Logger.getLogger(Play.class.getName());
	
	private MidiInfo midiInfo;

	public static void playMidiFilesOnKontaktFor() throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(Play.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			melodies.get(0).setInstrument(new KontaktLibPiano(0, 0));
			melodies.get(1).setInstrument(new KontaktLibPiano(0, 0));
			melodies.get(2).setInstrument(new KontaktLibPiano(0, 0));
			
			melodies.get(5).setInstrument(new KontaktLibViolin(0, 1));
			playOnKontakt(melodies, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	private static void playOnKontakt(List<MelodyInstrument> melodies,
			float tempo) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(melodies);
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}

	public static void playMidiFilesOnKontaktFor(Instrument instrument) throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(Play.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			melodies.forEach(m -> m.setInstrument(instrument));
			playOnKontakt(melodies, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	public void testPlayOnKontakt() throws InvalidMidiDataException, IOException {
		midiInfo = MidiParser.readMidi(Play.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		Play.playOnKontakt(midiInfo.getMelodies(), midiInfo.getTempo());
	}
	
	public void testPlayOnKontaktListOfMelodyInstrumentFloat() throws InvalidMidiDataException, IOException, InterruptedException {
		midiInfo = MidiParser.readMidi(Play.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		Play.playMidiFilesOnKontaktFor();
	}
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException, InterruptedException {
		Play play = new Play();
//		play.testPlayOnKontakt();//Kontakt channel 1
		play.playMidiFilesOnKontaktFor();
	}
	
}
