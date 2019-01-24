package com.quad.core.fx;

public class Content {

	//Bat
	public static Image[][] DarkBat = load("/Sprites/Enemies/DarkBat.gif", 25, 25);
	//Robot
	public static Image[][] Robot = load("/Sprites/Enemies/Robot.gif", 15, 28);
	//Dark Energy Attack
	public static Image[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);
	//Robot Energy Attack
	public static Image[][] RobotShot = load("/Sprites/Enemies/RobotShot.png", 13, 13);
	//Golem
	public static Image[][] Golem = load("/Sprites/Enemies/Golem.gif", 64, 70);
	//Beetle
	public static Image[][] Beetle = load("/Sprites/Enemies/Beetle.gif",40,19);
	//andro boss
	public static Image[][] AndroBoss = load("/Sprites/Enemies/boss.png",57,88);
	//healthbar
	public static Image[][] HealthBar = load("/Hud/HealthBar.gif",94,20);
	//health item
	public static Image[][] Heart = load("/Items/Heart.gif",16,32);
	//sparkle pickup item
	public static Image[][] Sparkle = load("/Items/sparkle.gif",16,16);
	//Fireball
	public static Image[][] Fire = load("/Items/FireBall.gif",17,32);
	//green smoke
	public static Image[][] Smoke = load("/Sprites/Enemies/Explosion.gif",30,30);
	//grey smoke
	public static Image[][] Smoke1 = load("/Sprites/Enemies/Smoke.png",16,16);
	
	public static Image[][] load(String s, int w, int h) {
		Image[][] ret;
		try {
			Image spritesheet = new Image(s);
			int width = spritesheet.width / w;
			int height = spritesheet.height / h;
			ret = new Image[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}

}
