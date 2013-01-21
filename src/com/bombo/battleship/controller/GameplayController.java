package com.bombo.battleship.controller;

import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.ShipConfiguration;

public class GameplayController {
	
	protected ShipConfiguration mPlayerConfiguration;
	protected ShipConfiguration mOpponentCOnfiguration;
	protected Board mPlayerBoard;
	protected Board mOpponentBoard;
	
	public ShipConfiguration getPlayerConfiguration() {
		return mPlayerConfiguration;
	}
	public void setPlayerConfiguration(ShipConfiguration mPlayerConfiguration) {
		this.mPlayerConfiguration = mPlayerConfiguration;
	}
	public ShipConfiguration getOpponentCOnfiguration() {
		return mOpponentCOnfiguration;
	}
	public void setOpponentCOnfiguration(ShipConfiguration mOpponentCOnfiguration) {
		this.mOpponentCOnfiguration = mOpponentCOnfiguration;
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
	
	

}
