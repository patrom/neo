package neo.out.print;

import static neo.model.note.NoteBuilder.note;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import neo.generator.MusicProperties;
import neo.midi.MelodyInstrument;
import neo.model.melody.Melody;
import neo.model.note.Note;
import neo.model.note.NoteBuilder;
import neo.out.instrument.Instrument;
import neo.out.instrument.KontaktLibPiano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MusicXMLWriter {

	private static final int DIVISIONS = 256;
	
	@Autowired
	private MusicProperties musicProperties;
	
	public void generateMusicXML(List<MelodyInstrument> melodies, String id) throws Exception {
		Map<Instrument, List<List<Note>>> melodiesForInstrument = new HashMap<Instrument, List<List<Note>>>();
		for (MelodyInstrument melody : melodies) {
			Instrument instrument = getInstrumentForVoice(melody.getVoice());
			melodiesForInstrument.compute(instrument, (k, v) -> {
					if (v == null) {
						List<List<Note>> list = new ArrayList<>();
						list.add(melody.getNotes());
						return list;
					}else {
						v.add(melody.getNotes());
						return v;
					}
				}
			);
		}
		createXML(id, melodiesForInstrument);
	}

	public void generateMusicXMLForMelodies(List<Melody> melodies, String id) throws Exception {
		Map<Instrument, List<List<Note>>> melodiesForInstrument = getMelodyNotesForInstrument(melodies);
		createXML(id, melodiesForInstrument);
	}
	
	public void generateMusicXMLForNotes(List<Note> notes, Instrument instrument, String id) throws Exception {
		List<Instrument> instruments = new ArrayList<Instrument>();
		instruments.add(instrument);
		musicProperties.setInstruments(instruments);
		MelodyInstrument melodyInstrument = new MelodyInstrument(notes, 0);
		generateMusicXML(Collections.singletonList(melodyInstrument), id);
	}

	private void createXML(String id, Map<Instrument, List<List<Note>>> melodiesForInstrument) throws FactoryConfigurationError, XMLStreamException, FileNotFoundException {
		XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream("resources/xml/" + id + ".xml"), "UTF-8");

		// Generate the XML
		// Write the default XML declaration
		xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
		xmlStreamWriter.writeCharacters("\n");
		xmlStreamWriter.writeDTD("<!DOCTYPE score-partwise PUBLIC '-//Recordare//DTD MusicXML 3.0 Partwise//EN' 'http://www.musicxml.org/dtds/partwise.dtd'>");
		xmlStreamWriter.writeCharacters("\n");

		// Write the root element
		xmlStreamWriter.writeStartElement("score-partwise");
		xmlStreamWriter.writeAttribute("version", "3.0");
		xmlStreamWriter.writeCharacters("\n");

		createElement(xmlStreamWriter, "work");
		createElement(xmlStreamWriter, "identification");
		createElement(xmlStreamWriter, "defaults");

		// part-list element
		xmlStreamWriter.writeStartElement("part-list");
		xmlStreamWriter.writeCharacters("\n");
		
		Set<Instrument> distinctInstruments = new HashSet<>(musicProperties.getInstruments());
		for (Instrument instrument : distinctInstruments) {
			createScorePartElement(xmlStreamWriter, instrument);
		}
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
		//part element
		for (Entry<Instrument, List<List<Note>>> entries : melodiesForInstrument.entrySet()) {
			createPartElement(xmlStreamWriter, entries.getValue(), entries.getKey());
		}
		
		// End the root element
		xmlStreamWriter.writeEndElement();

		// End the XML document
		xmlStreamWriter.writeEndDocument();

		// Close the XMLStreamWriter to free up resources
		xmlStreamWriter.close();
	}
	
	private List<Note> addRests(List<Note> notes){
		List<Note> notesRestsIncluded = new ArrayList<Note>();
		int size = notes.size() - 1;
		for (int i = 0; i < size; i++) {
			Note note = notes.get(i);
			notesRestsIncluded.add(note);
			Note nextNote = notes.get(i + 1);
			int endNote = note.getPosition() + note.getLength();
			if (endNote < nextNote.getPosition()) {
				int length = nextNote.getPosition() - endNote;
				Note rest = note().pitch(Integer.MIN_VALUE).pos(endNote).len(length).voice(note.getVoice()).build();
				notesRestsIncluded.add(rest);
			}
		}
		return notesRestsIncluded;
	}

	private Map<Instrument, List<List<Note>>> getMelodyNotesForInstrument(
			List<Melody> melodies) {
		Map<Instrument, List<List<Note>>> melodiesForInstrument = new HashMap<Instrument, List<List<Note>>>();
		for (Melody melody : melodies) {
			Instrument instrument = getInstrumentForVoice(melody.getVoice());
			melodiesForInstrument.compute(instrument, (k, v) -> {
					if (v == null) {
						List<List<Note>> list = new ArrayList<>();
						list.add(melody.getMelodieNotes());
						return list;
					}else {
						v.add(melody.getMelodieNotes());
						return v;
					}
				}
			);
		}
		return melodiesForInstrument;
	}
	
	private void createBackupElement(XMLStreamWriter xmlStreamWriter,int duration) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("backup");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "duration", String.valueOf(duration));
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private Instrument getInstrumentForVoice(int voice){
		List<Instrument> instruments = musicProperties.getInstruments();
		Optional<Instrument> instrument = instruments.stream().filter(instr -> (instr.getVoice()) == voice).findFirst();
		if (instrument.isPresent()) {
			return instrument.get();
		}else{
			throw new IllegalArgumentException("Instrument for voice " + voice + " is missing!");
		}
	}

	private void createPartElement(XMLStreamWriter xmlStreamWriter, List<List<Note>> list, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("part");
		xmlStreamWriter.writeAttribute("id", "P" + instrument.getVoice());
		xmlStreamWriter.writeCharacters("\n");
		
		createMeasureElements(xmlStreamWriter, list, instrument);
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createMeasureElements(XMLStreamWriter xmlStreamWriter, List<List<Note>> notesLists, Instrument instrument) throws XMLStreamException {
		// only 1 melody
		if (notesLists.size() == 1) {
			List<Measure> measures = getMeasures(notesLists.get(0));
			for (int i = 0; i < measures.size(); i++) {
				Measure measure = measures.get(i);
				xmlStreamWriter.writeComment("====== Part: " + instrument.getVoice() + " , Measure: " + i + " =======");
				xmlStreamWriter.writeCharacters("\n");
				xmlStreamWriter.writeStartElement("measure");
				xmlStreamWriter.writeAttribute("number", String.valueOf(i));
				xmlStreamWriter.writeCharacters("\n");
				if (i == 0) {
					createAttributes(xmlStreamWriter, instrument);
					createDirectionElement(xmlStreamWriter, instrument);
				}
				List<Note> notes = measure.getNotes();
				int position = -1;
				for (Note note : notes) {
//					if (note.hasDynamic()) {
//						<direction>
//						<direction-type>
//							<dynamics default-x="9" default-y="-118" color="#000000" font-family="Opus Text Std" font-style="normal" font-size="11.9365" font-weight="normal">
//								<p />
//							</dynamics>
//						</direction-type>
//						<voice>1</voice>
//						<staff>1</staff>
//					</direction>
//					}
					int staff = getStaff(instrument, note);
					if (position == note.getPosition()) {
						createNoteElement(xmlStreamWriter, note, instrument, true, -1 , staff);
					} else {
						createNoteElement(xmlStreamWriter, note, instrument, false, -1, staff);
					}
					position = note.getPosition();
				}
				xmlStreamWriter.writeEndElement();
				xmlStreamWriter.writeCharacters("\n");
			}
		} else {
			//multi voice
			List<Measure> multiVoiceMeasures = new ArrayList<>();
			for (List<Note> melodyNotes : notesLists) {
				List<Measure> measures = getMeasures(melodyNotes);
				multiVoiceMeasures.addAll(measures);
			}
			Map<Integer, List<Measure>> multiVoiceMeasureStartPositions = multiVoiceMeasures.stream()
					.collect(Collectors.groupingBy(Measure::getStart, TreeMap::new, Collectors.toList()));
			int start = 0;
			for (Entry<Integer, List<Measure>> startPositions : multiVoiceMeasureStartPositions.entrySet()) {
				xmlStreamWriter.writeComment("====== Part: " + instrument.getVoice() + " , Measure: " + start + " =======");
				xmlStreamWriter.writeCharacters("\n");
				xmlStreamWriter.writeStartElement("measure");
				xmlStreamWriter.writeAttribute("number", String.valueOf(start));
				xmlStreamWriter.writeCharacters("\n");
				if (start==0) {
					createAttributes(xmlStreamWriter, instrument);
					createDirectionElement(xmlStreamWriter, null);
				}
				List<Measure> measures = startPositions.getValue();
				int m = 1;
				int split = notesLists.size()/2;
				int lastMeasure = measures.size();
				boolean isChordNote = false;
				for (Measure measure : measures) {
					List<Note> notes = measure.getNotes();
					int position = -1;
					for (Note note : notes) {
						if (position == note.getPosition()) {
							isChordNote = true;
						} else {
							isChordNote = false;
						}
						position = note.getPosition();
						if (m <= split) {
							createNoteElement(xmlStreamWriter, note, instrument, isChordNote, m, 1);
						}else{
							createNoteElement(xmlStreamWriter, note, instrument, isChordNote, m, 2);
						}
					}
					if (m != lastMeasure) {
						int duration = getDuration(notes);
						createBackupElement(xmlStreamWriter, duration);
					}
					m++;
				}
				xmlStreamWriter.writeEndElement();
				xmlStreamWriter.writeCharacters("\n");
				start++;
			}
		}
	}

	private int getDuration(List<Note> notes) {
		int duration = notes.stream().mapToInt(note -> note.getLength()).sum();
		return (duration * DIVISIONS) / 12;
	}

	private int getStaff(Instrument instrument, Note note) {
		if (instrument instanceof KontaktLibPiano) {
			if (note.getPitch() < 60) {
				return 2;
			}
		}
		return 1;
	}

	private void createDirectionElement(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("direction");
		xmlStreamWriter.writeCharacters("\n");
		
		createDirectionTypeElement(xmlStreamWriter);
//		createElementWithValue(xmlStreamWriter, "voice", String.valueOf(instrument.getVoice()));
//		createElementWithValue(xmlStreamWriter, "staff", String.valueOf(instrument.getVoice());
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createDirectionTypeElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("direction-type");
		xmlStreamWriter.writeCharacters("\n");
		
		createMetronomeElement(xmlStreamWriter);

		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createMetronomeElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("metronome");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "beat-unit", "quarter");
		createElementWithValue(xmlStreamWriter, "per-minute", String.valueOf(musicProperties.getTempo()));
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private List<Measure> getMeasures(List<Note> notes) {
		Note lastNote = notes.get(notes.size() - 1);
		int totalLength = lastNote.getPosition() + lastNote.getLength();
		int measureSize = musicProperties.getNumerator() * 12;
		int totalMeasures = (totalLength + measureSize - 1) / measureSize;
		List<Measure> measures = new ArrayList<>();
		for (int i = 0; i < totalMeasures; i++) {
			Measure measure = new Measure();
			measure.setStart(i * measureSize);
			measure.setEnd((i + 1) * measureSize);
			measures.add(measure);
		}
		int measureIndex = 0;
		Measure measure = measures.get(measureIndex);
		for (Note note : notes) {
			if (note.getPosition() >= measure.getEnd()) {
				measure = measures.get(++measureIndex);
			}
			int length = note.getPosition() + note.getLength();
			if (length > measure.getEnd()) {
				//split
				Note firstNote = note.copy();
				firstNote.setLength(measure.getEnd() - note.getPosition());
				firstNote.setTieStart(true);
				measure.addNote(firstNote);
				Note secondNote = note.copy();
				secondNote.setPosition(measure.getEnd());
				secondNote.setLength(length - measure.getEnd());
				secondNote.setTieEnd(true);
				//add split note to next measure
				measure = measures.get(++measureIndex);
				measure.addNote(secondNote);
			} else {
				measure.addNote(note);
			}
		}
		return measures;
	}

	private void createNoteElement(XMLStreamWriter xmlStreamWriter, Note note, Instrument instrument, boolean isChordNote, int voice, int staff) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("note");
		xmlStreamWriter.writeCharacters("\n");
		if (isChordNote) {
			xmlStreamWriter.writeEmptyElement("chord");
			xmlStreamWriter.writeCharacters("\n");
		}
		if (note.isRest()) {
			xmlStreamWriter.writeEmptyElement("rest");
		} else {
			createPitchElement(xmlStreamWriter, note);
		}
		int length =  note.getLength() * DIVISIONS / 12;
		createElementWithValue(xmlStreamWriter, "duration", String.valueOf(length));
		createElementWithAttributeValue(xmlStreamWriter, "instrument", "id", "P" + instrument.getVoice() + "-I" + instrument.getVoice());
		if (voice > 0) {
			createElementWithValue(xmlStreamWriter, "voice", String.valueOf(voice));
		}
		NoteType noteType = NoteType.getNoteType(length);
		createElementWithValue(xmlStreamWriter, "type", noteType.getName());
		if (noteType.isDot()) {
			xmlStreamWriter.writeEmptyElement("dot");
		}
		if (noteType.isTriplet()) {
			createTimeModification(xmlStreamWriter, noteType);
		}
		createElementWithValue(xmlStreamWriter, "staff", String.valueOf(staff));
		if (note.hasArticulation()|| note.isTieStart() || note.isTieEnd()) {
			createNotationsElement(xmlStreamWriter, note);
		}
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}
	
	private void createNotationsElement(XMLStreamWriter xmlStreamWriter, Note note) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("notations");
		xmlStreamWriter.writeCharacters("\n");
		if (note.isTieStart()) {
			createElementWithAttributeValue(xmlStreamWriter, "tied", "type", "start");
		}else if (note.isTieEnd()) {
			createStopElement(xmlStreamWriter);
		}
		if (note.hasArticulation()) {
			createArticulationElement(xmlStreamWriter, note);
		}
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createArticulationElement(XMLStreamWriter xmlStreamWriter, Note note) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("Articulation");
		xmlStreamWriter.writeCharacters("\n");
		
		xmlStreamWriter.writeEmptyElement(note.getArticulation().getMusicXmlLabel());
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
	}

	private void createTimeModification(XMLStreamWriter xmlStreamWriter,NoteType noteType) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("time-modification");
		xmlStreamWriter.writeCharacters("\n");
		
		if (noteType.equals(NoteType.sixteenthTriplet)) {
			createElementWithValue(xmlStreamWriter, "actual-notes", "6");
			createElementWithValue(xmlStreamWriter, "normal-notes", "4");
		} else {
			createElementWithValue(xmlStreamWriter, "actual-notes", "3");
			createElementWithValue(xmlStreamWriter, "normal-notes", "2");
		}
		createElementWithValue(xmlStreamWriter, "normal-type", noteType.getName());
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createStopElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("slur");
		xmlStreamWriter.writeAttribute("color", "#000000");
		xmlStreamWriter.writeAttribute("type", "stop");
		xmlStreamWriter.writeAttribute("orientation", "over");
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithAttributeValue(xmlStreamWriter, "tied", "type", "stop");
	}

	private void createNotationsStartElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("notations");
		xmlStreamWriter.writeCharacters("\n");
		createElementWithAttributeValue(xmlStreamWriter, "tied", "type", "start");
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createPitchElement(XMLStreamWriter xmlStreamWriter, Note note)
			throws XMLStreamException {
		xmlStreamWriter.writeStartElement("pitch");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "step", getNoteName(note.getPitchClass()));
		if (note.getPitchClass() == 1 || note.getPitchClass() == 3 || note.getPitchClass() == 6 || note.getPitchClass() == 8){
			createElementWithValue(xmlStreamWriter, "alter", String.valueOf(1));
		} else if (note.getPitchClass() == 10 ){
			createElementWithValue(xmlStreamWriter, "alter", String.valueOf(-1));
		}
		createElementWithValue(xmlStreamWriter, "octave", String.valueOf(note.getOctave() - 1));
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private String getNoteName(int pitchClass) {
		switch (pitchClass) {
			case 0:
			case 1:	
				return "C";
			case 2:
			case 3:
				return "D";
			case 4:
				return "E";
			case 5:
			case 6:
				return "F";
			case 7:
			case 8:
				return "G";
			case 9:
				return "A";
			case 10:
			case 11:
				return "B";
			default:
				break;
		}
		return null;
	}

	private void createAttributes(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("attributes");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "divisions", String.valueOf(DIVISIONS));
		createKeyElement(xmlStreamWriter);
		createTimeElement(xmlStreamWriter);
		if (instrument instanceof KontaktLibPiano) {
			createElementWithValue(xmlStreamWriter, "staves", String.valueOf(2));
			createPianoClefElement(xmlStreamWriter);
		} else{
			createElementWithValue(xmlStreamWriter, "staves", String.valueOf(1));
			createClefElement(xmlStreamWriter, instrument);
		}
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}
	
	private void createPianoClefElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("clef");
		xmlStreamWriter.writeAttribute("number", "1");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "sign", "G");
		createElementWithValue(xmlStreamWriter, "line", "2");

		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
		xmlStreamWriter.writeStartElement("clef");
		xmlStreamWriter.writeAttribute("number", "2");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "sign", "F");
		createElementWithValue(xmlStreamWriter, "line", "4");

		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createClefElement(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("clef");
		
		if (instrument instanceof KontaktLibPiano) {
			xmlStreamWriter.writeAttribute("number", "1");
			xmlStreamWriter.writeCharacters("\n");
			
			createElementWithValue(xmlStreamWriter, "sign", "G");
			createElementWithValue(xmlStreamWriter, "line", "2");

			xmlStreamWriter.writeEndElement();
			xmlStreamWriter.writeCharacters("\n");
			
			xmlStreamWriter.writeStartElement("clef");
			xmlStreamWriter.writeAttribute("number", "2");
			xmlStreamWriter.writeCharacters("\n");
			
			createElementWithValue(xmlStreamWriter, "sign", "F");
			createElementWithValue(xmlStreamWriter, "line", "4");
		}else{
			xmlStreamWriter.writeCharacters("\n");
			if (instrument.getClef().equals("F")) {
				createElementWithValue(xmlStreamWriter, "sign", "F");
				createElementWithValue(xmlStreamWriter, "line", "4");
			} else {
				createElementWithValue(xmlStreamWriter, "sign", "G");
				createElementWithValue(xmlStreamWriter, "line", "2");
			}
		}
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createTimeElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("time");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "beats", String.valueOf(musicProperties.getNumerator()));
		createElementWithValue(xmlStreamWriter, "beat-type", String.valueOf(musicProperties.getDenominator()));
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createKeyElement(XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("key");
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "fifths", String.valueOf(musicProperties.getKeySignature()));
		createElementWithValue(xmlStreamWriter, "mode", "major");
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createScorePartElement(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("score-part");
		xmlStreamWriter.writeAttribute("id", "P" + instrument.getVoice());
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "part-name", instrument.getInstrumentName());
		createInstrumentScoreElement(xmlStreamWriter, instrument);
		
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}

	private void createInstrumentScoreElement(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("score-instrument");
		xmlStreamWriter.writeAttribute("id", "P" + instrument.getVoice() + "-I" + instrument.getVoice());
		xmlStreamWriter.writeCharacters("\n");
		
		createElementWithValue(xmlStreamWriter, "instrument-name", instrument.getInstrumentName());
		createElementWithValue(xmlStreamWriter, "instrument-sound", instrument.getInstrumentSound());
		
		xmlStreamWriter.writeEmptyElement("solo");
		createVirtualInstrumentElement(xmlStreamWriter, instrument);
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
	}

	private void createVirtualInstrumentElement(XMLStreamWriter xmlStreamWriter, Instrument instrument) throws XMLStreamException {
		xmlStreamWriter.writeStartElement("virtual-instrument");
		xmlStreamWriter.writeCharacters("\n");
		createElementWithValue(xmlStreamWriter, "virtual-library", instrument.getVirtualLibrary());
		createElementWithValue(xmlStreamWriter, "virtual-name", instrument.getVirtualName());
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
		
	}

	private void createElement(XMLStreamWriter xmlStreamWriter, String name) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(name);
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}
	
	private void createElementWithValue(XMLStreamWriter xmlStreamWriter, String name, String value) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(name);
		xmlStreamWriter.writeCharacters(value);
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}
	
	private void createElementWithAttributeValue(XMLStreamWriter xmlStreamWriter, String elementName, String attributeName, String attributeValue) throws XMLStreamException {
		xmlStreamWriter.writeStartElement(elementName);
		xmlStreamWriter.writeAttribute(attributeName, attributeValue);
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeCharacters("\n");
	}
}
