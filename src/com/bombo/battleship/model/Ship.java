package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ship implements Parcelable {

	protected BoardCell[] mPosition;
	protected int mSize;
	protected int mType;
	protected String mName;
	protected boolean mPositioned;
	protected Direction mDirection;
	protected boolean mSelected;
	protected ShipType mShipType;
	protected int mStartX;
	protected int mStartY;
	
	public Ship(ShipType shipType) {
		mSize = shipType.getSize();
		mType = shipType.getType();
		mName = shipType.getName();
		mPositioned = false;
		mShipType = shipType;
		mStartX = 0;
		mStartY = 0;
		
	}
	
	private Ship(Parcel in) {
		//...and then convert it back into a boolean
		mPositioned = (in.readByte() == 1);
		mSize = in.readInt();
		mType = in.readInt();
		mName = in.readString();
		mDirection = (Direction) in.readSerializable();
		mSelected = (in.readByte() == 1);
		mShipType = (ShipType) in.readSerializable();
		mStartX = in.readInt();
		mStartY = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//To write a boolean in a parcel you have to convert it into a byte...
		dest.writeByte((byte) (mPositioned ? 1 : 0));
		dest.writeInt(mSize);
		dest.writeInt(mType);
		dest.writeString(mName);
		dest.writeSerializable(mDirection);
		dest.writeByte((byte) (mSelected ? 1 : 0));
		dest.writeSerializable(mShipType);
		dest.writeInt( mStartX );
		dest.writeInt( mStartY );
		
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
	
	//Called after restoring the activity to avoid StackOverflow due to circular reference between Ship and BoardCell
	public void restoreShipPosition( Board board ) {
		
		setShipPosition( new BoardCell( mStartX, mStartY ), mDirection, board);
		
	}
	
	//Helper method to get all the board cells occupied by a ship given start cell and direction
	public void setShipPosition(BoardCell start, Direction direction, Board board) {
		
		mDirection = direction;
		int ix = 0;
		mStartX = start.getPosX();
		mStartY = start.getPosY();
		
		if (mDirection == null) {
			
			mPositioned = false;
		} else {
			
			mPositioned = true;
			//mPosition = new ArrayList<BoardCell>();
			mPosition = new BoardCell[ mShipType.getSize() ];
			
			switch (mDirection) {
			case NORTH:
				
				for (int i=start.getPosY(); i > (start.getPosY() - mSize); i--) {
					
					mPosition[ ix ] = board.getBoardCellFromCoord(start.getPosX(), i);
					mPosition[ ix++ ].setShipOver(this);
				}
				
				break;
				
			case EAST:
				
				for (int i=start.getPosX(); i < (start.getPosX() + mSize); i++) {
					
					mPosition[ ix ] = board.getBoardCellFromCoord(i, start.getPosY());
					mPosition[ ix++ ].setShipOver(this);
				}
				
				break;
				
			case SOUTH:
				
				for (int i=start.getPosY(); i < (start.getPosY() + mSize); i++) {
					
					mPosition[ ix ] = board.getBoardCellFromCoord(start.getPosX(), i);
					mPosition[ ix++ ].setShipOver(this);
				}
				
				break;
				
			case WEST:
				
				for (int i=start.getPosX(); i > (start.getPosX() - mSize); i--) {
					
					mPosition[ ix ] = board.getBoardCellFromCoord(i, start.getPosY());
					mPosition[ ix++ ].setShipOver(this);
				}
				
				break;
				
			default:
				
				break;
			}
		}
		
	}
	
	public void remove() {
		
		for (BoardCell cell : mPosition) {
			cell.free();
		}
		
		mPosition = null;
		mPositioned = false;
	}
	
	//Helper method to see if a ship has already been positioned
	public boolean isPositioned() {
		return mPositioned;
	}
	
	public boolean isSelected() {
		return mSelected;
	}
	
	public void select() {
		mSelected = true;
	}
	
	public void deselect() {
		mSelected = false;
	}

	public String getName() {
		return mName;
	}

	public ShipType getShipType() {
		return mShipType;
	}

	public void setShipType(ShipType mShipType) {
		this.mShipType = mShipType;
	}
	
	public BoardCell getFirstCell() {
		return mPosition[ 0 ];
	}
	
	public Direction getDirection() {
		return mDirection;
	}
	
	
}
