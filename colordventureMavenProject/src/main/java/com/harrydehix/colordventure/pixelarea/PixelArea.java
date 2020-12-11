package com.harrydehix.colordventure.pixelarea;

import com.harrydehix.colordventure.pixelarea.animation.PixelAnimation;
import com.harrydehix.logger.L;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * Stellt eine Rechteck dar, bei dem die Farbe jedes einzelnen Pixels einstellbar ist.
 */
public class PixelArea {
	/**
	 * 2 dimensionaler Array, in dem alle {@link Pixel} gespeichert sind.
	 */
	private Pixel[][] pixels;
	
	private volatile IntegerProperty widthProperty;
	private volatile IntegerProperty heightProperty;
	private volatile IntegerProperty rubberRadiusProperty;
	
	private PixelAnimation animation;
	
	public PixelAnimation getAnimation() {
		return animation;
	}
	
	public void setAnimation(PixelAnimation animation, Canvas output) {
		if(animation == null) throw new NullPointerException("The pixel area's animation is not allowed to be null!");
		this.animation = animation;
		getAnimation().initialize(output, this);
	}

	/**
	 * Instanziiert eine {@link PixelArea}.
	 * @param width Breite der PixelArea
	 * @param height Höhe der PixelArea
	 */
	public PixelArea(int width, int height, int rubberRadius, PixelAnimation animation, Canvas output) {
		this.widthProperty = new SimpleIntegerProperty(width);
		this.heightProperty = new SimpleIntegerProperty(height);
		this.rubberRadiusProperty = new SimpleIntegerProperty(rubberRadius);
		setAnimation(animation, output);
		widthProperty().addListener((ob, oldVal, newVal) -> {
			if(oldVal != newVal) {
				reset();
				if(!getAnimation().isAnimationRunning()) getAnimation().draw();
			}
		});
		heightProperty().addListener((ob, oldVal, newVal) -> {
			if(oldVal != newVal) {
				reset();
				if(!getAnimation().isAnimationRunning()) getAnimation().draw();
			}
		});
		reset();
	}
	
	public PixelArea(PixelAnimation animation, Canvas output) {
		this(100, 100, 10, animation, output);
	}

	public IntegerProperty widthProperty() {
		return widthProperty;
	}
	public void setWidth(int width) {
		widthProperty().set(width);
	}
	public int getWidth() {
		return getPixels().length;
	}
	
	public IntegerProperty heightProperty() {
		return heightProperty;
	}
	public void setHeight(int height) {
		heightProperty().set(height);
	}
	public int getHeight() {
		return getPixels()[0].length;
	}
	
	
	public IntegerProperty rubberRadiusProperty() {
		return rubberRadiusProperty;
	}
	public int getRubberRadius() {
		return rubberRadiusProperty().intValue();
	}
	public void setRubberWidth(int rubberRadius) {
		rubberRadiusProperty().set(rubberRadius);
	}
	
	public void reset() {
		boolean running = getAnimation().isAnimationRunning();
		synchronized(getAnimation().getLockReset()) {
			if(running) {
				getAnimation().lock();
				try {
					getAnimation().getLockReset().wait();
				} catch (InterruptedException e) {
					L.f(getClass(), "Animation calculation thread interrupted!", e);
				}
			}
			setPixels(new Pixel[widthProperty().intValue()][heightProperty().intValue()]);
			
			for(int x = 0; x < getWidth(); x++) {
				for(int y = 0; y < getHeight(); y++) {
					initializePixel(x, y, new Color(1, 1, 1, 1));
				}
			}
			
			getRandomPixel().setColor(new Color(Math.random(), Math.random(), Math.random(), 1));
		}
		if(running) getAnimation().unlock();
	}
	
	private Pixel getRandomPixel() {
		return getPixel(Random.randomInt(getWidth()), Random.randomInt(getHeight()));
	}

	private Pixel[][] getPixels() {
		return pixels;
	}

	private void setPixels(Pixel[][] pixels) {
		this.pixels = pixels;
	}
	
	/**
	 * Gibt Informationen über den {@link Pixel} an der gesuchten Stelle zurück.
	 * @param x x-Koordinate des Pixels
	 * @param y y-Koordinate des Pixels
	 * @return Informationen über den Pixel in Form des Pixel-Objekts
	 */
	public Pixel getPixel(int x, int y) {
		return getPixels()[x][y];
	}
	
	/**
	 * Setzt die Farbe des Pixels an der gewünschten Stelle.
	 * @param x x-Koordinate des Pixels
	 * @param y y-Koordinate des Pixels
	 * @param c Farbe des Pixels
	 */
	private void initializePixel(int x, int y, Color c) {
		getPixels()[x][y] = new Pixel(x, y, c);
	}
	
	/**
	 * Setzt die Farbe des mittleren Pixels.
	 * @param c Farbe des mittleren Pixels
	 */
	public void setCenterPixel(Color c) {
		getPixels()[getWidth() / 2][getHeight() / 2].setColor(c);
	}
	
	/**
	 * Färbt die Pixel, die innerhalb des Kreises liegen, weiß.
	 * @param xC x-Koordinate des Zentrums des Kreises
	 * @param yC y-Koordinate des Zentrums des Kreises
	 * @param radius Kreisradius
	 */
	public void rubber(int xC, int yC) {
		if(xC >= getWidth() || yC >= getHeight() || xC < 0 || yC < 0) throw new IllegalArgumentException("x or y are out of bounds!");
		if(getRubberRadius() < 1) throw new IllegalArgumentException("Invalid radius (min. Radius is 1) !");
		
		int maxX = xC+getRubberRadius();
		int minX = xC-getRubberRadius();
		int maxY = yC+getRubberRadius();
		int minY = yC-getRubberRadius();
		
		if(minX < 0) minX = 0;
		if(minY < 0) minY = 0;
		
		
		for(int x = minX; x <= maxX && x < getWidth(); x++) {
			for(int y = minY; y <= maxY && y < getHeight(); y++) {
				if(getPixel(x, y).distance(xC, yC) <= getRubberRadius()) {
					getPixel(x, y).setColor(Color.WHITE);
				}
			}
		}
	}
	
	/**
	 * Zeichnet die PixelArea auf ein {@link Canvas}.
	 */
	public void draw(Canvas output) {
		for(int x=0; x<getWidth(); x++) {
	    	for(int y=0; y<getHeight(); y++) {
	        	output.getGraphicsContext2D().getPixelWriter().setColor(x, y, getPixel(x, y).getColor());
	        }
	    }
	}
}
