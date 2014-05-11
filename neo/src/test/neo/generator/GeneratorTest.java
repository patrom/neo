package test.neo.generator;

import neo.data.Motive;
import neo.data.note.Scale;
import neo.evaluation.RhythmPosition;
import neo.generator.Generator;

import org.junit.Assert;
import org.junit.Test;

public class GeneratorTest {

	@Test
	public void testGenerateMotive() {
		int chordSize = 4;
		int octave = 6;
		RhythmPosition[] rhythmTemplate = new RhythmPosition[4];
		rhythmTemplate[0] = new RhythmPosition(0, 25);
		rhythmTemplate[1] = new RhythmPosition(12, 25);
		rhythmTemplate[2] = new RhythmPosition(18, 25);
		rhythmTemplate[3] = new RhythmPosition(24, 25);
		Motive motive = Generator.generateMotive(new Scale(Scale.MAJOR_SCALE), rhythmTemplate, chordSize, octave);
		Assert.assertEquals("", chordSize, motive.getMelodies().size());
	}

}
