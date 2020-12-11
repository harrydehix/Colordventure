package com.harrydehix.logger;

import com.harrydehix.logger.output.alert.FXOutput;
import com.harrydehix.logger.output.console.ConsoleOutput;
import com.harrydehix.logger.output.file.FileOutput;

/**
 * <p>
 * Interface to log messages and errors in an ordered way. Dependent on the {@link L.Config}
 * these messages are printed to the console and/or stored in one or multiple log files. Every log entry can
 * contain following types of (meta) information:
 * </p>
 * 
 * <ol>
 * 	<li><b>Tag</b>: The tag of a log entry is a string that assigns the log entry to a more
 * 		abstract context (e.g. "Encryption").</li>
 * 	<li><b>Class of origin</b>: The class of origin is the class where the log entry gets created.</li>
 * 	<li><b>Message</b>: The log entry's message as string.</li>
 * 	<li><b>{@link Level}</b>: The log level describes the importance of a log and how it affects the program execution. Every log level has its own function.</li>
 * </ol>
 *
 * <p>
 * Furthermore (fatal) errors can contain the {@link Exception} that caused
 * the unwanted program state or is important in that context. By that the exception's stack trace gets logged too.
 * </p>
 * 
 * @see L.Config
 */
public class L{
	public static final FileOutput FILE_OUTPUT = new FileOutput();
	public static final ConsoleOutput CONSOLE_OUTPUT = new ConsoleOutput();
	public static final FXOutput ALERT_OUTPUT = new FXOutput();
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, m, e));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, m, e));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, m, null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, m, null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, null, c, e.getMessage(), null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs a fatal error forcing a complete shutdown.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void f(String t, Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null");
		
		log(new LogEntry(System.currentTimeMillis(), Level.FATAL, t, c, e.getMessage(), null));
		
		System.exit(-1);
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, m, e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, m, null));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, null, c, e.getMessage(), e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, String m, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, m, e));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, m, null));
	}
	
	/**
	 * Logs an error that is fatal to the related operation but not to the whole application. The program continues to run. 
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param e The exception that is related to the error
	 * 
	 * @see L
	 */
	public static void e(String t, Class<?> c, Exception e) {
		if(e == null) throw new LoggerException("Exception e cannot be null"); 
		
		log(new LogEntry(System.currentTimeMillis(), Level.ERROR, t, c, e.getMessage(), e));
	}
	
	/**
	 * Logs a warning. Is used when something happened that can potentially cause application oddities but is not an error.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void w(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.WARNING, null, c, m, null));
	}
	
	/**
	 * Logs a warning. Is used when something happened that can potentially cause application oddities but is not an error.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void w(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.WARNING, t, c, m, null));
	}
	
	/**
	 * Logs generally useful information.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void i(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.INFO, null, c, m, null));
	}
	
	/**
	 * Logs generally useful information.
	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 * 
	 * @see L
	 */
	public static void i(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.INFO, t, c, m, null));
	}
	
	/**
	 * Logs information that is diagnostically helpful.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void d(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.DEBUG, null, c, m, null));
	}
	
	/**
	 * Logs information that is diagnostically helpful.
 	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void d(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.DEBUG, t, c, m, null));
	}
	
	/**
	 * Logs information that is important for tracing the program's execution in detail.
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void t(Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.TRACE, null, c, m, null));
	}
	
	/**
	 * Logs information that is important for tracing the program's execution in detail.
 	 * @param t The log entry's tag
	 * @param c The class where the log entry gets created
	 * @param m The log entry's message
	 */
	public static void t(String t, Class<?> c, String m) {
		log(new LogEntry(System.currentTimeMillis(), Level.TRACE, t, c, m, null));
	}
	
	/**
	 * Logs a log entry using the log entry object.
	 * @param entry the log entry as object
	 * @see LogEntry
	 */
	private static void log(LogEntry entry) {
		if(entry.getTime() < 0) throw new LoggerException("Invalid log time");
		if(entry.getLevel() == null) throw new LoggerException("Log level cannot be null"); 
		if(entry.getClassOfOrigin() == null) throw new LoggerException("Class of origin cannot be null"); 
		if(entry.getMessage() == null) throw new LoggerException("Log message cannot be null");
		
		CONSOLE_OUTPUT.out(entry);
		FILE_OUTPUT.out(entry);
		ALERT_OUTPUT.out(entry);
	}
	
	public static void main(String[] args) {
		L.d("Tag", L.class, "Hallooo");
		L.t(Level.class, "Hallooo");
	}
}
