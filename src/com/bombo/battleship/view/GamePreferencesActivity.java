package com.bombo.battleship.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.GamePreferences;

public class GamePreferencesActivity extends Activity 
									implements OnClickListener {
	
	private GamePreferences mGamePreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_preferences);
		
		//If activity is being recreated due to a configuration change, restore saved game preferences
		if (savedInstanceState != null) {
			mGamePreferences = savedInstanceState.getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
		} 
		//If the bundle didn't contain any game preference, creates a new one
		if (mGamePreferences == null) {
			mGamePreferences = new GamePreferences();
		}
		
		((Button) findViewById(R.id.grid_size_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.grid_size_plus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.aircraft_carrier_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.aircraft_carrier_plus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.battleship_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.battleship_plus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.submarine_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.submarine_plus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.destroyer_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.destroyer_plus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.patrol_boat_minus)).
			setOnClickListener(this);
		((Button) findViewById(R.id.patrol_boat_plus)).
			setOnClickListener(this);

		((Button) findViewById(R.id.game_preferences_back)).
		setOnClickListener(this);
		((Button) findViewById(R.id.game_preferences_next)).
		setOnClickListener(this);
		
		drawGamePreferences();
	}
	
	//Helper method to refresh on screen preferences
	public void drawGamePreferences () {
		
		((TextView) findViewById(R.id.grid_size)).
		setText(Integer.toString(mGamePreferences.getGridSize()));
		((TextView) findViewById(R.id.aircraft_carrier)).
		setText(Integer.toString(mGamePreferences.getAircraftCarrierNumber()));
		((TextView) findViewById(R.id.battleship)).
		setText(Integer.toString(mGamePreferences.getBattleshipNumber()));
		((TextView) findViewById(R.id.submarine)).
		setText(Integer.toString(mGamePreferences.getSubmarineNumber()));
		((TextView) findViewById(R.id.destroyer)).
		setText(Integer.toString(mGamePreferences.getDestroyerNumber()));
		((TextView) findViewById(R.id.patrol_boat)).
		setText(Integer.toString(mGamePreferences.getPatrolBoatNumber()));
	}

	//Saves game preferences for activity recreation
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelable(GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.grid_size_minus:
			
			if (mGamePreferences.getGridSize() > GamePreferences.MIN_GRID_SIZE) {
				
				mGamePreferences.setGridSize(mGamePreferences.getGridSize() - 1);
			}
			
			((TextView) findViewById(R.id.grid_size)).
				setText(Integer.toString(mGamePreferences.getGridSize()));
			
			break;

		case R.id.grid_size_plus:
			
			if (mGamePreferences.getGridSize() < GamePreferences.MAX_GRID_SIZE) {
				
				mGamePreferences.setGridSize(mGamePreferences.getGridSize() + 1);
			}
			
			((TextView) findViewById(R.id.grid_size)).
				setText(Integer.toString(mGamePreferences.getGridSize()));
			
			break;
			
		case R.id.aircraft_carrier_minus:
			
			if (mGamePreferences.getAircraftCarrierNumber() > GamePreferences.MIN_AIRCRAFT_CARRIER_NUMBER) {
				
				mGamePreferences.setAircraftCarrierNumber(mGamePreferences.getAircraftCarrierNumber() - 1);
			}
			
			((TextView) findViewById(R.id.aircraft_carrier)).
				setText(Integer.toString(mGamePreferences.getAircraftCarrierNumber()));
			
			break;

		case R.id.aircraft_carrier_plus:
			
			if (mGamePreferences.getAircraftCarrierNumber() < GamePreferences.MAX_AIRCRAFT_CARRIER_NUMBER) {
				
				mGamePreferences.setAircraftCarrierNumber(mGamePreferences.getAircraftCarrierNumber() + 1);
			}
			
			((TextView) findViewById(R.id.aircraft_carrier)).
				setText(Integer.toString(mGamePreferences.getAircraftCarrierNumber()));
			
			break;

		case R.id.battleship_minus:
			
			if (mGamePreferences.getBattleshipNumber() > GamePreferences.MIN_BATTLESHIP_NUMBER) {
				
				mGamePreferences.setBattleshipNumber(mGamePreferences.getBattleshipNumber() - 1);
			}
			
			((TextView) findViewById(R.id.battleship)).
				setText(Integer.toString(mGamePreferences.getBattleshipNumber()));
			
			break;

		case R.id.battleship_plus:
			
			if (mGamePreferences.getBattleshipNumber() < GamePreferences.MAX_BATTLESHIP_NUMBER) {
				
				mGamePreferences.setBattleshipNumber(mGamePreferences.getBattleshipNumber() + 1);
			}
			
			((TextView) findViewById(R.id.battleship)).
				setText(Integer.toString(mGamePreferences.getBattleshipNumber()));
			
			break;

		case R.id.submarine_minus:
			
			if (mGamePreferences.getSubmarineNumber() > GamePreferences.MIN_SUBMARINE_NUMBER) {
				
				mGamePreferences.setSubmarineNumber(mGamePreferences.getSubmarineNumber() - 1);
			}
			
			((TextView) findViewById(R.id.submarine)).
				setText(Integer.toString(mGamePreferences.getSubmarineNumber()));
			
			break;

		case R.id.submarine_plus:
			
			if (mGamePreferences.getSubmarineNumber() < GamePreferences.MAX_SUBMARINE_NUMBER) {
				
				mGamePreferences.setSubmarineNumber(mGamePreferences.getSubmarineNumber() + 1);
			}
			
			((TextView) findViewById(R.id.submarine)).
				setText(Integer.toString(mGamePreferences.getSubmarineNumber()));
			
			break;
		
		case R.id.destroyer_minus:
			
			if (mGamePreferences.getDestroyerNumber() > GamePreferences.MIN_DESTROYER_NUMBER) {
				
				mGamePreferences.setDestroyerNumber(mGamePreferences.getDestroyerNumber() - 1);
			}
			
			((TextView) findViewById(R.id.destroyer)).
				setText(Integer.toString(mGamePreferences.getDestroyerNumber()));
			
			break;

		case R.id.destroyer_plus:
			
			if (mGamePreferences.getDestroyerNumber() < GamePreferences.MAX_DESTROYER_NUMBER) {
				
				mGamePreferences.setDestroyerNumber(mGamePreferences.getDestroyerNumber() + 1);
			}
			
			((TextView) findViewById(R.id.destroyer)).
				setText(Integer.toString(mGamePreferences.getDestroyerNumber()));
			
			break;
			
		case R.id.patrol_boat_minus:
			
			if (mGamePreferences.getPatrolBoatNumber() > GamePreferences.MIN_PATROL_BOAT_NUMBER) {
				
				mGamePreferences.setPatrolBoatNumber(mGamePreferences.getPatrolBoatNumber() - 1);
			}
			
			((TextView) findViewById(R.id.patrol_boat)).
				setText(Integer.toString(mGamePreferences.getPatrolBoatNumber()));
			
			break;

		case R.id.patrol_boat_plus:
			
			if (mGamePreferences.getPatrolBoatNumber() < GamePreferences.MAX_PATROL_BOAT_NUMBER) {
				
				mGamePreferences.setPatrolBoatNumber(mGamePreferences.getPatrolBoatNumber() + 1);
			}
			
			((TextView) findViewById(R.id.patrol_boat)).
				setText(Integer.toString(mGamePreferences.getPatrolBoatNumber()));
			
			break;
			
		case R.id.game_preferences_back:
			
			finish();
			
			break;
			
		case R.id.game_preferences_next:
			
			Intent intent = new Intent(getApplicationContext(), ShipConfigurationActivity.class);
			intent.putExtra(GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences);
			
			startActivity(intent);
			
			break;

		default:
			break;
		}
	}
	
	
	
}
