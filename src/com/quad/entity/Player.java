package com.quad.entity;

import java.awt.Rectangle;
import java.util.ArrayList;


import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Image;
import com.quad.core.fx.SoundClip;


public class Player extends GameObject {
	
	// references
		private ArrayList<Enemy> enemies;
		private ArrayList<FireBall> fireBalls;
		
		// player stuff
		private int lives;
		private int health;
		private int maxHealth;
		private int damage;
		private int chargeDamage;
		private int fireballdamage;
		private boolean knockback;
		private boolean flinching;
		private long flinchCount;
		private int score;
		private boolean doubleJump;
		private boolean alreadyDoubleJump;
		private double doubleJumpStart;
		private int fireDelay;
		private int fireCount;
		private int numFireballs;
		private long time;
		
		// actions
		private boolean dashing;
		private boolean attacking;
		private boolean upattacking;
		private boolean charging;
		private int chargingTick;
		private boolean teleporting;
		private boolean firing;
		
		// animations
		private ArrayList<Image[]> sprites;
		private final int[] NUMFRAMES = {
			1, 8, 1, 1, 8, 6
		};
		private final int[] FRAMEWIDTHS = {
			64, 64, 64, 64, 64, 64
		};
		private final int[] FRAMEHEIGHTS = {
			64, 64, 64, 64, 64, 64
		};
		private final int[] SPRITEDELAYS = {
			-1, 3, -1, -1, 5, 4
		};
		
		private Rectangle ar;
		private Rectangle aur;
		private Rectangle cr;
		
		// animation actions
		private static final int IDLE = 0;
		private static final int WALKING = 4;
		private static final int ATTACKING = 5;
		private static final int JUMPING = 2;
		private static final int FALLING = 3;
		private static final int UPATTACKING = 2;
		private static final int CHARGING = 6;
		private static final int DASHING = 7;
		private static final int KNOCKBACK = 0;
		private static final int DEAD = 1;
		private static final int TELEPORTING = 10;
		private static final int FIREBALL = 11;
		
		// emotes
		private Image confused;
		private Image surprised;
		public static final int NONE = 0;
		public static final int CONFUSED = 1;
		public static final int SURPRISED = 2;
		private int emote = NONE;
		
		public Player(TileMap tm) {
			
			super(tm);
			
			ar = new Rectangle(0, 0, 0, 0);
			ar.width = 50;
			ar.height = 20;
			aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
			cr = new Rectangle(0, 0, 0, 0);
			cr.width = 50;
			cr.height = 40;
			
			width = 30;
			height = 30;
			cwidth = 30;
			cheight = 35;
			coffy = 14;
			coffx = 0;
			
			moveSpeed = 1.6;
			maxSpeed = 2.6;
			stopSpeed = 0.3;
			fallSpeed = 0.15;
			maxFallSpeed = 4.0;
			jumpStart = -5;
			stopJumpSpeed = 0.3;
			doubleJumpStart = -3.5;
			fireDelay = 10;
			numFireballs = 0;
			
			damage = 1;
			chargeDamage = 1;
			fireballdamage = 2;
			
			facingRight = true;
			
			lives = 3;
			health = maxHealth = 5;
			
			fireBalls = new ArrayList<FireBall>();
			
			// load sprites
			try {
				
				Image spritesheet = new Image(
						"/Sprites/Player/Character.gif"
					);
				
				int count = 0;
				sprites = new ArrayList<Image[]>();
				for(int i = 0; i < NUMFRAMES.length; i++) {
					Image[] bi = new Image[NUMFRAMES[i]];
					for(int j = 0; j < NUMFRAMES[i]; j++) {
						bi[j] = spritesheet.getSubimage(
							j * FRAMEWIDTHS[i],
							count,
							FRAMEWIDTHS[i],
							FRAMEHEIGHTS[i]
						);
					}
					sprites.add(bi);
					count += FRAMEHEIGHTS[i];
				}
				
				// emotes
				spritesheet = new Image(
					"/HUD/Emotes.gif"
				);
				confused = spritesheet.getSubimage(
					0, 0, 14, 17
				);
				surprised = spritesheet.getSubimage(
					14, 0, 14, 17
				);
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			
			setAnimation(IDLE);
			
			
		}
		
		public void init(
			ArrayList<Enemy> enemies){
			this.enemies = enemies;
		}
		
		public int getHealth() { return health; }
		public int getMaxHealth() { return maxHealth; }
		
		public int getNumFireBalls(){
			return numFireballs;
		}
		
		public void gainFireBall(){
			numFireballs++;
		}
		
		public void setEmote(int i) {
			emote = i;
		}
		public void setTeleporting(boolean b) { teleporting = b; }
		
		public void setJumping(boolean b) {
			if(knockback) return;
			if(b && !jumping && falling && !alreadyDoubleJump) {
				doubleJump = true;
			}
			jumping = b;
		}
		public void setAttacking() {
			if(knockback) return;
			if(charging) return;
			if(up && !attacking) upattacking = true;
			else attacking = true;
		}
		public void setFiring(){
			if(knockback) return;
			if(charging) return;
			if(!firing)firing = true;
		}
		public void setCharging() {
			if(knockback) return;
			if(!attacking && !upattacking && !charging) {
				charging = true;
				//JukeBox.play("playercharge");
				chargingTick = 0;
			}
		}
		public void setDashing(boolean b) {
			if(!b) dashing = false;
			else if(b && !falling) {
				dashing = true;
			}
		}
		public boolean isDashing() { return dashing; }
		
		public void setDead() {
			health = 0;
			stop();
		}
		
		public String getTimeToString() {
			int minutes = (int) (time / 3600);
			int seconds = (int) ((time % 3600) / 60);
			return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
		}
		public long getTime() { return time; }
		public void setTime(long t) { time = t; }
		public void setHealth(int i) { health = i; }
		public void setLives(int i) { lives = i; }
		public void gainLife() { lives++; }
		public void loseLife() { lives--; }
		public int getLives() { return lives; }
		public int getFireballs(){return numFireballs;}
		public void setFireballs(int i){numFireballs = i;}
		
		public void increaseScore(int score) {
			this.score += score; 
		}
		
		public int getScore() { return score; }
		
		public void hit(int damage) {
			if(flinching) return;
			SoundClip.play("hit");
			stop();
			health -= damage;
			if(health < 0) health = 0;
			flinching = true;
			flinchCount = 0;
			if(facingRight) dx = -1;
			else dx = 1;
			dy = -3;
			knockback = true;
			falling = true;
			jumping = false;
		}
		
		public void reset() {
			health = maxHealth;
			facingRight = true;
			currentAction = -1;
			stop();
		}
		
		public void stop() {
			left = right = up = down = flinching = 
				dashing = jumping = attacking = upattacking = charging = firing = false;
		}
		
		private void getNextPosition() {
			
			if(knockback) {
				dy += fallSpeed * 2;
				if(!falling) knockback = false;
				return;
			}
			
			double maxSpeed = this.maxSpeed;
			if(dashing) maxSpeed *= 1.75;
			
			// movement
			if(left) {
				dx -= moveSpeed;
				if(dx < -maxSpeed) {
					dx = -maxSpeed;
				}
			}
			else if(right) {
				dx += moveSpeed;
				if(dx > maxSpeed) {
					dx = maxSpeed;
				}
			}
			else {
				if(dx > 0) {
					dx -= stopSpeed;
					if(dx < 0) {
						dx = 0;
					}
				}
				else if(dx < 0) {
					dx += stopSpeed;
					if(dx > 0) {
						dx = 0;
					}
				}
			}
			
			// cannot move while attacking, except in air
			if((attacking || upattacking || charging || firing) &&
				!(jumping || falling)) {
				dx = 0;
			}
			
			// charging
			if(charging) {
				chargingTick++;
				if(facingRight) dx = moveSpeed * (3 - chargingTick * 0.07);
				else dx = -moveSpeed * (3 - chargingTick * 0.07);
			}
			
			// jumping
			if(jumping && !falling) {
				//sfx.get("jump").play();
				dy = jumpStart;
				falling = true;
				SoundClip.play("jump");
			}
			
			if(doubleJump) {
				dy = doubleJumpStart;
				alreadyDoubleJump = true;
				doubleJump = false;
				SoundClip.play("jump");
				
			}
			
			if(!falling) alreadyDoubleJump = false;
			
			// falling
			if(falling) {
				dy += fallSpeed;
				if(dy < 0 && !jumping) dy += stopJumpSpeed;
				if(dy > maxFallSpeed) dy = maxFallSpeed;
			}
			
		}
		
		private void setAnimation(int i) {
			currentAction = i;
			animation.setFrames(sprites.get(currentAction));
			animation.setDelay(SPRITEDELAYS[currentAction]);
			width = FRAMEWIDTHS[currentAction];
			height = FRAMEHEIGHTS[currentAction];
		}
		
		public void update(GameContainer gc, float dt) {
			
			time++;
			fireCount++;
			
			// check teleporting
			if(teleporting) {
				
			}
			
			//check if can fire
			if(fireCount < fireDelay ) firing = false;
			
			// update position
			boolean isFalling = falling;
			getNextPosition();
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
			if(isFalling && !falling) {
				SoundClip.play("land");
			}
			if(dx == 0) x = (int)x;
			
			// check done flinching
			if(flinching) {
				flinchCount++;
				if(flinchCount > 120) {
					flinching = false;
				}
			}
			
			if(currentAction == FIREBALL) {
				if(animation.hasPlayedOnce()) firing = false;
			}
			
			// check attack finished
			if(currentAction == ATTACKING ||
				currentAction == UPATTACKING || currentAction == FIREBALL) {
				if(animation.hasPlayedOnce()) {
					attacking = false;
					upattacking = false;
					firing = false;
				}
			}
			if(currentAction == CHARGING) {
				if(animation.hasPlayed(5)) {
					charging = false;
				}
				cr.y = (int)y - 20;
				if(facingRight) cr.x = (int)x - 15;
				else cr.x = (int)x - 35;
			}
			
			// check enemy interaction
			for(int i = 0; i < enemies.size(); i++) {
				
				Enemy e = enemies.get(i);
				
				// check attack
				if(currentAction == ATTACKING &&
						animation.getFrame() == 3 && animation.getCount() == 0) {
					if(e.intersects(ar)) {
						e.hit(damage);
					}
				}
				
				// fireballs
				for(int j = 0; j < fireBalls.size(); j++) {
					if(fireBalls.get(j).intersects(e)) {
						e.hit(fireballdamage);
						fireBalls.get(j).setHit();
						break;
					}
				}
				
				// check upward attack
				if(currentAction == UPATTACKING &&
						animation.getFrame() == 3 && animation.getCount() == 0) {
					if(e.intersects(aur)) {
						e.hit(damage);
					}
				}
				
				// check charging attack
				if(currentAction == CHARGING) {
					if(animation.getCount() == 0) {
						if(e.intersects(cr)) {
							e.hit(chargeDamage);
						}
						/*if(e.intersects(this)) {
							e.hit(chargeDamage);
						}*/
					}
				}
				
				// collision with enemy
				if(!e.isDead() && intersects(e) && !charging) {
					hit(e.getDamage());
				}
				
				if(e.isDead()) {
					SoundClip.play("ekill");
				}
				
			}
			
			
			
			// set animation, ordered by priority
			if(teleporting) {
				if(currentAction != TELEPORTING) {
					setAnimation(TELEPORTING);
				}
			}
			else if(knockback) {
				if(currentAction != KNOCKBACK) {
					setAnimation(KNOCKBACK);
				}
			}
			else if(health == 0) {
				if(currentAction != DEAD) {
					setAnimation(DEAD);
					animation.setFrame(4);
					animation.setDelay(-1);
				}
			}
			else if(upattacking) {
				if(currentAction != UPATTACKING) {
					//JukeBox.play("playerattack");
					setAnimation(UPATTACKING);
					aur.x = (int)x - 15;
					aur.y = (int)y - 50;
				}
				else {
					if(animation.getFrame() == 4 && animation.getCount() == 0) {
						for(int c = 0; c < 3; c++) {
							
						}
					}
				}
			}
			else if(attacking) {
				if(currentAction != ATTACKING) {
					SoundClip.play("attack");
					setAnimation(ATTACKING);
					ar.y = (int)y - 6;
					if(facingRight) ar.x = (int)x + 30;
					else ar.x = (int)x - 30;
				}
				else {
					if(animation.getFrame() == 4 && animation.getCount() == 0) {
					for(int c = 0; c < 3; c++) {
						if(facingRight);
							/*energyParticles.add(
								new EnergyParticle(
									tileMap, 
									ar.x + ar.width - 4, 
									ar.y + ar.height / 2,
									EnergyParticle.RIGHT));*/
						/*else
							energyParticles.add(
								new EnergyParticle(
									tileMap,
									ar.x + 4,
									ar.y + ar.height / 2,
									EnergyParticle.LEFT));	*/
					}}
				}
			}
			else if(firing){
			if(currentAction != FIREBALL) {
				if(numFireballs < 1){
					firing = false;
					currentAction = IDLE;
					setAnimation(IDLE);
					return;
				}
				currentAction = FIREBALL;
				animation.setFrames(sprites.get(ATTACKING));
				animation.setDelay(4);
			}
		}
			
			else if(charging) {
				if(currentAction != CHARGING) {
					setAnimation(CHARGING);
				}
			}
			else if(dy < 0) {
				if(currentAction != JUMPING) {
					setAnimation(JUMPING);
				}
			}
			else if(dy > 0) {
				if(currentAction != FALLING) {
					setAnimation(FALLING);
				}
			}
			else if(dashing && (left || right)) {
				if(currentAction != DASHING) {
					setAnimation(DASHING);
				}
			}
			else if(left || right) {
				if(currentAction != WALKING) {
					setAnimation(WALKING);
				}
			}
			else if(currentAction != IDLE) {
				setAnimation(IDLE);
			}
			
			// fireball attack
			if(firing && animation.getFrame() >= 3) {
				if(fireCount >= fireDelay){
					FireBall fb = new FireBall(tileMap, facingRight);
					fb.setPosition(x + 20, y);
					fireBalls.add(fb);
					numFireballs--;
				}
				fireCount = 0;
			}
			
			// update fireballs
			for(int i = 0; i < fireBalls.size(); i++) {
				fireBalls.get(i).update(gc, dt);
				if(fireBalls.get(i).shouldRemove()) {
					fireBalls.remove(i);
					i--;
				}
			}
			
			animation.update();
			
			// set direction
			if(!attacking && !upattacking && !charging && !knockback) {
				if(right) facingRight = true;
				if(left) facingRight = false;
			}
			
			updateComponents(gc, dt);
			
		}
		
		public void render(GameContainer gc, Renderer r) {
			
			// draw emote
			if(emote == CONFUSED) {
				r.drawImage(confused, (int)(x + xmap -cwidth / 2)+8, (int)(y + ymap - 30));
			}
			else if(emote == SURPRISED) {
				r.drawImage(surprised, (int)(x + xmap -cwidth / 2)+8, (int)(y + ymap - 30));
			}
			
			
			// flinch
			if(flinching && !knockback) {
				if(flinchCount % 10 < 5) return;
			}
			
			// draw fireballs
			for(int i = 0; i < fireBalls.size(); i++) {
				fireBalls.get(i).renderComponents(gc, r);
			}
			
			
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