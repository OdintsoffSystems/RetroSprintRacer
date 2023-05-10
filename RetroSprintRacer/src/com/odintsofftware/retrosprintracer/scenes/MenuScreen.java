package com.odintsofftware.retrosprintracer.scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.odintsofftware.gameapi.graphics.GraphicsUtils;
import com.odintsofftware.retrosprintracer.RetroSprintRacer;


public class MenuScreen implements Screen {

	private RetroSprintRacer game;
	private Sprite gameLogo;
	private Sprite odinLogo;
	private Sprite copySprite;
	private Sprite creditsSprite; 

	private Rectangle boundingBoxPlay;
	private Rectangle boundingBoxLeaderboards;
	private Rectangle boundingBoxConnect;
	private Rectangle boundingBoxNormalMode;
	private Rectangle boundingBoxHardMode;
	private Rectangle boundingBoxAchievements;
	private Rectangle boundingBoxGameLogo;
	
	private boolean modeSelect = false;
	private boolean showScore = false;
	private boolean showCredits = false;
	
	private Sound soundSelect = Gdx.audio.newSound(Gdx.files.internal("data/point.ogg"));
	
	private BitmapFont bigFont = new BitmapFont(Gdx.files.internal("data/retro.fnt"),
            Gdx.files.internal("data/retro_0.png"), true);
	
	public MenuScreen(RetroSprintRacer game) {
		this.game = game;
		gameLogo = GraphicsUtils.createSpriteByRegion("data/menu.png", 0, 0, 152, 72);
		odinLogo = GraphicsUtils.createSpriteByRegion("data/menu.png", 0, 82, 130, 29);
		copySprite = GraphicsUtils.createSpriteByRegion("data/menu.png", 0, 112, 177, 8);
		creditsSprite = GraphicsUtils.createSpriteByRegion("data/credits.png", 0, 0, 320, 480);
		
		gameLogo.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - gameLogo.getWidth() / 2, RetroSprintRacer.GAME_HEIGHT / 2 - gameLogo.getHeight());
		odinLogo.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - odinLogo.getWidth() / 2, 50);	
		copySprite.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - copySprite.getWidth() /2, gameLogo.getY() + gameLogo.getHeight() + copySprite.getHeight());
		creditsSprite.setPosition(0, 0);
			
		boundingBoxPlay = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("PLAY!").width / 2,
										copySprite.getY() + 80, 
										bigFont.getBounds("PLAY!").width, 
										bigFont.getXHeight());
		
		boundingBoxLeaderboards = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("Leaderboards").width / 2 ,
												boundingBoxPlay.getY() + bigFont.getXHeight() + 20, 
												bigFont.getBounds("Leaderboards").width, 
												bigFont.getXHeight());
		
		boundingBoxConnect = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("Log-in GPGS").width / 2 ,
											boundingBoxPlay.getY() + bigFont.getXHeight() + 20, 
											bigFont.getBounds("Log-in GPGS").width, 
											bigFont.getXHeight());
		
		boundingBoxNormalMode = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("Normal Mode").width / 2,
												copySprite.getY() + 80, 
												bigFont.getBounds("Normal Mode").width, 
												bigFont.getXHeight());
		
		boundingBoxHardMode = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("Hard Mode").width / 2 ,
											boundingBoxPlay.getY() + bigFont.getXHeight() + 20, 
											bigFont.getBounds("HARD MODE").width, 
											bigFont.getXHeight());
		
		boundingBoxAchievements = new Rectangle(RetroSprintRacer.GAME_WIDTH /2 - bigFont.getBounds("Achievements").width / 2 ,
											boundingBoxHardMode.getY() + bigFont.getXHeight() + 20, 
											bigFont.getBounds("Achievements").width, 
											bigFont.getXHeight());
		
		boundingBoxGameLogo = new Rectangle(gameLogo.getX(), gameLogo.getY(), gameLogo.getWidth(), gameLogo.getHeight());
	}
	
	public void update() {

		if(Gdx.input.justTouched()) {
			
			if (showCredits) {
				soundSelect.play();
				showCredits = false;				
				return;
			}
					
			Vector3 touchPos = new Vector3();
		    touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		    game.camera.unproject(touchPos);

		    if (modeSelect) {
		    	// MODE SELECT FOR LEADERBOARDS
		    	if (showScore) {
		    		if (boundingBoxNormalMode.contains(touchPos.x, touchPos.y)) {
				    	soundSelect.play();
				    	game.myRequestHandler.getLeaderboardGPGS("CgkI9q7yyIIVEAIQAA"); // EASY
				    	modeSelect = false;
				    	showScore = false;
			    	}
			    	if (boundingBoxHardMode.contains(touchPos.x, touchPos.y)) {
				    	soundSelect.play();
				    	game.myRequestHandler.getLeaderboardGPGS("CgkI9q7yyIIVEAIQEw"); // HARD
			    		modeSelect = false;
				    	showScore = false;
			    	}
		    	}
		    	// MODE SELECT FOR NEW GAME
		    	else {		    	
			    	if (boundingBoxNormalMode.contains(touchPos.x, touchPos.y)) {
				    	soundSelect.play();
			    		game.setScreen(new GameScreen(game, false));			    	
			    	}
			    	if (boundingBoxHardMode.contains(touchPos.x, touchPos.y)) {
				    	soundSelect.play();
			    		game.setScreen(new GameScreen(game, true));			    	
			    	}
		    	}
		    }
		    
		    else {
		    // PLAY
		    	
		    	if (boundingBoxGameLogo.contains(touchPos.x, touchPos.y)){
		    		soundSelect.play();
		    		showCredits = true;
		    	}
			    if (boundingBoxPlay.contains(touchPos.x, touchPos.y)) {
			    	soundSelect.play();
			    	modeSelect = true;
			    }
		    
			    // LEADERBOARDS
			    if (game.myRequestHandler.getSignedInGPGS()) {
			    	if (boundingBoxLeaderboards.contains(touchPos.x, touchPos.y)) {
			    		soundSelect.play();
				    	if (game.myRequestHandler.getSignedInGPGS()) { 
				    		soundSelect.play();
				    		showScore = true;
				    		modeSelect = true;
				    	}
				    	else game.myRequestHandler.loginGPGS();
				    }
			    	if (boundingBoxAchievements.contains(touchPos.x, touchPos.y)) {
			    		soundSelect.play();
				    	if (game.myRequestHandler.getSignedInGPGS()) game.myRequestHandler.getAchievementsGPGS();
				    	else game.myRequestHandler.loginGPGS();
				    }
			    }
			    else {
			    	if (boundingBoxConnect.contains(touchPos.x, touchPos.y)) {
			    		soundSelect.play();
				    	game.myRequestHandler.loginGPGS();
			    	}
			    }
		    }		    
		}
	}
	
	@Override
	public void render(float delta) {
		
		update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		
		game.camera.update();
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.enableBlending();
		game.batch.begin();		
		
			odinLogo.draw(game.batch);
			gameLogo.draw(game.batch);
			copySprite.draw(game.batch);

			if (modeSelect) {
				bigFont.draw(game.batch, "Normal Mode", boundingBoxNormalMode.x, boundingBoxNormalMode.y);
				bigFont.draw(game.batch, "Hard Mode", boundingBoxHardMode.x, boundingBoxHardMode.y);
			}
			else if (showCredits) {
				creditsSprite.draw(game.batch);
			}
			else {
			bigFont.draw(game.batch, "PLAY!", boundingBoxPlay.x, boundingBoxPlay.y);
				if (game.myRequestHandler.getSignedInGPGS()) {
					bigFont.draw(game.batch, "LEADERBOARDS", boundingBoxLeaderboards.x, boundingBoxLeaderboards.y);
					bigFont.draw(game.batch, "Achievements", boundingBoxAchievements.x, boundingBoxAchievements.y);
				}
				else
					bigFont.draw(game.batch, "Log-in GPGS", boundingBoxConnect.x, boundingBoxConnect.y);
			}
		game.batch.end();				
	}

	@Override
	public void resize(int width, int height) {
		game.camera.setToOrtho(false, RetroSprintRacer.GAME_WIDTH, RetroSprintRacer.GAME_HEIGHT);	
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
