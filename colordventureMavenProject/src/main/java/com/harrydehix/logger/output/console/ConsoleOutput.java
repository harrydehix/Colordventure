package com.harrydehix.logger.output.console;

import com.harrydehix.logger.LogEntry;
import com.harrydehix.logger.LoggerException;
import com.harrydehix.logger.output.Output;

/**
 * The logger's console output. Use {@link #print(LogEntry)} to print a log entry to the console.
 */
public class ConsoleOutput extends Output{

	@Override
	protected void handle(LogEntry e) {
		if(e == null) {
			throw new LoggerException("Cannot log a null entry.");
		}
		
		switch(e.getLevel()) {
		case TRACE:
		case DEBUG:
		case INFO:
			System.out.print(e.toString(isTaggingEnabled()));
			break;
		case ERROR:
		case FATAL:
		case WARNING:
			System.err.print(e.toString(isTaggingEnabled()));
			break;
		}
		
		if(e.getException() != null) {
			e.getException().printStackTrace();
		}
	}
}
