package com.odintsofftware.retrosprintracer.objects;


public class RacetrackEasyGreens 
	extends Racetrack {

	private static final int TRACK_LEFT_BOUNDS = 3;   
	private static final int TRACK_LANES = 3;
	private static final int BILLBOARD_COOLDOWN = 11;
	private static final int SPEEDLIMIT_COOLDOWN = 30;
	private int currentBillboardCooldown = BILLBOARD_COOLDOWN;
	private int currentSpeedLimitCooldown = SPEEDLIMIT_COOLDOWN;
		
		
		public RacetrackEasyGreens() {
			super("data/tileset.png", TRACK_LEFT_BOUNDS, TRACK_LANES);			
			setTrackInitialSpeed(5f); //4
			setTrackTopSpeed(8.5f); //9
			setTrackIncrementSpeed(0.1f); //0.1
			setTrackCurrentSpeed(getTrackInitialSpeed());			
		}
				
		@Override
		public void createNextRow() {			
			fillCells(0, 0, RACETRACK_COLUMNS, 1, 4);
			setCell(trackLeftBounds, 0, 1); setCell(trackLeftBounds + 1, 0, 2); setCell(trackLeftBounds + 2, 0, 3);
			
			//tree to the left
			int randomScenery = random.nextInt(2);
			
			if (randomScenery == 1) {
				randomScenery = random.nextInt(2);
				
				if (randomScenery == 1)
					setCell(1, 0, 10);
				else 
					setCell(1, 0, 11);
			}
			// If no tree, random bush
			else {
				randomScenery = random.nextInt(5);
				
				if (randomScenery < 3)
					setCell(1,0, 9);
			}
				
			
			//tree to the right
			randomScenery = random.nextInt(2);
			
			if (randomScenery == 1) {
				randomScenery = random.nextInt(2);
			
			if (randomScenery == 1)
				setCell(7, 0, 10);
			else 
				setCell(7, 0, 11);
			}
			// If no tree, random bush
			else {
				randomScenery = random.nextInt(5);
				
				if (randomScenery < 3)
					setCell(1,0, 9);
			}
			
			
			currentBillboardCooldown--;
			currentSpeedLimitCooldown--;
			
			if (currentSpeedLimitCooldown == 0) {
				currentSpeedLimitCooldown = SPEEDLIMIT_COOLDOWN;
				
				setCell(2,0,8);
				setCell(6,0,8);
			}
			else {
				//Place random billboard
				if (currentBillboardCooldown < 0) {
					currentBillboardCooldown = BILLBOARD_COOLDOWN;
					
					randomScenery = random.nextInt(2);
					
					if (randomScenery == 1)
						setCell(2, 0, 5);
					else {
						randomScenery = random.nextInt(5);
						
						if (randomScenery < 3)
							setCell(2,0, 9);
					}
					
					randomScenery = random.nextInt(2);
					
					if (randomScenery == 1)
						setCell(6, 0, 5);		
					else {
						randomScenery = random.nextInt(5);
						
						if (randomScenery < 3)
							setCell(6,0, 9);
					}
				}
			}
		}
		
	}
