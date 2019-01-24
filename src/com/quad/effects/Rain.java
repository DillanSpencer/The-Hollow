package com.quad.effects;

import com.quad.Tile.TileMap;
import com.quad.core.Renderer;

public class Rain {
	
	public static final int LIGHT = 0;
	public static final int MEDIUM = 1;
	public static final int HEAVY = 2;
	
	private boolean shouldRain;
	
	private int NUM_AMOUNT;
	private Drop[] d;
	
	private TileMap tm;
	
	public Rain(int type,TileMap tm){
		this.tm = tm;
		if(type == LIGHT){
			NUM_AMOUNT = 10;
		}
		else if(type == MEDIUM){
			NUM_AMOUNT = 50;
		}
		else if(type == HEAVY){
			NUM_AMOUNT = 100;
		}
	}
	
	public void init(){
		d = new Drop[NUM_AMOUNT];
		setRain(true);
		for(int i = 0; i < d.length; i++){
			d[i] = new Drop(tm);
			
		}
	}
	
	public void updateRain(){
		
		for(int i = 0; i < d.length;i++){
			d[i].fall();
		}
	}
	
	
	
	public void renderRain(Renderer r){
		if(!shouldRain)return;
		for(int i = 0; i < d.length; i++){
			d[i].render(r);
		}
	}
	
	public void restart(){
		for(int i = 0; i < d.length; i++){
			d[i].restart();
		}
	}
	
	public void setBounds(int x, int xMax){
		for(int i = 0; i < d.length;i++){
			d[i].setBounds(x, xMax);
		}
	}
	
	public void setRain(boolean b){
		shouldRain = b;
	}
	
	public boolean isRaining(){
		return shouldRain;
	}

}
