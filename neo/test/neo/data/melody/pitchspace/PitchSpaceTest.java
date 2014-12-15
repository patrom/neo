package neo.data.melody.pitchspace;

import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.List;

import neo.model.melody.HarmonicMelody;
import neo.out.instrument.Ensemble;
import neo.out.instrument.Instrument;

public abstract class PitchSpaceTest {

	protected List<Instrument> instruments = Ensemble.getStringQuartet();
	protected Integer[] range = {6};
	
	protected List<HarmonicMelody> getHarmonicMelodies(int voice0, int voice1, int voice2, int voice3) {
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		HarmonicMelody harmonicMelody0 = harmonicMelody()
				.harmonyNote(note().pc(voice0).build())
				.voice(0)
				.notes(note().pc(0).pos(0).len(6).build(), 
					   note().pc(0).pos(6).len(12).build(),
					   note().pc(0).pos(12).len(12).build())
				.build();
		harmonicMelodies.add(harmonicMelody0);
		HarmonicMelody harmonicMelody1 = harmonicMelody()
				.harmonyNote(note().pc(voice1).build())
				.voice(1)
				.notes(note().pc(1).pos(0).len(6).build(), 
					   note().pc(0).pos(6).len(12).build(),
					   note().pc(3).pos(12).len(12).build())
				.build();
		harmonicMelodies.add(harmonicMelody1);
		HarmonicMelody harmonicMelody2 = harmonicMelody()
				.harmonyNote(note().pc(voice2).build())
				.voice(2)
				.build();
		harmonicMelodies.add(harmonicMelody2);
		HarmonicMelody harmonicMelody3 = harmonicMelody()
				.harmonyNote(note().pc(voice3).build())
				.voice(3)
				.build();
		harmonicMelodies.add(harmonicMelody3);
		return harmonicMelodies;
	}
}
