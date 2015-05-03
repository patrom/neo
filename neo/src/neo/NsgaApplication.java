package neo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.SelectionFactory;
import neo.generator.Generator;
import neo.generator.MusicProperties;
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
	private Random random = new Random();
	
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
		for (int i = 0; i < 1; i++) {
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
//		musicProperties.threeFour();
		musicProperties.fourFour();
		int[] generatedHamonies = generateHarmonyPositions(12, 4, 4);
		int[][] melodyPositions = generateMelodies(generatedHamonies, 12);
		int[][] generatedMelodyPositions = new int[melodyPositions.length][];
		for (int j = 0; j < melodyPositions.length; j++) {
			int[] melody = generateMelody(melodyPositions[j], 6, 4);
			generatedMelodyPositions[j] = melody;
		}
		int[] harmonies = {0,24,36,48,72,96, 108,120,144,168,192};
		int[][] melodies = {{18,24},{0,6,18},{6,12},{0, 18, 24},{0,24},{0,12},{0,12},{0, 18, 24},{0,24},{}};
//		int[][] melodies2 = {{18,24},{0, 12},{0, 12},{12,24},{0,12},{0,12},{0,12},{0,12},{0,12,24},{}};

//		BeginEndChordGenerator generator = beginEndChordGenerator;
		
//		TonalChordGenerator tonalChordGenerator = new TonalChordGenerator(harmonies, musicProperties);
//		tonalChordGenerator.setChords(TonalChords.getTriads(0));
//		Generator generator = tonalChordGenerator;
		
		Generator generator = new RandomNotesGenerator(generatedHamonies, musicProperties);
		generator.generateHarmonicMelodiesForVoice(generatedMelodyPositions, 3);
//		generator.generateHarmonicMelodiesForVoice(melodies2, 4);
		
//		Generator generator = new DiffSizeGenerator(harmonies, musicProperties);
//		Generator generator = new PerleChordGenerator(harmonies, musicProperties);
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
	    harmonyNoteToPitch.setParameter("probabilityHarmonyNoteToPitch", 1.0);
	    harmonyNoteToPitch.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    harmonyNoteToPitch.setOuterBoundaryIncluded(false);//default == true;
	    
	    swapHarmonyNotes.setParameter("probabilitySwap", 1.0);
	    swapHarmonyNotes.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
	    
	    //melody
	    melodyNoteToHarmonyNote.setParameter("probabilityMelodyNoteToHarmonyNote", 1.0);
	    melodyNoteToHarmonyNote.setAllowedMelodyMutationIndexes(allowedDefaultIndexes);
//	    melodyNoteToHarmonyNote.setOuterBoundaryIncluded(false);
	    
	    oneNoteMutation.setParameter("probabilityOneNote", 1.0);
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
	
	private int[] generateHarmonyPositions(int minimumLength, int maxHarmonies, int bars){
		int limit = (12/minimumLength) * musicProperties.getNumerator() * bars;
		IntStream intStream = random.ints(limit, 0, limit);
		List<Integer> positions = intStream
				.distinct()
				.map(i -> i * minimumLength)
				.boxed()
				.collect(Collectors.toList());
		int max = (maxHarmonies > positions.size())?positions.size():maxHarmonies;
		positions = positions.subList(0, max + 1);
		positions.sort(Comparator.naturalOrder());
		int[] pos = new int[positions.size()];
		for (int j = 0; j < pos.length; j++) {
			pos[j] = positions.get(j);
		}
		return pos;
	}
	
	private int[][] generateMelodies(int[] harmonyPositions, int minimumLength){
		int[][] melodyPositions = new int[harmonyPositions.length - 1][];
		for (int i = 0; i < harmonyPositions.length - 1; i++) {
			int[] melPosition = new int[2];
			melPosition[0] = 0;
			melPosition[1] = harmonyPositions[i + 1] - harmonyPositions[i];
			melodyPositions[i] = melPosition;
		}
		return melodyPositions;
	}
	
	private int[] generateMelody(int[] harmony, int minimumLength, int maxMelodyNotes){
		int[] pos = null;
		int positionsInHarmony = ((harmony[1] - harmony[0])/minimumLength) - 1;//minus first position
		int limit = random.nextInt(positionsInHarmony + 1);
		if (limit > 0) {
			int from = ((harmony[0])/minimumLength) + 1;
			int toExlusive = (int)Math.ceil(harmony[1]/(double)minimumLength);
			IntStream intStream = random.ints(limit,from,toExlusive);
			List<Integer> positions = intStream
					.distinct()
					.map(i -> i * minimumLength)
					.boxed()
					.collect(Collectors.toList());
			int max = (maxMelodyNotes > positions.size())?positions.size():maxMelodyNotes;
			positions = positions.subList(0, max);
			positions.sort(Comparator.naturalOrder());
			pos = new int[positions.size() + 2];
			pos[0] = harmony[0];
			pos[pos.length - 1] = harmony[1];
			for (int j = 1; j < pos.length - 1; j++) {
				pos[j] = positions.get(j - 1);
			}
			return pos;
		} 
		return harmony;
	}
	
}
