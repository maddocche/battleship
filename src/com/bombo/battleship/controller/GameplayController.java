package com.bombo.battleship.controller;

import android.widget.ImageView;

import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.util.Utilities;
import com.bombo.battleship.view.BoardDrawHelper;

public class GameplayController {
	
	protected GamePreferences mGamePreferences;
	protected Board mPlayerBoard;
	protected Board mOpponentBoard;
	protected BoardDrawHelper mPlayerBoardAdapter;
	protected BoardDrawHelper mOpponentBoardAdapter;
	protected boolean mPlayerWon;
	protected boolean mOpponentWon;
	protected boolean mOpponentTurn;
	
	public void checkGameState() {
		
		if ( mPlayerBoard.getShipConfiguration().areAllShipsSinked() ) {
			
			mOpponentWon = true;
		} else {
			mOpponentWon = false;
		}
		
		if ( mOpponentBoard.getShipConfiguration().areAllShipsSinked() ) {
			
			mPlayerWon = true;
		} else {
			mPlayerWon = false;
		}
		
		mOpponentTurn = false;
	}

	public void generateBoardViews() {
		
		mPlayerBoardAdapter.generateBoardView();
		mOpponentBoardAdapter.generateBoardView();
	}
	
	public void refreshBoardsView() {
		
		mPlayerBoardAdapter.redrawEntireBoard( Board.PLAYER_BOARD );
		mOpponentBoardAdapter.redrawEntireBoard( Board.OPPONENT_BOARD );
	}
	
	public boolean sendPlayerShot( ImageView v ) {
		
		
		if ( !mPlayerWon && !mOpponentWon && !mOpponentTurn ) {
			
			boolean hitSomething;
			BoardCell selectedCell = mOpponentBoard.getBoardCellFromId( v.getId() );
			
			if ( !selectedCell.isHitted() ) {
				
				hitSomething = selectedCell.hit();
				mOpponentBoardAdapter.drawCellStatus( selectedCell, Board.OPPONENT_BOARD );
				
				if ( mOpponentBoard.getShipConfiguration().areAllShipsSinked() ) {
					
					mPlayerWon = true;
				} else {
					
					if ( !hitSomething ) {
						
						mOpponentTurn = true;
						new DelayedShooter( this ).execute();
					}
				}
				
				return true;
			}
		}
		
		return false;
	}
	
	public void sendOpponentShot() {
		
		BoardCell random;
		BoardCell hittedCell;
		
		boolean hitSomething;

		do {

			random = Utilities.generateRandomCell( mGamePreferences.getGridSize() );
			hittedCell = mPlayerBoard.getBoardCellFromCoord( random.getPosX(), random.getPosY() ); 

		} while ( hittedCell.isHitted() );

		hitSomething = hittedCell.hit();
		mPlayerBoardAdapter.drawCellStatus( hittedCell, Board.PLAYER_BOARD );

		if ( mPlayerBoard.getShipConfiguration().areAllShipsSinked() ) {

			mOpponentWon = true;
		}
		
		if ( hitSomething ) {
			
			mOpponentTurn = true;
			new DelayedShooter( this ).execute();
		} else {
			mOpponentTurn = false;
		}
		
	}
	
	public Board getPlayerBoard() {
		return mPlayerBoard;
	}
	public void setPlayerBoard(Board mPlayerBoard) {
		this.mPlayerBoard = mPlayerBoard;
	}
	public Board getOpponentBoard() {
		return mOpponentBoard;
	}
	public void setOpponentBoard(Board mOpponentBoard) {
		this.mOpponentBoard = mOpponentBoard;
	}
	public GamePreferences getGamePreferences() {
		return mGamePreferences;
	}
	public void setGamePreferences(GamePreferences mGamePreferences) {
		this.mGamePreferences = mGamePreferences;
	}
	public BoardDrawHelper getPlayerBoardAdapter() {
		return mPlayerBoardAdapter;
	}
	public void setPlayerBoardAdapter(BoardDrawHelper mPlayerBoardAdapter) {
		this.mPlayerBoardAdapter = mPlayerBoardAdapter;
	}
	public BoardDrawHelper getOpponentBoardAdapter() {
		return mOpponentBoardAdapter;
	}
	public void setOpponentBoardAdapter(BoardDrawHelper mOpponentBoardAdapter) {
		this.mOpponentBoardAdapter = mOpponentBoardAdapter;
	}
	
	public boolean hasPlayerWon() {
		return mPlayerWon;
	}
	
	public boolean hasOpponentWon() {
		return mOpponentWon;
	}

}
