package com.quad.entity.boss;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.entity.Enemy;
import com.quad.entity.Explosion;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.entity.enemies.DarkEnergy;
import com.quad.entity.enemies.Golem;
import com.quad.entity.enemies.Robot;

public class AndroBoss extends Enemy{
	

	private Image[] sprites;
	private Player player;
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	private ArrayList<Item> items;
	
	private boolean active;
	private boolean finalAttack;
	
	//sprites
	public static int normal = 0;
	public static int face = 1;
	public static int look = 2;
	
	private int step;
	private int stepCount;
	
	//attack pattern
	private int[] steps = {0, 2, 3, 1, 2, 3, 1, 2, 3, 1};
	
	private double ticks;
	private int tick;
	private double a;
	private double b;
	
	//attack
	private int attackTick;
	private int attackDelay;

	public AndroBoss(TileMap tm,Player p, ArrayList<Enemy> en, ArrayList<Explosion> e, ArrayList<Item> it) {
		super(tm);
		
		player = p;
		enemies = en;
		explosions = e;
		items = it;
		
		
		width = 57;
		height = 88;
		cwidth = 30;
		cheight = 50;
		
		health = maxHealth = 20;
		
		moveSpeed = 0.9;
		
		setSprites(look);
		
		damage = 1;
		
		attackDelay = 80;
		attackTick = 0;
				
		step = 0;
		stepCount = 0;
		
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
		
		
	}
	
	public void setActive(){
		active = true;
	}
	
	public void setSprites(int i){
		if(i == normal){
			sprites = Content.AndroBoss[0];
			animation.setFrames(sprites);
			animation.setDelay(8);
		}
		if(i == face){
			sprites = Content.AndroBoss[1];
			animation.setFrames(sprites);
			animation.setDelay(8);
			
		}
		if(i == look){
			sprites = Content.AndroBoss[2];
			animation.setFrames(sprites);
			animation.setDelay(10);
			animation.setNumFrames(4);
		}
	}
	
	public void update(GameContainer gc, float dt){
		super.update(gc, dt);
		if(health == 0)return;
		
		if(step == steps.length)step = 1;
		
		ticks++;
		System.out.println(health);
		
		if(flinching){
			flinchCount++;
			if(flinchCount == 100)flinching = false;
			
		}
		
		if(gc.getInput().isKeyPressed(KeyEvent.VK_A)){
			setSprites(face);
		}else if(gc.getInput().isKeyPressed(KeyEvent.VK_S)) setSprites(normal);
		
		if(gc.getInput().isKeyPressed(KeyEvent.VK_D)){
			dx = -dx;
			facingRight = !facingRight;
		}
				
		x+= dx;
		y+= dy;
		
		animation.update();
		
		if(!active)return;
		
		
		/////////
		//swoop down
		if(steps[step] == 0){
			stepCount++;
			
			if(stepCount == 1) {
				x = 9000;
				y = -9000;
			}
			
			if(stepCount == 60){
				x = 300;
				y = 0;
				dx = 0;
				dy = 0;
			}
			
			if(stepCount == 61) dy = 0.5;
			
			if(stepCount == 500) dy = 0;
			
			if(stepCount == 650){
				stepCount = 0;
				step++;
				dx = dy = 0;
			}
		}
		
		//attack across
		if(steps[step] == 1){
			stepCount++;
			if(stepCount == 1) {
				explosions.add(new Explosion(tileMap, (int)x, (int)y));
				setSprites(normal);
				x = -9000;
				y = 9000;
				dx = dy = 0;
			}
			if(stepCount == 60) {
				if(player.getx() > tileMap.getWidth() / 2) {
					facingRight = true;
					x = 30;
					y = tileMap.getHeight() - 60;
					dx = 4;
				}
				else {
					facingRight = false;
					x = tileMap.getWidth() - 30;
					y = tileMap.getHeight() - 60;
					dx = -4;
				}
				explosions.add(new Explosion(tileMap, (int)x, (int)y));
			}
			if((dx == -4 && x < 30) || (dx == 4 && x > tileMap.getWidth() - 30)) {
				stepCount = 0;
				step++;
				dx = dy = 0;
			}
		}
		
		//goto right side and attack
		if(steps[step] == 2){
			stepCount++;
			if(stepCount == 1){
				explosions.add(new Explosion(tileMap,(int)x, (int)y));
				setSprites(normal);
				x = tileMap.getWidth() - 60;
				facingRight = false;
				y = 325;
				dx = dy = 0;
				explosions.add(new Explosion(tileMap,(int)x, (int)y));
			}
			
			attackTick++;
			tick++;
			
			//movement
			b = 1* 0.02 + 0.07;
			y =  Math.sin(b * tick) + y;
			
			//attack
			if(attackTick >= attackDelay){
				setSprites(face);
				int t = (int) Math.sqrt((2*(x - player.getx()) / 0.09));
				int dx = (int) ((x - (player.getx() - player.getCHeight())) / t);
				int dy = (int) (((player.gety() + player.getCHeight())-y) / t);
				DarkEnergy de = new DarkEnergy(tileMap,1);
				de.setType(DarkEnergy.VECTOR);
				de.setPosition(x, y);
				if(!facingRight) de.setVector(-dx,dy);
				else de.setVector(dx, dy);
				enemies.add(de);
				attackTick = 0;
			}
			
			if(stepCount == (attackDelay* 3) + 60){
				stepCount=0;
				setSprites(look);
				step++;
			}
		}
		
		//goto middle and attack
		if(steps[step] == 3){
			stepCount++;
			if(stepCount == 1){
				explosions.add(new Explosion(tileMap,(int)x, (int)y));
				x = 300;
				y = 250;
				explosions.add(new Explosion(tileMap,(int)x, (int)y));
				dx = dy = 0;
			}
			
			tick++;
			x = Math.sin(a * tick) + x;
			y = Math.sin(b * tick) + y;
			
			if(stepCount == 100){
				explosions.add(new Explosion(tileMap,100, 350,1));
				explosions.add(new Explosion(tileMap,400, 350,1));
				
				Golem g;
				
				g = new Golem(tileMap, player,items);
				g.setPosition(100, 350);
				enemies.add(g);
				
				g = new Golem(tileMap, player,items);
				g.setPosition(400, 350);
				enemies.add(g);
				
				Robot r;
				
				r = new Robot(tileMap, player, enemies,items);
				r.setPosition(125, 250);
				enemies.add(r);
				
				r = new Robot(tileMap, player, enemies,items);
				r.setPosition(425, 250);
				enemies.add(r);
				
			}
			
			if(stepCount >= 90 && stepCount % 30 == 0 && health <= maxHealth/2){
				DarkEnergy de = new DarkEnergy(tileMap, 1);
				de.setPosition(x, y);
				de.setVector(3 * Math.sin(stepCount / 32), 3 * Math.cos(stepCount / 32));
				de.setType(DarkEnergy.BOUNCE);
				enemies.add(de);
			}
			
			//switch to next step if enemies are dead
			if( stepCount >= 100 && enemies.size() == 1){
				stepCount = 0;
				step++;
			}
			
		}
		
		
		
	}
	
	
	public void render(GameContainer gc, Renderer r) {
		if(flinching) {
			if(flinchCount % 10 < 5) return;
		}
		super.render(gc, r);
	}


}
