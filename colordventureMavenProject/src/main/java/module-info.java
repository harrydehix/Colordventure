module com.harrydehix.colordventure {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	requires javafx.swing;
	requires java.desktop;

    opens com.harrydehix.colordventure to javafx.fxml;
    exports com.harrydehix.colordventure;
}