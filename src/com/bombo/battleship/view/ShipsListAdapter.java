package com.bombo.battleship.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Ship;

public class ShipsListAdapter extends ArrayAdapter<Ship> {
	
	private LayoutInflater mInflater;
	private Context mCtx;
	private ShipConfigurationActivity mParent;
	
	public ShipsListAdapter(Context ctx, ShipConfigurationActivity parent) {
		super(ctx, R.layout.configuration_ship_item);
		
		mCtx = ctx;
		mParent = parent;
	}

	public void addAllShips( Ship[] collection ) {
		
		for (Ship ship: collection) {
			this.add(ship);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		mInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View v;
		Ship ship = getItem(position);
		TextView textView;

		if (ship.isPositioned()) {
			
			v = mInflater.inflate(R.layout.configuration_ship_item_positioned, null);
			textView = (TextView) v.findViewById(R.id.positioned_item_text);
			
			Button button = (Button) v.findViewById(R.id.positioned_item_button);
			button.setOnClickListener(new RemoveShipOnClickListener(mParent, ship));
			
		} else {
			
			if (ship.isSelected()) {
				v = mInflater.inflate(R.layout.configuration_ship_item_selected, null);
				textView = (TextView) v.findViewById(R.id.configuration_ship_item_selected);
			} else {
				v = mInflater.inflate(R.layout.configuration_ship_item, null);
				textView = (TextView) v.findViewById(R.id.configuration_ship_item);
			} 
		}
		
		textView.setText(getItem(position).getName());
		
		return v;
	}

}




















