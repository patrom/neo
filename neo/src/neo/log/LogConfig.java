package neo.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogConfig {

	public static void configureLogger(Level level){
		Logger topLogger = Logger.getLogger("");
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(level);
		topLogger.addHandler(ch);
		topLogger.setLevel(level);
//		FileHandler fileTxt = new FileHandler("Logging.txt");
//		SimpleFormatter formatterTxt = new SimpleFormatter();
//		fileTxt.setFormatter(formatterTxt);
//		topLogger.addHandler(fileTxt);
	}
}
