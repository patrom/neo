package neo.generator;

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
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;
import neo.data.note.Scale;
import neo.instrument.KontaktLibPiano;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.score.ScoreUtilities;

public class Generator {
	
	private static Random random = new Random();

	public static void main(String[] args) throws InvalidMidiDataException {
		int chordSize = 4;
		int octave = 6;
		int[] rhythmGeneratorTemplate = {0,12,18,24};
		Motive motive = generateMotive(new Scale(Scale.MAJOR_SCALE), rhythmGeneratorTemplate,  chordSize, octave);
		List<Harmony> harmonies = motive.getHarmonies();
		harmonies.forEach(h ->  System.out.print(h.getChord().getChordType() + ", "));
		harmonies.forEach(h ->  System.out.println(h.getChord().getPitchClassMultiSet() + ", "));
		harmonies.forEach(h ->  System.out.println(h.getChord().getPitchClassSet() + ", "));
		harmonies.forEach(h ->  System.out.println(h.getNotes() + ", "));
		Score score = ScoreUtilities.createScoreMotives(motive.getMelodies());
		View.notate(score);
		Write.midi(score, "midi/test.mid");
		
		List<neo.instrument.Instrument> ranges = new ArrayList<>();
//		ranges.add(new KontaktLibViolin(0, 1));
//		ranges.add(new KontaktLibViolin(1, 2));
//		ranges.add(new KontaktLibAltViolin(2, 2));
//		ranges.add(new KontaktLibCello(3, 3));
		ranges.add(new KontaktLibPiano(0,0));
		ranges.add(new KontaktLibPiano(1,0));
		ranges.add(new KontaktLibPiano(2,0));
		ranges.add(new KontaktLibPiano(3,0));
		
		Sequence seq = MidiDevicesUtil.createSequence(motive.getMelodies(), ranges);
		float tempo = 80f;
		MidiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTACT);
		
	}

	public static Motive generateMotive(Scale scale, int[] rhythmGeneratorTemplate ,int chordSize, int octaveHighestNote) {
		List<Harmony> harmonies = new ArrayList<>();
		for (int i = 0; i < rhythmGeneratorTemplate.length - 1; i++) {	
			List<Integer> chordPitchClasses = new ArrayList<>();
			for (int j = 0; j < chordSize; j++) {
				int pitchClass = scale.pickRandomFromScale();
				chordPitchClasses.add(pitchClass);
			}
			int voice = chordPitchClasses.size() - 1;
			List<NotePos> notePositions = new ArrayList<>();
			int length = rhythmGeneratorTemplate[i+1] - rhythmGeneratorTemplate[i];
			for (Integer pc : chordPitchClasses) {
				NotePos notePos = new NotePos(pc, voice , rhythmGeneratorTemplate[i], length);
				notePositions.add(notePos);
				voice--;
			}
			Harmony harmony = new Harmony(rhythmGeneratorTemplate[i], length, 
					notePositions, new UniformPitchSpace(notePositions, octaveHighestNote));
			harmony.translateToPitchSpace();
			harmonies.add(harmony);		
		}
		return new Motive(harmonies);
	}
	
}
