package com.harrydehix.colordventure.utils;

import javafx.beans.property.Property;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

public class SliderSpinnerUtils {
	public static void setupSliderSpinnerDouble(Slider slider, Spinner<Double> spinner, double min, double max, double initial, double step, Property<Number> bindProperty) {
		SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, step);
		spinner.setValueFactory(factory);
		
		slider.setMin(min);
		slider.setMax(max);
		slider.setValue(initial);
		slider.setBlockIncrement(step);
		
		slider.valueProperty().addListener((obs, oldValue, newValue) ->{
			spinner.getValueFactory().setValue(newValue.doubleValue());
		});
		spinner.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) ->{
			slider.setValue(newValue);
		});
		slider.valueProperty().bindBidirectional(bindProperty);
	}
	
	public static void setupSliderSpinnerInteger(Slider slider, Spinner<Integer> spinner, int min, int max, int initial, int step, Property<Number> bindProperty) {
		SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step);
		spinner.setValueFactory(factory);
		
		slider.setMin(min);
		slider.setMax(max);
		slider.setValue(initial);
		slider.setBlockIncrement(step);
		
		slider.valueProperty().addListener((obs, oldValue, newValue) ->{
			slider.setValue(newValue.intValue());
			spinner.getValueFactory().setValue(newValue.intValue());
		});
		spinner.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) ->{
			slider.setValue(newValue);
		});
		
		slider.valueProperty().bindBidirectional(bindProperty);
	}
	
	public static void setupSpinnerInteger(Spinner<Integer> spinner, int min, int max, int initial, int step, Property<Number> bindProperty) {
		SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step);
		
		spinner.setValueFactory(factory);
		
		bindProperty.addListener((obs, oldValue, newValue) ->{
			spinner.getValueFactory().setValue(newValue.intValue());
		});
		spinner.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) ->{
			bindProperty.setValue(newValue);
		});
	}
	
	public static void setupSpinnerDouble(Spinner<Double> spinner, double min, double max, double initial, double step, Property<Number> bindProperty) {
		SpinnerValueFactory<Double> factory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, step);
		
		spinner.setValueFactory(factory);
		
		bindProperty.addListener((obs, oldValue, newValue) ->{
			spinner.getValueFactory().setValue(newValue.doubleValue());
		});
		spinner.getValueFactory().valueProperty().addListener((obs, oldValue, newValue) ->{
			bindProperty.setValue(newValue);
		});
	}
}
