package neo.midi;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import neo.model.note.Note;

public class HarmonyCollector implements Collector<Note, HarmonyInstrument, List<HarmonyInstrument>>{

	@Override
	public Supplier<HarmonyInstrument> supplier() {
		return HarmonyInstrument::new;
	}

	@Override
	public BiConsumer<HarmonyInstrument, Note> accumulator() {
		return (harmony, note) -> { 
				harmony.setPosition(note.getPosition());
				harmony.addNote(note);
			};
	}

	@Override
	public BinaryOperator<HarmonyInstrument> combiner() {
		return (left, right) -> {
            left.setNotes(right.getNotes());
            return left;
        };
	}

	@Override
	public Function<HarmonyInstrument, List<HarmonyInstrument>> finisher() {
		return (harmony) -> {
				List<HarmonyInstrument> list = new ArrayList<>();
				list.add(harmony);
				return list;
			};
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		return EnumSet.of(Characteristics.UNORDERED);
	}
	
	public static <T> Collector<Note, HarmonyInstrument, List<HarmonyInstrument>> toHarmonyCollector() {
	    return new HarmonyCollector();
	}

}
