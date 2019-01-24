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
import com.quad.items.Heart;

public class DarkBat extends Enemy{

	private Image[] sprites;
	private Image[] attack;
	private Image[] hit;
	
	private Player player;
	private boolean active;
	
	private ArrayList<Enemy> enemies;
	
	private static final int FLYING = 0;
	private static final int ATTACKING = 1;
	private static final int HIT = 2;
	
	private int attackTick;
	private int attackDelay = 60;
	private int step;
	
	//items
	ArrayList<Item> items;
	
	public DarkBat(TileMap tm, Player p, ArrayList<Enemy> en, ArrayList<Item> it) {
		
		super(tm);
		player = p;
		enemies = en;
		items = it;
		
		health = maxHealth = 1;
		
		width = 25;
		height = 25;
		cwidth = 20;
		cheight = 15;
		
		damage = 1;
		moveSpeed = 0.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		
		sprites = Content.DarkBat[0];
		attack = Content.DarkBat[1];
		hit = Content.DarkBat[2];
		
		//shadow
		for(int i = 0; i < sprites.length;i++){
			sprites[i].shadowType = ShadowType.FADE;
		}
		
		animation.setFrames(sprites);
		animation.setNumFrames(4);
		animation.setDelay(8);
		
		left = true;
		facingRight = false;
		
	}
	
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) dx = moveSpeed;
		else dx = 0;
		
		if(x <= 0){
			left = false;
			right = facingRight = true;
		}
		if(x + width >= tileMap.getWidth()){
			right = facingRight = false;
			left = true;
		}
		
	}
	
	public void update(GameContainer gc, float dt) {
		
		if(!active) {
			if(Math.abs(player.getx() - x) < Settings.WIDTH) active = true;
			return;
		}
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
		
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		/*if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}*/
		setPosition(xtemp, ytemp);
		
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		
		//is dead
		if(dead){
			int i = new Random().nextInt(2);
			//if(i != 1)return;
			Heart h = new Heart(tileMap, player);
			h.setPosition(x, y);
			h.setVector(-0.5, 1);
			items.add(h);
		}
		
		// update animation
		animation.update();
		
		// idle
				if(step == 0) {
					if(currentAction != FLYING) {
						currentAction = FLYING;
						animation.setFrames(sprites);
						animation.setNumFrames(4);
						animation.setDelay(8);
					}
					attackTick++;
					if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 20 && Math.abs(player.gety() - y) < 100 ) {
						if(player.gety() < y) return;
						step++;
						attackTick = 0;
					}
				}
				
				// attack
				if(step == 1) {
					if(currentAction != ATTACKING) {
						currentAction = ATTACKING;
						animation.setFrames(attack);
						animation.setDelay(8);
						DarkEnergy de = new DarkEnergy(tileMap);
						de.setType(DarkEnergy.GRAVITY);
						de.setPosition(x, y);
						if(facingRight) de.setVector(0.5, 1);
						else de.setVector(-0.5, 1);
						enemies.add(de);
					}
					if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
						step++;
						currentAction = FLYING;
						animation.setFrames(sprites);
						animation.setNumFrames(4);
						animation.setDelay(8);
					}
				}
				// land
				if(step == 2) {
					step = 0;
				}
		
		super.update(gc, dt);
		
	}
	
	public void render(GameContainer gc, Renderer r) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.render(gc, r);
		
	}

}
