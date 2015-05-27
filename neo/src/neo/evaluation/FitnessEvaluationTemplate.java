package neo.evaluation;

import java.util.List;
import java.util.logging.Logger;

import neo.model.Motive;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.objective.Objective;
import neo.variation.Embellisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FitnessEvaluationTemplate {

	private static Logger LOGGER = Logger.getLogger(FitnessEvaluationTemplate.class.getName());

	@Autowired
	private Objective harmonicObjective;
	@Autowired
	private Objective melodicObjective;
	@Autowired
	private Objective voiceLeadingObjective;
	@Autowired
	private Objective tonalityObjective;
	@Autowired
	private Embellisher embellisher;
	private boolean nonChordTones = false;

	public FitnessObjectiveValues evaluate(Motive motive) {
		motive.extractMelodies();
		motive.updateInnerMetricWeightMelodies();
		motive.updateInnerMetricWeightHarmonies();
		if (nonChordTones) {
			embellish(motive);
		}
		motive.extractChords();
		return evaluateObjectives(motive);
	}

	private void embellish(Motive motive) {
		List<Melody> melodies = motive.getMelodies();
		for(Melody melody: melodies){
			List<Note> notes =  melody.getMelodieNotes();
			List<Note> embellishedNotes = embellisher.embellish(notes);
//			System.out.println(embellishedNotes);
		}
		
	}

	private FitnessObjectiveValues evaluateObjectives(Motive motive) {
		double harmony = harmonicObjective.evaluate(motive);
		LOGGER.fine("harmonic: " + harmony);

		double voiceLeading = voiceLeadingObjective.evaluate(motive);
		LOGGER.fine("voiceLeadingSize: " + voiceLeading);
		
		double melodic = melodicObjective.evaluate(motive);
		LOGGER.fine("melodic = " + melodic);
		
		double tonality = tonalityObjective.evaluate(motive);
		LOGGER.fine("tonality = " + tonality);
		
		FitnessObjectiveValues fitnessObjectives = new FitnessObjectiveValues();
		fitnessObjectives.setHarmony(harmony);
		fitnessObjectives.setMelody(melodic);
		fitnessObjectives.setVoiceleading(voiceLeading);
		fitnessObjectives.setTonality(tonality);

		//constraints
//		objectives[5] = lowestIntervalRegisterValue;
//		objectives[6] = repetitionsrhythmsMean;	//only for small motives (5 - 10 notes)
//		objectives[7] = repetitionsPitchesMean;	//only for small motives (5 - 10 notes)
		return fitnessObjectives;	
	}

}

