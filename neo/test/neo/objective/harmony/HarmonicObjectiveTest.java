package neo.objective.harmony;

import static neo.model.harmony.HarmonyBuilder.harmony;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import neo.DefaultConfig;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.objective.Objective;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class HarmonicObjectiveTest extends JFrame {
	
	@Autowired
	private Objective harmonicObjective;
	@Autowired
	private MusicProperties musicProperties;
	
	@Before
	public void setup() {
		musicProperties.setMinimumLength(6);
	}

	@Test
	public void testEvaluate() {
		List<Harmony> harmonies = new ArrayList<>();
		harmonies.add(harmony().pos(0).len(6).notes(0,4,7).positionWeight(1.0).build());
		harmonies.add(harmony().pos(6).len(6).notes(1,4,6).positionWeight(0.5).build());
		harmonies.add(harmony().pos(12).len(12).notes(11,2,7).positionWeight(1.0).build());
		double harmonicValue = harmonicObjective.evaluate(new Motive(harmonies));
	}

}
