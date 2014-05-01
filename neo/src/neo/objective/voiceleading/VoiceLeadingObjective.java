package neo.objective.voiceleading;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;
import neo.objective.harmony.Chord;

public class VoiceLeadingObjective extends Objective {

	public VoiceLeadingObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		Map<Double, List<Harmony>> map = motive.getHarmonies().stream().collect(Collectors.groupingBy(ch -> ch.getBeat(musicProperties.getHarmonyBeatDivider())));
		Map<Double, Chord> bestChordMap = new TreeMap<>();
		for (Entry<Double, List<Harmony>> entry: map.entrySet()) {
			List<Harmony> list = entry.getValue();
			Optional<Chord> bestChord = list.stream().map(t -> t.toChord()).max(Comparator.comparing(Chord::getWeight));
			if(bestChord.isPresent()) {
				bestChordMap.put(entry.getKey(), bestChord.get());
			}
		}
		Chord[] chords = new Chord[bestChordMap.size()];
		chords = bestChordMap.values().toArray(chords);
//		map.forEach((k,v) -> v.stream().map(ch -> ch.toChord()));
		bestChordMap.forEach((p,ch) -> System.out.println(p + ": " + ch.getChordType()));
		int totalSize = 0;
		for(int i = 0; i < chords.length - 1; i++){
			VoiceLeadingSize minimalVoiceLeadingSize = VoiceLeading.caculateSize(chords[i].getPitchClassMultiSet(), chords[i + 1].getPitchClassMultiSet());
//			System.out.print(minimalVoiceLeadingSize.getVlSource());
//			System.out.print(minimalVoiceLeadingSize.getVlTarget());
//			System.out.print(minimalVoiceLeadingSize.getSize());
//			System.out.println();
			totalSize = totalSize + minimalVoiceLeadingSize.getSize();
		}
		return totalSize/(chords.length - 1);
	}

}
