package com.odintsofftware.gameapi.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsUtils {

	public static Sprite createSprite(String imagePath) {
		Texture texture = new Texture(Gdx.files.internal(imagePath));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
		
		//region.flip(false, true);
		
		return new Sprite(region);
	}
	
	public static Sprite createSpriteByRegion(String imagePath, int x, int y, int width, int height) {
		Texture texture = new Texture(Gdx.files.internal(imagePath));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		TextureRegion region = new TextureRegion(texture, x, y, width, height);
		
		region.flip(false, true);
	
		return new Sprite(region);
	}
	
	
}
