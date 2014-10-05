package neo.model.harmony;

public enum ChordType {

	MAJOR(0.8,3),
	MINOR(0.8,3),
	HALFDIM(0.8,3),
	AUGM(0.5,3),
	DOM(0.8,3),
	CH0(0,0),
	CH1(0,1),
	CH2(0,2),
	CH3(0.5,3),
	CH4(0.5,4),
	CH5(0.5,5),
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
