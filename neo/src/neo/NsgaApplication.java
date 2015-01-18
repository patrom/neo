package neo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.SelectionFactory;
import neo.generator.BeginEndChordGenerator;
import neo.generator.DiffSizeGenerator;
import neo.generator.Generator;
import neo.generator.MusicProperties;
import neo.generator.PerleChordGenerator;
import neo.generator.RandomNotesGenerator;
import neo.generator.TonalChordGenerator;
import neo.generator.TonalChords;
import neo.model.Motive;
import neo.nsga.MusicSolutionType;
import neo.nsga.operator.mutation.harmony.HarmonyNoteToPitch;
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
	private HarmonyNoteToPitch harmonyNoteToPitch;
	@Autowired
	private MelodyNoteToHarmonyNote melodyNoteToHarmonyNote;
	@Autowired
	private Operator pitchSpaceMutation;
	@Autowired
	private Algorithm algorithm;
	@Autowired
	private Display display;
	@Autowired
	private MusicProperties musicProperties;
	@Autowired
	private HashMap<String, Object> parameters;
	@Autowired
	private String midiFilesPath;
	
	public static AtomicInteger COUNTER = new AtomicInteger();
	
	public static void main(final String[] args) throws IOException {
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
		deleteMidiFiles(midiFilesPath);
//		musicProperties.sixEight();
//		int[][] harmonies = {{0,0,18,36},{36,36,54,72},{72,72,90,108},{108,108,126,144}, {144,144,162,180}};
//		musicProperties.twoFour();
//		int[][] harmonies = {{0,0,24},{24,24,48},{48,48,72},{72,72,96}, {96,96,120}};
		musicProperties.threeFour();
		int[][] harmonies = {{0,0, 12, 18,24, 48},{36, 48,60},{72,60,84,108},{108,108,144}, {144,144,180}};
//		int[][] harmonies = {{0,0,36},{36,36,72},{72,72,108},{108,108,144}, {144,144,180}};

//		musicProperties.fourFour();
//		int[][] harmonies = {{0,0,48},{48,48,96},{96,96,144},{144,144,192}, {192,192,240}};
		
//		BeginEndChordGener¢r = beginEndChordGenerator;
		
		TonalChordGenerator tonalChordGenerator = new TonalChordGenerator(harmonies, musicProperties);
		tonalChordGenerator.setChords(TonalChords.getTriads(0));
		Generator generator = tonalChordGenerator;
		
//		Generator generator = new RandomNotesGenerator(harmonies, musicProperties);
//		generator.generateHarmonicMelodiesForVoice(harmonies, 3);
		
//		int[][] harmonies2 = {{0,12,36},{36,48,72},{72,72,96,108},{108,108,144}, {144,144,180}};
//		generator.generateHarmonicMelodiesForVoice(harmonies, 2);
		
//		Generator generator = new DiffSizeGenerator(harmonies, musicProperties);
//		Generator generator = new PerleChordGenerator(harmonies, musicProperties);
//		generator.generateHarmonicMelodiesForVoice(harmonies, 5);
	    Motive motive = generator.generateMotive();
	    solutionType.setMotive(motive);
	    
	    // Algorithm parameters
	    int populationSize = 20;
	    algorithm.setInputParameter("populationSize", populationSize);
	    algorithm.setInputParameter("maxEvaluations", populationSize * 1000);
	    
	    // Mutation and Crossover
	    crossover.setParameter("probabilityCrossover", 1.0); 
	    
	    //harmony
	    List<Integer> allowedDefaultIndexes = allowedDefaultIndexes();
	    harmonyNoteToPitch.setParameter("probabilityHarmonyNoteToPitch", 0.0);
	    harmonyNoteToPitch.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    harmonyNoteToPitch.setOuterBoundaryIncluded(false);//default == true;
	    
	    swapHarmonyNotes.setParameter("probabilitySwap", 1.0);
	    swapHarmonyNotes.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
	    
	    //melody
	    melodyNoteToHarmonyNote.setParameter("probabilityMelodyNoteToHarmonyNote", 1.0);
	    melodyNoteToHarmonyNote.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    melodyNoteToHarmonyNote.setOuterBoundaryIncluded(false);
	    
	    oneNoteMutation.setParameter("probabilityOneNote", 0.0);
	    oneNoteMutation.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    oneNoteMutation.setOuterBoundaryIncluded(false);
	    
	    //pitch
	    pitchSpaceMutation.setParameter("probabilityPitchSpace", 1.0);
	
	    // Add the operators to the algorithm
	    algorithm.addOperator("crossover", crossover);
	    algorithm.addOperator("oneNoteMutation", oneNoteMutation);
	    algorithm.addOperator("swapHarmonyNotes", swapHarmonyNotes);
	    algorithm.addOperator("harmonyNoteToPitch", harmonyNoteToPitch);
	    algorithm.addOperator("melodyNoteToHarmonyNote", melodyNoteToHarmonyNote);
	    algorithm.addOperator("pitchSpaceMutation", pitchSpaceMutation);
	    algorithm.addOperator("selection", SelectionFactory.getSelectionOperator("BinaryTournament2", parameters));
	
	    // Execute the Algorithm
	    SolutionSet population = algorithm.execute();
	    
	    // result
	    population.printObjectivesToFile("SOL");
	    display.view(population, musicProperties.getTempo());
	}

	private List<Integer> allowedDefaultIndexes() {
		List<Integer> allowedDefaultIndexes = new ArrayList<>();
	    for (int i = 0; i < musicProperties.getChordSize(); i++) {
	    	allowedDefaultIndexes.add(i);
		}
		return allowedDefaultIndexes;
	}
	
	private void prepareHarmonies() {
//        rhythmWeightValues = RhythmWeight.generateRhythmWeight(harmonies.length - 1, musicProperties.getMeasureWeights());
    }
	
	private void deleteMidiFiles(String midiFilesPath) throws IOException{
		List<File> midiFiles = Files.list(new File(midiFilesPath).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File file : midiFiles) {
			file.delete();
		}
	}
	
}
