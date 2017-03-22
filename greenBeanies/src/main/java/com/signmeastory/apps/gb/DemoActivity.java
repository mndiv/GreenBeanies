package com.signmeastory.apps.gb;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class DemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_main);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);      
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_demo);
		
		final ImageView mSplashImageView = (ImageView) findViewById(R.id.imageView1);
	    mSplashImageView.setScaleType(ScaleType.FIT_XY);
	    
	    String resources="01.jpg,02.jpg,03.jpg,04.jpg,05.jpg,06.jpg,07.jpg,08.jpg,09.jpg,10.jpg,11.jpg,12.jpg,13.jpg,14.jpg,15.jpg,16.jpg,17.jpg,18.jpg";
	    		
	    SceneAnimation SA = new SceneAnimation(this,mSplashImageView, "demo",resources,5);
	    SA.setFirstScreen();
	    SA.playConstant(1);
	    mSplashImageView.setOnClickListener(new OnClickListener() {           
	        @Override
	        public void onClick(View v) {
	            // TODO Auto-generated method stub
	            
	        }
	    });     
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.demo, menu);
		
		 
		return true;
	}

}