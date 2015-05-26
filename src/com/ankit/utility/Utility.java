package com.ankit.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

public class Utility {
	public static final String PREFS_LOGIN_NAME = "Retirely";
	//public static String serverurl = "http://aurelie1910.byethost7.com/api.php";
	public static String serverurl = "http://www.touchtoreview.com/api.php";
	public static Context mContext;
	public static Hashtable<String, String> currentPermissions = new Hashtable<String, String>();
	private static int MAX_IMAGE_DIMENSION = 720;
	public static final String HACK_ICON_URL = "http://www.facebookmobileweb.com/hackbook/img/facebook_icon_large.png";
	public static Date date;
	public static int mMonth, mYear, mDate, mHour, mMinute, mSecond;
	public static int curDate, curMonth, curYear, curHour, curMinute,curSecond;
	public static final String FONT = "fonts/exot350b.ttf";
	public static final String SLIDEMENURBROADCAST = "Menuchange";
	// Google project id
	public static final String SENDER_ID = "772928002004";
	public static final String DISPLAY_MESSAGE_ACTION = "com.androidhive.pushnotifications.DISPLAY_MESSAGE";
//	AIzaSyAZNPIhO5xv7ULaFoJ9OtGD8Ung4SevAQ4

	public static final String EXTRA_MESSAGE = "message";
	public static Bitmap bitmap;
	
	public static BitmapDrawable themeimage = null;
	public static String logoimage = null;
	public static String message1 = null;
	public static String message2 = null;
	public static String message3 = null;
	public static String externalUrl = null;
	public static String reviewEmail = null;
	public static Integer appmode = null;
	
	
	public static void displayMessage(Context context, String message) {

		Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);

		intent.putExtra(EXTRA_MESSAGE, message);

		context.sendBroadcast(intent);

	}
	
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
             
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
                 
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
               
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
	
	
	public static void setBitmap(Bitmap bm)
	{
		bitmap = bm;
	}
	
	public static Bitmap getBitmap()
	{
		return bitmap;
	}
	
	public static String parseToString(String dollarText)
	{
		String result = "0";
		if(dollarText.trim().length()>0)
		if (dollarText.contains("$")) {
			dollarText = dollarText.replace("$", "");
			if(dollarText.contains("."))
			{
				String [] fractional = dollarText.split("\\.");
				if(fractional[1].equalsIgnoreCase("00"))
				{
					result = fractional[0];
				}else
				{
					result = fractional[0] + "."+fractional[1];
				}
			}
			
		}else
		{
			result = dollarText;
		}
		
		return result;
		
	}
	@SuppressLint("UseValueOf")
	public static String parseDollars(double amount) {
		String useramount = "0";
		String useramountfractional = "00";
	    if(Double.isNaN(amount) || Double.isInfinite(amount)) return (new Double(amount)).toString();
	    
	    useramount = String.valueOf(amount);
	    if(useramount.contains(".")){
	    String [] fractional = useramount.split("\\.");
	    useramount = fractional[0];
	    if(fractional.length>0)
	    	useramountfractional = fractional[1];
	    }
	    //(int)Math.round()
	    
	    return parseDollars(Integer.parseInt(useramount),Integer.parseInt(useramountfractional));
	  }

	  public static String parseDollars(int amount, int fractional) {
	    if (amount < 0) {
	      return "-" + parseDollars(-amount);
	    }
	    //int cents = amount % 100;
	    //int dollars = amount / 100;
	    //return amount == 0 ? "$0" : "$" + dollars + "."
	      //  + (cents >= 10 ? cents : "0" + cents);
	    return amount== 0 ? "$0" : "$" + amount + "."+((fractional>10) ?fractional:"0"+fractional);
	  }
	  
	public static String convertTextToAmount(String value)
	{
		String result = "0";
		
		if(value.trim().length()>0)
		{
		 NumberFormat format = NumberFormat.getInstance();
	        format.setMaximumFractionDigits(2);
	        Currency currency = Currency.getInstance("USD");
	        format.setCurrency(currency);
	        
	        result =  ""+format.format(Double.parseDouble(value));
		}
		
		return result;
	}
	
	public static String convertAmountToText(String value) 
	{
		String result = "0";
		if(value.trim().length()>0)
		{
			DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
			format.setParseBigDecimal(true);
			BigDecimal number=null;
			try {
				number = (BigDecimal) format.parse(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
	       Log.d("", "Big Decimal "+ number);
		}
		
		return result;
	}
	

	// set string prefrences
	public static void setUserPrefernce(Context context, String name, String value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);

		// Commit the edits!
		editor.commit();
	}

	public static boolean isGooglePlayInstalled(Context context) {
	    PackageManager pm = context.getPackageManager();
	    boolean app_installed = false;
	    try
	    {
	           PackageInfo info = pm.getPackageInfo("com.android.vending", PackageManager.GET_ACTIVITIES);
	           String label = (String) info.applicationInfo.loadLabel(pm);
	           app_installed = (label != null && !label.equals("Market"));
	           if(!app_installed)
	           {
	        	   //com.google.market
	        	   PackageInfo info2 = pm.getPackageInfo("com.google.market", PackageManager.GET_ACTIVITIES);
		           String label2 = (String) info.applicationInfo.loadLabel(pm);
		           app_installed = (label2 != null && !label2.equals("Market"));
	           }
	    }
	    catch (PackageManager.NameNotFoundException e)
	    {
	           app_installed = false;
	    }
	    return app_installed;
	}
	
	public static int getAge(int year, int month, int day){
	    Calendar dob = Calendar.getInstance();
	    Calendar today = Calendar.getInstance();

	    dob.set(year, month, day); 

	    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

	    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
	        age--; 
	    }

	    Integer ageInt = new Integer(age);
	    //String ageS = ageInt.toString();

	    return ageInt;  
	}
	
	  public static int getAgeGregorian (int _year, int _month, int _day) {

          GregorianCalendar cal = new GregorianCalendar();
          int y, m, d, a;         

          y = cal.get(Calendar.YEAR);
          m = cal.get(Calendar.MONTH);
          d = cal.get(Calendar.DAY_OF_MONTH);
          cal.set(_year, _month, _day);
          a = y - cal.get(Calendar.YEAR);
          if ((m < cal.get(Calendar.MONTH))
                          || ((m == cal.get(Calendar.MONTH)) && (d < cal
                                          .get(Calendar.DAY_OF_MONTH)))) {
                  --a;
          }
          if(a < 0)
                  throw new IllegalArgumentException("Age < 0");
          return a;
	  }
	// check is email id is valid format someone@website.domain
	public static boolean isValidEmail(String emailAddress) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emailAddress);

		return matcher.matches();
	}
// get String prefrence
	public static String getUserPrefernce(Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		
		// / return settings.putString(name, null);
		return settings.getString(name, null);
	}

	// Convert json object to hash map
	public static HashMap<String, String> toMap(JSONObject object)
			throws JSONException {
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<?> keys = object.keys();
		while (keys.hasNext()) {
			String key = (String) keys.next();
			map.put(key, object.getString(key));
		}
		return map;
	}
// set boolean prefrence
	public static void setBooleanPreferences(Context context, String name,
			boolean value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, value);
		
		// Commit the edits!
		editor.commit();
	}

	// set integer prefrence
	public static void setIntegerPrefrence(Context context, String name,
			int value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(name, value);
		editor.commit();

	}

	// get integer prefrence
	public static int getIntegerPrefrence(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		return settings.getInt(key, 0);
	}
	
	// set integer prefrence


	// set float prefrence
	public static void setFloatPrefrence(Context context, String name, float value)
	{
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(name, value);
		editor.commit();
	}
	// get float prefrence
	public static float getFloatPrefrence(Context context, String name)
	{
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		return settings.getFloat(name, (float) 0.0);
	}
	
	public static void setCurrentLattitudePrefernce(Context context,
			String name, String value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);

		// Commit the edits!
		editor.commit();
	}

	public static String getCurrentLattitudePrefernce(Context context,
			String name) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);

		// / return settings.putString(name, null);
		return settings.getString(name, "0");
	}

	public static void setCurrentLongitudePrefernce(Context context,
			String name, String value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(name, value);
		// Commit the edits!
		editor.commit();
	}

	// Show alert dialog
	public static void ShowAlertWithMessage(Context context, String title,
			String msg) {
		// Assign the alert builder , this can not be assign in Click events
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setMessage(msg);
		// builder.setIcon(com.travelkashmir.travelsearch.R.drawable.alert_dialog_icon);
		builder.setTitle(title);
		// Set behavior of negative button
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// Cancel the dialog
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		try {
			alert.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	// Show alert dialog
		public static void ShowPurchaseAlertWithMessage(final Context context, String title,
				String msg, final String url) {
			// Assign the alert builder , this can not be assign in Click events
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setMessage(msg);
			// builder.setIcon(com.travelkashmir.travelsearch.R.drawable.alert_dialog_icon);
			builder.setTitle(title);
			// Set behavior of negative button
			builder.setNegativeButton("No, Thanks", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// Cancel the dialog
					dialog.cancel();
				}
			});
			builder.setPositiveButton("Renew", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					context.startActivity(i);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		}

	public static String getCurrentLongitudePrefernce(Context context,
			String name) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);

		// / return settings.putString(name, null);
		return settings.getString(name, "0");
	}

	public static void setBooleanprefence(Context context, String name,
			boolean value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(name, value);
		// Commit the edits!
		editor.commit();
	}

	public static boolean getBooleanPrefernce(Context context, String name) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);

		// / return settings.putString(name, null);
		return settings.getBoolean(name, false);
	}

	public static void showAlert(Context mContext, String title, String msg) {
		/*
		 * CommonUtility.ShowAlertWithMessageForAction(mContext,
		 * "You are not logged In", "Please log In first");
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(msg);
		// Set behavior of negative button

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});

		AlertDialog alert = builder.create();
		try{
		alert.show();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static Bitmap decodeBase64Drawable(String input) {
		try {
			byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
			Bitmap bm = BitmapFactory.decodeByteArray(decodedByte, 0,
					decodedByte.length);// new
										// BitmapDrawable(BitmapFactory.decodeByteArray(decodedByte,
										// 0, decodedByte.length));
			// is.read(decodedByte);
			decodedByte = null;
			return bm;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String encodeTobase64(Bitmap image) {
		Bitmap immagex = image;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		// String imageEncoded = Base64Coder.encodeTobase64(image);

		// Log.d("LOOK", imageEncoded);
		return imageEncoded;
	}

	public static String getJSONFromUrl(String url) {
		// TODO Auto-generated method stub
		JSONObject jObj = new JSONObject();
		String result = "";

		System.out.println("URL comes in jsonparser class is:  " + url);
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpGet);
			int status = httpResponse.getStatusLine().getStatusCode();

			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}
			is.close();
			result = sb.toString();

			} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	public static Bitmap getbitmapInSmallSize(String imageurl) {

		Bitmap myBitmap = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(imageurl);
		HttpResponse response;
		try {
			response = httpClient.execute(request);
			System.out.print("Response is = "+response.toString());
			InputStream is = response.getEntity().getContent();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPurgeable = true;
			options.outHeight = 30;
			options.outWidth = 30;
			options.inSampleSize = 2;
			myBitmap = BitmapFactory.decodeStream(is, null, options); // DecodeFile(is, options);
			myBitmap = Bitmap.createScaledBitmap(myBitmap, 100, 100, true);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (OutOfMemoryError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return myBitmap;
	}

	public static Bitmap getBitmap(String url) {
		
		
		Bitmap imageBitmap = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			try {
				imageBitmap = BitmapFactory.decodeStream(new FlushedInputStream(is));
			} catch (OutOfMemoryError error) {
				error.printStackTrace();
				System.out.println("exception in get bitmap utility");
			}

			bis.close();
			is.close();
			final int IMAGE_MAX_SIZE = 50;
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) > IMAGE_MAX_SIZE) {
				scale++;
			}
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				// b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = imageBitmap.getHeight();
				int width = imageBitmap.getWidth();

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap,
						(int) x, (int) y, true);
				imageBitmap.recycle();
				imageBitmap = scaledBitmap;

				System.gc();
			} else {
				// b = BitmapFactory.decodeStream(in);
			}

		} catch (OutOfMemoryError error) {
			error.printStackTrace();
			System.out.println("exception in get bitma putility");
		} catch (Exception e) {
			System.out.println("exception in get bitma putility");
			e.printStackTrace();
		}
		return imageBitmap;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}
	}

	public static String getAddressFromLtdLng() {
		return null;
	}

	public static String postfile(HashMap<String, String> map, String url)
	{
		
		String result = "";
		
		
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			//httpPost.setEntity(new UrlEncodedFormEntity(params));
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int status = httpResponse.getStatusLine().getStatusCode();

			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");

			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	
	public static String postParamsAndfindJSON(String url,
			ArrayList<NameValuePair> listParams) {
		String result = "";
		try {
			int TIMEOUT_MILLISEC = 100000; // = 10 seconds
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost();
			httpPost.setURI(new URI(url));
			httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
			httpPost.setHeader("Expect", "100-continue");
			httpPost.setEntity(new UrlEncodedFormEntity(listParams,HTTP.UTF_8));
			// httpGet.setURI(new URI(url));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			//int status = httpResponse.getStatusLine().getStatusCode();

			InputStream is = httpResponse.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();

		} catch (Exception e) {
			System.out.println("exception in jsonparser class ........");
			e.printStackTrace();
			return null;
		}
		return result;
	}

	
	public static void setUserIdPrefernce(Context context, String key,
			String value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		// Commit the edits!
		editor.commit();
	}

	
	public static String  getUserIdPrefernce(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_LOGIN_NAME, 0);
		// / return settings.putString(name, null);
		return settings.getString(key, null);
	}
	
	static Uri pngUri;

	public static Uri saveToSd(Bitmap bm, String name) {

		if (bm != null) {

			String sdPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();

			File dest = new File(sdPath, name);

			try {
				FileOutputStream out = new FileOutputStream(dest);
				bm.compress(Bitmap.CompressFormat.PNG, 90, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			pngUri = Uri.fromFile(dest);

			return pngUri;
		} else {

		}
		return null;

	}

	public static byte[] scaleImage(Context context, Uri photoUri)
			throws IOException {
		InputStream is = context.getContentResolver().openInputStream(photoUri);
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, dbo);
		is.close();

		int rotatedWidth, rotatedHeight;
		int orientation = 0;// getOrientation(context, photoUri);

		if (orientation == 90 || orientation == 270) {
			rotatedWidth = dbo.outHeight;
			rotatedHeight = dbo.outWidth;
		} else {
			rotatedWidth = dbo.outWidth;
			rotatedHeight = dbo.outHeight;
		}

		Bitmap srcBitmap;
		is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeStream(is, null, options);
		} else {
			srcBitmap = BitmapFactory.decodeStream(is);
		}
		is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		String type = context.getContentResolver().getType(photoUri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		/*
		 * if (type.equals("image/png")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
		 * (type.equals("image/jpg") || type.equals("image/jpeg")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
		 */
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		return bMapArray;
	}

	public static byte[] resizeImage(Context context, Uri photoUri) {
		byte[] data = null;
		try {
			ContentResolver cr = context.getContentResolver();
			InputStream inputStream = cr.openInputStream(photoUri);
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			data = baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return data;

	}

	public static int getOrientation(Context context, Uri photoUri) {
		/* it's on the external media. */
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { MediaStore.Images.ImageColumns.ORIENTATION },
				null, null, null);

		if (cursor.getCount() != 1) {
			return -1;
		}

		cursor.moveToFirst();
		return cursor.getInt(0);
	}

	

	public static byte[] scaleImage(Context context, int resId)
			throws IOException {
		// InputStream is =
		// context.getContentResolver().openInputStream(photoUri);
		// resId = com.travelkashmir.travelsearch.R.drawable.arrow_icon_red;
		BitmapFactory.Options dbo = new BitmapFactory.Options();
		dbo.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), resId, dbo);
		// BitmapFactory.decodeStream(is, null, dbo);
		// is.close();

		int rotatedWidth, rotatedHeight;
		// int orientation = getOrientation(context, photoUri);

		// if (orientation == 90 || orientation == 270) {
		// rotatedWidth = dbo.outHeight;
		// rotatedHeight = dbo.outWidth;
		// } else {
		// rotatedWidth = dbo.outWidth;
		// rotatedHeight = dbo.outHeight;
		// }
		rotatedHeight = dbo.outHeight;
		rotatedWidth = dbo.outWidth;
		Bitmap srcBitmap;
		// is = context.getContentResolver().openInputStream(photoUri);
		if (rotatedWidth > MAX_IMAGE_DIMENSION
				|| rotatedHeight > MAX_IMAGE_DIMENSION) {
			float widthRatio = ((float) rotatedWidth)
					/ ((float) MAX_IMAGE_DIMENSION);
			float heightRatio = ((float) rotatedHeight)
					/ ((float) MAX_IMAGE_DIMENSION);
			float maxRatio = Math.max(widthRatio, heightRatio);

			// Create the bitmap from file
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = (int) maxRatio;
			srcBitmap = BitmapFactory.decodeResource(context.getResources(),
					resId, dbo);
		} else {
			srcBitmap = BitmapFactory.decodeResource(context.getResources(),
					resId, dbo);
		}
		if (srcBitmap != null) {
			Log.i("Utility", "image");
		}
		// is.close();

		/*
		 * if the orientation is not 0 (or -1, which means we don't know), we
		 * have to do a rotation.
		 */
		int orientation = 1;
		if (orientation > 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);

			srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
					srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
		}

		// String type = context.getContentResolver().getType(photoUri);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		// srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		/*
		 * if (type.equals("image/png")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); } else if
		 * (type.equals("image/jpg") || type.equals("image/jpeg")) {
		 * srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); }
		 */
		byte[] bMapArray = baos.toByteArray();
		baos.close();
		return bMapArray;
	}

	public static HashMap<String, String> GetHashMapFromString(String strHmp)
	{
		strHmp = strHmp.replaceAll("{", "");
		strHmp = strHmp.replaceAll("}", "");
		HashMap<String, String> hm = new HashMap<String, String>();
		String [] arr1 = strHmp.split(",");
		for (int i = 0; i < arr1.length; i++) 
		{
			String hmKeys = arr1[i];
			System.out.println(hmKeys);
			String [] arr2 = hmKeys.split("=");
			System.out.println(arr2);
			if (arr2.length==1) 
			{
				hm.put(arr2[0],"");
			}else
			{
				hm.put(arr2[0], arr2[1]);
			}
		}
		return hm;
	}
	
	public static Date convertdate(String stringDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date convertDate;
		try {
			convertDate = sdf.parse(stringDate);
			return convertDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public static JSONObject getLocationInfo(String address) {
		StringBuilder stringBuilder = new StringBuilder();
		try {

			address = address.replaceAll(" ", "%20");

			HttpPost httppost = new HttpPost(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ address + "&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject;
	}
	
	public static Drawable getImageFromURL(String photoDomain) {
 		Drawable drawable = null;
 		try {
 			DefaultHttpClient httpClient = new DefaultHttpClient();
 			HttpGet request = new HttpGet(photoDomain.trim());
 			HttpResponse response = httpClient.execute(request);
 			InputStream is = response.getEntity().getContent();
 			drawable = Drawable.createFromStream(is, "src"); 	
 		} catch (MalformedURLException e) {
 			Log.e("firstcatch","lskdjflsdf");
 		} catch (IOException e) {
 			Log.e("secondcatch","lskdjflsdf");
 		}catch (Exception e) {
 			Log.e("Exception",e.toString()+", "+photoDomain);
 		} 		
 		return drawable;
 	 } 

	// check for Valid email Address
	
} // class ends here
