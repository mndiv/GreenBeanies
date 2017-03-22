package com.signmeastory.apps.gb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListWordsActivity extends Activity {

	private ArrayList<String> words = new ArrayList<String>();
	private String episode;
	//private String pathConst;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_words);
		Intent intent = getIntent();
		episode = intent.getStringExtra("episode");
		Scanner sc = null;
		try {
			//BufferedReader br = new BufferedReader(new FileReader(file));
			sc = new Scanner(getAssets().open(episode+".txt"));

			while (sc.hasNextLine()) {

				words.add(sc.nextLine());
				//text.append('\n');

			}
		}
		catch (IOException e) {
			//You'll need to add proper error handling here
		}
		sc.close();

		ListView lv = (ListView)findViewById(R.id.wordList);	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, words);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ListWordsActivity.this, VideoViewDemo.class);
				//intent.putExtra("URL", words.get(position)+".mp4");
				intent.putExtra("URL", words.get(position)+".3gp");
				startActivity(intent);
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_words, menu);
		return true;
	}
}
