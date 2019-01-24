package com.quad.levels;

import java.awt.event.KeyEvent;

import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.State;
import com.quad.core.fx.Image;
import com.quad.core.fx.SoundClip;

public class Intro extends State{
	
	//images
	private Image bg;
	private Image title;
	
	
	@Override
	public void init(GameContainer gc) {
		
		bg = new Image("/Backgrounds/outside.png");
		title = new Image("/Backgrounds/Intro.gif");
		
		//sfx
		SoundClip.load("/Sfx/menuselect.mp3", "select");
		
		//Theme
		SoundClip.load("/Music/Theme1.mp3", "theme");
		SoundClip.loop("theme");
	}

	@Override
	public void update(GameContainer gc, float dt) {
				
		if(gc.getInput().isKeyPressed(KeyEvent.VK_ENTER)){
			SoundClip.play("select");
			gc.getGame().setState(gc, AbstractGame.STAGEONE);
			SoundClip.close("theme");
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(bg);
		r.drawImage(title,0,0);
		
		
	}

	@Override
	public void dipose() {
		
	}

}
