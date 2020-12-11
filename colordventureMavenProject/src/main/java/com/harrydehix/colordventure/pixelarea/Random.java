package com.harrydehix.colordventure.pixelarea;

import java.security.SecureRandom;

public class Random {
	private static SecureRandom randomNumbersGenerator = new SecureRandom();
	
	/**
	 * Generiert eine Zufallszahl zwischen 0 und (bound-1).
	 * @param bound 
	 * @return eine Zufallszahl zwischen 0 und (bound-1)
	 */
	public static int randomInt(int bound) {
		return randomNumbersGenerator.nextInt(bound);
	}
}
