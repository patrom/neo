package neo.objective.voiceleading;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.Optional;
import java.util.TreeMap;

import neo.data.Motive;
import neo.data.harmony.Chord;
import neo.data.harmony.Harmony;
import neo.evaluation.MusicProperties;
import neo.objective.Objective;

public class VoiceLeadingObjective extends Objective {

	private static Logger LOGGER = Logger.getLogger(VoiceLeadingObjective.class.getName());
	
	public VoiceLeadingObjective(MusicProperties musicProperties, Motive motive) {
		super(musicProperties, motive);
	}

	@Override
	public double evaluate() {
		Map<Double, List<Harmony>> map = motive.getHarmonies().stream().collect(groupingBy(ch -> ch.getBeat(musicProperties.getHarmonyBeatDivider())));
		Map<Double, Chord> bestChordMap = new TreeMap<>();
		for (Entry<Double, List<Harmony>> entry: map.entrySet()) {
			List<Harmony> harmonies = entry.getValue();
			Optional<Chord> bestChord = harmonies.stream().map(t -> t.getChord()).max(comparing(Chord::getWeight));
			if(bestChord.isPresent()) {
				bestChordMap.put(entry.getKey(), bestChord.get());
			}
		}
		Chord[] chords = new Chord[bestChordMap.size()];
		chords = bestChordMap.values().toArray(chords);
		bestChordMap.forEach((p,ch) -> LOGGER.finest(p + ": " + ch.getChordType()));
		double totalSize = 0;
		for(int i = 0; i < chords.length - 1; i++){
			VoiceLeadingSize minimalVoiceLeadingSize = VoiceLeading.caculateSize(chords[i].getPitchClassMultiSet(), chords[i + 1].getPitchClassMultiSet());
			totalSize = totalSize + minimalVoiceLeadingSize.getSize();
		}
		return totalSize/(chords.length - 1);
	}

}
