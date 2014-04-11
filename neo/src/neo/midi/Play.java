package neo.midi;

import java.util.List;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import neo.data.note.Motive;
import neo.instrument.Instrument;
import neo.instrument.MidiDevice;

public class Play {

	private static Random random = new Random();
	
	public static void playOnKontakt(List<Motive> motives, List<Instrument> instruments) throws InvalidMidiDataException {
		Sequence seq = MidiDevicesUtil.createSequence(motives, instruments);
		MidiDevicesUtil.playOnDevice(seq, randomTempoFloat(), MidiDevice.KONTACT);
	}
	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	private static float randomTempoFloat() {
		float r = random.nextFloat();
		if (r < 0.5) {
			r = (r * 100) + 100;
		} else {
			r = r * 100;
		}
		//tempo between 50 - 150
		return r;
	}
	
}
