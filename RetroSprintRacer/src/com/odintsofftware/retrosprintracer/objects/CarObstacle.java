package com.odintsofftware.retrosprintracer.objects;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.odintsofftware.gameapi.graphics.GraphicsUtils;

public class CarObstacle extends Sprite implements IColidable {
	
	private static final String DATA_OBSTACLES_PNG = "data/car.png";
	private Rectangle boundingBoxSize;
	public static enum CAR_TYPE {
		RED,		
		POLICE,
		GREEN,
		AMBULANCE,
		PURPLE,
		GRAY,
		DELIVERY
	}
	
	private float speed;	
	private Random random = new Random();	
	private CarObstacle.CAR_TYPE type;
	
	public CarObstacle() {
	
	}
	
	public CarObstacle(float speed) {
		generateNew(speed);
	}
	
	public void generateNew(float speed) {
		int rnd = random.nextInt(10);
		// 20%
		if (rnd <= 1) {
			this.type = CarObstacle.CAR_TYPE.RED;			
		}
		// 20%
		else if (rnd >= 2 && rnd <= 3) {
			this.type = CarObstacle.CAR_TYPE.GREEN;
		}
		// 10%
		else if (rnd == 4) {
			this.type = CarObstacle.CAR_TYPE.PURPLE;
		}
		// 20%
		else if (rnd >= 5 && rnd <= 6) {
			this.type = CarObstacle.CAR_TYPE.GRAY;
		}
		// 10%
		else if (rnd == 7) {
				this.type = CarObstacle.CAR_TYPE.AMBULANCE;
		}
		// 10%
		else if (rnd == 8) {
				this.type = CarObstacle.CAR_TYPE.DELIVERY;
		}
		// 10%
		else if (rnd == 9) {
			this.type = CarObstacle.CAR_TYPE.POLICE;
		}
		
		this.speed = speed;
		
		loadObstacle();
		
	}
	
	private void loadObstacle() {
		switch (type) {
		
			case RED:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 18, 1, 18, 30)); // Load the graphics
				boundingBoxSize = new Rectangle(0, 0, 18, 30);
				setColisionBoundingBox(new Rectangle(0, 0, 13, 30));				
				speed -= 1.4f;
				break;
				
			case POLICE:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 36, 0, 18, 33)); // Load the graphics
				setColisionBoundingBox(new Rectangle(0, 0, 13, 33));
				speed -= 1.2f;
				break;
				
			case GREEN:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 54, 1, 18, 30)); // Load the graphics				
				setColisionBoundingBox(new Rectangle(0,0, 13,30));
				speed -= 1.6f;
				break;
				
			case PURPLE:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 93, 0, 18, 33)); // Load the graphics				
				setColisionBoundingBox(new Rectangle(0,0, 13,33));
				speed -= 2.5f;
				break;
			
			case GRAY:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 0, 34, 18, 30)); // Load the graphics				
				setColisionBoundingBox(new Rectangle(0,0, 13,30));
				speed -= 1.8f;
				break;
				
			case AMBULANCE:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 72, 1, 21, 30)); // Load the graphics
				setColisionBoundingBox(new Rectangle(0,0, 14,30));
				speed -= 1f;
				break;
				
			case DELIVERY:
				set(GraphicsUtils.createSpriteByRegion(DATA_OBSTACLES_PNG, 18, 34, 21, 30)); // Load the graphics
				setColisionBoundingBox(new Rectangle(0,0, 14,30));
				speed -= 1.5f;
				break;	
		}
		//setBounds(boundingBoxSize.x, boundingBoxSize.y, boundingBoxSize.width, boundingBoxSize.height);
	}

	@Override
	public boolean colidesWith(IColidable other) {
		if (getBoundingBox().overlaps(other.getBoundingBox())) 
			return true;
		else 
			return false;
	}

	private void setColisionBoundingBox(Rectangle boundingBox) {
		this.boundingBoxSize = boundingBox;
	}
	
	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(this.getX() + boundingBoxSize.x, this.getY() + boundingBoxSize.y, 
							 boundingBoxSize.width, boundingBoxSize.height);
	}
	
	public void move() {
		float nextY = getY() + speed;
		setY(nextY);		
	}

}
