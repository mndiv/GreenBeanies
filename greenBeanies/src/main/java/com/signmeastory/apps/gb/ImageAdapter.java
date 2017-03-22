package com.signmeastory.apps.gb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;


public class ImageAdapter extends PagerAdapter {

	Animation a;
	public String[] tween = new String[60]; 
	private String episode;
	private Context context;  
	public ArrayList<ArrayList<String>>  audioFileList = new ArrayList<ArrayList<String>>();
	public ZipResourceFile _mainExpansionFile;
	public ImageAdapter _imageAdapter;
	public TextView tv;
	private String pathConst;
	public SceneAnimation [] saArrayList = new SceneAnimation[150];

	//private final int mTapScreenTextAnimDuration = 5;
	public String[] GalImagesStr = new String[60]; 

	private String[] speech = new String[60];
	private String[] speechHtml = new String[60];
	private String[] animImages = new String[60];
	private int[] textTimer = new int[60];
	private int[] animDur = new int[60];

	public TextView [] tvArrayList = new TextView[speech.length+1];
	public ImageView [] ivArray = new ImageView[speech.length+1];
	private List<Page> pages = null;
	ImageAdapter(Context context, String episode){
		this.context=context;
		this._imageAdapter = this;
		this.episode=episode;
		//pages = SAXXMLParser.parse(context.getAssets().open("Books.xml"));
		pages = SAXXMLParser.getPages();


		for (int h = 0;h<pages.size();h++)
		{
			speech[h] = pages.get(h).getText();
			speechHtml[h] = pages.get(h).getTextHtml();
			animImages[h] = pages.get(h).getAnimImages();
			GalImagesStr[h] = pages.get(h).getImage();
			textTimer[h] = pages.get(h).getTime();
			animDur[h] = pages.get(h).getAnimDur();
			tween[h] = pages.get(h).getTween();
			audioFileList.add(pages.get(h).getAudios());

		}
		//pathConst = "/Android/obb/"+context.getPackageName() + "/"+episode+"/";
		pathConst = episode+"/";


	}
	@Override
	public int getCount() {
		return GalImagesStr.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {		 
		return view == ((RelativeLayout) object);		
	}

	public ImageAdapter getInstance()
	{
		return _imageAdapter;
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		CustomRelativeLayout cLL= new CustomRelativeLayout(context);
		if (position < pages.size()){
			ImageView imageView = new ImageView(context);          
			tv= new TextView(context);
			tvArrayList[position]=tv;
			ivArray[position]=imageView;
			int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_small);      
			imageView.setPadding(padding, padding, padding, padding);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(layoutParams);
			tv.setPadding(10, 0, 10, 0);			

			CharSequence sequence = Html.fromHtml(speechHtml[position]);
			SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

			URLSpan[] underlines = strBuilder.getSpans(0, strBuilder.length(), URLSpan.class);

			for(final URLSpan span : underlines) {

				int start = strBuilder.getSpanStart(span);
				int end = strBuilder.getSpanEnd(span);
				//int flags = strBuilder.getSpanFlags(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				ClickableSpan myActivityLauncher = new ClickableSpan() {
					public void onClick(View view) {
						//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
						Intent intent = new Intent(context, VideoViewDemo.class);
						intent.putExtra("URL", span.getURL());
						context.startActivity(intent);         
					}
				};
				strBuilder.setSpan(myActivityLauncher, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				strBuilder.removeSpan(span);

			}

			tv.setText(strBuilder);
			tv.setMovementMethod(LinkMovementMethod.getInstance());

			if(episode.equalsIgnoreCase("gb1"))
			{
				tv.setBackgroundColor(0xBB232325);
			}else
				tv.setBackgroundColor(0x55000000);
			tv.setTextColor(Color.WHITE);
			tv.setLineSpacing(0, 1.2f);
			tv.setTextSize(16);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( 
					RelativeLayout.LayoutParams.WRAP_CONTENT,                
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(20,8,20,0);
			lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);

			cLL.addView(imageView);
			cLL.addView(tv,lp);
			cLL.bringChildToFront(tv);
			((ViewPager) container).addView(cLL, 0);
			
			if(GalImagesStr[position].startsWith("a"))
			{
				//saArrayList[position] = new SceneAnimation(imageView, episode+"/"+GalImagesStr[position].substring(1), mTapScreenTextAnimDuration);			
				saArrayList[position] = new SceneAnimation(context,imageView, pathConst+GalImagesStr[position].substring(1), animImages[position], animDur[position]);

			}else{
				//Read from zip 
				// Get a ZipResourceFile representing a specific expansion file

				String fileName = Helpers.getExpansionAPKFileName(context, true, 7);

				/*if (Helpers.doesFileExist(context, fileName, 302381008L, true))
				{*/
				try
				{
					fileName = Helpers.generateSaveFileName(context, fileName);							
					_mainExpansionFile = MainActivity.getZipResourceFile();					
					String imageNameInsideZip = pathConst+"images/"+GalImagesStr[position]+".jpg";
					InputStream imageInputStream = _mainExpansionFile.getInputStream(imageNameInsideZip);

					if (imageInputStream == null)
					{
						Log.i("imagepath", "null");
					}
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					Bitmap bitmap = BitmapFactory.decodeStream(imageInputStream, null, options);//(imageInputStream);
					imageView.setImageBitmap(bitmap);
				}
				catch (IOException e)
				{
					Log.e("GB Application", e.getMessage(), e);
				}
				///}

			}
		}
		return cLL;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);
		tvArrayList[position] = null;
		saArrayList[position] = null;
		ivArray[position] = null;
	}
}
