package neo.objective.tonality;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import neo.model.melody.HarmonicMelody;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;

import org.junit.Before;
import org.junit.Test;

public class TonalityFunctionsTest {
	
	private static Logger LOGGER = Logger.getLogger(TonalityFunctionsTest.class.getName());

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMajorTonality() {
		List<Melody> melodies = new ArrayList<>();
		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
		List<Note> notes = new ArrayList<>();
		notes.add(NoteBuilder.note().len(12).pc(0).pitch(60).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(2).pitch(62).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(4).pitch(64).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(5).pitch(65).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(7).pitch(67).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(9).pitch(69).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(11).pitch(71).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(0).pitch(72).positionWeight(100.0).innerWeight(100.0).build());
		HarmonicMelody harmonicMelody = new HarmonicMelody(NoteBuilder.note().build(), notes, 0, 0);
		harmonicMelodies.add(harmonicMelody);
		Melody melody = new Melody(harmonicMelodies, 0);
		melodies.add(melody);
		double maxTonality = TonalityFunctions.getMaxCorrelationTonality(melodies, TonalityFunctions.vectorMajorTemplate);
		LOGGER.info("maxTonality : " + maxTonality);
	}
	
	@Test
	public void testChromaticTonality() {
		List<Melody> melodies = new ArrayList<>();
		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			notes.add(NoteBuilder.note().len(12).pc(i).pitch(60 + i).positionWeight(100.0).innerWeight(100.0).build());
		}
		HarmonicMelody harmonicMelody = new HarmonicMelody(NoteBuilder.note().build(), notes, 0, 0);
		harmonicMelodies.add(harmonicMelody);
		Melody melody = new Melody(harmonicMelodies, 0);
		melodies.add(melody);
		double maxTonality = TonalityFunctions.getMaxCorrelationTonality(melodies, TonalityFunctions.vectorMajorTemplate);
		LOGGER.info("maxTonality : " + maxTonality);
	}
	
	@Test
	public void testRegisterMajorTonality() {
		List<Melody> melodies = new ArrayList<>();
		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
		List<Note> notes = new ArrayList<>();
		notes.add(NoteBuilder.note().len(12).pc(0).pitch(48).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(2).pitch(62).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(4).pitch(64).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(5).pitch(65).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(7).pitch(55).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(9).pitch(69).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(11).pitch(71).positionWeight(100.0).innerWeight(100.0).build());
		notes.add(NoteBuilder.note().len(12).pc(0).pitch(60).positionWeight(100.0).innerWeight(100.0).build());
		HarmonicMelody harmonicMelody = new HarmonicMelody(NoteBuilder.note().build(), notes, 0, 0);
		harmonicMelodies.add(harmonicMelody);
		Melody melody = new Melody(harmonicMelodies, 0);
		melodies.add(melody);
		double maxTonality = TonalityFunctions.getMaxCorrelationTonality(melodies, TonalityFunctions.vectorMajorTemplate);
		LOGGER.info("maxTonality : " + maxTonality);
	}

}
