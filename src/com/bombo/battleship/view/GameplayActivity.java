package com.bombo.battleship.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageView;
import android.widget.Toast;

import com.bombo.battleship.R;
import com.bombo.battleship.controller.GameplayController;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.view.BoardFragment.GameControllerCallback;

public class GameplayActivity extends FragmentActivity 
								implements GameControllerCallback {
	
	protected GameplayController mGameController;
	protected BoardFragment mPlayerBoardFragment;
	protected BoardFragment mOpponentBoardFragment;
	
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
		Board playerBoard = extras.getParcelable( Board.PLAYER_BOARD );
		
		if ( playerBoard == null )
			playerBoard = new Board( gamePreferences.getGridSize(), playerConfiguration );
		
		mGameController.setPlayerBoard(playerBoard);		
		
		ShipConfiguration opponentConfiguration = extras.getParcelable( ShipConfigurationActivity.OPPONENT_CONFIGURATION );
		Board opponentBoard = extras.getParcelable( Board.OPPONENT_BOARD );
		
		if ( opponentBoard == null )
			opponentBoard = new Board( gamePreferences.getGridSize(), opponentConfiguration );
		
		mGameController.setOpponentBoard(opponentBoard);
		
		Bundle fragmentParameter = new Bundle();
		mPlayerBoardFragment = new BoardFragment();
		fragmentParameter.putParcelable( Board.BOARD_TAG, playerBoard );
		fragmentParameter.putString( Board.BOARD_TYPE, Board.PLAYER_BOARD );
		mPlayerBoardFragment.setArguments( fragmentParameter );
		
		mOpponentBoardFragment = new BoardFragment();
		fragmentParameter = new Bundle();
		fragmentParameter.putParcelable( Board.BOARD_TAG, opponentBoard );
		fragmentParameter.putString( Board.BOARD_TYPE, Board.OPPONENT_BOARD );
		mOpponentBoardFragment.setArguments( fragmentParameter );
		
		mGameController.checkGameState();
		/*
		TableLayout playerBoardView = (TableLayout) findViewById(R.id.player_board);
		TableLayout opponentBoardView = (TableLayout) findViewById(R.id.opponent_board);
		
		BoardDrawHelper playerBoardAdapter = new BoardDrawHelper(this, playerBoard, playerBoardView);
		BoardDrawHelper opponentBoardAdapter = new BoardDrawHelper(this, opponentBoard, opponentBoardView);
		
		mGameController.setPlayerBoardAdapter(playerBoardAdapter);
		mGameController.setOpponentBoardAdapter(opponentBoardAdapter);
		
		mGameController.generateBoardViews();
		mGameController.refreshBoardsView();
		
		setBoardsListener();*/
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
		outState.putParcelable( Board.PLAYER_BOARD, mGameController.getPlayerBoard() );
		outState.putParcelable( Board.OPPONENT_BOARD, mGameController.getOpponentBoard() );
		
	}
	
	@Override
	public void sendPlayerShot(ImageView v) {
		
		boolean checkState = mGameController.sendPlayerShot( ( ImageView ) v );
		
		if ( checkState ) {
			
			if ( mGameController.hasPlayerWon() ) {
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Player wins!!!", Toast.LENGTH_LONG);
				toast.show();
			}
			
			if ( mGameController.hasOpponentWon() ) {
				
				Toast toast = Toast.makeText(getApplicationContext(), 
						"Opponent wins!!!", Toast.LENGTH_LONG);
				toast.show();
			}
			
		}
	}
	
	public void showPlayerFragment() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction.replace( R.id.gameplay_container, mPlayerBoardFragment );
		
		fragmentTransaction.commit();
		
	}
	
	public void showOpponentFragment() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction.replace( R.id.gameplay_container, mOpponentBoardFragment );
		
		fragmentTransaction.commit();
				
	}
}

