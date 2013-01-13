package com.bombo.battleship.view;

import java.util.Collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Ship;

public class ShipsListAdapter extends ArrayAdapter<Ship> {
	
	private LayoutInflater mInflater;
	private Context mCtx;
	
	public ShipsListAdapter(Context ctx) {
		super(ctx, R.layout.configuration_ship_item);
		
		mCtx = ctx;
	}

	@Override
	public void addAll(Collection<? extends Ship> collection) {
		
		for (Ship ship: collection) {
			this.add(ship);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		mInflater = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View v;
		
		if (convertView == null) {
			v = mInflater.inflate(R.layout.configuration_ship_item, null);
		} else {
			v = convertView;
		}
		
		TextView textView = (TextView) v.findViewById(R.id.configuration_ship_item);
		textView.setText(getItem(position).getName());
		
		return v;
	}

}




















