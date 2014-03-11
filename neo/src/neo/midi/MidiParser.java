package neo.midi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

import neo.harmony.Chord;
import neo.note.Motive;
import neo.note.NotePos;
import neo.voiceleading.VoiceLeading;
import neo.voiceleading.VoiceLeadingSize;

public class MidiParser {

	private static Logger LOGGER = Logger.getLogger(MidiParser.class.getName());
	public static final int TRACK_TIMESIGNATURE = 0x58;

	public static final int NOTE_ON = 0x90;
	public static final int NOTE_OFF = 0x80;

	public static void main(String[] args) throws Exception {

		List<Motive> motives = readMidi("C:/workspace/tonal/music/Bach-choral227deel1.mid");
		
		
		Map<Integer, Chord> chords = extractChordMap(motives);
		
		Chord previousChord = null;
		for (Entry<Integer, Chord> ch : chords.entrySet()) {
			System.out.print(ch.getKey() + ": ");
			System.out.println(ch.getValue().getChordType());
			if (previousChord != null) {
				System.out.println("prev: " + previousChord.getChordType());
				VoiceLeadingSize minimalVoiceLeadingSize = VoiceLeading.caculateSize(previousChord.getPitchClassMultiSet(), ch.getValue().getPitchClassMultiSet());
				System.out.print(previousChord.getPitchClassMultiSet());
				System.out.print(ch.getValue().getPitchClassMultiSet());
				System.out.print(minimalVoiceLeadingSize.getVlSource());
				System.out.print(minimalVoiceLeadingSize.getVlTarget());
				System.out.print(minimalVoiceLeadingSize.getSize());
				System.out.println();
			}
			previousChord = ch.getValue();
		}
	}

	public static Map<Integer, Chord> extractChordMap(List<Motive> motives) {
		Map<Integer, Chord> chords = new TreeMap<>();
		Set<Integer> positions = new TreeSet<>();
		for (Motive motive : motives) {
			List<NotePos> notes = motive.getNotePositions();
			for (NotePos notePos : notes) {
				positions.add(notePos.getPosition());
			}
		}
		for (Motive motive : motives) {
			List<NotePos> notes = motive.getNotePositions();
			int melodyLength = notes.size() - 1;
			Iterator<Integer> iterator = positions.iterator();
			Integer position = iterator.next();
			for (int i = 0; i < melodyLength; i++) {
				NotePos firstNote = notes.get(i);
				Integer pitchClass = firstNote.getPitchClass();
				NotePos secondNote = notes.get(i + 1);			
				while (position < secondNote.getPosition()) {
					addNoteToChordMap(chords, position, pitchClass);
					position = iterator.next();
				}
			}
			NotePos lastNote = notes.get(melodyLength);
			addNoteToChordMap(chords, lastNote.getPosition(), lastNote.getPitchClass());
		}
		return chords;
	}

	private static void addNoteToChordMap(Map<Integer, Chord> chords,
			Integer position, Integer pitchClass) {
		Chord chord = null;
		if (chords.containsKey(position)) {
			chord = chords.get(position);
		} else {
			chord = new Chord();
		}
		chord.addPitchClass(pitchClass);
		chords.put(position, chord);
	}

	public static List<Motive> readMidi(String path)
			throws InvalidMidiDataException, IOException {
		Sequence sequence = MidiSystem.getSequence(new File(path));
		LOGGER.finer("Ticks: " + sequence.getResolution());
		LOGGER.finer("PPQ: " + sequence.PPQ);
		LOGGER.finer("DivisionType: " + sequence.getDivisionType());
		int trackNumber = 0;
		int voice = 0;
		int size = sequence.getTracks().length;
		voice = size - 1;
		int resolution = 12;
		Map<Integer, Motive> map = new TreeMap<Integer, Motive>();

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
				Motive motive = new Motive(notes, length);
				map.put(voice, motive);
			}
			voice--;
		}
		List<Motive> motives = new ArrayList<Motive>(map.values());
		return motives;
	}

}
