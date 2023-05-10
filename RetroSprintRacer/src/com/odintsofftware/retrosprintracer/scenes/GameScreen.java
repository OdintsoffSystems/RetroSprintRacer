package com.odintsofftware.retrosprintracer.scenes;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.odintsofftware.gameapi.graphics.GraphicsUtils;
import com.odintsofftware.retrosprintracer.RetroSprintRacer;
import com.odintsofftware.retrosprintracer.objects.Car;
import com.odintsofftware.retrosprintracer.objects.CarObstacle;
import com.odintsofftware.retrosprintracer.objects.RacetrackEasyGreens;


public class GameScreen implements Screen {

	private RetroSprintRacer game;
	
	// States of the game
	private enum GAME_STATE {
		HOW_TO_PLAY,
		GET_READY,
		PLAYING,
		GAME_OVER
	};
	
	private GAME_STATE currentState = GAME_STATE.GET_READY; 
	
	// Font
	private BitmapFont bigFont = new BitmapFont(Gdx.files.internal("data/retro.fnt"),
            Gdx.files.internal("data/retro_0.png"), true);
	
	private BitmapFont font = new BitmapFont(Gdx.files.internal("data/retro_small.fnt"),
            Gdx.files.internal("data/retro_small_0.png"), true);
	
	// Racetrack generator object
	private RacetrackEasyGreens racetrack = new RacetrackEasyGreens();
	
	// Car object
	private Car car;
	
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	// Dashboard images
	private Sprite dashboard;
	private Sprite dashLeft;
	private Sprite dashRight;
	private Sprite scoreArea;
	
	//List of obstacles 
	private ArrayList<CarObstacle> obstacles = new ArrayList<CarObstacle>();
	
	// Game Score
	private Preferences prefs = Gdx.app.getPreferences("odin-retrosprintracer");
	private int points = 0;
	// HighScore
	private int highScore;
    	
	// Direction control
	private boolean leftTouch = false;
	private boolean rightTouch = false;
	
	// Random for the obstacle creation
	// TODO: Place obstacle creation on Racetrack object? 
	private Random random = new Random();
	
	// How To Play overlay		
	private final int HOWTOPLAY_TIME = 300;
	private int howToPlayCooldown = HOWTOPLAY_TIME;
	
	//Get Ready overlay
	private final int GETREADY_TIME = 100;
	private int getReadyCooldown = GETREADY_TIME;
	private Sprite getReady;
	
	// Game Over overlay		
	private final int GAMEOVER_TIME = 50;
	private int gameOverCooldown = GAMEOVER_TIME;
	
	//Audio
	private Sound soundPoint;
	private Sound soundExplosion;
	private float soundPitch = 1.0f;
	
	//Statics
	private final int STREET_LEFT_CORNER;
	
	//Hard Mode flag
	private boolean hardMode = false;
	
	public GameScreen(RetroSprintRacer game, boolean hardMode) {
		this.game = game;
		
		if (hardMode) {
			this.hardMode = true;
			racetrack.setTrackTopSpeed(9.5f);
		}
		
		racetrack.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - racetrack.getWidth() / 2 , -44f);
		Gdx.graphics.setVSync(true);
		
		loadDashboard();
		
		loadGraphics();
		
		loadSounds();
						
		loadPlayerCar();		
		
		//Load Highscore
		highScore = prefs.getInteger("highscore", 0);		
		
		//Load if it's the user already played the game
		boolean alreadyPlayed = prefs.getBoolean("alreadyplayed");
		
		//Show "How To Play" if it's the user first time on the game 
		if (!alreadyPlayed) {
			currentState = GAME_STATE.HOW_TO_PLAY;
			prefs.putBoolean("alreadyplayed", true);
			prefs.flush();
		}		
				
		//Load first obstacles		
		obstacles.add(new CarObstacle(racetrack.getSpeed()));
		obstacles.add(new CarObstacle(racetrack.getSpeed()));
		obstacles.add(new CarObstacle(racetrack.getSpeed()));
		if (hardMode) obstacles.add(new CarObstacle(racetrack.getSpeed())); 
		STREET_LEFT_CORNER = (RetroSprintRacer.GAME_WIDTH / 2) - 66;		
		
		generateInitialObstacles();
	}

	/**
	 * 
	 */
	private void generateInitialObstacles() {
		//Position the first one		
		int lane = random.nextInt(5);
		obstacles.get(0).generateNew(racetrack.getSpeed());
		obstacles.get(0).setPosition(((22 - obstacles.get(0).getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacles.get(0).getHeight());
		
		//And the second
		lane = random.nextInt(5);
		obstacles.get(1).generateNew(racetrack.getSpeed());
		obstacles.get(1).setPosition(((22 - obstacles.get(1).getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacles.get(1).getHeight() - RetroSprintRacer.GAME_HEIGHT / 4);
		
		//And the third
		lane = random.nextInt(5);
		obstacles.get(2).generateNew(racetrack.getSpeed());
		obstacles.get(2).setPosition(((22 - obstacles.get(2).getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacles.get(2).getHeight() - RetroSprintRacer.GAME_HEIGHT / 2);

		//And the a fourth for hard mode
		if (hardMode) {
			lane = random.nextInt(5);
			obstacles.get(3).generateNew(racetrack.getSpeed());
			obstacles.get(3).setPosition(((22 - obstacles.get(3).getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacles.get(3).getHeight() - RetroSprintRacer.GAME_HEIGHT / 3);
		}
	}

	/**
	 * 
	 */
	private void loadPlayerCar() {
		car = new Car();
		car.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - car.getWidth() / 2, RetroSprintRacer.GAME_HEIGHT - 50 - car.getHeight() - dashboard.getHeight());
		car.flip(false, true);
	}

	/**
	 * 
	 */
	private void loadSounds() {
		soundPoint = Gdx.audio.newSound(Gdx.files.internal("data/passthrough.ogg"));
		soundExplosion = Gdx.audio.newSound(Gdx.files.internal("data/explosion.ogg"));
	}

	/**
	 * 
	 */
	private void loadGraphics() {
		scoreArea = GraphicsUtils.createSpriteByRegion("data/score2.png", 0, 0, 320, 82);
		scoreArea.setPosition(0, 0);		
		
		getReady = GraphicsUtils.createSpriteByRegion("data/ready.png", 0, 0, 193, 19);
		getReady.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - getReady.getWidth() / 2, RetroSprintRacer.GAME_HEIGHT / 2 - getReady.getHeight() / 2);
	}

	/**
	 * 
	 */
	private void loadDashboard() {
		dashboard = GraphicsUtils.createSpriteByRegion("data/dashboard.png", 0, 0, 320, 82);
		dashboard.setPosition(0, RetroSprintRacer.GAME_HEIGHT - dashboard.getHeight() - 30);
		
		dashLeft = GraphicsUtils.createSpriteByRegion("data/left.png", 0, 0, 45, 82);
		dashLeft.setPosition(0, RetroSprintRacer.GAME_HEIGHT - dashLeft.getHeight() - 30);
		
		dashRight = GraphicsUtils.createSpriteByRegion("data/right.png", 0, 0, 45, 82);
		dashRight.setPosition(RetroSprintRacer.GAME_WIDTH - dashRight.getWidth(), RetroSprintRacer.GAME_HEIGHT - dashRight.getHeight() - 30);
	}
	
	public void update(float delta) {
			
		leftTouch = false;
		rightTouch = false;
		
		switch (currentState) {
		case HOW_TO_PLAY:
			updateHowToPlay();
			break;
		case GET_READY:					
			updateGetReady();			
			break;
		
		case PLAYING:				
			updatePlaying();
			break;
			
		case GAME_OVER:			
			updateGameOver();			
			break;
		}								
	}

	private void updateHowToPlay() {
		howToPlayCooldown--;
		
		updateRacetrack();
		
		if (howToPlayCooldown == 0) {
			currentState = GAME_STATE.GET_READY;			
		}
	}
	
	private void updateGetReady() {
		getReadyCooldown--;
		
		updateRacetrack();
		
		if (getReadyCooldown == 0) {
			currentState = GAME_STATE.PLAYING;
			getReadyCooldown = GETREADY_TIME;
		}
	}

	private void updatePlaying() {

		updateRacetrack();
		
		for (int i = 0; i < 20; i++) { // 20 is max number of touch points
			if(Gdx.input.isTouched(i)) {
			     Vector3 touchPos = new Vector3();
			     touchPos.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
			     //camera.unproject(touchPos);
			     if (touchPos.x < Gdx.graphics.getWidth() / 2) {
			    	car.moveLeft();
			    	leftTouch = true;
			     }
			     else if (touchPos.x > Gdx.graphics.getWidth() / 2) {
			    	car.moveRight();
			    	rightTouch = true;
			     }
			}
		 }
		
		// check collision
		
		for (CarObstacle obstacle : obstacles) {
			if (car.colidesWith(obstacle))
			{				
				soundExplosion.play();
				currentState = GAME_STATE.GAME_OVER;
		
				// High-score check
				if (points > highScore) {
					prefs.putInteger("highscore", points);											
					prefs.flush();
				}
				
				if (game.myRequestHandler.getSignedInGPGS()) leaderboardsAndAchievements(points);
				
				return;
			}
				
			// check vanished obstacle and add points
			if (obstacle.getY() > RetroSprintRacer.GAME_HEIGHT - dashboard.getHeight() - 30)
			{
				points++;
				racetrack.increaseSpeed();
				if (soundPitch < 1.975f) soundPitch += 0.025f;
				long currentSound = soundPoint.play();
				soundPoint.setPitch(currentSound, soundPitch);				
				
				obstacle.generateNew(racetrack.getSpeed());			
				
				float distanceToOtherObstacle = 0;
				
				for (CarObstacle anotherObstacle : obstacles) {
					if (!obstacle.equals(anotherObstacle)) {
						if (Math.abs(obstacle.getY() - anotherObstacle.getY()) < 100)
						distanceToOtherObstacle = 150;
					}
				}
																			
				int lane = random.nextInt(5);				
				obstacle.setPosition(((22 - obstacle.getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacle.getHeight() - distanceToOtherObstacle);
				
				for (CarObstacle anotherObstacle : obstacles) {
					if (!obstacle.equals(anotherObstacle)) {
						if (obstacle.colidesWith(anotherObstacle)) {
							lane = random.nextInt(5);				
							obstacle.setPosition(((22 - obstacle.getWidth() / 2) + (22 * lane)) + STREET_LEFT_CORNER, - obstacle.getHeight() - distanceToOtherObstacle);
						}
					}
				}				
			}
		}
				
	}
	
	private void updateGameOver() {
		if (gameOverCooldown > 0) gameOverCooldown--;
		
		if (gameOverCooldown == 0) {
			if(Gdx.input.isTouched())
			{					
				if (points > highScore) highScore = points;
				racetrack.resetSpeed();
				soundPitch = 1.0f;
				points = 0;
				car.setPosition(RetroSprintRacer.GAME_WIDTH / 2 - car.getWidth() / 2, RetroSprintRacer.GAME_HEIGHT - 50 - car.getHeight() - dashboard.getHeight());
				currentState = GAME_STATE.GET_READY;
				gameOverCooldown = GAMEOVER_TIME;
				
				generateInitialObstacles();
			}				
		}
	}
	
	private void leaderboardsAndAchievements(int points) {

		game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQAw");
		
		if (!hardMode) {
			if (points >= 50) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQAQ"); //F
			if (points >= 80) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQAg"); //E
			if (points >= 100) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQBw"); //D
			if (points >= 120) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQBQ"); //C
			if (points >= 150) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQCA"); //B
			if (points >= 200) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQCg"); //A
			if (points >= 300) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQCw"); //S
		}
		else {
			if (points >= 50) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQDA"); //F
			if (points >= 80) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQDQ"); //E
			if (points >= 100) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQDg"); //D
			if (points >= 120) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQDw"); //C
			if (points >= 150) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQEA"); //B
			if (points >= 200) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQEQ"); //A
			if (points >= 300) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQEg"); //S
		}
		int crash = prefs.getInteger("timescrashed", 0);
		crash++;
		prefs.putInteger("timescrashed", crash);
		prefs.flush();
		
		if (crash >= 50) game.myRequestHandler.unlockAchievementGPGS("CgkI9q7yyIIVEAIQBA"); 
		
		if (hardMode) game.myRequestHandler.submitScoreGPGS(points, "CgkI9q7yyIIVEAIQEw"); // HARD
		else game.myRequestHandler.submitScoreGPGS(points, "CgkI9q7yyIIVEAIQAA"); // EASY
	}
	
	/**
	 * Check if racetrack needs to be animated
	 */
	private void updateRacetrack() {
	
		racetrack.update();	
		
		//lastMoveTime = System.currentTimeMillis();
	}
	
	@Override
	public void render(float delta) {
		
		update(delta);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		
		game.camera.update();
		shapeRenderer.setProjectionMatrix(game.camera.combined);
		game.batch.setProjectionMatrix(game.camera.combined);
		game.batch.enableBlending();
		game.batch.begin();		
			
			racetrack.draw(game.batch);		
			
			for (CarObstacle obstacle : obstacles) {				
				obstacle.draw(game.batch);		
			}			
			
			car.draw(game.batch);			
			dashboard.draw(game.batch);								
			scoreArea.draw(game.batch);			
	
			switch (currentState) {
				case HOW_TO_PLAY:
					renderHowToPlay();
					break;
			
				case PLAYING:
					for (CarObstacle obstacle : obstacles) {
						obstacle.move();	
					}	

					if (leftTouch) dashLeft.draw(game.batch);
					if (rightTouch) dashRight.draw(game.batch);
					
					break;
				case GAME_OVER:
					renderGameOver();
					break;
					
				case GET_READY:
					getReady.draw(game.batch);
	
				default:					
					break;
			}
			
			
			renderScore();					
						
		game.batch.end();				
	
	}

	private void renderHowToPlay() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();
		shapeRenderer.setColor(0, 0, 0, 0.8f);
		shapeRenderer.rect(0, 0, RetroSprintRacer.GAME_WIDTH, RetroSprintRacer.GAME_HEIGHT);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.identity();
		shapeRenderer.setColor(1, 1, 1, 1);		
		shapeRenderer.line(RetroSprintRacer.GAME_WIDTH / 2, 0, RetroSprintRacer.GAME_WIDTH / 2, RetroSprintRacer.GAME_HEIGHT);
		shapeRenderer.end();
		
		bigFont.draw(game.batch, "How to Play", (RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("How to Play").width / 2) - 20, RetroSprintRacer.GAME_HEIGHT / 2 - font.getXHeight());
		
		font.draw(game.batch, "<- Press left side", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("<- Press left side").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() + 5);
		font.draw(game.batch, "to move left!", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("to move left!").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() * 2 + 7);
		
		font.draw(game.batch, "Press right side ->", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("Press right side ->").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() * 3 + 25);
		font.draw(game.batch, "to move right!", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("to move right!").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() * 4 + 27);
	}

	/**
	 * Render for the score (all game states)
	 */
	private void renderScore() {
		font.draw(game.batch, "Points", 5, 5);
		font.draw(game.batch, String.valueOf(points), 5, 30);
		
		font.draw(game.batch, "High Score", RetroSprintRacer.GAME_WIDTH - font.getBounds("High Score").width - 5, 5);
		font.draw(game.batch, String.valueOf(highScore), RetroSprintRacer.GAME_WIDTH - font.getBounds(String.valueOf(highScore)).width - 5, 30);
	}

	/**
	 * Render for GAME_OVER state
	 */
	private void renderGameOver() {
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.identity();
		shapeRenderer.setColor(0, 0, 0, 0.8f);
		shapeRenderer.rect(0, 0, RetroSprintRacer.GAME_WIDTH, RetroSprintRacer.GAME_HEIGHT);
		shapeRenderer.end();

		bigFont.draw(game.batch, "Game Over", (RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("Game Over").width / 2) - 20, RetroSprintRacer.GAME_HEIGHT / 2 - font.getXHeight());
		
		if (points > highScore) {
			font.draw(game.batch, "Congratulations!", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("Congratulations!").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() + 5);
			font.draw(game.batch, "New HighScore!", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("New HighScore!").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + font.getXHeight() + 25);
		}
		
		if (gameOverCooldown == 0) font.draw(game.batch, "TAP TO TRY AGAIN!", RetroSprintRacer.GAME_WIDTH / 2 - font.getBounds("TAP TO TRY AGAIN!").width / 2, RetroSprintRacer.GAME_HEIGHT / 2 + 100);
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
