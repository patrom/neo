package neo.nsga;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jmetal.base.Variable;
import jmetal.util.JMException;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.melody.HarmonicMelody;
import neo.data.melody.Melody;
import neo.data.note.Note;

public class MusicVariable extends Variable {

	private static Logger LOGGER = Logger.getLogger(MusicVariable.class.getName());
	private Motive motive;
	
	public MusicVariable(MusicVariable musicVariable) throws JMException {
		this.motive = cloneMotives(musicVariable.getMotive());
	}

	public MusicVariable(Motive motive) {
		this.motive = motive;
	}
	
	public Motive getMotive() {
		return motive;
	}

	protected Motive cloneMotives(Motive motive) {
		List<Harmony> harmonies = new ArrayList<>();
		for (Harmony harmony : motive.getHarmonies()) {
			harmonies.add(copyHarmony(harmony));
		}
		
		List<Melody> melodies = new ArrayList<>();
		for (Melody melody : motive.getMelodies()) {
			List<HarmonicMelody> harmonicMelodies = melody.getHarmonicMelodies();
			List<HarmonicMelody> newHarmonicMelodies = new ArrayList<>();
			for (HarmonicMelody harmonicMelody : harmonicMelodies) {
				Harmony copyHarmony = findHarmoyAtPosition(harmonies, harmonicMelody.getPosition());
				HarmonicMelody newHarmonicMelody = copyHarmonicMelody(harmonicMelody, copyHarmony);
				newHarmonicMelodies.add(newHarmonicMelody);
			}
			Melody newMelody = new Melody(newHarmonicMelodies, melody.getVoice());
			melodies.add(newMelody);
		}
		harmonies.stream().sorted();
		Motive newMotive = new Motive(harmonies, melodies);
		newMotive.setMusicProperties(motive.getMusicProperties());
		return newMotive;
	}
	
	private Harmony findHarmoyAtPosition(List<Harmony> harmonies, int position){
		for (Harmony harmony : harmonies) {
			if (harmony.getPosition() == position) {
				return harmony;
			}
		}
		throw new IllegalArgumentException("No harmony for position: " + position);
	}

	private HarmonicMelody copyHarmonicMelody(HarmonicMelody harmonicMelody,
			Harmony copyHarmony) {
		List<Note> newNotePositions = copyNotes(harmonicMelody.getNotes());
		return new HarmonicMelody(newNotePositions, copyHarmony, harmonicMelody.getVoice());
	}
	
	private Harmony copyHarmony(Harmony harmony){
		List<Note> newNotePositions = copyNotes(harmony.getNotes());
		PitchSpaceStrategy pitchSpaceStrategy = harmony.getPitchSpaceStrategy();
		PitchSpaceStrategy newPitchSpaceStrategy = clonePitchClassStrategy(newNotePositions, pitchSpaceStrategy);
		Harmony copyHarmony = new Harmony(harmony.getPosition(), harmony.getLength(), newNotePositions, newPitchSpaceStrategy);
		copyHarmony.setPositionWeight(harmony.getPositionWeight());
		return copyHarmony;
	}

	private List<Note> copyNotes(List<Note> notePositions) {
		List<Note> newNotePositions = new ArrayList<Note>();
		int l = notePositions.size();
		for (int i = 0; i < l; i++) {	
			Note notePosition = new Note();
			Note notePos = notePositions.get(i);
			notePosition.setLength(notePos.getLength());
			notePosition.setPosition(notePos.getPosition());
			notePosition.setPitch(notePos.getPitch());
			notePosition.setPitchClass(notePos.getPitchClass());
			notePosition.setDuration(notePos.getDuration());
			notePosition.setVoice(notePos.getVoice());
			notePosition.setInnerMetricWeight(notePos.getInnerMetricWeight());
			notePosition.setRhythmValue(notePos.getRhythmValue());
			notePosition.setDynamic(notePos.getDynamic());
			notePosition.setHarmony(notePos.getHarmony());
			newNotePositions.add(notePosition);
		}
		return newNotePositions;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PitchSpaceStrategy clonePitchClassStrategy(List<Note> newNotePositions,
			PitchSpaceStrategy pitchSpaceStrategy) {
		PitchSpaceStrategy newPitchSpaceStrategy = null;
		try {
			Class pitchSpaceStrategyClass = Class.forName(pitchSpaceStrategy.getClass().getName());
			Constructor<?> constructor = pitchSpaceStrategyClass.getConstructor(List.class, Integer[].class);
			newPitchSpaceStrategy = (PitchSpaceStrategy) constructor.newInstance(newNotePositions, pitchSpaceStrategy.getOctaveHighestPitchClassRange());
		} catch (InvocationTargetException |IllegalArgumentException |SecurityException | NoSuchMethodException 
				| ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOGGER.severe("PitchSpace type error");
		}
		return newPitchSpaceStrategy;
	}

	@Override
	public Variable deepCopy() {
		try {
	      return new MusicVariable(this);
	    } catch (JMException e) {
	    	LOGGER.severe("MusicVariable.deepCopy.execute: JMException");
	      return null ;
	    }
	}
	
}

