package com.quad.entity;

import java.awt.Point;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;

public class Explosion extends GameObject{
	
private Image[] sprites;
	
	private boolean remove;
	
	private Point[] points;
	private int speed;
	private double diagSpeed;
	
	public Explosion(TileMap tm, int x, int y) {
		
		super(tm);
		
		this.x = x;
		this.y = y;
		
		width = 30;
		height = 30;
		
		speed = 2;
		diagSpeed = 1.41;
		
		sprites = Content.Smoke[0];
		
		animation.setFrames(sprites);
		animation.setDelay(6);
		
		points = new Point[8];
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}
		
	}
	
	public Explosion(TileMap tm, int x, int y, int type) {
			
		super(tm);
		
		this.x = x;
		this.y = y;
		
		speed = 2;
		diagSpeed = 1.41;
		
		if(type == 0){
			sprites = Content.Smoke[0];
			width = 30;
			height = 30;
		}else if(type == 1){
			sprites = Content.Smoke1[0];
			width = 16;
			height = 16;
		}
		
		animation.setFrames(sprites);
		animation.setDelay(6);
		
		points = new Point[8];
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}
			
		}
	
	
	
	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagSpeed;
		points[1].y += diagSpeed;
		points[2].y += speed;
		points[3].x -= diagSpeed;
		points[3].y += diagSpeed;
		points[4].x -= speed;
		points[5].x -= diagSpeed;
		points[5].y -= diagSpeed;
		points[6].y -= speed;
		points[7].x += diagSpeed;
		points[7].y -= diagSpeed;
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void render(GameContainer gc, Renderer r) {
		super.renderComponents(gc, r);
		setMapPosition();
		for(int i = 0; i < points.length; i++) {
			r.drawImage(
				animation.getImage(),
				(int) (points[i].x + xmap - width / 2),
				(int) (points[i].y + ymap - height / 2)
			);
		}
	}

	@Override
	public void componentEvent(String name, GameObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
