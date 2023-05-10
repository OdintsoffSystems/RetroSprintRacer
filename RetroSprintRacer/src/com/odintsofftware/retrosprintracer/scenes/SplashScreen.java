package com.odintsofftware.retrosprintracer.scenes;

import com.badlogic.gdx.graphics.Color;
import com.odintsofftware.gameapi.graphics.ImageFadeScreen;
import com.odintsofftware.retrosprintracer.RetroSprintRacer;

public class SplashScreen extends ImageFadeScreen {
	
	private RetroSprintRacer game;
	
	public SplashScreen(RetroSprintRacer game) {
		super("data/menu.png", 0, 122, 134, 134, new Color(0,0,0,1), 1000, 20, game.batch, game.camera);
		this.game = game;		
	}
	
	@Override
	public void resize(int width, int height) {
		game.camera.setToOrtho(false, RetroSprintRacer.GAME_WIDTH, RetroSprintRacer.GAME_HEIGHT);			
	}

	@Override
	public void finishScene() {
		game.setScreen(new MenuScreen(game));
	}

}
