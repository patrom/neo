package neo.instrument;

import java.util.ArrayList;
import java.util.List;

public class Ensemble {

	public static List<Instrument> getStringQuartet(){
		//reverse numbering Kontakt!
		List<Instrument> stringQuartet = new ArrayList<Instrument>();
		stringQuartet.add(new KontaktLibViolin(0, 3));
//		stringQuartet.add(new KontaktLibViolin(0, 1));
		stringQuartet.add(new KontaktLibAltViolin(0, 2));
		stringQuartet.add(new KontaktLibCello(0, 0));
		return stringQuartet;
	}
	
	public static List<Instrument> getWindQuartet(){
		List<Instrument> instruments = new ArrayList<Instrument>();
		instruments.add(new KontaktLibFlute(0, 3));
//		instruments.add(new KontaktLibOboe(0, 1));
		instruments.add(new KontaktLibClarinet(0, 2));
		instruments.add(new KontaktLibBassoon(0, 0));
		return instruments;
	}
}
