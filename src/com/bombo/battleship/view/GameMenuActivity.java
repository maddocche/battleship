package com.bombo.battleship.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.bombo.battleship.R;

public class GameMenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_menu);
		
		Button singlePlayerButton = (Button) findViewById(R.id.single_player_game);
		Button multiPlayerButton = (Button) findViewById(R.id.multi_player_game);
		
		singlePlayerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GamePreferencesActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				
				startActivity(intent);
			}
		});
		
		multiPlayerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), GamePreferencesActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return false;
	}

}
