package test.neo.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.evaluation.FitnessEvaluationTemplate;
import neo.evaluation.MusicProperties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.neo.AbstractTest;

public class FitnessEvaluationTest extends AbstractTest{

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTest.class.getName());
	
	private FitnessEvaluationTemplate fitnessEvaluation;
	private MusicProperties musicProperties;
	private Map<Integer, Double> rhythmWeightValues;

	@Before
	public void setUp(){
		musicProperties = new MusicProperties();
		musicProperties.setMinimumLength(6);
		rhythmWeightValues = new TreeMap<>();
		rhythmWeightValues.put(0, 1.0);
		rhythmWeightValues.put(6, 0.5);
		rhythmWeightValues.put(12, 1.0);
		rhythmWeightValues.put(18, 0.5);
		rhythmWeightValues.put(24, 1.0);
		rhythmWeightValues.put(30, 0.5);
		musicProperties.setRhythmWeightValues(rhythmWeightValues);
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(Examples.getChord(0,6, 0,4,7));
		harmonies.add(Examples.getChord(6,6, 1,4,6));
		harmonies.add(Examples.getChord(12,12, 11,2,7));
		harmonies.add(Examples.getChord(24,12, 0,4,9));
		fitnessEvaluation = new FitnessEvaluationTemplate(musicProperties, new Motive(harmonies));
	}
	
	@After
	public void objectivesInfo() {
		LOGGER.info(objectives.toString());
	}
	
	@Test
	public void evaluationTest() {
		objectives = fitnessEvaluation.evaluate();
	}

}
