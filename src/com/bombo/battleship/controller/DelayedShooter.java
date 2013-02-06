package com.bombo.battleship.controller;

import android.os.AsyncTask;

public class DelayedShooter extends AsyncTask< Void, Void, Void> {

	public static final long SHOOT_DELAY = 1000L;
	protected GameplayController mGameController;
	
	public DelayedShooter( GameplayController gameController ) {
		
		mGameController = gameController;
	}
	
	@Override
	protected Void doInBackground( Void... params ) {
		
		try {
			
			Thread.sleep( SHOOT_DELAY );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		
		mGameController.sendOpponentShot();
		
		super.onPostExecute(result);
	}

	
	
}
