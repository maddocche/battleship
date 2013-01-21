package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShipConfiguration implements Parcelable {

	public static final String SHIP_CONFIGURATION_TAG = "ShipConfiguration";
	
	protected Ship[] mShips;
	protected int mShipsNumber;
	protected int mPositionedShips;
	protected boolean mAllShipsPositioned;
	
	
	public ShipConfiguration() {
		mShipsNumber = 0;
		mPositionedShips = 0;
		mShips = null;
		mAllShipsPositioned = false;
	}
	
	private ShipConfiguration(Parcel in) {
		mShipsNumber = in.readInt();
		mPositionedShips = in.readInt();
		//in.readList(mShips, Ship.class.getClassLoader());
		mShips = ( Ship[] ) in.readArray(null);
		mAllShipsPositioned = (in.readByte() == 1);
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mShipsNumber);
		dest.writeInt(mPositionedShips);
		//dest.writeList(mShips);
		dest.writeArray(mShips);
		dest.writeByte((byte) (mAllShipsPositioned ? 1 : 0));
	}
	
	public static final Parcelable.Creator<ShipConfiguration> CREATOR
		= new Parcelable.Creator<ShipConfiguration>() {
	
			public ShipConfiguration createFromParcel (Parcel in) {
				return new ShipConfiguration(in);
			}
	
			public ShipConfiguration[] newArray(int size) {
				return new ShipConfiguration[size];
			}
		};

	@Override
	public int describeContents() {
		return 0;
	}
	
	//Helper method to insert non-positioned ships into the configuration from the preferences
	public void readShipsPreference(GamePreferences gamePreferences) {
		
		mShips = new Ship[ gamePreferences.getTotalShips() ];
		int ix = 0;
		
		for (int i = 0; i < gamePreferences.getAircraftCarrierNumber(); i++) {
			mShips[ ix++ ] = new Ship(ShipType.AIRCRAFT_CARRIER);
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getBattleshipNumber(); i++) {
			mShips[ ix++ ] = new Ship(ShipType.BATTLESHIP);
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getSubmarineNumber(); i++) {
			mShips[ ix++ ] = new Ship(ShipType.SUBMARINE);
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getDestroyerNumber(); i++) {
			mShips[ ix++ ] = new Ship(ShipType.DESTROYER);
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getPatrolBoatNumber(); i++) {
			mShips[ ix++ ] = new Ship(ShipType.PATROL_BOAT);
			++mShipsNumber;
		}
	}

	public Ship[] getShips() {
		return mShips;
	}

	public void setShips( Ship[] mShips ) {
		this.mShips = mShips;
	}
	
	public void putShipOnBoard(Ship ship, BoardCell start, Direction direction, Board board) {
		
		ship.setShipPosition(start, direction, board);
		mPositionedShips++;
		
		if (mPositionedShips == mShipsNumber) 
			mAllShipsPositioned = true;
		
	}
	
	public void removeShipFromBoard(Ship ship) {
		
		ship.remove();
		mPositionedShips--;
		
		if (mPositionedShips != mShipsNumber)
			mAllShipsPositioned = false;
	}

	public boolean areAllShipsPositioned() {
		return mAllShipsPositioned;
	}

	public void setAllShipsPositioned(boolean mAllShipsPositioned) {
		this.mAllShipsPositioned = mAllShipsPositioned;
	}
	
}
