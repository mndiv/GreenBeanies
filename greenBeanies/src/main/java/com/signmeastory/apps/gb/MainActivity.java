package com.signmeastory.apps.gb;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.Helpers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener{
	Animation a;
	private int MY_DATA_CHECK_CODE = 0;
	private CustomViewPager viewPager ;
	int i=0;
	int MAX_i; 
	private TextToSpeech tts;
	public ImageAdapter adapter;
	private int k =0;
	private String episode;
	private Timer timer = new Timer();
	private TimerTask timerTask;
	private String [] textArra;
	private String [] textArray_d;
	private String text;
	private String readOption;
	private int l=0;
	private String[] speech = new String[60];	
	private String[] speechHtml = new String[60];
	private int[] textTimer = new int[60];
	private MediaPlayer mp=new MediaPlayer();
	//private int[] animDur = new int[60];

	public static String fileName;
	public String[] tween = new String[60]; 
	private List<Page> pages = null;
	private static ZipResourceFile _mainExpansionFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent intent = getIntent();
		episode = intent.getStringExtra("episode");
		readOption = intent.getStringExtra("read");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);       
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		/*new Runnable(){
			public void run() { */
		//android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

		try {
			pages = SAXXMLParser.parse(getAssets().open(episode+".xml"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*}
		};*/

		fileName = Helpers.getExpansionAPKFileName(this, true, 7);

		fileName = Helpers
				.generateSaveFileName(this, fileName);
		try {
			_mainExpansionFile = new ZipResourceFile(fileName);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		MAX_i=pages.size();

		for (int h = 0;h<pages.size();h++)
		{
			speech[h] = pages.get(h).getText();
			tween[h] = pages.get(h).getTween();
			textTimer[h] = pages.get(h).getTime();
			speechHtml[h] = pages.get(h).getTextHtml();
			//animDur[h] = pages.get(h).getAnimDur();
		}

		if (readOption.equalsIgnoreCase("tome")){
			/*if( tts != null) {
				tts.shutdown();
			}
			 */
			if( mp.isPlaying()) {
				mp.stop();
			}
			tts = new TextToSpeech(this, this);
			//speakOut();
		}

		viewPager = (CustomViewPager) findViewById(R.id.view_pager);

		adapter = new ImageAdapter(this,episode);
		viewPager.setAdapter(adapter);
		viewPager.setPagingEnabled(false);

		final Button leftArrowBtn = (Button)findViewById(R.id.leftArrow);

		final Button rightArrowBtn = (Button)findViewById(R.id.rightArrow);
		leftArrowBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {                           
					return true;
				}            
				if (event.getAction() == MotionEvent.ACTION_UP ) {

					if (tween[i]!=""){
						a=null;
						view.clearAnimation();
						adapter.ivArray[i].clearAnimation();												
					}				
					System.gc();					

					if (readOption.equalsIgnoreCase("tome")){					
						stopTimer();
						//i=i-1;
						//speakOut();
					}
					i=i-1;
					viewPager.setCurrentItem(getItem(-1), true); //getItem(-1) for previous
					if(adapter.GalImagesStr[i].startsWith("a"))
					{
						//adapter.saArrayList[i].setDuration(animDur[i]);
						adapter.saArrayList[i].setFirstScreen();
						
							
						adapter.saArrayList[i].playConstant(1);
					}
					if (i==0) {leftArrowBtn.setVisibility(View.GONE);}					
					else {leftArrowBtn.setVisibility(View.VISIBLE);}
					if (i == MAX_i-2) {rightArrowBtn.setVisibility(View.VISIBLE);}
					if (tween[i]!=""){
						Field f;
						try {
							f = R.anim.class.getField(tween[i]);
							a = AnimationUtils.loadAnimation(MainActivity.this, f.getInt(new R.id()));
							a.reset();
							adapter.ivArray[i].startAnimation(a);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					if (readOption.equalsIgnoreCase("tome")){					
						//stopTimer();
						//i=i+1;
						speakOut();
					}


					return true;
				}

				return false;
			}
		});



		rightArrowBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_UP ) {
					if (tween[i]!=""){
						a=null;
						view.clearAnimation();
						adapter.ivArray[i].clearAnimation();												
					}

					System.gc();										

					if (readOption.equalsIgnoreCase("tome")){					
						stopTimer();
						//speakOut();
					}
					i=i+1;
					viewPager.getAdapter().notifyDataSetChanged();
					viewPager.setCurrentItem(getItem(+1), true); //getItem(-1) for previous
					if(adapter.GalImagesStr[i].startsWith("a"))
					{
						//adapter.saArrayList[i].setDuration(animDur[i]);
						adapter.saArrayList[i].setFirstScreen();
						if (i==MAX_i-1)
						{
							adapter.saArrayList[i].setLastScreen(true);
						}
						adapter.saArrayList[i].playConstant(1);
						//adapter.saArrayList[i].playConstant(0);
					}
					if(i==1){leftArrowBtn.setVisibility(View.VISIBLE);}
					else if (i == MAX_i-1) {rightArrowBtn.setVisibility(View.INVISIBLE);}

					if (tween[i]!=""){
						Field f;
						try {
							f = R.anim.class.getField(tween[i]);							
							a = AnimationUtils.loadAnimation(MainActivity.this, f.getInt(new R.id()));
							a.reset();
							adapter.ivArray[i].startAnimation(a);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}


					if (readOption.equalsIgnoreCase("tome")){					
						//stopTimer();
						//i=i+1;
						speakOut();
					}
					return true;
				}
				return false;
			}
		});



		Intent checkTTSIntent = new Intent();
		checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

	}

	private int getItem(int j) {
		int a = viewPager.getCurrentItem();
		j += a;
		return j;
	}


	/*@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			//Toast.makeText(this, "hello tts spech success", Toast.LENGTH_LONG).show();
			int result = tts.setLanguage(Locale.US);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {
				//Toast.makeText(this, "hello tts speak out calling", Toast.LENGTH_LONG).show();
				speakOut();
			}
		} else {
			Log.e("TTS", "Initilization Failed");
		}
	}*/

	public void speakOut() {
		text = speech[i];
		textArray_d = text.split(" ");
		textArra = new String[textArray_d.length];
		for (int m=0;m<textArray_d.length;m++)
		{
			textArra[m]=textArray_d[m];
		}
		//ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(this,2001,2001);
		AssetFileDescriptor fd = _mainExpansionFile.getAssetFileDescriptor(episode+"/"+adapter.audioFileList.get(i).get(0)+".wav");
		//playSong(fd.getFileDescriptor());
		playSong(fd);
		/*HashMap<String, String> params = new HashMap<String, String>();
		k=0;
		while (k<adapter.audioFileList.get(i).size())
		{
			//Toast.makeText(this, speech[i], Toast.LENGTH_LONG).show();			 
		     //AssetFileDescriptor fd = expansionFile.getAssetFileDescriptor(zip[0].mFileName);
		     //tts.addSpeech(speech[i],_mainExpansionFile.getAssetFileDescriptor("resource/"+episode+"/"+adapter.audioFileList.get(i).get(k)+".wav").toString());
			tts.addSpeech(speech[i],ZipContentProvider.buildUri("resource/"+episode+"/"+adapter.audioFileList.get(i).get(k)+".wav").getPath());

		    //tts.addSpeech(speech[i],Environment.getExternalStorageDirectory()+"/Android/obb/"+getPackageName() + "/"+episode+"/"+adapter.audioFileList.get(i).get(k)+".wav");
			if (k==0)
			{
				//Toast.makeText(this, speech[i] +" "+Environment.getExternalStorageDirectory()+"/"+episode+"/"+adapter.audioFileList.get(i).get(k)+".wav", Toast.LENGTH_LONG).show();
				tts.speak(speech[i], TextToSpeech.QUEUE_FLUSH, params);
			}
			else 
				tts.speak(speech[i], TextToSpeech.QUEUE_ADD, params);
			k++;
		}*/
		startTimer1();
	}

	//private void playSong(FileDescriptor fd)
	private void playSong(AssetFileDescriptor fd)
	{
		mp.reset();
		try {
			Log.i("file escriptor", fd.toString());
			mp.setDataSource(fd.getFileDescriptor(),fd.getStartOffset(),fd.getLength());
			mp.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				nextSong();
			}

		});
	}

	private void nextSong() {
		if (++k >= adapter.audioFileList.get(i).size()) {
			// Last song, just reset currentPosition
			k = 0;
		} else {
			// Play next song
			AssetFileDescriptor fd = _mainExpansionFile.getAssetFileDescriptor(episode+"/"+adapter.audioFileList.get(i).get(k)+".wav");
			//playSong(fd.getFileDescriptor());
			playSong(fd);        	
		}
	}

	private void startTimer1()
	{
		l=0;
		timer = new Timer();

		timerTask = new TimerTask() {
			public void run() {

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(l==textArra.length)
						{
							if(!mp.isPlaying())
								stopTimer();

						}else{
							for (int j=0;j<textArra.length;j++)
							{
								if(j==0)
								{
									text="";
								}
								if(l==j){
									textArra[j]= "<b><font color='#E59581'>" + textArray_d[j] + " </font></b>";//textArra[j].startsWith("type")||textArra[j].startsWith("<a")||textArra[j].startsWith("onclick")||textArra[j].startsWith("COLOR=")?textArray_d[j]:
									text = text+textArra[j]+" ";
								}
								else{
									textArra[j]=textArray_d[j];
									text = text+textArra[j]+" ";
								}


							}
							adapter.tvArrayList[i].setText(Html.fromHtml(text));
							adapter.tvArrayList[i].setMovementMethod(LinkMovementMethod.getInstance());

							l++;
						}                              
					}
				}); 
			}
		};
		timer.schedule(timerTask, 0, textTimer[i]);
	}


	public void stopTimer()
	{
		try{
			if (timer != null)
			{
				timer.cancel();
			}
			if(mp.isPlaying())
			{
				mp.stop();
			}
			/*  if(tts != null)
           {
          	 tts.stop();
          	 //tts=null;
           }*/
			/*text="";
			for (int i=0;i<textArra.length;i++)
			{
				text=text+" "+textArray_d[i];
			}*/
			//Toast.makeText(this, "hello stop called", Toast.LENGTH_LONG).show();
			//adapter.tvArrayList[i+1].setText(Html.fromHtml("hello abc "+speechHtml[i]));
			//adapter.tvArrayList[i-1].setText(Html.fromHtml("hello "+speechHtml[i]));

			//adapter.tvArrayList[i].setAutoLinkMask(Linkify.ALL );
			//adapter.tvArrayList[i].setLinksClickable(true);
			//adapter.tvArrayList[i].setClickable(true);


			CharSequence sequence = Html.fromHtml(speechHtml[i]);
			SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);

			URLSpan[] underlines = strBuilder.getSpans(0, strBuilder.length(), URLSpan.class);

			for(final URLSpan span : underlines) {

				int start = strBuilder.getSpanStart(span);
				int end = strBuilder.getSpanEnd(span);
				//int flags = strBuilder.getSpanFlags(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				ClickableSpan myActivityLauncher = new ClickableSpan() {
					public void onClick(View view) {


						//Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(span.getURL()));
						Intent intent = new Intent(MainActivity.this, VideoViewDemo.class);
						intent.putExtra("URL", span.getURL());
						startActivity(intent);         
					}
				};
				strBuilder.setSpan(myActivityLauncher, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				strBuilder.removeSpan(span);

			}
			adapter.tvArrayList[i].setText(strBuilder);
			adapter.tvArrayList[i].setMovementMethod(LinkMovementMethod.getInstance());

		}
		catch(Exception e)
		{

		}
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if(mp.isPlaying())
		{
			mp.stop();
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		if(mp.isPlaying())
		{
			mp.stop();
			mp.release();			
		}
	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) 
	{
		super.onSaveInstanceState(savedInstanceState);        
		savedInstanceState.putInt("i", i);

	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);                
		i = savedInstanceState.getInt("i");

	}
	@Override
	public void onResume() {

		super.onResume();  // Always call the superclass method first

		Button leftArrowBtn = (Button)findViewById(R.id.leftArrow);
		Button rightArrowBtn = (Button)findViewById(R.id.rightArrow);
		leftArrowBtn.setVisibility(View.VISIBLE);
		rightArrowBtn.setVisibility(View.VISIBLE);
		if (i == MAX_i-1)            
		{
			rightArrowBtn.setVisibility(View.INVISIBLE);
		}
		if(i==0)
		{            
			leftArrowBtn.setVisibility(View.INVISIBLE);

		}
	}
	public static ZipResourceFile getZipResourceFile(){
		return _mainExpansionFile;}

	public static String getZipResourceFilenm(){
		return fileName;
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		speakOut();
		if (i==0 && tween[i]!=""){
			Field f;
			try {
				f = R.anim.class.getField(tween[i]);							
				a = AnimationUtils.loadAnimation(MainActivity.this, f.getInt(new R.id()));
				a.reset();
				adapter.ivArray[i].startAnimation(a);
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		tts=null;
	}
}
