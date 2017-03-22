package com.signmeastory.apps.gb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.signmeastory.apps.gb.util.IabHelper;
import com.signmeastory.apps.gb.util.IabResult;
import com.signmeastory.apps.gb.util.Inventory;
import com.signmeastory.apps.gb.util.Purchase;


public class BookShelfActivity extends Activity implements OnClickListener {


	// user purchased GB2 ?
	boolean mIsPremiumGB2 = false;

	// SKUs for our products
	static final String SKU_GREEN_BEANIES2 = "greenbeanies_book2";

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	// The helper object
	IabHelper mHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_book_shelf);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ImageButton gb1 = (ImageButton)findViewById(R.id.gb1);
		gb1.setOnClickListener(this);

		ImageButton gb2 = (ImageButton)findViewById(R.id.gb2);
		gb2.setOnClickListener(this);

		ImageButton lock = (ImageButton)findViewById(R.id.lock);
		lock.setOnClickListener(this);

		ImageButton gb3 = (ImageButton)findViewById(R.id.gb3);
		gb3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast= Toast.makeText(getApplicationContext(), "Grean Beanies-3 will be coming soon..", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
		});

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAklnw+6StI1S4f4RjCQLtvzWMzUP35lhckWBMi33HKwfCB26hLYDAO4u2F3wIHiypazQw8o3I+5nig8yQmKcwkJbgcnhrUieSOuaB6j5U5b6Wk22eh5lq9OjsgKy7dpOstyrspSFfEil9xvWjVPIV7Of4JiGtgDf51fykPlB4z5f7flDE3rsWyreBVWmpojf3UU/HsjA5UPwex/M6nxrkc5mLCAPgjJnaOvWQLBkwTghtCdAap07GmylUKXhQVMboyMe/CioromEYebgEep7dZd/q1ImJIEgKfJ1LwaBsHCDJnDuJlwJ+ztVgFZ+VHmzuk6CRKBphJal0XB75uWO9CQIDAQAB";

		// Create the helper, passing it our context and the public key to verify signatures
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(false);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {

				if (!result.isSuccess()) {
					// in-app setup failed.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// IAB is fully set up.
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

	}

	// Listener that's called when we finish querying the items and subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// user purchased Green Beanies-2 ?
			Purchase premiumPurchase = inventory.getPurchase(SKU_GREEN_BEANIES2);
			mIsPremiumGB2 = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));

			findViewById(R.id.lock).setVisibility(mIsPremiumGB2 ? View.GONE:View.VISIBLE);			

		}
	};

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.gb1:
			intent = new Intent(BookShelfActivity.this, CoverLetter.class);        
			/*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
			intent.putExtra("episode", "gb1");
			startActivity(intent); 
			break;
		case R.id.gb2:
		case R.id.lock:
			if (!mIsPremiumGB2) {
				/*	TODO: for security, generate your payload here for verification. See the comments on 
				 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use 
				 *        an empty string, but on a production app you should carefully generate this.*/ 
				String payload = ""; 
				// launch the purchase UI flow.
				mHelper.launchPurchaseFlow(this, SKU_GREEN_BEANIES2, RC_REQUEST, 
						mPurchaseFinishedListener, payload);
			}
			else {
				intent = new Intent(BookShelfActivity.this, CoverLetter.class);        				
				intent.putExtra("episode", "gb2");
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else {
			//onActivityResult handled by IABUtil.
		}
	}


	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase and 
		 * verifying it here might seem like a good approach, but this will fail in the 
		 * case where the user purchases an item on one device and then uses your app on 
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on 
		 *    one device work on other devices owned by the user).
		 * 
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (result.isFailure()) {
				complain("Purchase Unsuccessful");
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			if (purchase.getSku().equals(SKU_GREEN_BEANIES2)) {
				// bought the green beanies 2 book!
				alert("Thank you for purchasing Green Beanies 2 book!");
				mIsPremiumGB2 = true;
				// need to implement update UI code to reflect new image for GB2 button
			}
		}
	};


	void complain(String message) {
		alert("Error: " + message);
	}


	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		bld.create().show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		if (mHelper != null) mHelper.dispose();
		mHelper = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.book_shelf, menu);
		return true;
	}
}
