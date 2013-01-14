package com.bombo.battleship.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.CellDrawType;
import com.bombo.battleship.model.Direction;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.Ship;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.model.ShipType;
import com.bombo.battleship.util.ConfigurationDragHelper;
import com.bombo.battleship.util.Utilities;

public class ShipConfigurationActivity extends Activity {

	public static final String SELECTED_SHIP = "SelectedShip";
	
	protected GamePreferences mGamePreferences;
	protected ShipConfiguration mShipConfiguration;
	protected Board mBoard;
	protected Ship mSelectedShip;
	protected ShipsListAdapter mShipsListAdapter;
	protected boolean isRecreated;
	
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
			mSelectedShip = savedInstanceState.getParcelable(SELECTED_SHIP);
			//Reset board IDs index due to recreation of all the cells
			mBoard.resetBoardIDsIndex();
			
			isRecreated = true;
		} else {
			//In case is the first time activity is launched
			mShipConfiguration = new ShipConfiguration();
			mGamePreferences = getIntent().getExtras().getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
			mBoard = new Board(mGamePreferences.getGridSize());
			mShipConfiguration.readShipsPreference(mGamePreferences);
			
			isRecreated = false;
		}
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.configuration_board);
		
		generateConfigurationBoard(tableLayout, mGamePreferences.getGridSize());
		setActionListener();
		
		ListView shipsList = (ListView) findViewById(R.id.configuration_ship_list);
		mShipsListAdapter = new ShipsListAdapter(getApplication(), this);
		shipsList.setAdapter(mShipsListAdapter);
		mShipsListAdapter.addAllShips(mShipConfiguration.getShips());
		shipsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if (isRecreated) {
			redrawPositionedShips();
		}
		
		//Setting listener for ship list
		shipsList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Ship selectedShip = (Ship) parent.getItemAtPosition(position);
				
				if (selectedShip.isSelected()) {
					
					selectedShip.deselect();
					mSelectedShip = null;
					notifyDataChanged();
				} else if (!selectedShip.isPositioned()) {
					
					if (mSelectedShip != null )
						mSelectedShip.deselect();
					
					selectedShip.select();
					mSelectedShip = selectedShip;
					notifyDataChanged();
				}
				
			}
		});
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putParcelable(GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences);
		outState.putParcelable(ShipConfiguration.SHIP_CONFIGURATION_TAG, mShipConfiguration);
		outState.putParcelable(Board.BOARD_TAG, mBoard);
		outState.putParcelable(SELECTED_SHIP, mSelectedShip);
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
	public void setActionListener() {
		
		RelativeLayout parent = (RelativeLayout) findViewById(R.id.parent_container);
		parent.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (mSelectedShip != null) {
					
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						
						dispatchEventToBoard(event);
						
						break;
						
					case MotionEvent.ACTION_MOVE:
					case MotionEvent.ACTION_UP:
						
						ConfigurationDragHelper dragHelper = ConfigurationDragHelper.getInstance();
						
						if (dragHelper.isDragStarted()) {
							
							dispatchEventToBoard(event);
						}
						
						break;
						
					default:
						break;
					}
				}
				
				return true;
			}
		});
	}
		
		
	public void dispatchEventToBoard(MotionEvent event) {
		
		if (event.getAction() == MotionEvent.ACTION_UP) {
			
			ConfigurationDragHelper dragHelper = ConfigurationDragHelper.getInstance();
			
			drawChoiceStatus(mSelectedShip.getShipType(), dragHelper.getStartCell()
					, dragHelper.getDirection(), true);
			
			mSelectedShip.setShipPosition(dragHelper.getStartCell(), dragHelper.getDirection(), mBoard);
			
			if (mSelectedShip.isPositioned()) {
				mSelectedShip.deselect();
				mSelectedShip = null;
				notifyDataChanged();
			}
			
			dragHelper.endDrag();
			
		} else {
			
			ImageView cell;
			
			int pointerX = (int) event.getX();
			int pointerY = (int) event.getY();
			
			for (int cellID : mBoard.getIDs()) {
				
				cell = (ImageView) findViewById(cellID);
				
				TableRow tableRow = (TableRow) cell.getParent();
				TableLayout tableLayout = (TableLayout) tableRow.getParent();
				
				int cellX = cell.getLeft() + tableLayout.getLeft();
				int cellY = tableRow.getTop() + tableLayout.getTop();
				int cellWidth = cell.getWidth();
				int cellHeight = cell.getHeight();
				
				if (cellX < pointerX && pointerX < (cellX + cellWidth) && 
					cellY < pointerY && pointerY < (cellY + cellHeight)) {
					
					ConfigurationDragHelper dragHelper = ConfigurationDragHelper.getInstance();
					
					switch (event.getActionMasked()) {
					//When a cell is pressed start drag process, using this cell as start position
					case MotionEvent.ACTION_DOWN:
						
						BoardCell boardCell = mBoard.getBoardCellFromId(cellID);
						
						dragHelper.startDrag(boardCell);
						dragHelper.setPositionChosen(false);
						
						drawChoiceStatus(mSelectedShip.getShipType(), boardCell, null, false);
						
						break;
						
					//When the user enter a new view recalculate chosen direction
					case MotionEvent.ACTION_MOVE:
						
						BoardCell start = dragHelper.getStartCell();
						BoardCell current = mBoard.getBoardCellFromId(cellID);
						
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
						
						//Reset direction if not valid
						if (dragHelper.getDirection() != null)
							if (!mBoard.isValidDirection(start, dragHelper.getDirection(), mSelectedShip.getShipType()))
								dragHelper.setDirection(null);
						
						//Redraw choice status only when direction changes, avoiding unnecessary redrawing
						if  (dragHelper.getDirection() != dragHelper.getPreviousDirection()) {
							
							dragHelper.setPreviousDirection(dragHelper.getDirection());
							dragHelper.setPositionChosen(false);
							
							drawChoiceStatus(mSelectedShip.getShipType(), start
									, dragHelper.getDirection(), false);
						}
						
						
					default:
						break;
					}
					
					return;
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
	
	public void drawShipPosition(ShipType ship, BoardCell start, Direction direction, 
									CellDrawType cellDrawType, boolean ignoreFirstCell) {
		
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
				
				v = (ImageView) findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				v.setImageDrawable(getResources().getDrawable(drawableType));
			}
		
			break;

		case EAST:
			
			startIndex = start.getPosX();
			endIndex = Utilities.getEndCoord(start, Direction.EAST, ship);

			if (ignoreFirstCell)
				startIndex++;
			
			for (i = startIndex; i <= endIndex; i++ ) {
				
				v = (ImageView) findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				v.setImageDrawable(getResources().getDrawable(drawableType));
			}
		
			break;
			
		case SOUTH:
			
			startIndex = start.getPosY();
			endIndex = Utilities.getEndCoord(start, Direction.SOUTH, ship);

			if (ignoreFirstCell)
				startIndex++;
			
			for (i = startIndex; i <= endIndex; i++ ) {
				
				v = (ImageView) findViewById(mBoard.getIdFromCoord(start.getPosX(), i));
				v.setImageDrawable(getResources().getDrawable(drawableType));
			}
		
			break;

		case WEST:
			
			startIndex = start.getPosX();
			endIndex = Utilities.getEndCoord(start, Direction.WEST, ship);

			if (ignoreFirstCell)
				startIndex--;
			
			for (i = startIndex; i >= endIndex; i-- ) {
				
				v = (ImageView) findViewById(mBoard.getIdFromCoord(i, start.getPosY()));
				v.setImageDrawable(getResources().getDrawable(drawableType));
			}
		
			break;
		default:
			break;
		}
	}	

	public void notifyDataChanged() {
		mShipsListAdapter.notifyDataSetChanged();
	}
	
	public void redrawPositionedShips() {
		
		for (Ship ship : mShipConfiguration.getShips()) {
			
			if (ship.isPositioned()) {
				
				drawShipPosition(ship.getShipType(), ship.getFirstCell(), ship.getDirection()
						, CellDrawType.SHIP_OVER, false);
			}
		}
	}
	
}
