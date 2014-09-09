package neo.midi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import neo.data.melody.Melody;
import neo.instrument.Instrument;
import neo.instrument.MidiDevice;

public class Play {

	public static void playOnKontakt(List<Melody> motives, List<Instrument> instruments, float tempo) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(motives, instruments);
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}
	
	public static void playOnKontakt(List<Melody> motives, Instrument instrument, float tempo) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(motives, instrument);
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}
	
	public static void playMidiFilesOnKontaktFor(String path, List<Instrument> instruments) throws IOException, InvalidMidiDataException {
		List<File> midiFiles = Files.list(new File(path).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			playOnKontakt(midiInfo.getMelodies(), instruments, midiInfo.getTempo());
		}
	}
	
	public static void playMidiFilesOnKontaktFor(String path, Instrument instrument) throws IOException, InvalidMidiDataException {
		List<File> midiFiles = Files.list(new File(path).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			MidiInfo midiInfo = MidiParser.readMidi(midiFile);
			playOnKontakt(midiInfo.getMelodies(), instrument, midiInfo.getTempo());
		}
	}
	
}
