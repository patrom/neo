package neo.model.rhythm;

import static neo.model.note.NoteBuilder.note;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import neo.DefaultConfig;
import neo.generator.MusicProperties;
import neo.model.note.Note;
import neo.out.instrument.KontaktLibPiano;
import neo.out.print.MusicXMLWriter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class RhythmTest {
	
	private Rhythm rhythm = new Rhythm();
	@Autowired
	private MusicXMLWriter musicXMLWriter;
	@Autowired
	private MusicProperties musicProperties;

	@Before
	public void setUp() throws Exception {
	
	}

	@Test
	public void testTexture() throws Exception {
		List<Note> chordNotes = new ArrayList<>();
		chordNotes.add(note().pc(0).pitch(60).ocatve(5).voice(0).build());
		chordNotes.add(note().pc(4).pitch(64).ocatve(5).voice(1).build());
		chordNotes.add(note().pc(7).pitch(67).ocatve(5).voice(2).build());
		List<Note> sounds = new ArrayList<>();
		sounds.add(note().pos(0).len(6).build());
		sounds.add(note().pos(6).len(12).build());
		sounds.add(note().pos(18).len(6).build());
		sounds.add(note().pos(26).len(6).build());
		int[] contour = new int[]{1,-2,1};
		List<Note> contourNotes = rhythm.getContour(chordNotes, sounds, contour, 1);
		int[] texture = {1,2,1,2};
		List<Note> textureNotes = rhythm.getTexture(chordNotes, contourNotes, texture);
		assertTrue(textureNotes.size() == 6);
		Note note = textureNotes.get(2);
		Note contourNote = contourNotes.get(1);
		assertEquals(60, note.getPitch());
		assertEquals(contourNote.getPosition(), note.getPosition());
		assertEquals(contourNote.getLength(), note.getLength());
		
		musicXMLWriter.generateMusicXMLForNotes(textureNotes, new KontaktLibPiano(0, 1) , "texture");
	}

	
	@Test
	public void testGetSounds(){
		int[] sounds = {0,6,18,24,30};
		List<Note> notes = rhythm.getSounds(sounds);
		assertTrue(notes.size() == 4);
		assertEquals(0, notes.get(0).getPosition());
		assertEquals(6, notes.get(1).getPosition());
		assertEquals(18, notes.get(2).getPosition());
		assertEquals(24, notes.get(3).getPosition());
		assertEquals(6, notes.get(0).getLength());
		assertEquals(12, notes.get(1).getLength());
	}
	
	@Test
	public void testGetContours(){
		List<Note> chordNotes = new ArrayList<>();
		chordNotes.add(note().pc(0).pitch(60).ocatve(5).voice(0).build());
		chordNotes.add(note().pc(4).pitch(64).ocatve(5).voice(1).build());
		chordNotes.add(note().pc(7).pitch(67).ocatve(5).voice(2).build());
		List<Note> sounds = new ArrayList<>();
		sounds.add(note().pos(0).len(6).build());
		sounds.add(note().pos(6).len(12).build());
		sounds.add(note().pos(18).len(6).build());
		sounds.add(note().pos(26).len(6).build());
		int[] contour = new int[]{1,-2,1};
		List<Note> contourNotes = rhythm.getContour(chordNotes, sounds, contour, 1);
		assertTrue(contourNotes.size() == 4);
		assertEquals(60, contourNotes.get(0).getPitch());
		assertEquals(64, contourNotes.get(1).getPitch());
		assertEquals(55, contourNotes.get(2).getPitch());
		assertEquals(60, contourNotes.get(3).getPitch());
	}
}
