package com.quad.levels;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.quad.Tile.Background;
import com.quad.Tile.TileMap;
import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.core.fx.Light;
import com.quad.entity.Enemy;
import com.quad.entity.Explosion;
import com.quad.entity.FireBall;
import com.quad.entity.HUD;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.entity.SaveInfo;
import com.quad.entity.boss.AndroBoss;
import com.quad.entity.enemies.Golem;
import com.quad.entity.enemies.Robot;
import com.quad.items.Fire;

public class AndroBossStage extends State{
	
	//Light 
	private ArrayList<Light> lights;
	private int ambient;
		
	//tilemap
	private TileMap tileMap;
	
	//player
	private Player player;
	
	//enemies
	private ArrayList<Enemy> enemies;
	private AndroBoss boss;
	
	//explosions
	private ArrayList<Explosion> explosions;
	
	//Item manager
	private ArrayList<Item> items;
	
	//HUD
	private HUD hud;
	
	//background
	private Background front;
	
	//events
	private boolean showStart;
	private boolean eventBegin;
	private boolean eventStart;
	private boolean eventPlay;
	private boolean blockinput;
	
	private int step;
	private int stepCount;
	
	@Override
	public void init(GameContainer gc) {
		Settings.setLight(gc, true);
		
		//init light
		lights = new ArrayList<Light>();
		ambient = 0x686851;
		
		//init tilemap
		tileMap = new TileMap(gc,16);
		tileMap.loadTiles("/TileSets/inside.gif");
		tileMap.loadMap("/Maps/AndroBoss.map");
		tileMap.setPosition(0, 0);
		
		//objects
		items = new ArrayList<Item>();
		
		//background
		front = new Background("/Backgrounds/insideC.png",0.4);
		
		//explosions
		explosions = new ArrayList<Explosion>();
		
		//enemies
		enemies = new ArrayList<Enemy>();
		
		//init player
		player = new Player(tileMap);
		player.init(enemies);
		player.setPosition(100, 340);
		
		//player settings
		player.setHealth(SaveInfo.getHealth());
		player.setLives(SaveInfo.getLives());
		player.setFireballs(SaveInfo.getFireballs());
		
		//boss
		boss = new AndroBoss(tileMap, player, enemies,explosions, items);
		
		//event
		eventBegin = true;
		
		setEnemies();
		setLights();
		setItems();
		
		//init HUD
		hud = new HUD(player);
		
	}
	
	private void setItems(){
		items.clear();
		
		Fire f;
		
		f = new Fire(tileMap, player);
		f.setPosition(170, 250);
		items.add(f);
	}
	
	private void setEnemies(){
		enemies.clear();
		
		boss.setPosition(-9000, -9000);
		enemies.add(boss);
		
		/*Golem g;
		
		g = new Golem(tileMap, player);
		g.setPosition(200, 250);
		enemies.add(g);*/
		
		/*Robot r;
		
		r = new Robot(tileMap, player, enemies);
		r.setPosition(200, 250);
		enemies.add(r);*/
	}
	
	private void setLights(){
		lights.clear();
		
		Light l;
		
		l = new Light(0xe5e5b0, 225);
		lights.add(l);
	}
	
	@Override
	public void update(GameContainer gc, float dt) {
		
		//input
		handleInput(gc);
		
		//events
		if(eventBegin) eventBegin();
		
		//update tilmap and position
		tileMap.setPosition(
				Settings.WIDTH / 2 - player.getx(),
				Settings.HEIGHT / 2 - player.gety()
			);
			tileMap.update();
			tileMap.fixBounds();
		tileMap.update();
		
		//items
		for(int i = 0; i < items.size(); i++){
			Item o = items.get(i);
			o.update(gc, dt);
			if(o.shouldRemove()){
				items.remove(i);
				i--;
			}
		}
		
		//enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update(gc, dt);
			if(e.shouldRemove() || e.isDead()){
				explosions.add(new Explosion(tileMap, e.getx(), e.gety(),1));
				enemies.remove(i);
				i--;
			}	
		}
		
		//explosions
		for(int i = 0; i < explosions.size(); i++) {
			Explosion e = explosions.get(i);
			e.update(gc, dt);
			if(e.shouldRemove()){
				explosions.remove(i);
				i--;
			}
			
		}
		
		//player
		player.update(gc, dt);
		
		//player settings
		SaveInfo.setHealth(player.getHealth());
		SaveInfo.setLives(player.getLives());
		SaveInfo.setFireballs(player.getFireballs());
		
		//update background
		front.setPosition(tileMap.getx(), tileMap.gety()+30);
		front.update();
		
	}
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.setAmbientLight(ambient);
		
		//render bg
		front.draw(r);
		
		//render tilemap
		tileMap.draw(r);
		
		//items
		for(int i = 0; i < items.size();i++){
			items.get(i).render(r, gc);
		}
		
		//enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).render(gc, r);
		}
		
		//explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).render(gc, r);
		}
		
		//lights
		for(int i = 0; i < lights.size(); i++){
			r.drawLight(lights.get(i), (int)(player.getx() + tileMap.getx()), (int)(player.gety() + tileMap.gety()));
		}
		
		
		//player
		player.render(gc, r);
		
		//render HUD
		hud.render(r);
		
	}
	
	private void handleInput(GameContainer gc){
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F3)){
			Settings.changeFps(gc, 120);
		}else if(gc.getInput().isKeyReleased(KeyEvent.VK_F3)){
			Settings.changeFps(gc, 60);
		}
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F4)){
			gc.getGame().setState(gc, AbstractGame.STAGEONE);
			gc.getGame().init(gc);
		}
		
		if(blockinput)return;
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F1)) Settings.setLight(gc, !Settings.LIGHT);
		player.setRight(gc.getInput().isKey(KeyEvent.VK_RIGHT));
		player.setLeft(gc.getInput().isKey(KeyEvent.VK_LEFT));
		player.setJumping(gc.getInput().isKey(KeyEvent.VK_UP));
		player.setDown(gc.getInput().isKey(KeyEvent.VK_DOWN));
		
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F))player.gainFireBall();
		if(gc.getInput().isKeyPressed(KeyEvent.VK_Z))player.setAttacking();
		if(gc.getInput().isKeyPressed(KeyEvent.VK_X))player.setFiring();
	}
	
	private void eventBegin(){
		if(step == 0){
			if(player.getx() >= 270){
				blockinput = true;
				player.setLeft(false);
				player.setRight(false);
				player.setJumping(false);
				boss.setActive();
				if(step == 0)
				step = 1;
			}
		}
		
		if(step == 1){
			stepCount++;
			if(stepCount == 1)player.setEmote(Player.CONFUSED);
			if(stepCount == 100) tileMap.setShaking(true, 2);
			if(stepCount == 150) player.setEmote(0);
			if(stepCount == 500){
				tileMap.setShaking(false, 1);
				blockinput = false;
			}
		}
	}
	
	@Override
	public void dipose() {
		// TODO Auto-generated method stub
		
	}

}
