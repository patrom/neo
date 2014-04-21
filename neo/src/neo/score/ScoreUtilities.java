package neo.score;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;
import jm.JMC;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Rest;
import jm.music.data.Score;

public class ScoreUtilities implements JMC{

	private static final double ATOMIC_VALUE = 12;
	private static Random random = new Random();

	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	public static double randomTempo() {
		double r = random.nextDouble();
		if (r < 0.5) {
			r = (r * 100) + 100;
		} else {
			r = r * 100;
		}
		//tempo between 50 - 150
		return r;
	}
	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	public static float randomTempoFloat() {
		float r = random.nextFloat();
		if (r < 0.5) {
			r = (r * 100) + 100;
		} else {
			r = r * 100;
		}
		//tempo between 50 - 150
		return r;
	}
	
	public static Score createScoreMotives(List<Melody> motives){
		Score score = new Score();
		Part[] scoreParts = new Part[motives.size()];
		int voice = 0;
		for (Melody motive : motives) {
			List<NotePos> notePosistions = motive.getMelody();
			Phrase phrase = new Phrase();
			int lastVoice = 0;
			if (!notePosistions.isEmpty()) {
				double startTime = (double)notePosistions.get(0).getPosition()/ATOMIC_VALUE;
				phrase.setStartTime(startTime);
				int length = notePosistions.size();
				Note note = null;
				for (int i = 0; i < length; i++) {
					NotePos notePos = notePosistions.get(i);
					lastVoice = notePos.getVoice();
					note = new Note(notePos.getPitch(),(double)notePos.getLength()/ATOMIC_VALUE);
					phrase.add(note);
					if ((i + 1) < length) {	
						NotePos nextNotePos = notePosistions.get(i + 1);
						int gap = (notePos.getPosition()+ notePos.getLength()) - nextNotePos.getPosition();
						if (gap < 0) {
							note = new Rest((double)-gap/ATOMIC_VALUE);
							phrase.add(note);
						}
					}	
				}
			}
			
			Part part = new Part(phrase);
			scoreParts[voice] = part;
			if (voice != lastVoice) {
				System.out.println("something wrong with voices");
			}
			voice++;	
		}

		for (int i = scoreParts.length - 1; i > -1; i--) {
			score.add(scoreParts[i]);
		}
		
		double r = randomTempo();
		//tempo between 50 - 150
		score.setTempo(r);
		return score;
	}
	
}
