package com.odintsofftware.retrosprintracer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
//import com.swarmconnect.Swarm;

public class MainActivity extends AndroidApplication implements GameHelperListener, IActivityRequestHandler  {
	
	protected AdView adView;
	
	private GameHelper gameHelper; //GPGS

    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;

    protected Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SHOW_ADS:
                {
                    adView.setVisibility(View.VISIBLE);
                    adView.setBackgroundColor(Color.BLACK);
                    break;
                }
                case HIDE_ADS:
                {
                    adView.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };
    
    public MainActivity() {

    }
    
    @Override
    public void onStart() {
    	super.onStart();   			
    	gameHelper.onStart(this);
    }
    
    public void onStop() {
    	super.onStop();
    	gameHelper.onStop();
    }
    
    @Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);
		gameHelper.onActivityResult(request, response, data);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        
//        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
//        cfg.useGL20 = false;
//                        
//        initialize(new RetroSprintRacer(), cfg);
    	super.onCreate(savedInstanceState);    	

        // Create the layout
        RelativeLayout layout = new RelativeLayout(this);

        // Do the stuff that initialize() would do for you
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        // Create the libgdx View
        View gameView = initializeForView(new RetroSprintRacer(this), false);

        // Create and setup the AdMob view
        adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1406949030334243/3731575557");
        adView.setAdSize(AdSize.BANNER);

        // Iniciar uma solicitação genérica.
        AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice("D89CAC0A2DE773F108B459A5C253A2C6")
        .build();

        // Carregar o adView com a solicitação de anúncio.
        adView.loadAd(adRequest);
                       
        // Add the libgdx view
        layout.addView(gameView);

        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = 
            new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        layout.addView(adView, adParams);

        // Hook it all up
        setContentView(layout);            
        // Add this method call
        //Swarm.setActive(this);
    	if (gameHelper == null) {
        	gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);    		
    		gameHelper.enableDebugLog(true);
    	}
    	gameHelper.setup(this);
    }

    @Override
    public void onResume() {
    	super.onResume();
    	//Swarm.setActive(this);
    	    
	    // Replace MY_APP_ID with your App ID from the Swarm Admin Panel
	    // Replace MY_APP_KEY with your string App Key from the Swarm Admin Panel
	    //Swarm.init(this, 13153, "73ee4ec3cf5c2c3317afc5823f5154b8");
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	//Swarm.setInactive(this);
    }
    
    // AD MOB
    // This is the callback that posts a message for the handler
    @Override
    public void showAds(boolean show) {
       handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }

    // GPGS
    @Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable(){
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score, String leaderboardId) {
		Games.Leaderboards.submitScore(gameHelper.getApiClient(), leaderboardId, score);
	}
	
	@Override
	public void unlockAchievementGPGS(String achievementId) {
	  Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}
	
	@Override
	public void getLeaderboardGPGS(String leaderboardId) {
	  if (gameHelper.isSignedIn()) {
	    startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), leaderboardId), 100);
	  }
	  else if (!gameHelper.isConnecting()) {
	    loginGPGS();
	  }
	}

	@Override
	public void getAchievementsGPGS() {
	  if (gameHelper.isSignedIn()) {
	    startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()), 101);
	  }
	  else if (!gameHelper.isConnecting()) {
	    loginGPGS();
	  }
	}
	
	@Override
	public void onSignInFailed() {
	}

	@Override
	public void onSignInSucceeded() {
	}
}