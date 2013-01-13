package com.bombo.battleship.model;

public enum ShipType {
	
	AIRCRAFT_CARRIER(1,5,"Aircraft Carrier"), 
	BATTLESHIP(2,4,"Battleship"),
	SUBMARINE(3,3,"Submarine"), 
	DESTROYER(4,3,"Destroyer"), 
	PATROL_BOAT(5,2,"Patrol Boat");
	
	private final int mType;
	private final int mSize;
	private final String mName;
	
	ShipType(int type, int size, String name) {
		mType = type;
		mSize = size;
		mName = name;
	}

	public int getType() {
		return mType;
	}

	public int getSize() {
		return mSize;
	}
	
	public String getName() {
		return mName;
	}
}
