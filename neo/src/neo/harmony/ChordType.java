package neo.harmony;

public enum ChordType {

	MAJOR(1),
	MINOR(2),
	HALFDIM(3),
	AUGM(4),
	CH0(0),
	CH1(0),
	CH2(0),
	CH3(0),
	CH4(0),
	CH5(0),
	MAJOR7(0),
	MINOR7(0),
	DOM7(0),
	HALFDIM7(0),
	DIM(0);
	
	
	private double value;
	
	ChordType(double value){
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
}
