package neo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.swing.JFrame;

import jm.music.data.Score;
import jm.util.View;
import neo.generator.MelodyGenerator;
import neo.generator.MusicProperties;
import neo.midi.GeneralMidi;
import neo.midi.HarmonyPosition;
import neo.midi.MelodyInstrument;
import neo.midi.MidiDevicesUtil;
import neo.midi.MidiInfo;
import neo.midi.MidiParser;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.model.rhythm.Rhythm;
import neo.out.arrangement.Accompagnement;
import neo.out.arrangement.Arrangement;
import neo.out.arrangement.Pattern;
import neo.out.instrument.Ensemble;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibPiano;
import neo.out.instrument.KontaktLibViolin;
import neo.out.instrument.MidiDevice;
import neo.out.print.MusicXMLWriter;
import neo.out.print.ScoreUtilities;
import neo.variation.Embellisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(DefaultConfig.class)
public class PlayApplication extends JFrame implements CommandLineRunner{
	
	private static Logger LOGGER = Logger.getLogger(PlayApplication.class.getName());
	
	@Autowired
	private MidiParser midiParser;
	@Autowired
	private MidiDevicesUtil midiDevicesUtil;
	@Autowired
	private MusicProperties musicProperties;
	@Autowired
	private Arrangement arrangement;
	@Autowired
	private ScoreUtilities scoreUtilities;
	@Autowired
	private String midiFilesPath;
	@Autowired
	private Embellisher embellisher;
	@Autowired
	private Rhythm rhythm;
	@Autowired
	private MelodyGenerator melodyGenerator;
	@Autowired
	private MusicXMLWriter musicXMLWriter;
	
	public static void main(final String[] args) {
	 	SpringApplication app = new SpringApplication(PlayApplication.class);
	    app.setShowBanner(false);
	    app.run(args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		playMidiFilesOnKontaktFor();
	}
	
	public void playMidiFilesOnKontaktFor() throws Exception {
		List<File> midiFiles = Files.list(new File(midiFilesPath).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = midiParser.readMidi(midiFile);
			List<MelodyInstrument> parsedMelodies = midiInfo.getMelodies();
			musicProperties.setInstruments(Ensemble.getStringQuartet());
			mapInstruments(parsedMelodies, Ensemble.getStringQuartet());
			//split
			int size = parsedMelodies.size();
			List<MelodyInstrument> melodies = new ArrayList<>(parsedMelodies.subList(0, size/2));
			List<MelodyInstrument> harmonies = new ArrayList<>(parsedMelodies.subList(size/2, size));
			arrangement.transpose(melodies.get(3).getNotes(), 12);
//			arrangement.transpose(melodies.get(4).getNotes(), 12);
//			arrangement.transpose(melodies.get(5).getNotes(), 12);
//			List<Integer> voicesForAccomp = new ArrayList<>();
//			voicesForAccomp.add(1);
//			voicesForAccomp.add(2);
//			voicesForAccomp.add(3);
//			List<MelodyInstrument> accompMelodies = filterAccompagnementMelodies(voicesForAccomp, melodies);
//			createAccompagnement(accompMelodies, melodies, midiInfo.getHarmonyPositionsForVoice(0));
			
			int[] sounds = {0,12,24,36,48,60,72};
			Integer[] texture = {1,1,1,1, 1 ,1,1,1};
			Integer[] contour = {1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1,1,1,1, 1,  -1,-1,-1,-1};
			List<Note> accompNotes = rhythm.getRhythm(midiInfo.getHarmonyPositions(), contour, 1, 1, 6);
//			List<Note> accompNotes = rhythm.getRhythm(midiInfo.getHarmonyPositions(), sounds, texture, contour, 1);
//			melodies.get(1).setNotes(accompNotes);
//			arrangement.transpose(melodies.get(1).getNotes(), -12);
			
//			embellish(melodies);

			playOnKontakt(melodies, midiInfo.getTempo());
			Score score = scoreUtilities.createScoreFromMelodyInstrument(melodies, midiInfo.getTempo());
			score.setTitle(midiFile.getName());
			View.notate(score);
			write(melodies , "resources/transform/" + midiFile.getName(), midiInfo.getTempo());
			generateMusicXml(melodies, midiFile.getName());
			Thread.sleep(7000);
		}
	}

	private void embellish(List<MelodyInstrument> melodies) {
		for (int i = melodies.size() - 1; i >= 0; i--) {
			MelodyInstrument melodyInstrument = melodies.get(i);
			List<Note> embellishedNotes = embellisher.embellish(melodyInstrument.getNotes());
			melodyInstrument.setNotes(embellishedNotes);
		}
	}

	private void createAccompagnement(List<MelodyInstrument> accompMelodies, List<MelodyInstrument> melodies, List<HarmonyPosition> harmonyPositions) {
		for (MelodyInstrument melodyInstrument : accompMelodies) {
			melodies.remove(melodyInstrument);
			MelodyInstrument accomp = getAccomp(melodyInstrument.getNotes(), harmonyPositions, melodyInstrument.getVoice());
			accomp.setInstrument(melodyInstrument.getInstrument());
			melodies.add(accomp);
		}
		melodies.sort(Comparator.comparing(m -> m.getVoice()));
	}
	
	private List<MelodyInstrument> filterAccompagnementMelodies(List<Integer> voicesForAccomp,
			List<MelodyInstrument> melodies){
		return melodies.stream().filter(m -> voicesForAccomp.contains(m.getVoice())).collect(Collectors.toList());
	}

	public List<List<Note>> chordal(List<Note> harmonyNotes) {
		ArrayList<List<Note>> list = new ArrayList<List<Note>>();
		list.add(harmonyNotes);
		return list;
	}
	
	private MelodyInstrument getAccomp(List<Note> melodies, List<HarmonyPosition> harmonyPositions, int voice) {
		List<List<Note>> patterns = new ArrayList<>();
		harmonyPositions.forEach(h -> patterns.add(Pattern.repeat(12, 24)));
		List<Note> accompagnement = arrangement.getAccompagnement(melodies, harmonyPositions, patterns, 12);
		return new MelodyInstrument(accompagnement, voice);
	}

	private List<MelodyInstrument> playOnInstrumentsAccomp(MidiInfo midiInfo) {
		List<MelodyInstrument> playList = new ArrayList<>();
		List<MelodyInstrument> melodies = midiInfo.getMelodies();
		
		//accompagnement
		List<HarmonyPosition> harmonyPositions = midiInfo.getHarmonyPositions();
		Integer[] compPattern = {6,12,18,24};
		List<Integer[]> compPatterns = new ArrayList<Integer[]>();
		compPatterns.add(compPattern);
		Accompagnement[] compStrategy = {Accompagnement::arpeggio};
		List<Note> accompagnement = arrangement.accompagnement(harmonyPositions, compPatterns, compStrategy);
		MelodyInstrument accomp = new MelodyInstrument(accompagnement, melodies.size() + 1);
		accomp.setInstrument(new KontaktLibPiano(0, 0));
		arrangement.transpose(accomp.getNotes(), -12);
		accomp.addNotes(melodies.get(0).getNotes());//add bass notes
		playList.add(accomp);
//		arrangement.applyFixedPattern(melodies.get(0).getNotes(), 6);
		//harmony
		melodies.get(1).setInstrument(new KontaktLibPiano(0, 0));
		melodies.get(2).setInstrument(new KontaktLibPiano(0, 0));
		arrangement.transpose(melodies.get(3).getNotes(), -12);
		melodies.get(3).setInstrument(new KontaktLibPiano(0, 0));
		//melody
		arrangement.transpose(melodies.get(7).getNotes(), -12);
		melodies.get(7).setInstrument(new KontaktLibViolin(0, 1));
		playList.add(melodies.get(7));
		return playList;
	}
	
	private void mapInstruments(List<MelodyInstrument> melodies, List<Instrument> instruments) {
		int offset = melodies.size() / 2;
		for (int i = 0; i < instruments.size(); i++) {
			MelodyInstrument melodyInstrument = melodies.get(i);
			MelodyInstrument melodyInstrumentOffset = melodies.get(i + offset);
			Optional<Instrument> instrument = instruments.stream().filter(instr -> (instr.getVoice()) == melodyInstrument.getVoice()).findFirst();
			if (instrument.isPresent()) {
				melodyInstrument.setInstrument(instrument.get());
				melodyInstrumentOffset.setInstrument(instrument.get());
			}else{
				throw new IllegalArgumentException("Instrument for voice " + i + " is missing!");
			}
		}
	}
	
	private void playOnKontakt(List<MelodyInstrument> melodies,
			int tempo) throws InvalidMidiDataException {
		Sequence seq = midiDevicesUtil.createSequence(melodies);
		midiDevicesUtil.playOnDevice(seq, tempo, MidiDevice.KONTAKT);
	}

	public void playMidiFilesOnKontaktFor(Instrument instrument) throws IOException, InvalidMidiDataException, InterruptedException {
		List<File> midiFiles = Files.list(new File(midiFilesPath).toPath()).map(p -> p.toFile()).collect(Collectors.toList());
		for (File midiFile : midiFiles) {
			LOGGER.info(midiFile.getName());
			MidiInfo midiInfo = midiParser.readMidi(midiFile);
			List<MelodyInstrument> melodies = midiInfo.getMelodies();
			melodies.forEach(m -> m.setInstrument(instrument));
			playOnKontakt(melodies, midiInfo.getTempo());
			Thread.sleep(13000);
		}
	}
	
	public void testPlayOnKontakt() throws InvalidMidiDataException, IOException {
		MidiInfo midiInfo = midiParser.readMidi(PlayApplication.class.getResource("/melodies/Wagner-Tristan.mid").getPath());
		playOnKontakt(midiInfo.getMelodies(), midiInfo.getTempo());
	}
	
	private void write(List<MelodyInstrument> melodies, String outputPath, int tempo) throws InvalidMidiDataException, IOException{
		Sequence seq = null;
		if (containsInstrument(melodies, GeneralMidi.PIANO)) {
			MelodyInstrument piano = mergeMelodies(melodies, 2, new KontaktLibPiano(1, 2));
			List<MelodyInstrument> otherInstruments = melodies.stream()
				.filter(m -> !m.getInstrument().getGeneralMidi().equals(GeneralMidi.PIANO))
				.collect(Collectors.toList());
			List<MelodyInstrument> instruments = new ArrayList<>();
			instruments.addAll(otherInstruments);
			instruments.add(piano);
			seq = midiDevicesUtil.createSequenceGeneralMidi(instruments, tempo);
		}else{
			seq = midiDevicesUtil.createSequenceGeneralMidi(melodies, tempo);
		}
		midiDevicesUtil.write(seq, outputPath);
	}

	private MelodyInstrument mergeMelodies(List<MelodyInstrument> melodies, int generalMidi, Instrument instrument) {
		List<Note> notes = melodies.stream()
				.filter(m -> m.getInstrument().getGeneralMidi().equals(GeneralMidi.PIANO))
				.flatMap(m -> m.getNotes().stream())
				.sorted()
				.collect(Collectors.toList());
		MelodyInstrument melodyInstrument = new MelodyInstrument(notes, instrument.getVoice());
		melodyInstrument.setInstrument(instrument);
		return melodyInstrument;
	}

	private boolean containsInstrument(List<MelodyInstrument> melodies, GeneralMidi gm) {
		return melodies.stream().anyMatch(m -> m.getInstrument().getGeneralMidi().equals(gm));
	}
	
	private void generateMusicXml(List<MelodyInstrument> melodies, String id) throws Exception{
		musicXMLWriter.generateMusicXML(melodies, id);
	}
	
}
