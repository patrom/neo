package neo.out.print;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import jm.music.data.Score;
import neo.generator.MusicProperties;
import neo.midi.MelodyInstrument;
import neo.model.melody.Melody;
import neo.model.note.NoteBuilder;
import neo.variation.Embellisher;

@Component
public class ScoreUtilities implements JMC{
	
	private Logger LOGGER = Logger.getLogger(ScoreUtilities.class.getName());

	private final double ATOMIC_VALUE = 12;
	private Random random = new Random();
	@Autowired
	private Embellisher embellisher;
	
	@Autowired
	private MusicProperties musicProperties;
	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	public float randomTempo() {
		float r = random.nextFloat();
		if (r < 0.5) {
			r = (r * 100) + 100;
		} else {
			r = r * 100;
		}
		return r;
	}
	
	public Score createScoreMelodies(List<Melody> melodies, double tempo){
		Score score = new Score();
		Part[] scoreParts = new Part[melodies.size() * 2];
		int voice = 0;
		for (Melody melody : melodies) {
			Phrase phrase = null;
			List<neo.model.note.Note> notes = melody.getMelodieNotes();
//			if (melody.getVoice() == 3) {
//				List<neo.model.note.Note> embellishedNotes = embellisher.embellish(notes);
//				phrase = createPhrase(embellishedNotes);	
//			}
//			else{
				phrase = createPhrase(notes);	
//			}
			
			Part part = new Part(phrase);
			scoreParts[voice] = part;
			voice++;	
		}
		for (Melody melody : melodies) {
			List<neo.model.note.Note> notes = melody.getHarmonyNotes();
			Phrase phrase = createPhrase(notes);	
			Part part = new Part(phrase);
			scoreParts[voice] = part;
			voice++;	
		}
		for (int i = scoreParts.length - 1; i > -1; i--) {
			score.add(scoreParts[i]);
		}
		score.setTempo(tempo);
		score.setNumerator(musicProperties.getNumerator());
		score.setDenominator(musicProperties.getDenominator());
		score.setKeySignature(musicProperties.getKeySignature());
		return score;
	}
	
	public Score createScoreFromMelodyInstrument(List<MelodyInstrument> melodies, double tempo){
		Score score = new Score();
		Part[] scoreParts = new Part[melodies.size()];
		int voice = 0;
		for (MelodyInstrument melody : melodies) {
			List<neo.model.note.Note> notes = melody.getNotes();
			Phrase phrase = createPhrase(notes);	
			Part part = new Part(phrase);
			scoreParts[voice] = part;
			voice++;	
		}
		for (int i = scoreParts.length - 1; i > -1; i--) {
			score.add(scoreParts[i]);
		}
		score.setTempo(tempo);
		score.setNumerator(musicProperties.getNumerator());
		score.setDenominator(musicProperties.getDenominator());
		score.setKeySignature(musicProperties.getKeySignature());
		return score;
	}

	public Phrase createPhrase(List<neo.model.note.Note> notes) {
		Phrase phrase = new Phrase();
		if (!notes.isEmpty()) {
			double startTime = (double)notes.get(0).getPosition()/ATOMIC_VALUE;
			phrase.setStartTime(startTime);
			int length = notes.size();
			Note note = null;
			for (int i = 0; i < length; i++) {
				neo.model.note.Note notePos = notes.get(i);
				note = new Note(notePos.getPitch(),((double)notePos.getLength()/ATOMIC_VALUE));
				note.setDuration(note.getRhythmValue());//note has DEFAULT_DURATION_MULTIPLIER = 0.9
				phrase.add(note);
				if ((i + 1) < length) {	
					neo.model.note.Note nextNotePos = notes.get(i + 1);
					int gap = (notePos.getPosition() + notePos.getLength()) - nextNotePos.getPosition();
					if (gap < 0) {
						note = new Rest((double)-gap/ATOMIC_VALUE);
						note.setDuration(note.getRhythmValue());//note has DEFAULT_DURATION_MULTIPLIER = 0.9
						phrase.add(note);
					}
				}	
			}
		}
		return phrase;
	}
	
	public Score createMelody(List<neo.model.note.Note> notes){
		Score score = new Score();
		Phrase phrase = createPhrase(notes);	
		Part part = new Part(phrase);
		score.add(part);
		return score;
	}
	
}

