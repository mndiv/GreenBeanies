package com.signmeastory.apps.gb;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

public class SceneAnimation{
	private ImageView mImageView;	
	private int mDuration;
	private String[] sNamelist;	
	private Bitmap mustOpen;
	private ZipResourceFile _mainExpansionFile;
	private int mLastFrameNo;
	private String qrPath;
	private long mBreakDelay;
	private boolean _returnToBookShelf=false;
	private Context _context;
	
	/*
	private int[] mDurations;
	private File rootPath;
	private File pictureDirectory;
	public void setDuration(int pDuration) {
		this.mDuration = pDuration;
	}
	public SceneAnimation(ImageView pImageView, String pFrameRess, int[] pDurations){
		rootPath = Environment.getExternalStorageDirectory();
		pictureDirectory = new File(rootPath, pFrameRess);

		addPicturesOnExternalStorageIfExist();
		mImageView = pImageView;
		mDurations = pDurations;
		mLastFrameNo = sNamelist.length - 1;
		String qrPath = pictureDirectory.getPath()  + "/"+sNamelist[0];

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;
		Bitmap mustOpen = BitmapFactory.decodeFile(qrPath, options);
		mImageView.setImageBitmap(mustOpen);
		play(1);
	}
*/
	public SceneAnimation(Context c, ImageView pImageView, String pFrameRess, String resources, int pDuration){
		//public SceneAnimation(ImageView pImageView, String pFrameRess){
		String fileName = Helpers.getExpansionAPKFileName(c, true, 7);
		fileName = Helpers
				.generateSaveFileName(c, fileName);
		try {
			_mainExpansionFile = new ZipResourceFile(fileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//_mainExpansionFile = MainActivity.getZipResourceFile();									
		sNamelist=resources.split(",");		
		String imageNameInsideZip = pFrameRess+"/"+sNamelist[0];
		InputStream imageInputStream = null;
		try {
			imageInputStream = _mainExpansionFile.getInputStream(imageNameInsideZip);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_context = c;

		//rootPath = Environment.getExternalStorageDirectory();
		//pictureDirectory = new File(rootPath, pFrameRess);
		//addPicturesOnExternalStorageIfExist();
		mImageView = pImageView;
		mDuration = pDuration;
		mLastFrameNo = sNamelist.length - 1;
		qrPath = pFrameRess;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;
		//mustOpen = BitmapFactory.decodeFile(qrPath, options);		
		mustOpen = BitmapFactory.decodeStream(imageInputStream, null, options);//(imageInputStream);
		//imageView.setImageBitmap(bitmap);
		//mImageView.setImageBitmap(mustOpen);
		//playConstant(1);
	}
	public void setFirstScreen() {
		mImageView.setImageBitmap(mustOpen);
	}
	public void setLastScreen(boolean returnToBookShelf) {
		this._returnToBookShelf = returnToBookShelf;
		//Toast.makeText(_context, _returnToBookShelf+"", Toast.LENGTH_SHORT).show();
	}


/*	public SceneAnimation(ImageView pImageView, String pFrameRess, int pDuration, long pBreakDelay){


		pictureDirectory = new File(rootPath, pFrameRess);
		addPicturesOnExternalStorageIfExist();		
		mImageView = pImageView;
		mDuration = pDuration;
		mLastFrameNo = sNamelist.length - 1;
		mBreakDelay = pBreakDelay;
		String qrPath = pictureDirectory.getPath()  + "/"+sNamelist[0];
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inSampleSize = 1;
		Bitmap mustOpen = BitmapFactory.decodeFile(qrPath, options);
		mImageView.setImageBitmap(mustOpen);
		//playConstant(1);
	}


	private void play(final int pFrameNo){
		mImageView.postDelayed(new Runnable(){
			public void run() {
				//String qrPath = pictureDirectory.getPath()  + "/"+sNamelist[pFrameNo];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				options.inSampleSize = 1;

				Bitmap mustOpen = BitmapFactory.decodeFile(qrPath, options);
				mImageView.setImageBitmap(mustOpen);
				if(pFrameNo == mLastFrameNo )
					return;
				else if (pFrameNo < mLastFrameNo)
					play(pFrameNo + 1);
			}
		}, mDurations[pFrameNo]);
	}

*/
	public void playConstant(final int pFrameNo){

		mImageView.postDelayed(new Runnable(){
			public void run() {
				String imageNameInsideZip = qrPath+"/"+sNamelist[pFrameNo];
				InputStream imageInputStream = null;
				try {
					imageInputStream = _mainExpansionFile.getInputStream(imageNameInsideZip);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//String qrPath = pictureDirectory.getPath()  + "/"+sNamelist[pFrameNo];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inPreferredConfig = Bitmap.Config.ARGB_8888;
				options.inSampleSize = 1;
				//Bitmap mustOpen = BitmapFactory.decodeFile(qrPath, options);
				Bitmap mustOpen = BitmapFactory.decodeStream(imageInputStream, null, options);//(imageInputStream);

				mImageView.setImageBitmap(mustOpen);
				if(pFrameNo == mLastFrameNo){
					//Toast.makeText(_context, _returnToBookShelf+"", Toast.LENGTH_SHORT).show();
					if (!_returnToBookShelf)
					{
						mImageView.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								SceneAnimation.this.mImageView.setClickable(false);
								setFirstScreen();
								playConstant(1);
							}
						});
					}else{
						new Handler().postDelayed(new Runnable() {

							/*
							 * Showing splash screen with a timer. This will be useful when you
							 * want to show case your app logo / company
							 */

							@Override
							public void run() {
								// This method will be executed once the timer is over
								// Start your app main activity
								Intent intent = new Intent(_context, BookShelfActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
								_context.startActivity(intent);

								/*// close this activity
								finish();*/
							}
						}, 3000);
					}
					return;
				}
				else if (pFrameNo < mLastFrameNo)
					playConstant(pFrameNo + 1);
				/* if(pFrameNo < mLastFrameNo)
                    playConstant(pFrameNo + 1);*/
			}
		}, pFrameNo==mLastFrameNo && mBreakDelay>0 ? mBreakDelay : mDuration);
	}  

	/*private void addPicturesOnExternalStorageIfExist() {
		// check if external storage 
		String state = Environment.getExternalStorageState();
		if ( !(Environment.MEDIA_MOUNTED.equals(state) || 
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) ) {
			return;
		} 

		if ( !pictureDirectory.exists() ) {
			Log.d("Activity1", "NoFoundExternalDirectory");
			return;
		}

		FilenameFilter filefilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith(".jpeg") || 
						name.endsWith(".jpg") || 
						name.endsWith(".png") );
			}
		};

		sNamelist = pictureDirectory.list(filefilter);
		if (sNamelist.length == 0) {
			Log.d("Activity2", "No pictures in directory.");
			return;
		}
		else
		{
			Arrays.sort(sNamelist);
		}
	}
*/
}
