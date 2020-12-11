package com.harrydehix.colordventure;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
    	App.stage = stage;
    	
        stage.setScene(new Scene(loadFXML("main", new Controller()), 640, 480));
        stage.setTitle("Colordventure - v1.0.0");
        stage.show();
    }
    
    public static Stage getStage() {
    	return stage;
    }
    
    private static Parent loadFXML(String fxml, Controller c) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/layout/" + fxml + ".fxml"));
        fxmlLoader.setController(c);
        return fxmlLoader.load();
    }
    public static void main(String[] args) {
        launch();
    }

}