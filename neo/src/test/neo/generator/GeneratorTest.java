package test.neo.generator;

import neo.data.Motive;
import neo.data.note.Scale;
import neo.generator.Generator;

import org.junit.Assert;
import org.junit.Test;

public class GeneratorTest {

	@Test
	public void testGenerateMotive() {
		int chordSize = 4;
		int octave = 6;
		int[] rhythmGeneratorTemplate = {0,12,18,24};
		Motive motive = Generator.generateMotive(new Scale(Scale.MAJOR_SCALE), rhythmGeneratorTemplate, chordSize, octave);
		Assert.assertEquals("", chordSize, motive.getMelodies().size());
	}

}
