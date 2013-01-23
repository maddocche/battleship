package com.bombo.battleship.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Board;
import com.bombo.battleship.model.BoardCell;
import com.bombo.battleship.model.Direction;
import com.bombo.battleship.model.GamePreferences;
import com.bombo.battleship.model.Ship;
import com.bombo.battleship.model.ShipConfiguration;
import com.bombo.battleship.util.ConfigurationDragHelper;
import com.bombo.battleship.util.Utilities;

public class ShipConfigurationActivity extends Activity {

	public static final String SELECTED_SHIP = "SelectedShip";
	public static final String LOG_TAG = "Ship_conf";
	public static final String PLAYER_CONFIGURATION = "Player_configuration";
	public static final String OPPONENT_CONFIGURATION = "Opponent_configuration";
	
	protected GamePreferences mGamePreferences;
	protected ShipConfiguration mShipConfiguration;
	protected Board mBoard;
	protected Ship mSelectedShip;
	protected ShipsListAdapter mShipsListAdapter;
	protected BoardAdapter mBoardAdapter;
	protected boolean isRecreated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ship_configuration);
		
		Log.d( LOG_TAG, "Creating activity..." );
		
		//If activity is being recreated due to a configuration change, restore saved game preferences
		//and ship configuration
		if (savedInstanceState != null) {
			
			Log.d( LOG_TAG, "...restoring..." );
			
			mGamePreferences = savedInstanceState.getParcelable( GamePreferences.GAME_PREFERENCES_TAG );
			mBoard = savedInstanceState.getParcelable( Board.BOARD_TAG );
			mShipConfiguration = mBoard.getShipConfiguration();
			mSelectedShip = savedInstanceState.getParcelable( SELECTED_SHIP );
			
			isRecreated = true;
		} else {
			
			Log.d( LOG_TAG, "...first time..." );
			
			//In case is the first time activity is launched
			mGamePreferences = getIntent().getExtras().getParcelable(GamePreferences.GAME_PREFERENCES_TAG);
			mShipConfiguration = new ShipConfiguration();
			mBoard = new Board( mGamePreferences.getGridSize(), mShipConfiguration );
			mShipConfiguration.readShipsPreference(mGamePreferences);
			
			isRecreated = false;
		}
		
		TableLayout tableLayout = (TableLayout) findViewById(R.id.configuration_board);
		
		mBoardAdapter = new BoardAdapter(this, mBoard, tableLayout);
		mBoardAdapter.generateBoardView();
		
		setActionListener();
		
		ListView shipsList = (ListView) findViewById(R.id.configuration_ship_list);
		mShipsListAdapter = new ShipsListAdapter(getApplication(), this);
		shipsList.setAdapter(mShipsListAdapter);
		mShipsListAdapter.addAllShips(mShipConfiguration.getShips());
		shipsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if (isRecreated) {
			
			mBoardAdapter.redrawPositionedShips(mShipConfiguration);
			
			if ( mShipConfiguration.areAllShipsPositioned() )
				enableStartGameButton();
		}
		
		//Setting listener for ship list
		shipsList.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Ship selectedShip = (Ship) parent.getItemAtPosition(position);
				
				if (selectedShip.isSelected()) {
					
					selectedShip.deselect();
					mSelectedShip = null;
					notifyShipsListChanged();
				} else if (!selectedShip.isPositioned()) {
					
					if (mSelectedShip != null )
						mSelectedShip.deselect();
					
					selectedShip.select();
					mSelectedShip = selectedShip;
					notifyShipsListChanged();
				}
				
			}
		});
		
		Button startGame = (Button) findViewById(R.id.configuration_end);
		startGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ShipConfiguration opponentConfiguration = Utilities.generateRandomConfiguration( mGamePreferences );
				
				Intent intent = new Intent(getApplicationContext(), GameplayActivity.class);
				Bundle extras = new Bundle();
				
				extras.putParcelable( GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences );
				extras.putParcelable( PLAYER_CONFIGURATION, mShipConfiguration );
				extras.putParcelable( OPPONENT_CONFIGURATION, opponentConfiguration );
				
				intent.putExtras( extras );
				
				
				startActivity(intent);
				
				finish();
			}
		});
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		Log.d(LOG_TAG, "Saving instance state...");
		
		outState.putParcelable(GamePreferences.GAME_PREFERENCES_TAG, mGamePreferences);
		outState.putParcelable(ShipConfiguration.SHIP_CONFIGURATION_TAG, mShipConfiguration);
		outState.putParcelable( Board.BOARD_TAG, mBoard );
		
		outState.putParcelable(SELECTED_SHIP, mSelectedShip);
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
			
			mBoardAdapter.drawChoiceStatus(mSelectedShip.getShipType(), dragHelper.getStartCell()
					, dragHelper.getDirection(), true);
			
			mBoard.putShip(mSelectedShip, dragHelper.getStartCell()
					, dragHelper.getDirection());
			
			if ( mShipConfiguration.areAllShipsPositioned() ) {
				
				enableStartGameButton();
			} 
			
			if (mSelectedShip.isPositioned()) {
				mSelectedShip.deselect();
				mSelectedShip = null;
				notifyShipsListChanged();
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
						
						mBoardAdapter.drawChoiceStatus(mSelectedShip.getShipType(), boardCell, null, false);
						
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
							
							mBoardAdapter.drawChoiceStatus(mSelectedShip.getShipType(), start
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

	public void notifyShipsListChanged() {
		mShipsListAdapter.notifyDataSetChanged();
	}
	
	public void enableStartGameButton() {

		Button endConfiguration = (Button) findViewById( R.id.configuration_end );
		
		endConfiguration.setEnabled( true );
	}
	
	public void disableStartGameButton() {

		Button endConfiguration = (Button) findViewById( R.id.configuration_end );
		
		endConfiguration.setEnabled( false );
	}

	public Board getBoard() {
		return mBoard;
	}
	
	public BoardAdapter getBoardAdapter() {
		return mBoardAdapter;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		mBoard = null;
		mSelectedShip = null;
		mGamePreferences = null;
		mShipConfiguration = null;
		mShipsListAdapter = null;
	}
	
}
