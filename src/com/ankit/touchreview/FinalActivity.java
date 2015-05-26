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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ankit.utility.Utility;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

/**
 * PagerActivity: A Sample Activity for PagerContainer
 */

public class FinalActivity extends Activity {

	Context mContext;
	ProgressDialog mProcessing;
	ImageView logo;
	LinearLayout LinearLayout1;
	String result = "";
	JSONObject object = null;
	boolean returnBack = false;
	String themeimage = "";
	String logoimage  = "";
	TextView message;
	String msg = "";
	EditText email;
	boolean submitted = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.finalactivity);

		logo = (ImageView) findViewById(R.id.logo);
		LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		mContext = this;
		
		email = (EditText)findViewById(R.id.email);
		message = (TextView)findViewById(R.id.message);
		
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		//int height = metrics.heightPixels;
		logo.getLayoutParams().width=width/2;	
		Button finish = (Button)findViewById(R.id.finish);
		finish.getLayoutParams().width=width/3;
		
		
		setThemeImage();
		setLogoImage();
		setMessage();
		mProcessing = new ProgressDialog(mContext);
		mProcessing.setCancelable(true);
		mProcessing.setMessage("Please Wait");
		mProcessing.setOnCancelListener(new  OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(submitted)
			
				finish();
		
			
			}
		});
	}

	public void setMessage(){
		if(Utility.message3!=null){
			runOnUiThread(new Thread(){
				public void run(){
					message.setText(Utility.message3);
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

	
	public void submitFeedback(View v) {
		if(!email.getText().toString().trim().equals(""))
			submitEmail.start();
		else
			finish();
	}

	Thread submitEmail = new Thread() {
		@Override
		public void run() {
			showProcessingDialog();
			ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

			listParams.add(new BasicNameValuePair("token", Utility.getUserPrefernce(mContext, "token")));
			listParams.add(new BasicNameValuePair("email", email.getText().toString()));
			result = Utility
					.postParamsAndfindJSON(Utility.serverurl+"?method=savecustomermailid&token="+Utility.getUserPrefernce(mContext, "token"),listParams);
			if (result == null || result == "") {
				cancelProcessingDialog();
				showAlertDialog("Please check your Internet connection");
			} else {
				Log.e("Result", result);
				try {
					object = new JSONObject(result);
					submitted = true;
					cancelProcessingDialog();
				} catch (JSONException e) {
					cancelProcessingDialog();
					Log.e("Error", e.toString());
				}
			}
		}
	};

	public void setThemeImage(final String themeimage) {
		new Thread() {
			public void run() {
				try {
					InputStream is = (InputStream) new URL(themeimage)
							.getContent();
					Bitmap bmImg = BitmapFactory.decodeStream(is);
					@SuppressWarnings("deprecation")
					final BitmapDrawable background = new BitmapDrawable(bmImg);
					runOnUiThread(new Runnable() {
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							LinearLayout1.setBackgroundDrawable(background);
						}
					});
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void setLogoImage(final String image) {
		runOnUiThread(new Thread() {
			public void run() {
				UrlImageViewHelper.setUrlDrawable(logo, image);
			}
		});
	}

	public void onDestroy() {
		super.onDestroy();
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