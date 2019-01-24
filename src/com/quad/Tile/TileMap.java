package com.quad.Tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Image;
import com.quad.core.fx.ShadowType;


public class TileMap {
	
	// position
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private Image tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	private GameContainer gc;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	// effects
	private boolean shaking;
	private int intensity;
	
	public TileMap(GameContainer gc, int tileSize) {
		this.tileSize = tileSize;
		this.gc = gc;
		numRowsToDraw = gc.getHeight() / tileSize + 2;
		numColsToDraw = gc.getWidth() / tileSize + 2;
		tween = 0.7;
	}
	
	public void loadTiles(String s) {
		
		try {

			tileset = new Image(s);
			numTilesAcross = tileset.getImage().getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			
			
			Image subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
							col * tileSize,
							0,
							tileSize,
							tileSize
						);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
							col * tileSize,
							tileSize,
							tileSize,
							tileSize
						);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
				tiles[1][col].getImage().shadowType = ShadowType.FADE;
			}
			
			//list of all tiles that arnt collidable that need shadows
			for(int i = 1; i <= 16; i++){
				tiles[0][i].getImage().shadowType = ShadowType.FADE;
				
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
						new InputStreamReader(in)
					);
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = gc.getWidth() - width;
			xmax = 0;
			ymin = gc.getHeight() - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public double getx() { return x; }
	public double gety() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public int getType(int row, int col) {
		int rc = map[row][col];
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		return tiles[r][c].getType();
	}
	public boolean isShaking() { return shaking; }
	
	public void setTween(double d) { tween = d; }
	public void setShaking(boolean b, int i) { shaking = b; intensity = i; }
	public void setBounds(int i1, int i2, int i3, int i4) {
		xmin = gc.getHeight() - i1;
		ymin = gc.getWidth() - i2;
		xmax = i3;
		ymax = i4;
	}
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
		
	}
	
	public void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void update() {
		if(shaking) {
			this.x += Math.random() * intensity - intensity / 2;
			this.y += Math.random() * intensity - intensity / 2;
		}
	}
	
	public void draw(Renderer g) {
		
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
		
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize
				);
				
				
				
			}
			
		}
		
	}
	
}