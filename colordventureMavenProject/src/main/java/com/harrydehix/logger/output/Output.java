package com.harrydehix.logger.output;

import com.harrydehix.logger.Level;
import com.harrydehix.logger.LogEntry;
import com.harrydehix.logger.LoggerException;

public abstract class Output {
	private Level logLevel = Level.DEBUG;
	private boolean enabled = true;
	private boolean taggingEnabled = false;
	
	protected abstract void handle(LogEntry e);
	
	public final void out(LogEntry e) {
		if(isEnabled() && Level.asInt(e.getLevel()) >= Level.asInt(getLogLevel())) handle(e);
	}

	public final Level getLogLevel() {
		return logLevel;
	}

	public final void setLogLevel(Level logLevel) {
		if(logLevel == null) throw new LoggerException("The log level is not allowed to be null!");
		this.logLevel = logLevel;
	}

	public final boolean isEnabled() {
		return enabled;
	}

	public final void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public final boolean isTaggingEnabled() {
		return taggingEnabled;
	}

	public final void setTaggingEnabled(boolean taggingEnabled) {
		this.taggingEnabled = taggingEnabled;
	}
	
	public final void set(boolean enabled, Level logLevel, boolean taggingEnabled) {
		setEnabled(taggingEnabled);
		setLogLevel(logLevel);
		setTaggingEnabled(taggingEnabled);
	}
	
	
}
