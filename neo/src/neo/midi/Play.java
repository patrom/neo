package neo.midi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import org.junit.Before;
import org.junit.Test;

import neo.data.melody.Melody;
import neo.instrument.Ensemble;
import neo.instrument.Instrument;
import neo.instrument.KontaktLibAltViolin;
import neo.instrument.KontaktLibCello;
import neo.instrument.KontaktLibPiano;
import neo.instrument.KontaktLibViolin;
import neo.instrument.MidiDevice;
import neo.print.Display;

public class Play {
	
	private static Logger LOGGER = Logger.getLogger(Play.class.getName());
	
	private MidiInfo midiInfo;

	public static void playOnKontakt(List<Melody> motives, List<Instrument> instruments, float tempo) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(motives, instruments);
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}
	
	public static void playOnKontakt(List<Melody> motives, Instrument instrument, float tempo) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(motives, instrument);
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}
	
	public static void playMidiFilesOnKontaktFor(List<Instrument> instruments) throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(MidiParserTest.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			playOnKontakt(midiInfo.getMelodies(), instruments, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	public static void playMidiFilesOnKontaktFor(Instrument instrument) throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(MidiParserTest.class.getResource("/midi").getPath()).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			playOnKontakt(midiInfo.getMelodies(), instrument, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	public void testPlayOnKontakt() throws InvalidMidiDataException, IOException {
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		Play.playOnKontakt(midiInfo.getMelodies(), new KontaktLibPiano(0,0), midiInfo.getTempo());
	}
	
	public void testPlayOnKontaktListOfMelodyInstrumentFloat() throws InvalidMidiDataException, IOException, InterruptedException {
		midiInfo = MidiParser.readMidi(MidiParserTest.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		Play.playMidiFilesOnKontaktFor(Ensemble.getStringQuartet());
	}
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException, InterruptedException {
		Play play = new Play();
//		play.testPlayOnKontakt();//Kontakt channel 1
		play.testPlayOnKontaktListOfMelodyInstrumentFloat();
	}
	
}
