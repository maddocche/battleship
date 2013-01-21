package com.bombo.battleship.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TableLayout;

import com.bombo.battleship.R;
import com.bombo.battleship.controller.GameplayController;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.ShipConfiguration;

public class GameplayActivity extends Activity {
	
	protected GameplayController mGameController;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gameplay);
		
		mGameController = new GameplayController();
		
		ShipConfiguration playerConfiguration = getIntent().getExtras().getParcelable(ShipConfiguration.SHIP_CONFIGURATION_TAG);
		
		mGameController.setPlayerConfiguration(playerConfiguration);
		
		GamePreferences gamePreferences = getIntent().getExtras().getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
		
		Board playerBoard = new Board(gamePreferences.getGridSize());
		
		mGameController.setPlayerBoard(playerBoard);
		
		TableLayout playerBoardView = (TableLayout) findViewById(R.id.player_board);
		TableLayout opponentBoardView = (TableLayout) findViewById(R.id.opponent_board);
		
		BoardAdapter playerBoardAdapter = new BoardAdapter(this, playerBoard, playerBoardView);
		BoardAdapter opponentBoardAdapter = new BoardAdapter(this, playerBoard, opponentBoardView);
		
		playerBoardAdapter.generateBoardView();
		opponentBoardAdapter.generateBoardView();
			
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

}
