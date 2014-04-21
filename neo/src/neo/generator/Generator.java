package neo.generator;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.UniformPitchSpace;
import neo.data.note.NotePos;
import neo.instrument.KontaktPiano;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.score.ScoreUtilities;

public class Generator {
	
	private static Random random = new Random();

	public static void main(String[] args) throws InvalidMidiDataException {
		int chordSize = 4;
		int beat = 6;
		int[] rhythmTemplate = {0, 12, 24,36};
		Motive motive = generateMotive(rhythmTemplate, chordSize, beat);
		Score score = ScoreUtilities.createScoreMotives(motive.getMelodies());
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
		
		Sequence seq = MidiDevicesUtil.createSequence(motive.getMelodies(), ranges);
		float tempo = 80f;
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
		
	}

	public static Motive generateMotive(int[] rhythmTemplate ,int chordSize, int octaveHighestNote) {
		List<Harmony> harmonies = new ArrayList<>();
		for (int i = 0; i < rhythmTemplate.length - 1; i++) {
			List<Integer> chordPitchClasses = random.ints(0, 12).limit(chordSize).boxed().collect(toList());
			int voice = chordPitchClasses.size() - 1;
			List<NotePos> notePositions = new ArrayList<>();
			for (Integer pc : chordPitchClasses) {
				NotePos notePos = new NotePos(pc, voice , rhythmTemplate[i], rhythmTemplate[i+1] - rhythmTemplate[i]);
				notePositions.add(notePos);
				voice--;
			}
			Harmony harmony = new UniformPitchSpace(rhythmTemplate[i], notePositions, octaveHighestNote);
			harmony.translateToPitchSpace();
			harmonies.add(harmony);		
		}
		return new Motive(harmonies);
	}
	
}
