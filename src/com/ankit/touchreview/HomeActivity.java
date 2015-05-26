package com.ankit.touchreview;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

public class HomeActivity extends Activity {

	Button nextButton;
	ViewPagerCustomDuration pager;
	boolean slidieng = true;
	Context mContext;
	ProgressDialog mProcessing;
	ImageView logo;
	ArrayList<String> images;
	LinearLayout LinearLayout1;
	JSONObject object = null;
	TextView messagg;
	boolean returnBack = false;
	String logoImage="";
	String themeImage="";
	boolean loggedOut = false;
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		
		//ExternalUrlActivity.settings = getSharedPreferences(ExternalUrlActivity.PREFS_NAME, MODE_PRIVATE);
		//SharedPreferences.Editor editor = ExternalUrlActivity.settings.edit();
        //editor.putBoolean("isFirstRun", true);
        //editor.commit();
		
		Utility.message1 = null;
		Utility.message2 = null;
		Utility.message3 = null;
		Utility.externalUrl = null;
		Utility.logoimage = null;
		Utility.themeimage = null;
		
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		
		logo = (ImageView) findViewById(R.id.logo);
		logo.getLayoutParams().width=width/2;	
		nextButton = (Button)findViewById(R.id.submit);

		messagg = (TextView)findViewById(R.id.message);
		
		nextButton.getLayoutParams().width = width/3;
		//nextButton.getLayoutParams().height =nextButton.getLayoutParams().width/2;
		
		LinearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		mContext = this;
		mProcessing = new ProgressDialog(mContext);
		mProcessing.setCancelable(true);
		mProcessing.setMessage("Please Wait");
		mProcessing.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(loggedOut)
					finish();
			}
		});
		pager = (ViewPagerCustomDuration) findViewById(R.id.pager);
		pager.getLayoutParams().width = width/2;
		pager.getLayoutParams().height = width/4;
		pager.setScrollDurationFactor(5);
		images = new ArrayList<String>();
		new Thread(activityThread).start();
		new Thread(getMessageAndUrl).start();
	}
	public void logOut(View v){
		new Thread(){
			public void run(){
				Utility.setUserIdPrefernce(mContext, "token", "");
				Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
//				showProcessingDialog();
//				ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
//				listParams.add(new BasicNameValuePair("token", Utility
//						.getUserPrefernce(mContext, "token")));
//				String result = Utility.postParamsAndfindJSON(Utility.serverurl+"?method=SignOut&token="+Utility.getUserPrefernce(mContext, "token"),listParams);
//				if (result == null || result == "") {
//					cancelProcessingDialog();
//					showAlertDialog("Please check your Internet connection");
//				} else {
//					try {
//						Log.e("Logout Response = ", result);
//						object = new JSONObject(result);
//						if(object.getString("results").equals("1")){
//							Utility.setUserIdPrefernce(mContext, "token", "");
//						}
//						Utility.setUserIdPrefernce(mContext, "token", "");
//						finish();
//						cancelProcessingDialog();
//					} catch (JSONException e) {
//						cancelProcessingDialog();
//						Log.e("Error", e.toString());
//					}
//				}
			}
		}.start();
	}
	
	public void refresh(View v){
		slidieng = false;
		new Thread(activityThread).start();
		new Thread(getMessageAndUrl).start();
	}
	
	public void gotoNextScreen(View v){
		if(object!=null){
			Log.e("AppMode", Utility.appmode+"");
			if(Utility.appmode!=null && Utility.appmode==2){
				returnBack = true;
				ExternalUrlActivity.settings = getSharedPreferences(ExternalUrlActivity.PREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = ExternalUrlActivity.settings.edit();
		        editor.putBoolean("isFirstRun", true);
		        editor.commit();
				Intent intent = new Intent(HomeActivity.this, ExternalUrlActivity.class);
				startActivity(intent);
			}else if(Utility.appmode!=null && (Utility.appmode==3 || Utility.appmode==4)){
				returnBack = true;
				Intent intent = new Intent(HomeActivity.this, FeedbackQuestionActivity.class);
				startActivity(intent);
			}else{
				returnBack = false;
				Intent intent = new Intent(HomeActivity.this, ActionsActivity.class);
				startActivity(intent);
			}
		}
	}
	
	public void onResume(){
		super.onResume();
		if(returnBack==true){
			returnBack=false;
			Intent intent = new Intent(HomeActivity.this, FinalActivity.class);						
			startActivity(intent);
		}
	}
	
	Runnable activityThread = new Runnable() {
		@Override
		public void run() {
			showProcessingDialog();
			
			String result = Utility.getJSONFromUrl(Utility.serverurl+"?method=getImages&token="+Utility.getUserPrefernce(mContext, "token").trim());			
			if (result == null || result == "") {
				cancelProcessingDialog();
				showAlertDialog("Please check your Internet connection");
			} else {
				try {
					Log.e("Images Response = ", result);
					images.clear();
					object = new JSONObject(result);
					if (object.has("ThemeImage")) {
						themeImage = object.getString("ThemeImage");
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inJustDecodeBounds = true;
						InputStream is = (InputStream) new URL(
								object.getString("ThemeImage")).getContent();
						Bitmap bmImg = BitmapFactory.decodeStream(is);
						Utility.themeimage = new BitmapDrawable(bmImg);
						setThemeImage();
					}
					if (object.has("LogoImage")) {
						Utility.logoimage = object.getString("LogoImage");
						setLogoImage();
					}
					if (object.has("appmode")) {
						Utility.appmode = object.getInt("appmode");	
						if(Utility.appmode==4 && object.has("review_email")){
							Utility.reviewEmail = object.getString("review_email");
						}
					}else{
						Utility.appmode = 1;
					}
					JSONArray sliderImages = object.getJSONArray("sliderImages");
					for (int i = 0; i < sliderImages.length(); i++) {
						if(!"".equals(sliderImages.getJSONObject(i).getString("image")))
							images.add(sliderImages.getJSONObject(i).getString("image"));
					}
					if (images.size() > 1) {
						images.add(new String(images.get(0)));					
					}					
					if(images.size()>0){
						slidieng = true;
						setSliderImages();
					}
					checkAppMode();
					cancelProcessingDialog();
				} catch (JSONException e) {
					cancelProcessingDialog();
					Log.e("Error", e.toString());
				}
				catch (MalformedURLException e) {
					cancelProcessingDialog();
					Log.e("Error", e.toString());
				} catch (IOException e) {
					cancelProcessingDialog();
					Log.e("Error", e.toString());
				}
			}
		}
	};

	public void checkAppMode(){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(Utility.appmode!=null && Utility.appmode>1)
					nextButton.setText("Give FeedBack");
				else
					nextButton.setText("Submit");
			}
		});
	}
	
	Runnable getMessageAndUrl = new Runnable() {
		@Override
		public void run() {
			ArrayList<NameValuePair> listParams = new ArrayList<NameValuePair>();
			listParams.add(new BasicNameValuePair("token", Utility.getUserPrefernce(mContext, "token")));
			String getMessageresult = Utility.postParamsAndfindJSON(Utility.serverurl+"?method=getMessagesAndExternalUrl&token="+Utility.getUserPrefernce(mContext, "token"),listParams);
			if (getMessageresult == null || getMessageresult == "") {

			} else {
				Log.e("getMessagesAndExternalUrl Response = ", getMessageresult);
				try {
					JSONObject messages= new JSONObject(getMessageresult).getJSONObject("results");
					if(messages.has("text1")){
						Utility.message1 = messages.getString("text1");
					}
					if(messages.has("text2")){
						Utility.message2 = messages.getString("text2");
					}
					if(messages.has("text3")){
						Utility.message3 = messages.getString("text3");
					}
					if(messages.has("text4")){
						Utility.externalUrl = messages.getString("text4");
					}
					setMessage();
				} catch (JSONException e) {
					Log.e("Error", e.toString());
				}
			}
		}
	};
	
	public void setMessage(){
		if(Utility.message1!=null){
			runOnUiThread(new Thread(){
				public void run(){
					messagg.setText(Utility.message1);
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
	public void setSliderImages() {
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {				
				MyPagerAdapter adapter = new MyPagerAdapter(images);
				if(adapter.container!=null)
					adapter.container.removeAllViews();
				
				pager.setAdapter(adapter);
				// Necessary or the pager will only have one extra page to show
				// make this at least however many pages you can see
				pager.setOffscreenPageLimit(adapter.getCount());
				// A little space between pages
				pager.setPageMargin(15);

				// If hardware acceleration is enabled, you should also remove
				// clipping on the pager for its children.
				pager.setClipChildren(false);

				new Thread(new Runnable() {
					public void run() {						
						try {
							Thread.sleep(2000);
							if(images.size()>0)
							while (slidieng) {
								if ((pager.getCurrentItem() + 1)
										% images.size() == 0) {
									pager.setCurrentItem(
											(pager.getCurrentItem() + 1)
													% images.size(), false);
								} else {
									pager.setCurrentItem(
											(pager.getCurrentItem() + 1)
													% images.size(), true);
									Thread.sleep(2000);
								}
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
	}

	public void onDestroy() {
		slidieng = false;
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
	
	public void  onStop(){		
		slidieng = false;
		super.onStop();
	}
	
	protected void onRestart() {
		slidieng = true;
		setSliderImages();
	    super.onRestart();
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

	// Nothing special about this adapter, just throwing up colored views for
	// demo
	private class MyPagerAdapter extends PagerAdapter {
		ArrayList<String> images;
		public ViewGroup container = null;
		public MyPagerAdapter(ArrayList<String> images) {
			this.images = images;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if(container == null)
				this.container = container;
			ImageView view = new ImageView(HomeActivity.this);
			UrlImageViewHelper.setUrlDrawable(view, images.get(position));
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}
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