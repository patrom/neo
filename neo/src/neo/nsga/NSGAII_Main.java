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

import static neo.data.harmony.HarmonyBuilder.harmony;
import static neo.data.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.data.note.NoteBuilder.note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;

import jm.JMC;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.SolutionType;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.util.JMException;
import neo.data.harmony.HarmonyBuilder;
import neo.data.melody.HarmonicMelody;
import neo.evaluation.MusicProperties;
import neo.nsga.operator.crossover.OnePointCrossover;
import neo.nsga.operator.mutation.OneNoteMutation;
import neo.nsga.operator.mutation.PitchSpaceMutation;
import neo.print.Display;


public class NSGAII_Main implements JMC{

	public static void main(String [] args) throws JMException, SecurityException, IOException, InvalidMidiDataException, ClassNotFoundException {
		MusicProperties musicProperties = getMusicProperties();
		
	    Problem problem = new MusicProblem("music", 1, musicProperties);
	    SolutionType type = new MusicSolutionType(problem, musicProperties) ;
	    problem.setSolutionType(type);
	    Algorithm algorithm = new NSGAII(problem);
	
	    // Algorithm parameters
	    int populationSize = 10;
	    algorithm.setInputParameter("populationSize",populationSize);
	    algorithm.setInputParameter("maxEvaluations",populationSize * 200);
	    // Mutation and Crossover
	    Operator crossover = new OnePointCrossover();
	    crossover.setParameter("probabilityCrossover", 1.0);       
	
	    Operator oneNoteMutation = new OneNoteMutation();
	    oneNoteMutation.setParameter("probabilityOneNote", 1.0);
	    
	    Operator pitchSpaceMutation = new PitchSpaceMutation();
	    pitchSpaceMutation.setParameter("probabilityPitchSpace", 1.0);
	
	    // Selection Operator 
	    Operator selection = SelectionFactory.getSelectionOperator("BinaryTournament2") ;                           
	
	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover",crossover);
	    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
	    algorithm.addOperator("pitchSpaceMutation",pitchSpaceMutation);
	    algorithm.addOperator("selection",selection);
	
	    // Execute the Algorithm
	    SolutionSet population = algorithm.execute();
	    
	    // Result messages 
	    population.printObjectivesToFile("FUN");
	    Display.view(population, musicProperties.getTempo());
	}

	private static MusicProperties getMusicProperties() {
		MusicProperties musicProperties = new MusicProperties();
		List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
		harmonyBuilders.add(harmony().pos(0).len(24));
		harmonyBuilders.add(harmony().pos(24).len(24));
		harmonyBuilders.add(harmony().pos(48).len(24));
		harmonyBuilders.add(harmony().pos(72).len(24));
		harmonyBuilders.add(harmony().pos(96).len(24));
		musicProperties.setHarmonyBuilders(harmonyBuilders);
		
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		harmonicMelodies.add(harmonicMelody().voice(2).pos(0)
				.notes(note().voice(2).pos(0).len(12).build(), 
					   note().voice(2).pos(12).len(24).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(24)
				.notes(note().voice(2).pos(36).len(12).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(2).pos(48)
				.notes(note().voice(2).pos(54).len(6).build(),
						note().voice(2).pos(60).len(12).build()).build());
		
		harmonicMelodies.add(harmonicMelody().voice(1).pos(24)
				.notes(note().voice(1).pos(24).len(12).build(), 
					   note().voice(1).pos(36).len(24).build()).build());
		harmonicMelodies.add(harmonicMelody().voice(1).pos(48)
				.notes(note().voice(1).pos(60).len(12).build()).build());
		musicProperties.setHarmonicMelodies(harmonicMelodies);
		
		Map<Integer, Double> rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 0.75);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		rhythmWeightValues.put(30, 0.5);
		rhythmWeightValues.put(36, 0.75);
		rhythmWeightValues.put(42, 0.5);
		
		rhythmWeightValues.put(48, 1.0);
		rhythmWeightValues.put(54, 0.5);
		rhythmWeightValues.put(60, 0.75);
		rhythmWeightValues.put(66, 0.5);
		rhythmWeightValues.put(72, 1.0);
		rhythmWeightValues.put(78, 0.5);
		rhythmWeightValues.put(84, 0.75);
		rhythmWeightValues.put(90, 0.5);
		
		rhythmWeightValues.put(96, 1.0);
		rhythmWeightValues.put(102, 0.5);
		rhythmWeightValues.put(108, 0.75);
		rhythmWeightValues.put(114, 0.5);

		musicProperties.setRhythmWeightValues(rhythmWeightValues);
		return musicProperties;
	} 
	
} 
