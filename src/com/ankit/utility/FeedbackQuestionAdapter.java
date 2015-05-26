package com.ankit.utility;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ankit.touchreview.R;


@SuppressLint("NewApi")
public class FeedbackQuestionAdapter extends ArrayAdapter<JSONObject> {
	ArrayList applist;
	Context mContext;
	int count = 0;
	LayoutInflater inflater;
	
	public FeedbackQuestionAdapter(Context context,int textViewResourceId, ArrayList applist) {
		super(context, textViewResourceId);
		this.applist = applist;
		mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return applist.size();
	}

	public JSONObject getItem(int aPosition) {
		return (JSONObject)applist.get(aPosition);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(final int aPosition, View convertView, ViewGroup parent) {
		View viewHolder = convertView;	
			final JSONObject object = getItem(aPosition);
			if (viewHolder == null) {
				viewHolder = inflater.inflate(R.layout.questtion_layout,null);	
			}
			try {
				RadioButton option1 = (RadioButton)viewHolder.findViewById(R.id.option1);
				RadioButton option2 = (RadioButton)viewHolder.findViewById(R.id.option2);
				RadioButton option3 = (RadioButton)viewHolder.findViewById(R.id.option3);
				RadioButton option4 = (RadioButton)viewHolder.findViewById(R.id.option4);
				RadioButton option5 = (RadioButton)viewHolder.findViewById(R.id.option5);
				option1.setChecked(false);
				option2.setChecked(false);
				option3.setChecked(false);
				option4.setChecked(false);
				option5.setChecked(false);
				((TextView)viewHolder.findViewById(R.id.question)).setText(object.getString("servayquestion"));
				if(object.has("option1")){
					option1.setText(object.getString("option1"));
				}else{
					View v = (View) option1.getParent();
					v.setVisibility(View.GONE);
				}
				if(object.has("option2")){
					option2.setText(object.getString("option2"));
				}else{
					View v = (View) option2.getParent();
					v.setVisibility(View.GONE);
				}
				if(object.has("option3")){
					option3.setText(object.getString("option3"));
				}else{
					View v = (View) option3.getParent();
					v.setVisibility(View.GONE);	
				}
				if(object.has("option4")){
					option4.setText(object.getString("option4"));
				}else{
					View v = (View) option4.getParent();
					v.setVisibility(View.GONE);
				}
				if(object.has("option5")){
					option5.setText(object.getString("option5"));
				}else{
					View v = (View) option5.getParent();
					v.setVisibility(View.GONE);
				}
				option1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						try {
							object.put("selectedOption", "option1");
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				option2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						try {
							object.put("selectedOption", "option2");
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				option3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						try {
							object.put("selectedOption", "option3");
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				option4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						try {
							object.put("selectedOption", "option4");
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				option5.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(isChecked)
						try {
							object.put("selectedOption", "option5");
							notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				if("option1".equals(object.getString("selectedOption"))){
					option1.setChecked(true);
				}else if("option2".equals(object.getString("selectedOption"))){
					option2.setChecked(true);
				}else if("option3".equals(object.getString("selectedOption"))){
					option3.setChecked(true);
				}else if("option4".equals(object.getString("selectedOption"))){
					option4.setChecked(true);
				}else if("option5".equals(object.getString("selectedOption"))){
					option5.setChecked(true);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		return viewHolder;
	}
}