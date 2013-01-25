package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ship implements Parcelable {

	protected boolean mPositioned;
	protected Direction mDirection;
	protected boolean mSelected;
	protected ShipType mShipType;
	protected int mStartX;
	protected int mStartY;
	protected int mHittedParts;
	protected boolean mSinked;
	
	public Ship(ShipType shipType) {
		mPositioned = false;
		mShipType = shipType;
		mStartX = 0;
		mStartY = 0;
		mHittedParts = 0;
		mSinked = false;
		
	}
	
	private Ship(Parcel in) {
		//...and then convert it back into a boolean
		mPositioned = (in.readByte() == 1);
		mDirection = ( Direction ) in.readSerializable();
		mSelected = ( in.readByte() == 1 );
		mShipType = ( ShipType ) in.readSerializable();
		mStartX = in.readInt();
		mStartY = in.readInt();
		mHittedParts = in.readInt();
		mSinked = ( in.readByte() == 1 );
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//To write a boolean in a parcel you have to convert it into a byte...
		dest.writeByte((byte) (mPositioned ? 1 : 0));
		dest.writeSerializable(mDirection);
		dest.writeByte((byte) (mSelected ? 1 : 0));
		dest.writeSerializable(mShipType);
		dest.writeInt( mStartX );
		dest.writeInt( mStartY );
		dest.writeInt( mHittedParts );
		dest.writeByte( ( byte ) ( mSinked ? 1 : 0 ) );
		
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
	
	//Helper method to set a ship position given start cell and direction
	public void setShipPosition(BoardCell start, Direction direction, Board board) {
		
		int size = mShipType.getSize();
		
		mDirection = direction;
		
		mStartX = start.getPosX();
		mStartY = start.getPosY();
		
		if (mDirection == null) {
			
			mPositioned = false;
		} else {
			
			mPositioned = true;

			switch (mDirection) {
			case NORTH:
				
				for (int i=start.getPosY(); i > (start.getPosY() - size); i--) 
					board.getBoardCellFromCoord(start.getPosX(), i).setShipOver( this );
				
				break;
				
			case EAST:
				
				for (int i=start.getPosX(); i < (start.getPosX() + size); i++) 
					board.getBoardCellFromCoord(i, start.getPosY()).setShipOver( this );
				
				break;
				
			case SOUTH:
				
				for (int i=start.getPosY(); i < (start.getPosY() + size); i++) 
					board.getBoardCellFromCoord(start.getPosX(), i).setShipOver( this );
				
				break;
				
			case WEST:
				
				for (int i=start.getPosX(); i > (start.getPosX() - size); i--) 
					board.getBoardCellFromCoord(i, start.getPosY()).setShipOver( this );
				
				break;
				
			default:
				
				break;
			}
		}
		
	}
	
	public void remove(Board board) {
		
		int size = mShipType.getSize();
		
		BoardCell start = new BoardCell(mStartX, mStartY);
		
		switch (mDirection) {
		case NORTH:
			
			for (int i=start.getPosY(); i > (start.getPosY() - size); i--) 
				board.getBoardCellFromCoord(start.getPosX(), i).free();
			
			break;
			
		case EAST:
			
			for (int i=start.getPosX(); i < (start.getPosX() + size); i++) 
				board.getBoardCellFromCoord(i, start.getPosY()).free();
			
			break;
			
		case SOUTH:
			
			for (int i=start.getPosY(); i < (start.getPosY() + size); i++) 
				board.getBoardCellFromCoord(start.getPosX(), i).free();
			
			break;
			
		case WEST:
			
			for (int i=start.getPosX(); i > (start.getPosX() - size); i--) 
				board.getBoardCellFromCoord(i, start.getPosY()).free();
			
			break;
			
		default:
			
			break;
		}
		
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

	public ShipType getShipType() {
		return mShipType;
	}

	public void setShipType(ShipType mShipType) {
		this.mShipType = mShipType;
	}
	
	public BoardCell getFirstCell() {
		return new BoardCell(mStartX, mStartY);
	}
	
	public Direction getDirection() {
		return mDirection;
	}
	
	public void hit() {
		
		mHittedParts++;
		
		if ( mHittedParts == mShipType.getSize() ) {
			
			mSinked = true;
		}
	}
	
	public boolean isSinked() {
		
		return mSinked;
	}
	
}
