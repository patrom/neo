package neo.variation;

import java.util.ArrayList;
import java.util.List;

import neo.model.note.Note;
import neo.util.Util;
import neo.variation.nonchordtone.DefaultVariation;
import neo.variation.nonchordtone.Variation;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Embellisher {

	@Autowired
	private VariationSelector variationSelector;
	@Autowired
	private DefaultVariation defaultVariation;
	
	public List<Note> embellish(List<Note> notes){
		if (notes.size() <= 1) {
			throw new IllegalArgumentException("size");
		}
		List<Note> embellishedMelody = new ArrayList<>();
		Note[] notePositions = notes.toArray(new Note[notes.size()]);
		for (int j = 0; j < notePositions.length - 1; j++) {
			Note note = notePositions[j];
			Note nextNote = notePositions[j + 1];
			int interval = nextNote.getPitch() - note.getPitch();
			Variation variation = selectVariation(interval);
			if (variation.getExcludedVoices().contains(note.getVoice())) {
				embellishedMelody.add(note.copy());
				continue;
			}
			List<Note> embellishedNotes  = variation.createVariation(note, nextNote);
			if (variation.isSecondNoteChanged()) {
				j++;
			}
			embellishedMelody.addAll(embellishedNotes);
		}
		embellishedMelody.add(notes.get(notes.size() - 1));//add last note of list
		return embellishedMelody;
	}

	private Variation selectVariation(int interval) {
		List<Variation> variations = new ArrayList<>();
		variations.add(defaultVariation);
		List<Integer> profiles = new ArrayList<>();
		profiles.add(defaultVariation.getProfile());
		List<Variation> intervalVariations = variationSelector.getIntervalVariations(interval);
		for (Variation variation : intervalVariations) {
			variations.add(variation);
			profiles.add(variation.getProfile());
		}
		Variation singleNote = variationSelector.getVariation();
		if (singleNote != null) {
			variations.add(singleNote);
			profiles.add(singleNote.getProfile());
		}
		int[] profilesArray = ArrayUtils.toPrimitive(profiles.toArray(new Integer[profiles.size()]));
		Variation variation = Util.selectFromListProbability(variations, profilesArray);
		return variation;
	}

}
