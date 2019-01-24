package com.quad.entity.enemies;

import java.util.ArrayList;
import java.util.Random;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.core.fx.ShadowType;
import com.quad.entity.Enemy;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.items.Fire;
import com.quad.items.Heart;

public class Golem extends Enemy{

	private Image[] sprites;
	private Image[] attack;
	
	private Player player;
	private boolean active;
	private boolean stop;
	
	
	private static final int WALKING = 0;
	private static final int ATTACKING = 1;
	
	private int attackTick;
	private int attackDelay = 100;
	private int step;
	
	//items
	private int numItems;
	private int isDrop;
	private ArrayList<Item> items;
	
	public Golem(TileMap tm, Player p,ArrayList<Item> it) {
		
		super(tm);
		player = p;
		items = it;
		
		health = maxHealth = 6;
		
		width = 64;
		height = 69;
		cwidth = 60;
		cheight = 50;
		coffx = -15;
		coffy = 8;
		
		damage = 0;
		moveSpeed = 0.4;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		sprites = Content.Golem[0];
		attack = Content.Golem[1];

		
		//shadow
		for(int i = 0; i < sprites.length;i++){
			sprites[i].shadowType = ShadowType.FADE;
		}
		
		animation.setFrames(sprites);
		animation.setDelay(8);
		
		
	}
	
	private void getNextPosition() {
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
		
		if(!active) {
			if(Math.abs(player.getx() - x) < Settings.WIDTH) active = true;
			return;
		}
		
		//change direction facing
		if(dx > 0) facingRight = true;
		if(dx < 0) facingRight = false;
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount > 20) {
				flinching = false;
			}
		}
		if(!stop){
			getNextPosition();
		}
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
		setPosition(xtemp, ytemp);
		
		if(dx == 0 && currentAction != ATTACKING) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		// update animation
		animation.update();
		
		//is dead
		if(dead){
			Heart h = new Heart(tileMap, player);
			h.setPosition(x+5, y);
			h.setVector(2, -3);
			items.add(h);
			Fire t = new Fire(tileMap, player);
			t.setPosition(x-5, y + 10);
			t.setVector(2, -3);
			items.add(t);
		}
		
		// idle
				if(step == 0) {
					if(currentAction != WALKING) {
						currentAction = WALKING;
						animation.setFrames(sprites);
						animation.setDelay(8);
					}
					attackTick++;
					if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 70 ) {
						step++;
						attackTick = 0;
					}
				}
				
				// attack
				if(step == 1) {
					dx = 0;
					damage = 3;
					if(player.getx() < x) facingRight = false;
					else{
						facingRight = true;
					}
					
					if(currentAction != ATTACKING) {
						currentAction = ATTACKING;
						stop = true;
						animation.setFrames(attack);
						animation.setDelay(4);
						cwidth = 70;
					}
					if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
						step++;
						currentAction = WALKING;
						stop = false;
						damage = 1;
						animation.setFrames(sprites);
						animation.setDelay(8);
					}
				}
				if(step == 2) {
					cwidth = 30;
					step = 0;
				}
		
		super.update(gc, dt);
		
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		if(flinching) {
			if(flinching) {
				if(flinchCount % 10 < 5) return;
			}
		}
		
		super.render(gc, r);
		
	}

}
