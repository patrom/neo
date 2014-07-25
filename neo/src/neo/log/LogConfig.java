package neo.log;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogConfig {
	
	public static void configureLogger(Level level){
		Logger topLogger = Logger.getLogger("");
		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(level);
		Formatter formatter = new Formatter() {
		      public String format(LogRecord record) {
		        return record.getLevel() + "  :  "
//		        	+ record.getSourceMethodName() + "  :  "
		            + record.getMessage() + "\n";
		      }
		    };
		ch.setFormatter(formatter);
		topLogger.addHandler(ch);
		topLogger.setLevel(level);
		
		try {
			FileHandler fileTxt = new FileHandler("Logging.txt");
			fileTxt.setFormatter(formatter);
			fileTxt.setLevel(level);
			topLogger.addHandler(fileTxt);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
			
		// suppress the logging output to the console
	    Handler[] handlers = topLogger.getHandlers();
	    if (handlers[0] instanceof ConsoleHandler) {
	    	topLogger.removeHandler(handlers[0]);
	    }

	}
}
