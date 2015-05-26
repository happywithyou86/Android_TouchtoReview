package com.ankit.touchreview;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ankit.utility.FeedbackQuestionAdapter;
import com.ankit.utility.Utility;

public class FeedbackQuestionActivity extends Activity {

	Context mContext;
	ProgressDialog mProcessing;
	ImageView logo;
	LinearLayout LinearLayout1;
	String result = "";
	JSONArray questionsArray = null;
	String themeimage = "";
	String logoimage  = "";
	ListView questions;
	ArrayList<JSONObject> questionsList;
	FeedbackQuestionAdapter adapter;
	EditText customer_message;
	boolean reviewSubmitted = false;
	
	RadioButton happy;
	RadioButton needResolution;
	RadioButton by_phone;
	RadioButton by_letter;
	RadioButton by_email;
	EditText for_resolution;
	
	LinearLayout resolution_ways;
	LinearLayout resolution;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedbackquestions);
		
		
		resolution_ways = (LinearLayout)findViewById(R.id.resolution_ways);
		resolution = (LinearLayout)findViewById(R.id.resolution);
		resolution_ways.setVisibility(View.GONE);
		if(Utility.appmode==4){
			happy = (RadioButton)findViewById(R.id.happy);
			happy.setChecked(true);
			needResolution = (RadioButton)findViewById(R.id.needresolution);
			by_phone = (RadioButton)findViewById(R.id.by_phone);
			by_phone.setChecked(true);
			by_letter = (RadioButton)findViewById(R.id.by_letter);
			by_email = (RadioButton)findViewById(R.id.by_email);
			for_resolution = (EditText)findViewById(R.id.for_resolution);
			for_resolution.setHint("Enter Contact Number");
			happy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						needResolution.setChecked(false);
						resolution_ways.setVisibility(View.GONE);
					}
				}
			});
			
			needResolution.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						happy.setChecked(false);
						resolution_ways.setVisibility(View.VISIBLE);
					}
				}
			});
			
			by_phone.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
					if(isChecked){
						by_letter.setChecked(false);
						by_email.setChecked(false);
						for_resolution.setHint("Enter Contact Number");
					}
				}
			});
			
			by_letter.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
					if(isChecked){
						by_phone.setChecked(false);
						by_email.setChecked(false);
						for_resolution.setHint("Enter Postal Address");
					}
				}
			});
			
			by_email.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {				
					if(isChecked){
						by_letter.setChecked(false);
						by_phone.setChecked(false);
						for_resolution.setHint("Enter Email Address");
					}
				}
			});
		}else{
			resolution.setVisibility(View.GONE);
		}
		customer_message = (EditText)findViewById(R.id.customer_message);
		logo = (ImageView) findViewById(R.id.logo);
		LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		mContext = this;
		questions = (ListView)findViewById(R.id.questions);
		questionsList = new ArrayList<JSONObject>();
		adapter = new FeedbackQuestionAdapter(mContext, android.R.layout.simple_list_item_1, questionsList);
		questions.setAdapter(adapter);
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		Button submit = (Button)findViewById(R.id.submit);
		submit.getLayoutParams().width=width/3;
		
		setThemeImage();
		
		mProcessing = new ProgressDialog(mContext);
		mProcessing.setCancelable(true);
		mProcessing.setMessage("Please Wait");
		mProcessing.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(reviewSubmitted){
					finish();
				}
			}
		});
		
		new Thread(activityThread).start();
	}
	


	public void submitFeedback(View v) {
		Log.e("FeedbackResponse", questionsList.toString());
		new Thread(submitReview).start();
	}

	Runnable activityThread = new Runnable() {
		@Override
		public void run() {
			showProcessingDialog();
			ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();

			listParams.add(new BasicNameValuePair("token", Utility
					.getUserPrefernce(mContext, "token")));
			result = Utility
					.postParamsAndfindJSON(Utility.serverurl+"?method=getFeedbackQuestions&token="+Utility.getUserPrefernce(mContext, "token"),listParams);
			if (result == null || result == "") {
				cancelProcessingDialog();
				showAlertDialog("Please check your Internet connection");
			} else {
				Log.e("FeedbackQuestionResult", result);
				try {
					JSONArray questionsArray = new JSONObject(result).getJSONObject("results").getJSONArray("questions");
					JSONObject jo;
					for(int i=0;i<questionsArray.length();i++){
						jo = questionsArray.getJSONObject(i).getJSONObject("ques");
						jo.put("selectedOption", "");
						questionsList.add(jo);
					}
					notifyAdapter();
					cancelProcessingDialog();
				} catch (JSONException e) {
					cancelProcessingDialog();
					Log.e("Error", e.toString());
				}
			}
		}
	};

	Thread submitReview = new Thread() {
		@Override
		public void run() {
			
			ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
			listParams.add(new BasicNameValuePair("token", Utility.getUserPrefernce(mContext, "token")));
			if(Utility.appmode==4 && needResolution.isChecked()){
				listParams.add(new BasicNameValuePair("appmode", "4"));
				if(for_resolution.getText().toString().trim().equals("")){
					showAlertDialog("Please Fill Field Below");
					return;
				}
				listParams.add(new BasicNameValuePair("resolution_on", for_resolution.getText().toString()));
				listParams.add(new BasicNameValuePair("needResolution", "Yes"));
				if(by_phone.isChecked())
					listParams.add(new BasicNameValuePair("resolution_by", "Phone"));
				if(by_email.isChecked())
					listParams.add(new BasicNameValuePair("resolution_by", "Email"));
				if(by_letter.isChecked())
					listParams.add(new BasicNameValuePair("resolution_by", "Letter"));
			}
			showProcessingDialog();
			if(Utility.reviewEmail!=null && Utility.appmode==4)
				listParams.add(new BasicNameValuePair("review_email", Utility.reviewEmail));
			try {
				String question = "";
				for(int i = 0; i<questionsList.size();i++){
					if(question.equals("")){
						question = questionsList.get(i).getString("id")+","+questionsList.get(i).getString("selectedOption");
					}else{
						question = question+"|"+questionsList.get(i).getString("id")+","+questionsList.get(i).getString("selectedOption");
					}
				}
				listParams.add(new BasicNameValuePair("question", question));				
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			if(!customer_message.getText().toString().equals(""))
				listParams.add(new BasicNameValuePair("msg", customer_message.getText().toString()));
			Log.e("ListParams", listParams.toString());
			result = Utility
					.postParamsAndfindJSON(Utility.serverurl+"?method=submitfeedback&token="+Utility.getUserPrefernce(mContext, "token"),listParams);
			cancelProcessingDialog();
			if (result == null || result == "") {
				
				showAlertDialog("Please check your Internet connection");
			} else {
				Log.e("FeedbackQuestionResult", result);
				finish();
				try {
					JSONObject jo = new JSONObject(result);	
					reviewSubmitted = true;
									
				} catch (JSONException e) {
					
					Log.e("Error", e.toString());
				}
			}
		}
	};
	
	public void notifyAdapter(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
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