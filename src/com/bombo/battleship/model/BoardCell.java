package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class BoardCell implements Parcelable {
	
	protected int mPosX;
	protected int mPosY;
	protected boolean mHitted;
	protected boolean mOccupied;
	protected Ship mShipOver;
	
	public BoardCell(int x, int y) {
		mPosX = x;
		mPosY = y;
		mHitted = false;
		mOccupied = false;
		mShipOver = null;
	}
	
	private BoardCell(Parcel in) {
		mPosX = in.readInt();
		mPosY = in.readInt();
		//...and then convert it back into a boolean
		mHitted = (in.readByte() == 1);
		mOccupied = (in.readByte() == 1);
		mShipOver = in.readParcelable(null);
	}

	public int getPosX() {
		return mPosX;
	}

	public void setPosX(int mPosX) {
		this.mPosX = mPosX;
	}

	public int getPosY() {
		return mPosY;
	}

	public void setPosY(int mPosY) {
		this.mPosY = mPosY;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mPosX);
		dest.writeInt(mPosY);
		//To write a boolean in a parcel you have to convert it into a byte...
		dest.writeByte((byte) (mHitted ? 1 : 0));
		dest.writeByte((byte) (mOccupied ? 1 : 0));
		dest.writeParcelable(mShipOver, 0);
	}
	
	public static final Parcelable.Creator<BoardCell> CREATOR
		= new Parcelable.Creator<BoardCell>() {
	
			public BoardCell createFromParcel (Parcel in) {
				return new BoardCell(in);
			}
	
			public BoardCell[] newArray(int size) {
				return new BoardCell[size];
			}
		};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public boolean isHitted() {
		
		return mHitted;
	}
	
	public boolean isOccupied() {
		
		return mOccupied;
	}
	
	public void hit() {
		
		mHitted = true;		
	}
	
	public void occupy() {
		
		mOccupied = true;
	}

	public Ship getShipOver() {
		return mShipOver;
	}

	public void setShipOver(Ship mShipOver) {
		this.mShipOver = mShipOver;
	}
	
}
