package neo.generator;

import static org.junit.Assert.assertTrue;
import neo.AbstractTest;
import neo.DefaultConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DefaultConfig.class, loader = SpringApplicationContextLoader.class)
public class TonalChordGeneratorTest extends AbstractTest{

	private TonalChordGenerator tonalChordGenerator;
	private int[][] positions;
	
	@Before
	public void setUp() throws Exception {
		positions = new int[][]{{0,0,48},{48,48,96},{96,96,144},{144,144,192}, {192,192,240}};
		musicProperties.setMeasureWeights(new double[]{1.0, 0.5, 0.75, 0.5, 1.0, 0.5, 0.75, 0.5});
		musicProperties.setChordSize(4);
	}

	@Test
	public void testPickRandomChords() {
		tonalChordGenerator = new TonalChordGenerator(positions, musicProperties);
		int[] chord = tonalChordGenerator.pickRandomChords();
		assertTrue(chord.length == musicProperties.getChordSize());
	}

}
