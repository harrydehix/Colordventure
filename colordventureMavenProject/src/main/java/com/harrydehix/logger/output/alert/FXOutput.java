package com.harrydehix.logger.output.alert;

import com.harrydehix.logger.Level;
import com.harrydehix.logger.LogEntry;
import com.harrydehix.logger.output.Output;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FXOutput extends Output{
	public FXOutput() {
		setLogLevel(Level.ERROR);
	}
	/**
	 * Shows a log message to the user using JavaFX.
	 * @param entry the entry to print
	 */
	@Override
	protected void handle(LogEntry e) {
		if(e.getLevel() == Level.FATAL) return;
		
		Alert alert = null;
		
		switch(e.getLevel()) {
		case INFO:
		case TRACE:
		case DEBUG:
			alert = new Alert(AlertType.INFORMATION);
			break;
		case ERROR:
		case FATAL:
			alert = new Alert(AlertType.ERROR);
			break;
		case WARNING:
			alert = new Alert(AlertType.WARNING);
			break;
		}
		alert.setTitle(e.getLevel().toString());
        alert.setHeaderText(e.getLevel().toString());
        alert.setContentText(e.getMessage());
 
        alert.showAndWait();
	}
}
