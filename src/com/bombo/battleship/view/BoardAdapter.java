package com.bombo.battleship.view;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.CellDrawType;
import com.bombo.battleship.model.Direction;
import com.bombo.battleship.model.Ship;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.model.ShipType;
import com.bombo.battleship.util.ConfigurationDragHelper;
import com.bombo.battleship.util.Utilities;

public class BoardAdapter {

	protected Board mBoard;
	protected Activity mParent;
	protected TableLayout mBoardView;
	
	public BoardAdapter( Activity parent, Board board, TableLayout boardView ) {
		
		mBoard = board;
		mParent = parent;
		mBoardView = boardView;
	}
	
	public Board getBoard() {
		return mBoard;
	}
	
	//Dinamically generate the content of the table layout based on the chosen grid size
	public void generateBoardView() {
		
		Utilities util = Utilities.getInstance();
		
		TableLayout.LayoutParams tableParams = 
				new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		
		TableRow.LayoutParams rowParams = 
				new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		
		TableRow tableRow;
		ImageView imageView;
		TextView textView;
		
		int uniqueID = 0;
		
		for (int y=0; y < mBoard.getGridSize() + 1; y++) {
			
			tableRow = new TableRow( mParent.getApplication() );
			uniqueID = util.getNextId( mParent );
			tableRow.setId(uniqueID);
			mBoardView.addView(tableRow, tableParams);

			//First row will contain numbers, to identify the column
			if (y == 0) {
				
				for (int j=0; j < mBoard.getGridSize() + 1; j++) {
					textView = new TextView( mParent.getApplication() );
					
					if (j == 0) {
						textView.setText("");
					} else {
						textView.setText(Integer.toString(j));
					}
					
					uniqueID = util.getNextId( mParent );
					textView.setId(uniqueID);
					tableRow.addView(textView, rowParams);
				}
			} else {
				
				for (int x=0; x < mBoard.getGridSize() + 1; x++) {
					
					//First column of every row will contain a letter, to identify the row
					if (x == 0) {
						textView = new TextView( mParent.getApplication() );
						textView.setText(Utilities.convertItoC(y));						
						uniqueID = util.getNextId( mParent );
						textView.setId(uniqueID);
						tableRow.addView(textView, rowParams);
					} else {
						imageView = new ImageView( mParent.getApplication() );
						imageView.setImageResource(R.drawable.blue_square);
						uniqueID = util.getNextId( mParent );
						imageView.setId(uniqueID);
						tableRow.addView(imageView, rowParams);
						mBoard.mapCellID( uniqueID, x, y );
					}
					
				}
			}
		}
	}
	
	public void drawChoiceStatus(ShipType ship, BoardCell start, Direction direction, boolean isKeyUp) {
		
		ConfigurationDragHelper dragHelper = ConfigurationDragHelper.getInstance();
		
		boolean ignoreFirstCell = false;
		
		if (mBoard.isValidDirection(start, Direction.NORTH, ship)) {
			
			if (Direction.NORTH == direction) {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.NORTH, CellDrawType.SHIP_OVER, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.NORTH, CellDrawType.CURRENT_CHOICE, ignoreFirstCell);
				ignoreFirstCell = true;
				dragHelper.setPositionChosen(true);
			} else {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.NORTH, CellDrawType.VOID, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.NORTH, CellDrawType.POSSIBLE_CHOICE, ignoreFirstCell);
			}
				
		}
		
		if (mBoard.isValidDirection(start, Direction.EAST, ship)) {
			
			if (Direction.EAST == direction) {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.EAST, CellDrawType.SHIP_OVER, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.EAST, CellDrawType.CURRENT_CHOICE, ignoreFirstCell);
				ignoreFirstCell = true;
				dragHelper.setPositionChosen(true);
			} else {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.EAST, CellDrawType.VOID, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.EAST, CellDrawType.POSSIBLE_CHOICE, ignoreFirstCell);
			}
		}
		
		if (mBoard.isValidDirection(start, Direction.SOUTH, ship)) {
			
			if (Direction.SOUTH == direction) {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.SOUTH, CellDrawType.SHIP_OVER, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.SOUTH, CellDrawType.CURRENT_CHOICE, ignoreFirstCell);
				ignoreFirstCell = true;
				dragHelper.setPositionChosen(true);
			} else {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.SOUTH, CellDrawType.VOID, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.SOUTH, CellDrawType.POSSIBLE_CHOICE, ignoreFirstCell);
			}
		}
		
		if (mBoard.isValidDirection(start, Direction.WEST, ship)) {
			
			if (Direction.WEST == direction) {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.WEST, CellDrawType.SHIP_OVER, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.WEST, CellDrawType.CURRENT_CHOICE, ignoreFirstCell);
				ignoreFirstCell = true;
				dragHelper.setPositionChosen(true);
			} else {
				
				if (isKeyUp)
					drawShipPosition(ship, start, Direction.WEST, CellDrawType.VOID, ignoreFirstCell);
				else
					drawShipPosition(ship, start, Direction.WEST, CellDrawType.POSSIBLE_CHOICE, ignoreFirstCell);
			}
		}
		
	}
	
	public void drawShipPosition(ShipType ship, BoardCell start,
			Direction direction, CellDrawType cellDrawType, boolean ignoreFirstCell) {

		ImageView v;
		int startIndex;
		int endIndex;
		int i;
		int drawableType;
		
		switch (cellDrawType) {
		case VOID:
			drawableType = R.drawable.blue_square;
		break;
		case POSSIBLE_CHOICE:
			drawableType = R.drawable.light_blue_square;
		break;
		case CURRENT_CHOICE:
			drawableType = R.drawable.yellow_square;
		break;
		case SHIP_OVER:
			drawableType = R.drawable.grey_square;
		break;
		default:
			drawableType = 0;
		break;
		}
		
		
		switch (direction) {
		case NORTH:
		
			startIndex = start.getPosY();
			endIndex = Utilities.getEndCoord(start, Direction.NORTH, ship);
			
			if (ignoreFirstCell)
				startIndex--;
			
			for (i = startIndex; i >= endIndex; i-- ) {
			
				v = (ImageView) mParent.findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				v.setImageDrawable(mParent.getResources().getDrawable(drawableType));
			}
		
		break;
		
		case EAST:

			startIndex = start.getPosX();
			endIndex = Utilities.getEndCoord(start, Direction.EAST, ship);

			if (ignoreFirstCell)
				startIndex++;

			for (i = startIndex; i <= endIndex; i++ ) {

				v = (ImageView) mParent.findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				v.setImageDrawable(mParent.getResources().getDrawable(drawableType));
			}

			break;

		case SOUTH:

			startIndex = start.getPosY();
			endIndex = Utilities.getEndCoord(start, Direction.SOUTH, ship);

			if (ignoreFirstCell)
				startIndex++;

			for (i = startIndex; i <= endIndex; i++ ) {

				v = (ImageView) mParent.findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				v.setImageDrawable(mParent.getResources().getDrawable(drawableType));
			}

			break;

		case WEST:

			startIndex = start.getPosX();
			endIndex = Utilities.getEndCoord(start, Direction.WEST, ship);

			if (ignoreFirstCell)
				startIndex--;

			for (i = startIndex; i >= endIndex; i-- ) {

				v = (ImageView) mParent.findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				v.setImageDrawable(mParent.getResources().getDrawable(drawableType));
			}

			break;
		default:
			break;
		}
	}	

	public void redrawPositionedShips(ShipConfiguration shipConfiguration) {
		
		for (Ship ship : shipConfiguration.getShips()) {
			
			if (ship.isPositioned()) {
				
				drawShipPosition(ship.getShipType(), ship.getFirstCell(), ship.getDirection()
						, CellDrawType.SHIP_OVER, false);
			}
		}
	}
	
}
