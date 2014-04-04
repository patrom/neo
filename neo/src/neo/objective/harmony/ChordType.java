package neo.objective.harmony;

public enum ChordType {

	MAJOR(1,3),
	MINOR(1,3),
	HALFDIM(1,3),
	AUGM(1,3),
	CH0(0,0),
	CH1(0,1),
	CH2(0,2),
	CH3(0.5,3),
	CH4(0.5,4),
	CH5(0,5),
	MAJOR7(1,4),
	MINOR7(1,4),
	DOM7(1,4),
	HALFDIM7(1,4),
	DIM(1,4);
	
	
	private double dissonance;
	private int size;
	
	ChordType(double dissonance, int size){
		this.dissonance = dissonance;
		this.size = size;
	}
	
	public double getDissonance() {
		return dissonance;
	}
	
	public int getSize() {
		return size;
	}
}
