package com.harrydehix.colordventure.pixelarea;

import com.harrydehix.logger.L;

import javafx.scene.paint.Color;

/**
 * Repräsentiert einen Pixel der {@link PixelArea}. Hat die Koordinaten {@link #x} und {@link #y} und eine Farbe {@link #c}.
 */
public class Pixel {
	/**
	 * Die x-Koordinate eines Pixels
	 */
	private final int x;
	/**
	 * Die y-Koordinate eines Pixels
	 */
	private final int y;
	
	/**
	 * Die Farbe eines Pixels
	 */
	private Color c;
	
	public Pixel(int x, int y, Color c) {
		this.x = x;
		this.y = y;
		setColor(c);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		if(c == null) L.f(this.getClass(), new NullPointerException("A pixel's color cannot be null!"));
		this.c = c;
	}
	
	public double distance(Pixel p) {
		return distance(p.getX(), p.getY());
	}
	
	public double distance(int x, int y) {
		double d = Math.sqrt(Math.pow(x-getX(), 2) + Math.pow(y-getY(), 2));
		//System.out.printf("(%d, %d)-(%d, %d) = %.2f\n", getX(), getY(), x, y, d);
		return d;
	}
	
}
