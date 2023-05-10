package com.odintsofftware.retrosprintracer;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main implements IActivityRequestHandler {
	private static Main application;
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "RetroSprintRacer";
		cfg.useGL20 = false;
		cfg.width = 320;
		cfg.height = 480;
		
		if (application == null) {
			application = new Main();
		}
		
		new LwjglApplication(new RetroSprintRacer(application), cfg);
	}

}
