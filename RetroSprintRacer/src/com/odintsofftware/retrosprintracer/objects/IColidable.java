package com.odintsofftware.retrosprintracer.objects;

import com.badlogic.gdx.math.Rectangle;

public interface IColidable {
	
	public boolean colidesWith(IColidable other);
	
	public Rectangle getBoundingBox();
	
}
