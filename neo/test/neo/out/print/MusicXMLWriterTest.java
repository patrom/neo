package neo.out.print;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import neo.DefaultConfig;
import neo.VariationConfig;
import neo.generator.MusicProperties;
import neo.model.melody.HarmonicMelody;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibPiano;
import neo.out.instrument.KontaktLibViolin;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DefaultConfig.class, VariationConfig.class}, loader = SpringApplicationContextLoader.class)
public class MusicXMLWriterTest {
	
	@Autowired
	private MusicXMLWriter musicXMLWriter;
	private List<Melody> melodies;
	@Autowired
	private MusicProperties musicProperties;

	@Before
	public void setUp() throws Exception {
		List<Instrument> instruments = new ArrayList<>();
		Instrument instrument = new KontaktLibPiano(0, 1);
		instruments.add(instrument);
		instrument = new KontaktLibPiano(1, 1);
		instruments.add(instrument);
		musicProperties.setInstruments(instruments);
		melodies = new ArrayList<>();
		List<HarmonicMelody> harmonicMelodies = new ArrayList<HarmonicMelody>();
		List<Note> notes = new ArrayList<>();
		notes.add(NoteBuilder.note().len(12).pc(4).pitch(64).ocatve(4).pos(0).build());
		notes.add(NoteBuilder.note().len(24).pc(2).pitch(62).ocatve(4).pos(12).build());
		notes.add(NoteBuilder.note().len(24).pc(11).pitch(59).ocatve(3).pos(12).build());
		notes.add(NoteBuilder.note().len(24).pc(7).pitch(55).ocatve(3).pos(12).build());
		notes.add(NoteBuilder.note().len(24).pc(4).pitch(64).ocatve(4).pos(36).build());
		notes.add(NoteBuilder.note().len(6).pc(5).pitch(65).ocatve(4).pos(60).build());
		notes.add(NoteBuilder.note().len(6).pc(7).pitch(67).ocatve(4).pos(66).build());
		notes.add(NoteBuilder.note().len(12).pc(9).pitch(69).ocatve(4).pos(72).build());
		notes.add(NoteBuilder.note().len(24).pc(11).pitch(71).ocatve(4).pos(84).build());
		notes.add(NoteBuilder.note().len(24).pc(0).pitch(60).ocatve(4).pos(108).build());
		HarmonicMelody harmonicMelody = new HarmonicMelody(NoteBuilder.note().build(), notes, 0, 0);
		harmonicMelodies.add(harmonicMelody);
		Melody melody = new Melody(harmonicMelodies, 0);
		melodies.add(melody);
		
		notes = new ArrayList<>();
		notes.add(NoteBuilder.note().len(48).pc(0).pitch(36).ocatve(3).pos(0).build());
		notes.add(NoteBuilder.note().len(48).pc(2).pitch(38).ocatve(3).pos(48).build());
		notes.add(NoteBuilder.note().len(24).pc(4).pitch(40).ocatve(3).pos(96).build());
//		notes.add(NoteBuilder.note().len(6).pc(5).pitch(53).ocatve(3).pos(60).build());
//		notes.add(NoteBuilder.note().len(6).pc(7).pitch(43).ocatve(3).pos(66).build());
//		notes.add(NoteBuilder.note().len(12).pc(9).pitch(57).ocatve(3).pos(72).build());
//		notes.add(NoteBuilder.note().len(24).pc(11).pitch(59).ocatve(3).pos(84).build());
//		notes.add(NoteBuilder.note().len(24).pc(0).pitch(48).ocatve(3).pos(108).build());
		harmonicMelody = new HarmonicMelody(NoteBuilder.note().build(), notes, 1, 0);
		harmonicMelodies = new ArrayList<HarmonicMelody>();
		harmonicMelodies.add(harmonicMelody);
		melody = new Melody(harmonicMelodies, 1);
		melodies.add(melody);
	}

	@Test
	public void testGenerateMusicXML() throws Exception {
		musicXMLWriter.generateMusicXMLForMelodies(melodies, "test");
	}

}
