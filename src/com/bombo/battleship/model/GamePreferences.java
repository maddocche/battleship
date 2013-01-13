package com.bombo.battleship.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GamePreferences implements Parcelable{
	
	public static final String GAME_PREFERENCES_TAG = "GamePreferences";
	
	public static final int MAX_GRID_SIZE = 20;
	public static final int MIN_GRID_SIZE = 10;
	public static final int MAX_AIRCRAFT_CARRIER_NUMBER = 5;
	public static final int MIN_AIRCRAFT_CARRIER_NUMBER = 1;
	public static final int MAX_BATTLESHIP_NUMBER = 5;
	public static final int MIN_BATTLESHIP_NUMBER = 1;
	public static final int MAX_SUBMARINE_NUMBER = 5;
	public static final int MIN_SUBMARINE_NUMBER = 1;
	public static final int MAX_DESTROYER_NUMBER = 5;
	public static final int MIN_DESTROYER_NUMBER = 1;
	public static final int MAX_PATROL_BOAT_NUMBER = 5;
	public static final int MIN_PATROL_BOAT_NUMBER = 1;
	
	public static final int DEFAULT_GRID_SIZE = 10;
	public static final int DEFAULT_AIRCRAFT_CARRIER_NUMBER = 1;
	public static final int DEFAULT_BATTLESHIP_NUMBER = 1;
	public static final int DEFAULT_SUBMARINE_NUMBER = 1;
	public static final int DEFAULT_DESTROYER_NUMBER = 1;
	public static final int DEFAULT_PATROL_BOAT_NUMBER = 1;
	
	private int mGridSize;
	private int mAircraftCarrierNumber;
	private int mBattleshipNumber;
	private int mSubmarineNumber;
	private int mDestroyerNumber;
	private int mPatrolBoatNumber;
	
	public GamePreferences () {
		mGridSize = DEFAULT_GRID_SIZE;
		mAircraftCarrierNumber = DEFAULT_AIRCRAFT_CARRIER_NUMBER;
		mBattleshipNumber = DEFAULT_BATTLESHIP_NUMBER;
		mSubmarineNumber = DEFAULT_SUBMARINE_NUMBER;
		mDestroyerNumber = DEFAULT_DESTROYER_NUMBER;
		mPatrolBoatNumber = DEFAULT_PATROL_BOAT_NUMBER;
		
	}
	
	private GamePreferences (Parcel in) {
		mGridSize = in.readInt();
		mAircraftCarrierNumber = in.readInt();
		mBattleshipNumber = in.readInt();
		mSubmarineNumber = in.readInt();
		mDestroyerNumber = in.readInt();
		mPatrolBoatNumber = in.readInt();
	}

	public int getGridSize() {
		return mGridSize;
	}

	public void setGridSize(int mGridSize) {
		this.mGridSize = mGridSize;
	}

	public int getAircraftCarrierNumber() {
		return mAircraftCarrierNumber;
	}

	public void setAircraftCarrierNumber(int mAircraftCarrierNumber) {
		this.mAircraftCarrierNumber = mAircraftCarrierNumber;
	}

	public int getBattleshipNumber() {
		return mBattleshipNumber;
	}

	public void setBattleshipNumber(int mBattleshipNumber) {
		this.mBattleshipNumber = mBattleshipNumber;
	}

	public int getSubmarineNumber() {
		return mSubmarineNumber;
	}

	public void setSubmarineNumber(int mSubmarineNumber) {
		this.mSubmarineNumber = mSubmarineNumber;
	}

	public int getDestroyerNumber() {
		return mDestroyerNumber;
	}

	public void setDestroyerNumber(int mDestroyerNumber) {
		this.mDestroyerNumber = mDestroyerNumber;
	}

	public int getPatrolBoatNumber() {
		return mPatrolBoatNumber;
	}

	public void setPatrolBoatNumber(int mPatrolBoatNumber) {
		this.mPatrolBoatNumber = mPatrolBoatNumber;
	}
	
	public void writeToParcel(Parcel out, int flags)  {
		out.writeInt(mGridSize);
		out.writeInt(mAircraftCarrierNumber);
		out.writeInt(mBattleshipNumber);
		out.writeInt(mSubmarineNumber);
		out.writeInt(mDestroyerNumber);
		out.writeInt(mPatrolBoatNumber);
	}
	
	public static final Parcelable.Creator<GamePreferences> CREATOR
		= new Parcelable.Creator<GamePreferences>() {
			
			public GamePreferences createFromParcel (Parcel in) {
				return new GamePreferences(in);
			}
			
			public GamePreferences[] newArray(int size) {
				return new GamePreferences[size];
			}
		};
		
	public int describeContents() {
		return 0;
	}
	
}
