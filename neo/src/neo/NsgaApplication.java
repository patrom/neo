package neo;

import static neo.model.harmony.HarmonyBuilder.harmony;
import static neo.model.melody.HarmonicMelodyBuilder.harmonicMelody;
import static neo.model.note.NoteBuilder.note;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import neo.generator.RhythmWeight;
import neo.model.Motive;
import neo.model.harmony.HarmonyBuilder;
import neo.model.melody.HarmonicMelody;
import neo.nsga.MusicSolutionType;
import neo.out.print.Display;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
//	@Qualifier(value="oneNoteMutation")
	private Operator oneNoteMutation;
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
		prepareHarmonies();
	    Motive motive = generator.generateMotive();
	    solutionType.setMotive(motive);
	    
	    // Algorithm parameters
	    int populationSize = 10;
	    algorithm.setInputParameter("populationSize", populationSize);
	    algorithm.setInputParameter("maxEvaluations", populationSize * 200);
	    
	    // Mutation and Crossover
	    crossover.setParameter("probabilityCrossover", 1.0);       
	    oneNoteMutation.setParameter("probabilityOneNote", 1.0);
	    pitchSpaceMutation.setParameter("probabilityPitchSpace", 1.0);
	
	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
	    algorithm.addOperator("pitchSpaceMutation", pitchSpaceMutation);
	    algorithm.addOperator("selection", SelectionFactory.getSelectionOperator("BinaryTournament2", parameters));
	
	    // Execute the Algorithm
	    SolutionSet population = algorithm.execute();
	    
	    // result
	    population.printObjectivesToFile("SOL");
	    display.view(population, musicProperties.getTempo());
		
	}
	
	private  void prepareHarmonies() {
		int[] positions = {0,24,48,72,96,120};// last = length
		List<HarmonyBuilder> harmonyBuilders = generateHarmonyBuilders(positions);
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
		
		double[] barWeights = {1.0, 0.5, 0.75, 0.5};
		Map<Integer, Double> rhythmWeightValues = RhythmWeight.generateRhythmWeight(5, barWeights, musicProperties.getMinimumLength());
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
	} 
	
	private List<HarmonyBuilder> generateHarmonyBuilders(int[] positions){
		List<HarmonyBuilder> harmonyBuilders = new ArrayList<>();
		for (int i = 0; i < positions.length - 1; i++) {
			harmonyBuilders.add(harmony().pos(positions[i]).len(positions[i + 1] - positions[i]));
		}
		return harmonyBuilders;
	}

}
