package neo.objective.objective;

import neo.generator.MusicProperties;
import neo.model.Motive;

public abstract class Objective {
	
	protected MusicProperties musicProperties;
	protected Motive motive;
	
	public Objective(Motive motive){
		this.motive = motive;
	}

	public Objective(MusicProperties musicProperties, Motive motive) {
		this.musicProperties = musicProperties;
		this.motive = motive;
	}

	public abstract double evaluate();
}
