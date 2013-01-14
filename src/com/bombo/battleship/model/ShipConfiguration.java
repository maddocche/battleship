package com.bombo.battleship.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class ShipConfiguration implements Parcelable {

	public static final String SHIP_CONFIGURATION_TAG = "ShipConfiguration";
	
	private List<Ship> mShips;
	private int mShipsNumber;
	private int mPositionedShips;
	
	public ShipConfiguration() {
		mShipsNumber = 0;
		mPositionedShips = 0;
		mShips = new ArrayList<Ship>();
	}
	
	private ShipConfiguration(Parcel in) {
		mShipsNumber = in.readInt();
		mPositionedShips = in.readInt();
		in.readList(mShips, Ship.class.getClassLoader());
		
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mShipsNumber);
		dest.writeInt(mPositionedShips);
		dest.writeList(mShips);
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
		
		for (int i = 0; i < gamePreferences.getAircraftCarrierNumber(); i++) {
			mShips.add(new Ship(ShipType.AIRCRAFT_CARRIER));
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getBattleshipNumber(); i++) {
			mShips.add(new Ship(ShipType.BATTLESHIP));
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getSubmarineNumber(); i++) {
			mShips.add(new Ship(ShipType.SUBMARINE));
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getDestroyerNumber(); i++) {
			mShips.add(new Ship(ShipType.DESTROYER));
			++mShipsNumber;
		}
		
		for (int i = 0; i < gamePreferences.getPatrolBoatNumber(); i++) {
			mShips.add(new Ship(ShipType.PATROL_BOAT));
			++mShipsNumber;
		}
	}

	public List<Ship> getShips() {
		return mShips;
	}

	public void setShips(List<Ship> mShips) {
		this.mShips = mShips;
	}
	
}
