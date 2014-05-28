package neo.midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.instrument.KontaktLibAltViolin;
import neo.instrument.KontaktLibCello;
import neo.instrument.KontaktLibViolin;
import neo.instrument.MidiDevice;

public class MidiParser {

	private static Logger LOGGER = Logger.getLogger(MidiParser.class.getName());
	public static final int TRACK_TIMESIGNATURE = 0x58;
	private static Random random = new Random();

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;
	
	public static void main(String[] args) throws InvalidMidiDataException, IOException {
		List<neo.instrument.Instrument> ranges = new ArrayList<>();
		ranges.add(new KontaktLibViolin(0, 1));
		ranges.add(new KontaktLibViolin(1, 2));
		ranges.add(new KontaktLibAltViolin(2, 2));
		ranges.add(new KontaktLibCello(3, 3));
		List<Melody> motives = readMidi(MidiParser.class.getResource("/Bach-choral227deel1.mid").getPath());
		
		Sequence seq = MidiDevicesUtil.createSequence(motives, ranges);
		float tempo = randomTempoFloat();
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
	}

	public static List<Harmony> extractHarmony(List<Melody> motives, int octave){
		Map<Integer, List<NotePos>> chords = extractNoteMap(motives);
		List<Harmony> list = new ArrayList<>();
		for (Entry<Integer, List<NotePos>> ch : chords.entrySet()) {
			Harmony noteList = new Harmony(ch.getKey(),ch.getValue().get(0).getLength()
					, ch.getValue(), new UniformPitchSpace(ch.getValue(),octave));
			list.add(noteList);
		}
		return list;
	}

	public static Map<Integer, List<NotePos>> extractNoteMap(List<Melody> motives) {
		Map<Integer, List<NotePos>> chords = new TreeMap<>();
		Set<Integer> positions = new TreeSet<>();
		for (Melody motive : motives) {
			List<NotePos> notes = motive.getNotes();
			for (NotePos notePos : notes) {
				positions.add(notePos.getPosition());
			}
		}
		int voice = 0;
		for (Melody motive : motives) {
			List<NotePos> notes = motive.getNotes();
			int melodyLength = notes.size() - 1;
			Iterator<Integer> iterator = positions.iterator();
			Integer position = iterator.next();
			for (int i = 0; i < melodyLength; i++) {
				NotePos firstNote = notes.get(i);
				NotePos secondNote = notes.get(i + 1);			
				while (position < secondNote.getPosition()) {
					addNoteToChordMap(chords, firstNote, voice);
					position = iterator.next();
				}
			}
			NotePos lastNote = notes.get(melodyLength);
			addNoteToChordMap(chords, lastNote, voice);
			voice++;
		}
		return chords;
	}

	private static void addNoteToChordMap(Map<Integer, List<NotePos>> chords, NotePos note,
			 int voice) {
		int position = note.getPosition();
		List<NotePos> chord = null;
		if (chords.containsKey(position)) {
			chord = chords.get(position);
		} else {
			chord = new ArrayList<>();
		}
		NotePos notePos = new NotePos(note.getPitchClass(), voice , position, note.getLength());
		chord.add(notePos);
		chords.put(position, chord);
	}
	
	
	public static List<Melody> readMidi(String path) throws InvalidMidiDataException, IOException{
		File file = new File(path);
		return readMidi(file);
	}

	public static List<Melody> readMidi(File midiFile)
			throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(midiFile);
		LOGGER.finer("Ticks: " + sequence.getResolution());
		LOGGER.finer("PPQ: " + sequence.PPQ);
		LOGGER.finer("DivisionType: " + sequence.getDivisionType());
		int trackNumber = 0;
		int voice = 0;
		int size = sequence.getTracks().length;
		voice = size - 1;
		int resolution = 12;
		Map<Integer, Melody> map = new TreeMap<Integer, Melody>();

		for (int j = 0; j < size; j++) {
			Track track = sequence.getTracks()[j];
			List<NotePos> notes = new ArrayList<NotePos>();
			trackNumber++;
			LOGGER.finer("Track " + trackNumber + ": size = " + track.size());
			for (int i = 0; i < track.size(); i++) {
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();

				if (message instanceof ShortMessage) {

					LOGGER.finer("Voice:" + voice);
					long ticks = Math
							.round(((double) event.getTick() / (double) sequence
									.getResolution()) * resolution);
					ShortMessage sm = (ShortMessage) message;

					// Er zijn twee manieren om een note-off commando te
					// versturen.
					// // Er bestaat een echt note-off commando, maar de meeste
					// apparaten versturen in plaats van een note-off commando
					// // een note-on commando met velocitywaarde 0. Een noot
					// die aangeschakeld wordt met velocitywaarde 0 is voor midi
					// hetzelfde als die noot uitschakelen.
					if (sm.getCommand() == ShortMessage.NOTE_ON
							&& sm.getData2() != 0) {
						LOGGER.finer("on: " + ticks + " ");
						LOGGER.finer("@" + event.getTick() + " ");
						LOGGER.finer("Pitch: " + sm.getData1() + " ");
						NotePos jNote = new NotePos();
						int key = sm.getData1();
						jNote.setPitch(key);
						jNote.setVoice(voice);
						jNote.setPosition((int) ticks);

						int velocity = sm.getData2();
						jNote.setDynamic(velocity);

						if (jNote != null) {
							notes.add(jNote);
						}
					}
					if (sm.getCommand() == ShortMessage.NOTE_OFF
							|| (sm.getCommand() == ShortMessage.NOTE_ON && sm
									.getData2() == 0)) {
						LOGGER.finer("off:" + ticks);
						LOGGER.finer(" @" + event.getTick() + " ");
						LOGGER.finer("Pitch: " + sm.getData1() + " ");
						int key = sm.getData1();
						int l = notes.size();
						for (int k = l - 1; k > -1; k--) {// find note on
															// belonging to note
															// off
							NotePos noteOn = notes.get(k);
							if (noteOn.getPitch() == key) {
								noteOn.setLength((int) ticks
										- noteOn.getPosition());
								break;
							}
						}
					}
				}
			}

			if (!notes.isEmpty()) {
				NotePos firstNote = notes.get(0);
				NotePos lastNote = notes.get(notes.size() - 1);
				int length = lastNote.getPosition() + lastNote.getLength()
						- firstNote.getPosition();
				Melody motive = new Melody(notes, length);
				map.put(voice, motive);
			}
			voice--;
		}
		List<Melody> motives = new ArrayList<Melody>(map.values());
		return motives;
	}
	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	public static float randomTempoFloat() {
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
