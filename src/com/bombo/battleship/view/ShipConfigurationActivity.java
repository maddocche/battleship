package com.bombo.battleship.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.Direction;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.model.ShipType;
import com.bombo.battleship.util.ConfigurationDragHelper;
import com.bombo.battleship.util.Utilities;

public class ShipConfigurationActivity extends Activity {

	private GamePreferences mGamePreferences;
	private ShipConfiguration mShipConfiguration;
	private Board mBoard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ship_configuration);
		
		
		//If activity is being recreated due to a configuration change, restore saved game preferences
		//and ship configuration
		if (savedInstanceState != null) {
			mGamePreferences = savedInstanceState.getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
			mShipConfiguration = savedInstanceState.getParcelable(ShipConfiguration.SHIP_CONFIGURATION_TAG);
			mBoard = savedInstanceState.getParcelable(Board.BOARD_TAG);
			//Reset board IDs due to recreation of all the cells
			mBoard.resetBoardIDs();
		} else {
			//In case is the first time activity is launched
			mShipConfiguration = new ShipConfiguration();
			mGamePreferences = getIntent().getExtras().getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
			mBoard = new Board(mGamePreferences.getGridSize());
		}
		
		mShipConfiguration.readShipsPreference(mGamePreferences);
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.configuration_board);
		
		generateConfigurationBoard(tableLayout, mGamePreferences.getGridSize());
		setBoardListeners();
		
		ListView shipsList = (ListView) findViewById(R.id.configuration_ship_list);
		ShipsListAdapter shipsListAdapter = new ShipsListAdapter(getApplication());
		
		shipsList.setAdapter(shipsListAdapter);
		shipsListAdapter.addAll(mShipConfiguration.getShips());
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelable(GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences);
		outState.putParcelable(ShipConfiguration.SHIP_CONFIGURATION_TAG, mShipConfiguration);
		outState.putParcelable(Board.BOARD_TAG, mBoard);
	}
	
	//Dinamically generate the content of the table layout based on the chosen grid size
	public void generateConfigurationBoard(TableLayout configurationBoard, int gridSize) {

		Utilities util = Utilities.getInstance();
		
		TableLayout.LayoutParams tableParams = 
				new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
		
		TableRow.LayoutParams rowParams = 
				new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		
		TableRow tableRow;
		ImageView imageView;
		TextView textView;
		
		int uniqueID = 0;
		
		for (int y=0; y < gridSize + 1; y++) {
			
			tableRow = new TableRow(getApplication());
			uniqueID = util.getNextId(this);
			tableRow.setId(uniqueID);
			configurationBoard.addView(tableRow, tableParams);

			//First row will contain numbers, to identify the column
			if (y == 0) {
				
				for (int j=0; j < gridSize + 1; j++) {
					textView = new TextView(getApplication());
					
					if (j == 0) {
						textView.setText("");
					} else {
						textView.setText(Integer.toString(j));
					}
					
					uniqueID = util.getNextId(this);
					textView.setId(uniqueID);
					tableRow.addView(textView, rowParams);
				}
			} else {
				
				for (int x=0; x < gridSize + 1; x++) {
					
					//First column of every row will contain a letter, to identify the row
					if (x == 0) {
						textView = new TextView(getApplication());
						textView.setText(Utilities.convertItoC(y));						
						uniqueID = util.getNextId(this);
						textView.setId(uniqueID);
						tableRow.addView(textView, rowParams);
					} else {
						imageView = new ImageView(getApplication());
						imageView.setImageResource(R.drawable.blue_square);
						uniqueID = util.getNextId(this);
						imageView.setId(uniqueID);
						tableRow.addView(imageView, rowParams);
						mBoard.addBoardCell(uniqueID, x, y);
					}
					
				}
			}
		}
	}
	
	//Sets cells listeners for handling ship position by drag and drop operation
	public void setBoardListeners() {
		
		TableLayout table = (TableLayout) findViewById(R.id.configuration_board);
		table.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_MOVE:
					
					dispatchEventToBoard(event);
					
					break;

				default:
					break;
				}
				
				return true;
			}
		});
	}
		
		
	public void dispatchEventToBoard(MotionEvent event) {
		
		ImageView cell;
		
		for (int cellID : mBoard.getIDs()) {
			cell = (ImageView) findViewById(cellID);

			TableRow tableRow = (TableRow) cell.getParent();
			
			int pointerX = (int) event.getX();
			int pointerY = (int) event.getY();
			int cellX = cell.getLeft();
			int cellY = tableRow.getTop();
			int cellWidth = cell.getWidth();
			int cellHeight = cell.getHeight();
			
			if (cellX < pointerX && pointerX < (cellX + cellWidth) && 
				cellY < pointerY && pointerY < (cellY + cellHeight)) {
				
				BoardCell test = mBoard.getBoardCellFromId(cellID);
				ConfigurationDragHelper dragHelper = ConfigurationDragHelper.getInstance();
				Log.d("DragOperation", "Pos x: " + Integer.toString(test.getPosX()) + "- Pos y: "  
						+ Integer.toString(test.getPosY()));
				
				
				switch (event.getActionMasked()) {
				//When a cell is pressed start drag process, using this cell as start position
				case MotionEvent.ACTION_DOWN:
					
					BoardCell boardCell = mBoard.getBoardCellFromId(cellID);
					
					if (!dragHelper.isDragStarted()) {
						dragHelper.startDrag(boardCell);
					}
					
					drawChoiceStatus(null, boardCell, null);
					
					break;
					
					//When the user release the event, end drag process
				case MotionEvent.ACTION_UP:
					
					if (dragHelper.isDragStarted()) {
						
						dragHelper.endDrag();
					}
					
					break;
					
					//When the user enter a new view recalculate chosen direction
				case MotionEvent.ACTION_MOVE:
					
					BoardCell start = dragHelper.getStartCell();
					BoardCell current = mBoard.getBoardCellFromId(cellID);
					
					if (cellID != dragHelper.getCurrentID()) {
						dragHelper.setCurrentID(cellID);
					}
					
					int offsetX = current.getPosX() - start.getPosX();
					int offsetY = current.getPosY() - start.getPosY();
					
					if (offsetX != offsetY) {
						if (Math.abs(offsetX) < Math.abs(offsetY)) {
							if (offsetY > 0) {
								dragHelper.setDirection(Direction.SOUTH);
							} else {
								dragHelper.setDirection(Direction.NORTH);
							}
						} else {
							if (offsetX > 0) {
								dragHelper.setDirection(Direction.EAST);
							} else {
								dragHelper.setDirection(Direction.WEST);
							}
						}
					}
					
					drawChoiceStatus(null, start, dragHelper.getDirection());
					
				default:
					break;
				}
				
				return;
			}
			
		}
		
	}
	
	public void drawChoiceStatus(ShipType ship, BoardCell start, Direction direction) {
		
		//TODO Remove before releasing game; it's just a test
		if (ship == null) {
			ship = ShipType.AIRCRAFT_CARRIER;
		}
		
		int endNorthCell = start.getPosY() - ship.getSize() + 1;
		int endEastCell = start.getPosX() + ship.getSize() - 1;
		int endSouthCell = start.getPosY() + ship.getSize() - 1;
		int endWestCell = start.getPosX() - ship.getSize() + 1;
		
		if (mBoard.isValidPosition(start, new BoardCell(start.getPosX(), endNorthCell))) {
			drawShipPosition(ship, start, Direction.NORTH, (direction == Direction.NORTH));
		}
		
		if (mBoard.isValidPosition(start, new BoardCell(endEastCell, start.getPosY()))) {
			drawShipPosition(ship, start, Direction.EAST, (direction == Direction.EAST));
		}
		
		if (mBoard.isValidPosition(start, new BoardCell(start.getPosX(), endSouthCell))) {
			drawShipPosition(ship, start, Direction.SOUTH, (direction == Direction.SOUTH));
		}
		
		if (mBoard.isValidPosition(start, new BoardCell(endWestCell, start.getPosY()))) {
			drawShipPosition(ship, start, Direction.WEST, (direction == Direction.WEST));
		}
		
	}
	
	public void drawShipPosition(ShipType ship, BoardCell start, Direction direction, boolean chosen) {
		
		ImageView v;
		
		switch (direction) {
		case NORTH:
			
			for (int i = start.getPosY(); i > (start.getPosY() - ship.getSize()); i-- ) {
				v = (ImageView) findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				if (chosen) {
					v.setImageDrawable(getResources().getDrawable(R.drawable.yellow_square));
				} else {
					v.setImageDrawable(getResources().getDrawable(R.drawable.light_blue_square));
				}
			}
		
			break;

		case EAST:
			
			for (int i = start.getPosX(); i < (start.getPosX() + ship.getSize()); i++ ) {
				v = (ImageView) findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				if (chosen) {
					v.setImageDrawable(getResources().getDrawable(R.drawable.yellow_square));
				} else {
					v.setImageDrawable(getResources().getDrawable(R.drawable.light_blue_square));
				}
			}
		
			break;
			
		case SOUTH:
			
			for (int i = start.getPosY(); i < (start.getPosY() + ship.getSize()); i++ ) {
				v = (ImageView) findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				if (chosen) {
					v.setImageDrawable(getResources().getDrawable(R.drawable.yellow_square));
				} else {
					v.setImageDrawable(getResources().getDrawable(R.drawable.light_blue_square));
				}
			}
		
			break;

		case WEST:
			
			for (int i = start.getPosX(); i > (start.getPosX() - ship.getSize()); i-- ) {
				v = (ImageView) findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				if (chosen) {
					v.setImageDrawable(getResources().getDrawable(R.drawable.yellow_square));
				} else {
					v.setImageDrawable(getResources().getDrawable(R.drawable.light_blue_square));
				}
			}
		
			break;
		default:
			break;
		}
	}	
	
	
}
