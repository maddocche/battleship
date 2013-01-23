package com.bombo.battleship.controller;

import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.view.BoardAdapter;

public class GameplayController {
	
	protected GamePreferences mGamePreferences;
	protected Board mPlayerBoard;
	protected Board mOpponentBoard;
	protected BoardAdapter mPlayerBoardAdapter;
	protected BoardAdapter mOpponentBoardAdapter;

	public void generateBoardViews() {
		
		mPlayerBoardAdapter.generateBoardView();
		mOpponentBoardAdapter.generateBoardView();
	}
	
	public void drawShipsOnBoards() {
		
		mPlayerBoardAdapter.redrawPositionedShips( mPlayerBoard.getShipConfiguration() );
		mOpponentBoardAdapter.redrawPositionedShips( mOpponentBoard.getShipConfiguration() );
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
	
	

}
