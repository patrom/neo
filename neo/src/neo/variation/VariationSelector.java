package neo.variation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import neo.util.RandomUtil;
import neo.variation.nonchordtone.Variation;

import org.springframework.stereotype.Component;

@Component
public class VariationSelector {

	@Resource(name="intervalVariations")
	private Map<Integer, List<Variation>> intervalVariations;
	@Resource(name="variations")
	private List<Variation> variations;
	
	public Variation getVariation(){
		if (variations.isEmpty()) {
			return null;
		}
		return RandomUtil.getRandomFromList(variations);
	}
	
	public List<Variation> getIntervalVariations(Integer interval){
		List<Variation> singleNoteVariations = intervalVariations.getOrDefault(interval, Collections.emptyList());
		return singleNoteVariations;
	}
	
}
