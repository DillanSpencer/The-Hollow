package com.quad.items;

import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;
import com.quad.core.fx.SoundClip;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.entity.Sparkle;

public class Fire extends Item{
	
	//player
		private Player player;
		
		//sprites
		private Image[] sprites;
		
		//movement
		private int tick;
		private double b;

		public Fire(TileMap tm, Player p) {
			super(tm);

			player = p;
			
			sparkles = new ArrayList<Sparkle>();
			
			moveSpeed = 0.5;
			fallSpeed = 0.15;
			maxFallSpeed = 4.0;
			jumpStart = -5;
			
			width = 17;
			height = 32;
			cwidth = 16;
			cheight = 32;
			coffy = 10;
			
			tick = 0;
			b = 1 * 0.06 + 0.07;
			
			sprites = Content.Fire[0];
			
			animation.setFrames(sprites);
			animation.setDelay(-1);
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
			
			x+=dx;
			y+=dy;
			
		}

		public void update(GameContainer gc, float dt){
			
			super.update(gc, dt);
			
			tick++;
			
			getNextPosition();
			checkTileMapCollision();
			calculateCorners(x, ydest + 1);
			
			if(dy == 0)
			y = Math.sin(b * tick) + y;
			
			//update sparkles
			for(int i = 0; i < sparkles.size(); i++){
				Sparkle s = sparkles.get(i);
				s.update(gc, dt);
				if(s.shouldRemove()){
					sparkles.remove(i);
					i--;
				}
			}
			
			//check to see if player collides with item, and then adds one health to the player
			if(getRectangle().intersects(player.getRectangle())){
				remove = true;
				dead = true;
				Sparkle s = new Sparkle(tileMap);
				s.setPosition(x, y);
				sparkles.add(s);
				player.gainFireBall();
			}
			
			//sound of pickup
			if(remove || dead){
				SoundClip.play("item");
			}
			
			animation.update();
			
		}
		
		public void render(Renderer r, GameContainer gc){
			super.render(r, gc);
			
			for(int i = 0; i < sparkles.size(); i++){
				sparkles.get(i).render(gc, r);
			}
		}

		@Override
		public void componentEvent(String name, GameObject object) {
			
		}

		@Override
		public void dispose() {
			
		}

}
