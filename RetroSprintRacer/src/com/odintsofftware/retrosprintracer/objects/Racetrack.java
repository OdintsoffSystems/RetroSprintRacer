package com.odintsofftware.retrosprintracer.objects;

import java.util.Random;

import javax.swing.text.DefaultEditorKit.CutAction;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.odintsofftware.gameapi.graphics.TiledLayer;

public abstract class Racetrack extends TiledLayer {

	protected static int RACETRACK_COLUMNS = 9;
	protected static int RACETRACK_ROWS = 10;
	protected static int TILE_WIDTH = 44;
	protected static int TILE_HEIGHT = 44;
	
	protected int trackLeftBounds; 
	protected int trackLanes; // Min 2, max 5
	
	private float trackInitialSpeed;
	private float trackCurrentSpeed;
	private float trackIncrementSpeed;
	private float trackTopSpeed;	
		
	protected Random random = new Random();
	
	public Racetrack(String tileset, int trackLeftBounds, int trackLanes) {
		super(RACETRACK_COLUMNS, RACETRACK_ROWS, TILE_HEIGHT, TILE_WIDTH, new Texture(Gdx.files.internal(tileset)));
		
		this.fillCells(0, 0, RACETRACK_COLUMNS, RACETRACK_ROWS, 4);
		
		this.trackLeftBounds = trackLeftBounds;
		this.trackLanes = trackLanes;
		
		// TODO Generate track for custom number of lanes (trackLanes property)
		for (int x = 0; x < RACETRACK_ROWS; x++) {
			setCell(trackLeftBounds, x, 1); setCell(trackLeftBounds + 1, x, 2); setCell(trackLeftBounds + 2, x, 3);			
		}		
		
	}
	
	protected void generateBlock() {
		
		//Descend with current blocks
		for (int x = RACETRACK_COLUMNS - 1; x >= 0; x--) {
			for (int y = RACETRACK_ROWS - 1; y >= 1; y--) { // Rows stops in the second row
				setCell(x, y, getCell(x, y - 1));  
			}
		}
		
		createNextRow();

	}
	
	public abstract void createNextRow();
	
	public void update() {
		float trackPos = getY() + getTrackCurrentSpeed();	
		if (trackPos > -1) {
			generateBlock();
			trackPos = -TILE_HEIGHT; 
		}
		setPosition(getX(), trackPos);		
	}
	
	public float getSpeed() {
		return getTrackCurrentSpeed();
	}
	
	public void resetSpeed() {
		setTrackCurrentSpeed(getTrackInitialSpeed());
	}
	
	public void increaseSpeed() {
		if (getTrackCurrentSpeed() < getTrackTopSpeed())
			setTrackCurrentSpeed(getTrackCurrentSpeed() + getTrackIncrementSpeed()); 
	}


	protected float getTrackInitialSpeed() {
		return trackInitialSpeed;
	}

	
	protected void setTrackInitialSpeed(float trackInitialSpeed) {
		this.trackInitialSpeed = trackInitialSpeed;
	}

	protected float getTrackCurrentSpeed() {
		return trackCurrentSpeed;
	}

	protected void setTrackCurrentSpeed(float trackCurrentSpeed) {
		this.trackCurrentSpeed = trackCurrentSpeed;
	}

	protected float getTrackIncrementSpeed() {
		return trackIncrementSpeed;
	}

	protected void setTrackIncrementSpeed(float trackIncrementSpeed) {
		this.trackIncrementSpeed = trackIncrementSpeed;
	}

	protected float getTrackTopSpeed() {
		return trackTopSpeed;
	}

	public void setTrackTopSpeed(float trackTopSpeed) {
		this.trackTopSpeed = trackTopSpeed;
	}
	
}
