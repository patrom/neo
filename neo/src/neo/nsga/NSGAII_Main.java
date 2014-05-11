/**
 * NSGAII_main.java
 *
 * @author Juan J. Durillo
 * @author Antonio J. Nebro
 * @version 1.0
 *   This implementation of NSGA-II makes use of a QualityIndicator object
 *   to obtained the convergence speed of the algorithm. This version is used
 *   in the paper:
 *     A.J. Nebro, J.J. Durillo, C.A. Coello Coello, F. Luna, E. Alba 
 *     "A Study of Convergence Speed in Multi-Objective Metaheuristics." 
 *     To be presented in: PPSN'08. Dortmund. September 2008.
 *     
 *   Besides the classic NSGA-II, a steady-state version (ssNSGAII) is also
 *   included (See: J.J. Durillo, A.J. Nebro, F. Luna and E. Alba 
 *                  "On the Effect of the Steady-State Selection Scheme in 
 *                  Multi-Objective Genetic Algorithms"
 *                  5th International Conference, EMO 2009, pp: 183-197. 
 *                  April 2009)
 *   
 */
package neo.nsga;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;

import jm.JMC;
import jm.music.data.Score;
import jm.util.View;
import jm.util.Write;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.Solution;
import jmetal.base.SolutionSet;
import jmetal.base.SolutionType;
import jmetal.base.operator.mutation.Mutation;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.util.JMException;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.Scale;
import neo.evaluation.MusicProperties;
import neo.midi.Play;
import neo.nsga.operator.crossover.OnePointCrossover;
import neo.nsga.operator.mutation.OneNoteMutation;
import neo.score.ScoreUtilities;


public class NSGAII_Main implements JMC{
	  private static MusicProperties inputProps = new MusicProperties();
	  private static Random random = new Random();

  public static void main(String [] args) throws JMException, SecurityException, IOException, InvalidMidiDataException, ClassNotFoundException {
    Problem problem = new MusicProblem("music", 1, inputProps);
    SolutionType type = new MusicSolutionType(problem, inputProps.getScale()) ;
    problem.setSolutionType(type);
    Algorithm algorithm = new NSGAII(problem);

    // Algorithm parameters
    int populationSize = 30;
    algorithm.setInputParameter("populationSize",populationSize);
    algorithm.setInputParameter("maxEvaluations",populationSize * 20);
    // Mutation and Crossover
    Operator crossover = new OnePointCrossover();
    //if homophonic don't do crossover!
//    if (inputProps.getPopulationStrategy().equals("homophonic")) {
    	 crossover.setParameter("probabilityCrossover",0.0);       
//	} else {
//		 crossover.setParameter("probabilityCrossover",0.0);       
//	}      
//    crossover.setParameter("distributionIndex",20.0);

//    Operator mutation = MutationFactory.getMutationOperator("BitFlipMutation");
      Operator oneNoteMutation = new OneNoteMutation(inputProps.getScale());
      oneNoteMutation.setParameter("probabilityOneNote",1.0);
    
//    mutation = new SwitchVoiceMutationAtonal(inputProps.getRanges());
//    mutation.setParameter("probabilitySwitchVoice",0.0);
//    mutation.setParameter("distributionIndex",20.0);  
//    Mutation mutation2 = new ShiftNoteMutationAtonal2();
//    mutation2.setParameter("probabilityShiftNote",0.4);
//    Mutation mutation3 = new ShiftPartitionMutationAtonal();
//    mutation3.setParameter("probabilityShiftPartition",0.2);
//    Mutation mutation5 = new InsertRestMutation();
//    mutation5.setParameter("probabilityInsertRest",0.0);
    
    
    // Selection Operator 
    Operator selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;                           

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
//    algorithm.addOperator("mutation2",mutation2);
//    algorithm.addOperator("mutation3",mutation3);
//    algorithm.addOperator("mutation4",mutation4);
//    algorithm.addOperator("mutation5",mutation5);
    algorithm.addOperator("selection",selection);

    // Execute the Algorithm
    SolutionSet population = algorithm.execute();
    
    // Result messages 
//    population.printVariablesToFile("VAR");   
//    population.printVariablesToNotes("VAR"); 
    population.printObjectivesToFile("FUN");
    printVariablesToMidi(population);
  } 
  
  private static void printVariablesToMidi(SolutionSet solutionsList) throws JMException, InvalidMidiDataException{
	  Map<Double, Solution> solutionMap = new TreeMap<Double, Solution>();
	  Iterator<Solution> iterator = solutionsList.iterator();
	  
	  while (iterator.hasNext()) {
		MusicSolution solution = (MusicSolution) iterator.next();
		solutionMap.put(solution.getHarmony(), solution);
	  }
	  int i = 1;
	  for (Solution solution : solutionMap.values()) {
		Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
//		List<MusicalStructure> structures = FugaUtilities.addTransposedVoices(sentences, inputProps.getScale(), 8, 12);
//		sentences.addAll(structures);
//		MusicalStructure structure = FugaUtilities.harmonizeMelody(sentences, inputProps.getScale(), 2, 1, inputProps.getMelodyLength() * 12);
//		sentences.add(structure);
//		MusicalStructure structure2 = FugaUtilities.harmonizeMelody(sentences, inputProps.getScale(), 2, 2, inputProps.getMelodyLength() * 12);
//		sentences.add(structure2);
//		changeLengths(sentences);
		printNotes(motive.getHarmonies());
		viewScore(motive.getMelodies(), i);
//		printVextab(sentences);
//		if (inputProps.getTempo() > 0f) {
//			Play.playOnKontakt(motive.getMelodies(), inputProps.getRanges(), inputProps.getTempo());
//		} else {
//			Play.playOnKontakt(motive.getMelodies(), inputProps.getRanges(), randomTempoFloat());
//		}
		i++;
	  }
	  
	  int j = 1;
	  for (Solution solution : solutionMap.values()) {
			System.out.println(j + ": " + solution);
			j++;
	  }
	  
//	  for (Solution solution : solutionMap.values()) {
//		  List<MusicalStructure> structures = ((MusicVariable)solution.getDecisionVariables()[0]).getMelodies();
//		  Score score = ScoreUtilities.createScore(structures);
//		  BufferedOutputStream out = null;
//    	  FileOutputStream writer = null;
//    	  try {
//    		 writer = new FileOutputStream("C://midi//test" + (i) + ".mid");
//    		 out = new BufferedOutputStream(writer);
//    		 Write.midi(score, out);
//    	  } catch (IOException e) {
//  			e.printStackTrace();
//  		  }finally{
//	  			try {
//	  				out.close();
//	  				writer.close();
//	  			} catch (IOException e) {
//	  				e.printStackTrace();
//	  		}
//	    	i++;
//  		  }
//	  }
  }
  
	private static void viewScore(List<Melody> melodies, int i) {
		Score score = ScoreUtilities.createScoreMotives(melodies);
		if (i <=8) {
			score.setTitle("test " + (i));
			Write.midi(score, "test" + (i) + ".mid");	
			View.notate(score);	
		}
	}

//	private static void printVextab(List<MusicalStructure> sentences) {
//		String vexTab = ScoreUtilities.createVexTab(sentences, inputProps);
//		System.out.println(vexTab);
//	}
//
//	private static void changeLengths(List<MusicalStructure> sentences) {
//		for (MusicalStructure musicalStructure : sentences) {
//			List<NotePos> notes = musicalStructure.getNotePositions();
//			int size = notes.size() - 1;
//			for (int i = 0; i < size; i++) {
//				NotePos firstNote = notes.get(i);
//				NotePos secondNote = notes.get(i + 1);
//				int diff = secondNote.getPosition() - firstNote.getPosition();
//				firstNote.setLength(diff);
//			}
//		}
//	}

	private static void printNotes(List<Harmony> sentences) {
//		for (NoteList musicalStructure : sentences) {
//			List<NotePos> notes = musicalStructure.getNotes();
//			int length = musicalStructure.getLength();
//			int pos = 0;
//			System.out.print(musicalStructure.getVoice() + ":");
//			for (NotePos notePos : notes) {
//				while (pos != notePos.getPosition() && pos <= length) {
//					System.out.print("\t");
//					pos = pos + 6;
//				}
//				System.out.print(notePos.getPitch() + "," + notePos.getPosition() +";");
//			}
//			System.out.println();
//		}
//		System.out.println("Notes");
	}
	
	/**
	 * Generates random tempo between 50 - 150 bpm
	 * @return
	 */
	private static float randomTempoFloat() {
		float r = random.nextFloat();
		if (r < 0.5) {
			r = (r * 100) + 100;
		} else {
			r = r * 100;
		}
		//tempo between 50 - 150
		return r;
	}
	
} 
