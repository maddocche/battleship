package com.bombo.battleship.util;

import android.app.Activity;

import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.Direction;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.Ship;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.model.ShipType;

public class Utilities {
	
	private static Object mLock;
	private static Utilities mInstance;
	private int mUniqueID;
	
	private Utilities() {
		
		mUniqueID = Integer.MAX_VALUE;
	}
	
	public static Utilities getInstance() {
		
		mLock = new Object();
		
		synchronized (mLock) {
			
			if (mInstance == null) {
				mInstance = new Utilities();
			}
			
			return mInstance;
		}
	}
	
	public int getNextId(Activity parent) {
		
		synchronized (mLock) {
			
			if (mUniqueID <= 0) {
				mUniqueID = Integer.MAX_VALUE;
			}
			
			do {
				
				--mUniqueID;
			} while (parent.findViewById(mUniqueID) != null);
		}
		
		return mUniqueID;
	}
	
	public static int getEndCoord(BoardCell start, Direction direction, ShipType shipType) {
		
		switch (direction) {
		case NORTH:
			
			return start.getPosY() - shipType.getSize() + 1;
			
		case EAST:
			
			return start.getPosX() + shipType.getSize() - 1;
			
		case SOUTH:
			
			return start.getPosY() + shipType.getSize() - 1;
			
		case WEST:
			
			return start.getPosX() - shipType.getSize() + 1;
			
		default:
			break;
		}
		
		return 0;
		
	}
	
	public static ShipConfiguration generateRandomConfiguration( GamePreferences gamePreferences ) {
		
		ShipConfiguration configuration = new ShipConfiguration();
		configuration.readShipsPreference( gamePreferences );
		int gridSize = gamePreferences.getGridSize();
		Board board = new Board( gridSize, configuration );
		Direction direction;
		BoardCell start;
		
		for ( Ship ship : configuration.getShips() ) {
			
			do {
				
				start = generateRandomCell( gridSize );
				direction = generateRandomDirection();
				
			} while ( !board.isValidDirection( start, direction, ship.getShipType() ) );
			
			board.putShip( ship, start, direction );
		}
		
		return configuration;
	}
	
	public static BoardCell generateRandomCell( int gridSize ) {
		
		int startX = ( int ) Math.round( Math.random() * ( gridSize - 1 ) ) + 1;
		int startY = ( int ) Math.round( Math.random() * ( gridSize - 1 ) ) + 1;
		
		return new BoardCell( startX, startY );
	}
	
	public static Direction generateRandomDirection() {
		
		int dir = ( int ) Math.round( Math.random() * 3 ) + 1;
		
		switch (dir) {
		case 1:
			
			return Direction.NORTH;
			
		case 2:
			
			return Direction.EAST;
			
		case 3:
			
			return Direction.SOUTH;
			
		case 4:
			
			return Direction.WEST;
			
		default:
			
			return null;
		}
	}
	
	public static int getIndexFromCoord( int x, int y, int gridSize ) {
		
		return ((y - 1) * gridSize) + (x - 1);
	}
	
	public static String convertItoC( int i ) {
		
		switch (i) {
		case 0:
			
			return " ";
		case 1:
			
			return "A";
		case 2:
			
			return "B";
		case 3:
			
			return "C";
		case 4:
			
			return "D";
		case 5:
			
			return "E";
		case 6:
			
			return "F";
		case 7:
			
			return "G";
		case 8:
			
			return "H";
		case 9:
			
			return "I";
		case 10:
			
			return "J";
		case 11:
			
			return "K";
		case 12:
			
			return "L";
		case 13:
			
			return "M";
		case 14:
			
			return "N";
		case 15:
			
			return "O";
		case 16:
			
			return "P";
		case 17:
			
			return "Q";
		case 18:
			
			return "R";
		case 19:
			
			return "S";
		case 20:
			
			return "T";

		default:
			break;
		}
		
		return "@";
	}
}
