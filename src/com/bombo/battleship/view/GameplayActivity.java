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
		
		Bundle extras;
		
		if ( savedInstanceState == null ) {
			
			extras = getIntent().getExtras();
		} else {
			
			extras = savedInstanceState;
		}
		
		GamePreferences gamePreferences = getIntent().getExtras().getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
		
		mGameController.setGamePreferences( gamePreferences );
		
		ShipConfiguration playerConfiguration = extras.getParcelable( ShipConfigurationActivity.PLAYER_CONFIGURATION );
		Board playerBoard = new Board( gamePreferences.getGridSize(), playerConfiguration );
		
		mGameController.setPlayerBoard(playerBoard);		
		
		ShipConfiguration opponentConfiguration = extras.getParcelable( ShipConfigurationActivity.OPPONENT_CONFIGURATION );
		Board opponentBoard = new Board( gamePreferences.getGridSize(), opponentConfiguration );
		
		mGameController.setOpponentBoard(opponentBoard);
		
		TableLayout playerBoardView = (TableLayout) findViewById(R.id.player_board);
		TableLayout opponentBoardView = (TableLayout) findViewById(R.id.opponent_board);
		
		BoardAdapter playerBoardAdapter = new BoardAdapter(this, playerBoard, playerBoardView);
		BoardAdapter opponentBoardAdapter = new BoardAdapter(this, opponentBoard, opponentBoardView);
		
		mGameController.setPlayerBoardAdapter(playerBoardAdapter);
		mGameController.setOpponentBoardAdapter(opponentBoardAdapter);
		
		mGameController.generateBoardViews();
		mGameController.drawShipsOnBoards();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelable( GamePreferences.GAME_PREFERENCES_TAG
				, mGameController.getGamePreferences() );
		outState.putParcelable( ShipConfigurationActivity.PLAYER_CONFIGURATION
				, mGameController.getPlayerBoard().getShipConfiguration() );
		outState.putParcelable( ShipConfigurationActivity.OPPONENT_CONFIGURATION
				, mGameController.getOpponentBoard().getShipConfiguration() );
		
	}

}
