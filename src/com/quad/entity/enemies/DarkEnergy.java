package com.quad.entity.enemies;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.core.fx.SoundClip;
import com.quad.entity.Enemy;

public class DarkEnergy extends Enemy{
	

	private Image[] startSprites;
	private Image[] sprites;
	private Image[] robotSprites;
	
	private boolean start;
	private boolean permanent;
	
	private int type = 0;
	public static int VECTOR = 0;
	public static int GRAVITY = 1;
	public static int BOUNCE = 2;
	
	private int bounceCount = 0;
	
	public DarkEnergy(TileMap tm) {
		
		super(tm);
		
		health = maxHealth = 1;
		
		width = 20;
		height = 20;
		cwidth = 12;
		cheight = 12;
		
		damage = 1;
		moveSpeed = 5;
		
		startSprites = Content.DarkEnergy[0];
		sprites = Content.DarkEnergy[1];
		
		animation.setFrames(startSprites);
		animation.setDelay(2);
		
		start = true;
		flinching = true;
		permanent = false;
		
	}
	
	public DarkEnergy(TileMap tm, int size){
		super(tm);
		
		health = maxHealth = 1;
		
		width = 13;
		height = 13;
		cwidth = 12;
		cheight = 12;
		
		damage = 1;
		moveSpeed = 5;
		
		startSprites = Content.RobotShot[0];
		sprites = Content.RobotShot[1];
		
		animation.setFrames(startSprites);
		animation.setDelay(10);
		
		start = true;
		flinching = true;
		permanent = false;
	}
	
	public void setType(int i) { type = i; }
	public void setPermanent(boolean b) { permanent = b; }
	
	public void update(GameContainer gc, float dt) {
		
		if(start) {
			if(animation.hasPlayedOnce()) {
				animation.setFrames(sprites);
				animation.setNumFrames(3);
				animation.setDelay(2);
				start = false;
			}
		}
		
		if(type == VECTOR) {
			x += dx;
			y += dy;
		}
		else if(type == GRAVITY) {
			checkTileMapCollision();
			if(dy == 0) remove = true;
			dy += 0.2;
			x += dx;
			y += dy;
		}
		else if(type == BOUNCE) {
			double dx2 = dx;
			double dy2 = dy;
			checkTileMapCollision();
			if(dx == 0) {
				dx = -dx2;
				bounceCount++;
			}
			if(dy == 0) {
				dy = -dy2;
				bounceCount++;
			}
			x += dx;
			y += dy;
		}
		
		// update animation
		animation.update();
		
		if(!permanent) {
			if(x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
				remove = true;
			}
			if(bounceCount == 1) {
				remove = true;
			}
		}
				
		updateComponents(gc, dt);
		
	}
	
	public void draw(GameContainer gc, Renderer r) {
		super.renderComponents(gc, r);
	}

}
