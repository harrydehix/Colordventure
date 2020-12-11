package com.harrydehix.colordventure.pixelarea.animation.defaultanimation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.harrydehix.colordventure.pixelarea.Pixel;
import com.harrydehix.colordventure.pixelarea.Random;
import com.harrydehix.colordventure.pixelarea.animation.PixelAnimation;
import com.harrydehix.logger.L;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public class DefaultAnimation extends PixelAnimation{
	private volatile DoubleProperty fillProportionProperty;
	private volatile DoubleProperty randomnessProperty;
	private volatile DoubleProperty brightnessVarProperty;
	private volatile DoubleProperty saturationVarProperty;
	private volatile DoubleProperty hueVarProperty;
	private volatile DoubleProperty hueAddProperty;
	private volatile DoubleProperty saturationAddProperty;
	private volatile DoubleProperty brightnessAddProperty;
	private volatile IntegerProperty smoothnessProperty;
	
	public DefaultAnimation(double fillProportion, double randomness, double brightnessVar,
			double saturationVar, double hueVar, double hueAdd, double saturationAdd,
			double brightnessAdd, int smoothness, int speed) {
		super(speed);
		this.fillProportionProperty = new SimpleDoubleProperty(this, "fillProportion", fillProportion);
		this.randomnessProperty = new SimpleDoubleProperty(this, "randomness", randomness);
		this.brightnessVarProperty = new SimpleDoubleProperty(this, "brightnessVar", brightnessVar);
		this.saturationVarProperty = new SimpleDoubleProperty(this, "saturationVar", saturationVar);
		this.hueVarProperty = new SimpleDoubleProperty(this, "hueVar", hueVar);
		this.brightnessAddProperty = new SimpleDoubleProperty(this, "brightnessAdd", brightnessAdd);
		this.saturationAddProperty = new SimpleDoubleProperty(this, "saturationAdd", saturationAdd);
		this.hueAddProperty = new SimpleDoubleProperty(this, "hueAdd", hueAdd);
		this.smoothnessProperty = new SimpleIntegerProperty(this, "smoothness", smoothness);
	}
	
   public DefaultAnimation() {
		this(0.4, 0.03, 0.5, 0.4, 0.5, 0, 0, 0, 1, 50);
	}
	
	@Override
	public void next() {
		ArrayList<WhitePixelContainer> whitePixels = new ArrayList<WhitePixelContainer>();
		
		for(int x = 0; x < getParent().getWidth(); x++) {
			for(int y = 0; y < getParent().getHeight(); y++) {
				if(getParent().getPixel(x, y).getColor().equals(Color.WHITE)) {
					WhitePixelContainer p = new WhitePixelContainer(getParent().getPixel(x, y), new ArrayList<Pixel>());
					
					int maxRight = x + getSmoothness();
					int maxLeft = x - getSmoothness();
					int maxTop = y + getSmoothness();
					int maxBottom = y - getSmoothness();
					
					if(maxRight >= getParent().getWidth()) maxRight = getParent().getWidth()-1;
					if(maxLeft < 0) maxLeft = 0;
					if(maxTop >= getParent().getHeight()) maxTop = getParent().getHeight()-1;
					if(maxBottom < 0) maxBottom = 0;
					
					for(int xI = maxLeft; xI<=maxRight; xI++) {
						for(int yI = maxBottom; yI<=maxTop; yI++) {
							if(xI == x && yI == y) continue;
							if(!getParent().getPixel(xI, yI).getColor().equals(Color.WHITE)) p.getNeighbours().add(getParent().getPixel(xI, yI));
						}
					}
					if(p.getNeighbours().size() != 0) whitePixels.add(p);
				}
			}
		}
		
		if(whitePixels.size()!=0) {
			int amount = (int) (getFillProportion()*whitePixels.size());
			if(amount <= 0) amount = 1;
			if(amount > whitePixels.size()) amount = whitePixels.size();
			
			for(int i=0; i<amount; i++) {
				int randomIndex = Random.randomInt(whitePixels.size());
				whitePixels.get(randomIndex).fillUsingNeighboursColor(this);
				whitePixels.remove(randomIndex);
			}
		}
	}

	@Override
	public void draw() {
		getParent().draw(getOutput());
	}
	
	public IntegerProperty smoothnessProperty() {
		return smoothnessProperty;
	}
	public int getSmoothness() {
		return smoothnessProperty().intValue();
	}
	public void setSmoothness(int smoothness) {
		smoothnessProperty().set(smoothness);
	}
	
	public DoubleProperty brightnessAddProperty() {
		return brightnessAddProperty;
	}
	public double getBrightnessAdd() {
		return brightnessAddProperty().doubleValue();
	}
	public void setBrightnessAdd(double brightnessAdd) {
		brightnessAddProperty().set(brightnessAdd);
	}
	
	public DoubleProperty hueAddProperty() {
		return hueAddProperty;
	}
	public double getHueAdd() {
		return hueAddProperty().doubleValue();
	}
	public void setHueAdd(double hueAdd) {
		hueAddProperty().set(hueAdd);
	}
	
	public DoubleProperty saturationAddProperty() {
		return saturationAddProperty;
	}
	public double getSaturationAdd() {
		return saturationAddProperty().doubleValue();
	}
	public void setSaturationAdd(double saturationAdd) {
		saturationAddProperty().set(saturationAdd);
	}
	
	public DoubleProperty brightnessVarProperty() {
		return brightnessVarProperty;
	}
	public double getBrightnessVar() {
		return brightnessVarProperty().doubleValue();
	}
	public void setBrightnessVar(double brightnessVar) {
		brightnessVarProperty().set(brightnessVar);
	}
	
	public DoubleProperty saturationVarProperty() {
		return saturationVarProperty;
	}
	public double getSaturationVar() {
		return saturationVarProperty().doubleValue();
	}
	public void setSaturationVar(double saturationVar) {
		saturationVarProperty().set(saturationVar);
	}
	
	public DoubleProperty hueVarProperty() {
		return hueVarProperty;
	}
	public double getHueVar() {
		return hueVarProperty().doubleValue();
	}
	public void setHueVar(double hueVar) {
		hueVarProperty().set(hueVar);
	}
	
	public DoubleProperty randomnessProperty() {
		return randomnessProperty;
	}
	public double getRandomness() {
		return randomnessProperty().doubleValue();
	}
	public void setRandomness(double randomness) {
		randomnessProperty().set(randomness);
	}
	
	public DoubleProperty fillProportionProperty() {
		return fillProportionProperty;
	}
	public double getFillProportion() {
		return fillProportionProperty.doubleValue();
	}
	public void setFillProportion(double fillProportion) {
		fillProportionProperty().set(fillProportion);
	}

	@Override
	public void saveToFile(File f) {
		try {
			f.createNewFile();
			
			Properties props = new Properties();
		    props.setProperty(brightnessAddProperty().getName(), String.valueOf(getBrightnessAdd()));
		    props.setProperty(brightnessVarProperty().getName(), String.valueOf(getBrightnessVar()));
		    props.setProperty(smoothnessProperty().getName(), String.valueOf(getSmoothness()));
		    props.setProperty(saturationAddProperty().getName(), String.valueOf(getSaturationAdd()));
		    props.setProperty(saturationVarProperty().getName(), String.valueOf(getSaturationVar()));
		    props.setProperty(hueAddProperty().getName(), String.valueOf(getHueAdd()));
		    props.setProperty(hueVarProperty().getName(), String.valueOf(getHueVar()));
		    props.setProperty(fillProportionProperty().getName(), String.valueOf(getFillProportion()));
		    props.setProperty(randomnessProperty().getName(), String.valueOf(getRandomness()));
		    props.setProperty(speedProperty().getName(), String.valueOf(getSpeed()));
		    
		    FileWriter writer;
			writer = new FileWriter(f);
			
			props.store(writer, "pixel animation settings");
		    writer.close(); 
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to store pixel animation's settings!", e);
		}
	}

	@Override
	public void loadFromFile(File f) {
		 
		try {
		    FileReader reader = new FileReader(f);
		    Properties props = new Properties();
		    props.load(reader);
		    
		    setBrightnessAdd(Double.valueOf(props.getProperty("brightnessAdd")));
		    setBrightnessVar(Double.valueOf(props.getProperty("brightnessVar")));
		    setSmoothness(Integer.valueOf(props.getProperty("smoothness")));
		    setSaturationAdd(Double.valueOf(props.getProperty("saturationAdd")));
		    setSaturationVar(Double.valueOf(props.getProperty("saturationVar")));
		    setHueAdd(Double.valueOf(props.getProperty("hueAdd")));
		    setHueVar(Double.valueOf(props.getProperty("hueVar")));
		    setFillProportion(Double.valueOf(props.getProperty("fillProportion")));
		    setRandomness(Double.valueOf(props.getProperty("randomness")));
		    setSpeed(Integer.valueOf(props.getProperty("speed")));
		    
		    reader.close();
		} catch (IOException e) {
			L.e(this.getClass(), "Failed to load pixel animation's settings from selected file!", e);
		}
	}
}
