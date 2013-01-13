package com.bombo.battleship.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Ship implements Parcelable {

	protected List<BoardCell> mPosition;
	protected int mSize;
	protected int mType;
	protected String mName;
	protected boolean mPositioned;
	protected Direction mDirection;
	
	public Ship(ShipType shipType) {
		mSize = shipType.getSize();
		mType = shipType.getType();
		mName = shipType.getName();
		mPositioned = false;
	}
	
	private Ship(Parcel in) {
		//...and then convert it back into a boolean
		mPositioned = (in.readByte() == 1);
		mSize = in.readInt();
		mType = in.readInt();
		mName = in.readString();
		mDirection = (Direction) in.readSerializable();
		in.readList(mPosition, BoardCell.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//To write a boolean in a parcel you have to convert it into a byte...
		dest.writeByte((byte) (mPositioned ? 1 : 0));
		dest.writeInt(mSize);
		dest.writeInt(mType);
		dest.writeString(mName);
		dest.writeSerializable(mDirection);
		dest.writeList(mPosition);
		
	}
	
	public static final Parcelable.Creator<Ship> CREATOR
		= new Parcelable.Creator<Ship>() {
		
			public Ship createFromParcel (Parcel in) {
				return new Ship(in);
			}
		
			public Ship[] newArray(int size) {
				return new Ship[size];
			}
		};
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	//Helper method to get all the board cells occupied by a ship given start and end cells
	public void setShipPosition(BoardCell start, BoardCell end) {
		
		if (start.getPosX() == end.getPosX()) {
			if (start.getPosY() < end.getPosY()) {
				for (int i=end.getPosY(); i >= start.getPosY(); i--)
					mPosition.add(new BoardCell(start.getPosX(), i));
				
				mDirection = Direction.EAST;
			} else {
				for (int i=start.getPosY(); i <= end.getPosY(); i++)
					mPosition.add(new BoardCell(start.getPosX(), i));
				
				mDirection = Direction.WEST;
			}
		} else if (start.getPosY() == end.getPosY()) {
			if (start.getPosX() < end.getPosX()) {
				for (int i=end.getPosX(); i >= start.getPosX(); i--)
					mPosition.add(new BoardCell(i, start.getPosY()));
				
				mDirection = Direction.NORTH;
			} else {
				for (int i=start.getPosX(); i <= end.getPosX(); i++)
					mPosition.add(new BoardCell(i, start.getPosY()));
				
				mDirection = Direction.SOUTH;
			}
		}
		
		mPositioned = true;
	}
	
	//Helper method to see if a ship has already been positioned
	public boolean isPositioned() {
		return mPositioned;
	}

	public String getName() {
		return mName;
	}
	
	
}
