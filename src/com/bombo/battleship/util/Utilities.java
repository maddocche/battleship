package com.bombo.battleship.util;

import android.app.Activity;

public class Utilities {
	
	private static Object mLock;
	private static Utilities mInstance;
	private int mUniqueID;
	
	private Utilities() {
		
		mUniqueID = Integer.MAX_VALUE;
	}
	
	public static Utilities getInstance() {
		
		mLock = new Object();
		
		synchronized (mLock) {
			
			if (mInstance == null) {
				mInstance = new Utilities();
			}
			
			return mInstance;
		}
	}
	
	public int getNextId(Activity parent) {
		
		if (mUniqueID <= 0) {
			mUniqueID = Integer.MAX_VALUE;
		}
		
		do {
			
			--mUniqueID;
		} while (parent.findViewById(mUniqueID) != null);
		
		return mUniqueID;
	}
	
	public static String convertItoC(int i) {
		
		switch (i) {
		case 0:
			
			return " ";
		case 1:
			
			return "A";
		case 2:
			
			return "B";
		case 3:
			
			return "C";
		case 4:
			
			return "D";
		case 5:
			
			return "E";
		case 6:
			
			return "F";
		case 7:
			
			return "G";
		case 8:
			
			return "H";
		case 9:
			
			return "I";
		case 10:
			
			return "J";
		case 11:
			
			return "K";
		case 12:
			
			return "L";
		case 13:
			
			return "M";
		case 14:
			
			return "N";
		case 15:
			
			return "O";
		case 16:
			
			return "P";
		case 17:
			
			return "Q";
		case 18:
			
			return "R";
		case 19:
			
			return "S";
		case 20:
			
			return "T";

		default:
			break;
		}
		
		return "@";
	}
}
