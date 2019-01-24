package com.quad.entity.enemies;

import java.util.ArrayList;
import java.util.Random;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.entity.Enemy;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.items.Fire;

public class Robot extends Enemy{
	
	//player
	private Player player;
	
	//enemies
	private ArrayList<Enemy> enemies;
	
	//Items
	private ArrayList<Item> items;
	
	//sprites
	private Image[] idleSprites;
	private Image[] angrySprites;
	private Image[] attackSprites;
	
	//actions
	private static final int IDLE = 0;
	private static final int ANGRY = 1;
	private static final int ATTACKING = 2;
	
	//event variables
	private int attackTick;
	private int attackDelay;
	private int step;
	private boolean stop;

	private int tick;
	private double a;
	private double b;
	
	public Robot(TileMap tm, Player p, ArrayList<Enemy> e, ArrayList<Item> it) {
		
		super(tm);
		
		health = maxHealth = 2;
		
		player = p;
		enemies = e;
		items = it;
		
		width = 15;
		height = 28;
		cwidth = 25;
		cheight = 15;
		
		damage = 1;
		moveSpeed = 5;
		attackDelay = 60;
		
		idleSprites = Content.Robot[0];
		angrySprites = Content.Robot[1];
		attackSprites = Content.Robot[2];
		step = 0;
		
		
		tick = 0;
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
		
		animation.setFrames(idleSprites);
		animation.setDelay(10);
				
	}
	
	private void getNextPosition(){
		x += dx;
		y += dy;
	}
	
	
	public void update(GameContainer gc, float dt) {
		
		super.update(gc, dt);
		
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) flinching = false;
		}
				
		// update animation
		animation.update();
		
		//position
		getNextPosition();
		
		//is dead
		if(dead){
			int i = new Random().nextInt(2);
			//if(i != 1)return;
			Fire h = new Fire(tileMap, player);
			h.setPosition(x, y);
			h.setVector(-0.5, 1);
			items.add(h);
		}
				
		//handle events
		
		if(step == 0) {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				animation.setFrames(idleSprites);
				animation.setDelay(10);
			}
			
			tick++;
			x = Math.sin(a * tick) + x;
			y = Math.sin(b * tick) + y;
			
			attackTick++;
			if(attackTick >= attackDelay && Math.abs(player.getx() - x) < 30 ) {
				step++;
				attackTick = 0;
			}
		}
		
		//chase
		if(step == 1){
			if(currentAction != ANGRY){
				currentAction = ANGRY;
				animation.setFrames(angrySprites);
				animation.setNumFrames(5);
				animation.setDelay(10);
			}
			
			tick++;
			b = 1* 0.02 + 0.07;
			y =  Math.sin(b * tick) + y;
			
			attackTick++;
			
			if(attackTick >= attackDelay){
				if(currentAction != ATTACKING) {
					currentAction = ATTACKING;
					animation.setFrames(attackSprites);
					animation.setNumFrames(4);
					animation.setDelay(10);
					if(player.getx() < x){
						int t = (int) Math.sqrt((2*(x - player.getx()) / 0.09));
						int dx = (int) ((x - (player.getx() - player.getCHeight())) / t);
						int dy = (int) (((player.gety() + player.getCHeight())-y) / t);
						DarkEnergy de = new DarkEnergy(tileMap,1);
						de.setType(DarkEnergy.VECTOR);
						de.setPosition(x, y);
						if(!facingRight) de.setVector(-dx,dy);
						else de.setVector(dx, dy);
						enemies.add(de);
						
					}else{
						int t = (int) Math.sqrt((2*(player.getx() - x / 0.09)));
						int dx = (int) ((x + (player.getx() - player.getCHeight())) / t);
						int dy = (int) (((player.gety() + player.getCHeight())-y) / t);
						DarkEnergy de = new DarkEnergy(tileMap,1);
						de.setType(DarkEnergy.VECTOR);
						de.setPosition(x, y);
						if(!facingRight) de.setVector(-dx,dy);
						else de.setVector(-dx, dy);
						enemies.add(de);
					}
					attackTick = 0;
				}
				if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
					step--;
					currentAction = ANGRY;
					animation.setFrames(angrySprites);
					animation.setDelay(10);
				}
			}
			
			if(Math.abs(player.getx() - x) > 90){
				step--;
				
			}
		}
		
		// attack
		if(step == 2) {
			if(currentAction != ATTACKING) {
				currentAction = ATTACKING;
				animation.setFrames(attackSprites);
				animation.setNumFrames(4);
				animation.setDelay(10);
				DarkEnergy de = new DarkEnergy(tileMap,1);
				de.setType(DarkEnergy.VECTOR);
				de.setPosition(x, y);
				if(facingRight) de.setVector(Math.abs(player.getx() / x)*0.09 + 0.06 , Math.abs(player.gety() / y)+1);
				else de.setVector(-0.5, 1);
				enemies.add(de);
			}
			if(currentAction == ATTACKING && animation.hasPlayedOnce()) {
				step--;
				currentAction = ANGRY;
				animation.setFrames(angrySprites);
				animation.setDelay(10);
			}
		}
		// land
		if(step == 2) {
			step = 0;
		}
	}
	
	public void render(Renderer r, GameContainer gc) {
		
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) return;
		}
		
		super.render(gc, r);
		
	}

}
