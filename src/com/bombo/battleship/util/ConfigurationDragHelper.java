package com.bombo.battleship.util;

import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.Direction;

public class ConfigurationDragHelper {
	
	private static ConfigurationDragHelper mConfigurationDragHelper;
	
	private Object mLock;
	
	private BoardCell mStartCell;
	private boolean mDragStarted;
	private Direction mDirection;
	private Direction mPreviousDirection;
	private boolean mPositionChosen;
	
	private ConfigurationDragHelper() {
		
		mLock = new Object();
		mStartCell = new BoardCell(0, 0);
		mDragStarted = false;
	}
	
	public static ConfigurationDragHelper getInstance() {
		
		if (mConfigurationDragHelper == null) {
			mConfigurationDragHelper = new ConfigurationDragHelper();
		}
		
		return mConfigurationDragHelper;
	}
	
	public boolean isDragStarted() {
		return mDragStarted;
	}

	public void startDrag(BoardCell start) {
		
		synchronized (mLock) {
			
			if (!mDragStarted) {
				
				mStartCell = start;
				mDragStarted = true;
			}
		}
	}
	
	public void endDrag() {
		
		synchronized (mLock) {
			
			if (mDragStarted) {
				
				mStartCell = null;
				mDragStarted = false;
				mDirection = null;
				mPreviousDirection = null;
				mPositionChosen = false;
			}
		}
	}

	public BoardCell getStartCell() {
		return mStartCell;
	}

	public void setStartCell(BoardCell mStartCell) {
		this.mStartCell = mStartCell;
	}

	public Direction getDirection() {
		return mDirection;
	}

	public void setDirection(Direction mDirection) {
		this.mDirection = mDirection;
	}
	
	public boolean isChosenDirection(Direction d) {
		return d == mDirection;
	}
	
	public Direction getPreviousDirection() {
		return mPreviousDirection;
	}

	public void setPreviousDirection(Direction mPreviousDirection) {
		this.mPreviousDirection = mPreviousDirection;
	}

	public boolean isPositionChosen() {
		return mPositionChosen;
	}

	public void setPositionChosen(boolean mPositionChosen) {
		this.mPositionChosen = mPositionChosen;
	}
	
	

}

