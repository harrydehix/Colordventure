package com.harrydehix.colordventure.pixelarea.animation;

import java.io.File;

import com.harrydehix.colordventure.pixelarea.PixelArea;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.Canvas;

public abstract class PixelAnimation{
	private Thread calculationThread;
	private AnimationTimer drawAnimation;
	private volatile boolean locked = false;
	private volatile boolean running = false;
	private final Object animationLocker = new Object();
	private final Object lockReset = new Object();
	private Canvas output;
	private PixelArea parent;

	private volatile IntegerProperty speedProperty;
	
	public PixelAnimation(int speed) {
		this.speedProperty = new SimpleIntegerProperty(this, "speed", speed);
	}
	
	public PixelAnimation() {
		this(50);
	}
	
	public void initialize(Canvas output, PixelArea parent) {
		setOutput(output);
		setParent(parent);
		if(getCalculationThread() == null) {
			setCalculationThread(new Thread(() -> {
				while(true) {
					if(isAnimationLocked()) {
						synchronized(getAnimationLocker()) {
							synchronized (getLockReset()) {
								getLockReset().notifyAll();
							}
							try {
								getAnimationLocker().wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					if(isAnimationRunning()) {
						try {
							Thread.sleep((long) (0.001*(100-getSpeed())*1000));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						next();
					}
				}
			}));
		}
		setDrawAnimation(new AnimationTimer() {
			@Override
			public void handle(long now) {
				draw();
			}
		});
		getCalculationThread().start();
	}
		
	public void unlock() {
		setAnimationLocked(false);
		synchronized (getAnimationLocker()) {
			getAnimationLocker().notifyAll();
		}
		getDrawAnimation().start();
	}
	public void lock() {
		setAnimationLocked(true);
		getDrawAnimation().stop();
	}
	
	public void destroy() {
		getCalculationThread().stop();
		getDrawAnimation().stop();
	}
	
	public void play() {
		setAnimationRunning(true);
		getDrawAnimation().start();
	}
	public void pause() {
		setAnimationRunning(false);
		getDrawAnimation().stop();
	}
	
	public IntegerProperty speedProperty() {
		return speedProperty;
	}
	public int getSpeed() {
		return speedProperty().intValue();
	}
	public void setSpeed(int speed) {
		speedProperty().set(speed);
	}
	
	public Thread getCalculationThread() {
		return calculationThread;
	}

	public void setCalculationThread(Thread calculationThread) {
		this.calculationThread = calculationThread;
	}

	public AnimationTimer getDrawAnimation() {
		return drawAnimation;
	}

	public void setDrawAnimation(AnimationTimer drawAnimation) {
		this.drawAnimation = drawAnimation;
	}

	public Canvas getOutput() {
		return output;
	}

	public void setOutput(Canvas output) {
		this.output = output;
	}

	public PixelArea getParent() {
		return parent;
	}

	public void setParent(PixelArea parent) {
		this.parent = parent;
	}

	public boolean isAnimationLocked() {
		return locked;
	}

	public void setAnimationLocked(boolean animationLocked) {
		this.locked = animationLocked;
	}

	public boolean isAnimationRunning() {
		return running;
	}

	public void setAnimationRunning(boolean animationRunning) {
		this.running = animationRunning;
	}

	public Object getAnimationLocker() {
		return animationLocker;
	}

	public Object getLockReset() {
		return lockReset;
	}

	public abstract void next();
	public abstract void draw();
	public abstract void saveToFile(File f);
	public abstract void loadFromFile(File f);
}
