
package com.ankit.touchreview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.ankit.utility.Utility;

/**
 * PagerActivity: A Sample Activity for PagerContainer
 */

@SuppressLint("SetJavaScriptEnabled")
public class ExternalUrlActivity extends Activity {
	static String PREFS_NAME="RUN";
	private Context mContext;
	private WebView mWebview;
	private WebView mWebviewPop;
	private FrameLayout mContainer;
	String newUA= "Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";
	boolean facebook = false;
	boolean google = false;
	boolean logging_in = false;
	static SharedPreferences settings;
	Context context;

	WebSettings webSettings;
	
	@Override
	public void onResume(){
	    super.onResume();
	    // put your code here...
		doFirstRun();

	}
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.externalurl);

		doFirstRun();
		 
		/*CookieManager cookieManager = CookieManager.getInstance(); 
	    cookieManager.setAcceptCookie(true); 
	    mWebview = (WebView) findViewById(R.id.webview);
	    
	    //mWebviewPop = (WebView) findViewById(R.id.webviewPop);
	    mContainer = (FrameLayout) findViewById(R.id.webview_frame);
	    webSettings = mWebview.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setAppCacheEnabled(false);
	    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
	    webSettings.setSupportMultipleWindows(true);
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setAllowFileAccess(true);
	    
	    //
	   // webSettings.setSaveFormData(true);
	    //webSettings.setSavePassword(true); 
	   // webSettings.setDatabaseEnabled(true);
	    
	    //SAVE THE DATA ON WEBVIEW
	    webSettings.setDomStorageEnabled(true);
	    
	   
	    //mWebview.setWebViewClient(new myWebClient());
	    mWebview.setWebViewClient(new UriWebViewClient());
	    mWebview.setWebChromeClient(new UriChromeClient());

	    mWebview.getSettings().setUserAgentString(newUA);
	    mWebview.loadUrl(Utility.externalUrl);

	    mContext=this.getApplicationContext();
	    */
		
		CookieManager cookieManager = CookieManager.getInstance(); 
	    cookieManager.setAcceptCookie(true); 
	    mWebview = (WebView) findViewById(R.id.webview);
	    //mWebviewPop = (WebView) findViewById(R.id.webviewPop);
	    mContainer = (FrameLayout) findViewById(R.id.webview_frame);
	    WebSettings webSettings = mWebview.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setAppCacheEnabled(true);
	    webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
	    webSettings.setSupportMultipleWindows(true);
	    mWebview.setWebViewClient(new UriWebViewClient());
	    mWebview.setWebChromeClient(new UriChromeClient());
	    
	    mWebview.getSettings().setJavaScriptEnabled(true);
	    mWebview.getSettings().setPluginState(PluginState.ON);
	    mWebview.getSettings().setLoadWithOverviewMode(true);  
	    mWebview.getSettings().setUseWideViewPort(true); 
	    
	    
	    mWebview.getSettings().setUserAgentString(newUA);
	    mWebview.loadUrl(Utility.externalUrl);

	    mContext=this.getApplicationContext();
	    
	}
	
	private class UriWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        String host = Uri.parse(url).getHost();
	        //Log.d("shouldOverrideUrlLoading", url);
	        Log.e("Host =", host);
	        if (host.contains("tripadvisor")) {

	            // This is my web site, so do not override; let my WebView load
	            // the page
	            if(mWebviewPop!=null)
	            {
	                mWebviewPop.setVisibility(View.GONE);
	                mContainer.removeView(mWebviewPop);
	                mWebviewPop=null;
	            }
	            
	            return false;
	        }

	        if(host.contains("facebook"))
	        {
	        	facebook = true;
	            return false;
	        }
	        
	        if(host.contains("google"))
	        {
	        	google = true;
	            return false;  
	        }

	        return true;
	    }
	   
	    @Override
	    public void onReceivedSslError(WebView view, SslErrorHandler handler,
	            SslError error) {
	        Log.d("onReceivedSslError", "onReceivedSslError");
	        //super.onReceivedSslError(view, -handler, error);
	        Log.e("SSL Error", error.toString());
	        handler.proceed();
	    }
	}

	class UriChromeClient extends WebChromeClient {
	    @Override
	    public boolean onCreateWindow(WebView view, boolean isDialog,boolean isUserGesture, Message resultMsg) {
	    	
	        mWebviewPop = new WebView(mContext);
	        mWebviewPop.setVerticalScrollBarEnabled(false);
	        mWebviewPop.setHorizontalScrollBarEnabled(false);
	        mWebviewPop.setWebViewClient(new UriWebViewClient());
	        mWebviewPop.getSettings().setJavaScriptEnabled(true);
	      
	        mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
	        ViewGroup.LayoutParams.MATCH_PARENT));
	      
	       // if(facebook || google)//{
	        //	if(google)//{
	
            
		    mWebviewPop.getSettings().setUserAgentString(newUA);
		    mContainer.addView(mWebviewPop);        
		    WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
		    transport.setWebView(mWebviewPop);
		    resultMsg.sendToTarget();

	        mWebviewPop.setVisibility(View.GONE);
            mContainer.removeView(mWebviewPop);
            mWebviewPop=null;

	        

	        return true;
	    }

	    @Override
	    public void onCloseWindow(WebView window) {
	        Log.d("onCloseWindow", "called");
	        
	    }
	}

	public class myWebClient extends WebViewClient{
    	@Override
    	public void onPageStarted(WebView view, String url, Bitmap favicon) {
    		// TODO Auto-generated method stub
    		super.onPageStarted(view, url, favicon);
    	}
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		// TODO Auto-generated method stub
    		view.loadUrl(url);
    		return true;
    	}
    }

	@SuppressWarnings("deprecation")
	private void doFirstRun() {
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if (settings.getBoolean("isFirstRun", true)) {

        	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    		
    		// Setting Dialog Title
    		alertDialog.setTitle(Html.fromHtml("<b>"+"Thank you for leaving a review"+"</b>"));
    		
    		// Setting Dialog Message
    		alertDialog.setMessage("Please note; \n\n"+
    				"1. Tripadvisor requires that you type at least 200 characters/letters \n\n"+
    				"2. You will log in using your Facebook or Google account. Click “yes” to agree to the terms so that your review will be posted.");
    		
    		// Setting Icon to Dialog
    		//alertDialog.setIcon(R.drawable.ic_launcher);
    		
    		// Setting OK Button
    		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    		        public void onClick(DialogInterface dialog, int which) {
    		        // Write your code here to execute after dialog closed
    		      //  Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
    		        }
    		});
    		
    		// Showing Alert Message
    		alertDialog.show();
    		
            
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        }
	}
	
	
	@Override
	public void onDestroy(){
		
	    CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(mContext);
	    cookieSyncMngr.resetSync();
	    CookieManager cookieManager = CookieManager.getInstance();
	    cookieManager.removeAllCookie();
	    cookieManager.removeSessionCookie();
	    mWebview.clearCache(true);
	    mWebview.clearHistory();
	    mWebview = null;
	    if(mWebviewPop!=null){
	    	mWebviewPop.clearCache(true);
	    	mWebviewPop.clearHistory();
	    	mWebviewPop = null;
	    }
	    super.onDestroy();
		
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