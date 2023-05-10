package com.odintsofftware.retrosprintracer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.odintsofftware.retrosprintracer.scenes.SplashScreen;

public class RetroSprintRacer extends Game {
	
	public OrthographicCamera camera;
	public SpriteBatch batch;
	public static final int GAME_WIDTH = 320;
	public static final int GAME_HEIGHT = 480;
	
	public IActivityRequestHandler myRequestHandler;
	
	public RetroSprintRacer(IActivityRequestHandler handler) {
        super();
		myRequestHandler = handler;		
    }
	 
	@Override
	public void create() {		
		
		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		
		camera.setToOrtho(true, GAME_WIDTH, GAME_HEIGHT);	
		
		setScreen(new SplashScreen(this));
		myRequestHandler.showAds(true);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
