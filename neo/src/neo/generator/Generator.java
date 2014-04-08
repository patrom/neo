package neo.generator;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import neo.data.note.Motive;
import neo.data.note.NoteList;
import neo.data.note.NotePos;
import neo.instrument.KontaktLibAltViolin;
import neo.instrument.KontaktLibCello;
import neo.instrument.KontaktLibViolin;
import neo.instrument.KontaktPiano;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.midi.MidiParser;
import neo.score.ScoreUtilities;

public class Generator {

	public static void main(String[] args) throws InvalidMidiDataException {
		int chordSize = 4;
		int beat = 6;
		int[] rhythmTemplate = {0, 12, 24,36};
		List<NoteList> chords = generateChords(rhythmTemplate, chordSize, beat);
		int[] octaves = {4, 5, 5, 6};
		List<Motive> motives = convertToMotives(chords, octaves);
		Score score = ScoreUtilities.createScoreMotives(motives);
		View.notate(score);
		Write.midi(score, "midi/test.mid");
		
		List<neo.instrument.Instrument> ranges = new ArrayList<>();
//		ranges.add(new KontaktLibViolin(0, 1));
//		ranges.add(new KontaktLibViolin(1, 2));
//		ranges.add(new KontaktLibAltViolin(2, 2));
//		ranges.add(new KontaktLibCello(3, 3));
		ranges.add(new KontaktPiano(0,0));
		ranges.add(new KontaktPiano(1,0));
		ranges.add(new KontaktPiano(2,0));
		ranges.add(new KontaktPiano(3,0));
		
		Sequence seq = MidiDevicesUtil.createSequence(motives, ranges);
		float tempo = 80f;
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
		
	}

	public static List<NoteList> generateChords(int[] rhythmTemplate ,int chordSize, int beat) {
		Random random = new Random();
		List<NoteList> list = new ArrayList<>();
		for (int i = 0; i < rhythmTemplate.length - 1; i++) {
			int voice = 0;
			List<Integer> chordPitchClasses = random.ints(0, 12).limit(chordSize).boxed().collect(toList());
			List<NotePos> notePositions = new ArrayList<>();
			for (Integer pc : chordPitchClasses) {
				NotePos notePos = new NotePos(pc, voice , rhythmTemplate[i], rhythmTemplate[i+1] - rhythmTemplate[i]);
				notePositions.add(notePos);
				voice++;
			}
			NoteList noteList = new NoteList(rhythmTemplate[i], notePositions);
			list.add(noteList);		}
		return list;
	}
	
	
	public static List<Motive> convertToMotives(List<NoteList> noteList, int[] octaves) {
		List<NotePos> allNotes = new ArrayList<>();
		for (NoteList list : noteList) {
			for (NotePos notePos : list.getNotes()) {
				allNotes.add(notePos);
			}
		}
		Map<Integer, List<NotePos>> melodies = allNotes.stream().collect(Collectors.groupingBy(n -> n.getVoice()));
		List<Motive> motives = new ArrayList<>();
		int i = 0;
		for (Entry<Integer, List<NotePos>> entry: melodies.entrySet()) {
			List<NotePos> notes = entry.getValue();
			for (NotePos notePos : notes) {
				notePos.setPitch(notePos.getPitchClass() + (12 * octaves[i]));
			}
			i++;
			Motive motive = new Motive(notes, 0);
			motives.add(motive);
		}
		return motives;
	}
}
