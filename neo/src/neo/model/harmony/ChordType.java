package neo.model.harmony;

public enum ChordType {
	
	CH0(0),
	CH1(1),
	CH2(2),
	
	CH3(3),
	MAJOR(3),
	MINOR(3),
	HALFDIM(3),
	AUGM(3),
	DOM(3),
	
	
	CH4(4),
	MAJOR7(4),
	MINOR7(4),
	DOM7(4),
	HALFDIM7(4),
	DIM(4),
	
	CH5(5);
	
	private int size;
	
	ChordType(int size){
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
}
