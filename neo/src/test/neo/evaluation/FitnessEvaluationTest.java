package test.neo.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import neo.data.Motive;
import neo.data.harmony.Examples;
import neo.data.harmony.Harmony;
import neo.evaluation.FitnessEvaluation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.neo.AbstractTest;

public class FitnessEvaluationTest extends AbstractTest{

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTest.class.getName());
	
	FitnessEvaluation fitnessEvaluation;
	
	@Before
	public void setUp(){
		fitnessEvaluation = new FitnessEvaluation();
	}
	
	@After
	public void objectivesInfo() {
		LOGGER.info("test" + "\n"  
				+ "Harmony: " + objectives[0] + ", " 
				+ "VoiceLeading: " + objectives[1] + ", " 
				+ "Melody: " + objectives[2] + ", "
				+ "Rhythm: " + objectives[3] + ", "
				+ "tonality: " + objectives[4] + "\n");
//				+ "Constraints: lowest interval register: " + objectives[5] + ", "
//				+ "repetitions Pitches: " + objectives[6] + ", "
//				+ "repetitions rhythms: " + objectives[7]);
	}
	
	@Test
	public void evaluationTest() {
		List<Harmony> list = new ArrayList<>();
		list.add(Examples.getChord(0,6, 0,4,7));
		list.add(Examples.getChord(6,6, 1,4,6));
		list.add(Examples.getChord(12,12, 11,2,7));
		list.add(Examples.getChord(24,12, 0,4,9));
		Motive motive = new Motive(list);
		objectives = fitnessEvaluation.evaluate(motive);
	}

}
