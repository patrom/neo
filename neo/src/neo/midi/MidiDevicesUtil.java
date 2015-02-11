package neo.midi;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;
import javax.sound.midi.spi.MidiFileWriter;

import jm.midi.event.EndTrack;
import jm.midi.event.KeySig;
import jm.midi.event.TempoEvent;
import jm.midi.event.TimeSig;

import org.springframework.stereotype.Component;

import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.out.instrument.Instrument;

@Component
public class MidiDevicesUtil {

	private static final int START_TICK = 0;

	 private static final int SET_TEMPO = 0x51;;

	private static Logger LOGGER = Logger.getLogger(MidiDevicesUtil.class.getName());

	private final int RESOLUTION = 12;
	
	public void playOnDevice(Sequence sequence, float tempo, neo.out.instrument.MidiDevice kontakt) {
		LOGGER.info("tempo:" + tempo);
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) {
			try {
				LOGGER.info(infos[i].toString());
				if (infos[i].getName().equals(kontakt.getName())) {
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
						public void meta(MetaMessage event) {//sequencer will close in case of looping files
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

	public Sequence createSequence(List<Melody> motives, List<Instrument> instruments)
			throws InvalidMidiDataException {
		int motiveSize = motives.size();
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motiveSize);
		for (int i = 0; i < motiveSize; i++) {
			List<Note> notes = motives.get(i).getMelodieNotes();
			createTrack(sequence, notes, instruments.get(i));
		}
		return sequence;
	}
	
	public Sequence createSequence(List<Melody> motives, Instrument instrument)
			throws InvalidMidiDataException {
		int motiveSize = motives.size();
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motiveSize);
		for (int i = 0; i < motiveSize; i++) {
			List<Note> notes = motives.get(i).getMelodieNotes();
			createTrack(sequence, notes, instrument);
		}
		return sequence;
	}
	
	public Sequence createSequence(List<MelodyInstrument> melodies)
			throws InvalidMidiDataException {
		int motiveSize = melodies.size();
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motiveSize);
		for (MelodyInstrument melodyInstrument : melodies) {
			if (melodyInstrument.getInstrument() != null) {
				createTrack(sequence, melodyInstrument.getNotes(), melodyInstrument.getInstrument());
			}
		}
		return sequence;
	}
	
	public Sequence createSequenceGeneralMidi(List<MelodyInstrument> melodies)
			throws InvalidMidiDataException {
		int motiveSize = melodies.size();
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motiveSize);
		for (MelodyInstrument melodyInstrument : melodies) {
			if (melodyInstrument.getInstrument() != null) {
				createTrackGeneralMidi(sequence, melodyInstrument.getNotes(), melodyInstrument.getInstrument());
			}
		}
		return sequence;
	}
	
//	public Sequence createSequenceFromNotes(List<Note> notes, Instrument intstrument) throws InvalidMidiDataException {
//		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, 1);
//		createTrack(sequence, notes, intstrument);
//		return sequence;
//	}
	
	private void createTrack(Sequence sequence, List<Note> notes, Instrument instrument)
			throws InvalidMidiDataException {
		Track track = sequence.createTrack();
		int prevPerfomance = 0;
		for (Note notePos : notes) {
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
	
	public int write(DataOutputStream dos) throws IOException{
		int bytes_out = jm.midi.MidiUtil.writeVarLength(0,dos); 
		dos.writeByte(0xFF);
		dos.writeByte(0x58);	
		bytes_out += jm.midi.MidiUtil.writeVarLength(4,dos); 
		dos.writeByte((byte) 3); //numerator
		int num = 4; //this.denominator;
        int cnt = 0;
		while(num%2==0){num /= 2;cnt++;}
        dos.writeByte((byte)cnt);
        dos.writeByte(0x18);
		dos.writeByte(0x08);
		return bytes_out+6;
	} 
	
	// write a meta event to current track at certain tick
    private void writeMetaEvent(int id, byte[] val, int b3, int tick, Track currentTrack) {
        MetaMessage mt = new MetaMessage();
        try {
            mt.setMessage(id, val, b3);
        } catch (InvalidMidiDataException e) {
            System.out.println(e.toString());
        }
        MidiEvent me = new MidiEvent(mt, (long) tick);
        currentTrack.add(me);
    }
	
	private void createTrackGeneralMidi(Sequence sequence, List<Note> notes, Instrument instrument)
			throws InvalidMidiDataException {
		Track track = sequence.createTrack();
		
		// general MIDI configuration
        // have to do (byte) casts because Java has unsigned int problems
//        byte[] b = {(byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7};
//        SysexMessage sm = new SysexMessage();
//        sm.setMessage(b, 6);
//
//        MidiEvent me = new MidiEvent(sm, START_TICK);
//        track.add(me);
//
//        // calculate tempo in bytes
//        float microPerMinute = 60000000;
//        int microPerPulse = (int) (microPerMinute / 50);
//        byte[] bytes = ByteBuffer.allocate(4).putInt(microPerPulse).array();
//
//        // three bytes represent number of microseconds per pulse
//        byte[] bt = {bytes[1], bytes[2], bytes[3]};
//        writeMetaEvent(SET_TEMPO, bt, 3, START_TICK, track);
        
//		MetaMessage message = new MetaMessage();
//		byte b1 = (byte) 3;
//		byte b2 = (byte) 2;
//		byte b3 = 0x18;
//		byte b4 = 0x08;
//		byte[] data = ByteBuffer.allocate(4).put(0, b1).put(1, b2).put(1, b3).put(3, b4).array();
//		for (byte b : data) {
//			System.out.format("0x%x ", b);
//		}
////		strMessage = "Time Signature: "
////				+ (abData[0] & 0xFF) + "/" + (1 << (abData[1] & 0xFF))
////				+ ", MIDI clocks per metronome tick: " + (abData[2] & 0xFF)
////				+ ", 1/32 per 24 MIDI clocks: " + (abData[3] & 0xFF);
//		byte[] arr = ByteBuffer.allocate(1).put(0,(byte) (0x58 & 0x00ff)).array();
//		int type = ByteBuffer.wrap(arr).getInt();
//		message.setMessage(type, data, 1);//time signature
//
//		MidiEvent ev = new MidiEvent(message, 0);
//		track.add(ev);
		MidiEvent event = createGeneralMidiEvent(instrument);
		track.add(event);
		for (Note notePos : notes) {
			MidiEvent eventOn = createNoteMidiEvent(ShortMessage.NOTE_ON, notePos, notePos.getPosition(), instrument.getChannel());
			track.add(eventOn);
			MidiEvent eventOff = createNoteMidiEvent(ShortMessage.NOTE_OFF, notePos, notePos.getPosition() + notePos.getLength(), instrument.getChannel());
			track.add(eventOff);	
		}
	}

	private MidiEvent createGeneralMidiEvent(Instrument instrument)
			throws InvalidMidiDataException {
		ShortMessage change = new ShortMessage();
		change.setMessage(ShortMessage.PROGRAM_CHANGE, instrument.getChannel(), instrument.getGeneralMidi().getEvent(), 0);
		MidiEvent event = new MidiEvent(change, 0);
		return event;
	}

	private MidiEvent createInstrumentChange(Instrument instrument, int performance) throws InvalidMidiDataException {
		if (instrument.isKeySwitch()) {
			Note keySwitch = createKeySwitch(performance);
			MidiEvent change = createNoteMidiEvent(ShortMessage.NOTE_ON, keySwitch, 30, instrument.getChannel());
			return change;
		} else {
			MidiEvent event = createProgramChangeMidiEvent(instrument.getChannel(), instrument.getGeneralMidi().getEvent(), performance);
			return event;
		}
	}

	private Note createKeySwitch(int performance) {
		Note keySwitch = new Note();
		keySwitch.setPitch(performance);
		keySwitch.setDynamic(80);
		return keySwitch;
	}

	public Sequence createSequenceFromStructures(List<Melody> motives, List<Instrument> instruments)
			throws InvalidMidiDataException {
		Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION, motives.size());
		int i = 0;
		for (Melody motive : motives) {
			List<Note> notes = motive.getMelodieNotes();
			createTrack(sequence, notes, instruments.get(i));
			i++;
		}
		return sequence;
	}

	private MidiEvent createNoteMidiEvent(int cmd, Note notePos, int position, int channel)
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
	
	private MidiEvent createProgramChangeMidiEvent(int channel, int pc, int position)
			throws InvalidMidiDataException {
		ShortMessage change = new ShortMessage();
		change.setMessage(ShortMessage.PROGRAM_CHANGE, channel, pc, 0);
		MidiEvent event = new MidiEvent(change, position);
		return event;
	}
	
	public void write(Sequence in, String ouputPath) throws IOException{
		MidiSystem.write(in, 1, new File(ouputPath));//1 = multi-track
	}

}
