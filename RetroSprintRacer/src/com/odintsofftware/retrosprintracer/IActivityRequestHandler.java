package com.odintsofftware.retrosprintracer;

public interface IActivityRequestHandler {
	public void showAds(boolean show);
	
	public boolean getSignedInGPGS();
	public void loginGPGS();
	public void submitScoreGPGS(int score, String leaderboardId);
	public void unlockAchievementGPGS(String achievementId);
	public void getLeaderboardGPGS(String leaderboardID);
	public void getAchievementsGPGS();
	
}
