package com.bombo.battleship.model;

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
		
		//int currentCell = 0;
		
		//for (int i = 1; i < gridSize; i++ ) 
		//	for (int j = 1; j < gridSize; j++ )
		//		mBoard[currentCell++] = new BoardCell(i, j);
				
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
	
	public void resetBoardIDs() {
		
		mIndex = 0;
	}

	public int[] getIDs() {
		return mIDs;
	}

	public int getGridSize() {
		return mGridSize;
	}
	
	public boolean isValidPosition(BoardCell start, BoardCell end) {
		
		if (end.getPosX() <= 0 ||
			end.getPosX() > mGridSize ||
			end.getPosY() <= 0 ||
			end.getPosY() > mGridSize)
			
			return false;
		
		
		if (start.getPosX() == end.getPosX()) {
			if (start.getPosY() < end.getPosY()) {
				for (int i=end.getPosY(); i >= start.getPosY(); i--)
					if (getBoardCellFromCoord(start.getPosX(), i).isOccupied())
						return false;
			} else {
				for (int i=start.getPosY(); i <= end.getPosY(); i++)
					if (getBoardCellFromCoord(start.getPosX(), i).isOccupied())
						return false;
			}
		} else if (start.getPosY() == end.getPosY()) {
			if (start.getPosX() < end.getPosX()) {
				for (int i=end.getPosX(); i >= start.getPosX(); i--)
					if (getBoardCellFromCoord(i, start.getPosY()).isOccupied())
						return false;
			} else {
				for (int i=start.getPosX(); i <= end.getPosX(); i++)
					if (getBoardCellFromCoord(i, start.getPosY()).isOccupied())
						return false;
			}
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
