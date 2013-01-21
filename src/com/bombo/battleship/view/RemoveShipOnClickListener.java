package com.bombo.battleship.view;

import android.view.View;
import android.view.View.OnClickListener;

import com.bombo.battleship.model.CellDrawType;
import com.bombo.battleship.model.Ship;

public class RemoveShipOnClickListener implements OnClickListener {
	
	protected Ship mShip;
	protected ShipConfigurationActivity mParent;
	
	public RemoveShipOnClickListener(ShipConfigurationActivity parent, Ship ship) {
		mShip = ship;
		mParent = parent;
	}

	@Override
	public void onClick(View v) {
		
		mParent.getBoardAdapter().drawShipPosition(mShip.getShipType(), mShip.getFirstCell()
				, mShip.getDirection(), CellDrawType.VOID, false);
		
		mParent.getShipConfiguration().removeShipFromBoard( mShip );
		
		mParent.disableStartGameButton();
		
		mParent.notifyDataChanged();
	}

}
