package com.bombo.battleship.controller;

import android.os.AsyncTask;

public class DelayedShooter extends AsyncTask< Void, Void, Void> {

	protected GameplayController mGameController;
	
	public DelayedShooter( GameplayController gameController ) {
		
		mGameController = gameController;
	}
	
	@Override
	protected Void doInBackground( Void... params ) {
		
		try {
			
			Thread.sleep( 500 );
			
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
