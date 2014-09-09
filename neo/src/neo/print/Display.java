package neo.print;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;

import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.util.JMException;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.Note;
import neo.nsga.MusicSolution;
import neo.nsga.MusicVariable;

public class Display {

	private static Logger LOGGER = Logger.getLogger(Display.class.getName());
	 
	 public static void view(SolutionSet solutionsList, double tempo) throws JMException, InvalidMidiDataException{
		  Iterator<Solution> iterator = solutionsList.iterator();
		  int i = 1;
		  while (iterator.hasNext() && i < 11) {
			MusicSolution solution = (MusicSolution) iterator.next();
			LOGGER.info("Test " + i);
			LOGGER.info(solution.toString());
			Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
//			List<MusicalStructure> structures = FugaUtilities.addTransposedVoices(sentences, inputProps.getScale(), 8, 12);
//			sentences.addAll(structures);
//			MusicalStructure structure = FugaUtilities.harmonizeMelody(sentences, inputProps.getScale(), 2, 1, inputProps.getMelodyLength() * 12);
//			sentences.add(structure);
//			MusicalStructure structure2 = FugaUtilities.harmonizeMelody(sentences, inputProps.getScale(), 2, 2, inputProps.getMelodyLength() * 12);
//			sentences.add(structure2);
//			changeLengths(sentences);
//			motive.getHarmonies().stream().flatMap(h -> h.getHarmonicMelodies().stream()).forEach(harmony -> harmony.translateToPitchSpace());
			printHarmonies(motive.getHarmonies());
//			printNotes(motive.getHarmonies());
//			motive.getMelodies().stream().forEach(melody -> melody.updateMelodies());
			viewScore(motive.getMelodies(), i, tempo);
//			printVextab(sentences);
//			if (inputProps.getTempo() > 0f) {
//				Play.playOnKontakt(motive.getMelodies(), inputProps.getRanges(), inputProps.getTempo());
//			} else {
//				Play.playOnKontakt(motive.getMelodies(), inputProps.getRanges(), randomTempoFloat());
//			}
			i++;
		  }
		  
//			  List<MusicalStructure> structures = ((MusicVariable)solution.getDecisionVariables()[0]).getMelodies();
//			  Score score = ScoreUtilities.createScore(structures);
//			  BufferedOutputStream out = null;
//	    	  FileOutputStream writer = null;
//	    	  try {
//	    		 writer = new FileOutputStream("C://midi//test" + (i) + ".mid");
//	    		 out = new BufferedOutputStream(writer);
//	    		 Write.midi(score, out);
//	    	  } catch (IOException e) {
//	  			e.printStackTrace();
//	  		  }finally{
//		  			try {
//		  				out.close();
//		  				writer.close();
//		  			} catch (IOException e) {
//		  				e.printStackTrace();
//		  		}
//		    	i++;
//	  		  }
	  }
	  
		private static void printHarmonies(List<Harmony> harmonies) {
			harmonies.forEach(h ->  LOGGER.info(h.getChord().getChordType() + ", "));
//			harmonies.forEach(h ->  LOGGER.info(h.getChord().getPitchClassMultiSet() + ", "));
//			harmonies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
		}

		private static void viewScore(List<Melody> melodies, int i, double tempo) {
			melodies.forEach(h ->  LOGGER.info(h.getNotes() + ", "));
			Score score = ScoreUtilities.createScoreMelodies(melodies, tempo);
			score.setTitle("test " + (i));
			Write.midi(score, "resources/midi/test" + (i) + ".mid");	
			View.notate(score);	
		}

//		private static void printVextab(List<Harmony> harmonies) {
//			String vexTab = ScoreUtilities.createVexTab(harmonies, inputProps);
//			LOGGER.info(vexTab);
//		}
		
		private static void changeLengths(List<Harmony> harmonies) {
			for (Harmony harmony : harmonies) {
				List<Note> notes = harmony.getNotes();
				int size = notes.size() - 1;
				for (int i = 0; i < size; i++) {
					Note firstNote = notes.get(i);
					Note secondNote = notes.get(i + 1);
					int diff = secondNote.getPosition() - firstNote.getPosition();
					firstNote.setLength(diff);
				}
			}
		}

		private static void printNotes(List<Harmony> harmonies) {
			for (Harmony harmony : harmonies) {
				List<Note> notes = harmony.getNotes();
				int length = harmony.getLength();
				int pos = 0;
				for (Note notePos : notes) {
					while (pos != notePos.getPosition() && pos <= length) {
						System.out.print("\t");
						pos = pos + 6;
					}
					System.out.print(notePos.getPitch() + "," + notePos.getPosition() +";");
				}
				System.out.println();
			}
			System.out.println("Notes");
		}
		
}
