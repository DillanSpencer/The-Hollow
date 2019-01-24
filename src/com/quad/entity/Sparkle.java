package com.quad.entity;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Content;

public class Sparkle extends GameObject{
	
	private boolean remove;
	
	public Sparkle(TileMap tm) {
		super(tm);
		animation.setFrames(Content.Sparkle[0]);
		animation.setDelay(50);
		width = height = 100;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);
		animation.update();
		if(animation.hasPlayedOnce()) remove = true;
	}
	
	public void render(GameContainer gc, Renderer r) {
		super.renderComponents(gc, r);
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
