package com.bombo.battleship.model;

import com.bombo.battleship.util.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Board implements Parcelable{

	public static final String BOARD_TAG = "Board";
	
	protected BoardCell[] mBoard;
	protected int mSize;
	protected int mGridSize;
	protected int[] mIDs;
	protected int mIndex;
	
	public Board(int gridSize) {
		
		mGridSize = gridSize;
		mSize = gridSize * gridSize;
		mIndex = 0;
		mBoard = new BoardCell[mSize];
		mIDs = new int[mSize];
		
	}

	private Board(Parcel in) {
		
		mSize = in.readInt();
		mGridSize = in.readInt();
		mIndex = in.readInt();
		mBoard = (BoardCell[]) in.readArray(null);
		in.readIntArray(mIDs);
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeInt(mSize);
		dest.writeInt(mGridSize);
		dest.writeInt(mIndex);
		dest.writeArray(mBoard);
		dest.writeIntArray(mIDs);
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
	
	public BoardCell getBoardCell(int cell) {
		
		return mBoard[cell];
	}

	public BoardCell[] getBoard() {
		return mBoard;
	}

	public void setBoard(BoardCell[] mBoard) {
		this.mBoard = mBoard;
	}
	
	public void addBoardCell(int ID, int x, int y) {
		
		mIDs[mIndex] = ID;
		mBoard[mIndex] = new BoardCell(x, y);
		mIndex++;
	}
	
	public void resetBoardIDsIndex() {
		
		mIndex = 0;
	}

	public int[] getIDs() {
		return mIDs;
	}

	public int getGridSize() {
		return mGridSize;
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
		
		return mBoard[((y - 1) * mGridSize) + (x - 1)];
	}
	
	public int getIdFromCoord(int x, int y) {
		
		return mIDs[((y - 1) * mGridSize) + (x - 1)];
	}
	
	public BoardCell getBoardCellFromId(int id) {
		
		for (int i = 0; i < mIDs.length; i++ ) 
			if (mIDs[i] == id) 
				return mBoard[i];
		
		return null;
	}
	
	public int getIdFromBoardCell(BoardCell cell) {
		
		return mIDs[((cell.getPosY() - 1) * mGridSize) + (cell.getPosY() - 1)];
	}
}
