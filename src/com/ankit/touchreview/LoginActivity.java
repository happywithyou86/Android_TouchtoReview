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

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.ankit.utility.Utility;

/**
 * PagerActivity: A Sample Activity for PagerContainer
 */

public class LoginActivity extends Activity {

	TextView username;
	TextView password;	
	Context mContext;
	ProgressDialog mProcessing;
	boolean logging_in = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

		mContext = this;
		mProcessing = new ProgressDialog(mContext);
		mProcessing.setCancelable(true);
		mProcessing.setMessage("Please Wait");
		mProcessing.setTitle("Logging in");
		mProcessing.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {				
				if(logging_in){
					Intent i = new Intent(LoginActivity.this, HomeActivity.class);
					startActivity(i);
					finish();
				}
			}
		});
		
		username = (TextView)findViewById(R.id.username);
		password = (TextView)findViewById(R.id.password);
		
		String token = Utility.getUserPrefernce(mContext, "token");
		if(token!=null && !"".equals(token)){
			Intent i = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(i);
			finish();
		}
		
		TextView thanking_text = (TextView)findViewById(R.id.thanking_text);
		thanking_text.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://touchtoreview.com/thankyou"));
			    startActivity(myIntent);
			}
		});
	}
	
	public void gotoNextScreen(View v){
		if((username.getText().toString().trim().equals("") || password.getText().toString().trim().equals(""))){
			showAlertDialog("Please Enter Username And Password");
			return;
		}
		new Thread(loginThread).start();		
	}
	Runnable loginThread = new Runnable() {
		@Override
		public void run() {
			showProcessingDialog();
			ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
			listParams.add(new BasicNameValuePair("username", username.getText().toString().trim()));
			listParams.add(new BasicNameValuePair("password", password.getText().toString().trim()));
			Log.e("Logging in ", Utility.serverurl+"?method=logIn : "+listParams.toString());
			String result = Utility.postParamsAndfindJSON(Utility.serverurl+"?method=logIn",listParams);			
			if (result == null || result == "") {
				
				showAlertDialog("Please check your Internet connection");
			}
			else {
				Log.e("LoginResult", result);
				try {
					JSONObject object = new JSONObject(result);//.getJSONObject("results");
					if(object.get("results") instanceof String){
						logging_in = false;
						cancelProcessingDialog();
						showAlertDialog("Invalid Username Or Password.");
						return;
					}
					object = object.getJSONObject("results");
					String token = object.getString("login_token");
					//Utility.setUserPrefernce(mContext, "token", "8e3f7f10b8c151e4f942538ae66e9d8e");
					Utility.setUserPrefernce(mContext, "token", token);
					logging_in = true;
					cancelProcessingDialog();
				} catch (JSONException e) {
					logging_in = false;					
					cancelProcessingDialog();
					showAlertDialog("Please Check Your Internet Connection");
					Log.e("Error", e.toString());
				} 
			}
		}
	};

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