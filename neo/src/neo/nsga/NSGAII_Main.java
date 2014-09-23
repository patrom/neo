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

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sound.midi.InvalidMidiDataException;

import jm.JMC;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.core.SolutionType;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import neo.generator.Generator;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.nsga.operator.crossover.OnePointCrossover;
import neo.nsga.operator.mutation.OneNoteMutation;
import neo.nsga.operator.mutation.PitchSpaceMutation;
import neo.out.print.Display;


public class NSGAII_Main implements JMC{
	
	private static Problem problem;
	private static SolutionType type;
	private static Operator crossover;
	private static Operator oneNoteMutation;
	private static Operator pitchSpaceMutation;
	private static Algorithm algorithm;
	private static Display display = new Display();
	private static Generator generator;

	public static void main(String [] args) throws JMException, SecurityException, IOException, InvalidMidiDataException, ClassNotFoundException {
		MusicProperties musicProperties = getMusicProperties();
		
	    problem = new MusicProblem(musicProperties);
	    generator = new Generator(musicProperties);
	    Motive motive = generator.generateMotive();
	    type = new MusicSolutionType(problem, motive) ;
	    problem.setSolutionType(type);
	    algorithm = new NSGAII(problem);
	
	    // Algorithm parameters
	    int populationSize = 10;
	    algorithm.setInputParameter("populationSize",populationSize);
	    algorithm.setInputParameter("maxEvaluations",populationSize * 200);
	    // Mutation and Crossover
	    HashMap<String, Object> parameters = new HashMap<>();
	    crossover = new OnePointCrossover(parameters);
	    crossover.setParameter("probabilityCrossover", 1.0);       
	
	    oneNoteMutation = new OneNoteMutation(parameters);
	    oneNoteMutation.setParameter("probabilityOneNote", 1.0);
	    
	    pitchSpaceMutation = new PitchSpaceMutation(parameters);
	    pitchSpaceMutation.setParameter("probabilityPitchSpace", 1.0);
	
	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
	    algorithm.addOperator("pitchSpaceMutation", pitchSpaceMutation);
	    algorithm.addOperator("selection", SelectionFactory.getSelectionOperator("BinaryTournament2", parameters));
	
	    // Execute the Algorithm
	    SolutionSet population = algorithm.execute();
	    
	    // Result messages 
	    population.printObjectivesToFile("SOL");
	    display.view(population, musicProperties.getTempo());
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
