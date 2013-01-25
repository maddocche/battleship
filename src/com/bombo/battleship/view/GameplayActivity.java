package com.bombo.battleship.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.Toast;

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
		Board playerBoard = extras.getParcelable( Board.PLAYER_BOARD );
		
		if ( playerBoard == null )
			playerBoard = new Board( gamePreferences.getGridSize(), playerConfiguration );
		
		mGameController.setPlayerBoard(playerBoard);		
		
		ShipConfiguration opponentConfiguration = extras.getParcelable( ShipConfigurationActivity.OPPONENT_CONFIGURATION );
		Board opponentBoard = extras.getParcelable( Board.OPPONENT_BOARD );
		
		if ( opponentBoard == null )
			opponentBoard = new Board( gamePreferences.getGridSize(), opponentConfiguration );
		
		mGameController.setOpponentBoard(opponentBoard);
		
		TableLayout playerBoardView = (TableLayout) findViewById(R.id.player_board);
		TableLayout opponentBoardView = (TableLayout) findViewById(R.id.opponent_board);
		
		BoardAdapter playerBoardAdapter = new BoardAdapter(this, playerBoard, playerBoardView);
		BoardAdapter opponentBoardAdapter = new BoardAdapter(this, opponentBoard, opponentBoardView);
		
		mGameController.setPlayerBoardAdapter(playerBoardAdapter);
		mGameController.setOpponentBoardAdapter(opponentBoardAdapter);
		
		mGameController.generateBoardViews();
		mGameController.refreshBoardsView();
		mGameController.checkGameState();
		
		setBoardsListener();
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
	
	public void setBoardsListener() {
		
		Board board = mGameController.getOpponentBoard();
		ImageView v;
		
		for ( int id : board.getIDs() ) {
			
			v = ( ImageView ) findViewById( id );
			v.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
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
			} );
			
		}
		
	}

}
