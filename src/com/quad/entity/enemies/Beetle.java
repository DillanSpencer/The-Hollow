package com.quad.entity.enemies;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.entity.Player;

public class Beetle extends com.quad.entity.Enemy{
	
	private Image[] sprites;
	private Player player;
	private boolean active;
	
	public Beetle(TileMap tm, Player p) {
		
		super(tm);
		player = p;
		
		health = maxHealth = 3;
		
		width = 40;
		height = 19;
		cwidth = 20;
		cheight = 17;
		
		damage = 1;
		moveSpeed = 0.8;
		fallSpeed = 3;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		sprites = Content.Beetle[0];
		
		animation.setFrames(sprites);
		animation.setDelay(-1);
				
		left = true;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		if(knockback) {
			dy -= fallSpeed * 2;
			if(!falling) knockback = false;
			return;
		}
		
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) {
			dy = jumpStart;
		}
	}
	
	public void update(GameContainer gc, float dt) {
		
		super.updateComponents(gc, dt);
		
		if(!active) {
			if(Math.abs(player.getx() - x) < Settings.WIDTH) active = true;
			return;
		}
		
		
		
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount > 60) {
				flinching = false;
			}
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		if(topRight ) {
			left = true;
			right = facingRight = false;
		}
		if(topLeft) {
			left = false;
			right = facingRight = true;
		}
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update animation
		animation.update();
		
	}
	
	public void draw(GameContainer gc, Renderer r) {
		
		if(flinching && !knockback) {
			if(flinchCount % 10 < 5) return;
		}
		
		
		super.renderComponents(gc, r);
		
	}

}
