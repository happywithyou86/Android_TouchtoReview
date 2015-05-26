/*
 * Copyright (c) 2012 Wireless Designs, LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.ankit.touchreview;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankit.utility.Utility;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * PagerActivity: A Sample Activity for PagerContainer
 */

public class ActionsActivity extends Activity {
	Context mContext;
	ProgressDialog mProcessing;
	ImageView logo;
	LinearLayout LinearLayout1;
	String result = "";
	JSONObject object = null;
	boolean returnBack = false;
	String themeimage = "";
	String logoimage = "";
	TextView message;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.actions);

		logo = (ImageView) findViewById(R.id.logo);
		
		LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		mContext = this;

		message = (TextView)findViewById(R.id.message);
		
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		//int height = metrics.heightPixels;
		logo.getLayoutParams().width=width/2;
		Button goodReview = (Button)findViewById(R.id.goodreview);
		goodReview.getLayoutParams().width=width/3;
		Button badReview = (Button)findViewById(R.id.badreview);
		badReview.getLayoutParams().width=width/3;
		
		mProcessing = new ProgressDialog(mContext);
		mProcessing.setCancelable(true);
		mProcessing.setMessage("Please Wait");
		mProcessing.setTitle("Fetching Details");
		setLogoImage();
		setThemeImage();
		setMessage();
		
		if(Utility.appmode!=null && Utility.appmode>1){
			goodReview.setText("Give FeedBack");
			badReview.setText("Give Feedback");
			if(Utility.appmode == 2)
				badReview.setVisibility(View.GONE);
			if(Utility.appmode == 3)
				goodReview.setVisibility(View.GONE);
		}
	}

	public void onResume() {
		super.onResume();
		if (returnBack) {
			Intent intent = new Intent(ActionsActivity.this,FinalActivity.class);						
			startActivity(intent);
			finish();
		}
	}
	
	public void gotoExternalUrl(View v) {
		if (Utility.externalUrl!=null){
			Log.e("External Url", Utility.externalUrl);
//			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utility.externalUrl));
//			startActivity(browserIntent);
			Intent intent = new Intent(ActionsActivity.this,ExternalUrlActivity.class);				
			startActivity(intent);

			
			ExternalUrlActivity.settings = getSharedPreferences(ExternalUrlActivity.PREFS_NAME, MODE_PRIVATE);
			SharedPreferences.Editor editor = ExternalUrlActivity.settings.edit();
	        editor.putBoolean("isFirstRun", true);
	        editor.commit();
			
			returnBack = true;
			
		}else{
			showAlertDialog("No external Url is Added");
			
		}
	}
	
	public void submitFeedback(View v) {
		Intent intent = new Intent(ActionsActivity.this,FeedbackQuestionActivity.class);
		startActivity(intent);
		returnBack = true;
	}
	public void setMessage(){
		if(Utility.message2!=null){
			runOnUiThread(new Thread(){
				public void run(){
					message.setText(Utility.message2);
				}
			});
		}
	}
	
	public void setThemeImage() {
		if(Utility.themeimage!=null)
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					LinearLayout1.setBackgroundDrawable(Utility.themeimage);
				}
			});
	}
	public void setLogoImage() {
		if(Utility.logoimage!=null)
			runOnUiThread(new Thread() {
				public void run() {
					UrlImageViewHelper.setUrlDrawable(logo, Utility.logoimage);
				}
			});
	}

	public void showProcessingDialog() {
		runOnUiThread(new Thread() {
			public void run() {
				if (mProcessing != null) {
					mProcessing.show();
				}
			}
		});
	}

	public void cancelProcessingDialog() {
		runOnUiThread(new Thread() {
			public void run() {
				if (mProcessing != null) {
					mProcessing.cancel();
				}
			}
		});
	}

	public void showAlertDialog(final String msg) {
		runOnUiThread(new Thread() {
			public void run() {
				Utility.ShowAlertWithMessage(mContext, "Alert", msg);
			}
		});
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);    
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);    
	}


}