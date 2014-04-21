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
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.melody.Melody;
import neo.data.note.NotePos;
import neo.evaluation.MusicProperties;
import neo.generator.Generator;
import neo.instrument.MidiDevice;
import neo.midi.MidiDevicesUtil;
import neo.nsga.operator.OnePointCrossover;
import neo.score.ScoreUtilities;
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
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;


public class NSGAII_TwelveToneMain implements JMC{
	  public static Logger      logger_ ;      // Logger object
	  public static FileHandler fileHandler_ ; // FileHandler object
	  private static MusicProperties inputProps = new MusicProperties();

  /**
   * @param args Command line arguments.
   * @throws JMException 
   * @throws IOException 
   * @throws SecurityException 
   * Usage: three options
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName
   *      - jmetal.metaheuristics.nsgaII.NSGAII_main problemName paretoFrontFile
   */
  public static void main(String [] args) throws 
                                  JMException, 
                                  SecurityException, 
                                  IOException, 
                                  ClassNotFoundException {
    Problem   problem   ;         // The problem to solve
    Algorithm algorithm ;         // The algorithm to use
    Operator  crossover ;         // Crossover operator
    Operator  mutation  ;         // Mutation operator
    Operator  selection ;         // Selection operator
    
    QualityIndicator indicators = null; // Object to get quality indicators

    // Logger object and file to store log messages
    logger_      = Configuration.logger_ ;
    fileHandler_ = new FileHandler("NSGAII_main.log"); 
    logger_.addHandler(fileHandler_) ;
    
    problem = new MusicAtonalProblem("music", 1, inputProps);
    SolutionType type = new MusicSolutionType(problem, inputProps.getMelodyLength(), inputProps.getScale()
			, inputProps.getRhythmProfile(), inputProps.getPopulationStrategy(), inputProps.getRanges(), inputProps.getMelodyLength() * 12) ;
    problem.setSolutionType(type);
    algorithm = new NSGAII_TwelveTone(problem);

    // Algorithm parameters
    int populationSize = 30;
    algorithm.setInputParameter("populationSize",populationSize);
    algorithm.setInputParameter("maxEvaluations",populationSize * 20);
    // Mutation and Crossover
    crossover = new OnePointCrossover();
    //if homophonic don't do crossover!
//    if (inputProps.getPopulationStrategy().equals("homophonic")) {
    	 crossover.setParameter("probabilityCrossover",0.0);       
//	} else {
//		 crossover.setParameter("probabilityCrossover",0.0);       
//	}      
//    crossover.setParameter("distributionIndex",20.0);

//    mutation = MutationFactory.getMutationOperator("BitFlipMutation");
//    mutation = new OneNoteMutation(inputProps.getScale(), inputProps.getRanges());
//    mutation.setParameter("probabilityOneNote",0.0);
    
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
    selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;                           

    // Add the operators to the algorithm
    algorithm.addOperator("crossover",crossover);
//    algorithm.addOperator("mutation",mutation);
//    algorithm.addOperator("mutation2",mutation2);
//    algorithm.addOperator("mutation3",mutation3);
//    algorithm.addOperator("mutation4",mutation4);
//    algorithm.addOperator("mutation5",mutation5);
    algorithm.addOperator("selection",selection);

    // Execute the Algorithm
    long initTime = System.currentTimeMillis();
    SolutionSet population = algorithm.execute();
    long estimatedTime = System.currentTimeMillis() - initTime;
    
    // Result messages 
    logger_.info("Total execution time: "+estimatedTime + "ms");
    logger_.info("Variables values have been writen to file VAR");
//    population.printVariablesToFile("VAR");   
//    population.printVariablesToNotes("VAR"); 
    logger_.info("Objectives values have been writen to file FUN");
    population.printObjectivesToFile("FUN");
    printVariablesToMidi(population);
  } 
  
  private static void printVariablesToMidi(SolutionSet solutionsList) throws JMException{
	  Map<Double, Solution> solutionMap = new TreeMap<Double, Solution>();
	  Iterator<Solution> iterator = solutionsList.iterator();
	  
	  while (iterator.hasNext()) {
		MusicSolution solution = (MusicSolution) iterator.next();
		solutionMap.put(solution.getHarmony(), solution);
	  }
	  
	  int[] ensemble = new int[4];
	  ensemble[0] = VIOLIN;
	  ensemble[1] = VIOLIN;
	  ensemble[2] = VIOLA;
	  ensemble[3] = CELLO;
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
		playOnKontakt(motive.getMelodies());
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

	private static void playOnKontakt(List<Melody> melodies) {
		try {
			Sequence seq = MidiDevicesUtil.createSequence(melodies, inputProps.getRanges());
			MidiDevicesUtil.playOnDevice(seq, ScoreUtilities.randomTempoFloat(), MidiDevice.KONTACT);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	
	private static void viewScore(List<Melody> melodies, int i) {
		Score score = ScoreUtilities.createScoreMotives(melodies);
		if (i <=8) {
			score.setTitle("test " + (i));
			Write.midi(score, "test" + (i) + ".mid");	
			View.notate(score);	
		}
	}
  
} 