package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bombo.battleship.util.Utilities;

public class Board implements Parcelable{

	public static final String BOARD_TAG = "Board";
	
	protected BoardCell[] mBoard;
	protected int mSize;
	protected int mGridSize;
	protected int[] mIDs;
	protected ShipConfiguration mShipConfiguration;
	
	public Board( int gridSize, ShipConfiguration shipConfiguration ) {
		
		mGridSize = gridSize;
		mSize = gridSize * gridSize;
		mBoard = new BoardCell[ mSize ];
		
		int i = 0;
		
		for ( int y = 1; y <= mGridSize; y++ )
			for ( int x = 1; x <= mGridSize; x++ )
				mBoard[ i++ ] = new BoardCell( x, y );
		
		mIDs = new int[ mSize ];
		mShipConfiguration = shipConfiguration;
	}

	private Board( Parcel in ) {
		
		mSize = in.readInt();
		mGridSize = in.readInt();
		
		Object[] values = in.readArray( BoardCell.class.getClassLoader() );
		mBoard = new BoardCell[ values.length ];
		
		for ( int i = 0; i < values.length; i++ )
			mBoard[ i ] = ( BoardCell ) values[ i ];
		
		mIDs = new int[ mSize ];
		
		in.readIntArray( mIDs );
		mShipConfiguration = in.readParcelable( ShipConfiguration.class.getClassLoader() );
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt( mSize );
		dest.writeInt( mGridSize );
		dest.writeArray( mBoard );
		dest.writeIntArray( mIDs );
		dest.writeParcelable( mShipConfiguration, 0 );
	}
	
	public static final Parcelable.Creator<Board> CREATOR
		= new Parcelable.Creator<Board>() {

			public Board createFromParcel (Parcel in) {
				return new Board(in);
			}

			public Board[] newArray(int size) {
				return new Board[size];
			}
		};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public void mapCellID(int ID, int x, int y) {
		
		mIDs[ Utilities.getIndexFromCoord( x, y, mGridSize ) ] = ID;
	}
	
	public int[] getIDs() {
		return mIDs;
	}

	public int getGridSize() {
		return mGridSize;
	}
	
	public ShipConfiguration getShipConfiguration() {
		return mShipConfiguration;
	}
	
	public boolean isValidDirection(BoardCell start, Direction direction, ShipType shipType) {
		
		int endCell = Utilities.getEndCoord(start, direction, shipType);
		
		if (endCell <= 0 ||
			endCell > mGridSize)
						
			return false;
		
		switch (direction) {
		case NORTH:
			
			for (int i=start.getPosY(); i >= endCell; i--)
				if (getBoardCellFromCoord(start.getPosX(), i).isOccupied())
					return false;
			
			break;
			
		case EAST:
			
			for (int i=start.getPosX(); i <= endCell; i++)
				if (getBoardCellFromCoord(i, start.getPosY()).isOccupied())
					return false;
			
			break;
			
		case SOUTH:
			
			for (int i=start.getPosY(); i <= endCell; i++)
				if (getBoardCellFromCoord(start.getPosX(), i).isOccupied())
					return false;
			
			break;
			
		case WEST:
			
			for (int i=start.getPosX(); i >= endCell; i--)
				if (getBoardCellFromCoord(i, start.getPosY()).isOccupied())
					return false;
			
			break;

		default:
			break;
		}
		
		return true;
	}
	
	public BoardCell getBoardCellFromCoord(int x, int y) {
		
		return mBoard[ Utilities.getIndexFromCoord( x, y, mGridSize ) ];
	}
	
	public int getIdFromCoord(int x, int y) {
		
		return mIDs[ Utilities.getIndexFromCoord( x, y, mGridSize ) ];
	}
	
	public BoardCell getBoardCellFromId(int id) {
		
		for (int i = 0; i < mIDs.length; i++ ) 
			if (mIDs[i] == id) 
				return mBoard[i];
		
		return null;
	}
	
	public void putShip( Ship ship, BoardCell start, Direction direction ) {
		
		mShipConfiguration.addShipPosition( ship, start, direction, this );
	}
	
	public void removeShip( Ship ship ) {
		
		mShipConfiguration.removeShipFromBoard( ship, this ); 
	}
	
}
