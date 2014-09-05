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
import neo.data.melody.HarmonicMelody;
import neo.data.melody.pitchspace.PitchSpace;
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
		List<Note> melodyNotes = copyNotes(harmonicMelody.getMelodyNotes());
		Note harmonyNote = harmonicMelody.getHarmonyNote().copy();
		return new HarmonicMelody(harmonyNote, melodyNotes, harmonicMelody.getVoice(), harmonicMelody.getPosition());
	}
	
	private Harmony copyHarmony(Harmony harmony){
		List<HarmonicMelody> harmonicMelodies = new ArrayList<>();
		for (HarmonicMelody harmonicMelody : harmony.getHarmonicMelodies()) {
			HarmonicMelody newHarmonicMelody = copyHarmonicMelody(harmonicMelody);
			harmonicMelodies.add(newHarmonicMelody);
		}
		Harmony copyHarmony = new Harmony(harmony.getPosition(), harmony.getLength(), harmonicMelodies);
		PitchSpace newPitchSpace = clonePitchClass(harmony.getPitchSpace());
		newPitchSpace.setNotes(copyHarmony.getNotes());
		copyHarmony.setPitchSpace(newPitchSpace);
		copyHarmony.setPositionWeight(harmony.getPositionWeight());
		return copyHarmony;
	}

	private List<Note> copyNotes(List<Note> notePositions) {
		List<Note> newNotes = new ArrayList<Note>();
		int l = notePositions.size();
		for (int i = 0; i < l; i++) {	
			Note note = notePositions.get(i);
			Note newNote = note.copy();
			newNotes.add(newNote);
		}
		return newNotes;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PitchSpace clonePitchClass(PitchSpace pitchSpace) {
		PitchSpace newPitchSpaceStrategy = null;
		try {
			Class pitchSpaceStrategyClass = Class.forName(pitchSpace.getClass().getName());
			Constructor<PitchSpace> constructor = pitchSpaceStrategyClass.getConstructor(Integer[].class);
			newPitchSpaceStrategy = constructor.newInstance((Object)pitchSpace.getOctaveHighestPitchClassRange());
		} catch (InvocationTargetException |IllegalArgumentException |SecurityException | NoSuchMethodException 
				| ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("PitchSpace type error: " + pitchSpace.getClass().getName());
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

