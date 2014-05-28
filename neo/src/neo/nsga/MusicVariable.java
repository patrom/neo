package neo.nsga;

import java.util.ArrayList;
import java.util.List;

import jmetal.base.Variable;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import neo.data.Motive;
import neo.data.harmony.Harmony;
import neo.data.harmony.pitchspace.PitchSpaceStrategy;
import neo.data.harmony.pitchspace.UniformPitchSpace;
import neo.data.note.NotePos;

public class MusicVariable extends Variable {

	private Motive motive;
	
	public Motive getMotive() {
		return motive;
	}
	
	/**
	 * Offspring cloning!
	 * @param musicVariable
	 * @throws JMException
	 */
	public MusicVariable(MusicVariable musicVariable) throws JMException {
		cloneMotives(musicVariable.getMotive());
	}

	public MusicVariable(Motive motive) {
		this.motive = motive;
	}

	private void cloneMotives(Motive motive) {
		List<Harmony> harmonies = new ArrayList<>();
		for (Harmony harmony : motive.getHarmonies()) {
			List<NotePos> notePositions = harmony.getNotes();
			List<NotePos> newNotePositions = new ArrayList<NotePos>();
			int l = notePositions.size();
			for (int i = 0; i < l; i++) {		
				NotePos notePosition = new NotePos();
				notePosition.setLength(notePositions.get(i).getLength());
				notePosition.setPosition(notePositions.get(i).getPosition());
				notePosition.setPitch(notePositions.get(i).getPitch());
				notePosition.setPitchClass(notePositions.get(i).getPitchClass());
				notePosition.setDuration(notePositions.get(i).getDuration());
				notePosition.setVoice(notePositions.get(i).getVoice());
				notePosition.setInnerMetricWeight(notePositions.get(i).getInnerMetricWeight());
				notePosition.setPositionWeight(notePositions.get(i).getPositionWeight());
				notePosition.setRhythmValue(notePositions.get(i).getRhythmValue());
				notePosition.setWeight(notePositions.get(i).getWeight());
				notePosition.setDynamic(notePositions.get(i).getDynamic());
				newNotePositions.add(notePosition);
			}
			PitchSpaceStrategy pitchSpaceStrategy = harmony.getPitchSpaceStrategy();
			PitchSpaceStrategy newPitchSpaceStrategy = null;
			if (pitchSpaceStrategy instanceof UniformPitchSpace) {
				newPitchSpaceStrategy = new UniformPitchSpace(newNotePositions, pitchSpaceStrategy.getOctaveHighestPitchClass());
				
			}
			Harmony copyHarmony = new Harmony(harmony.getPosition(), harmony.getLength(), newNotePositions, newPitchSpaceStrategy);
			harmonies.add(copyHarmony);
		}
		this.motive = new Motive(harmonies);
	}

	@Override
	public Variable deepCopy() {
		try {
	      return new MusicVariable(this);
	    } catch (JMException e) {
	      Configuration.logger_.severe("MusicVariable.deepCopy.execute: JMException");
	      return null ;
	    }
	}
	
}

