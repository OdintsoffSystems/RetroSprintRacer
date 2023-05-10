package com.odintsofftware.retrosprintracer.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Car extends Sprite
				 implements IColidable
					{
	
	private final static float SIDE_SPEED = 4f;
	private Rectangle boundingBoxSize;
	
	public Car() {
		super(new Texture("data/car.png"), 18, 33);
		setColisionBoundingBox(new Rectangle(0,0,13,33));
		//super(new Texture("data/car.png"), 12, 23);
		//flip(false, true);			
	}

	@Override
	public boolean colidesWith(IColidable other) {		
		if (getBoundingBox().overlaps(other.getBoundingBox())) return true;
		else return false;
	}


	private void setColisionBoundingBox(Rectangle boundingBox) {
		this.boundingBoxSize = boundingBox;
	}
	
	@Override
	public Rectangle getBoundingBox() {		
		return new Rectangle(this.getX() + boundingBoxSize.x, this.getY() + boundingBoxSize.y, 
				 boundingBoxSize.width, boundingBoxSize.height);
	}
	
	public void moveLeft() {		
		if (getX() > 100)
		setX(getX() - SIDE_SPEED);
	}
	
	public void moveRight() {
		if (getX() < 204)
		setX(getX() + SIDE_SPEED);
	}
	
}
