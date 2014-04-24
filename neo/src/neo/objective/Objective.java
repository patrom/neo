package neo.objective;

import neo.data.Motive;
import neo.evaluation.MusicProperties;

public abstract class Objective {
	
	protected MusicProperties musicProperties;
	
	public Objective(){
	}

	public Objective(MusicProperties musicProperties) {
		this.musicProperties = musicProperties;
	}

	public abstract double evaluate(Motive motive);
}
