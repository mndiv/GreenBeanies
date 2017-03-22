package com.signmeastory.apps.gb;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class CoverLetter extends Activity implements OnClickListener {

	private String episode = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent i = getIntent();

		episode = i.getStringExtra("episode"); 
		if(episode.equals("gb1"))
		{
			setContentView(R.layout.activity_cover_letter_gb1);
		}
		else if (episode.equals("gb2"))
		{
			setContentView(R.layout.activity_cover_letter_gb2);
		}		

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);		

		Button btminus = (Button)findViewById(R.id.button1);
		btminus.setOnClickListener(this);

		Button btplus = (Button)findViewById(R.id.button2);
		btplus.setOnClickListener(this);
		
		Button btBkShelf = (Button)findViewById(R.id.button3);
		btBkShelf.setOnClickListener(this);
		
		Button btSigns = (Button)findViewById(R.id.button4);
		btSigns.setOnClickListener(this);
		
		Button btDemo = (Button)findViewById(R.id.button5);
		btDemo.setOnClickListener(this);
		
		
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.button1:
			intent	 = new Intent(CoverLetter.this, MainActivity.class);        
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
			intent.putExtra("episode", episode);
			intent.putExtra("read", "tome");
			startActivity(intent); 
			break;
		case R.id.button2:
			intent	 = new Intent(CoverLetter.this, MainActivity.class);        
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
			intent.putExtra("episode", episode);
			intent.putExtra("read", "myself");
			startActivity(intent); 
			break;
		case R.id.button3:
			intent	 = new Intent(CoverLetter.this, BookShelfActivity.class);        
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			//finish();
			startActivity(intent); 
			break;
		case R.id.button4:
			intent	 = new Intent(CoverLetter.this, ListWordsActivity.class);        
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
			intent.putExtra("episode", episode);
			//intent.putExtra("read", "myself");
			startActivity(intent); 
			break;
		case R.id.button5:
			intent	 = new Intent(CoverLetter.this, DemoActivity.class);        
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
			//intent.putExtra("episode", episode);
			//intent.putExtra("read", "myself");
			startActivity(intent); 
			break;
		default:
			break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cover_letter, menu);
		return true;
	}



}
