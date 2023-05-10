package com.odintsofftware.gameapi.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.odintsofftware.retrosprintracer.RetroSprintRacer;

public abstract class ImageFadeScreen implements Screen {

	private long pause;
    private float alpha = 0.00f;
    private boolean fadeIn = true;
    private boolean slept = false;

    private float steps = 0;
    private int maxSteps = 100;
    private Color bgColor;
    private Sprite image;
    private SpriteBatch sb;
    private OrthographicCamera camera;
    
    
    public ImageFadeScreen(String imagePath, int originX, int originY, int width, int height, Color bgColor, long pause, SpriteBatch sb, OrthographicCamera camera) {           	
    	image = GraphicsUtils.createSpriteByRegion(imagePath, originX, originY, width, height);
		
        this.pause = pause;
        this.bgColor = bgColor;
        this.sb = sb;
        this.camera = camera;
        
        float x = RetroSprintRacer.GAME_WIDTH / 2 - image.getWidth() /2; 
        float y = RetroSprintRacer.GAME_HEIGHT / 2 - image.getHeight() /2;   
        
        image.setPosition(x, y);                            
    }
    
    public ImageFadeScreen(String imagePath, int originX, int originY, int width, int height, Color bgColor, long pause, int maxSteps, SpriteBatch sb, OrthographicCamera camera) {       
    	image = GraphicsUtils.createSpriteByRegion(imagePath, originX, originY, width, height);
		
        this.pause = pause;
        this.bgColor = bgColor;
        this.maxSteps = maxSteps;
        this.sb = sb;
        this.camera = camera;
        
        float x = RetroSprintRacer.GAME_WIDTH / 2 - image.getWidth() /2; 
        float y = RetroSprintRacer.GAME_HEIGHT / 2 - image.getHeight() /2;   
        
        image.setPosition(x, y);    
    }
    
	@Override
	public void render(float delta) {

        if (fadeIn) {
            if (alpha < 1f) { alpha = ++steps / maxSteps; }
            else if (alpha > 0.90f) { alpha = 1.0f; fadeIn = false;}
        }
        else {            

            if (!slept) {
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException ex) {
                   //Catch
                }
                finally {
                    slept = true;
                }
            }
            else {
                if (alpha > 0f) { alpha = --steps / maxSteps; }
                else if (alpha < 0.01f) { finishScene(); }
            }
        }
        
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glEnable(GL10.GL_BLEND);
		
		camera.update();
		sb.setProjectionMatrix(camera.combined);
		sb.enableBlending();
		sb.begin();		
			sb.enableBlending();
			Color color = image.getColor();						
			image.setColor(color.r, color.g, color.b, alpha);
			image.draw(sb);
		sb.end();
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


	public abstract void finishScene();
		

}
