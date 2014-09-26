package neo.generator;

import java.util.Map;
import java.util.TreeMap;

public class RhythmWeight {

	public static Map<Integer, Double> generateRhythmWeight(int bars, double[] barWeights, int minimumLength){
		Map<Integer, Double> rhythmWeightValues = new TreeMap<>();
		int length = bars * barWeights.length;
		for (int i = 0; i < length ; i++) {
			rhythmWeightValues.put(i * minimumLength, barWeights[i % barWeights.length]);
		}
		return rhythmWeightValues;
	}
	
	public static void main(String[] args) {
		double[] barWeights = {1.0, 0.5, 0.75, 0.5};
		Map<Integer, Double> map = generateRhythmWeight(5, barWeights, 6);
		System.out.println(map);
	}
}
