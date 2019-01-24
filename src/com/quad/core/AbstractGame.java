package com.quad.core;


import com.quad.core.components.State;
import com.quad.core.fx.SoundClip;
import com.quad.levels.AndroBossStage;
import com.quad.levels.Intro;
import com.quad.levels.PauseState;
import com.quad.levels.StageThree;
import com.quad.levels.StageTwo;
import com.quad.levels.StartingArea;


public class AbstractGame{
	
	private State[] states;
	private int currentState;
	
	//Pause
	private PauseState pauseState;
	private boolean paused;
	
	//states
	public static final int NUMSTATES = 10;
	public static final int MENU = 0;
	public static final int STAGEONE = 1;
	public static final int STAGETWO = 2;
	public static final int STAGETHREE = 3;
	public static final int BOSSSTAGE = 9;
	
	public AbstractGame(){
		
		states = new State[NUMSTATES];
		
		pauseState = new PauseState();
		paused = false;
		
		SoundClip.init();
		
		//load basic sounds
		SoundClip.load("/Sfx/explode.mp3", "explode");
		SoundClip.load("/Sfx/playerlands.mp3", "land");
		SoundClip.load("/Sfx/swing.wav", "attack");
		SoundClip.load("/Sfx/playerhit.mp3", "hit");
		SoundClip.load("/Sfx/playerjump.mp3", "jump");
		SoundClip.load("/Sfx/ekill.mp3", "ekill");
		SoundClip.load("/Sfx/menuselect.mp3", "item");

		
		
		currentState = MENU;
		loadState(currentState);
	}
	
	private void loadState(int state) {
		if(state == MENU)states[currentState] = new Intro();
		if(state == STAGEONE)states[currentState] = new StartingArea();
		if(state == STAGETWO)states[currentState] = new StageTwo();
		if(state == STAGETHREE)states[currentState] = new StageThree();
		if(state == BOSSSTAGE)states[currentState] = new AndroBossStage();
	}
	
	private void unloadState(int state) {
		states[state] = null;
	}
	
	public void setState(GameContainer gc,int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
		init(gc);
	}
	
	public void setPaused(GameContainer gc, boolean i){
		paused = i;
		
		if(paused) init(gc);
	}
	
	public void init(GameContainer gc){
		if(paused){
			pauseState.init(gc);
			return;
		}
		if(states[currentState] != null) states[currentState].init(gc);
	}
	
	public void update(GameContainer gc, float dt){
		if(paused){
			pauseState.update(gc, dt);
			return;
		}
		if(states[currentState] != null) states[currentState].update(gc, dt);
	}
	
	public void render(GameContainer gc, Renderer r){
		if(paused){
			pauseState.render(gc, r);
			return;
		}
		if(states[currentState] != null) states[currentState].render(gc, r);
		
	}
	
}
