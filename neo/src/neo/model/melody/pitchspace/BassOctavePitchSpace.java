package neo.model.melody.pitchspace;

import java.util.List;

import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.out.instrument.Instrument;

public class BassOctavePitchSpace extends PitchSpace {

	public BassOctavePitchSpace(Integer[] octaveHighestPitchClassRange,
			List<Instrument> instruments) {
		super(octaveHighestPitchClassRange, instruments);
	}

	@Override
	public void translateToPitchSpace() {
		setPitchLowestNote();
		for (int i = 1; i < size; i++) {
			Note prevNote = getNote(i - 1);
			HarmonicMelody harmonicMelody = getHarmonicMelody(i);
			Note note = harmonicMelody.getHarmonyNote();
			int prevPc = prevNote.getPitchClass();
			int pc = note.getPitchClass();
			int pitch;
			int octave;
			if (prevPc > pc) {
				pitch = prevNote.getPitch() + (12 + (pc - prevPc));
				octave = prevNote.getOctave() + 1;
			} else {
				pitch = prevNote.getPitch() + (pc - prevPc);
				octave = prevNote.getOctave();
			}
			if (i == 1) {
				pitch = pitch + 12;
				octave++;
			}
			Instrument instrument = getInstrument(i);
			while (pitch < instrument.getLowest()) {
				pitch = pitch + 12;
				octave++;
			}
			while (pitch > instrument.getHighest()) {
				pitch = pitch - 12;
				octave--;
			}
			note.setPitch(pitch);
			note.setOctave(octave);
			harmonicMelody.updateMelodyPitchesToHarmonyPitch();
		}
	}

}
