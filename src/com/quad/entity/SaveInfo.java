package com.quad.entity;

public class SaveInfo {
	
	public static int lives = 3;
	public static int health = 5;
	public static int fireballs = 0;
	
	public static void init(){
		lives = 3;
		health = 5;
		fireballs = 0;
	}
	
	public static int getLives(){return lives;}
	public static void setLives(int i){lives = i;}
	
	public static int getHealth(){return health;}
	public static void setHealth(int i){health = i;}
	
	public static int getFireballs(){return fireballs;}
	public static void setFireballs(int i){fireballs = i;}

}
