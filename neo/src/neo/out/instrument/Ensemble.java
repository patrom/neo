package neo.out.instrument;

import java.util.ArrayList;
import java.util.List;

//reverse numbering Kontakt!
public class Ensemble {

	public static List<Instrument> getStringQuartet(){
		List<Instrument> stringQuartet = new ArrayList<Instrument>();
		stringQuartet.add(new KontaktLibViolin(3, 0));
		stringQuartet.add(new KontaktLibViolin(2, 0));
		stringQuartet.add(new KontaktLibViola(1, 1));
		stringQuartet.add(new KontaktLibCello(0, 2));
		return stringQuartet;
	}
	
	public static List<Instrument> getWindQuartet(){
		List<Instrument> instruments = new ArrayList<Instrument>();
		instruments.add(new KontaktLibFlute(3, 3));
		instruments.add(new KontaktLibOboe(2, 1));
		instruments.add(new KontaktLibClarinet(1, 2));
		instruments.add(new KontaktLibBassoon(0, 0));
		return instruments;
	}
	
	public static List<Instrument> getPianoAndViolin(){
		List<Instrument> voices = new ArrayList<Instrument>();
		voices.add(new KontaktLibViolin(4, 2));
		voices.add(new KontaktLibPiano(3, 1));
		voices.add(new KontaktLibPiano(2, 1));
		voices.add(new KontaktLibPiano(1, 1));
		voices.add(new KontaktLibPiano(0, 1));
		return voices;
	}
	
	public static List<Instrument> getChoir(){
		List<Instrument> voices = new ArrayList<Instrument>();
		voices.add(new KontaktLibSoprano(3, 0));
		voices.add(new KontaktLibAlto(2, 1));
		voices.add(new KontaktLibTenor(1, 2));
		voices.add(new KontaktLibBass(0, 3));
		return voices;
	}
	
	public static List<Instrument> getPiano(int totalVoices){
		List<Instrument> voices = new ArrayList<Instrument>();
		for (int i = 0; i < totalVoices; i++) {
			voices.add(new KontaktLibPiano(i, 0));
		}
		return voices;
	}
}
