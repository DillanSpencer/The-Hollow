package com.quad.core;

public class Settings {
	
	public static final int WIDTH = 256;
	public static final int HEIGHT = 300;
	public static final int SCALE = 3;	
	
	public static final int FPS = 60;
	
	public static boolean LIGHT = false;
	
	public static final boolean RAINING = false;
	
	public static void setLight(GameContainer gc, boolean i){
		Settings.LIGHT = i;
		gc.setLightEnable(i);
		gc.setDynamicLights(i);
	}
	
	public static void changeFps(GameContainer gc, int i){
		gc.setFrameCap(i);
	}

}
