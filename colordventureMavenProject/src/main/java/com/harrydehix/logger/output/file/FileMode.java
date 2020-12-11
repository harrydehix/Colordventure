package com.harrydehix.logger.output.file;

/**
 * Enumeration holding all different file output modes.
 *
 */
public enum FileMode {
	/**
	 * The logger will create one log file for all logs.
	 */
	SINGLE, 
	/**
	 * The logger will create one log file per thread.
	 */
	ONE_PER_THREAD
}
