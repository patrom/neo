package neo.midi;

import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;

import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.instrument.Instrument;

public class MidiDevicesUtil {

	private static final int RESOLUTION = 12;
	private static Logger LOGGER = Logger.getLogger(MidiDevicesUtil.class.getName());

	
	public static void playOnDevice(Sequence sequence, float tempo, neo.instrument.MidiDevice kontact) {
		LOGGER.info("tempo:" + tempo);
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			try {
				System.out.println(infos[i]);
//				if (infos[i].getName().equals("Real Time Sequencer")) {
				if (infos[i].getName().equals(kontact.getName())) {
					final MidiDevice device = MidiSystem
							.getMidiDevice(infos[i]);
					device.open();

					final Sequencer sequencer = MidiSystem.getSequencer(false);

					/*
					 * There is a bug in the Sun jdk1.3/1.4. It prevents correct
					 * termination of the VM. So we have to exit ourselves. To
					 * accomplish this, we register a Listener to the Sequencer.
					 * It is called when there are "meta" events. Meta event 47
					 * is end of track.
					 */
					sequencer.addMetaEventListener(new MetaEventListener() {
						public void meta(MetaMessage event) {
//							if (event.getType() == 47) {
//								sequencer.close();
//								if (device != null) {
//									device.close();
//								}
//								System.exit(0);
//							}
						}
					});
					sequencer.open();
					
					sequencer.setSequence(sequence);

					Receiver receiver = device.getReceiver();
					Transmitter seqTransmitter = sequencer.getTransmitter();
					seqTransmitter.setReceiver(receiver);
					sequencer.setTempoInBPM(tempo);
					sequencer.start();
					break;
				}
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
	}

	public static Sequence createSequence(List<Melody> motives, List<Instrument> instruments)
			throws InvalidMidiDataException {
		int motiveSize = motives.size();
		if (motiveSize != instruments.size()) {
			throw new IllegalArgumentException("Motives and ranges not equal!");
		}
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motiveSize);
		for (int i = 0; i < motiveSize; i++) {
			List<NotePos> notes = motives.get(i).getMelody();
			createTrack(sequence, notes, instruments.get(i));
		}
		return sequence;
	}

	private static void createTrack(Sequence sequence, List<NotePos> notes, Instrument instrument)
			throws InvalidMidiDataException {
		Track track = sequence.createTrack();
		int prevPerfomance = 0;
		for (NotePos notePos : notes) {
			int performance = instrument.getPerformanceValue(notePos.getPerformance());
			if (performance != prevPerfomance) {
				MidiEvent changeEvent = createInstrumentChange(instrument, performance);
				track.add(changeEvent);
				prevPerfomance = performance;
			}
							
			MidiEvent eventOn = createNoteMidiEvent(ShortMessage.NOTE_ON, notePos, notePos.getPosition(), instrument.getChannel());
			track.add(eventOn);
			MidiEvent eventOff = createNoteMidiEvent(ShortMessage.NOTE_OFF, notePos, notePos.getPosition() + notePos.getLength(), instrument.getChannel());
			track.add(eventOff);	
		}
	}

	private static MidiEvent createInstrumentChange(Instrument instrument, int performance) throws InvalidMidiDataException {
		if (instrument.isKeySwitch()) {
			NotePos keySwitch = createKeySwitch(performance);
			MidiEvent change = createNoteMidiEvent(ShortMessage.NOTE_ON, keySwitch, 30, 0);
			return change;
		} else {
			MidiEvent event = createProgramChangeMidiEvent(instrument.getChannel(), 0, performance);
			return event;
		}
	}

	private static NotePos createKeySwitch(int performance) {
		NotePos keySwitch = new NotePos();
		keySwitch.setPitch(performance);
		keySwitch.setDynamic(80);
		return keySwitch;
	}

	public static Sequence createSequenceFromStructures(List<Melody> motives, List<Instrument> instruments)
			throws InvalidMidiDataException {
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motives.size());
		int i = 0;
		for (Melody motive : motives) {
			List<NotePos> notes = motive.getMelody();
			createTrack(sequence, notes, instruments.get(i));
			i++;
		}
		return sequence;
	}

	private static MidiEvent createNoteMidiEvent(int cmd, NotePos notePos, int position, int channel)
			throws InvalidMidiDataException {
		ShortMessage note = new ShortMessage();
		if (notePos.isRest()) {
			note.setMessage(cmd, channel, 0, 0);
		} else {
			note.setMessage(cmd, channel,
					notePos.getPitch(), notePos.getDynamic());
		}
		
		MidiEvent event = new MidiEvent(note, position);
		return event;
	}
	
	private static MidiEvent createProgramChangeMidiEvent(int channel, int pc, int position)
			throws InvalidMidiDataException {
		ShortMessage change = new ShortMessage();
		change.setMessage(ShortMessage.PROGRAM_CHANGE, channel, pc, 0);
		MidiEvent event = new MidiEvent(change, position);
		return event;
	}

}