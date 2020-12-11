package com.harrydehix.logger.output.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;

import com.harrydehix.logger.Level;
import com.harrydehix.logger.LogEntry;
import com.harrydehix.logger.LoggerException;
import com.harrydehix.logger.output.Output;

/**
 * The logger's file output. Use {@link #print(LogEntry)} to write a log entry to the current log file.
 */
public class FileOutput extends Output{
	/**
	 * The maximum file size of a log file (in bytes).
	 */
	public volatile long maxFileSize = 2 * 1024 * 1024;
	
	/**
	 * The used file output mode.
	 * @see FileMode
	 */
	public volatile FileMode mode = FileMode.ONE_PER_THREAD;
	
	/**
	 * The path of the current log file
	 */
	private ThreadLocal<File> logFile = ThreadLocal.withInitial(()-> null);;
	
	/**
	 * Stores whether the file output got initialized
	 * @see #initialize()
	 */
	private volatile boolean initialized = false;
	
	/**
	 * Writes a log entry to the current log file.
	 * @param entry the entry to log
	 */
	public void handle(LogEntry entry) {
		if(!isInitialized()) initialize();
		
		updateLogFile();
		createLogFileIfNotExists();
		cutLogFileToMaximumSize();
		
		String output = entry.toString(isTaggingEnabled());
		if(entry.getException() != null) output += entry.getStackTrace();
		appendToLogFile(output);
	}

	private void updateLogFile() {
		if(getMode() == FileMode.ONE_PER_THREAD) getLogFile().set(new File("log/" + Thread.currentThread().getName() + ".log"));
		else getLogFile().set(new File("log/Logs.log"));
	}
	
	private void createLogFileIfNotExists() {
		if(!getLogFile().get().exists()) {
			try {
				getLogFile().get().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				throw new LoggerException("Failed to create file: " + getLogFile().get().getPath());
			}
		}
	}
	
	private void cutLogFileToMaximumSize() {
		if(getLogFile().get().length() > getMaxFileSize()) {
			deleteLinesFromLogFile(0, 10000);
		}
	}

	/**
	 * Initializes the file output. E.g. the log directory is cleared.
	 */
	private void initialize() {
		deleteLogDirectory();
		new File("log/").mkdirs();
		setInitialized(true);
	}
	
	/**
	 * Deletes a directory.
	 * @param directory the directory to delete
	 */
	private static void deleteLogDirectory(){
		if(new File("log/").exists()) {
			 try {
					Files.walk(Paths.get("log/"))
					  .sorted(Comparator.reverseOrder())
					  .map(Path::toFile)
					  .forEach(File::delete);
				} catch (IOException e) {
					e.printStackTrace();
					throw new LoggerException("Failed to delete log directory (log/)");
				}
		}
	}
	
	/**
	 * Append a string to a file.
	 * @param path the file's path
	 * @param txt the string to append
	 */
	private void appendToLogFile(String txt) {
		try {
			OutputStream o = Files.newOutputStream(getLogFile().get().toPath(), StandardOpenOption.APPEND);
			o.write(txt.getBytes());
			o.close();
		} catch (IOException e) {
			throw new LoggerException("Failed to append string to log file (" + getLogFile().get().getPath() + ")! Did you close the file?");
		}
	}

	/**
	 * Deletes a certain amount of lines from a file.
	 * @param path the file's path
	 * @param startline the line to start from
	 * @param numlines the amount of lines to delete
	 */
	private void deleteLinesFromLogFile(int startline, int numlines){
		try{
			BufferedReader br = new BufferedReader(new FileReader(getLogFile().get()));
 
			StringBuffer sb=new StringBuffer("");

			int linenumber = 0;
			String line;
 
			while((line=br.readLine())!=null){
				if(linenumber<startline||linenumber>startline+numlines)
					sb.append(line+"\n");
				linenumber++;
			}
			br.close();
 
			FileWriter fw = new FileWriter(getLogFile().get());
			
			fw.write(sb.toString());
			fw.close();
		}
		catch (Exception e){
			throw new LoggerException("Failed to delete lines from log file: " + getLogFile().get().getPath());
		}
	}

	public final long getMaxFileSize() {
		return maxFileSize;
	}

	public final void setMaxFileSize(long maxFileSize) {
		if(maxFileSize <= 9) throw new LoggerException("The max log file size has to be at least 10 bytes!");
		this.maxFileSize = maxFileSize;
	}

	public final FileMode getMode() {
		return mode;
	}

	public final void setMode(FileMode mode) {
		if(mode == null) throw new LoggerException("The mode of the file out cannot be null!");
		this.mode = mode;
	}

	public final ThreadLocal<File> getLogFile() {
		return logFile;
	}

	public final void setLogFile(ThreadLocal<File> logFile) {
		if(logFile == null) throw new LoggerException("The used log file cannot be null!");
		this.logFile = logFile;
	}

	public final boolean isInitialized() {
		return initialized;
	}

	public final void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public void set(boolean enabled, Level logLevel, boolean taggingEnabled, long maxFileSize, FileMode mode) {
		setMaxFileSize(maxFileSize);
		setMode(mode);
		super.set(enabled, logLevel, taggingEnabled);
	}
}
