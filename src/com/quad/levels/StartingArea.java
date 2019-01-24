package com.quad.levels;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.quad.Tile.Background;
import com.quad.Tile.TileMap;
import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.core.fx.Light;
import com.quad.core.fx.SoundClip;
import com.quad.effects.Rain;
import com.quad.entity.Enemy;
import com.quad.entity.Explosion;
import com.quad.entity.HUD;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.entity.SaveInfo;
import com.quad.entity.enemies.DarkBat;
import com.quad.entity.enemies.Robot;
import com.quad.items.Fire;



public class StartingArea extends State{
	
	//Light 
	private ArrayList<Light> lights;
	
	//tilemap
	private TileMap tileMap;
	private int startingPosX;
	private int startingPosY;
	
	//player
	private Player player;
	
	//enemies
	private ArrayList<Enemy> enemies;
	
	//Item manager
	private ArrayList<Item> items;
	
	//blackout screen
	private ArrayList<Rectangle> tb;
	
	//Explosions
	private ArrayList<Explosion> explosions;
	
	//HUD
	private HUD hud;
	
	//background
	private Background bg;
	private Background front;
	private Background mid;
	
	//rain
	private Rain rain;
	private int randRain;
		
	private float ambient;
	
	//events
	private boolean showStart;
	private boolean eventStart;
	private boolean eventPlay;
	private boolean blockinput;
	
	private int step;
	private int stepCount;
	
	private double textX, textY;
		
	@Override
	public void init(GameContainer gc) {
		Settings.setLight(gc, true);
		SoundClip.load("/Music/Theme2.mp3", "theme2");
		SoundClip.loop("theme2");
		
		//init light
		lights = new ArrayList<Light>();
		
		//init tilemap
		tileMap = new TileMap(gc,16);
		tileMap.loadTiles("/TileSets/inside.gif");
		tileMap.loadMap("/Maps/Stage1.map");
		tileMap.setPosition(0, 0);
		startingPosX = 100;
		startingPosY = 400;
		
		//background
		bg = new Background("/Backgrounds/outside1.png",0.05);
		front = new Background("/Backgrounds/outside.png",0.1);
		
		//enemies
		enemies = new ArrayList<Enemy>();
		
		//objects
		items = new ArrayList<Item>();
		
		//blackout screen
		tb = new ArrayList<Rectangle>();
		
		//explosions
		explosions = new ArrayList<Explosion>();
		
		//sfx
		//
				
		//init player
		player = new Player(tileMap);
		player.init(enemies);
		player.setPosition(startingPosX, startingPosY);
		
		//player settings
		SaveInfo.init();
		player.setHealth(SaveInfo.getHealth());
		player.setLives(SaveInfo.getLives());
		player.setFireballs(SaveInfo.getFireballs());
		
		//init HUD
		hud = new HUD(player);
		
		//init rain
		rain = new Rain(Rain.MEDIUM, tileMap);
		rain.init();
		randRain =  new Random().nextInt(10);
		
		//ambient light
		ambient = 0x4e8ded;
		
		tileMap.setTween(1);
		setEnemies();
		setLights();
		setItems();
		
		//event start
		stepCount = 0;
		step = 0;
		textX = Settings.WIDTH / 2 - 50;
		textY = -50;
		eventStart = true;
		
	}
	
	private void setLights(){
		lights.clear();
		
		Light l;
		l = new Light(0xe5e5b0, 150);
		lights.add(l);
	}
	
	private void setEnemies(){
		enemies.clear();		
		
		DarkBat b;
		
		b = new DarkBat(tileMap, player, enemies, items);
		b.setPosition(300, 300);
		enemies.add(b);
		
		Robot r;
		
		r = new Robot(tileMap, player, enemies,items);
		r.setPosition(500, 400);
		enemies.add(r);
		
	}
	
	private void setItems(){
		items.clear();
		
		Item i;
		
		i = new Fire(tileMap, player);
		i.setPosition(10, 200);
		items.add(i);
		
	}

	@Override
	public void update(GameContainer gc, float dt) {
		
		//events
		if(eventStart) eventStart();
		if(eventPlay) eventPlay();
		
		//update tilmap and position
		tileMap.setPosition(
				Settings.WIDTH / 2 - player.getx(),
				Settings.HEIGHT / 2 - player.gety() 
			);
			tileMap.update();
			tileMap.fixBounds();
		tileMap.update();
		
		//rain 
		if(randRain == 5){
			rain.updateRain();
		}
		
		
		//items
		for(int i = 0; i < items.size(); i++){
			Item o = items.get(i);
			o.update(gc, dt);
			if(o.shouldRemove()){
				items.remove(i);
				i--;
			}
		}
		
		//explosion
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).update(gc, dt);
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		
		
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update(gc, dt);
			if(e.shouldRemove()){
				enemies.remove(i);
				i--;
			}
			
		}
		
		setWorldBounds(gc);
		
		//player
		player.update(gc, dt);
		if(player.getHealth() == 0) blockinput = true;
		
		//player settings
		SaveInfo.setHealth(player.getHealth());
		SaveInfo.setLives(player.getLives());
		SaveInfo.setFireballs(player.getFireballs());
		
		//update background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		bg.update();
		front.setPosition(tileMap.getx(), tileMap.gety()+200);
		front.update();
		
		handleInput(gc);
		
		
	}

	/* (non-Javadoc)
	 * @see com.quad.core.components.State#render(com.quad.core.GameContainer, com.quad.core.Renderer)
	 */
	@Override
	public void render(GameContainer gc, Renderer r) {
		
		r.setAmbientLight( (int) ambient);
		
		//render bg
		front.draw(r);
		bg.draw(r);
		
		//render lights
		r.drawLight(lights.get(0), (int) (player.getx() + tileMap.getx()), (int) (player.gety() + tileMap.gety()));
		
		/*r.drawLight(lights.get(0),(int) (10 + tileMap.getx()), (int)(10+tileMap.gety()));
		r.drawLight(lights.get(1),(int) (300 + tileMap.getx()), (int)(100+tileMap.gety()));
		r.drawLight(lights.get(2),(int) (400 + tileMap.getx()), (int)(100+tileMap.gety()));*/

		
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
		
		//explosion
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).render(gc, r);
		}
		
		//player
		player.render(gc, r);
		
		//rain
		if(randRain == 5){
			rain.renderRain(r);
		}
		
		//render HUD
		hud.render(r);
		
		//blackout screen
		for(int i = 0; i < tb.size(); i++){
			Rectangle re = tb.get(i);
			r.drawFillRect((int)re.getX(),(int) re.getY(), (int)re.getWidth(),(int) re.getHeight(), 0x000000);
		}
		
		
	}
	
	public void handleInput(GameContainer gc){
		
		if(blockinput)return;
		if(gc.getInput().isKeyPressed(KeyEvent.VK_ESCAPE)) gc.getGame().setPaused(gc, true);
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F1)) Settings.setLight(gc, !Settings.LIGHT);
		player.setRight(gc.getInput().isKey(KeyEvent.VK_RIGHT));
		player.setLeft(gc.getInput().isKey(KeyEvent.VK_LEFT));
		player.setJumping(gc.getInput().isKey(KeyEvent.VK_UP));
		player.setDown(gc.getInput().isKey(KeyEvent.VK_DOWN));
		
		if(gc.getInput().isKeyPressed(KeyEvent.VK_F))player.gainFireBall();
		if(gc.getInput().isKeyPressed(KeyEvent.VK_Z))player.setAttacking();
		if(gc.getInput().isKeyPressed(KeyEvent.VK_X))player.setFiring();
	}
	
	//////////Events/////////
	private void eventStart(){
		stepCount++;
		if(stepCount == 1) {
			blockinput = true;
			tileMap.setShaking(true, 1);
			tb.clear();
			tb.add(new Rectangle(0, 0, Settings.WIDTH, Settings.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, Settings.WIDTH / 2, Settings.HEIGHT));
			tb.add(new Rectangle(0, Settings.HEIGHT / 2, Settings.WIDTH, Settings.HEIGHT / 2));
			tb.add(new Rectangle(Settings.WIDTH / 2, 0, Settings.WIDTH / 2, Settings.HEIGHT));
		}
		if(stepCount > 1 && stepCount < 60) {
			tb.get(0).height -= 2;
			tb.get(1).width -= 3;
			tb.get(2).y += 2;
			tb.get(3).x += 3;
		}
		
		if(stepCount >= 70 && stepCount <= 300){
			player.setEmote(Player.SURPRISED);
		}
		
		if(stepCount >= 250){
			player.setEmote(Player.NONE);

			tb.get(0).height -= 1;
			tb.get(1).width -= 1;
			tb.get(2).y += 1;
			tb.get(3).x += 1;
		}
		
		if(stepCount == 300) {
			eventStart = blockinput = false;
			stepCount = 0;
			tb.clear();
			tileMap.setShaking(false, 0);
			SoundClip.play("explode");
		}
		
	}
	
	private void eventPlay(){
		blockinput = false;
		showStart = false;
	}
	
	private void reset(){
		player.reset();
		player.setPosition(startingPosX, startingPosY);
		setEnemies();
		blockinput = true;
		stepCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
		
	}
	
	private void setWorldBounds(GameContainer gc){
		//update on map
		if(player.getx() <= 0 ){
			reset();
		}
		
		if(player.getx() >= tileMap.getWidth()){
			gc.getGame().setState(gc, AbstractGame.STAGETWO);
		}
	}

	@Override
	public void dipose() {
		
	}

}
