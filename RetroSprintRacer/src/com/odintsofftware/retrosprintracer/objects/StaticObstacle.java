package com.odintsofftware.retrosprintracer.objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.odintsofftware.gameapi.graphics.GraphicsUtils;

public class StaticObstacle extends Sprite implements IColidable {
	
	private static final String DATA_OBSTACLES_PNG = "data/obstacles.png";
	private Rectangle boundingBoxSize;
	public static enum OBSTACLE_TYPE {
		ROAD_BLOCK,		
		ACCIDENT,
		HOLE
	}
	
	private float speed;	
	private Random random = new Random();	
	private StaticObstacle.OBSTACLE_TYPE type;
		
	private void loadObstacle() {
		switch (type) {
		case ROAD_BLOCK:
			set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 0, 0, 25, 12)); // Load the graphics
			boundingBoxSize = new Rectangle(0,0, 25, 12); // Set the actual bounding box size and offset relative to the sprite
			break;
			
		case ACCIDENT:
			set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 42, 1, 14, 31)); // Load the graphics
			//boundingBoxSize = new Rectangle(0,0, 9,29);
			boundingBoxSize = new Rectangle(0,0, 14,31);
			break;

		case HOLE:
			set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 61, 0, 20, 12)); // Load the graphics
			boundingBoxSize = new Rectangle(0,0, 20,12); 			
			break;					
		}
		setBounds(boundingBoxSize.x, boundingBoxSize.y, boundingBoxSize.width, boundingBoxSize.height);
	}

	@Override
	public boolean colidesWith(IColidable other) {
		if (getBoundingRectangle().overlaps(other.getBoundingBox())) 
			return true;
		else 
			return false;
	}

	@Override
	public Rectangle getBoundingBox() {		
		return 
			getBoundingRectangle();
	}
	
	public void move() {
		float nextY = getY() + speed;
		setY(nextY);		
	}

	public void generateNew(float speed) {
		int rnd = random.nextInt(10);
		
		if (rnd <= 6) {
			this.type = StaticObstacle.OBSTACLE_TYPE.ROAD_BLOCK;
		}
		else if (rnd == 7) {
			this.type = StaticObstacle.OBSTACLE_TYPE.ACCIDENT;
		}
		else if (rnd >= 8) {
			this.type = StaticObstacle.OBSTACLE_TYPE.HOLE;
		}
		
		this.speed = speed;
		
		loadObstacle();
		
	}

}
