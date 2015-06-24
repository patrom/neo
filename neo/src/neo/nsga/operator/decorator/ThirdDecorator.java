package neo.nsga.operator.decorator;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jmetal.core.Solution;
import neo.generator.MusicProperties;
import neo.model.Motive;
import neo.model.harmony.Harmony;
import neo.model.melody.HarmonicMelody;
import neo.model.note.Note;
import neo.model.note.Scale;
import neo.nsga.MusicVariable;

@Component(value="ThirdDecorator")
public class ThirdDecorator implements Decorator{
	
	@Autowired 
	private MusicProperties musicProperties;

	@Override
	public void decorate(Solution solution) {
		Scale scale = musicProperties.getScale();
		int step = 2;
		Consumer<Note> noteConsumer = (note -> {
			int pitchClass = scale.pickLowerStepFromScale(note.getPitchClass(), step);
			note.setPitchClass(pitchClass);
//			note.setPitch(note.getPitch() - pitchClass);
			});
		
		int voice = 1;
		int voiceToDecorate = voice - 1;
		Motive motive = ((MusicVariable)solution.getDecisionVariables()[0]).getMotive();
		List<Harmony> harmonies = motive.getHarmonies();
		for (Harmony harmony : harmonies) {
			decorateHarmony(harmony, voice, voiceToDecorate, noteConsumer);
		}
	}

	protected void decorateHarmony(Harmony harmony, int voice, int voiceToDecorate, Consumer<Note> noteConsumer) {
		Optional<HarmonicMelody> harmOptional = harmony.getHarmonicMelodies().stream().filter(h -> h.getVoice() == voice).findFirst();
		if (harmOptional.isPresent()) {
			HarmonicMelody decoratedHarmonicMelody = updateCopyHarmonicMelody(harmOptional.get(), voiceToDecorate, noteConsumer);
			harmony.replaceHarmonicMelody(decoratedHarmonicMelody);
		}
	}

	protected HarmonicMelody updateCopyHarmonicMelody(HarmonicMelody harmonicMelody, int voice, Consumer<Note> noteConsumer) {
		HarmonicMelody copyHarmonicMelody = harmonicMelody.copy(voice);
		copyHarmonicMelody.getMelodyNotes().forEach(noteConsumer);
		noteConsumer.accept(copyHarmonicMelody.getHarmonyNote());
		return copyHarmonicMelody;
	}

}
