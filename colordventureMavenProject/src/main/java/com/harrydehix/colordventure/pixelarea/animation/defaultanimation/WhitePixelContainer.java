package com.harrydehix.colordventure.pixelarea.animation.defaultanimation;

import java.util.ArrayList;

import com.harrydehix.colordventure.pixelarea.Pixel;
import com.harrydehix.logger.L;

import javafx.scene.paint.Color;

/**
 * Speichert weißen Pixel mit der Farbe seines gefärbten Nachbarns.
 */
class WhitePixelContainer{
	/**
	 * Farbe des benachbarten Pixels.
	 */
	private final ArrayList<Pixel> neighbours;
	
	/**
	 * Der weiße Pixel.
	 */
	private final Pixel whitePixel;

	public WhitePixelContainer(Pixel whitePixel, ArrayList<Pixel> neighbours) {
		this.neighbours = neighbours;
		this.whitePixel = whitePixel;
	}

	public ArrayList<Pixel> getNeighbours() {
		return neighbours;
	}
	
	public Pixel getWhitePixel() {
		return whitePixel;
	}
	
	/**
	 * Färbt den weißen Pixel abhängig von der Farbe des Nachbarpixels.
	 * {@link C#RANDOMNESS} legt fest, wie sehr sich am Nachbarn orientiert wird (0 = Nachbarsfarbe; 1 = völlig zufällig; 0.5 = ausgeglichener Mix).
	 */
	public void fillUsingNeighboursColor(DefaultAnimation d) {
		double r = 0, g = 0, b = 0;
		
		double cumulatedWeight = 0;
		double[] rawWeights = new double[getNeighbours().size()];
		for(int i=0; i<getNeighbours().size(); i++) {
			// Gewicht eines um r entfernten Punkt =  1/r²
			rawWeights[i] = 1.0/Math.pow(getNeighbours().get(i).distance(getWhitePixel()), 4);
			cumulatedWeight += rawWeights[i];
		}
		
		if(getNeighbours().size() >= 2) {
			for(int i=0; i<getNeighbours().size(); i++) {
				// Angepasstes Gewicht
				double weight = rawWeights[i] / cumulatedWeight;
				r += getNeighbours().get(i).getColor().getRed() * weight;
				g += getNeighbours().get(i).getColor().getGreen() * weight;
				b += getNeighbours().get(i).getColor().getBlue() * weight;
			}
		}else if(getNeighbours().size() == 1){
			r = getNeighbours().get(0).getColor().getRed();
			g = getNeighbours().get(0).getColor().getGreen();
			b = getNeighbours().get(0).getColor().getBlue();
		}else {
			L.f(this.getClass(), new IllegalStateException("Cannot fill white pixel, that doesn't has any colored neighbours."));
		}
		
		double randomness = d.getRandomness();
		r = randomness*Math.random() + (1-randomness)*r;
		g = randomness*Math.random() + (1-randomness)*g;
		b = randomness*Math.random() + (1-randomness)*b;
		if(r > 1) r = 1;
		if(g > 1) g = 1;
		if(b > 1) b = 1;
		if(r < 0) r = 0;
		if(g < 0) g = 0;
		if(b < 0) b = 0;
		Color c = new Color(r, g, b, 1);
		getWhitePixel().setColor(c.deriveColor(d.getHueVar()*2*(Math.random()-0.5)+d.getHueAdd(), 1+d.getSaturationVar()*2*(Math.random()-0.5)+d.getSaturationAdd(), 1+d.getBrightnessVar()*2*(Math.random()-0.5)+d.getBrightnessAdd(), 1));
	}
}
