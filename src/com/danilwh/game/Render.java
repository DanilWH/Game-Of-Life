package com.danilwh.game;

import java.util.Random;

public class Render {
    private int width, height, MAP_WIDTH, MAP_HEIGHT;
    private int[] pixels;
    private boolean[][] tilesMasks;
    
    private int tileSize = 1;
    private static final Random random = new Random();
    
    public Render(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.MAP_WIDTH = width / this.tileSize;
        this.MAP_HEIGHT = height / this.tileSize;
        this.pixels = pixels;
        this.tilesMasks = new boolean[MAP_HEIGHT][MAP_WIDTH];
        this.fill();
    }
    
    private void fill() {
        /*** fill the array of tiles with random boolean values.
         * We don't fill the edge cells. That's why the following
         * cycles begin from 1 (not from 0) and keep doing iteration till the  ***/
        for (int y = 1; y < MAP_HEIGHT - 1; y++) {
        	for (int x = 1; x < MAP_WIDTH - 1; x++) {
        		this.tilesMasks[y][x] = random.nextBoolean();
        	}
        }
    }
    
    public void generation() {
    	boolean[][] tilesMasksClone = new boolean[MAP_HEIGHT][MAP_WIDTH];
    	
    	for (int y = 1; y < MAP_HEIGHT - 1; y++) {
    		for (int x = 1; x < MAP_WIDTH - 1; x++) {
    		    // get the number of alive neighborhoods around the current tile.
    		    int tilesAlive = this.getArounded(x, y);
    		    
    			if (this.tilesMasks[y][x])
    				tilesMasksClone[y][x] = (tilesAlive == 3 || tilesAlive == 2)? true : false;
    			else
    				tilesMasksClone[y][x] = (tilesAlive == 3)? true : false;
    		}
    	}
    	this.tilesMasks = tilesMasksClone.clone();
    }
    
    public int getArounded(int x, int y) {
    	int tilesAlive = 0;
    	
    	for (int yShift = -1; yShift <= 1; yShift++) {
    		for (int xShift = -1; xShift <= 1; xShift++) {
    			if (this.tilesMasks[y + yShift][x + xShift]) tilesAlive++;
    		}
    	}
    	
    	tilesAlive -= (this.tilesMasks[y][x])? 1 : 0;
    	
    	return tilesAlive;
    }
    
    public void clear() {
        /*** clears the screen. (fills the screen with black color. ***/
        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = 0;
        }
    }
    
    public void render() {
        /***
         * Draws each pixel of the screen according to a tile's color
         * that is stored in the array of tiles.
        ***/
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
            	
            	int yIndex = y / this.tileSize;
            	int xIndex = x / this.tileSize;
            	
            	boolean isOutOfArray = x >= MAP_WIDTH * this.tileSize || y >= MAP_HEIGHT * this.tileSize;
            	this.pixels[x + (y * this.width)] = (!isOutOfArray && this.tilesMasks[yIndex][xIndex])? 0xffffff : 0;
            }
        }
    }
}