package neo.evaluation;

import static neo.data.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import neo.AbstractTest;
import neo.data.Motive;
import neo.data.harmony.Harmony;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FitnessEvaluationTest extends AbstractTest{

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTest.class.getName());
	
	private FitnessEvaluationTemplate fitnessEvaluation;
	private MusicProperties musicProperties;
	private Motive motive;

	@Before
	public void setUp(){
		musicProperties = new MusicProperties();
		musicProperties.setMinimumLength(6);
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(6).notes(0,4,7).positionWeight(1.0).build());
		harmonies.add(harmony().pos(6).len(6).notes(1,4,6).positionWeight(0.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).positionWeight(1.0).build());
		harmonies.add(harmony().pos(24).len(12).notes(0,4,9).positionWeight(0.5).build());
		motive = new Motive(harmonies);
		motive.setMusicProperties(musicProperties);
		fitnessEvaluation = new FitnessEvaluationTemplate(musicProperties, motive);
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
