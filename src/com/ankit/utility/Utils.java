package com.ankit.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {

	public static String serverUrl = "http://me.intlfaces.com/api/v1/api.php?method=";
	private static String PREFS_INTLFACES = "intlfaces";
	public static final String OFFERURL = "http://me.intlfaces.com/api/v1/api.php?method=GetOffers";
	public static final String IMAGEURL  = "http://me.intlfaces.com/";
	public static final int EARNREWARD = 1, TRACKREWARD = 2;
	public static ArrayList<HashMap<String, String>> getOfferAds() {
		return getOffer();
	}

	public static boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}
	// check is email id is valid format someone@website.domain
		public static boolean isValidEmail(String emailAddress) {
			String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(emailAddress);

			return matcher.matches();
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
			}catch(RuntimeException e)
			{
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO: handle exception
			e.printStackTrace();
			}
			
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
	
	
	
	public static Bitmap getBitmap(String url) {

		Bitmap imageBitmap = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			try {
				imageBitmap = BitmapFactory
						.decodeStream(new FlushedInputStream(is));
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

	public static ArrayList<HashMap<String, String>> getOffer() {
		ArrayList<HashMap<String, String>> listOffer = new ArrayList<HashMap<String, String>>();
		String response = getJSONFromUrl(OFFERURL);
		if (response != null) {
			try {
				JSONObject jsonOffers = new JSONObject(response);
				JSONArray jArrayOffer = jsonOffers.getJSONArray("offers");
				for (int i = 0; i < jArrayOffer.length(); i++) {
					JSONObject jsonOffer = jArrayOffer.getJSONObject(i);
					jsonOffer = jsonOffer.getJSONObject("offer");
					HashMap<String, String> mapoffer = toMap(jsonOffer);
					listOffer.add(mapoffer);
				}
			} catch (JSONException e) {
				// TODO: handle exception
			}
		}

		return listOffer;
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
	
	
	// set string prefrences
			public static void setBooleanPrefernce(Context context, String name,
					boolean value) {
				SharedPreferences settings = context.getSharedPreferences(
						PREFS_INTLFACES, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean(name, value);

				// Commit the edits!
				editor.commit();
			}
			
			public static boolean getBooleanPrefernce(Context context, String key) {
				SharedPreferences settings = context.getSharedPreferences(
						PREFS_INTLFACES, 0);
				// / return settings.putString(name, null);
				return settings.getBoolean(key, false);
			}
	// set string prefrences
		public static void setUserPrefernce(Context context, String name,
				String value) {
			SharedPreferences settings = context.getSharedPreferences(
					PREFS_INTLFACES, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putString(name, value);

			// Commit the edits!
			editor.commit();
		}
		
		public static void setIntegerPrefrence(Context context,String name, int value)
		{
			SharedPreferences settings = context.getSharedPreferences(
					PREFS_INTLFACES, 0);
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(name, value);
			// Commit the edits!
						editor.commit();
			
		}
		
		public static int getIntegerPrefrence(Context context, String key)
		{
			SharedPreferences settings = context.getSharedPreferences(
					PREFS_INTLFACES, 0);
			
			return settings.getInt(key, 0);
			
		}
		
		public static String getUserPrefernce(Context context, String key) {
			SharedPreferences settings = context.getSharedPreferences(
					PREFS_INTLFACES, 0);
			// / return settings.putString(name, null);
			return settings.getString(key, "0");
		}
	public static void setPrefernce(Context context, String key,
			long value) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_INTLFACES, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		
		// Commit the edits!
		editor.commit();
	}

	
	public static long getPrefernce(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(
				PREFS_INTLFACES, 0);
		// / return settings.putString(name, null);
		return settings.getLong(key, 0);
	}
}
