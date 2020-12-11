package com.harrydehix.colordventure;

import java.awt.Desktop;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.harrydehix.colordventure.pixelarea.PixelArea;
import com.harrydehix.colordventure.pixelarea.animation.defaultanimation.DefaultAnimation;
import com.harrydehix.colordventure.utils.SliderSpinnerUtils;
import com.harrydehix.logger.L;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class Controller {
	private PixelArea pixelArea;
	
	@FXML private Canvas canvas;
	@FXML private Pane center;
	@FXML private TabPane tabPane;
	@FXML private MenuBar menu;
	@FXML private HBox animationControls;
	@FXML private Label zoomLabel;
	@FXML private Button animationControlButton;
	@FXML private Button resetButton;
	@FXML private Button nextFrameButton;
	
	@FXML private Spinner widthSpinner;
	
	@FXML private Spinner heightSpinner;
	
	@FXML private Slider speedSlider;
	@FXML private Spinner speedSpinner;
	
	@FXML private Slider randomnessSlider;
	@FXML private Spinner randomnessSpinner;
	
	@FXML private Slider hueVSlider;
	@FXML private Spinner hueVSpinner;
	
	@FXML private Slider hueASlider;
	@FXML private Spinner hueASpinner;
	
	@FXML private Slider brightVSlider;
	@FXML private Spinner brightVSpinner;
	
	@FXML private Slider brightASlider;
	@FXML private Spinner brightASpinner;
	
	@FXML private Slider satVSlider;
	@FXML private Spinner satVSpinner;
	
	@FXML private Slider satASlider;
	@FXML private Spinner satASpinner;
	
	@FXML private Slider smoothSlider;
	@FXML private Spinner smoothSpinner;
	
	private File configurationFile = null;
	
	@FXML
	private void save() {
		if(configurationFile != null) pixelArea.getAnimation().saveToFile(configurationFile);
		else saveAs();
	}
	
	@FXML
	private void saveAs() {
		FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("Coloradventure Config File (*.ccf)", "*.ccf");
        fileChooser.getExtensionFilters().add(extFilter);
      
        File f = fileChooser.showSaveDialog(App.getStage());
        if(f != null) {
        	configurationFile = f;
        	pixelArea.getAnimation().saveToFile(configurationFile);
        }
	}
	
	@FXML
	private void help() {
		try {
		   Desktop.getDesktop().browse(new URL("https://github.com/harrydehix/Colordventure").toURI());
		} catch (IOException | URISyntaxException e) {
		    L.e(getClass(), "Failed to open about website!", e);
		}
	}
	
	@FXML
	private void open() {
		FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("Coloradventure Config File (*.ccf)", "*.ccf");
        fileChooser.getExtensionFilters().add(extFilter);
      
        File f = fileChooser.showOpenDialog(App.getStage());
        if(f != null) {
        	configurationFile = f;
        	pixelArea.getAnimation().loadFromFile(configurationFile);
        }
	}
	
	@FXML
	private void export() {
		pixelArea.getAnimation().lock();
		FileChooser fileChooser = new FileChooser();
        
        FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
      
        File file = fileChooser.showSaveDialog(App.getStage());
		double sX = canvas.getScaleX(), sY = canvas.getScaleY();
		canvas.setScaleX(1);
		canvas.setScaleY(1);
        if(file != null){
        	WritableImage writableImage = new WritableImage((int)canvas.getWidth(),(int) canvas.getHeight());
            canvas.snapshot(null, writableImage);
            canvas.setScaleX(sX);
            canvas.setScaleY(sY);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            try {
				ImageIO.write(renderedImage, "png", file);
			} catch (IOException e) {
				L.e(getClass(), "Failed to export image as PNG!", e);
			}
        }
        pixelArea.getAnimation().unlock();
	}
	
	private void setSliderAndSpinners() {
		// WIDTH
		SliderSpinnerUtils.setupSpinnerInteger(widthSpinner, 10, 4000, 100, 1, pixelArea.widthProperty());
		canvas.widthProperty().bind(pixelArea.widthProperty());
		canvas.widthProperty().addListener((obs, oldValue, newValue) ->{
			centerCanvas();
		});
		// HEIGHT
		SliderSpinnerUtils.setupSpinnerInteger(heightSpinner, 10, 4000, 100, 1, pixelArea.heightProperty());
		canvas.heightProperty().bind(pixelArea.heightProperty());
		canvas.heightProperty().addListener((obs, oldValue, newValue) ->{
			centerCanvas();
		});
		// SPEED
		SliderSpinnerUtils.setupSliderSpinnerInteger(speedSlider, speedSpinner, 1, 100, 1, 1, pixelArea.getAnimation().speedProperty());

		// RANDOMNESS
		SliderSpinnerUtils.setupSliderSpinnerDouble(randomnessSlider, randomnessSpinner, 0, 1, 0.03, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).randomnessProperty());
		
		// HUE V
		SliderSpinnerUtils.setupSliderSpinnerDouble(hueVSlider, hueVSpinner, 0, 1, 0.5, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).hueVarProperty());
		
		// HUE A
		SliderSpinnerUtils.setupSliderSpinnerDouble(hueASlider, hueASpinner, -20, 20, 0.5, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).hueAddProperty());
		
		// BRIGHTNESS V
		SliderSpinnerUtils.setupSliderSpinnerDouble(brightVSlider, brightVSpinner, 0, 1, 0.3, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).brightnessVarProperty());
		
		// BRIGHTNESS A
		SliderSpinnerUtils.setupSliderSpinnerDouble(brightASlider, brightASpinner, -1, 1, 0.0, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).brightnessAddProperty());
		
		// SATURATION V
		SliderSpinnerUtils.setupSliderSpinnerDouble(satVSlider, satVSpinner, 0, 1, 0.5, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).saturationVarProperty());
		
		// SATURATION A
		SliderSpinnerUtils.setupSliderSpinnerDouble(satASlider, satASpinner, -1, 1, 0.0, 0.01, ((DefaultAnimation) pixelArea.getAnimation()).saturationAddProperty());
		
		// SMOOTHNESS
		SliderSpinnerUtils.setupSliderSpinnerInteger(smoothSlider, smoothSpinner, 1, 10, 1, 1, ((DefaultAnimation) pixelArea.getAnimation()).smoothnessProperty());
	}
	
	@FXML
	private void toggleAnimation() {
		if(pixelArea.getAnimation().isAnimationRunning()) {
			stopAnimation();
		}else {
			startAnimation();
		}
	}
	
	private void stopAnimation() {
		animationControlButton.setText("Play");
		((ImageView) animationControlButton.getGraphic()).setImage(new Image(getClass().getResource("/icons/play72.png").toExternalForm()));
		nextFrameButton.setDisable(false);
		pixelArea.getAnimation().pause();
	}
	
	private void startAnimation() {
		animationControlButton.setText("Pause");
		((ImageView) animationControlButton.getGraphic()).setImage(new Image(getClass().getResource("/icons/pause72.png").toExternalForm()));
		nextFrameButton.setDisable(true);
		pixelArea.getAnimation().play();
	}
	
	
	@FXML
	private void nextFrame() {
		pixelArea.getAnimation().next();
		pixelArea.getAnimation().draw();
	}
	
	@FXML
	private void resetAnimation() {
		pixelArea.reset();
		pixelArea.draw(canvas);
	}
	
	@FXML
	private void close() {
		pixelArea.getAnimation().destroy();
		App.getStage().close();
	}
	
	@FXML
	private void toggleSettings() {
		if(!tabPane.isVisible()) {
			tabPane.setManaged(true);
			tabPane.setVisible(true);
		}else {
			tabPane.setManaged(false);
			tabPane.setVisible(false);
		}
		centerCanvas();
	}
	
	private void centerCanvas() {
		if(!tabPane.isVisible()) {
			canvas.setLayoutX(App.getStage().getScene().getWidth()/2 - 0.5*canvas.getWidth());
			canvas.setLayoutY((App.getStage().getScene().getHeight()-animationControls.getHeight())/2 - 0.5*canvas.getHeight() - 0.5*menu.getHeight());
		}else {
			canvas.setLayoutX((App.getStage().getScene().getWidth()-tabPane.getWidth())/2 - 0.5*canvas.getWidth());
			canvas.setLayoutY((App.getStage().getScene().getHeight()-animationControls.getHeight())/2 - 0.5*canvas.getHeight() - 0.5*menu.getHeight());
		}
	}
	
	@FXML
	private void resetZoom() {
		canvas.setScaleX(1);
		canvas.setScaleY(1);
		zoomLabel.setText("100%");
		centerCanvas();
	}
	
	private void adaptTabPane(double sceneHeight) {
		if(sceneHeight <= 350) {return;}
	    double tabPaneHeight = sceneHeight - menu.getHeight() - animationControls.getHeight();
	    if(tabPaneHeight > 0) {
	    	tabPane.setMaxHeight(tabPaneHeight);
	    	tabPane.setMinHeight(tabPaneHeight);
	    	tabPane.setPrefHeight(tabPaneHeight);
	    }
	}
	
	private void setTabPaneXPosition() {
		tabPane.setLayoutX(App.getStage().getScene().getWidth()-tabPane.getWidth());
	}

	@FXML
	private void initialize() {
		pixelArea = new PixelArea(new DefaultAnimation(), canvas);
		setSliderAndSpinners();
		
		App.getStage().setOnCloseRequest((event) -> {
			pixelArea.getAnimation().destroy();
		});
		
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				App.getStage().getScene().heightProperty().addListener((obs, oldVal, newVal) -> {
					adaptTabPane(newVal.doubleValue());
					centerCanvas();
				});
				App.getStage().getScene().widthProperty().addListener((obs, oldVal, newVal) -> {
					centerCanvas();
					setTabPaneXPosition();
				});

				setTabPaneXPosition();
				adaptTabPane(App.getStage().getScene().getHeight());
				centerCanvas();
			}
			
		});
		
		canvas.getGraphicsContext2D().setFill(Color.WHITE);
		canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		center.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				if(event.getX() > center.getWidth()-tabPane.getWidth()) return;
				
				double deltaY = event.getDeltaY()/40;
				
				double newScaleX = canvas.getScaleX() + deltaY*0.06;
				double newScaleY = canvas.getScaleX() + deltaY*0.06;
				
				
				if(newScaleX>0 && newScaleY >0) {
					canvas.setScaleX(newScaleX);
					canvas.setScaleY(newScaleY);
					zoomLabel.setText((int)(newScaleX*100) + "%");
				}
			}
		});
		
		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					if(event.getX() >= 0 && 
							event.getX() < canvas.getWidth() && 
							event.getY() >=0 &&
							event.getY() < canvas.getHeight())
					pixelArea.rubber((int)event.getX(), (int) event.getY());
				}
			}
		});
		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.PRIMARY) {
					if(event.getX() >= 0 && 
							event.getX() < canvas.getWidth() && 
							event.getY() >=0 &&
							event.getY() < canvas.getHeight())
					pixelArea.rubber((int)event.getX(), (int) event.getY());
				}
			}
		});
		
		center.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if(event.getButton() == MouseButton.MIDDLE) {
					if(dragStarting) {
						dragStarting = false;
						xPositionBefore = event.getX();
						yPositionBefore = event.getY();
						return;
					}
					App.getStage().getScene().setCursor(Cursor.MOVE);
					double dtX = event.getX()-xPositionBefore;
					double dtY = event.getY()-yPositionBefore;
					
					double newX = canvas.getLayoutX() + dtX;
					double newY = canvas.getLayoutY() + dtY;
					
					canvas.setLayoutX(newX);
					
					canvas.setLayoutY(newY);
					
					xPositionBefore = event.getX();
					yPositionBefore = event.getY();
				}
			}
		});
		
		center.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				dragStarting = true;
				App.getStage().getScene().setCursor(Cursor.DEFAULT);
			}
		});
	}

	private static double xPositionBefore, yPositionBefore;
	private static boolean dragStarting = true;
}
