/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.signmeastory.apps.gb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewDemo extends Activity {
	//private static final String TAG = "VideoViewDemo";
	//private EditText mPath;
	private String mPath;
	private String pathConst;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_splash);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//pathConst = Environment.getExternalStorageDirectory()+"/Android/obb/"+this.getPackageName() +"/videos/";
        pathConst = "videos/";
		Intent intent=getIntent();
		mPath=pathConst+(intent.getStringExtra("URL"));
		
		VideoView videoView = (VideoView)findViewById(R.id.videoView);		
		//videoView.setVideoURI(Uri.parse(mPath));
		
		videoView.setVideoURI(ZipContentProvider.buildUri(mPath));
		//videoView.setMinimumHeight(getWindowManager().getDefaultDisplay().getWidth());
		//videoView.setMinimumHeight(getWindowManager().getDefaultDisplay().getHeight());
		videoView.setMediaController(new MediaController(this));
	    videoView.requestFocus();
	    //videoView.setFitsSystemWindows(true);
	    videoView.start();		
		
	}

}
