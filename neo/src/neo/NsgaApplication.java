package neo;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.SelectionFactory;
import neo.generator.Generator;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.nsga.MusicSolutionType;
import neo.nsga.operator.mutation.harmony.HarmonyNoteToPreviousPitchFromScale;
import neo.nsga.operator.mutation.harmony.SwapHarmonyNotes;
import neo.nsga.operator.mutation.melody.MelodyNoteToHarmonyNote;
import neo.nsga.operator.mutation.melody.OneNoteMutation;
import neo.out.print.Display;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(DefaultConfig.class)
public class NsgaApplication extends JFrame implements CommandLineRunner{
	
	private static Logger LOGGER = Logger.getLogger(NsgaApplication.class.getName());
	
	@Autowired
	private Problem problem;
	@Autowired
	private MusicSolutionType solutionType;
	@Autowired 
//	@Qualifier(value="crossover")
	private Operator crossover;
	@Autowired
	private OneNoteMutation oneNoteMutation;
	@Autowired
	private SwapHarmonyNotes swapHarmonyNotes;
	@Autowired
	private HarmonyNoteToPreviousPitchFromScale harmonyNoteToPreviousPitchFromScale;
	@Autowired
	private MelodyNoteToHarmonyNote melodyNoteToHarmonyNote;
	@Autowired
	private Operator pitchSpaceMutation;
	@Autowired
	private Algorithm algorithm;
	@Autowired
	private Display display;
	@Autowired
	private Generator generator;
	@Autowired
	private MusicProperties musicProperties;
	@Autowired
	private HashMap<String, Object> parameters;
	
	public static AtomicInteger COUNTER = new AtomicInteger();
	
	public static void main(final String[] args) {
		for (int i = 0; i < 2; i++) {
			LOGGER.info("RUN: " + i + " START");
			SpringApplication app = new SpringApplication(NsgaApplication.class);
		    app.setShowBanner(false);
		    app.run(args);
		    LOGGER.info("RUN: " + i + " END");
		}
	}

	@Override
	public void run(String... arg0) throws Exception {
		musicProperties.threeFour();
		int[][] harmonies = {{0,12,18,24},{36,30,36,42,60},{72, 72,78,84},{108}};
		generator.generateHarmonicMelodiesForVoice(harmonies, 3);
		int[][] harmonies2 = {{0,0, 6,12,18,24,30,36},{36,36,42,72},{72, 72,78,108},{108}};
		generator.generateHarmonicMelodiesForVoice(harmonies2, 2);
        generator.generateRhythmWeights(harmonies.length - 1 , musicProperties.getMeasureWeights());
		generator.generateHarmonyBuilders(harmonies);
	    Motive motive = generator.generateMotive();
	    solutionType.setMotive(motive);
	    
	    // Algorithm parameters
	    int populationSize = 10;
	    algorithm.setInputParameter("populationSize", populationSize);
	    algorithm.setInputParameter("maxEvaluations", populationSize * 1000);
	    
	    // Mutation and Crossover
	    crossover.setParameter("probabilityCrossover", 1.0); 
	    
	    //harmony
	    int[] allowedDefaultIndexes = allowedDefaultIndexes();
	    harmonyNoteToPreviousPitchFromScale.setParameter("probabilityOneNote", 1.0);
	    harmonyNoteToPreviousPitchFromScale.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    harmonyNoteToPreviousPitchFromScale.setOuterBoundaryIncluded(false);//default == true;
	    
	    swapHarmonyNotes.setParameter("probabilityOneNote", 1.0);
	    swapHarmonyNotes.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
	    
	    //melody
	    melodyNoteToHarmonyNote.setParameter("probabilityOneNote", 1.0);
	    int[] allowedMutationIndexesMelodyNoteToHarmonyNote = {0,2,3};
	    melodyNoteToHarmonyNote.setAllowedMelodyMutationIndexes(allowedMutationIndexesMelodyNoteToHarmonyNote);
	    
	    oneNoteMutation.setParameter("probabilityOneNote", 1.0);
	    int[] allowedMutationIndexesOneNoteMutation = {0,1,3};
	    oneNoteMutation.setAllowedMelodyMutationIndexes(allowedMutationIndexesOneNoteMutation);
	    
	    //pitch
	    pitchSpaceMutation.setParameter("probabilityPitchSpace", 1.0);
	
	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
	    algorithm.addOperator("swapHarmonyNotes", swapHarmonyNotes);
	    algorithm.addOperator("harmonyNoteToPreviousPitchFromScale", harmonyNoteToPreviousPitchFromScale);
	    algorithm.addOperator("melodyNoteToHarmonyNote", melodyNoteToHarmonyNote);
	    algorithm.addOperator("pitchSpaceMutation", pitchSpaceMutation);
	    algorithm.addOperator("selection", SelectionFactory.getSelectionOperator("BinaryTournament2", parameters));
	
	    // Execute the Algorithm
	    SolutionSet population = algorithm.execute();
	    
	    // result
	    population.printObjectivesToFile("SOL");
	    display.view(population, musicProperties.getTempo());
	}

	private int[] allowedDefaultIndexes() {
		int[] allowedDefaultIndexes = new int[musicProperties.getChordSize()];
	    for (int i = 0; i < musicProperties.getChordSize(); i++) {
			allowedDefaultIndexes[i] = i;
		}
		return allowedDefaultIndexes;
	}
	
	private void prepareHarmonies() {
//        rhythmWeightValues = RhythmWeight.generateRhythmWeight(harmonies.length - 1, musicProperties.getMeasureWeights());
    }
	
}
