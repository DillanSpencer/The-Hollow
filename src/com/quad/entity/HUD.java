package com.quad.entity;

import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.Content;
import com.quad.core.fx.Image;

public class HUD {
	
private Player player;
	
	private Image[][] health;
	
	public HUD(Player p) {
		player = p;
		health = Content.HealthBar;
	}
	
	public void render(Renderer r) {
		r.drawImage(health[SaveInfo.getHealth()][0], 2, 0);
	}

}
