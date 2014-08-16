package neo.nsga;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import jmetal.base.Variable;
import jmetal.util.JMException;
import neo.data.Motive;
import neo.data.harmony.Harmony;
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
		harmonies.stream().sorted();
		Motive newMotive = new Motive(harmonies);
		newMotive.setMusicProperties(motive.getMusicProperties());
		return newMotive;
	}
	
	private HarmonicMelody copyHarmonicMelody(HarmonicMelody harmonicMelody) {
		List<Note> newNotes = copyNotes(harmonicMelody.getNotes());
		return new HarmonicMelody(newNotes, harmonicMelody.getVoice());
	}
	
	private Harmony copyHarmony(Harmony harmony){
		List<Note> newNotes = copyNotes(harmony.getNotes());
		Harmony.PitchSpaceStrategy pitchSpaceStrategy = harmony.getPitchSpaceStrategy();
		Harmony copyHarmony = new Harmony(harmony.getPosition(), harmony.getLength(), newNotes);
		for (HarmonicMelody harmonicMelody : harmony.getHarmonicMelodies()) {
			HarmonicMelody newHarmonicMelody = copyHarmonicMelody(harmonicMelody);
			copyHarmony.addHarmonicMelody(newHarmonicMelody);
		}
		Harmony.PitchSpaceStrategy newPitchSpaceStrategy = clonePitchClassStrategy(copyHarmony, pitchSpaceStrategy);
		copyHarmony.setPitchSpaceStrategy(newPitchSpaceStrategy);
		copyHarmony.setPositionWeight(harmony.getPositionWeight());
		return copyHarmony;
	}

	private List<Note> copyNotes(List<Note> notePositions) {
		List<Note> newNotes = new ArrayList<Note>();
		int l = notePositions.size();
		for (int i = 0; i < l; i++) {	
			Note newNote = new Note();
			Note note = notePositions.get(i);
			newNote.setLength(note.getLength());
			newNote.setPosition(note.getPosition());
			newNote.setPitch(note.getPitch());
			newNote.setPitchClass(note.getPitchClass());
			newNote.setDuration(note.getDuration());
			newNote.setVoice(note.getVoice());
			newNote.setInnerMetricWeight(note.getInnerMetricWeight());
			newNote.setRhythmValue(note.getRhythmValue());
			newNote.setDynamic(note.getDynamic());
			newNote.setOctave(note.getOctave());
			newNotes.add(newNote);
		}
		return newNotes;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Harmony.PitchSpaceStrategy clonePitchClassStrategy(
			Harmony harmony, Harmony.PitchSpaceStrategy pitchSpaceStrategy) {
		Harmony.PitchSpaceStrategy newPitchSpaceStrategy = null;
		try {
			Class<?> innerClass = Class.forName(pitchSpaceStrategy.getClass().getName());
			Constructor<?> ctor = innerClass.getDeclaredConstructor(Harmony.class, Integer[].class);
			newPitchSpaceStrategy = (Harmony.PitchSpaceStrategy) ctor.newInstance(harmony, pitchSpaceStrategy.getOctaveHighestPitchClassRange());
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

