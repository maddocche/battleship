package com.bombo.battleship.view;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.bombo.battleship.R;
import com.bombo.battleship.model.Board;


public class BoardFragment extends Fragment {
	
	Board mBoard;
	String mBoardType;
	View mView;
	GameControllerCallback mGameController;
	
	public interface GameControllerCallback {
		
		public void sendPlayerShot( ImageView v );
	}

	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		
		mBoard = savedInstanceState.getParcelable( Board.BOARD_TAG );
		mBoardType = savedInstanceState.getString( Board.BOARD_TYPE );
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		mGameController = (GameControllerCallback) activity;
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState ) {
		
		mView = inflater.inflate( R.layout.fragment_board, container );
		
		TableLayout board = (TableLayout) mView.findViewById( R.id.board );
		
		BoardDrawHelper drawHelper = new BoardDrawHelper( getActivity(), mBoard, board );
		
		drawHelper.generateBoardView();
		drawHelper.redrawEntireBoard( mBoardType );
		
		if ( mBoardType == Board.OPPONENT_BOARD ) {
			
			setBoardListener();
		}

		return mView;
	}
	
	public void setBoardListener() {
		
		ImageView v;
		
		for ( int id : mBoard.getIDs() ) {
			
			v = ( ImageView ) mView.findViewById( id );
			v.setOnClickListener( new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					mGameController.sendPlayerShot( ( ImageView ) v );
				}
			} );
			
		}
		
	}

}
