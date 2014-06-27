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
import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;

import jm.JMC;
import jmetal.base.Algorithm;
import jmetal.base.Operator;
import jmetal.base.Problem;
import jmetal.base.SolutionSet;
import jmetal.base.SolutionType;
import jmetal.base.operator.selection.SelectionFactory;
import jmetal.util.JMException;
import neo.evaluation.MusicProperties;
import neo.nsga.operator.crossover.OnePointCrossover;
import neo.nsga.operator.mutation.OneNoteMutation;
import neo.nsga.operator.mutation.PitchSpaceMutation;
import neo.print.Display;


public class NSGAII_Main implements JMC{
	private static MusicProperties inputProps = new MusicProperties();
	private static Random random = new Random();

	public static void main(String [] args) throws JMException, SecurityException, IOException, InvalidMidiDataException, ClassNotFoundException {
	    Problem problem = new MusicProblem("music", 1, inputProps);
	    SolutionType type = new MusicSolutionType(problem, inputProps) ;
	    problem.setSolutionType(type);
	    Algorithm algorithm = new NSGAII(problem);
	
	    // Algorithm parameters
	    int populationSize = 20;
	    algorithm.setInputParameter("populationSize",populationSize);
	    algorithm.setInputParameter("maxEvaluations",populationSize * 200);
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
	    
	      Operator pitchSpaceMutation = new PitchSpaceMutation();
	      pitchSpaceMutation.setParameter("probabilityPitchSpace",1.0);
	
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
	    Display.view(population);
	} 
	
} 
