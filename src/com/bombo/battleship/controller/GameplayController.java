package com.bombo.battleship.controller;

import android.widget.ImageView;

import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.util.Utilities;
import com.bombo.battleship.view.BoardAdapter;

public class GameplayController {
	
	protected GamePreferences mGamePreferences;
	protected Board mPlayerBoard;
	protected Board mOpponentBoard;
	protected BoardAdapter mPlayerBoardAdapter;
	protected BoardAdapter mOpponentBoardAdapter;
	protected boolean mPlayerWon;
	protected boolean mOpponentWon;
	
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
	}

	public void generateBoardViews() {
		
		mPlayerBoardAdapter.generateBoardView();
		mOpponentBoardAdapter.generateBoardView();
	}
	
	public void refreshBoardsView() {
		
		mPlayerBoardAdapter.redrawEntireBoard();
		mOpponentBoardAdapter.redrawEntireBoard();
	}
	
	public boolean sendPlayerShot( ImageView v ) {
		
		if ( !mPlayerWon && !mOpponentWon ) {
			
			BoardCell hittedCell = mOpponentBoard.getBoardCellFromId( v.getId() );
			
			hittedCell.hit();
			mOpponentBoardAdapter.drawCellStatus( hittedCell );
			
			if ( mOpponentBoard.getShipConfiguration().areAllShipsSinked() ) {
				
				mPlayerWon = true;
			} else {
				
				sendOpponentShot();
			}
			
			return true;
		}
		
		return false;
	}
	
	public void sendOpponentShot() {
		
		BoardCell random;
		
		BoardCell hittedCell;
		
		do {
			
			random = Utilities.generateRandomCell( mGamePreferences.getGridSize() );
			hittedCell = mPlayerBoard.getBoardCellFromCoord( random.getPosX(), random.getPosY() ); 
			
		} while ( hittedCell.isHitted() );
		
		hittedCell.hit();
		mPlayerBoardAdapter.drawCellStatus( hittedCell );
		
		if ( mPlayerBoard.getShipConfiguration().areAllShipsSinked() ) {
			
			mOpponentWon = true;
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
	public BoardAdapter getPlayerBoardAdapter() {
		return mPlayerBoardAdapter;
	}
	public void setPlayerBoardAdapter(BoardAdapter mPlayerBoardAdapter) {
		this.mPlayerBoardAdapter = mPlayerBoardAdapter;
	}
	public BoardAdapter getOpponentBoardAdapter() {
		return mOpponentBoardAdapter;
	}
	public void setOpponentBoardAdapter(BoardAdapter mOpponentBoardAdapter) {
		this.mOpponentBoardAdapter = mOpponentBoardAdapter;
	}
	
	public boolean hasPlayerWon() {
		return mPlayerWon;
	}
	
	public boolean hasOpponentWon() {
		return mOpponentWon;
	}

}
