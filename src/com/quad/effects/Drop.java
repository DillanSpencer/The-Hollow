package com.quad.effects;

import java.util.Random;

import com.quad.Tile.TileMap;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.ShadowType;

public class Drop {
	
	private float x;
	private float y;
	private float gravity;
	private int len;
	
	public int xMin, xMax;
	
	private TileMap tm;
	
	
	public Drop(TileMap tm){
		this.tm = tm;
		xMin = 0;
		xMax = tm.getWidth();
		x = (float) (tm.getx()+new Random().nextInt(xMax));
		y = (float) (tm.gety()+new Random().nextInt(100));
		if(x < xMin) x = xMin;
		y = -y;
		
		gravity = new Random().nextInt(6);
		len = new Random().nextInt(6);
	}
	
	public void fall(){
		if(gravity < 2) gravity = new Random().nextInt(6);
		y += gravity;
				
		gravity += new Random().nextDouble()/25;
		
		if(y > Settings.HEIGHT){
			y = new Random().nextInt(100);
			y = -y;
			gravity = new Random().nextInt(5);
		}
		
		
	}
	
	public void render(Renderer r){
		if(len < 2) len = new Random().nextInt(6);
		r.drawFillRect((int)x, (int)y, 1, len, 0x000056, ShadowType.FADE);
	}
	
	public void restart(){
		y = new Random().nextInt(200);
		
		if(y < 100){
			y = new Random().nextInt(200);
		}
		
		y = -y;
	
	}
	
	public void setBounds(int x, int xMax){
		this.xMin = x;
		this.xMax = xMax;
	}
	
	

}
