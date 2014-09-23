package neo.model.setclass;

public class PcSetUnorderedProperties {

	private TnTnIType setClass;
	private Set prime;
	
	public PcSetUnorderedProperties(int[] set) {
		super();
		this.setClass = new TnTnIType(set);
		setClass.getTnTnIType();
		prime = new Set();
	}

	public String getForteName() {
		return setClass.getPrimeByTnTnI().name;
	}

	public String[] getSetClassProperties() {
		prime = setClass.getPrimeByTnTnI();
		String tntypeString = setClass.tntypeToString();
		String tnitypeString = setClass.tnitypeToString();
		String tntnitypeString = setClass.tntnitypeToString();
		 
		// I-vector
		String ivectorString = prime.ivectorToString();
		String m7m7iString = "N/A";
		// M7/M7I
		if (prime.set.length <= 6){
			m7m7iString = prime.m7setToString();
		}
		// Symmetry
		String symmetryString = prime.symmetry[0] + ", " + prime.symmetry[1];
		String combinatorialString = "N/A";
		// Combinatoriality
		if (prime.set.length == 6){
			combinatorialString = prime.combinatorialToString();
		}	
		String[] properties = {prime.name,
				       tntypeString,
				       tnitypeString,
				       tntnitypeString,
				       ivectorString,
				       m7m7iString,
				       symmetryString,
				       combinatorialString};
		return properties;
	}
}
